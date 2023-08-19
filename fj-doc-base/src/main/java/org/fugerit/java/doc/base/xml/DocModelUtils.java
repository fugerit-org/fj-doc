package org.fugerit.java.doc.base.xml;

import java.awt.Color;

import org.fugerit.java.core.util.BinaryCalc;

public class DocModelUtils {

	private DocModelUtils() {}
	
	public static Color parseHtmlColor( String c ) {
		int r = (int)BinaryCalc.hexToLong( c.substring( 1, 3 ) );
		int g = (int)BinaryCalc.hexToLong( c.substring( 3, 5 ) );
		int b = (int)BinaryCalc.hexToLong( c.substring( 5, 7 ) );
		return new Color( r, g, b );
	}
	
}
