package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlet.xsdparser.xsdelements.XsdComplexType;

public class AutodocType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1549582953481172034L;

	private final static Logger logger = LoggerFactory.getLogger( AutodocType.class );
	
	private XsdComplexType xsdComplexType;

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
			logger.warn( "Null pointer exception getting choice for type "+this.getXsdComplexType() );
		}
		return result;
	}
	
	public AutodocSequence getSequence() {
		AutodocSequence result = null;
		try {
			result = new AutodocSequence( this.getXsdComplexType().getChildAsSequence() );
		} catch (NullPointerException npe) {
			logger.warn( "Null pointer exception getting sequence for type "+this.getXsdComplexType() );
		}
		return result;
	}
	
}