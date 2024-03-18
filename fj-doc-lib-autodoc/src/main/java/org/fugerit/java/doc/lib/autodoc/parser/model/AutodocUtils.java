package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.util.stream.Collectors;

import org.fugerit.java.core.lang.helpers.StringUtils;
import org.xmlet.xsdparser.xsdelements.XsdAnnotation;
import org.xmlet.xsdparser.xsdelements.XsdDocumentation;
import org.xmlet.xsdparser.xsdelements.XsdNamedElements;

public class AutodocUtils {

	private AutodocUtils() {}
	
	public static String annotationAsSingleStringHelper( XsdAnnotation xsdAnnotation ) {
		String annotation = null;
		if ( xsdAnnotation != null ) {
			annotation = StringUtils.concat( " ", xsdAnnotation.getDocumentations().stream().map( XsdDocumentation::getContent ).collect( Collectors.toList() ) );
		} else {
			annotation = "";
		}
		return annotation;
	}

	public static String toKey(XsdNamedElements current) {
		return current.getXsdSchema().getTargetNamespace()+"_"+current.getRawName();
	}
	
}
