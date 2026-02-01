package org.fugerit.java.doc.mod.pdfbox;

import java.util.Properties;

public class PdfBoxConfig {

    private boolean pdfAEnabled = false;
    private boolean pdfUAEnabled = false;
    private String documentTitle = "Untitled";
    private String documentAuthor = "Unknown";
    private String documentSubject = "";
    private String documentLanguage = "en-US";
    private String fontDirectory = null;

    // PDF/A conformance level (A1b, A2b, A3b)
    private String pdfALevel = "1b";

    public static final String PDFA_ENABLED = "pdfaEnabled";
    public static final String PDFUA_ENABLED = "pdfuaEnabled";
    public static final String TITLE = "title";
    public static final String SUBJECT = "subject";
    public static final String AUTHOR = "author";
    public static final String LANGUAGE = "language";

    public static PdfBoxConfig fromProperties(Properties props) {
        PdfBoxConfig config = new PdfBoxConfig();

        config.setPdfAEnabled(Boolean.parseBoolean(
                props.getProperty(PDFA_ENABLED, "false")) );
        config.setPdfUAEnabled( Boolean.parseBoolean(
                props.getProperty(PDFUA_ENABLED, "false")) );
        config.setPdfALevel( props.getProperty("pdfa.level", "1b") );
        config.setDocumentTitle( props.getProperty(TITLE, "Untitled") );
        config.setDocumentAuthor( props.getProperty(AUTHOR, "Unknown") );
        config.setDocumentSubject( props.getProperty(SUBJECT, "") );
        config.setDocumentLanguage( props.getProperty(LANGUAGE, "en-US") );
        config.setFontDirectory(  props.getProperty("font.directory") );

        return config;
    }

    // Getters and setters
    public boolean isPdfAEnabled() { return pdfAEnabled; }
    public void setPdfAEnabled(boolean pdfAEnabled) { this.pdfAEnabled = pdfAEnabled; }

    public boolean isPdfUAEnabled() { return pdfUAEnabled; }
    public void setPdfUAEnabled(boolean pdfUAEnabled) { this.pdfUAEnabled = pdfUAEnabled; }

    public String getDocumentTitle() { return documentTitle; }
    public void setDocumentTitle(String documentTitle) { this.documentTitle = documentTitle; }

    public String getDocumentAuthor() { return documentAuthor; }
    public void setDocumentAuthor(String documentAuthor) { this.documentAuthor = documentAuthor; }

    public String getDocumentSubject() { return documentSubject; }
    public void setDocumentSubject(String documentSubject) { this.documentSubject = documentSubject; }

    public String getDocumentLanguage() { return documentLanguage; }
    public void setDocumentLanguage(String documentLanguage) { this.documentLanguage = documentLanguage; }

    public String getPdfALevel() { return pdfALevel; }
    public void setPdfALevel(String pdfALevel) { this.pdfALevel = pdfALevel; }

    public String getFontDirectory() { return fontDirectory; }
    public void setFontDirectory(String fontDirectory) { this.fontDirectory = fontDirectory; }
}