package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.util.stream.Collectors;

import org.fugerit.java.core.lang.helpers.StringUtils;
import org.xmlet.xsdparser.xsdelements.XsdAnnotation;

public class AutodocUtils {

	public static String annotationAsSingleStringHelper( XsdAnnotation xsdAnnotation ) {
		String annotation = null;
		if ( xsdAnnotation != null ) {
			annotation = StringUtils.concat( " ", xsdAnnotation.getDocumentations().stream().map( current -> { return current.getContent(); } ).collect( Collectors.toList() ) );
		} else {
			annotation = "";
		}
		return annotation;
	}
	
}
