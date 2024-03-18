package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.io.Serializable;

import org.xmlet.xsdparser.xsdelements.XsdComplexType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AutodocType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1549582953481172034L;

	private transient XsdComplexType xsdComplexType;

	public XsdComplexType getXsdComplexType() {
		return xsdComplexType;
	}

	public AutodocType(XsdComplexType xsdComplexType) {
		super();
		this.xsdComplexType = xsdComplexType;
	}
	
	public AutodocChoice getChoice() {
		AutodocChoice result = null;
		try {
			result = new AutodocChoice( this.getXsdComplexType().getChildAsChoice() );
		} catch (NullPointerException npe) {
			log.warn( "Null pointer exception getting choice for type {}", this.getXsdComplexType() );
		}
		return result;
	}
	
	public AutodocSequence getSequence() {
		AutodocSequence result = null;
		try {
			result = new AutodocSequence( this.getXsdComplexType().getChildAsSequence() );
		} catch (NullPointerException npe) {
			log.warn( "Null pointer exception getting sequence for type {}", this.getXsdComplexType() );
		}
		return result;
	}

	public String getKey() {
		return AutodocUtils.toKey( this.xsdComplexType );
	}

}
