package org.fugerit.java.doc.mod.pdfbox;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FontManager {

    private final PdfBoxConfig config;
    private final PDDocument document;
    private final Map<Integer, PDFont> fontCache;

    public FontManager(PdfBoxConfig config, PDDocument document) {
        this.config = config;
        this.document = document;
        this.fontCache = new HashMap<>();
    }

    public PDFont getFont(int style) throws IOException {
        // For PDF/A compliance, fonts must be embedded
        if (config.isPdfAEnabled() || config.isPdfUAEnabled()) {
            return getEmbeddedFont(style);
        }

        // Use standard fonts for regular PDFs
        return getStandardFont(style);
    }

    private PDFont getStandardFont(int style) throws IOException {
        if (fontCache.containsKey(style)) {
            return fontCache.get(style);
        }

        PDFont font;

        // Map styles to fonts (you can expand this)
        switch (style) {
            case 1: // Bold
                font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                break;
            case 2: // Italic
                font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE);
                break;
            case 3: // Bold Italic
                font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD_OBLIQUE);
                break;
            default:
                font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        }

        fontCache.put(style, font);
        return font;
    }

    private PDFont getEmbeddedFont(int style) throws IOException {
        if (fontCache.containsKey(style)) {
            return fontCache.get(style);
        }

        // Load custom font or use a default embedded font
        // For PDF/A, ALL fonts must be embedded
        PDFont font = loadCustomFont(style);
        fontCache.put(style, font);
        return font;
    }

    private PDFont loadCustomFont(int style) throws IOException {
        // Try to load TrueType fonts from configured directory
        if (config.getFontDirectory() != null) {
            String fontFileName = getFontFileName(style);
            File fontFile = new File(config.getFontDirectory(), fontFileName);
            if (fontFile.exists()) {
                log.info("Loading font from file: {}", fontFile.getAbsolutePath());
                return PDType0Font.load(document, fontFile);
            }
        }

        // Fallback: For now use standard font
        // TODO: Include embedded fonts in resources for PDF/A compliance
        log.warn("Custom font not found, using standard font (not PDF/A compliant)");
        return getStandardFont(style);
    }

    private String getFontFileName(int style) {
        // Map style codes to font file names
        switch (style) {
            case 1:
                return "font-bold.ttf";
            case 2:
                return "font-italic.ttf";
            case 3:
                return "font-bolditalic.ttf";
            default:
                return "font-regular.ttf";
        }
    }

    public float getFontSize(int style) {
        // You can make this configurable or based on style
        return 12f;
    }
}