package org.fugerit.java.doc.base.helper;

import org.fugerit.java.core.function.SafeFunction;

public class TextWrapHelper {

    private TextWrapHelper() {}

    public static final String ZERO_WITH_SPACE = "&#8203;";

    public static String padZeroWithSpace( String input ) {
        return SafeFunction.getIfNotNull( input, () -> {
            StringBuilder builder = new StringBuilder();
            for( int i=0; i<input.length(); i++ ) {
                builder.append( input.charAt( i ) );
                builder.append( ZERO_WITH_SPACE );
            }
            return builder.toString();
        } );
    }

}
