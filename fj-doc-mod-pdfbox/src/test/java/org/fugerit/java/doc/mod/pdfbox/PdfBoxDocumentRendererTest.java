package org.fugerit.java.doc.mod.pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.fugerit.java.doc.base.model.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PdfBoxDocumentRendererTest {

    @Test
    void testRenderDocument() throws IOException {
        try (PDDocument document = new PDDocument()) {
            PdfBoxConfig config = new PdfBoxConfig();
            PdfBoxDocumentRenderer renderer = new PdfBoxDocumentRenderer(document, config);

            DocBase docBase = new DocBase();

            // Setup Header
            DocHeader header = new DocHeader();
            DocPara headerPara = new DocPara();
            headerPara.setText("Header - Page ${currentPage} of ${pageCount}");
            headerPara.setAlign(DocPara.ALIGN_CENTER);
            header.addElement(headerPara);
            
            DocPhrase headerPhrase = new DocPhrase();
            headerPhrase.setText(" - Phrase");
            header.addElement(headerPhrase);
            
            docBase.setDocHeader(header);

            // Setup Footer
            DocFooter footer = new DocFooter();
            DocPara footerPara = new DocPara();
            footerPara.setText("Footer - Page ${currentPage} of ${pageCount}");
            footerPara.setAlign(DocPara.ALIGN_RIGHT);
            footer.addElement(footerPara);
            
            DocHeader emptyHeader = new DocHeader();
            DocImage headerImg = new DocImage();
            emptyHeader.addElement(headerImg);
            docBase.setDocHeader(emptyHeader);
            
            docBase.setDocFooter(footer);

            // Setup Body
            DocContainer body = new DocContainer();
            docBase.setDocBody(body);

            // Empty Paragraph
            DocPara emptyPara = new DocPara();
            body.addElement(emptyPara);

            // Basic Paragraphs
            DocPara paraLeft = new DocPara();
            paraLeft.setText("Left aligned paragraph.");
            paraLeft.setAlign(DocPara.ALIGN_LEFT);
            body.addElement(paraLeft);

            DocPara paraCenter = new DocPara();
            paraCenter.setText("Center aligned paragraph.");
            paraCenter.setAlign(DocPara.ALIGN_CENTER);
            body.addElement(paraCenter);

            DocPara paraRight = new DocPara();
            paraRight.setText("Right aligned paragraph.");
            paraRight.setAlign(DocPara.ALIGN_RIGHT);
            body.addElement(paraRight);

            DocPara paraJustify = new DocPara();
            paraJustify.setText("Justified paragraph. This is a long text to force text wrapping to the next line. This is a long text to force text wrapping to the next line. This is a long text to force text wrapping to the next line.");
            paraJustify.setAlign(DocPara.ALIGN_JUSTIFY);
            body.addElement(paraJustify);

            DocPara paraJustifyAll = new DocPara();
            paraJustifyAll.setText("Justified all paragraph.");
            paraJustifyAll.setAlign(PdfBoxDocumentRenderer.ALIGN_JUSTIFYALL);
            body.addElement(paraJustifyAll);

            // Paragraph with invalid alignment
            DocPara paraInvalidAlign = new DocPara();
            paraInvalidAlign.setText("Paragraph with invalid alignment.");
            paraInvalidAlign.setAlign(999);
            body.addElement(paraInvalidAlign);

            // Paragraph with newlines
            DocPara paraNewline = new DocPara();
            paraNewline.setText("Paragraph with\nnewlines\nand spaces");
            body.addElement(paraNewline);

            // Paragraph starting with spaces to hit wrapText empty word branch
            DocPara spaceStartPara = new DocPara();
            spaceStartPara.setText(" Starts with space and  has   multiple spaces.");
            body.addElement(spaceStartPara);

            // Empty texts and nulls
            DocPara nullPara = new DocPara();
            body.addElement(nullPara);
            
            DocPara emptyTextPara = new DocPara();
            emptyTextPara.setText("");
            body.addElement(emptyTextPara);
            
            DocPhrase emptyPhrase = new DocPhrase();
            emptyPhrase.setText("");
            body.addElement(emptyPhrase);

            // Space-only text to trigger wrapText edge cases
            DocPara spacesPara = new DocPara();
            spacesPara.setText("     ");
            body.addElement(spacesPara);

            // Unknown element to trigger extractText fallback
            DocImage docImage = new DocImage();
            body.addElement(docImage);

            // Long unbreakable word to trigger wrap issues
            DocPara longWordPara = new DocPara();
            longWordPara.setText("supercalifragilisticexpialidociousextralongwordtotriggerwrappingandchecktextwidthcalculations");
            body.addElement(longWordPara);

            // Force multiple pages to test checkNewPage()
            for (int i = 0; i < 50; i++) {
                DocPara p = new DocPara();
                p.setText("Filling page to trigger checkNewPage() logic - line " + i);
                body.addElement(p);
            }

            // List (Unordered)
            DocList ulList = new DocList();
            ulList.setListType(DocList.LIST_TYPE_UL);
            DocLi li1 = new DocLi();
            DocPhrase phrase1 = new DocPhrase();
            phrase1.setText("List item 1");
            li1.addElement(phrase1);
            ulList.addElement(li1);
            
            // Non-DocLi element in DocList
            DocPhrase nonLi = new DocPhrase();
            nonLi.setText("Non-Li item");
            ulList.addElement(nonLi);
            
            body.addElement(ulList);

            // Empty List
            DocList emptyList = new DocList();
            body.addElement(emptyList);

            // Empty DocLi
            DocList ulList2 = new DocList();
            DocLi emptyLi = new DocLi();
            ulList2.addElement(emptyLi);
            body.addElement(ulList2);

            // List (Ordered)
            DocList olList = new DocList();
            olList.setListType(DocList.LIST_TYPE_OL);
            DocLi li2 = new DocLi();
            DocPara para2 = new DocPara();
            para2.setText("Numbered list item 1 with long text to wrap around the line: more text more text more text");
            li2.addElement(para2);
            olList.addElement(li2);
            body.addElement(olList);

            // List with null type
            DocList nullTypeList = new DocList();
            DocLi li3 = new DocLi();
            DocPhrase phrase3 = new DocPhrase();
            phrase3.setText("List item no type");
            li3.addElement(phrase3);
            nullTypeList.addElement(li3);
            body.addElement(nullTypeList);

            // Table
            DocTable table = new DocTable();
            table.setColumns(2);
            
            // Add a non-row element to table
            DocPhrase invalidRow = new DocPhrase();
            invalidRow.setText("Invalid row");
            table.addElement(invalidRow);

            // Add an empty row
            DocRow emptyRow = new DocRow();
            table.addElement(emptyRow);

            // Normal row
            DocRow row1 = new DocRow();
            DocCell cell1 = new DocCell();
            DocPara cellPara1 = new DocPara();
            cellPara1.setText("Cell 1");
            cell1.addElement(cellPara1);
            
            DocCell cell2 = new DocCell();
            DocPhrase cellPhrase2 = new DocPhrase();
            cellPhrase2.setText("Cell 2");
            cell2.addElement(cellPhrase2);
            
            // DocCell with multiple phrases to trigger appendIfNotEmpty result.length() > 0
            DocCell cellMulti = new DocCell();
            DocPhrase multi1 = new DocPhrase();
            multi1.setText("Multi1");
            DocPhrase multi2 = new DocPhrase();
            multi2.setText("Multi2");
            cellMulti.addElement(multi1);
            cellMulti.addElement(multi2);
            
            // DocCell with empty text
            DocCell emptyCell = new DocCell();
            DocPhrase emptyCellPhrase = new DocPhrase();
            emptyCellPhrase.setText("");
            emptyCell.addElement(emptyCellPhrase);
            
            row1.addElement(cell1);
            row1.addElement(cell2);
            row1.addElement(cellMulti);
            row1.addElement(emptyCell);
            
            // Row with non-cell element
            DocPhrase nonCell = new DocPhrase();
            nonCell.setText("Non-cell element");
            row1.addElement(nonCell);
            
            table.addElement(row1);
            body.addElement(table);
            
            // Empty Table
            DocTable emptyTable = new DocTable();
            body.addElement(emptyTable);
            
            // Table with 0 columns (auto calculate columns)
            DocTable tableAutoCol = new DocTable();
            tableAutoCol.setColumns(0);
            DocRow autoRow = new DocRow();
            DocCell autoCell1 = new DocCell();
            DocPhrase autoPhrase1 = new DocPhrase();
            autoPhrase1.setText("Auto 1");
            autoCell1.addElement(autoPhrase1);
            DocCell autoCell2 = new DocCell();
            DocPhrase autoPhrase2 = new DocPhrase();
            autoPhrase2.setText("Auto 2");
            autoCell2.addElement(autoPhrase2);
            autoRow.addElement(autoCell1);
            autoRow.addElement(autoCell2);
            tableAutoCol.addElement(autoRow);
            
            // Row with empty cell
            DocRow emptyCellsRow = new DocRow();
            DocCell emptyCell1 = new DocCell();
            DocPara emptyCellPara = new DocPara();
            emptyCellPara.setText("");
            emptyCell1.addElement(emptyCellPara);
            emptyCellsRow.addElement(emptyCell1);
            tableAutoCol.addElement(emptyCellsRow);
            
            // Non-row element to hit maxCol skips
            DocPhrase nonRow = new DocPhrase();
            nonRow.setText("Non-row in auto col table");
            tableAutoCol.addElement(nonRow);
            
            body.addElement(tableAutoCol);

            assertDoesNotThrow(() -> {
                renderer.renderDocument(docBase);
            });

            // ensure the document has been rendered to some byte array without issues
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
        }
    }

    @Test
    void testEmptyDocument() throws IOException {
        try (PDDocument document = new PDDocument()) {
            PdfBoxConfig config = new PdfBoxConfig();
            PdfBoxDocumentRenderer renderer = new PdfBoxDocumentRenderer(document, config);
            DocBase docBase = new DocBase(); // No body, header, footer
            assertDoesNotThrow(() -> {
                renderer.renderDocument(docBase);
            });
        }
    }
}
