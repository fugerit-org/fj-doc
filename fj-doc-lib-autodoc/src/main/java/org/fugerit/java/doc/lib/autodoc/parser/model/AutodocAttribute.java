package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.util.List;
import java.util.stream.Collectors;

import org.fugerit.java.core.lang.helpers.StringUtils;
import org.xmlet.xsdparser.xsdelements.XsdAnnotation;
import org.xmlet.xsdparser.xsdelements.XsdAttribute;
import org.xmlet.xsdparser.xsdelements.XsdRestriction;
import org.xmlet.xsdparser.xsdelements.xsdrestrictions.XsdEnumeration;

public class AutodocAttribute {

	private XsdAttribute xsdAttribute;

	private AutodocModel autodocModel;
	
	private static final String EMPTY = "";
	
	public AutodocAttribute(XsdAttribute xsdAttribute, AutodocModel autodocModel) {
		super();
		this.xsdAttribute = xsdAttribute;
		this.autodocModel = autodocModel;
	}

	public XsdAttribute getXsdAttribute() {
		return xsdAttribute;
	}
	
	private String parseBaseType( String type ) {
		String baseType = type;
		if ( type.startsWith( this.autodocModel.getXsdPrefix() ) ) {
			baseType = type.replace( this.autodocModel.getXsdPrefix() , EMPTY );
		} else if ( type.startsWith( this.autodocModel.getAutodocPrefix() ) ) {
			baseType = type.replace( this.autodocModel.getAutodocPrefix() , EMPTY );
		}
		return baseType;
	}
	
	public XsdAnnotation getXsdAnnotationDeep() {
		XsdAnnotation annotation = this.getXsdAttribute().getAnnotation();
		if ( annotation == null && this.getXsdAttribute().getXsdSimpleType() != null ) {
			annotation = this.getXsdAttribute().getXsdSimpleType().getAnnotation();
		}
		return annotation;
	}
	
	public String getName() {
		return this.getXsdAttribute().getRawName();
	}
	
	private void handleBaseRestriction( StringBuilder builder, XsdRestriction xsdRestriction ) {
		if ( xsdRestriction.getBase() != null ) {
			builder.append( " , base : " );
			builder.append( this.parseBaseType( xsdRestriction.getBase() ) );
		}
	}
	
	private void handleLenthRestrictions( StringBuilder builder, XsdRestriction xsdRestriction ) {
		if ( xsdRestriction.getMinLength() != null ) {
			builder.append( " , minLength : " );
			builder.append( xsdRestriction.getMinLength().getValue() );	
		}
		if ( xsdRestriction.getMaxLength() != null ) {
			builder.append( " , maxLength : " );
			builder.append( xsdRestriction.getMaxLength().getValue() );	
		}
	}
	
	private void handleMinRestrictions( StringBuilder builder, XsdRestriction xsdRestriction ) {
		if ( xsdRestriction.getMinInclusive() != null ) {
			builder.append( " , minInclusive : " );
			builder.append( xsdRestriction.getMinInclusive().getValue() );	
		}
		if ( xsdRestriction.getMaxInclusive() != null ) {
			builder.append( " , maxInclusive : " );
			builder.append( xsdRestriction.getMaxInclusive().getValue() );	
		}
	}
	
	private void handleMaxRestrictions( StringBuilder builder, XsdRestriction xsdRestriction ) {
		if ( xsdRestriction.getMinExclusive() != null ) {
			builder.append( " , minExclusive : " );
			builder.append( xsdRestriction.getMinExclusive().getValue() );	
		}
		if ( xsdRestriction.getMaxExclusive() != null ) {
			builder.append( " , maxExclusive : " );
			builder.append( xsdRestriction.getMaxExclusive().getValue() );	
		}
	}
	
	private void handlePatternRestriction( StringBuilder builder, XsdRestriction xsdRestriction ) {
		if ( xsdRestriction.getPattern() != null ) {
			builder.append( " , pattern : " );
			builder.append( xsdRestriction.getPattern().getValue() );
		}
	}
	
	private void handleEnumerationRestriction( StringBuilder builder, XsdRestriction xsdRestriction ) {
		List<XsdEnumeration> enumeratios = xsdRestriction.getEnumeration();
		if ( enumeratios != null && !enumeratios.isEmpty() ) {
			builder.append( " , enumeration : [ " );
			builder.append( StringUtils.concat( " , " , enumeratios.stream().map( current -> { return current.getValue(); } ).collect( Collectors.toList() ) ) );
			builder.append( " ]" );
		}
	}

	public String getNote() {
		StringBuilder builder = new StringBuilder();
		String type = this.parseBaseType( this.getXsdAttribute().getType() );
		builder.append( type );
		List<XsdRestriction> restrictions = this.getXsdAttribute().getAllRestrictions();
		if ( restrictions != null && !restrictions.isEmpty() ) {
			for ( XsdRestriction xsdRestriction : restrictions ) {
				this.handleBaseRestriction(builder, xsdRestriction);
				this.handleLenthRestrictions(builder, xsdRestriction);
				this.handleMinRestrictions(builder, xsdRestriction);
				this.handleMaxRestrictions(builder, xsdRestriction);
				this.handlePatternRestriction(builder, xsdRestriction);
				this.handleEnumerationRestriction(builder, xsdRestriction);
			}
		}
		return builder.toString();
	}

}
