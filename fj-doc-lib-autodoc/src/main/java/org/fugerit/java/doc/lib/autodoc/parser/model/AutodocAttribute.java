package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.util.List;
import java.util.stream.Collectors;

import org.fugerit.java.core.lang.helpers.StringUtils;
import org.xmlet.xsdparser.xsdelements.XsdAttribute;
import org.xmlet.xsdparser.xsdelements.XsdRestriction;
import org.xmlet.xsdparser.xsdelements.xsdrestrictions.XsdEnumeration;

public class AutodocAttribute {

	private XsdAttribute xsdAttribute;

	private String xsdPrefix;
	
	private String autodocPrefix;
	
	public AutodocAttribute(XsdAttribute xsdAttribute) {
		super();
		this.xsdAttribute = xsdAttribute;
		this.xsdPrefix = "xsd:";
		this.autodocPrefix = "doc:";
	}

	public XsdAttribute getXsdAttribute() {
		return xsdAttribute;
	}
	
	private String parseBaseType( String type ) {
		String baseType = type;
		if ( type.startsWith( this.xsdPrefix ) ) {
			baseType = type.replace( this.xsdPrefix , "" );
		} else if ( type.startsWith( this.autodocPrefix ) ) {
			baseType = type.replace( this.autodocPrefix , "" );
		}
		return baseType;
	}
	
	public String getNote() {
		StringBuilder builder = new StringBuilder();
		String type = this.parseBaseType( this.getXsdAttribute().getType() );
		builder.append( type );
		List<XsdRestriction> restrictions = this.getXsdAttribute().getAllRestrictions();
		if ( restrictions != null && !restrictions.isEmpty() ) {
			for ( XsdRestriction xsdRestriction : restrictions ) {
				if ( xsdRestriction.getBase() != null ) {
					builder.append( " , base : " );
					builder.append( this.parseBaseType( xsdRestriction.getBase() ) );
				}
				if ( xsdRestriction.getMinLength() != null ) {
					builder.append( " , minLength : " );
					builder.append( xsdRestriction.getMinLength().getValue() );	
				}
				if ( xsdRestriction.getMaxLength() != null ) {
					builder.append( " , maxLength : " );
					builder.append( xsdRestriction.getMaxLength().getValue() );	
				}
				if ( xsdRestriction.getMinExclusive() != null ) {
					builder.append( " , minExclusive : " );
					builder.append( xsdRestriction.getMinExclusive().getValue() );	
				}
				if ( xsdRestriction.getMaxExclusive() != null ) {
					builder.append( " , maxExclusive : " );
					builder.append( xsdRestriction.getMaxExclusive().getValue() );	
				}
				if ( xsdRestriction.getPattern() != null ) {
					builder.append( " , pattern : " );
					builder.append( xsdRestriction.getPattern().getValue() );
				}
				List<XsdEnumeration> enumeratios = xsdRestriction.getEnumeration();
				if ( enumeratios != null && !enumeratios.isEmpty() ) {
					builder.append( " , enumeration : [ " );
					builder.append( StringUtils.concat( " , " , enumeratios.stream().map( current -> { return current.getValue(); } ).collect( Collectors.toList() ) ) );
					builder.append( " ]" );
				}
			}
		}
		return builder.toString();
	}

}
