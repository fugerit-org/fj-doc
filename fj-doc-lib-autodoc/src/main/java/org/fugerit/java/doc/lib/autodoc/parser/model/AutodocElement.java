package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import org.fugerit.java.core.util.collection.KeyString;
import org.xmlet.xsdparser.xsdelements.XsdAnnotation;
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
	
	public String getName() {
		return this.getXsdElement().getRawName();
	}
	
	@Override
	public String getKey() {
		return this.getXsdElement().getRawName();
	}
	
	public XsdComplexType getComplexType() {
		XsdComplexType complexType = this.getXsdElement().getTypeAsComplexType();
		if ( complexType == null ) {
			complexType = this.getXsdElement().getXsdComplexType();
		}
		return complexType;
	}
	
	public AutodocType getAutodocType() {
		return new AutodocType( this.getComplexType() );
	}
	
	public XsdAnnotation getXsdAnnotationDeep() {
		XsdAnnotation annotation = this.getXsdElement().getAnnotation();
		if ( annotation == null && this.getComplexType() != null ) {
			annotation = this.getComplexType().getAnnotation();
		}
		return annotation;
	}
	
	public Collection<XsdAttribute> getXsdAttributes() {
		return this.getComplexType().getAllXsdAttributes().collect( Collectors.toList() );
	}
	
	public Collection<AutodocAttribute> getAutodocAttributes() {
		return this.getComplexType().getAllXsdAttributes().map( current -> { return new AutodocAttribute( current, this.autodocModel ); } ).collect( Collectors.toList() );
	}
	
}
