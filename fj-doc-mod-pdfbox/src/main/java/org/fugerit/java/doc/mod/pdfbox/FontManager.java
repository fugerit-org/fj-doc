package org.fugerit.java.doc.mod.pdfbox;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.lang.helpers.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
public class FontManager {

    private static final String DEFAULT_EMBEDDED_FONT_RESOURCE = "org/apache/pdfbox/resources/ttf/LiberationSans-Regular.ttf";

    private static final String[] CLASSPATH_PREFIXES = { "classloader://", "classpath://", "cl://" };

    private static final List<String> SUFFIXES_REGULAR = Arrays.asList("-Regular.ttf", "-regular.ttf", ".ttf");
    private static final List<String> SUFFIXES_BOLD = Arrays.asList("-Bold.ttf", "-bold.ttf");
    private static final List<String> SUFFIXES_ITALIC = Arrays.asList("-Italic.ttf", "-italic.ttf", "-Oblique.ttf", "-oblique.ttf");
    private static final List<String> SUFFIXES_BOLDITALIC = Arrays.asList("-BoldItalic.ttf", "-bolditalic.ttf", "-BoldOblique.ttf", "-boldoblique.ttf");

    private final PdfBoxConfig config;
    private final PDDocument document;
    private final Map<String, PDFont> fontCache;
    private final Map<String, PDFont> resourceFontCache;
    private PDFont defaultEmbeddedFont;

    // Font style constants matching DocPara / EnumDocStyle (1=normal, 2=bold, 3=underline, 4=italic, 5=bolditalic)
    public static final int STYLE_NORMAL = 1;
    public static final int STYLE_BOLD = 2;
    public static final int STYLE_UNDERLINE = 3;
    public static final int STYLE_ITALIC = 4;
    public static final int STYLE_BOLDITALIC = 5;
    public static final int STYLE_UNSET = -1;

    public static final float DEFAULT_FONT_SIZE = 10f;

    public FontManager(PdfBoxConfig config, PDDocument document) {
        this.config = config;
        this.document = document;
        this.fontCache = new HashMap<>();
        this.resourceFontCache = new HashMap<>();
    }

    /**
     * Get font based on style code and optional font name
     * @param style Style code (1=normal, 2=bold, 3=underline, 4=italic, 5=bolditalic)
     * @param fontName Font name (times-roman, courier, symbol, helvetica, custom family, or null for default)
     * @return PDFont instance
     */
    public PDFont getFont(int style, String fontName) throws IOException {
        // Create cache key
        String cacheKey = (fontName != null ? fontName : "helvetica") + "-" + style;

        if (fontCache.containsKey(cacheKey)) {
            return fontCache.get(cacheKey);
        }

        PDFont font;

        // For PDF/A and PDF/UA compliance, fonts must be embedded
        if (config.isPdfAEnabled() || config.isPdfUAEnabled()) {
            font = getEmbeddedFont(style, fontName);
        } else if (hasCustomFontConfig()) {
            PDFont customFont = loadCustomEmbeddedFont(style, fontName);
            font = customFont != null ? customFont : getStandardFont(style, fontName);
        } else {
            font = getStandardFont(style, fontName);
        }

        fontCache.put(cacheKey, font);
        return font;
    }

    /**
     * Get font with just style code (uses default font)
     */
    public PDFont getFont(int style) throws IOException {
        return getFont(style, null);
    }

    private boolean hasCustomFontConfig() {
        return StringUtils.isNotEmpty(config.getFontClasspath())
                || StringUtils.isNotEmpty(config.getFontDirectory())
                || StringUtils.isNotEmpty(config.getDefaultFontName())
                || !config.getCustomFonts().isEmpty();
    }

