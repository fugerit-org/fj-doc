package org.fugerit.java.doc.mod.pdfbox;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.fugerit.java.doc.base.model.*;
import org.fugerit.java.doc.base.xml.DocModelUtils;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Renderer with header/footer, page number, colour, size and spacing support.
 */
@Slf4j
public class PdfBoxDocumentRenderer {

    private static final float PAGE_WIDTH = PDRectangle.A4.getWidth();
    private static final float PAGE_HEIGHT = PDRectangle.A4.getHeight();

    // Fixed internal heights for header / footer bands
    private static final float HEADER_BAND_HEIGHT = 30f;
    private static final float FOOTER_BAND_HEIGHT = 30f;
    private static final float LINE_HEIGHT = 15f;
    private static final float DEFAULT_FONT_SIZE = 12f;

    // Alignment constants (mirrors DocPara)
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final int ALIGN_JUSTIFY = 3;
    public static final int ALIGN_JUSTIFYALL = 4;

    private final PDDocument document;
    private final FontManager fontManager;

    // Page-layout values derived from the document's <info name="margins">
    private float marginLeft;
    private float marginRight;
    private float marginTop;
    private float marginBottom;

    // Content-area vertical boundaries (recalculated per document)
    private float contentTop;
    private float contentBottom;

    // Header / footer containers (resolved once per document)
    private DocContainer headerContainer;
    private DocContainer footerContainer;

    // Per-page content streams + page list
    private final List<PDPage> pages = new ArrayList<>();
    private PDPageContentStream contentStream;
    private float currentY;

    // Numbered-list counter reset per list
    private int numberedListCounter = 0;

    public PdfBoxDocumentRenderer(PDDocument document, PdfBoxConfig config) {
        this.document = document;
        this.fontManager = new FontManager(config, document);
    }

    // -----------------------------------------------------------------------
    // Public entry point
    // -----------------------------------------------------------------------

    public void renderDocument(DocBase docBase) throws IOException {
        log.debug("Starting document rendering");

        // Resolve margins from document metadata (values are in points, matching
        // the convention used by fj-doc-mod-openpdf-ext).
        marginLeft   = Math.max(docBase.getMarginLeft(),   10);
        marginRight  = Math.max(docBase.getMarginRight(),  10);
        marginTop    = Math.max(docBase.getMarginTop(),    10);
        marginBottom = Math.max(docBase.getMarginBottom(), 10);

        // Resolve header / footer
        headerContainer = docBase.isUseHeader() ? docBase.getDocHeader() : null;
        footerContainer = docBase.isUseFooter() ? docBase.getDocFooter() : null;

        // Content area avoids header and footer bands
        contentTop    = PAGE_HEIGHT - marginTop    - (headerContainer != null ? HEADER_BAND_HEIGHT : 0);
        contentBottom = marginBottom               + (footerContainer != null ? FOOTER_BAND_HEIGHT : 0);

        // Render body
        createNewPage();
        DocContainer body = docBase.getDocBody();
        if (body != null) {
            renderContainer(body);
        }
        closeContentStream();

        // Overlay header/footer on every page
        renderHeadersAndFooters();

        log.debug("Document rendering completed");
    }

    // -----------------------------------------------------------------------
    // Page management
    // -----------------------------------------------------------------------

    private void createNewPage() throws IOException {
        closeContentStream();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        pages.add(page);
        currentY = contentTop;
        contentStream = new PDPageContentStream(
                document, page, PDPageContentStream.AppendMode.APPEND, true, true);
        log.debug("Created page {}, Y={}", pages.size(), currentY);
    }

    private void closeContentStream() throws IOException {
        if (contentStream != null) {
            contentStream.close();
            contentStream = null;
        }
    }

    private void checkNewPage() throws IOException {
        if (currentY < contentBottom + LINE_HEIGHT * 2) {
            createNewPage();
        }
    }

    // -----------------------------------------------------------------------
    // Container / element dispatch
    // -----------------------------------------------------------------------

