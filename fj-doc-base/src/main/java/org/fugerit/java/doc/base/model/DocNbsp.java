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
 * @(#)DocPara.java
 *
 * @project    : org.fugerit.java.doc.base
 * @package    : org.fugerit.java.doc.base
 * @creation   : 06/set/06
 * @license	   : META-INF/LICENSE.TXT
 */
package org.fugerit.java.doc.base.model;

/**
 * 
 *
 * @author mfranci
 *
 */
public class DocNbsp extends DocPhrase {
	
	public static final String TAG_NAME = "nbsp";

	/**
	 * 
	 */
	private static final long serialVersionUID = -3626274368156597064L;
	
	private int length;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
		StringBuilder buffer = new StringBuilder();
		for ( int k=0; k<length; k++ ) {
			buffer.append( " " );
		}
		this.setText( buffer.toString() );
	}

	public String toString() {
		return super.toString()+"[size:"+this.getSize()+"]";
	}
	
}
