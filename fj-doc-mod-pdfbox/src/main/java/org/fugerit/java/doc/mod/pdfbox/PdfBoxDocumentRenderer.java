package org.fugerit.java.doc.mod.pdfbox;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitWidthDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.helper.SourceResolverHelper;
import org.fugerit.java.doc.base.model.*;
import org.fugerit.java.doc.base.xml.DocModelUtils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

/**
 * Complete renderer with header/footer, bookmarks, background and page number support.
 */
@Slf4j
public class PdfBoxDocumentRenderer {

    private final PDDocument document;
    private final FontManager fontManager;

    // Document structure
    private DocContainer headerContainer;
    private DocContainer footerContainer;
    private final List<PDPage> pages;

    // Current page and rendering state
    private PDPageContentStream contentStream;
    private float currentY;

    // Page layout dimensions
    private float pageWidth = PDRectangle.A4.getWidth();
    private float pageHeight = PDRectangle.A4.getHeight();
    private float marginTop = 20f;
    private float marginBottom = 25f;
    private float marginLeft = 20f;
    private float marginRight = 20f;
    private static final float LINE_HEIGHT = 15f;

    private float headerExtent = 0f;
    private float footerExtent = 0f;

    // Alignment constants matching DocPara / EnumDocAlignH
    public static final int ALIGN_LEFT = DocPara.ALIGN_LEFT;
    public static final int ALIGN_CENTER = DocPara.ALIGN_CENTER;
    public static final int ALIGN_RIGHT = DocPara.ALIGN_RIGHT;
    public static final int ALIGN_JUSTIFY = DocPara.ALIGN_JUSTIFY;
    public static final int ALIGN_JUSTIFYALL = DocPara.ALIGN_JUSTIFY_ALL;

    private int numberedListCounter = 0;
    private final Map<String, PDPageDestination> destinations = new HashMap<>();

    public PdfBoxDocumentRenderer(PDDocument document, PdfBoxConfig config) {
        this.document = document;
        this.fontManager = new FontManager(config, document);
        this.pages = new ArrayList<>();
    }

    private void setupPageGeometry(DocBase docBase) {
        Properties info = docBase.getInfo();
        String marginsProp = info.getProperty("margins");
        if (StringUtils.isNotEmpty(marginsProp)) {
            String[] split = marginsProp.split(";");
            if (split.length >= 4) {
                SafeFunction.applySilent(() -> {
                    this.marginLeft = Float.parseFloat(split[0].trim());
                    this.marginRight = Float.parseFloat(split[1].trim());
                    this.marginTop = Float.parseFloat(split[2].trim());
                    this.marginBottom = Float.parseFloat(split[3].trim());
                });
            }
        }
        String orient = info.getProperty("page-orient");
        if ("horizontal".equalsIgnoreCase(orient)) {
            this.pageWidth = PDRectangle.A4.getHeight();
            this.pageHeight = PDRectangle.A4.getWidth();
        }
    }

    private void computeHeaderFooterExtents() {
        if (headerContainer != null && headerContainer.getElementList() != null && !headerContainer.getElementList().isEmpty()) {
            float baseExtent = 0f;
            if (headerContainer instanceof DocHeaderFooter) {
                int exp = ((DocHeaderFooter) headerContainer).getExpectedSize();
                if (exp > 0) {
                    baseExtent = exp * 72f / 25.4f;
                }
            }
            float estimated = estimateContainerHeight(headerContainer);
            this.headerExtent = Math.max(Math.max(baseExtent, estimated), 25f);
        } else {
            this.headerExtent = 0f;
        }

        if (footerContainer != null && footerContainer.getElementList() != null && !footerContainer.getElementList().isEmpty()) {
            float baseExtent = 0f;
            if (footerContainer instanceof DocHeaderFooter) {
                int exp = ((DocHeaderFooter) footerContainer).getExpectedSize();
                if (exp > 0) {
                    baseExtent = exp * 72f / 25.4f;
                }
            }
            float estimated = estimateContainerHeight(footerContainer);
            this.footerExtent = Math.max(Math.max(baseExtent, estimated), 25f);
        } else {
            this.footerExtent = 0f;
        }
    }

    private float estimateContainerHeight(DocContainer container) {
        if (container == null || container.getElementList() == null) {
            return 0f;
        }
        float h = 0f;
        for (DocElement el : container.getElementList()) {
            if (el instanceof DocPara) {
                h += 14f;
            } else if (el instanceof DocPhrase) {
                h += 12f;
            } else if (el instanceof DocImage) {
                DocImage img = (DocImage) el;
                float scale = img.getScaling() != null ? img.getScaling().floatValue() / 100f : 1.0f;
                h += 60f * scale;
            } else if (el instanceof DocTable) {
                DocTable t = (DocTable) el;
                int rows = t.getElementList() != null ? t.getElementList().size() : 1;
                h += rows * 16f;
            }
        }
        return h + 8f;
    }

    private float getContentTop() {
        return this.pageHeight - this.marginTop - this.headerExtent;
    }

    private float getContentBottom() {
        return this.marginBottom + this.footerExtent;
    }