    private void renderContainer(DocContainer container) throws IOException {
        List<DocElement> elements = container.getElementList();
        if (elements != null) {
            for (DocElement element : elements) {
                processElement(element);
            }
        }
    }

    private void processElement(DocElement element) throws IOException {
        if (element instanceof DocPara) {
            renderParagraph((DocPara) element);
        } else if (element instanceof DocTable) {
            renderTable((DocTable) element);
        } else if (element instanceof DocList) {
            renderList((DocList) element);
        } else if (element instanceof DocBr) {
            // Top-level line break
            currentY -= LINE_HEIGHT;
            checkNewPage();
        }
    }

    // -----------------------------------------------------------------------
    // Paragraph rendering
    // -----------------------------------------------------------------------

    private void renderParagraph(DocPara para) throws IOException {
        checkNewPage();

        // spaceBefore
        Float spaceBefore = para.getSpaceBefore();
        if (spaceBefore != null && spaceBefore > 0) {
            currentY -= spaceBefore;
            checkNewPage();
        }

        int styleCode   = para.getStyle();
        float fontSize  = resolveSize(para.getSize());
        PDFont font     = fontManager.getFont(styleCode, para.getFontName());
        int align       = para.getAlign();
        String foreColor = para.getForeColor();

        // Render inline children (phrases / br) or plain text
        List<DocElement> children = para.getElementList();
        if (children != null && !children.isEmpty()) {
            renderInlineChildren(children, font, fontSize, align, foreColor);
        } else {
            String text = para.getText();
            if (text != null && !text.trim().isEmpty()) {
                renderTextBlock(text, font, fontSize, align, foreColor);
            }
        }

        // Inter-paragraph gap
        currentY -= LINE_HEIGHT * 0.3f;

        // spaceAfter
        Float spaceAfter = para.getSpaceAfter();
        if (spaceAfter != null && spaceAfter > 0) {
            currentY -= spaceAfter;
        }
    }

    /**
     * Renders the inline children of a paragraph (DocPhrase / DocBr) while
     * keeping track of X position so phrases flow across the same line.
     */
    private void renderInlineChildren(List<DocElement> children,
                                      PDFont defaultFont, float defaultFontSize,
                                      int align, String defaultForeColor) throws IOException {
        // Collect text runs; DocBr inserts an explicit "\n"
        StringBuilder buffer = new StringBuilder();
        List<TextRun> runs = new ArrayList<>();

        for (DocElement child : children) {
            if (child instanceof DocBr) {
                if (buffer.length() > 0) {
                    runs.add(new TextRun(buffer.toString(), defaultFont, defaultFontSize, defaultForeColor));
                    buffer = new StringBuilder();
                }
                runs.add(TextRun.lineBreak());
            } else if (child instanceof DocPhrase) {
                DocPhrase phrase = (DocPhrase) child;
                String text = phrase.getText();
                if (text != null && !text.isEmpty()) {
                    PDFont phraseFont   = fontManager.getFont(phrase.getStyle(), phrase.getFontName());
                    float phraseSize    = resolveSize(phrase.getSize(), defaultFontSize);
                    String phraseColor  = phrase.getForeColor() != null ? phrase.getForeColor() : defaultForeColor;
                    runs.add(new TextRun(text, phraseFont, phraseSize, phraseColor));
                }
            }
        }

        if (buffer.length() > 0) {
            runs.add(new TextRun(buffer.toString(), defaultFont, defaultFontSize, defaultForeColor));
        }

        if (runs.isEmpty()) {
            return;
        }

        // If there is only one run without a line-break, delegate to the simple path
        if (runs.size() == 1 && !runs.get(0).isLineBreak) {
            TextRun r = runs.get(0);
            renderTextBlock(r.text, r.font, r.fontSize, align, r.foreColor);
            return;
        }

        // Multi-run layout: word-wrap across runs (simplified: each run is its own block)
        for (TextRun run : runs) {
            if (run.isLineBreak) {
                currentY -= LINE_HEIGHT;
                checkNewPage();
            } else {
                renderTextBlock(run.text, run.font, run.fontSize, align, run.foreColor);
            }
        }
    }

