package org.fugerit.java.doc.base.xml;

import java.util.regex.Pattern;

public class DocXMLUtils {

    private DocXMLUtils() {}

    public static final String DEFAULT_CLEAN_XML_REGEX = "[^\\u0009\\u000A\\u000D\\u0020-\\uD7FF\\uE000-\\uFFFD\\u10000-\\u10FFF]+";

    /**
     * Clean the input text with the default clean xml regext
     *
     * @param input     the input text
     * @return          the cleaned text
     */
    public static String cleanXML( String input ) {
        return cleanText( input, DEFAULT_CLEAN_XML_REGEX );
    }

    /**
     * Clean the input text with a give regex
     *
     * @param input         the input text
     * @param cleanRegex    the regex used to clean (everything matching it will be replaced by a "")
     * @return              the cleaned text
     */
    public static String cleanText( String input, String cleanRegex ) {
        Pattern p = Pattern.compile( cleanRegex );
        return p.matcher( input ).replaceAll("");
    }

}
