package org.fugerit.java.doc.lib.autodoc.model;

import org.fugerit.java.core.util.collection.KeyString;
import org.w3c.dom.Element;

public class XsdTagModel implements KeyString {

	private XsdDocModel docModel;
	
	private Element element;

	public XsdTagModel( XsdDocModel docModel, Element tag ) {
		super();
		this.element = tag;
		this.docModel = docModel;
	}

	public Element getElement() {
		return element;
	}

	public XsdDocModel getDocModel() {
		return docModel;
	}

	@Override
	public String getKey() {
		return this.getElement().getAttribute( "name" );
	}
	
}