    // -----------------------------------------------------------------------
    // Low-level text rendering
    // -----------------------------------------------------------------------

    /** Renders a block of text (possibly multi-line after wrapping). */
    private void renderTextBlock(String text, PDFont font, float fontSize,
                                 int align, String foreColor) throws IOException {
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        float maxWidth = PAGE_WIDTH - marginLeft - marginRight;
        // Split on explicit newlines first (from DocBr embedded in text)
        String[] paragraphs = text.split("\n", -1);
        for (String paragraph : paragraphs) {
            List<String> lines = paragraph.isEmpty()
                    ? Collections.singletonList("")
                    : wrapWords(paragraph, font, fontSize, maxWidth);
            for (String line : lines) {
                checkNewPage();
                if (!line.isEmpty()) {
                    float xPos = calculateXPosition(line, font, fontSize, align, maxWidth);
                    drawTextLine(line, font, fontSize, xPos, currentY, foreColor);
                }
                currentY -= LINE_HEIGHT;
            }
        }
    }

    /** Draws a single line of text at (x, y) with the given colour. */
    private void drawTextLine(String text, PDFont font, float fontSize,
                              float x, float y, String foreColor) throws IOException {
        Color color = parseColor(foreColor);
        contentStream.setNonStrokingColor(color);
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
        // Reset to black after each line to avoid colour leaking
        contentStream.setNonStrokingColor(Color.BLACK);
    }

    private float calculateXPosition(String text, PDFont font, float fontSize,
                                     int align, float maxWidth) throws IOException {
        if (align == ALIGN_LEFT || align == ALIGN_JUSTIFY || align == ALIGN_JUSTIFYALL) {
            return marginLeft;
        }
        float textWidth = font.getStringWidth(text) / 1000f * fontSize;
        if (align == ALIGN_CENTER) {
            return marginLeft + (maxWidth - textWidth) / 2f;
        } else if (align == ALIGN_RIGHT) {
            return PAGE_WIDTH - marginRight - textWidth;
        }
        return marginLeft;
    }

    /**
     * Computes the absolute X position of text inside a table cell.
     *
     * @param cellOriginX the left edge of the cell's inner content area (absolute page coordinate)
     * @param innerWidth  the inner width of the cell (excluding padding)
     */
    private float calculateCellXPosition(String text, PDFont font, float fontSize,
                                         int align, float cellOriginX, float innerWidth) throws IOException {
        if (align == ALIGN_LEFT || align == ALIGN_JUSTIFY || align == ALIGN_JUSTIFYALL
                || align == ALIGN_UNSET) {
            return cellOriginX;
        }
        float textWidth = font.getStringWidth(text) / 1000f * fontSize;
        if (align == ALIGN_CENTER) {
            return cellOriginX + (innerWidth - textWidth) / 2f;
        } else if (align == ALIGN_RIGHT) {
            return cellOriginX + innerWidth - textWidth;
        }
        return cellOriginX;
    }

    private static final int ALIGN_UNSET = DocPara.ALIGN_UNSET;

    // -----------------------------------------------------------------------
    // List rendering
    // -----------------------------------------------------------------------

    private void renderList(DocList list) throws IOException {
        String listType = list.getListType();
        boolean isNumbered = DocList.LIST_TYPE_OL.equals(listType);
        if (isNumbered) {
            numberedListCounter = 1;
        }
        List<DocElement> items = list.getElementList();
        if (items != null) {
            for (DocElement item : items) {
                if (item instanceof DocLi) {
                    renderListItem((DocLi) item, isNumbered);
                }
            }
        }
        currentY -= LINE_HEIGHT * 0.3f;
    }

