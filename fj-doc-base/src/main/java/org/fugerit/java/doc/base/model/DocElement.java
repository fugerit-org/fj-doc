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
 * @(#)DocElement.java
 *
 * @project    : org.fugerit.java.doc.base
 * @package    : org.fugerit.java.doc.base
 * @creation   : 06/set/06
 * @license	   : META-INF/LICENSE.TXT
 */
package org.fugerit.java.doc.base.model;

import java.io.Serializable;

/**
 * 
 *
 * @author mfranci
 *
 */
public class DocElement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1934678965894426319L;

	@Override
	public String toString() {
		return this.getClass().getName()+"[id:"+this.getId()+"]";
	}
	
	public static final int ELEMENT_TYPE_TABLE = 1;
	
	public static final int ELEMENT_TYPE_TABLEROW = 2;
	
	public static final int ELEMENT_TYPE_TABLECELL = 3;
	
	public static final int ELEMENT_TYPE_PARAGRAPH = 4;
	
	public static final String STRING_1 = "1";
	
	public static final String STRING_0 = "0";
	
	public static final String UNSET = "-1";
	
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
