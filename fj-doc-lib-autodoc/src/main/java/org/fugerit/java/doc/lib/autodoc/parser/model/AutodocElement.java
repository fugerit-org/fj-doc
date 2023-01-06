package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import org.fugerit.java.core.util.collection.KeyString;
import org.xmlet.xsdparser.xsdelements.XsdAttribute;
import org.xmlet.xsdparser.xsdelements.XsdComplexType;
import org.xmlet.xsdparser.xsdelements.XsdElement;

public class AutodocElement implements KeyString, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1969948252985082612L;

	private AutodocModel autodocModel;
	
	private XsdElement xsdElement;

	public AutodocElement(AutodocModel autodocModel, XsdElement xsdElement) {
		super();
		this.autodocModel = autodocModel;
		this.xsdElement = xsdElement;
	}

	public AutodocModel getAutodocModel() {
		return autodocModel;
	}

	public XsdElement getXsdElement() {
		return xsdElement;
	}

	public String getAnnotationAsSingleString() {
		return AutodocUtils.annotationAsSingleStringHelper( this.getXsdElement().getAnnotation() );
	}
	
	@Override
	public String getKey() {
		return this.getXsdElement().getName();
	}
	
	public XsdComplexType getComplexType() {
		XsdComplexType complexType = this.getXsdElement().getTypeAsComplexType();
		if ( complexType == null ) {
			complexType = this.getXsdElement().getXsdComplexType();
		}
		return complexType;
	}
	
	public Collection<XsdAttribute> getXsdAttributes() {
		return this.getComplexType().getAllXsdAttributes().collect( Collectors.toList() );
	}
	
}
