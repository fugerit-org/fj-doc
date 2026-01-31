package org.fugerit.java.doc.mod.pdfbox;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.fugerit.java.doc.base.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Complete renderer with header/footer and page number support
 */
@Slf4j
public class PdfBoxDocumentRenderer {

    private final PDDocument document;
    private final PdfBoxConfig config;
    private final FontManager fontManager;

    // Document structure
    private DocContainer headerContainer;
    private DocContainer footerContainer;
    private List<PDPage> pages;

    // Current page and rendering state
    private PDPage currentPage;
    private PDPageContentStream contentStream;
    private float currentY;

    // Page layout constants
    private static final float PAGE_WIDTH = PDRectangle.A4.getWidth();
    private static final float PAGE_HEIGHT = PDRectangle.A4.getHeight();
    private static final float TOP_MARGIN = 50f;
    private static final float BOTTOM_MARGIN = 80f; // Extra space for footer
    private static final float SIDE_MARGIN = 50f;
    private static final float LINE_HEIGHT = 15f;
    private static final float HEADER_HEIGHT = 30f;
    private static final float FOOTER_HEIGHT = 50f;

    // Content area boundaries
    private static final float CONTENT_TOP = PAGE_HEIGHT - TOP_MARGIN - HEADER_HEIGHT;
    private static final float CONTENT_BOTTOM = BOTTOM_MARGIN + FOOTER_HEIGHT;

    // Alignment constants
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final int ALIGN_JUSTIFY = 3;
    public static final int ALIGN_JUSTIFYALL = 4;

    private int numberedListCounter = 0;

    public PdfBoxDocumentRenderer(PDDocument document, PdfBoxConfig config) {
        this.document = document;
        this.config = config;
        this.fontManager = new FontManager(config, document);
        this.pages = new ArrayList<PDPage>();
    }

    public void renderDocument(DocBase docBase) throws IOException {
        log.info("=== Starting document rendering ===");

        // Store header and footer for later rendering
        headerContainer = docBase.getDocHeader();
        footerContainer = docBase.getDocFooter();

        // Create first page
        createNewPage();

        // Process document body
        DocContainer bodyContainer = docBase.getDocBody();
        if (bodyContainer != null) {
            log.info("Rendering body");
            renderContainer(bodyContainer);
        }

        // Close current content stream before adding headers/footers
        closeContentStream();

        // Render headers and footers on all pages with page numbers
        renderHeadersAndFooters();

        log.info("=== Document rendering completed ===");
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

        currentPage = new PDPage(PDRectangle.A4);
        document.addPage(currentPage);
        pages.add(currentPage);

        // Start content below header area
        currentY = CONTENT_TOP;

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
        if (currentY < CONTENT_BOTTOM + LINE_HEIGHT * 2) {
            log.info("Need new page, current Y: {}", currentY);
            createNewPage();
        }
    }

    private void processElement(DocElement element) throws IOException {
        if (element instanceof DocPara) {
            renderParagraph((DocPara) element);
        } else if (element instanceof DocTable) {
            renderTable((DocTable) element);
        } else if (element instanceof DocList) {
            renderList((DocList) element);
        }
    }

    private void renderParagraph(DocPara para) throws IOException {
        checkNewPage();

        int styleCode = para.getStyle();
        String text = extractText(para);

        if (text == null || text.trim().isEmpty()) {
            return;
        }

        PDFont font = fontManager.getFont(styleCode, para.getFontName());
        float fontSize = fontManager.getFontSize(styleCode);
        int align = para.getAlign();

        renderText(text, font, fontSize, align);
        currentY -= LINE_HEIGHT * 0.5f;
    }

    private void renderText(String text, PDFont font, float fontSize, int align) throws IOException {
        if (text == null || text.trim().isEmpty()) {
            return;
        }

        List<String> lines = wrapText(text, font, fontSize, PAGE_WIDTH - 2 * SIDE_MARGIN);

        for (String line : lines) {
            checkNewPage();

            float xPos = calculateXPosition(line, font, fontSize, align);

            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(xPos, currentY);
            contentStream.showText(line);
            contentStream.endText();
            currentY -= LINE_HEIGHT;
        }
    }

