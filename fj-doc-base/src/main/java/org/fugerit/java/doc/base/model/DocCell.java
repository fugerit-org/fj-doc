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
 * @(#)DocCell�.java
 *
 * @project    : org.fugerit.java.doc.base
 * @package    : org.fugerit.java.doc.base
 * @creation   : 06/set/06
 * @license	   : META-INF/LICENSE.TXT
 */
package org.fugerit.java.doc.base.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 *
 * @author mfranci
 *
 */
public class DocCell extends DocContainer implements DocStyle {
	
	public static final String TAG_NAME = "cell";

	public static final String ATTRIBUTE_NAME_COLSPAN = "colspan";

	public static final String ATTRIBUTE_NAME_ROWSPAN = "rowspan";

	/**
	 * 
	 */
	private static final long serialVersionUID = -6769143730899701281L;
	
	@Override
	public String toString() {
		return super.toString()+"[align:"+this.getAlign()+"]";
	}
	
	public boolean isHeader() {
		return header;
	}

	public void setHeader(boolean header) {
		this.header = header;
	}

	private boolean header;
	
	@Setter @Getter private int align;
	
	@Setter @Getter private int valign;
	
	@Setter @Getter private String type;
	
	@Setter @Getter private int cSpan;
	
	@Setter @Getter private int rSpan;
	
	@Setter @Getter private String foreColor;
	
	@Setter @Getter private String backColor;
	
	@Setter @Getter private DocBorders docBorders;
	
	public int getRowSpan() {
		int res = this.getRSpan();
		if ( this.getRSpan() < 1 ) {
			res = 1;
		}
		return res;
	}
	
	public int getColumnSpan() {
		int res = this.getCSpan();
		if ( this.getCSpan() < 1 ) {
			res = 1;
		}
		return res;
	}
	
}
