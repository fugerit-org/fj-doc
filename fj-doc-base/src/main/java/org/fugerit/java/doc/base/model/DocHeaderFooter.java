/*****************************************************************
<copyright>
	Fugerit Java Library 

	Copyright (c) 2007 Fugerit

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
 * @(#)DocHeaderFooter.java
 *
 * @project     : org.fugerit.java.doc.base
 * @package     : org.fugerit.java.doc.base
 * @creation	: 23/ago/07
 * @release		: xxxx.xx.xx
 */
package org.fugerit.java.doc.base.model;

/**
 * 
 *
 * @author Fugerit
 *
 */
public class DocHeaderFooter extends DocContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2131910639031770942L;

	public DocHeaderFooter() {
		this.basic = true;
	}
	
	private boolean basic;
	
	public boolean isBasic() {
		return basic;
	}

	public void setBasic(boolean basic) {
		this.basic = basic;
	}

	private int borderWidth;
	
	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}

	private int align;

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
	
	private boolean numbered;

	/**
	 * @return the numbered
	 */
	public boolean isNumbered() {
		return numbered;
	}

	/**
	 * @param numbered the numbered to set
	 */
	public void setNumbered(boolean numbered) {
		this.numbered = numbered;
	}
	
	private int expectedSize;

	public int getExpectedSize() {
		return expectedSize;
	}

	public void setExpectedSize(int expectedSize) {
		this.expectedSize = expectedSize;
	}
	
}
