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
 * @(#)DocInfo.java
 *
 * @project    : org.fugerit.java.doc.base
 * @package    : org.fugerit.java.doc.base
 * @creation   : 22/set/06
 * @license	   : META-INF/LICENSE.TXT
 */
package org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;

/**
 * 
 *
 * @author Matteo Franci a.k.a. Fugerit
 *
 */
public class DocInfo extends DocElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1258119296671751462L;

	public static final String TAG_NAME = "info";

	// properties for document meta informations
	public static final String INFO_DOC_TITLE = "doc-title";
	public static final String INFO_DOC_AUTHOR = "doc-author";
	public static final String INFO_DOC_SUBJECT = "doc-subject";
	public static final String INFO_DOC_VERSION = "doc-version";
	public static final String INFO_DOC_CREATOR = "doc-creator";
	public static final String INFO_DOC_LANGUAGE = "doc-language";
	
	// properties specific for HTML format
	public static final String INFO_NAME_CSS_LINK = "html-css-link";
	public static final String INFO_NAME_CSS_STYLE = "html-css-style";
	public static final String INFO_NAME_HTML_ADD_TO_HEAD = "html-add-to-head";
	
	// properties specific for fixed size formats (like PDF)
	public static final String INFO_NAME_MARGINS = GenericConsts.INFO_KEY_MARGINS;
	public static final String INFO_NAME_PAGE_ORIENT = "page-orient";
	public static final String INFO_VALUE_PAGE_ORIENT_PORTRAIT = "vertical";
	public static final String INFO_VALUE_PAGE_ORIENT_LANDSCAPE = "horizontal";
	public static final String INFO_NAME_PAGE_WIDTH = "page-width";
	public static final String INFO_NAME_PAGE_HEIGHT = "page-height";
	public static final String INFO_NAME_PDF_FORMAT = "pdf-format";

	public static final String INFO_DOC_VERSION_COMPATIBILITY = "doc-version-compatibility";
	
	private String name;
	
	private StringBuffer content = new StringBuffer();

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the content
	 */
	public StringBuffer getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(StringBuffer content) {
		this.content = content;
	}
	
}