    public void renderDocument(DocBase docBase) throws IOException {
        log.info("=== Starting document rendering ===");

        setupPageGeometry(docBase);

        // Store header and footer for later rendering
        headerContainer = docBase.getDocHeader();
        footerContainer = docBase.getDocFooter();

        computeHeaderFooterExtents();

        // Create first page
        createNewPage();

        // Process document body
        DocContainer bodyContainer = docBase.getDocBody();
        if (bodyContainer != null) {
            log.info("Rendering body");
            renderContainer(bodyContainer);
        }

        // Close current content stream before adding background, headers/footers
        closeContentStream();

        // Render background under pages if present (now that pages are created)
        renderBackground(docBase);

        // Render headers and footers on all pages with page numbers
        renderHeadersAndFooters();

        // Process bookmarks
        renderBookmarks(docBase);

        log.info("=== Document rendering completed ===");
    }

    private void renderBackground(DocBase docBase) {
        DocBackground bg = docBase.getDocBackground();
        if (bg != null && !bg.getElementList().isEmpty()) {
            DocElement first = bg.getElementList().get(0);
            if (first instanceof DocImage) {
                DocImage bgImage = (DocImage) first;
                SafeFunction.applySilent(() -> {
                    PDImageXObject pdImg = createPDImage(bgImage);
                    if (pdImg != null) {
                        float scale = bgImage.getScaling() != null ? bgImage.getScaling().floatValue() / 100f : 1.0f;
                        float imgWidth = pdImg.getWidth() * scale;
                        float imgHeight = pdImg.getHeight() * scale;
                        for (PDPage page : pages) {
                            try (PDPageContentStream cs = new PDPageContentStream(
                                    document, page, PDPageContentStream.AppendMode.PREPEND, true, true)) {
                                cs.drawImage(pdImg, 0, 0, imgWidth, imgHeight);
                            }
                        }
                    }
                });
            }
        }
    }

    private void renderBookmarks(DocBase docBase) {
        DocBookmarkTree tree = docBase.getDocBookmarkTree();
        if (tree != null && !tree.getElementList().isEmpty()) {
            PDDocumentOutline outline = new PDDocumentOutline();
            document.getDocumentCatalog().setDocumentOutline(outline);
            for (DocElement el : tree.getElementList()) {
                if (el instanceof DocBookmark) {
                    DocBookmark b = (DocBookmark) el;
                    PDOutlineItem item = new PDOutlineItem();
                    item.setTitle(b.getTitle());
                    PDPageDestination dest = destinations.get(b.getRef());
                    if (dest != null) {
                        item.setDestination(dest);
                    }
                    outline.addLast(item);
                }
            }
        }
    }

    private void renderContainer(DocContainer container) throws IOException {
        List<DocElement> elements = container.getElementList();
        if (elements != null) {
            for (DocElement element : elements) {
                processElement(element);
            }
        }
    }

    private void createNewPage() throws IOException {
        closeContentStream();

        PDPage currentPage = new PDPage(new PDRectangle(this.pageWidth, this.pageHeight));
        document.addPage(currentPage);
        pages.add(currentPage);

        currentY = getContentTop() - 10f;

        contentStream = new PDPageContentStream(
                document, currentPage, PDPageContentStream.AppendMode.APPEND, true, true);

        log.info("Created page {}, Y position: {}", pages.size(), currentY);
    }

    private void closeContentStream() throws IOException {
        if (contentStream != null) {
            contentStream.close();
            contentStream = null;
        }
    }

    private void checkNewPage() throws IOException {
        if (currentY < getContentBottom() + LINE_HEIGHT * 2) {
            log.info("Need new page, current Y: {}", currentY);
            createNewPage();
        }
    }

    private void registerDestination(String id) {
        if (StringUtils.isNotEmpty(id) && !pages.isEmpty()) {
            PDPage currentPage = pages.get(pages.size() - 1);
            PDPageFitWidthDestination dest = new PDPageFitWidthDestination();
            dest.setPage(currentPage);
            dest.setTop((int) currentY);
            destinations.putIfAbsent(id, dest);
        }
    }

    private void processElement(DocElement element) throws IOException {
        if (element instanceof DocPara) {
            renderParagraph((DocPara) element);
        } else if (element instanceof DocTable) {
            renderTable((DocTable) element);
        } else if (element instanceof DocList) {
            renderList((DocList) element);
        } else if (element instanceof DocPhrase) {
            renderStandalonePhrase((DocPhrase) element);
        } else if (element instanceof DocImage) {
            renderImage((DocImage) element);
        } else if (element instanceof DocBarcode) {
            renderBarcode((DocBarcode) element);
        } else if (element instanceof DocPageBreak) {
            createNewPage();
        }
    }

    private void renderBarcode(DocBarcode barcode) throws IOException {
        checkNewPage();
        registerDestination(barcode.getId());
        String text = barcode.getText();
        if (StringUtils.isEmpty(text)) {
            return;
        }
        PDFont font = fontManager.getFont(FontManager.STYLE_NORMAL, "courier");
        float fontSize = 9f;
        renderText(String.format("* %s *", text), font, fontSize, ALIGN_CENTER, null, null, LINE_HEIGHT);
        currentY -= LINE_HEIGHT * 0.5f;
    }