    private PDFont getStandardFont(int style, String fontName) {
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
        return fontName.toLowerCase(Locale.ROOT).replace("-", "").replace("_", "");
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
        // For PDF/A and PDF/UA, ALL fonts must be embedded
        PDFont customFont = loadCustomEmbeddedFont(style, fontName);
        if (customFont != null) {
            return customFont;
        }

        if (defaultEmbeddedFont == null) {
            defaultEmbeddedFont = loadClasspathFont(DEFAULT_EMBEDDED_FONT_RESOURCE);
        }
        if (defaultEmbeddedFont != null) {
            return defaultEmbeddedFont;
        }

        log.warn("Embedded font not found, using standard font (not PDF/A compliant)");
        return getStandardFont(style, fontName);
    }

    private PDFont loadCustomEmbeddedFont(int style, String fontName) throws IOException {
        int effectiveStyle = normalizeStyle(style);
        List<String> familyCandidates = buildFamilyCandidates(fontName);

        PDFont font = findCustomFontForStyle(familyCandidates, effectiveStyle);
        if (font == null && effectiveStyle != STYLE_NORMAL) {
            font = findCustomFontForStyle(familyCandidates, STYLE_NORMAL);
        }
        return font;
    }

    private List<String> buildFamilyCandidates(String fontName) {
        List<String> candidates = new ArrayList<>();
        if (StringUtils.isNotEmpty(fontName)) {
            candidates.add(fontName.trim());
        }
        String defaultFamily = config.getDefaultFontName();
        if (StringUtils.isNotEmpty(defaultFamily)) {
            String trimmedDefault = defaultFamily.trim();
            boolean alreadyPresent = false;
            for (String existing : candidates) {
                if (existing.equalsIgnoreCase(trimmedDefault)) {
                    alreadyPresent = true;
                    break;
                }
            }
            if (!alreadyPresent) {
                candidates.add(trimmedDefault);
            }
        }
        if (candidates.isEmpty()) {
            candidates.add("default");
        }
        return candidates;
    }

    private PDFont findCustomFontForStyle(List<String> familyCandidates, int effectiveStyle) throws IOException {
        String styleKey = toStyleKey(effectiveStyle);
        List<String> suffixes = getFontFileSuffixes(effectiveStyle);
        String classpathPrefix = resolveClasspathPrefix();
        String fsDir = resolveFilesystemFontDirectory();

        for (String family : familyCandidates) {
            PDFont explicitFont = findExplicitCustomFont(family, styleKey);
            if (explicitFont != null) {
                return explicitFont;
            }
            PDFont cpFont = findClasspathFontForFamily(classpathPrefix, family, suffixes);
            if (cpFont != null) {
                return cpFont;
            }
            PDFont fsFont = findFilesystemFontForFamily(fsDir, family, suffixes);
            if (fsFont != null) {
                return fsFont;
            }
        }
        return null;
    }

    private PDFont findExplicitCustomFont(String family, String styleKey) throws IOException {
        String registeredPath = config.getCustomFontPath(family, styleKey);
        if (StringUtils.isNotEmpty(registeredPath)) {
            return loadFontFromPath(registeredPath);
        }
        return null;
    }

    private PDFont findClasspathFontForFamily(String classpathPrefix, String family, List<String> suffixes) throws IOException {
        if (classpathPrefix == null) {
            return null;
        }
        for (String suffix : suffixes) {
            PDFont font = loadClasspathFont(classpathPrefix + family + suffix);
            if (font != null) {
                return font;
            }
        }
        return null;
    }

    private PDFont findFilesystemFontForFamily(String fsDir, String family, List<String> suffixes) throws IOException {
        if (fsDir == null) {
            return null;
        }
        for (String suffix : suffixes) {
            PDFont font = loadFileFont(new File(fsDir, family + suffix));
            if (font != null) {
                return font;
            }
        }
        return null;
    }

    private PDFont loadFontFromPath(String path) throws IOException {
        if (isClasspathUri(path)) {
            return loadClasspathFont(stripLeadingSlash(stripClasspathPrefix(path)));
        }
        PDFont cpFont = loadClasspathFont(stripLeadingSlash(path));
        if (cpFont != null) {
            return cpFont;
        }
        return loadFileFont(new File(path));
    }

