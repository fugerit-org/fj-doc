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

/**
 * 
 *
 * @author mfranci
 *
 */
public class DocCell extends DocContainer implements DocStyle {
	
	public DocCell() {
	}
	
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
	
	private int align;
	
	private int valign;

	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getValign() {
		return valign;
	}

	public void setValign(int valign) {
		this.valign = valign;
	}

	private int cSpan;
	
	private int rSpan;
	
	private String foreColor;
	
	private String backColor;
	
	private DocBorders docBorders;

	/**
	 * @return the docBorders
	 */
	public DocBorders getDocBorders() {
		return docBorders;
	}

	/**
	 * @param docBorders the docBorders to set
	 */
	public void setDocBorders(DocBorders docBorders) {
		this.docBorders = docBorders;
	}

	/**
	 * @return the cSpan
	 */
	public int getCSpan() {
		return cSpan;
	}

	/**
	 * @param span the cSpan to set
	 */
	public void setCSpan(int span) {
		cSpan = span;
	}

	/**
	 * @return the rSpan
	 */
	public int getRSpan() {
		return rSpan;
	}

	/**
	 * @param span the rSpan to set
	 */
	public void setRSpan(int span) {
		rSpan = span;
	}

	/**
	 * @return the align
	 */
	public int getAlign() {
		return align;
	}

	/**
	 * @param align the align to set
	 */
	public void setAlign(int align) {
		this.align = align;
	}

	/**
	 * @return the backColor
	 */
	public String getBackColor() {
		return backColor;
	}

	/**
	 * @param backColor the backColor to set
	 */
	public void setBackColor(String backColor) {
		this.backColor = backColor;
	}

	/**
	 * @return the foreColor
	 */
	public String getForeColor() {
		return foreColor;
	}

	/**
	 * @param foreColor the foreColor to set
	 */
	public void setForeColor(String foreColor) {
		this.foreColor = foreColor;
	}
	
}