    private PDImageXObject createPDImage(DocImage docImage) {
        if (docImage.isSvg()) {
            log.warn("SVG image not natively supported in pdfbox, skipping image (url={}, alt={})", docImage.getUrl(), docImage.getAlt());
            return null;
        }
        if (StringUtils.isEmpty(docImage.getUrl()) && StringUtils.isEmpty(docImage.getBase64())) {
            log.warn("Image has no url or base64 data, skipping image (alt={})", docImage.getAlt());
            return null;
        }
        return SafeFunction.get(() -> {
            byte[] data = SourceResolverHelper.resolveImage(docImage);
            if (data == null || data.length == 0) {
                return null;
            }
            if ("jpg".equalsIgnoreCase(docImage.getType()) || "jpeg".equalsIgnoreCase(docImage.getType())) {
                try {
                    return JPEGFactory.createFromStream(document, new ByteArrayInputStream(data));
                } catch (Exception e) {
                    log.debug("JPEGFactory failed, falling back to LosslessFactory: {}", e.getMessage());
                }
            }
            BufferedImage bImg = ImageIO.read(new ByteArrayInputStream(data));
            if (bImg != null) {
                return LosslessFactory.createFromImage(document, bImg);
            }
            return null;
        });
    }

    private void renderImage(DocImage docImage) throws IOException {
        PDImageXObject pdImg = createPDImage(docImage);
        if (pdImg == null) {
            return;
        }
        checkNewPage();
        registerDestination(docImage.getId());

        float scale = docImage.getScaling() != null ? docImage.getScaling().floatValue() / 100f : 1.0f;
        float imgWidth = pdImg.getWidth() * scale;
        float imgHeight = pdImg.getHeight() * scale;

        float availableWidth = this.pageWidth - this.marginLeft - this.marginRight;
        if (imgWidth > availableWidth) {
            float ratio = availableWidth / imgWidth;
            imgWidth = availableWidth;
            imgHeight = imgHeight * ratio;
        }

        if (currentY - imgHeight < getContentBottom()) {
            createNewPage();
        }

        float xPos = this.marginLeft;
        if (docImage.getAlign() == DocPara.ALIGN_CENTER) {
            xPos = this.marginLeft + (availableWidth - imgWidth) / 2f;
        } else if (docImage.getAlign() == DocPara.ALIGN_RIGHT) {
            xPos = this.pageWidth - this.marginRight - imgWidth;
        }

        contentStream.drawImage(pdImg, xPos, currentY - imgHeight, imgWidth, imgHeight);
        currentY -= imgHeight + LINE_HEIGHT * 0.5f;
    }

