package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.util.List;
import java.util.stream.Collectors;

import org.fugerit.java.core.lang.helpers.StringUtils;
import org.xmlet.xsdparser.xsdelements.XsdAnnotation;
import org.xmlet.xsdparser.xsdelements.XsdAttribute;
import org.xmlet.xsdparser.xsdelements.XsdRestriction;
import org.xmlet.xsdparser.xsdelements.xsdrestrictions.XsdEnumeration;
import org.xmlet.xsdparser.xsdelements.xsdrestrictions.XsdPattern;

public class AutodocAttribute {

	private XsdAttribute xsdAttribute;

	private AutodocModel autodocModel;

	
	public AutodocAttribute(XsdAttribute xsdAttribute, AutodocModel autodocModel) {
		super();
		this.xsdAttribute = xsdAttribute;
		this.autodocModel = autodocModel;
	}

	public XsdAttribute getXsdAttribute() {
		return xsdAttribute;
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



	public String getNote() {
		return AutodocUtils.toNote( this.getXsdAttribute().getType(), this.autodocModel, this.getXsdAttribute().getAllRestrictions() );
	}

}