    private float calculateXPosition(String text, PDFont font, float fontSize, int align) throws IOException {
        if (align == ALIGN_LEFT || align == ALIGN_JUSTIFY || align == ALIGN_JUSTIFYALL) {
            return SIDE_MARGIN;
        }

        float textWidth = font.getStringWidth(text) / 1000 * fontSize;
        float availableWidth = PAGE_WIDTH - 2 * SIDE_MARGIN;

        if (align == ALIGN_CENTER) {
            return SIDE_MARGIN + (availableWidth - textWidth) / 2;
        } else if (align == ALIGN_RIGHT) {
            return PAGE_WIDTH - SIDE_MARGIN - textWidth;
        } else {
            return SIDE_MARGIN;
        }
    }

    private void renderList(DocList list) throws IOException {
        if (!(list instanceof DocContainer)) {
            return;
        }

        String listType = list.getListType();
        boolean isNumbered = (listType != null && listType.equals(DocList.LIST_TYPE_OL));

        if (isNumbered) {
            numberedListCounter = 1;
        }

        DocContainer listContainer = (DocContainer) list;
        List<DocElement> items = listContainer.getElementList();

        if (items != null) {
            for (DocElement item : items) {
                if (item instanceof DocLi) {
                    renderListItem((DocLi) item, isNumbered);
                }
            }
        }

        currentY -= LINE_HEIGHT * 0.5f;
    }

    private void renderListItem(DocLi listItem, boolean isNumbered) throws IOException {
        checkNewPage();

        PDFont font = fontManager.getFont(0, null);
        float fontSize = fontManager.getFontSize(0);

        String bullet = isNumbered ? (numberedListCounter++ + ".") : "•";

        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(SIDE_MARGIN, currentY);
        contentStream.showText(bullet);
        contentStream.endText();

        String text = extractText(listItem);

        if (text != null && !text.isEmpty()) {
            float indent = SIDE_MARGIN + 30f;
            List<String> lines = wrapText(text, font, fontSize, PAGE_WIDTH - indent - SIDE_MARGIN);

            for (int i = 0; i < lines.size(); i++) {
                if (i > 0) {
                    currentY -= LINE_HEIGHT;
                    checkNewPage();
                }

                contentStream.beginText();
                contentStream.setFont(font, fontSize);
                contentStream.newLineAtOffset(indent, currentY);
                contentStream.showText(lines.get(i));
                contentStream.endText();
            }
        }

        currentY -= LINE_HEIGHT;
    }