    private void renderListItem(DocLi listItem, boolean isNumbered) throws IOException {
        checkNewPage();

        PDFont font   = fontManager.getFont(0, null);
        float fontSize = DEFAULT_FONT_SIZE;
        String bullet  = isNumbered ? (numberedListCounter++ + ".") : "\u2022";
        float indent   = marginLeft + 20f;
        float maxWidth = PAGE_WIDTH - indent - marginRight;

        // Draw bullet / number
        drawTextLine(bullet, font, fontSize, marginLeft, currentY, null);

        // Draw item text
        String text = extractText(listItem);
        if (text != null && !text.isEmpty()) {
            List<String> lines = wrapWords(text, font, fontSize, maxWidth);
            boolean first = true;
            for (String line : lines) {
                if (!first) {
                    currentY -= LINE_HEIGHT;
                    checkNewPage();
                }
                drawTextLine(line, font, fontSize, indent, currentY, null);
                first = false;
            }
        }
        currentY -= LINE_HEIGHT;
    }

    // -----------------------------------------------------------------------
    // Table rendering
    // -----------------------------------------------------------------------

    private void renderTable(DocTable table) throws IOException {
        checkNewPage();
        List<DocElement> rows = table.getElementList();
        if (rows == null || rows.isEmpty()) {
            return;
        }

        int columnCount  = getMaxColumnCount(rows);
        float tableWidth = PAGE_WIDTH - marginLeft - marginRight;

        // Resolve column widths from the document model
        float[] colWidths = resolveColumnWidths(table, columnCount, tableWidth);

        boolean isFirstRow = true;
        for (DocElement rowElement : rows) {
            if (rowElement instanceof DocRow) {
                renderRow((DocRow) rowElement, colWidths, columnCount, isFirstRow);
                isFirstRow = false;
            }
        }
        currentY -= LINE_HEIGHT * 0.3f;
    }

    /**
     * Resolves per-column widths in points.
     * If the table specifies colwidths (percentage values), they are honoured;
     * otherwise columns are equally distributed.
     */
    private float[] resolveColumnWidths(DocTable table, int columnCount, float tableWidth) {
        float[] widths = new float[columnCount];
        int[] colWithds = table.getColWithds();
        if (colWithds != null && colWithds.length >= columnCount) {
            // colwidths are percentage values (sum should be 100)
            int total = 0;
            for (int i = 0; i < columnCount; i++) {
                total += colWithds[i];
            }
            int safeTotal = total > 0 ? total : 100;
            for (int i = 0; i < columnCount; i++) {
                widths[i] = tableWidth * colWithds[i] / safeTotal;
            }
        } else {
            float cellWidth = tableWidth / columnCount;
            for (int i = 0; i < columnCount; i++) {
                widths[i] = cellWidth;
            }
        }
        return widths;
    }

    private int getMaxColumnCount(List<DocElement> rows) {
        int max = 0;
        for (DocElement rowElement : rows) {
            if (rowElement instanceof DocRow && rowElement instanceof DocContainer) {
                List<DocElement> cells = ((DocContainer) rowElement).getElementList();
                if (cells != null) {
                    max = Math.max(max, cells.size());
                }
            }
        }
        return max > 0 ? max : 1;
    }

    private void renderRow(DocRow row, float[] colWidths, int columnCount,
                           boolean isHeaderRow) throws IOException {
        checkNewPage();
        List<DocElement> cells = row.getElementList();
        if (cells == null) {
            return;
        }

        int styleCode = (isHeaderRow || row.isHeader()) ? DocPara.STYLE_BOLD : DocPara.STYLE_NORMAL;
        PDFont font   = fontManager.getFont(styleCode, null);
        float fontSize = DEFAULT_FONT_SIZE;

        float cellX     = marginLeft;
        float rowStartY = currentY;
        float padding   = 2f;

        for (int colIdx = 0; colIdx < cells.size() && colIdx < columnCount; colIdx++) {
            DocElement cellElement = cells.get(colIdx);
            if (!(cellElement instanceof DocCell)) {
                cellX += colWidths[colIdx];
                continue;
            }
            DocCell cell = (DocCell) cellElement;
            String cellText = extractText(cell);

            if (cellText != null && !cellText.isEmpty()) {
                float innerWidth = colWidths[colIdx] - 2 * padding;
                List<String> lines = wrapWords(cellText, font, fontSize, innerWidth);
                int cellAlign = cell.getAlign();
                float cellY = rowStartY;
                for (String line : lines) {
                    float absX = calculateCellXPosition(line, font, fontSize, cellAlign,
                            cellX + padding, colWidths[colIdx] - 2 * padding);
                    drawTextLine(line, font, fontSize, absX, cellY, cell.getForeColor());
                    cellY -= LINE_HEIGHT;
                }
            }
            cellX += colWidths[colIdx];
        }

        // Bottom border of the row
        contentStream.setLineWidth(0.5f);
        contentStream.moveTo(marginLeft, rowStartY - 2);
        contentStream.lineTo(marginLeft + sumWidths(colWidths, Math.min(cells.size(), columnCount)),
                rowStartY - 2);
        contentStream.stroke();

        currentY -= LINE_HEIGHT * 1.5f;
    }

