package org.fugerit.java.doc.mod.pdfbox;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class PdfBoxConfig {

    private boolean pdfAEnabled = false;
    private boolean pdfUAEnabled = false;
    private String documentTitle = "Untitled";
    private String documentAuthor = "Unknown";
    private String documentSubject = "";
    private String documentLanguage = "en-US";
    private String fontDirectory = null;
    private String fontClasspath = null;
    private String defaultFontName = null;
    private final Map<String, String> customFonts = new HashMap<>();

    // PDF/A conformance level (A1b, A2b, A3b)
    private String pdfALevel = "1b";

    public static final String PDFA_ENABLED = "pdfaEnabled";
    public static final String PDFUA_ENABLED = "pdfuaEnabled";
    public static final String PDFA_LEVEL = "pdfa.level";
    public static final String PDFA_LEVEL_ALT = "pdfaLevel";
    public static final String TITLE = "title";
    public static final String SUBJECT = "subject";
    public static final String AUTHOR = "author";
    public static final String LANGUAGE = "language";
    public static final String FONT_DIRECTORY = "font.directory";
    public static final String FONT_DIRECTORY_ALT = "fontDirectory";
    public static final String FONT_CLASSPATH = "font.classpath";
    public static final String FONT_CLASSPATH_ALT = "fontClasspath";
    public static final String DEFAULT_FONT_NAME = "font.default";
    public static final String DEFAULT_FONT_NAME_ALT = "defaultFontName";
    public static final String FORMAT = "format";

    public static final String FONT_STYLE_REGULAR = "regular";
    public static final String FONT_STYLE_BOLD = "bold";
    public static final String FONT_STYLE_ITALIC = "italic";
    public static final String FONT_STYLE_BOLDITALIC = "bolditalic";

    public PdfBoxConfig() {
        // default constructor
    }

    public PdfBoxConfig(PdfBoxConfig other) {
        if (other != null) {
            this.pdfAEnabled = other.pdfAEnabled;
            this.pdfUAEnabled = other.pdfUAEnabled;
            this.documentTitle = other.documentTitle;
            this.documentAuthor = other.documentAuthor;
            this.documentSubject = other.documentSubject;
            this.documentLanguage = other.documentLanguage;
            this.fontDirectory = other.fontDirectory;
            this.fontClasspath = other.fontClasspath;
            this.defaultFontName = other.defaultFontName;
            this.pdfALevel = other.pdfALevel;
            this.customFonts.putAll(other.customFonts);
        }
    }

    public PdfBoxConfig copy() {
        return new PdfBoxConfig(this);
    }

    private static String getPropertyWithFallback(Properties props, String primaryKey, String altKey, String defaultValue) {
        String value = props.getProperty(primaryKey);
        if (value == null && altKey != null) {
            value = props.getProperty(altKey);
        }
        return value != null ? value : defaultValue;
    }

    public static PdfBoxConfig fromProperties(Properties props) {
        PdfBoxConfig config = new PdfBoxConfig();

        config.setPdfAEnabled(Boolean.parseBoolean(
                props.getProperty(PDFA_ENABLED, "false")));
        config.setPdfUAEnabled(Boolean.parseBoolean(
                props.getProperty(PDFUA_ENABLED, "false")));
        config.setPdfALevel(getPropertyWithFallback(props, PDFA_LEVEL, PDFA_LEVEL_ALT, "1b"));
        config.setDocumentTitle(props.getProperty(TITLE, "Untitled"));
        config.setDocumentAuthor(props.getProperty(AUTHOR, "Unknown"));
        config.setDocumentSubject(props.getProperty(SUBJECT, ""));
        config.setDocumentLanguage(props.getProperty(LANGUAGE, "en-US"));
        config.setFontDirectory(getPropertyWithFallback(props, FONT_DIRECTORY, FONT_DIRECTORY_ALT, null));
        config.setFontClasspath(getPropertyWithFallback(props, FONT_CLASSPATH, FONT_CLASSPATH_ALT, null));
        config.setDefaultFontName(getPropertyWithFallback(props, DEFAULT_FONT_NAME, DEFAULT_FONT_NAME_ALT, null));

        return config;
    }

    public static String normalizeFontStyleKey(String style) {
        if (style == null || style.trim().isEmpty()) {
            return FONT_STYLE_REGULAR;
        }
        String normalized = style.trim().toLowerCase(Locale.ROOT).replace("-", "").replace("_", "");
        if (FONT_STYLE_BOLD.equals(normalized)) {
            return FONT_STYLE_BOLD;
        } else if (FONT_STYLE_ITALIC.equals(normalized) || "oblique".equals(normalized)) {
            return FONT_STYLE_ITALIC;
        } else if (FONT_STYLE_BOLDITALIC.equals(normalized) || "boldoblique".equals(normalized)) {
            return FONT_STYLE_BOLDITALIC;
        }
        return FONT_STYLE_REGULAR;
    }

    public static String buildCustomFontKey(String fontName, String style) {
        return fontName.trim().toLowerCase(Locale.ROOT) + ":" + normalizeFontStyleKey(style);
    }

    public void registerCustomFont(String fontName, String style, String path) {
        if (fontName != null && !fontName.trim().isEmpty() && path != null && !path.trim().isEmpty()) {
            this.customFonts.put(buildCustomFontKey(fontName, style), path.trim());
        }
    }

    public void registerCustomFont(String fontName, String path) {
        this.registerCustomFont(fontName, FONT_STYLE_REGULAR, path);
    }

    public String getCustomFontPath(String fontName, String style) {
        if (fontName == null || fontName.trim().isEmpty()) {
            return null;
        }
        return this.customFonts.get(buildCustomFontKey(fontName, style));
    }

    public Map<String, String> getCustomFonts() {
        return Collections.unmodifiableMap(this.customFonts);
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

    public String getFontClasspath() { return fontClasspath; }
    public void setFontClasspath(String fontClasspath) { this.fontClasspath = fontClasspath; }

    public String getDefaultFontName() { return defaultFontName; }
    public void setDefaultFontName(String defaultFontName) { this.defaultFontName = defaultFontName; }
}