    private boolean canEncode(PDFont font, String text) {
        if (font == null || text == null) {
            return false;
        }
        try {
            font.encode(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private PDFont getEncodableFont(int style, String fontName, String text) throws IOException {
        PDFont font = fontManager.getFont(style, fontName);
        if (text != null && !canEncode(font, text)) {
            log.warn("Font {} cannot encode text, falling back to default font", font.getName());
            font = fontManager.getFont(style, null);
        }
        return font;
    }

    private String sanitizeTextForFont(PDFont font, String text) {
        if (text == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            try {
                font.encode(String.valueOf(c));
                sb.append(c);
            } catch (Exception e) {
                sb.append("?");
            }
        }
        return sb.toString();
    }

    private void renderStandalonePhrase(DocPhrase phrase) throws IOException {
        String text = extractText(phrase);
        if (StringUtils.isEmpty(text)) {
            return;
        }
        checkNewPage();
        registerDestination(phrase.getId());

        PDFont font = getEncodableFont(phrase.getStyle(), phrase.getFontName(), text);
        float fontSize = fontManager.getFontSize(phrase.getStyle(), phrase.getSize());
        float lineHeight = Math.max(LINE_HEIGHT, fontSize * 1.25f);
        if (phrase.getLeading() != null) {
            if (phrase.getLeading().floatValue() <= 4f) {
                lineHeight = fontSize * phrase.getLeading().floatValue();
            } else {
                lineHeight = phrase.getLeading().floatValue();
            }
        }
        renderText(text, font, fontSize, ALIGN_LEFT, phrase.getForeColor(), phrase.getBackColor(), lineHeight);
        currentY -= lineHeight * 0.25f;
    }

    private void renderParagraph(DocPara para) throws IOException {
        String text = extractText(para);
        if (text == null || text.trim().isEmpty()) {
            return;
        }

        if (para.getSpaceBefore() != null) {
            currentY -= para.getSpaceBefore().floatValue();
        } else if (para.getHeadLevel() == 1) {
            currentY -= 14f;
        } else if (para.getHeadLevel() == 2) {
            currentY -= 10f;
        } else if (para.getHeadLevel() == 3) {
            currentY -= 8f;
        } else if (para.getHeadLevel() > 3) {
            currentY -= 6f;
        }

        checkNewPage();
        registerDestination(para.getId());

        int styleCode = para.getStyle();
        PDFont font = getEncodableFont(styleCode, para.getFontName(), text);
        float fontSize;
        if (para.getSize() > 0) {
            fontSize = fontManager.getFontSize(styleCode, para.getSize());
        } else if (para.getHeadLevel() > 0) {
            switch (para.getHeadLevel()) {
                case 1: fontSize = 18f; break;
                case 2: fontSize = 14f; break;
                case 3: fontSize = 12f; break;
                case 4: fontSize = 11f; break;
                default: fontSize = 10f; break;
            }
        } else {
            fontSize = fontManager.getFontSize(styleCode, para.getSize());
        }

        float lineHeight = Math.max(LINE_HEIGHT, fontSize * 1.25f);
        if (para.getLeading() != null) {
            if (para.getLeading().floatValue() <= 4f) {
                lineHeight = fontSize * para.getLeading().floatValue();
            } else {
                lineHeight = para.getLeading().floatValue();
            }
        }

        int align = para.getAlign();
        renderText(text, font, fontSize, align, para.getForeColor(), para.getBackColor(), lineHeight);

        if (para.getSpaceAfter() != null) {
            currentY -= para.getSpaceAfter().floatValue();
        } else if (para.getHeadLevel() == 1) {
            currentY -= 8f;
        } else if (para.getHeadLevel() == 2) {
            currentY -= 6f;
        } else if (para.getHeadLevel() > 2) {
            currentY -= 4f;
        } else {
            currentY -= lineHeight * 0.25f;
        }
    }

    private void renderText(String text, PDFont font, float fontSize, int align, String foreColor, String backColor, float lineHeight) throws IOException {
        if (text == null || text.trim().isEmpty()) {
            return;
        }

        if (!canEncode(font, text)) {
            font = getEncodableFont(FontManager.STYLE_NORMAL, null, text);
        }

        float availableWidth = this.pageWidth - this.marginLeft - this.marginRight;
        List<String> lines = wrapText(text, font, fontSize, availableWidth);

        for (String line : lines) {
            checkNewPage();

            float textWidth = calculateTextWidth(line, font, fontSize);
            float xPos = calculateXPosition(line, font, fontSize, align);

            if (StringUtils.isNotEmpty(backColor)) {
                SafeFunction.applySilent(() -> {
                    Color bg = DocModelUtils.parseHtmlColor(backColor);
                    contentStream.setNonStrokingColor(bg);
                    contentStream.addRect(xPos, currentY - 2, textWidth, fontSize + 2);
                    contentStream.fill();
                });
            }

            if (StringUtils.isNotEmpty(foreColor)) {
                SafeFunction.applySilent(() -> {
                    Color fg = DocModelUtils.parseHtmlColor(foreColor);
                    contentStream.setNonStrokingColor(fg);
                });
            } else {
                contentStream.setNonStrokingColor(Color.BLACK);
            }

            if (!canEncode(font, line)) {
                line = sanitizeTextForFont(font, line);
            }

            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(xPos, currentY);
            contentStream.showText(line);
            contentStream.endText();
            currentY -= lineHeight;
        }
    }

    private void renderText(String text, PDFont font, float fontSize, int align) throws IOException {
        renderText(text, font, fontSize, align, null, null, Math.max(LINE_HEIGHT, fontSize * 1.25f));
    }

    private float calculateXPosition(String text, PDFont font, float fontSize, int align) throws IOException {
        if (align == ALIGN_CENTER) {
            float textWidth = calculateTextWidth(text, font, fontSize);
            float availableWidth = this.pageWidth - this.marginLeft - this.marginRight;
            return this.marginLeft + (availableWidth - textWidth) / 2;
        } else if (align == ALIGN_RIGHT) {
            float textWidth = calculateTextWidth(text, font, fontSize);
            return this.pageWidth - this.marginRight - textWidth;
        }
        return this.marginLeft;
    }

    private void renderList(DocList list) throws IOException {
        checkNewPage();
        registerDestination(list.getId());

        if (!(list instanceof DocContainer)) {
            return;
        }

        List<DocElement> items = list.getElementList();
        if (items == null) {
            return;
        }

        numberedListCounter = 1;
        String listType = list.getListType();
        String clt = list.getClt();

        for (DocElement item : items) {
            if (item instanceof DocLi) {
                renderListItem((DocLi) item, listType, clt);
            }
        }

        currentY -= LINE_HEIGHT * 0.5f;
    }

    private String getBulletSymbol(String listType, String clt) {
        if (DocList.LIST_TYPE_OL.equalsIgnoreCase(listType) || DocList.LIST_TYPE_OL.equalsIgnoreCase(clt)) {
            return (numberedListCounter++) + ".";
        } else if (DocList.LIST_TYPE_OLL.equalsIgnoreCase(listType) || DocList.LIST_TYPE_OLL.equalsIgnoreCase(clt)) {
            char letter = (char) ('a' + (numberedListCounter++ - 1));
            return letter + ".";
        } else if (DocList.LIST_TYPE_ULM.equalsIgnoreCase(listType) || DocList.LIST_TYPE_ULM.equalsIgnoreCase(clt)) {
            return "-";
        } else {
            return "•";
        }
    }

    private void renderListItem(DocLi listItem, String listType, String clt) throws IOException {
        checkNewPage();
        registerDestination(listItem.getId());

        String bullet = getBulletSymbol(listType, clt);
        PDFont font = getEncodableFont(0, null, bullet);
        float fontSize = fontManager.getFontSize(0);

        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(this.marginLeft, currentY);
        if (!canEncode(font, bullet)) {
            bullet = sanitizeTextForFont(font, bullet);
        }
        contentStream.showText(bullet);
        contentStream.endText();

        String text = extractText(listItem);

        if (text != null && !text.isEmpty()) {
            PDFont itemFont = getEncodableFont(0, null, text);
            float indent = this.marginLeft + 25f;
            List<String> lines = wrapText(text, itemFont, fontSize, this.pageWidth - indent - this.marginRight);

            for (int i = 0; i < lines.size(); i++) {
                if (i > 0) {
                    currentY -= LINE_HEIGHT;
                    checkNewPage();
                }

                String l = lines.get(i);
                if (!canEncode(itemFont, l)) {
                    l = sanitizeTextForFont(itemFont, l);
                }

                contentStream.beginText();
                contentStream.setFont(itemFont, fontSize);
                contentStream.newLineAtOffset(indent, currentY);
                contentStream.showText(l);
                contentStream.endText();
            }
        }

        currentY -= LINE_HEIGHT;
    }

    private void renderTable(DocTable table) throws IOException {
        if (table.getSpaceBefore() != null) {
            currentY -= table.getSpaceBefore().floatValue();
        }

        checkNewPage();
        registerDestination(table.getId());

        if (!(table instanceof DocContainer)) {
            return;
        }

        List<DocElement> rows = table.getElementList();
        if (rows == null || rows.isEmpty()) {
            return;
        }

        int columnCount = table.getColumns() > 0 ? table.getColumns() : getMaxColumnCount(rows);
        float tableWidth = this.pageWidth - this.marginLeft - this.marginRight;
        float[] cellWidths = calculateColWidths(table, columnCount, tableWidth);

        boolean isFirstRow = true;
        for (DocElement rowElement : rows) {
            if (rowElement instanceof DocRow) {
                renderRow((DocRow) rowElement, cellWidths, isFirstRow, table);
                isFirstRow = false;
            }
        }

        if (table.getSpaceAfter() != null) {
            currentY -= table.getSpaceAfter().floatValue();
        } else {
            currentY -= LINE_HEIGHT * 0.5f;
        }
    }

    private float[] calculateColWidths(DocTable table, int columnCount, float tableWidth) {
        float[] widths = new float[columnCount];
        int[] cw = table.getColWithds();
        if (cw != null && cw.length == columnCount) {
            float totalRatio = 0f;
            for (int c : cw) {
                totalRatio += c;
            }
            if (totalRatio > 0) {
                for (int i = 0; i < columnCount; i++) {
                    widths[i] = (tableWidth * cw[i]) / totalRatio;
                }
                return widths;
            }
        }
        float def = tableWidth / columnCount;
        Arrays.fill(widths, def);
        return widths;
    }

    private int getMaxColumnCount(List<DocElement> rows) {
        int maxCols = 0;
        for (DocElement rowElement : rows) {
            if (rowElement instanceof DocRow && rowElement instanceof DocContainer) {
                DocContainer rowContainer = (DocContainer) rowElement;
                List<DocElement> cells = rowContainer.getElementList();
                if (cells != null) {
                    maxCols = Math.max(maxCols, cells.size());
                }
            }
        }
        return maxCols > 0 ? maxCols : 1;
    }

    private void renderRow(DocRow row, float[] cellWidths, boolean isHeader, DocTable parentTable) throws IOException {
        registerDestination(row.getId());

        if (!(row instanceof DocContainer)) {
            return;
        }

        List<DocElement> cells = row.getElementList();
        if (cells == null || cells.isEmpty()) {
            return;
        }

        int colIdx = 0;
        float[] cWidths = new float[cells.size()];
        List<List<String>> allCellLines = new ArrayList<>();
        List<PDImageXObject> cellImages = new ArrayList<>();
        List<Float> cellImageHeights = new ArrayList<>();
        List<PDFont> cellFonts = new ArrayList<>();
        List<Float> cellFontSizes = new ArrayList<>();

        float maxCellHeight = LINE_HEIGHT;

        for (int i = 0; i < cells.size(); i++) {
            DocElement cellElement = cells.get(i);
            int colspan = 1;
            if (cellElement instanceof DocCell) {
                colspan = Math.max(1, ((DocCell) cellElement).getColumnSpan());
            }
            float cWidth = 0;
            for (int s = 0; s < colspan && (colIdx + s) < cellWidths.length; s++) {
                cWidth += cellWidths[colIdx + s];
            }
            if (cWidth <= 0) {
                cWidth = cellWidths[0] * colspan;
            }
            cWidths[i] = cWidth;
            colIdx += colspan;

            if (cellElement instanceof DocCell) {
                DocCell cell = (DocCell) cellElement;
                registerDestination(cell.getId());

                DocImage docImg = findFirstImage(cell);
                PDImageXObject pdImg = null;
                float imgH = 0f;
                if (docImg != null) {
                    pdImg = createPDImage(docImg);
                    if (pdImg != null) {
                        float scale = docImg.getScaling() != null ? docImg.getScaling().floatValue() / 100f : 1.0f;
                        float w = pdImg.getWidth() * scale;
                        imgH = pdImg.getHeight() * scale;
                        if (w > cWidth - 6) {
                            float r = (cWidth - 6) / w;
                            imgH = imgH * r;
                        }
                    }
                }
                cellImages.add(pdImg);
                cellImageHeights.add(imgH);

                String cellText = extractText(cell);
                boolean bold = isHeader || cell.isHeader() || row.isHeader();
                PDFont font = getEncodableFont(bold ? FontManager.STYLE_BOLD : FontManager.STYLE_NORMAL, null, cellText);
                float fontSize = fontManager.getFontSize(0);
                cellFonts.add(font);
                cellFontSizes.add(fontSize);

                List<String> lines = new ArrayList<>();
                if (cellText != null && !cellText.isEmpty()) {
                    lines = wrapText(cellText, font, fontSize, cWidth - 6);
                }
                allCellLines.add(lines);

                float cellH = 0f;
                if (pdImg != null) {
                    cellH += imgH + 4f;
                }
                if (!lines.isEmpty()) {
                    cellH += lines.size() * LINE_HEIGHT + 4f;
                } else if (pdImg == null) {
                    cellH = LINE_HEIGHT + 4f;
                }
                maxCellHeight = Math.max(maxCellHeight, cellH);
            } else {
                allCellLines.add(Collections.emptyList());
                cellImages.add(null);
                cellImageHeights.add(0f);
                cellFonts.add(fontManager.getFont(0, null));
                cellFontSizes.add(10f);
            }
        }

        float rowHeight = maxCellHeight;

        if (currentY - rowHeight < getContentBottom()) {
            createNewPage();
        }

        float cellX = this.marginLeft;
        float rowStartY = currentY;

        // Pass 2: draw cell backgrounds
        for (int i = 0; i < cells.size(); i++) {
            DocElement cellElement = cells.get(i);
            float cWidth = cWidths[i];
            if (cellElement instanceof DocCell) {
                DocCell cell = (DocCell) cellElement;
                String backColor = StringUtils.isNotEmpty(cell.getBackColor())
                        ? cell.getBackColor()
                        : (parentTable != null ? parentTable.getBackColor() : null);

                if (StringUtils.isNotEmpty(backColor)) {
                    final float finalCellX = cellX;
                    final float finalCWidth = cWidth;
                    final float finalRowStartY = rowStartY;
                    final float finalRowHeight = rowHeight;
                    SafeFunction.applySilent(() -> {
                        Color bg = DocModelUtils.parseHtmlColor(backColor);
                        contentStream.setNonStrokingColor(bg);
                        contentStream.addRect(finalCellX, finalRowStartY - finalRowHeight + 2, finalCWidth, finalRowHeight);
                        contentStream.fill();
                    });
                }
            }
            cellX += cWidth;
        }

        // Pass 3: draw cell contents
        cellX = this.marginLeft;
        for (int i = 0; i < cells.size(); i++) {
            DocElement cellElement = cells.get(i);
            float cWidth = cWidths[i];
            if (cellElement instanceof DocCell) {
                DocCell cell = (DocCell) cellElement;
                PDImageXObject pdImg = cellImages.get(i);
                float imgH = cellImageHeights.get(i);
                List<String> lines = allCellLines.get(i);
                PDFont font = cellFonts.get(i);
                float fontSize = cellFontSizes.get(i);

                float currCellY = rowStartY - 2;

                if (pdImg != null) {
                    float drawW = pdImg.getWidth();
                    float drawH = imgH;
                    if (drawW > cWidth - 6) {
                        drawW = cWidth - 6;
                    }
                    contentStream.drawImage(pdImg, cellX + 3, currCellY - drawH, drawW, drawH);
                    currCellY -= drawH + 2f;
                }

                if (lines != null && !lines.isEmpty()) {
                    contentStream.setNonStrokingColor(Color.BLACK);
                    for (String line : lines) {
                        if (!canEncode(font, line)) {
                            line = sanitizeTextForFont(font, line);
                        }
                        float textWidth = calculateTextWidth(line, font, fontSize);
                        float lineX = cellX + 3;
                        if (cell.getAlign() == DocPara.ALIGN_CENTER) {
                            lineX = cellX + (cWidth - textWidth) / 2;
                        } else if (cell.getAlign() == DocPara.ALIGN_RIGHT) {
                            lineX = cellX + cWidth - textWidth - 3;
                        }
                        contentStream.beginText();
                        contentStream.setFont(font, fontSize);
                        contentStream.newLineAtOffset(lineX, currCellY - fontSize);
                        contentStream.showText(line);
                        contentStream.endText();
                        currCellY -= LINE_HEIGHT;
                    }
                }
            }
            cellX += cWidth;
        }

        // Draw bottom row line
        contentStream.setStrokingColor(Color.LIGHT_GRAY);
        contentStream.setLineWidth(0.5f);
        contentStream.moveTo(this.marginLeft, rowStartY - rowHeight + 2);
        contentStream.lineTo(this.pageWidth - this.marginRight, rowStartY - rowHeight + 2);
        contentStream.stroke();

        currentY -= (rowHeight + 2f);
    }

    private DocImage findFirstImage(DocContainer container) {
        if (container == null || container.getElementList() == null) {
            return null;
        }
        for (DocElement el : container.getElementList()) {
            if (el instanceof DocImage) {
                return (DocImage) el;
            }
        }
        return null;
    }

    private void renderHeadersAndFooters() throws IOException {
        int totalPages = pages.size();

        for (int i = 0; i < pages.size(); i++) {
            PDPage page = pages.get(i);
            int currentPage = i + 1;

            try (PDPageContentStream stream = new PDPageContentStream(
                    document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {

                // Render header if present
                if (headerContainer != null) {
                    renderHeaderFooterContent(stream, headerContainer, true, currentPage, totalPages);
                }

                // Render footer if present
                if (footerContainer != null) {
                    renderHeaderFooterContent(stream, footerContainer, false, currentPage, totalPages);
                }
            }
        }
    }

    private void renderHeaderFooterContent(PDPageContentStream stream, DocContainer container,
                                           boolean isHeader, int currentPage, int totalPages) throws IOException {
        List<DocElement> elements = container.getElementList();
        if (elements == null) {
            return;
        }

        // Draw border if requested
        if (container instanceof DocHeaderFooter) {
            int borderWidth = ((DocHeaderFooter) container).getBorderWidth();
            if (borderWidth > 0) {
                stream.setStrokingColor(Color.LIGHT_GRAY);
                stream.setLineWidth(borderWidth);
                float lineY = isHeader
                        ? (this.pageHeight - this.marginTop - this.headerExtent)
                        : (this.marginBottom + this.footerExtent);
                stream.moveTo(this.marginLeft, lineY);
                stream.lineTo(this.pageWidth - this.marginRight, lineY);
                stream.stroke();
            }
        }

        float yPosition = isHeader
                ? (this.pageHeight - this.marginTop - 10f)
                : (this.marginBottom + this.footerExtent - 12f);
        PDFont font = fontManager.getFont(0, null);
        float fontSize = 9f;

        for (DocElement element : elements) {
            if (element instanceof DocPara) {
                DocPara para = (DocPara) element;
                String text = extractText(para);

                if (text != null && !text.isEmpty()) {
                    text = replacePlaceholders(text, currentPage, totalPages);

                    int align = para.getAlign();
                    float xPos = calculateXPosition(text, font, fontSize, align);

                    if (!canEncode(font, text)) {
                        text = sanitizeTextForFont(font, text);
                    }

                    stream.setNonStrokingColor(Color.BLACK);
                    stream.beginText();
                    stream.setFont(font, fontSize);
                    stream.newLineAtOffset(xPos, yPosition);
                    stream.showText(text);
                    stream.endText();

                    yPosition -= 12f;
                }
            } else if (element instanceof DocPhrase) {
                DocPhrase phrase = (DocPhrase) element;
                String text = extractText(phrase);
                if (text != null && !text.isEmpty()) {
                    text = replacePlaceholders(text, currentPage, totalPages);
                    float xPos = this.marginLeft;
                    if (!canEncode(font, text)) {
                        text = sanitizeTextForFont(font, text);
                    }
                    stream.setNonStrokingColor(Color.BLACK);
                    stream.beginText();
                    stream.setFont(font, fontSize);
                    stream.newLineAtOffset(xPos, yPosition);
                    stream.showText(text);
                    stream.endText();
                    yPosition -= 12f;
                }
            } else if (element instanceof DocImage) {
                DocImage docImg = (DocImage) element;
                PDImageXObject pdImg = createPDImage(docImg);
                if (pdImg != null) {
                    float scale = docImg.getScaling() != null ? docImg.getScaling().floatValue() / 100f : 1.0f;
                    float imgWidth = pdImg.getWidth() * scale;
                    float imgHeight = pdImg.getHeight() * scale;
                    float availableWidth = this.pageWidth - this.marginLeft - this.marginRight;
                    if (imgWidth > availableWidth) {
                        float ratio = availableWidth / imgWidth;
                        imgWidth = availableWidth;
                        imgHeight = imgHeight * ratio;
                    }
                    float xPos = this.marginLeft;
                    if (docImg.getAlign() == DocPara.ALIGN_CENTER) {
                        xPos = this.marginLeft + (availableWidth - imgWidth) / 2f;
                    } else if (docImg.getAlign() == DocPara.ALIGN_RIGHT) {
                        xPos = this.pageWidth - this.marginRight - imgWidth;
                    }
                    stream.drawImage(pdImg, xPos, yPosition - imgHeight, imgWidth, imgHeight);
                    yPosition -= imgHeight + 4f;
                }
            } else if (element instanceof DocTable) {
                DocTable table = (DocTable) element;
                float tableWidth = this.pageWidth - this.marginLeft - this.marginRight;
                int colCount = table.getColumns() > 0 ? table.getColumns() : 2;
                float[] colWidths = calculateColWidths(table, colCount, tableWidth);
                if (table.getElementList() != null) {
                    for (DocElement rowElem : table.getElementList()) {
                        if (rowElem instanceof DocRow) {
                            DocRow row = (DocRow) rowElem;
                            float cellX = this.marginLeft;
                            if (row.getElementList() != null) {
                                for (int c = 0; c < row.getElementList().size(); c++) {
                                    DocElement cElem = row.getElementList().get(c);
                                    float cWidth = (c < colWidths.length) ? colWidths[c] : (tableWidth / row.getElementList().size());
                                    if (cElem instanceof DocCell) {
                                        DocCell cell = (DocCell) cElem;
                                        String text = extractText(cell);
                                        if (text != null && !text.isEmpty()) {
                                            text = replacePlaceholders(text, currentPage, totalPages);
                                            int align = cell.getAlign();
                                            float textWidth = calculateTextWidth(text, font, fontSize);
                                            float xPos = cellX;
                                            if (align == DocPara.ALIGN_CENTER) {
                                                xPos = cellX + (cWidth - textWidth) / 2;
                                            } else if (align == DocPara.ALIGN_RIGHT) {
                                                xPos = cellX + cWidth - textWidth;
                                            }
                                            if (!canEncode(font, text)) {
                                                text = sanitizeTextForFont(font, text);
                                            }
                                            stream.setNonStrokingColor(Color.BLACK);
                                            stream.beginText();
                                            stream.setFont(font, fontSize);
                                            stream.newLineAtOffset(xPos, yPosition);
                                            stream.showText(text);
                                            stream.endText();
                                        }
                                    }
                                    cellX += cWidth;
                                }
                            }
                            yPosition -= 12f;
                        }
                    }
                }
            }
        }
    }

    private String replacePlaceholders(final String text, int currentPage, int totalPages) {
        if (text == null) {
            return text;
        }

        String curStr = String.valueOf(currentPage);
        String totStr = String.valueOf(totalPages);

        return text
                .replace("${r\"${currentPage}\"}", curStr)
                .replace("${r\"${pageCount}\"}", totStr)
                .replace("${currentPage}", curStr)
                .replace("${pageCount}", totStr)
                .replace("${totalPage}", totStr);
    }

    private String extractText(DocElement element) {
        if (element instanceof DocPara) {
            DocPara para = (DocPara) element;
            String paraText = para.getText();
            if (para.getElementList() != null && !para.getElementList().isEmpty()) {
                StringBuilder sb = new StringBuilder();
                if (paraText != null && !paraText.isEmpty()) {
                    sb.append(paraText);
                }
                for (DocElement child : para.getElementList()) {
                    appendChildText(sb, child);
                }
                return sb.toString().trim();
            }
            if (paraText != null && !paraText.isEmpty()) {
                return paraText;
            }
        }
        if (element instanceof DocContainer) {
            return extractTextFromContainer((DocContainer) element);
        }
        if (element instanceof DocPhrase) {
            return nullSafeText(((DocPhrase) element).getText());
        }
        if (element instanceof DocBarcode) {
            return "*" + ((DocBarcode) element).getText() + "*";
        }
        return "";
    }

    private String extractTextFromContainer(DocContainer container) {
        List<DocElement> children = container.getElementList();
        if (children == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (DocElement child : children) {
            appendChildText(result, child);
        }
        return result.toString().trim();
    }

    private void appendChildText(StringBuilder result, DocElement child) {
        String text;
        if (child instanceof DocPhrase) {
            text = nullSafeText(((DocPhrase) child).getText());
        } else if (child instanceof DocBarcode) {
            text = "*" + ((DocBarcode) child).getText() + "*";
        } else {
            text = extractText(child);
        }
        appendIfNotEmpty(result, text);
    }

    private void appendIfNotEmpty(StringBuilder result, String text) {
        if (text != null && !text.isEmpty()) {
            if (result.length() > 0) {
                result.append(" ");
            }
            result.append(text);
        }
    }

    private String nullSafeText(String text) {
        return text != null ? text : "";
    }

    private List<String> wrapText(String text, PDFont font, float fontSize, float maxWidth)
            throws IOException {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }

        String[] words = text.split("\\s+");
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }

            if (shouldStartNewLine(currentLine, word, font, fontSize, maxWidth)) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                appendWord(currentLine, word);
            }
        }

        addRemainingLine(lines, currentLine, text);
        return lines;
    }

    private boolean shouldStartNewLine(StringBuilder currentLine, String word,
                                       PDFont font, float fontSize, float maxWidth)
            throws IOException {
        if (currentLine.length() == 0) {
            return false;
        }

        String testLine = currentLine + " " + word;
        float width = calculateTextWidth(testLine, font, fontSize);

        return width > maxWidth;
    }

    private void appendWord(StringBuilder line, String word) {
        if (line.length() > 0) {
            line.append(" ");
        }
        line.append(word);
    }

    private void addRemainingLine(List<String> lines, StringBuilder currentLine, String originalText) {
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        if (lines.isEmpty()) {
            lines.add(originalText);
        }
    }

    private float calculateTextWidth(String text, PDFont font, float fontSize) throws IOException {
        try {
            return font.getStringWidth(text) / 1000 * fontSize;
        } catch (Exception e) {
            log.warn("Cannot calculate text width for font {}, falling back to sanitized text: {}", font.getName(), e.getMessage());
            String sanitized = sanitizeTextForFont(font, text);
            try {
                return font.getStringWidth(sanitized) / 1000 * fontSize;
            } catch (Exception e2) {
                return text.length() * fontSize * 0.5f;
            }
        }
    }
}