    private PDFont loadClasspathFont(String resourcePath) throws IOException {
        String key = "cp:" + resourcePath;
        if (resourceFontCache.containsKey(key)) {
            return resourceFontCache.get(key);
        }
        try (InputStream is = ClassHelper.loadFromDefaultClassLoader(resourcePath)) {
            if (is != null) {
                log.info("Loading embedded font from classpath: {}", resourcePath);
                PDFont font = PDType0Font.load(document, is);
                resourceFontCache.put(key, font);
                return font;
            }
        }
        return null;
    }

    private PDFont loadFileFont(File fontFile) throws IOException {
        String absPath = fontFile.getAbsolutePath();
        String key = "file:" + absPath;
        if (resourceFontCache.containsKey(key)) {
            return resourceFontCache.get(key);
        }
        if (fontFile.exists() && fontFile.isFile()) {
            log.info("Loading embedded font from file: {}", absPath);
            PDFont font = PDType0Font.load(document, fontFile);
            resourceFontCache.put(key, font);
            return font;
        }
        return null;
    }

    private String resolveClasspathPrefix() {
        if (StringUtils.isNotEmpty(config.getFontClasspath())) {
            return normalizeClasspathPrefix(config.getFontClasspath());
        }
        if (isClasspathUri(config.getFontDirectory())) {
            return normalizeClasspathPrefix(config.getFontDirectory());
        }
        return null;
    }

    private String resolveFilesystemFontDirectory() {
        String dir = config.getFontDirectory();
        if (StringUtils.isNotEmpty(dir) && !isClasspathUri(dir)) {
            return dir;
        }
        return null;
    }

    private static boolean isClasspathUri(String path) {
        if (path == null) {
            return false;
        }
        String lower = path.trim().toLowerCase(Locale.ROOT);
        for (String prefix : CLASSPATH_PREFIXES) {
            if (lower.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private static String stripClasspathPrefix(String path) {
        String trimmed = path.trim();
        String lower = trimmed.toLowerCase(Locale.ROOT);
        for (String prefix : CLASSPATH_PREFIXES) {
            if (lower.startsWith(prefix)) {
                return trimmed.substring(prefix.length());
            }
        }
        return trimmed;
    }

    private static String stripLeadingSlash(String path) {
        String result = path.trim();
        while (result.startsWith("/")) {
            result = result.substring(1);
        }
        return result;
    }

    private static String normalizeClasspathPrefix(String rawPrefix) {
        String cleaned = stripLeadingSlash(stripClasspathPrefix(rawPrefix));
        if (!cleaned.isEmpty() && !cleaned.endsWith("/")) {
            cleaned = cleaned + "/";
        }
        return cleaned;
    }

    private static int normalizeStyle(int style) {
        switch (style) {
            case STYLE_BOLD:
            case STYLE_ITALIC:
            case STYLE_BOLDITALIC:
                return style;
            default:
                return STYLE_NORMAL;
        }
    }

    private static String toStyleKey(int effectiveStyle) {
        switch (effectiveStyle) {
            case STYLE_BOLD:
                return PdfBoxConfig.FONT_STYLE_BOLD;
            case STYLE_ITALIC:
                return PdfBoxConfig.FONT_STYLE_ITALIC;
            case STYLE_BOLDITALIC:
                return PdfBoxConfig.FONT_STYLE_BOLDITALIC;
            default:
                return PdfBoxConfig.FONT_STYLE_REGULAR;
        }
    }

    private static List<String> getFontFileSuffixes(int effectiveStyle) {
        switch (effectiveStyle) {
            case STYLE_BOLD:
                return SUFFIXES_BOLD;
            case STYLE_ITALIC:
                return SUFFIXES_ITALIC;
            case STYLE_BOLDITALIC:
                return SUFFIXES_BOLDITALIC;
            default:
                return SUFFIXES_REGULAR;
        }
    }

    public float getFontSize(int style) {
        log.trace("Get font size [{}]", style);
        return DEFAULT_FONT_SIZE;
    }

    public float getFontSize(int style, int size) {
        log.trace("size : {}, style is currently ignored : {}", size, style);
        return size > 0 ? (float) size : DEFAULT_FONT_SIZE;
    }
}
