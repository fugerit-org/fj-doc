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
 * @(#)DocText.java
 *
 * @project    : org.fugerit.java.doc.base
 * @package    : org.fugerit.java.doc.base
 * @creation   : 06/set/06
 * @license	   : META-INF/LICENSE.TXT
 */
package org.fugerit.java.doc.base.model;

/**
 * <p></p>
 *
 * @author mfranci
 *
 */
public class DocText extends DocElement {

	private int vAlign;
	
	private int hAlign;

	/**
	 * @return the hAlign
	 */
	public int getHAlign() {
		return hAlign;
	}

	/**
	 * @param align the hAlign to set
	 */
	public void setHAlign(int align) {
		hAlign = align;
	}

	/**
	 * @return the vAlign
	 */
	public int getVAlign() {
		return vAlign;
	}

	/**
	 * @param align the vAlign to set
	 */
	public void setVAlign(int align) {
		vAlign = align;
	} 
	
}
