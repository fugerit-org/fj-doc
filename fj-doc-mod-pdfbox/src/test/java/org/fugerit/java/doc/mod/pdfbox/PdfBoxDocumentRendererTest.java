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
            docBase.setDocHeader(header);

            // Setup Footer
            DocFooter footer = new DocFooter();
            DocPara footerPara = new DocPara();
            footerPara.setText("Footer - Page ${currentPage} of ${pageCount}");
            footerPara.setAlign(DocPara.ALIGN_RIGHT);
            footer.addElement(footerPara);
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

            // List (Unordered)
            DocList ulList = new DocList();
            ulList.setListType(DocList.LIST_TYPE_UL);
            DocLi li1 = new DocLi();
            DocPhrase phrase1 = new DocPhrase();
            phrase1.setText("List item 1");
            li1.addElement(phrase1);
            ulList.addElement(li1);
            body.addElement(ulList);

            // List (Ordered)
            DocList olList = new DocList();
            olList.setListType(DocList.LIST_TYPE_OL);
            DocLi li2 = new DocLi();
            DocPara para2 = new DocPara();
            para2.setText("Numbered list item 1 with long text to wrap around the line: more text more text more text");
            li2.addElement(para2);
            olList.addElement(li2);
            body.addElement(olList);

            // Table
            DocTable table = new DocTable();
            table.setColumns(2);
            DocRow row1 = new DocRow();
            DocCell cell1 = new DocCell();
            DocPara cellPara1 = new DocPara();
            cellPara1.setText("Cell 1");
            cell1.addElement(cellPara1);
            DocCell cell2 = new DocCell();
            DocPhrase cellPhrase2 = new DocPhrase();
            cellPhrase2.setText("Cell 2");
            cell2.addElement(cellPhrase2);
            row1.addElement(cell1);
            row1.addElement(cell2);
            table.addElement(row1);
            body.addElement(table);

            assertDoesNotThrow(() -> {
                renderer.renderDocument(docBase);
            });

            // ensure the document has been rendered to some byte array without issues
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
        }
    }
}
