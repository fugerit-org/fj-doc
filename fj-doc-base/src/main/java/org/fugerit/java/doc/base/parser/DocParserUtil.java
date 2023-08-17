package org.fugerit.java.doc.base.parser;

import java.util.Properties;

public class DocParserUtil {

	public static String doubleNested( Properties atts, String key1, String key2 ) {
		return atts.getProperty( key1, atts.getProperty( key2 ) );
	}
	
	public static int doubleNestedWithDefaultInt( Properties atts, String key1, String key2, String defValue ) {
		return Integer.valueOf( atts.getProperty( key1, atts.getProperty( key2, defValue ) ) );
	}
	
}
