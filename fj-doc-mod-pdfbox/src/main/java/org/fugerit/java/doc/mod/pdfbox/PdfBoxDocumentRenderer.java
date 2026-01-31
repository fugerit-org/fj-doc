package org.fugerit.java.doc.mod.pdfbox;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.*;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.fugerit.java.doc.base.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class PdfBoxDocumentRenderer {

    private final PDDocument document;
    private final PdfBoxConfig config;
    private final FontManager fontManager;
    private PDStructureTreeRoot structTreeRoot;
    private List<PDStructureElement> structureElements;

    // Current page and content stream
    private PDPage currentPage;
    private PDPageContentStream currentContentStream;
    private float currentY;
    private static final float MARGIN = 50f;
    private static final float LINE_HEIGHT = 15f;

    public PdfBoxDocumentRenderer(PDDocument document, PdfBoxConfig config) {
        this.document = document;
        this.config = config;
        this.fontManager = new FontManager(config, document);
        this.structureElements = new ArrayList<>();

        if (config.isPdfUAEnabled()) {
            this.structTreeRoot = document.getDocumentCatalog().getStructureTreeRoot();
        }
    }

    public void renderDocument(DocBase docBase) throws IOException {
        log.info("Starting document rendering");

        // Create first page
        createNewPage();

        // Process document body
        DocContainer body = docBase.getDocBody();
        if (body != null && body.getElementList() != null) {
            for (DocElement element : body.getElementList()) {
                log.info("Processing element: {}", element.getClass().getSimpleName());
                
                if (element instanceof DocPara) {
                    DocPara para = (DocPara) element;
                    if ( para.getHeadLevel() == 0 ) {
                        renderParagraph(para);    
                    } else {
                        renderHeading(para);
                    }
                } else if (element instanceof DocTable) {
                    renderTable((DocTable) element);
                } else if (element instanceof DocImage) {
                    renderImage((DocImage) element);
                }
            }
        }

        // Close the last content stream
        if (currentContentStream != null) {
            currentContentStream.close();
        }

        log.info("Document rendering completed");
    }

    private void createNewPage() throws IOException {
        // Close previous content stream if exists
        if (currentContentStream != null) {
            currentContentStream.close();
        }

        // Create new page
        currentPage = new PDPage(PDRectangle.A4);
        document.addPage(currentPage);

        // Initialize Y position from top
        currentY = currentPage.getMediaBox().getHeight() - MARGIN;

        // Create new content stream
        currentContentStream = new PDPageContentStream(
                document, currentPage, PDPageContentStream.AppendMode.APPEND, true, true);

        log.info("Created new page, initial Y: {}", currentY);
    }

    private void checkNewPage() throws IOException {
        // Check if we need a new page (less than margin from bottom)
        if (currentY < MARGIN + LINE_HEIGHT) {
            log.info("Creating new page, currentY: {}", currentY);
            createNewPage();
        }
    }

    private void renderParagraph(DocPara para) throws IOException {
        log.info("Rendering paragraph");

        checkNewPage();

        // Get font and size
        PDFont font = fontManager.getFont(para.getStyle());
        float fontSize = fontManager.getFontSize(para.getStyle());

        // Extract text from paragraph
        String text = extractText(para);
        log.info("Paragraph text: '{}'", text);

        if (text == null || text.isEmpty()) {
            log.warn("Empty paragraph, skipping");
            return;
        }

        // If PDF/UA is enabled, add structure
        if (config.isPdfUAEnabled()) {
            beginMarkedContent(currentContentStream, "P", para);
        }

        // Render text
        currentContentStream.beginText();
        currentContentStream.setFont(font, fontSize);
        currentContentStream.newLineAtOffset(MARGIN, currentY);

        // Handle text wrapping if needed
        List<String> lines = wrapText(text, font, fontSize,
                currentPage.getMediaBox().getWidth() - 2 * MARGIN);

        boolean first = true;
        for (String line : lines) {
            if (!first) {
                currentY -= LINE_HEIGHT;
                checkNewPage();
                currentContentStream.newLineAtOffset(0, -LINE_HEIGHT);
            }
            currentContentStream.showText(line);
            first = false;
        }

        currentContentStream.endText();

        if (config.isPdfUAEnabled()) {
            endMarkedContent(currentContentStream);
        }

        // Move to next line
        currentY -= LINE_HEIGHT * 1.5f;
    }

    private void renderHeading(DocPara heading) throws IOException {
        log.info("Rendering heading level {}", heading.getHeadLevel());

        checkNewPage();

        // Use larger font for headings
        PDFont font = fontManager.getFont(heading.getStyle());
        float fontSize = fontManager.getFontSize(heading.getStyle()) * 1.5f;

        String text = extractText(heading);

        if (text == null || text.isEmpty()) {
            return;
        }

        // Add some space before heading
        currentY -= LINE_HEIGHT;

        if (config.isPdfUAEnabled()) {
            beginMarkedContent(currentContentStream, "H" + heading.getHeadLevel(), heading);
        }

        currentContentStream.beginText();
        currentContentStream.setFont(font, fontSize);
        currentContentStream.newLineAtOffset(MARGIN, currentY);
        currentContentStream.showText(text);
        currentContentStream.endText();

        if (config.isPdfUAEnabled()) {
            endMarkedContent(currentContentStream);
        }

        currentY -= LINE_HEIGHT * 1.5f;
    }

    private void renderTable(DocTable table) throws IOException {
        log.info("Rendering table (basic implementation)");

        checkNewPage();

        if (config.isPdfUAEnabled()) {
            beginMarkedContent(currentContentStream, "Table", table);
        }

        // Iterate through rows
        if (table.getElementList() != null) {
            for (DocElement element : table.getElementList()) {
                if (element instanceof DocRow) {
                    renderTableRow((DocRow) element);
                }
            }
        }

        if (config.isPdfUAEnabled()) {
            endMarkedContent(currentContentStream);
        }

        currentY -= LINE_HEIGHT;
    }

    private void renderTableRow(DocRow row) throws IOException {
        checkNewPage();

        PDFont font = fontManager.getFont(0); // Default style
        float fontSize = fontManager.getFontSize(0);

        float cellX = MARGIN;
        float cellWidth = 100f; // Fixed width for now

        if (row.getElementList() != null) {
            for (DocElement element : row.getElementList()) {
                if (element instanceof DocCell) {
                    DocCell cell = (DocCell) element;
                    String cellText = extractText(cell);

                    currentContentStream.beginText();
                    currentContentStream.setFont(font, fontSize);
                    currentContentStream.newLineAtOffset(cellX, currentY);
                    currentContentStream.showText(cellText != null ? cellText : "");
                    currentContentStream.endText();

                    cellX += cellWidth;
                }
            }
        }

        currentY -= LINE_HEIGHT;
    }

    private void renderImage(DocImage image) throws IOException {
        log.info("Rendering image (not yet implemented)");
        // Image rendering would go here
    }

    private void beginMarkedContent(PDPageContentStream contentStream,
                                    String tag, DocElement element) throws IOException {
        if (config.isPdfUAEnabled()) {
            PDStructureElement structElem = new PDStructureElement(tag, structTreeRoot);

            // Add alt text if available (important for images)
            if (element instanceof DocImage) {
                DocImage img = (DocImage) element;
                if (img.getAlt() != null) {
                    structElem.setAlternateDescription(img.getAlt());
                }
            }

            structureElements.add(structElem);
            contentStream.beginMarkedContent(org.apache.pdfbox.cos.COSName.getPDFName(tag));
        }
    }

    private void endMarkedContent(PDPageContentStream contentStream) throws IOException {
        if (config.isPdfUAEnabled()) {
            contentStream.endMarkedContent();
        }
    }

    private String extractText(DocElement element) {
        StringBuilder text = new StringBuilder();

        if (element instanceof DocContainer) {
            List<DocElement> children = ((DocContainer)element).getElementList();
            for (DocElement child : children) {
                if (child instanceof DocPhrase) {
                    DocPhrase phrase = (DocPhrase) child;
                    if (phrase.getText() != null) {
                        text.append(phrase.getText());
                    }
                } else if (child instanceof DocNbsp) {
                    text.append(" ");
                }
            }
        }

        return text.toString();
    }

    private List<String> wrapText(String text, PDFont font, float fontSize, float maxWidth)
            throws IOException {
        List<String> lines = new ArrayList<>();

        if (text == null || text.isEmpty()) {
            return lines;
        }

        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
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