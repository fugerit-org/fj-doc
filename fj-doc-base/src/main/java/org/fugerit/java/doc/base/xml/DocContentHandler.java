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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Properties;

import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.model.DocBarcode;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocBorders;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocContainer;
import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocFooter;
import org.fugerit.java.doc.base.model.DocHeader;
import org.fugerit.java.doc.base.model.DocHeaderFooter;
import org.fugerit.java.doc.base.model.DocHelper;
import org.fugerit.java.doc.base.model.DocImage;
import org.fugerit.java.doc.base.model.DocInfo;
import org.fugerit.java.doc.base.model.DocPageBreak;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.model.DocPhrase;
import org.fugerit.java.doc.base.model.DocRow;
import org.fugerit.java.doc.base.model.DocTable;
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
	
	private static final String[] ELEMENT_CONTAINER = { "table", 
														"row", 
														"cell", 
														"body", 
														"meta", 
														"metadata", 
														"header", 
														"footer", 
														"header-ext", 
														"footer-ext" };
	
	private static final Collection<String> CONTAINER_LIST = new HashSet<>( Arrays.asList( ELEMENT_CONTAINER ) );
	
	private DocBase docBase;
	
	private DocElement currentElement;
	
	private DocContainer currentContainer;
	
	private LinkedList<DocContainer> parents;
	
	private DocHelper docHelper;
	
	public DocContentHandler( DocHelper docHelper ) {
		this.docHelper = docHelper;
	}
	
	public DocContentHandler() {
		this( new DocHelper() );
	}
	
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		String text = this.docHelper.filterText( new String( ch, start, length ) );
		if ( text.trim().length() > 0 && this.currentElement instanceof DocPhrase ) {	
			DocPhrase docPhrase = (DocPhrase)this.currentElement;
			docPhrase.setText( docPhrase.getText()+text );
		} else if ( text.trim().length() > 0 && this.currentElement instanceof DocPara ) {
			DocPara docPara = (DocPara)this.currentElement;
			docPara.setText( docPara.getText()+text );
		} else if ( text.trim().length() > 0 && this.currentElement instanceof DocInfo ) {
			DocInfo docInfo = (DocInfo)this.currentElement;
			docInfo.getContent().append( text );
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
		
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	public void endPrefixMapping(String prefix) throws SAXException {
		
		
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
		
		
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	public void setDocumentLocator(Locator locator) {
		
		
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	public void skippedEntity(String name) throws SAXException {
	
		
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		this.parents = new LinkedList<>();
		this.currentContainer = null;
		this.currentElement = null;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ( CONTAINER_LIST.contains( qName ) ) {
			if ( !this.parents.isEmpty() ) {
				this.currentContainer = (DocContainer)this.parents.remove( this.parents.size()-1 );	
			} else {
				this.currentContainer = null;
			}
		}
	}	
	
	private static int getAlign( String align ) {
		int result = DocPara.ALIGN_UNSET;
		if ( "center".equalsIgnoreCase( align ) ) {
			result = DocPara.ALIGN_CENTER;
		} else if ( "right".equalsIgnoreCase( align ) ) {
			result = DocPara.ALIGN_RIGHT;
		} else if ( "left".equalsIgnoreCase( align ) ) {
			result = DocPara.ALIGN_LEFT;
		} else if ( "justify".equalsIgnoreCase( align ) ) {
			result = DocPara.ALIGN_JUSTIFY;
		} else if ( "justifyall".equalsIgnoreCase( align ) ) {
			result = DocPara.ALIGN_JUSTIFY_ALL;
		}
		return result;
	}	
	
	private static int getValign( String align ) {
		int result = DocPara.ALIGN_UNSET;
		if ( "middle".equalsIgnoreCase( align ) ) {
			result = DocPara.ALIGN_MIDDLE;
		} else if ( "top".equalsIgnoreCase( align ) ) {
			result = DocPara.ALIGN_TOP;
		} else if ( "bottom".equalsIgnoreCase( align ) ) {
			result = DocPara.ALIGN_BOTTOM;
		} 
		return result;
	}
	
	private static void handleHeaderFooter( DocHeaderFooter headerFooter, Properties atts ) {
		String align = atts.getProperty( "align" );
		headerFooter.setAlign( getAlign( align ) );
		String numbered = atts.getProperty( "numbered" );
		headerFooter.setNumbered( Boolean.valueOf( numbered ).booleanValue() );
		String borderWidth = atts.getProperty( "border-width", "0" );
		headerFooter.setBorderWidth( Integer.valueOf( borderWidth ).intValue() );
	}
	
	private DocBorders createBorders( Properties atts ) {
		DocBorders docBorders = new DocBorders();
		docBorders.setBorderColorBottom( atts.getProperty( "border-color-bottom", atts.getProperty( "border-color" ) ) );
		docBorders.setBorderColorTop( atts.getProperty( "border-color-top", atts.getProperty( "border-color" ) ) );
		docBorders.setBorderColorLeft( atts.getProperty( "border-color-left", atts.getProperty( "border-color" ) ) );
		docBorders.setBorderColorRight( atts.getProperty( "border-color-right", atts.getProperty( "border-color" ) ) );
		docBorders.setBorderWidthBottom( Integer.parseInt( atts.getProperty( "border-width-bottom", atts.getProperty( "border-width", "-1" ) ) ) );
		docBorders.setBorderWidthTop( Integer.parseInt( atts.getProperty( "border-width-top", atts.getProperty( "border-width", "-1" ) ) ) );
		docBorders.setBorderWidthLeft( Integer.parseInt( atts.getProperty( "border-width-left", atts.getProperty( "border-width", "-1" ) ) ) );
		docBorders.setBorderWidthRight( Integer.parseInt( atts.getProperty( "border-width-right", atts.getProperty( "border-width", "-1" ) ) ) );
		docBorders.setPaddingBottom( Integer.parseInt( atts.getProperty( "padding-bottom", atts.getProperty( "padding", "-1" ) ) ) );
		docBorders.setPaddingTop( Integer.parseInt( atts.getProperty( "padding-top", atts.getProperty( "padding", "-1" ) ) ) );
		docBorders.setPaddingLeft( Integer.parseInt( atts.getProperty( "padding-left", atts.getProperty( "padding", "-1" ) ) ) );
		docBorders.setPaddingRight( Integer.parseInt( atts.getProperty( "padding-right", atts.getProperty( "padding", "-1" ) ) ) );
		return docBorders;
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		Properties props =  new Properties();
		for ( int k=0; k<atts.getLength(); k++ ) {
			String key = atts.getQName( k );
			String value = atts.getValue( k );
			props.setProperty(key, value);
		}
		if ( "doc".equalsIgnoreCase( qName ) ) {
			this.docBase = new DocBase();
		} else if ( "meta".equalsIgnoreCase( qName ) || "metadata".equalsIgnoreCase( qName ) ) {
			DocContainer docMeta = this.docBase.getDocMeta();
			this.currentElement = docMeta;
		} else if ( "info".equalsIgnoreCase( qName ) ) {
			DocInfo docInfo = new DocInfo();
			docInfo.setName( props.getProperty( "name" ) );
			this.currentElement = docInfo;
		} else if ( "header".equalsIgnoreCase( qName ) || "header-ext".equalsIgnoreCase( qName )  ) {
			DocHeader docHeader = this.docBase.getDocHeader();
			handleHeaderFooter( docHeader , props );
			docHeader.setUseHeader( true );
			if ( "header-ext".equalsIgnoreCase( qName ) ) {
				docHeader.setBasic( false );
			} else {
				docHeader.setBasic( true );
			}
			this.currentElement = docHeader;		
		} else if ( "footer".equalsIgnoreCase( qName ) || "footer-ext".equalsIgnoreCase( qName ) ) {
			DocFooter docFooter = this.docBase.getDocFooter();
			handleHeaderFooter( docFooter , props );
			docFooter.setUseFooter( true );
			if ( "footer-ext".equalsIgnoreCase( qName ) ) {
				docFooter.setBasic( false );
			} else {
				docFooter.setBasic( true );
			}
			this.currentElement = docFooter;				
		} else if ( "body".equalsIgnoreCase( qName ) ) {
			DocContainer docBody = this.docBase.getDocBody();
			this.currentElement = docBody;
			//Properties info = this.docBase.getInfo();
		} else if ( "image".equalsIgnoreCase( qName ) ) {
			DocImage docImage = new DocImage();
			// setting paragraph style
			String url = props.getProperty( "url" );
			docImage.setUrl( url );
			String scaling = props.getProperty( "scaling" );
			if ( scaling != null ) {
				docImage.setScaling( Integer.valueOf( scaling ) );	
			} else {
				docImage.setScaling( null );
			}
			String base64 = props.getProperty( "base64" );
			if ( StringUtils.isNotEmpty( base64 ) ) {
				docImage.setBase64( base64 );
			}
			this.currentElement = docImage;			
		} else if ( "para".equalsIgnoreCase( qName ) ) {
			DocPara docPara = new DocPara();
			// setting paragraph style
			String style = props.getProperty( "style" );
			docPara.setStyle( DocPara.parseStyle( style ) );
			// setting paragraph align
			String align = props.getProperty( "align" );
			docPara.setAlign( getAlign( align ) );
			String fontName = props.getProperty(  "font-name" );
			docPara.setFontName( fontName );
			String leading = props.getProperty( "leading" );
			docPara.setBackColor( props.getProperty( "back-color" ) );
			docPara.setForeColor( props.getProperty( "fore-color" ) );
			docPara.setFormat( props.getProperty( "format" ) );
			docPara.setType( props.getProperty( "type" ) );
			if ( leading != null ) {
				docPara.setLeading( Float.valueOf( leading ) );
			}
			// setting paragraph size
			docPara.setSize( Integer.parseInt( props.getProperty( "size", "-1" ) ) );
			String spaceBefore = props.getProperty( "space-before" );
			String spaceAfter = props.getProperty( "space-after" );
			if ( spaceBefore != null ) {
				docPara.setSpaceBefore( Float.valueOf( spaceBefore ) );
			}
			if ( spaceAfter != null ) {
				docPara.setSpaceAfter( Float.valueOf( spaceAfter ) );
			}				
			this.currentElement = docPara;
		} else if ( "phrase".equalsIgnoreCase( qName ) ) {
			DocPhrase docPhrase = new DocPhrase();
			// setting phrase size
			docPhrase.setSize( Integer.parseInt( props.getProperty( "size", "-1" ) ) );
			// setting phrase style
			String style = props.getProperty( "style" );
			docPhrase.setStyle( DocPara.parseStyle( style ) );
			//leading
			String leading = props.getProperty( "leading" );
			if ( leading != null ) {
				docPhrase.setLeading( Float.valueOf( leading ) );
			}
			this.currentElement = docPhrase;			
		} else if ( "barcode".equalsIgnoreCase( qName ) ) {
			DocBarcode barcode = new DocBarcode();
			barcode.setSize( Integer.parseInt( props.getProperty( "size", "-1" ) ) );
			barcode.setType( props.getProperty( "type", "EAN" ) );
			barcode.setText( props.getProperty( "text" ) );
			this.currentElement = barcode;	
		} else if ( "table".equalsIgnoreCase( qName ) ) {
			DocTable docTable = new DocTable();
			docTable.setColumns( Integer.parseInt( props.getProperty( "columns" ) )  );
			docTable.setWidth( Integer.parseInt( props.getProperty( "width", "-1" ) )  );
			docTable.setBackColor( props.getProperty( "back-color" ) );
			docTable.setForeColor( props.getProperty( "fore-color" ) );
			docTable.setSpacing( Integer.parseInt( props.getProperty( "spacing", "0" ) ) );
			docTable.setPadding( Integer.parseInt( props.getProperty( "padding", "0" ) ) );
			String cols = props.getProperty( "colwidths" );
			if ( cols != null ) {
				String[] colsParsed = cols.split( ";" );
				int[] withds = new int[colsParsed.length];
				for ( int k=0; k<withds.length; k++ ) {
					withds[k] = Integer.parseInt( colsParsed[k] );
				}
				docTable.setColWithds( withds );
			}
			String spaceBefore = props.getProperty( "space-before" );
			String spaceAfter = props.getProperty( "space-after" );
			if ( spaceBefore != null ) {
				docTable.setSpaceBefore( Float.valueOf( spaceBefore ) );
			}
			if ( spaceAfter != null ) {
				docTable.setSpaceAfter( Float.valueOf( spaceAfter ) );
			}			
			this.currentElement = docTable;
		} else if ( "row".equalsIgnoreCase( qName ) ) {
			DocRow docRow = new DocRow();
			this.currentElement = docRow;
		} else if ( "cell".equalsIgnoreCase( qName ) ) {
			DocCell docCell = new DocCell();
			docCell.setCSpan( Integer.parseInt( props.getProperty( "colspan", "1" ) ) );
			docCell.setRSpan( Integer.parseInt( props.getProperty( "rowspan", "1" ) ) );
			docCell.setBackColor( props.getProperty( "back-color" ) );
			docCell.setForeColor( props.getProperty( "fore-color" ) );
			docCell.setType( props.getProperty( "type" ) );
			docCell.setHeader( "true".equalsIgnoreCase( props.getProperty( "header" ) ) );
			// h align
			String align = props.getProperty( "align" );		
			docCell.setAlign( getAlign( align ) );
			// v align
			String valign = props.getProperty( "valign" );
			docCell.setValign( getValign( valign ) );
			docCell.setDocBorders( this.createBorders( props ) );
			this.currentElement = docCell;
		} else if ( "page-break".equalsIgnoreCase( qName ) ) {
			this.currentElement = new DocPageBreak();
		}
		// processamenti finali
		if ( this.currentContainer != null && this.currentContainer != this.currentElement ) {
			this.currentContainer.addElement( this.currentElement );
		}
		if ( CONTAINER_LIST.contains( qName ) ) {
			//if ( !qName.equals( "header-ext" ) && !qName.equals( "footer-ext" ) ) {
				this.parents.add( this.currentContainer );
			//}
			this.currentContainer = (DocContainer)this.currentElement;
		}
		// setting id
		String id = props.getProperty( "id" );
		if ( id != null ) {
			this.docBase.setId(id, this.currentElement );
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		
	}

	/**
	 * @return the docBase
	 */
	public DocBase getDocBase() {
		return docBase;
	}
	
}