    private float sumWidths(float[] widths, int count) {
        float sum = 0;
        for (int i = 0; i < count; i++) {
            sum += widths[i];
        }
        return sum;
    }

    // -----------------------------------------------------------------------
    // Header / footer rendering
    // -----------------------------------------------------------------------

    private void renderHeadersAndFooters() throws IOException {
        int totalPages = pages.size();
        for (int i = 0; i < totalPages; i++) {
            PDPage page = pages.get(i);
            int pageNum = i + 1;
            try (PDPageContentStream stream = new PDPageContentStream(
                    document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                if (headerContainer != null) {
                    renderBand(stream, headerContainer, true, pageNum, totalPages);
                }
                if (footerContainer != null) {
                    renderBand(stream, footerContainer, false, pageNum, totalPages);
                }
            }
        }
    }

    private void renderBand(PDPageContentStream stream, DocContainer container,
                            boolean isHeader, int currentPage, int totalPages) throws IOException {
        List<DocElement> elements = container.getElementList();
        if (elements == null) {
            return;
        }
        float yPos     = isHeader ? (PAGE_HEIGHT - marginTop) : marginBottom;
        PDFont font    = fontManager.getFont(0, null);
        float fontSize = 10f;
        float maxWidth = PAGE_WIDTH - marginLeft - marginRight;

        for (DocElement element : elements) {
            if (element instanceof DocPara) {
                DocPara para = (DocPara) element;
                String text  = extractText(para);
                if (text != null && !text.isEmpty()) {
                    text = replacePlaceholders(text, currentPage, totalPages);
                    int align  = para.getAlign();
                    float xPos = calculateXPosition(text, font, fontSize, align, maxWidth);
                    Color color = parseColor(para.getForeColor());
                    stream.setNonStrokingColor(color);
                    stream.beginText();
                    stream.setFont(font, fontSize);
                    stream.newLineAtOffset(xPos, yPos);
                    stream.showText(text);
                    stream.endText();
                    stream.setNonStrokingColor(Color.BLACK);
                    yPos -= LINE_HEIGHT;
                }
            }
        }
    }

    // -----------------------------------------------------------------------
    // Placeholder substitution
    // -----------------------------------------------------------------------

    /**
     * Replaces {@code ${currentPage}} / {@code ${pageCount}} placeholders.
     * Also handles the FreeMarker raw-string escape form
     * {@code ${r"${currentPage}"}} that may appear when the source XML was
     * originally a FreeMarker template.
     */
    private String replacePlaceholders(String text, int currentPage, int totalPages) {
        if (text == null) {
            return null;
        }
        // FreeMarker raw-string form first (more specific match)
        String result = text.replace("${r\"${currentPage}\"}", String.valueOf(currentPage));
        result = result.replace("${currentPage}", String.valueOf(currentPage));
        result = result.replace("${r\"${pageCount}\"}", String.valueOf(totalPages));
        result = result.replace("${pageCount}", String.valueOf(totalPages));
        return result;
    }

    // -----------------------------------------------------------------------
    // Text extraction helpers
    // -----------------------------------------------------------------------

    private String extractText(DocElement element) {
        if (element instanceof DocPara) {
            DocPara para = (DocPara) element;
            String paraText = para.getText();
            if (paraText != null && !paraText.isEmpty()) {
                return paraText;
            }
        }

        StringBuilder result = new StringBuilder();
        if (element instanceof DocContainer) {
            for (DocElement child : ((DocContainer) element).getElementList()) {
                if (child instanceof DocBr) {
                    result.append("\n");
                } else if (child instanceof DocPhrase) {
                    String text = ((DocPhrase) child).getText();
                    if (text != null && !text.isEmpty()) {
                        if (result.length() > 0 && result.charAt(result.length() - 1) != '\n') {
                            result.append(" ");
                        }
                        result.append(text);
                    }
                } else {
                    String childText = extractText(child);
                    if (childText != null && !childText.isEmpty()) {
                        if (result.length() > 0 && result.charAt(result.length() - 1) != '\n') {
                            result.append(" ");
                        }
                        result.append(childText);
                    }
                }
            }
        } else if (element instanceof DocPhrase) {
            String text = ((DocPhrase) element).getText();
            if (text != null) {
                result.append(text);
            }
        }
        return result.toString().trim();
    }

    // -----------------------------------------------------------------------
    // Word-wrap
    // -----------------------------------------------------------------------

    private List<String> wrapWords(String text, PDFont font, float fontSize,
                                   float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            lines.add("");
            return lines;
        }
        String[] words = text.split("\\s+");
        StringBuilder current = new StringBuilder();

        for (String word : words) {
            if (word.isEmpty()) continue;
            if (current.length() == 0) {
                current.append(word);
            } else {
                String test = current + " " + word;
                float w = font.getStringWidth(test) / 1000f * fontSize;
                if (w > maxWidth) {
                    lines.add(current.toString());
                    current = new StringBuilder(word);
                } else {
                    current.append(" ").append(word);
                }
            }
        }
        if (current.length() > 0) {
            lines.add(current.toString());
        }
        if (lines.isEmpty()) {
            lines.add(text);
        }
        return lines;
    }

