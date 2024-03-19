package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.util.List;
import java.util.stream.Collectors;

import org.fugerit.java.core.lang.helpers.StringUtils;
import org.xmlet.xsdparser.xsdelements.XsdAnnotation;
import org.xmlet.xsdparser.xsdelements.XsdDocumentation;
import org.xmlet.xsdparser.xsdelements.XsdNamedElements;
import org.xmlet.xsdparser.xsdelements.XsdRestriction;
import org.xmlet.xsdparser.xsdelements.xsdrestrictions.XsdEnumeration;
import org.xmlet.xsdparser.xsdelements.xsdrestrictions.XsdPattern;

public class AutodocUtils {

	private AutodocUtils() {}

	private static final String EMPTY = "";

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
		return ( current.getXsdSchema() != null ? current.getXsdSchema().getTargetNamespace()+"_" : "" )+current.getRawName();
	}


	private static String parseBaseType( String xsdPrefix, String autodocPrefix, String type ) {
		String baseType = type;
		if ( type.startsWith( xsdPrefix ) ) {
			baseType = type.replace( xsdPrefix , EMPTY );
		} else if ( type.startsWith( autodocPrefix ) ) {
			baseType = type.replace( autodocPrefix , EMPTY );
		}
		return baseType;
	}

	private static void handleBaseRestriction( StringBuilder builder, XsdRestriction xsdRestriction, String xsdPrefix, String autodocPrefix ) {
		if ( xsdRestriction.getBase() != null ) {
			builder.append( " , base : " );
			builder.append( parseBaseType( xsdPrefix, autodocPrefix, xsdRestriction.getBase() ) );
		}
	}

	private static void handleLenthRestrictions( StringBuilder builder, XsdRestriction xsdRestriction ) {
		if ( xsdRestriction.getMinLength() != null ) {
			builder.append( " , minLength : " );
			builder.append( xsdRestriction.getMinLength().getValue() );
		}
		if ( xsdRestriction.getMaxLength() != null ) {
			builder.append( " , maxLength : " );
			builder.append( xsdRestriction.getMaxLength().getValue() );
		}
	}

	private static void handleMinMaxInclusiveRestrictions( StringBuilder builder, XsdRestriction xsdRestriction ) {
		if ( xsdRestriction.getMinInclusive() != null ) {
			builder.append( " , minInclusive : " );
			builder.append( xsdRestriction.getMinInclusive().getValue() );
		}
		if ( xsdRestriction.getMaxInclusive() != null ) {
			builder.append( " , maxInclusive : " );
			builder.append( xsdRestriction.getMaxInclusive().getValue() );
		}
	}

	private static void handleMinMaxExclusiveRestrictions( StringBuilder builder, XsdRestriction xsdRestriction ) {
		if ( xsdRestriction.getMinExclusive() != null ) {
			builder.append( " , minExclusive : " );
			builder.append( xsdRestriction.getMinExclusive().getValue() );
		}
		if ( xsdRestriction.getMaxExclusive() != null ) {
			builder.append( " , maxExclusive : " );
			builder.append( xsdRestriction.getMaxExclusive().getValue() );
		}
	}

	private static void handlePatternRestriction( StringBuilder builder, XsdRestriction xsdRestriction ) {
		List<XsdPattern> patterns = xsdRestriction.getPatterns();
		if ( patterns != null && !patterns.isEmpty() ) {
			builder.append( " , pattern : " );
			builder.append( StringUtils.concat( ", ", patterns.stream().map(  xp -> xp.getValue() ).collect( Collectors.toList() ) ) );
		}
	}

	private static void handleEnumerationRestriction( StringBuilder builder, XsdRestriction xsdRestriction ) {
		List<XsdEnumeration> enumeratios = xsdRestriction.getEnumeration();
		if ( enumeratios != null && !enumeratios.isEmpty() ) {
			builder.append( " , enumeration : [ " );
			builder.append( StringUtils.concat( " , " , enumeratios.stream().map( XsdEnumeration::getValue ).collect( Collectors.toList() ) ) );
			builder.append( " ]" );
		}
	}

	public static String toNoteNoBase( AutodocModel model, List<XsdRestriction> restrictions ) {
		return toNote( null, model, restrictions );
	}

	public static String toNote( String type, AutodocModel model, List<XsdRestriction> restrictions ) {
		StringBuilder builder = new StringBuilder();
		if ( type != null ) {
			String baseType = parseBaseType( model.getXsdPrefix(), model.getAutodocPrefix(), type );
			builder.append( baseType );
		}
		if ( restrictions != null && !restrictions.isEmpty() ) {
			for ( XsdRestriction xsdRestriction : restrictions ) {
				if ( type != null ) {
					handleBaseRestriction(builder, xsdRestriction, model.getXsdPrefix(), model.getAutodocPrefix());
				}
				handleLenthRestrictions(builder, xsdRestriction);
				handleMinMaxInclusiveRestrictions(builder, xsdRestriction);
				handleMinMaxExclusiveRestrictions(builder, xsdRestriction);
				handlePatternRestriction(builder, xsdRestriction);
				handleEnumerationRestriction(builder, xsdRestriction);
			}
		}
		return builder.toString();
	}

	public static String getBaseName( List<XsdRestriction> restrictions, AutodocModel autodocModel ) {
		String res = null;
		for ( XsdRestriction xsdRestriction : restrictions ) {
			if ( xsdRestriction.getBase() != null ) {
				res = parseBaseType( autodocModel.getXsdPrefix(), autodocModel.getAutodocPrefix(), xsdRestriction.getBase() );
			}
		}
		return res;
	}

}