    private void renderTable(DocTable table) throws IOException {
        checkNewPage();

        if (!(table instanceof DocContainer)) {
            return;
        }

        DocContainer tableContainer = (DocContainer) table;
        List<DocElement> rows = tableContainer.getElementList();

        if (rows == null || rows.isEmpty()) {
            return;
        }

        int columnCount = getMaxColumnCount(rows);
        float tableWidth = PAGE_WIDTH - 2 * SIDE_MARGIN;
        float cellWidth = tableWidth / columnCount;

        boolean isFirstRow = true;
        for (DocElement rowElement : rows) {
            if (rowElement instanceof DocRow) {
                renderRow((DocRow) rowElement, cellWidth, columnCount, isFirstRow);
                isFirstRow = false;
            }
        }

        currentY -= LINE_HEIGHT * 0.5f;
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

    private void renderRow(DocRow row, float cellWidth, int columnCount, boolean isHeader) throws IOException {
        checkNewPage();

        if (!(row instanceof DocContainer)) {
            return;
        }

        PDFont font = fontManager.getFont(isHeader ? 1 : 0, null);
        float fontSize = fontManager.getFontSize(0);

        DocContainer rowContainer = (DocContainer) row;
        List<DocElement> cells = rowContainer.getElementList();

        if (cells == null) {
            return;
        }

        float cellX = SIDE_MARGIN;
        float rowStartY = currentY;

        for (DocElement cellElement : cells) {
            if (cellElement instanceof DocCell) {
                DocCell cell = (DocCell) cellElement;
                String cellText = extractText(cell);

                if (cellText != null && !cellText.isEmpty()) {
                    List<String> lines = wrapText(cellText, font, fontSize, cellWidth - 4);

                    float cellY = rowStartY;
                    for (String line : lines) {
                        contentStream.beginText();
                        contentStream.setFont(font, fontSize);
                        contentStream.newLineAtOffset(cellX + 2, cellY);
                        contentStream.showText(line);
                        contentStream.endText();
                        cellY -= LINE_HEIGHT;
                    }
                }

                cellX += cellWidth;
            }
        }

        // Draw border
        contentStream.setLineWidth(0.5f);
        contentStream.moveTo(SIDE_MARGIN, rowStartY - 2);
        contentStream.lineTo(SIDE_MARGIN + (cellWidth * columnCount), rowStartY - 2);
        contentStream.stroke();

        currentY -= LINE_HEIGHT * 1.5f;
    }

    /**
     * Render headers and footers on all pages with page number substitution
     */
    private void renderHeadersAndFooters() throws IOException {
        int totalPages = pages.size();

        for (int pageNum = 0; pageNum < totalPages; pageNum++) {
            PDPage page = pages.get(pageNum);
            int currentPageNumber = pageNum + 1;

            try (PDPageContentStream stream = new PDPageContentStream(
                    document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {

                // Render header
                if (headerContainer != null) {
                    renderHeaderFooterContent(stream, headerContainer, true, currentPageNumber, totalPages);
                }

                // Render footer
                if (footerContainer != null) {
                    renderHeaderFooterContent(stream, footerContainer, false, currentPageNumber, totalPages);
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

        float yPosition = isHeader ? (PAGE_HEIGHT - TOP_MARGIN) : BOTTOM_MARGIN;
        PDFont font = fontManager.getFont(0, null);
        float fontSize = 10f;

        for (DocElement element : elements) {
            if (element instanceof DocPara) {
                DocPara para = (DocPara) element;
                String text = extractText(para);

                if (text != null && !text.isEmpty()) {
                    // Replace placeholders
                    text = replacePlaceholders(text, currentPage, totalPages);

                    int align = para.getAlign();
                    float xPos = calculateXPosition(text, font, fontSize, align);

                    stream.beginText();
                    stream.setFont(font, fontSize);
                    stream.newLineAtOffset(xPos, yPosition);
                    stream.showText(text);
                    stream.endText();

                    yPosition -= LINE_HEIGHT;
                }
            }
        }
    }

    /**
     * Replace page number placeholders like ${currentPage} and ${pageCount}
     */
    private String replacePlaceholders(String text, int currentPage, int totalPages) {
        if (text == null) {
            return text;
        }

        // Replace ${currentPage} or ${r"${currentPage}"}
        text = text.replaceAll("\\$\\{r\"\\$\\{currentPage\\}\"\\}", String.valueOf(currentPage));
        text = text.replaceAll("\\$\\{currentPage\\}", String.valueOf(currentPage));

        // Replace ${pageCount} or ${r"${pageCount}"}
        text = text.replaceAll("\\$\\{r\"\\$\\{pageCount\\}\"\\}", String.valueOf(totalPages));
        text = text.replaceAll("\\$\\{pageCount\\}", String.valueOf(totalPages));

        return text;
    }

    private String extractText(DocElement element) {
        // DocPara stores text directly
        if (element instanceof DocPara) {
            DocPara para = (DocPara) element;
            String paraText = para.getText();
            if (paraText != null && !paraText.isEmpty()) {
                return paraText;
            }
        }

        StringBuilder result = new StringBuilder();

        // For containers, check children
        if (element instanceof DocContainer) {
            DocContainer container = (DocContainer) element;
            List<DocElement> children = container.getElementList();

            if (children != null) {
                for (DocElement child : children) {
                    if (child instanceof DocPhrase) {
                        DocPhrase phrase = (DocPhrase) child;
                        String text = phrase.getText();
                        if (text != null) {
                            if (result.length() > 0) {
                                result.append(" ");
                            }
                            result.append(text);
                        }
                    } else {
                        String childText = extractText(child);
                        if (childText != null && !childText.isEmpty()) {
                            if (result.length() > 0) {
                                result.append(" ");
                            }
                            result.append(childText);
                        }
                    }
                }
            }
        } else if (element instanceof DocPhrase) {
            DocPhrase phrase = (DocPhrase) element;
            String text = phrase.getText();
            if (text != null) {
                result.append(text);
            }
        }

        return result.toString().trim();
    }

    private List<String> wrapText(String text, PDFont font, float fontSize, float maxWidth)
            throws IOException {
        List<String> lines = new ArrayList<String>();

        if (text == null || text.isEmpty()) {
            return lines;
        }

        String[] words = text.split("\\s+");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }

            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
            float width = font.getStringWidth(testLine) / 1000 * fontSize;

            if (width > maxWidth && currentLine.length() > 0) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines.isEmpty() ? Arrays.asList(text) : lines;
    }
}