    // -----------------------------------------------------------------------
    // Font-size resolution
    // -----------------------------------------------------------------------

    /**
     * Resolves the effective font size.
     * A value {@code <= 0} (including -1 which means "unset") falls back to
     * {@link #DEFAULT_FONT_SIZE}.
     */
    private float resolveSize(int size) {
        return resolveSize(size, DEFAULT_FONT_SIZE);
    }

    private float resolveSize(int size, float defaultSize) {
        return size > 0 ? (float) size : defaultSize;
    }

    // -----------------------------------------------------------------------
    // Colour helpers
    // -----------------------------------------------------------------------

    /** Parses a {@code #rrggbb} colour string; falls back to black. */
    private Color parseColor(String htmlColor) {
        if (htmlColor == null || htmlColor.isEmpty()) {
            return Color.BLACK;
        }
        try {
            return DocModelUtils.parseHtmlColor(htmlColor);
        } catch (Exception e) {
            log.warn("Cannot parse colour '{}', using black", htmlColor);
            return Color.BLACK;
        }
    }

    // -----------------------------------------------------------------------
    // Inner helper: text run for inline layout
    // -----------------------------------------------------------------------

    private static final class TextRun {
        final String  text;
        final PDFont  font;
        final float   fontSize;
        final String  foreColor;
        final boolean isLineBreak;

        TextRun(String text, PDFont font, float fontSize, String foreColor) {
            this.text        = text;
            this.font        = font;
            this.fontSize    = fontSize;
            this.foreColor   = foreColor;
            this.isLineBreak = false;
        }

        private TextRun() {
            this.text        = null;
            this.font        = null;
            this.fontSize    = 0;
            this.foreColor   = null;
            this.isLineBreak = true;
        }

        static TextRun lineBreak() {
            return new TextRun();
        }
    }
}
