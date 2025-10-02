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

import lombok.Getter;
import lombok.Setter;

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
	private static final long serialVersionUID = 80026421125892032L;

	public static final String TAG_NAME = "doc";
	
	private static void print( DocContainer docContainer, PrintStream s, int pad ) {
		StringBuilder builder = new StringBuilder();
		for ( int k=0; k<pad; k++ ) {
			builder.append( "  " );
		}
		Iterator<DocElement> it = docContainer.docElements();
		String p = builder.toString();
		while ( it.hasNext() ) {
			DocElement docElement = it.next();
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
	}

	@Getter @Setter private DocHeader docHeader;
	
	@Getter @Setter private DocFooter docFooter;
	
	@Getter @Setter private DocContainer docBody;

	@Getter @Setter private DocContainer docMeta;
	
	@Getter @Setter private DocBackground docBackground;
	
	@Getter @Setter private DocBookmarkTree docBookmarkTree;
	
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
		return this.idMap.get( id );
	}
		
	public Properties getInfo() {
		Properties info = new Properties();
		Iterator<DocElement> itInfo = this.getDocMeta().docElements();
		while ( itInfo.hasNext() ) {
			DocElement docElement = itInfo.next();
			if ( docElement instanceof DocInfo ) {
				DocInfo docInfo = (DocInfo) docElement;
				info.setProperty( docInfo.getName() , docInfo.getContent().toString() );
			}
		}
		return info;
	}
	
	@Getter @Setter private Properties stableInfo;

	public Properties getStableInfoSafe() {
		if ( this.getStableInfo() == null ) {
			this.setStableInfo( this.getInfo() );
		}
		return this.getStableInfo();
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

    public String getInfoDocProducer() {
        return this.getStableInfo().getProperty( DocInfo.INFO_DOC_PRODUCER );
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
	
}
