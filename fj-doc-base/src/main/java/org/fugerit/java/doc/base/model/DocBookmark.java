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
package org.fugerit.java.doc.base.model;
/*
 * @(#)DocTable.java
 *
 * @project    : org.fugerit.java.doc.base
 * @package    : org.fugerit.java.doc.base
 * @creation   : 06/set/06
 * @license	   : META-INF/LICENSE.TXT
 */

/**
 * 
 *
 * @author fugerit
 *
 */
public class DocBookmark extends DocElement {

	public DocBookmark() {
		this.title = "";
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 470846678198846L;
	
	public static final String TAG_NAME = "bookmark";
	
	public static final String ATT_REF = "ref";
	
	private String ref;
	
	private String title;

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
