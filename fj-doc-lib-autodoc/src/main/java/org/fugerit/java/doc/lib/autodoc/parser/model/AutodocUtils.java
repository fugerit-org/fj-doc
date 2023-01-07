package org.fugerit.java.doc.lib.autodoc.parser.model;

import org.xmlet.xsdparser.xsdelements.XsdAnnotation;
import org.xmlet.xsdparser.xsdelements.XsdDocumentation;

public class AutodocUtils {

	public static String annotationAsSingleStringHelper( XsdAnnotation xsdAnnotation ) {
		StringBuilder annotation = new StringBuilder();
		if ( xsdAnnotation != null ) {
			boolean start = false;
			for ( XsdDocumentation xsdDocumentation : xsdAnnotation.getDocumentations() ) {
				if ( start ) {
					start = true;
				} else {
					annotation.append( " " );
				}
				annotation.append( xsdDocumentation.getContent() );
			}
		}
		return annotation.toString();
	}
	
}
