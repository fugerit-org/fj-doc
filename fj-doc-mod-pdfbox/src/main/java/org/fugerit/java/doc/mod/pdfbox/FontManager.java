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
    private final Map<String, PDFont> fontCache;

    // Font style constants from DocPara
    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_BOLDITALIC = 3;
    public static final int STYLE_UNDERLINE = 4;

    public FontManager(PdfBoxConfig config, PDDocument document) {
        this.config = config;
        this.document = document;
        this.fontCache = new HashMap<>();
    }

    /**
     * Get font based on style code and optional font name
     * @param style Style code (0=normal, 1=bold, 2=italic, 3=bolditalic, 4=underline)
     * @param fontName Font name (times-roman, courier, symbol, helvetica, or null for default)
     * @return PDFont instance
     */
    public PDFont getFont(int style, String fontName) throws IOException {
        // Create cache key
        String cacheKey = (fontName != null ? fontName : "helvetica") + "-" + style;

        if (fontCache.containsKey(cacheKey)) {
            return fontCache.get(cacheKey);
        }

        PDFont font;

        // For PDF/A compliance, fonts must be embedded
        if (config.isPdfAEnabled() || config.isPdfUAEnabled()) {
            font = getEmbeddedFont(style, fontName);
        } else {
            font = getStandardFont(style, fontName);
        }

        fontCache.put(cacheKey, font);
        return font;
    }

    /**
     * Get font with just style code (uses default helvetica)
     */
    public PDFont getFont(int style) throws IOException {
        return getFont(style, null);
    }

    private PDFont getStandardFont(int style, String fontName) throws IOException {
        // Normalize font name
        String normalizedFontName = normalizeFontName(fontName);

        // Map to Standard 14 fonts
        Standard14Fonts.FontName baseFontName = getBaseFontName(normalizedFontName);

        // Apply style
        Standard14Fonts.FontName styledFontName = applyStyle(baseFontName, style);

        log.debug("Using standard font: {} (requested: {}, style: {})", styledFontName, fontName, style);

        return new PDType1Font(styledFontName);
    }

    private String normalizeFontName(String fontName) {
        if (fontName == null || fontName.isEmpty()) {
            return "helvetica";
        }
        // Normalize font name (lowercase, remove hyphens)
        return fontName.toLowerCase().replace("-", "").replace("_", "");
    }

    private Standard14Fonts.FontName getBaseFontName(String normalizedFontName) {
        // Map Venus Doc font names to Standard 14 fonts
        if (normalizedFontName.contains("times") || normalizedFontName.contains("roman")) {
            return Standard14Fonts.FontName.TIMES_ROMAN;
        } else if (normalizedFontName.contains("courier")) {
            return Standard14Fonts.FontName.COURIER;
        } else if (normalizedFontName.contains("symbol")) {
            return Standard14Fonts.FontName.SYMBOL;
        } else {
            // Default to Helvetica
            return Standard14Fonts.FontName.HELVETICA;
        }
    }

    private Standard14Fonts.FontName applyStyle(Standard14Fonts.FontName baseFont, int style) {
        // Symbol font doesn't have styles
        if (baseFont == Standard14Fonts.FontName.SYMBOL ||
                baseFont == Standard14Fonts.FontName.ZAPF_DINGBATS) {
            return baseFont;
        }

        // Apply style based on base font family
        if (baseFont == Standard14Fonts.FontName.TIMES_ROMAN ||
                baseFont == Standard14Fonts.FontName.TIMES_BOLD ||
                baseFont == Standard14Fonts.FontName.TIMES_ITALIC ||
                baseFont == Standard14Fonts.FontName.TIMES_BOLD_ITALIC) {
            // Times family
            switch (style) {
                case STYLE_BOLD:
                    return Standard14Fonts.FontName.TIMES_BOLD;
                case STYLE_ITALIC:
                    return Standard14Fonts.FontName.TIMES_ITALIC;
                case STYLE_BOLDITALIC:
                    return Standard14Fonts.FontName.TIMES_BOLD_ITALIC;
                default:
                    return Standard14Fonts.FontName.TIMES_ROMAN;
            }
        } else if (baseFont == Standard14Fonts.FontName.COURIER ||
                baseFont == Standard14Fonts.FontName.COURIER_BOLD ||
                baseFont == Standard14Fonts.FontName.COURIER_OBLIQUE ||
                baseFont == Standard14Fonts.FontName.COURIER_BOLD_OBLIQUE) {
            // Courier family
            switch (style) {
                case STYLE_BOLD:
                    return Standard14Fonts.FontName.COURIER_BOLD;
                case STYLE_ITALIC:
                    return Standard14Fonts.FontName.COURIER_OBLIQUE;
                case STYLE_BOLDITALIC:
                    return Standard14Fonts.FontName.COURIER_BOLD_OBLIQUE;
                default:
                    return Standard14Fonts.FontName.COURIER;
            }
        } else {
            // Helvetica family (default)
            switch (style) {
                case STYLE_BOLD:
                    return Standard14Fonts.FontName.HELVETICA_BOLD;
                case STYLE_ITALIC:
                    return Standard14Fonts.FontName.HELVETICA_OBLIQUE;
                case STYLE_BOLDITALIC:
                    return Standard14Fonts.FontName.HELVETICA_BOLD_OBLIQUE;
                default:
                    return Standard14Fonts.FontName.HELVETICA;
            }
        }
    }

    private PDFont getEmbeddedFont(int style, String fontName) throws IOException {
        // For PDF/A, ALL fonts must be embedded
        // Try to load custom font from configured directory
        if (config.getFontDirectory() != null) {
            String fontFileName = getFontFileName(style, fontName);
            File fontFile = new File(config.getFontDirectory(), fontFileName);
            if (fontFile.exists()) {
                log.info("Loading embedded font from file: {}", fontFile.getAbsolutePath());
                return PDType0Font.load(document, fontFile);
            }
        }

        // Fallback: For now use standard font
        // TODO: Include embedded fonts in resources for full PDF/A compliance
        log.warn("Embedded font not found, using standard font (not PDF/A compliant)");
        return getStandardFont(style, fontName);
    }

    private String getFontFileName(int style, String fontName) {
        String baseName = (fontName != null && !fontName.isEmpty()) ? fontName : "default";

        // Map style to font file suffix
        switch (style) {
            case STYLE_BOLD:
                return baseName + "-bold.ttf";
            case STYLE_ITALIC:
                return baseName + "-italic.ttf";
            case STYLE_BOLDITALIC:
                return baseName + "-bolditalic.ttf";
            default:
                return baseName + "-regular.ttf";
        }
    }

    public float getFontSize(int style) {
        // Default font size - can be made configurable
        log.debug("Get font size [{}]", style);
        return 12f;
    }
}