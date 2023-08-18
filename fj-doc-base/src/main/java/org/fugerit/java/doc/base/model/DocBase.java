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
 * @(#)DocBase.java
 *
 * @project    : org.fugerit.java.doc.base
 * @package    : org.fugerit.java.doc.base
 * @creation   : 06/set/06
 * @license	   : META-INF/LICENSE.TXT
 */
package org.fugerit.java.doc.base.model;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;

/**
 * 
 *
 * @author mfranci
 *
 */
public class DocBase extends DocElement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8002627421125892032L;

	public static final String TAG_NAME = "doc";
	
	private static void print( DocContainer docContainer, PrintStream s, int pad ) {
		String p = "";
		for ( int k=0; k<pad; k++ ) {
			p+= "  ";
		}
		Iterator<DocElement> it = docContainer.docElements();
		while ( it.hasNext() ) {
			DocElement docElement = (DocElement)it.next();
			s.println( p+docElement );
			if ( docElement instanceof DocContainer ) {
				print( (DocContainer)docElement, s, pad+1 );
			}
		}
	}
	
	public static void print( DocBase docBase, PrintStream s ) {
		s.println( docBase );
		print( docBase.getDocBody(), s, 1 );
	}
	
	public DocBase() {
		this.docBody = new DocContainer();
		this.docMeta = new DocContainer();
		this.docHeader = new DocHeader();
		this.docFooter = new DocFooter();
		this.idMap = new HashMap<>();
		//this.additionalData = new HashMap<>();
	}
	
//	private Map additionalData;
//	
//	public Map getAdditionalData() {
//		return additionalData;
//	}

	private DocHeader docHeader;
	
	private DocFooter docFooter;
	
	private DocContainer docBody;

	private DocContainer docMeta;
	
	private DocBackground docBackground;
	
	private DocBookmarkTree docBookmarkTree;
	
	private HashMap<String, DocElement> idMap;
	
	private String xsdVersion;
	
	public String getXsdVersion() {
		return xsdVersion;
	}

	public void setXsdVersion(String xsdVersion) {
		this.xsdVersion = xsdVersion;
	}

	public void setId( String id, DocElement element ) {
		element.setId( id );
		this.idMap.put( id , element );
	}
	
	public DocElement getElementById( String id ) {
		return (DocElement)this.idMap.get( id );
	}
	
	/**
	 * @return the docMeta
	 */
	public DocContainer getDocMeta() {
		return docMeta;
	}

	/**
	 * @param docMeta the docMeta to set
	 */
	public void setDocMeta(DocContainer docMeta) {
		this.docMeta = docMeta;
	}

	/**
	 * @return the docBody
	 */
	public DocContainer getDocBody() {
		return docBody;
	}

	/**
	 * @param docBody the docBody to set
	 */
	public void setDocBody(DocContainer docBody) {
		this.docBody = docBody;
	}

	/**
	 * @return the docFooter
	 */
	public DocFooter getDocFooter() {
		return docFooter;
	}

	/**
	 * @param docFooter the docFooter to set
	 */
	public void setDocFooter(DocFooter docFooter) {
		this.docFooter = docFooter;
	}

	/**
	 * @return the docHeader
	 */
	public DocHeader getDocHeader() {
		return docHeader;
	}

	/**
	 * @param docHeader the docHeader to set
	 */
	public void setDocHeader(DocHeader docHeader) {
		this.docHeader = docHeader;
	}
	
	public Properties getInfo() {
		Properties info = new Properties();
		Iterator<DocElement> itInfo = this.getDocMeta().docElements();
		while ( itInfo.hasNext() ) {
			DocElement docElement = (DocElement)itInfo.next();
			if ( docElement instanceof DocInfo ) {
				DocInfo docInfo = (DocInfo) docElement;
				info.setProperty( docInfo.getName() , docInfo.getContent().toString() );
			}
		}
		return info;
	}
	
	private Properties stableInfo;
	
	public Properties getStableInfo() {
		return stableInfo;
	}

	public void setStableInfo(Properties stableInfo) {
		this.stableInfo = stableInfo;
	}
	
	public String getInfoPageWidth() {
		return this.getStableInfo().getProperty( DocInfo.INFO_NAME_PAGE_WIDTH );
	}
	
	public String getInfoPageHeight() {
		return this.getStableInfo().getProperty( DocInfo.INFO_NAME_PAGE_HEIGHT );
	}
	
	public String getInfoDocVersion() {
		return this.getStableInfo().getProperty( DocInfo.INFO_DOC_VERSION );
	}
	
	public String getInfoDocTitle() {
		return this.getStableInfo().getProperty( DocInfo.INFO_DOC_TITLE );
	}
	
	public String getInfoDocSubject() {
		return this.getStableInfo().getProperty( DocInfo.INFO_DOC_SUBJECT );
	}
	
	public String getInfoDocAuthor() {
		return this.getStableInfo().getProperty( DocInfo.INFO_DOC_AUTHOR );
	}
	
	public String getInfoDocCreator() {
		return this.getStableInfo().getProperty( DocInfo.INFO_DOC_CREATOR );
	}

	public String getInfoDocLanguage() {
		return this.getStableInfo().getProperty( DocInfo.INFO_DOC_LANGUAGE );
	}
	
	private static int getMargin( Properties props, int position ) {
		String margins = props.getProperty( GenericConsts.INFO_KEY_MARGINS, "10;10;10;10" );
		return Integer.parseInt( margins.split( ";")[position] );
	}
	
	public int getMarginLeft() {
		return getMargin( this.getInfo() , GenericConsts.POSITION_MARGIN_LEFT );
	}

	public int getMarginRight() {
		return getMargin( this.getInfo() , GenericConsts.POSITION_MARGIN_RIGHT );
	}

	public int getMarginTop() {
		return getMargin( this.getInfo() , GenericConsts.POSITION_MARGIN_TOP );
	}

	public int getMarginBottom() {
		return getMargin( this.getInfo() , GenericConsts.POSITION_MARGIN_BOTTOM );
	}
	
	public boolean isUseHeader() {
		return this.getDocHeader() != null && this.getDocHeader().isUseHeader();
	}
	
	public boolean isUseFooter() {
		return this.getDocFooter() != null && this.getDocFooter().isUseFooter();
	}

	public DocBackground getDocBackground() {
		return docBackground;
	}

	public void setDocBackground(DocBackground docBackground) {
		this.docBackground = docBackground;
	}

	public DocBookmarkTree getDocBookmarkTree() {
		return docBookmarkTree;
	}

	public void setDocBookmarkTree(DocBookmarkTree docBookmarkTree) {
		this.docBookmarkTree = docBookmarkTree;
	}
	
}
