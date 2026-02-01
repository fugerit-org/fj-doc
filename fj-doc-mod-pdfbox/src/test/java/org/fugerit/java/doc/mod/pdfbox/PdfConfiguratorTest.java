package org.fugerit.java.doc.mod.pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

class PdfConfiguratorTest {

    private PdfBoxConfig createConfig() {
        String documentTitle = "title";
        String documentAuthor = "author";
        String documentSubject = "subject";
        String documentLanguage = "it";
        Properties props = new Properties();
        props.setProperty( PdfBoxConfig.TITLE, documentTitle );
        props.setProperty( PdfBoxConfig.AUTHOR, documentAuthor );
        props.setProperty( PdfBoxConfig.SUBJECT, documentSubject );
        props.setProperty( PdfBoxConfig.LANGUAGE, documentLanguage );
        PdfBoxConfig config = PdfBoxConfig.fromProperties( props );
        config.setPdfAEnabled( Boolean.TRUE );
        Assertions.assertTrue( config.isPdfAEnabled() );
        config.setPdfUAEnabled( Boolean.TRUE );
        Assertions.assertTrue( config.isPdfUAEnabled() );
        Assertions.assertEquals( documentTitle, config.getDocumentTitle() );
        Assertions.assertEquals( documentAuthor, config.getDocumentAuthor() );
        Assertions.assertEquals( documentSubject, config.getDocumentSubject() );
        Assertions.assertEquals( documentLanguage, config.getDocumentLanguage() );
        return config;
    }

    @Test
    void testPdfAConfigurator() throws IOException {
        PdfBoxConfig config = this.createConfig();
        try (PDDocument doc = new PDDocument()) {

            PdfAConfigurator.configurePdfA1b( doc, config );
        }
        Assertions.assertTrue( Boolean.TRUE );
    }

    @Test
    void testPdfUAConfigurator() throws IOException {
        PdfBoxConfig config = this.createConfig();
        try (PDDocument doc = new PDDocument()) {
            PdfUAConfigurator.configurePdfUA1( doc, config );
        }
        Assertions.assertTrue( Boolean.TRUE );
    }

}
