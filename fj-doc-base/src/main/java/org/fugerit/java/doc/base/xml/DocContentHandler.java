/*****************************************************************
<copyright>
	Fugerit Java Library org.fugerit.java.doc.base 

	Copyright (c) 2019 Fugerit

	All rights reserved. This program and the accompanying materials
	are made available under the terms of the Apache License v2.0
	which accompanies this distribution, and is available at
	http://www.apache.org/licenses/
	(txt version : http://www.apache.org/licenses/LICENSE-2.0.txt
	html version : http://www.apache.org/licenses/LICENSE-2.0.html)

   This product includes software developed at
   The Apache Software Foundation (http://www.apache.org/).
</copyright>
*****************************************************************/
/*
 * @(#)DocContentHandler.java
 *
 * @project    : org.fugerit.java.doc.base
 * @package    : org.fugerit.java.doc.base.xml
 * @creation   : 06/set/06
 * @license	   : META-INF/LICENSE.TXT
 */
package org.fugerit.java.doc.base.xml;

import java.util.Properties;

import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocHelper;
import org.fugerit.java.doc.base.parser.DocParserContext;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * 
 *
 * @author Matteo 
 *
 */
public class DocContentHandler implements ContentHandler {
	
	public static Properties attsToProperties(Attributes atts) {
		Properties props =  new Properties();
		for ( int k=0; k<atts.getLength(); k++ ) {
			String key = atts.getQName( k );
			String value = atts.getValue( k );
			props.setProperty(key, value);
		}
		return props;
	}
	
	private DocParserContext context;
	
	private DocHelper docHelper;
	
	public DocContentHandler( DocHelper docHelper ) {
		this.docHelper = docHelper;
		this.context = new DocParserContext();
	}
	
	public DocContentHandler() {
		this( new DocHelper() );
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		String text = this.docHelper.filterText( new String( ch, start, length ) );
		this.context.handleText(text);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
		this.context.endDocument();
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	public void endPrefixMapping(String prefix) throws SAXException {
		// this SAX event is not used for parsing doc xsd
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		this.characters(ch, start, length);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	public void processingInstruction(String target, String data) throws SAXException {
		// this SAX event is not used for parsing doc xsd
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	public void setDocumentLocator(Locator locator) {
		// this SAX event is not used for parsing doc xsd
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	public void skippedEntity(String name) throws SAXException {
		// this SAX event is not used for parsing doc xsd
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		this.context.startDocument();
	}
	

	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		this.context.handleEndElement( qName );
	}		
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		Properties props =  attsToProperties(atts);
		this.context.handleStartElement(qName, props);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		// this SAX event is not used for parsing doc xsd
	}

	/**
	 * @return the docBase
	 */
	public DocBase getDocBase() {
		return this.context.getDocBase();
	}

}
