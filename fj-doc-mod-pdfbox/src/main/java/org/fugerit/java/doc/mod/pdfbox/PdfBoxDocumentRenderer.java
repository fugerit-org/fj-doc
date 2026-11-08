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
import org.fugerit.java.core.lang.helpers.BooleanUtils;
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

    public static final String INFO_KEY_PDFBOX_DEFAULT_TABLE_BEHAVIOUR = "pdfbox-default-table-behaviour";

    private boolean pdfboxDefaultTableBehaviour = false;

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
        this.pdfboxDefaultTableBehaviour = false;
        if (info != null) {
            this.pdfboxDefaultTableBehaviour = BooleanUtils.isTrue(info.getProperty(INFO_KEY_PDFBOX_DEFAULT_TABLE_BEHAVIOUR, "false"));
        }
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
        this.headerExtent = computeContainerExtent(headerContainer);
        this.footerExtent = computeContainerExtent(footerContainer);
    }

    private float computeContainerExtent(DocContainer container) {
        if (container != null && container.getElementList() != null && !container.getElementList().isEmpty()) {
            float baseExtent = 0f;
            if (container instanceof DocHeaderFooter) {
                int exp = ((DocHeaderFooter) container).getExpectedSize();
                if (exp > 0) {
                    baseExtent = (float) exp * 72f / 25.4f;
                }
            }
            float estimated = estimateContainerHeight(container);
            return Math.max(Math.max(baseExtent, estimated), 25f);
        }
        return 0f;
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
                SafeFunction.applySilent(() -> renderBackgroundImage(bgImage));
            }
        }
    }

    private void renderBackgroundImage(DocImage bgImage) throws IOException {
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
            font.encode(text.replace("\r", "").replace("\n", ""));
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

    private float calculateLineHeight(Number leading, float fontSize) {
        float lineHeight = Math.max(LINE_HEIGHT, fontSize * 1.25f);
        if (leading != null) {
            if (leading.floatValue() <= 4f) {
                lineHeight = fontSize * leading.floatValue();
            } else {
                lineHeight = leading.floatValue();
            }
        }
        return lineHeight;
    }

    private float calculateSpaceBefore(DocPara para) {
        if (para.getSpaceBefore() != null) {
            return para.getSpaceBefore().floatValue();
        }
        switch (para.getHeadLevel()) {
            case 1: return 18f;
            case 2: return 14f;
            case 3: return 12f;
            default: return para.getHeadLevel() > 3 ? 6f : 0f;
        }
    }

    private float calculateSpaceAfter(DocPara para, float defaultSpacing) {
        if (para.getSpaceAfter() != null) {
            return para.getSpaceAfter().floatValue();
        }
        switch (para.getHeadLevel()) {
            case 1: return 10f;
            case 2: return 8f;
            default: return para.getHeadLevel() > 2 ? 4f : defaultSpacing;
        }
    }

    private float calculateParaFontSize(DocPara para, int styleCode) {
        if (para.getSize() > 0) {
            return fontManager.getFontSize(styleCode, para.getSize());
        }
        switch (para.getHeadLevel()) {
            case 1: return 18f;
            case 2: return 14f;
            case 3: return 12f;
            case 4: return 11f;
            case 5:
            case 6:
            case 7: return 10f;
            default: return fontManager.getFontSize(styleCode, para.getSize());
        }
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
        float lineHeight = calculateLineHeight(phrase.getLeading(), fontSize);
        renderText(text, font, fontSize, ALIGN_LEFT, phrase.getForeColor(), phrase.getBackColor(), lineHeight);
        currentY -= lineHeight * 0.25f;
    }

    private void renderParagraph(DocPara para) throws IOException {
        String text = extractText(para);
        if (text == null || text.trim().isEmpty()) {
            return;
        }

        currentY -= calculateSpaceBefore(para);

        checkNewPage();
        registerDestination(para.getId());

        int styleCode = para.getStyle();
        PDFont font = getEncodableFont(styleCode, para.getFontName(), text);
        float fontSize = calculateParaFontSize(para, styleCode);
        float lineHeight = calculateLineHeight(para.getLeading(), fontSize);

        renderText(text, new TextStyle(font, fontSize, para.getAlign(), para.getForeColor(), para.getBackColor(), lineHeight, isPreserveLine(para)));

        currentY -= calculateSpaceAfter(para, lineHeight * 0.25f);
    }

    private static class TextStyle {
        final PDFont font;
        final float fontSize;
        final int align;
        final String foreColor;
        final String backColor;
        final float lineHeight;
        final boolean preserveLine;

        TextStyle(PDFont font, float fontSize, int align, String foreColor, String backColor, float lineHeight, boolean preserveLine) {
            this.font = font;
            this.fontSize = fontSize;
            this.align = align;
            this.foreColor = foreColor;
            this.backColor = backColor;
            this.lineHeight = lineHeight;
            this.preserveLine = preserveLine;
        }
    }

    private void renderText(String text, PDFont font, float fontSize, int align, String foreColor, String backColor, float lineHeight) throws IOException {
        renderText(text, new TextStyle(font, fontSize, align, foreColor, backColor, lineHeight, false));
    }

    private void renderText(String text, TextStyle style) throws IOException {
        PDFont font = style.font;
        float fontSize = style.fontSize;
        int align = style.align;
        String foreColor = style.foreColor;
        String backColor = style.backColor;
        float lineHeight = style.lineHeight;
        boolean preserveLine = style.preserveLine;
        if (text == null || text.trim().isEmpty()) {
            return;
        }

        PDFont realFont = font;

        if (!canEncode(realFont, text)) {
            realFont = getEncodableFont(FontManager.STYLE_NORMAL, null, text);
        }

        float availableWidth = this.pageWidth - this.marginLeft - this.marginRight;
        List<String> lines = wrapParaLines(text, preserveLine, realFont, fontSize, availableWidth);

        for (String line : lines) {
            checkNewPage();

            float textWidth = calculateTextWidth(line, realFont, fontSize);
            float xPos = calculateXPosition(line, realFont, fontSize, align);

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

            if (!canEncode(realFont, line)) {
                line = sanitizeTextForFont(realFont, line);
            }

            contentStream.beginText();
            contentStream.setFont(realFont, fontSize);
            contentStream.newLineAtOffset(xPos, currentY);
            contentStream.showText(line);
            contentStream.endText();
            currentY -= lineHeight;
        }
    }

    private List<String> wrapParaLines(String text, boolean preserveLine, PDFont font, float fontSize, float availableWidth) throws IOException {
        if (preserveLine) {
            List<String> lines = new ArrayList<>();
            String[] parts = text.split("\\r?\\n");
            for (String part : parts) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    lines.addAll(wrapText(trimmed, font, fontSize, availableWidth));
                }
            }
            return lines;
        }
        return wrapText(text, font, fontSize, availableWidth);
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
        } else {
            currentY -= LINE_HEIGHT * 0.5f;
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
            for (int colW : cw) {
                totalRatio += (float) colW;
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

    private static class CellLine {
        private final String text;
        private final PDFont font;
        private final float fontSize;
        private final int align;
        private final String foreColor;

        public CellLine(String text, PDFont font, float fontSize, int align, String foreColor) {
            this.text = text;
            this.font = font;
            this.fontSize = fontSize;
            this.align = align;
            this.foreColor = foreColor;
        }
    }

    private static class CellRenderData {
        private DocCell cell;
        private float width;
        private PDImageXObject image;
        private float imageHeight;
        private List<CellLine> lines = Collections.emptyList();
    }

    private float calculateCellWidth(DocElement cellElement, int colIdx, float[] cellWidths) {
        int colspan = (cellElement instanceof DocCell)
                ? Math.max(1, ((DocCell) cellElement).getColumnSpan())
                : 1;
        float cWidth = 0;
        for (int s = 0; s < colspan && (colIdx + s) < cellWidths.length; s++) {
            cWidth += cellWidths[colIdx + s];
        }
        if (cWidth <= 0) {
            cWidth = cellWidths[0] * colspan;
        }
        return cWidth;
    }

    private PDImageXObject prepareCellImage(DocCell cell, float cWidth, float[] outImgHeight) {
        DocImage docImg = findFirstImage(cell);
        if (docImg == null) {
            return null;
        }
        PDImageXObject pdImg = createPDImage(docImg);
        if (pdImg != null) {
            float scale = docImg.getScaling() != null ? docImg.getScaling().floatValue() / 100f : 1.0f;
            float w = pdImg.getWidth() * scale;
            float imgH = pdImg.getHeight() * scale;
            if (w > cWidth - 6) {
                float r = (cWidth - 6) / w;
                imgH = imgH * r;
            }
            outImgHeight[0] = imgH;
            return pdImg;
        }
        return null;
    }

    private CellRenderData prepareCell(DocElement cellElement, float cWidth, boolean isHeader, DocRow row) throws IOException {
        CellRenderData data = new CellRenderData();
        data.width = cWidth;
        if (cellElement instanceof DocCell) {
            DocCell cell = (DocCell) cellElement;
            data.cell = cell;
            registerDestination(cell.getId());

            float[] imgH = new float[1];
            data.image = prepareCellImage(cell, cWidth, imgH);
            data.imageHeight = imgH[0];

            data.lines = prepareCellLines(cell, cWidth, isHeader, row);
        }
        return data;
    }

    private List<CellLine> prepareCellLines(DocCell cell, float cWidth, boolean isHeader, DocRow row) throws IOException {
        List<DocElement> elements = cell.getElementList();
        if (elements == null || elements.isEmpty()) {
            return fallbackOrEmpty(cell, cWidth, isHeader, row);
        }

        List<CellLine> lines = new ArrayList<>();
        float maxWidth = cWidth - 6;
        for (DocElement el : elements) {
            populateElementLines(lines, el, cell, maxWidth, isHeader, row);
        }

        if (lines.isEmpty() && dataHasNoImage(cell)) {
            return fallbackOrEmpty(cell, cWidth, isHeader, row);
        }
        return lines;
    }

    private void populateElementLines(List<CellLine> lines, DocElement el, DocCell cell, float maxWidth, boolean isHeader, DocRow row) throws IOException {
        if (el instanceof DocPara) {
            addParaLines(lines, (DocPara) el, cell, maxWidth, isHeader, row);
        } else if (el instanceof DocPhrase) {
            addPhraseLines(lines, (DocPhrase) el, cell, maxWidth, isHeader, row);
        }
    }

    private boolean dataHasNoImage(DocCell cell) {
        return findFirstImage(cell) == null;
    }

    private List<CellLine> fallbackOrEmpty(DocCell cell, float cWidth, boolean isHeader, DocRow row) throws IOException {
        String fallback = extractText(cell);
        if (fallback != null && !fallback.trim().isEmpty()) {
            return createFallbackCellLines(cell, fallback, cWidth, isHeader, row);
        }
        return Collections.emptyList();
    }

    private List<CellLine> createFallbackCellLines(DocCell cell, String fallback, float cWidth, boolean isHeader, DocRow row) throws IOException {
        boolean bold = isHeader || (cell != null && cell.isHeader()) || (row != null && row.isHeader());
        int style = bold ? FontManager.STYLE_BOLD : FontManager.STYLE_NORMAL;
        PDFont font = getEncodableFont(style, null, fallback);
        float fontSize = fontManager.getFontSize(0);
        int align = cell != null ? cell.getAlign() : DocPara.ALIGN_LEFT;
        String foreColor = cell != null ? cell.getForeColor() : null;
        List<CellLine> lines = new ArrayList<>();
        appendWrappedLines(lines, fallback, font, fontSize, cWidth - 6, align, foreColor);
        return lines;
    }

    private void addParaLines(List<CellLine> lines, DocPara para, DocCell cell, float maxWidth, boolean isHeader, DocRow row) throws IOException {
        String text = extractText(para);
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        int style = resolveParaStyle(para, cell, isHeader, row);
        String fontName = para.getFontName();
        PDFont font = getEncodableFont(style, fontName, text);
        float fontSize = para.getSize() > 0 ? fontManager.getFontSize(style, para.getSize()) : fontManager.getFontSize(0);
        int align = (para.getAlign() == DocPara.ALIGN_UNSET) ? cell.getAlign() : para.getAlign();
        String foreColor = StringUtils.isNotEmpty(para.getForeColor()) ? para.getForeColor() : cell.getForeColor();

        if (isPreserveLine(para)) {
            String[] splitLines = text.split("\\r?\\n");
            for (String split : splitLines) {
                String trimmed = split.trim();
                if (!trimmed.isEmpty()) {
                    appendWrappedLines(lines, trimmed, font, fontSize, maxWidth, align, foreColor);
                }
            }
        } else {
            appendWrappedLines(lines, text, font, fontSize, maxWidth, align, foreColor);
        }
    }

    private void addPhraseLines(List<CellLine> lines, DocPhrase phrase, DocCell cell, float maxWidth, boolean isHeader, DocRow row) throws IOException {
        String text = phrase.getText();
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        int style = phrase.getStyle();
        if (style == FontManager.STYLE_NORMAL && (isHeader || (cell != null && cell.isHeader()) || (row != null && row.isHeader()))) {
            style = FontManager.STYLE_BOLD;
        }
        String fontName = phrase.getFontName();
        PDFont font = getEncodableFont(style, fontName, text);
        float fontSize = phrase.getSize() > 0 ? fontManager.getFontSize(style, phrase.getSize()) : fontManager.getFontSize(0);
        int align = cell != null ? cell.getAlign() : DocPara.ALIGN_LEFT;
        String foreColor = StringUtils.isNotEmpty(phrase.getForeColor()) ? phrase.getForeColor() : cell.getForeColor();
        appendWrappedLines(lines, text, font, fontSize, maxWidth, align, foreColor);
    }

    private void appendWrappedLines(List<CellLine> lines, String text, PDFont font, float fontSize, float maxWidth, int align, String foreColor) throws IOException {
        List<String> wrapped = wrapText(text, font, fontSize, maxWidth);
        for (String w : wrapped) {
            lines.add(new CellLine(w, font, fontSize, align, foreColor));
        }
    }

    private int resolveParaStyle(DocPara para, DocCell cell, boolean isHeader, DocRow row) {
        if (para.getStyle() != DocPara.STYLE_NORMAL && para.getStyle() != DocPara.STYLE_UNSET) {
            return para.getStyle();
        }
        if (isHeader || (cell != null && cell.isHeader()) || (row != null && row.isHeader())) {
            return FontManager.STYLE_BOLD;
        }
        return para.getStyle();
    }

    private boolean isPreserveLine(DocPara para) {
        if (para == null) {
            return false;
        }
        return "preserve-line".equalsIgnoreCase(para.getFormat())
                || "preserve".equalsIgnoreCase(para.getWhiteSpaceCollapse())
                || "false".equalsIgnoreCase(para.getWhiteSpaceCollapse())
                || para.isNotWhiteSpaceCollapse();
    }

    private float calculateCellHeight(CellRenderData data) {
        float cellH = 0f;
        if (data.image != null) {
            cellH += data.imageHeight + 4f;
        }
        if (!data.lines.isEmpty()) {
            float linesH = 0f;
            for (CellLine line : data.lines) {
                linesH += calculateLineHeight(null, line.fontSize);
            }
            cellH += linesH + 4f;
        } else if (data.image == null) {
            cellH = LINE_HEIGHT + 4f;
        }
        return cellH;
    }

    private void renderRowBackgrounds(List<CellRenderData> cellDataList, float rowStartY, float rowHeight, DocTable parentTable) {
        float cellX = this.marginLeft;
        for (CellRenderData data : cellDataList) {
            if (data.cell != null) {
                DocCell cell = data.cell;
                String backColor = cell.getBackColor();
                if (StringUtils.isEmpty(backColor) && parentTable != null) {
                    backColor = parentTable.getBackColor();
                }

                if (StringUtils.isNotEmpty(backColor)) {
                    final String finalBackColor = backColor;
                    final float finalCellX = cellX;
                    final float finalCWidth = data.width;
                    final float finalRowStartY = rowStartY;
                    final float finalRowHeight = rowHeight;
                    SafeFunction.applySilent(() -> {
                        Color bg = DocModelUtils.parseHtmlColor(finalBackColor);
                        contentStream.setNonStrokingColor(bg);
                        contentStream.addRect(finalCellX, finalRowStartY - finalRowHeight + 2, finalCWidth, finalRowHeight);
                        contentStream.fill();
                    });
                }
            }
            cellX += data.width;
        }
    }

    private void renderCellImage(PDImageXObject pdImg, float imgH, float cellX, float currCellY, float cWidth) throws IOException {
        float drawW = (float) pdImg.getWidth();
        float drawH = imgH;
        if (drawW > cWidth - 6) {
            drawW = cWidth - 6;
        }
        contentStream.drawImage(pdImg, cellX + 3, currCellY - drawH, drawW, drawH);
    }

    private void renderCellLines(CellRenderData data, float cellX, float startY) throws IOException {
        if (data.lines.isEmpty()) {
            return;
        }
        float cellY = startY;
        for (CellLine line : data.lines) {
            String text = line.text;
            if (!canEncode(line.font, text)) {
                text = sanitizeTextForFont(line.font, text);
            }
            float textWidth = calculateTextWidth(text, line.font, line.fontSize);
            float lineX = cellX + 3;
            int align = (line.align == DocPara.ALIGN_UNSET) ? data.cell.getAlign() : line.align;
            if (align == DocPara.ALIGN_CENTER) {
                lineX = cellX + (data.width - textWidth) / 2;
            } else if (align == DocPara.ALIGN_RIGHT) {
                lineX = cellX + data.width - textWidth - 3;
            }

            if (StringUtils.isNotEmpty(line.foreColor)) {
                SafeFunction.applySilent(() -> {
                    Color fg = DocModelUtils.parseHtmlColor(line.foreColor);
                    contentStream.setNonStrokingColor(fg);
                });
            } else {
                contentStream.setNonStrokingColor(Color.BLACK);
            }

            contentStream.beginText();
            contentStream.setFont(line.font, line.fontSize);
            contentStream.newLineAtOffset(lineX, cellY - line.fontSize);
            contentStream.showText(text);
            contentStream.endText();

            float lineH = calculateLineHeight(null, line.fontSize);
            cellY -= lineH;
        }
    }

    private void renderCellContent(CellRenderData data, float cellX, float rowStartY) throws IOException {
        if (data.cell == null) {
            return;
        }
        float currCellY = rowStartY - 2;
        if (data.image != null) {
            renderCellImage(data.image, data.imageHeight, cellX, currCellY, data.width);
            currCellY -= data.imageHeight + 2f;
        }
        renderCellLines(data, cellX, currCellY);
    }

    private void renderRowCells(List<CellRenderData> cellDataList, float rowStartY) throws IOException {
        float cellX = this.marginLeft;
        for (CellRenderData data : cellDataList) {
            renderCellContent(data, cellX, rowStartY);
            cellX += data.width;
        }
    }

    private void renderRowBorders(List<CellRenderData> cellDataList, float rowStartY, float rowHeight) throws IOException {
        float cellX = this.marginLeft;
        float topY = rowStartY + 2;
        float bottomY = rowStartY - rowHeight + 2;
        for (CellRenderData data : cellDataList) {
            if (data.cell != null && data.cell.getDocBorders() != null) {
                renderCellBorders(data.cell.getDocBorders(), cellX, cellX + data.width, topY, bottomY);
            }
            cellX += data.width;
        }
    }

    private void renderCellBorders(DocBorders borders, float leftX, float rightX, float topY, float bottomY) throws IOException {
        float wTop = resolveBorderWidth(borders.getBorderWidthTop());
        if (wTop > 0) {
            drawLine(leftX, topY, rightX, topY, wTop, borders.getBorderColorTop());
        }
        float wBottom = resolveBorderWidth(borders.getBorderWidthBottom());
        if (wBottom > 0) {
            drawLine(leftX, bottomY, rightX, bottomY, wBottom, borders.getBorderColorBottom());
        }
        float wLeft = resolveBorderWidth(borders.getBorderWidthLeft());
        if (wLeft > 0) {
            drawLine(leftX, bottomY, leftX, topY, wLeft, borders.getBorderColorLeft());
        }
        float wRight = resolveBorderWidth(borders.getBorderWidthRight());
        if (wRight > 0) {
            drawLine(rightX, bottomY, rightX, topY, wRight, borders.getBorderColorRight());
        }
    }

    private float resolveBorderWidth(int width) {
        if (this.pdfboxDefaultTableBehaviour) {
            return width > 0 ? (float) width : 0f;
        }
        if (width == 0) {
            return 0f;
        }
        return width > 0 ? (float) width : 1.0f;
    }

    private void drawLine(float x1, float y1, float x2, float y2, float width, String colorStr) throws IOException {
        Color color = Color.BLACK;
        if (StringUtils.isNotEmpty(colorStr)) {
            try {
                color = DocModelUtils.parseHtmlColor(colorStr);
            } catch (Exception e) {
                log.debug("Failed to parse border color: {}", colorStr);
            }
        }
        contentStream.setStrokingColor(color);
        contentStream.setLineWidth(width);
        contentStream.moveTo(x1, y1);
        contentStream.lineTo(x2, y2);
        contentStream.stroke();
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
        float maxCellHeight = LINE_HEIGHT;
        List<CellRenderData> cellDataList = new ArrayList<>(cells.size());

        for (DocElement cellElement : cells) {
            float cWidth = calculateCellWidth(cellElement, colIdx, cellWidths);
            int colspan = (cellElement instanceof DocCell)
                    ? Math.max(1, ((DocCell) cellElement).getColumnSpan())
                    : 1;
            colIdx += colspan;

            CellRenderData data = prepareCell(cellElement, cWidth, isHeader, row);
            cellDataList.add(data);
            maxCellHeight = Math.max(maxCellHeight, calculateCellHeight(data));
        }

        float rowHeight = maxCellHeight;
        if (currentY - rowHeight < getContentBottom()) {
            createNewPage();
        }

        float rowStartY = currentY;
        renderRowBackgrounds(cellDataList, rowStartY, rowHeight, parentTable);
        renderRowCells(cellDataList, rowStartY);
        renderRowBorders(cellDataList, rowStartY, rowHeight);

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

    private void drawHeaderFooterBorder(PDPageContentStream stream, DocContainer container, boolean isHeader) throws IOException {
        if (container instanceof DocHeaderFooter) {
            int borderWidth = ((DocHeaderFooter) container).getBorderWidth();
            if (borderWidth > 0) {
                stream.setStrokingColor(Color.LIGHT_GRAY);
                stream.setLineWidth((float) borderWidth);
                float lineY = isHeader
                        ? (this.pageHeight - this.marginTop - this.headerExtent)
                        : (this.marginBottom + this.footerExtent);
                stream.moveTo(this.marginLeft, lineY);
                stream.lineTo(this.pageWidth - this.marginRight, lineY);
                stream.stroke();
            }
        }
    }

    private float renderHeaderFooterPara(PDPageContentStream stream, DocPara para, float yPosition,
                                         PDFont font, float fontSize, int currentPage, int totalPages) throws IOException {
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
            return yPosition - 12f;
        }
        return yPosition;
    }

    private float renderHeaderFooterPhrase(PDPageContentStream stream, DocPhrase phrase, float yPosition,
                                           PDFont font, float fontSize, int currentPage, int totalPages) throws IOException {
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
            return yPosition - 12f;
        }
        return yPosition;
    }

    private float renderHeaderFooterImage(PDPageContentStream stream, DocImage docImg, float yPosition) throws IOException {
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
            return yPosition - imgHeight - 4f;
        }
        return yPosition;
    }

    private static class HeaderFooterRenderContext {
        final PDPageContentStream stream;
        final PDFont font;
        final float fontSize;
        final int currentPage;
        final int totalPages;

        HeaderFooterRenderContext(PDPageContentStream stream, PDFont font, float fontSize, int currentPage, int totalPages) {
            this.stream = stream;
            this.font = font;
            this.fontSize = fontSize;
            this.currentPage = currentPage;
            this.totalPages = totalPages;
        }
    }

    private void renderHeaderFooterCell(HeaderFooterRenderContext ctx, DocCell cell, float cellX, float cWidth, float yPosition) throws IOException {
        String text = extractText(cell);
        if (text != null && !text.isEmpty()) {
            text = replacePlaceholders(text, ctx.currentPage, ctx.totalPages);
            int align = cell.getAlign();
            float textWidth = calculateTextWidth(text, ctx.font, ctx.fontSize);
            float xPos = cellX;
            if (align == DocPara.ALIGN_CENTER) {
                xPos = cellX + (cWidth - textWidth) / 2;
            } else if (align == DocPara.ALIGN_RIGHT) {
                xPos = cellX + cWidth - textWidth;
            }
            if (!canEncode(ctx.font, text)) {
                text = sanitizeTextForFont(ctx.font, text);
            }
            ctx.stream.setNonStrokingColor(Color.BLACK);
            ctx.stream.beginText();
            ctx.stream.setFont(ctx.font, ctx.fontSize);
            ctx.stream.newLineAtOffset(xPos, yPosition);
            ctx.stream.showText(text);
            ctx.stream.endText();
        }
    }

    private float renderHeaderFooterRow(HeaderFooterRenderContext ctx, DocRow row, float yPosition,
                                        float[] colWidths, float tableWidth) throws IOException {
        float cellX = this.marginLeft;
        if (row.getElementList() != null) {
            for (int c = 0; c < row.getElementList().size(); c++) {
                DocElement cElem = row.getElementList().get(c);
                float cWidth = (c < colWidths.length) ? colWidths[c] : (tableWidth / row.getElementList().size());
                if (cElem instanceof DocCell) {
                    renderHeaderFooterCell(ctx, (DocCell) cElem, cellX, cWidth, yPosition);
                }
                cellX += cWidth;
            }
        }
        return yPosition - 12f;
    }

    private float renderHeaderFooterTable(HeaderFooterRenderContext ctx, DocTable table, float yPosition) throws IOException {
        float tableWidth = this.pageWidth - this.marginLeft - this.marginRight;
        int colCount = table.getColumns() > 0 ? table.getColumns() : 2;
        float[] colWidths = calculateColWidths(table, colCount, tableWidth);
        float currentYPos = yPosition;
        if (table.getElementList() != null) {
            for (DocElement rowElem : table.getElementList()) {
                if (rowElem instanceof DocRow) {
                    currentYPos = renderHeaderFooterRow(ctx, (DocRow) rowElem, currentYPos, colWidths, tableWidth);
                }
            }
        }
        return currentYPos;
    }

    private float renderHeaderFooterElement(HeaderFooterRenderContext ctx, DocElement element, float yPosition) throws IOException {
        if (element instanceof DocPara) {
            return renderHeaderFooterPara(ctx.stream, (DocPara) element, yPosition, ctx.font, ctx.fontSize, ctx.currentPage, ctx.totalPages);
        } else if (element instanceof DocPhrase) {
            return renderHeaderFooterPhrase(ctx.stream, (DocPhrase) element, yPosition, ctx.font, ctx.fontSize, ctx.currentPage, ctx.totalPages);
        } else if (element instanceof DocImage) {
            return renderHeaderFooterImage(ctx.stream, (DocImage) element, yPosition);
        } else if (element instanceof DocTable) {
            return renderHeaderFooterTable(ctx, (DocTable) element, yPosition);
        }
        return yPosition;
    }

    private void renderHeaderFooterContent(PDPageContentStream stream, DocContainer container,
                                           boolean isHeader, int currentPage, int totalPages) throws IOException {
        List<DocElement> elements = container.getElementList();
        if (elements == null) {
            return;
        }

        drawHeaderFooterBorder(stream, container, isHeader);

        float yPosition = isHeader
                ? (this.pageHeight - this.marginTop - 10f)
                : (this.marginBottom + this.footerExtent - 12f);
        PDFont font = fontManager.getFont(0, null);
        float fontSize = 9f;

        HeaderFooterRenderContext ctx = new HeaderFooterRenderContext(stream, font, fontSize, currentPage, totalPages);
        for (DocElement element : elements) {
            yPosition = renderHeaderFooterElement(ctx, element, yPosition);
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

    private String extractParaText(DocPara para) {
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
        return paraText != null ? paraText : "";
    }

    private String extractText(DocElement element) {
        if (element instanceof DocPara) {
            return extractParaText((DocPara) element);
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
