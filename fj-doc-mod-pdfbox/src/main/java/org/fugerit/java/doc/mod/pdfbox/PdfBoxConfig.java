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

    public static PdfBoxConfig fromProperties(Properties props) {
        PdfBoxConfig config = new PdfBoxConfig();

        config.pdfAEnabled = Boolean.parseBoolean(
                props.getProperty("pdfa.enabled", "false"));
        config.pdfUAEnabled = Boolean.parseBoolean(
                props.getProperty("pdfua.enabled", "false"));
        config.pdfALevel = props.getProperty("pdfa.level", "1b");

        config.documentTitle = props.getProperty("document.title", "Untitled");
        config.documentAuthor = props.getProperty("document.author", "Unknown");
        config.documentSubject = props.getProperty("document.subject", "");
        config.documentLanguage = props.getProperty("document.language", "en-US");
        config.fontDirectory = props.getProperty("font.directory", null);

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