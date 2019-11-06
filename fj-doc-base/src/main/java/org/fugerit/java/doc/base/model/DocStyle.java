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
 * @(#)DocStyle.java
 *
 * @project    : org.fugerit.java.doc.base
 * @package    : org.fugerit.java.doc.base
 * @creation   : 03/ott/06
 * @license	   : META-INF/LICENSE.TXT
 */
package org.fugerit.java.doc.base.model;

/**
 * <p></p>
 *
 * @author mfranci
 *
 */
public interface DocStyle {

	/**
	 * @return the backColor
	 */
	public String getBackColor();

	/**
	 * @param backColor the backColor to set
	 */
	public void setBackColor(String backColor);

	/**
	 * @return the foreColor
	 */
	public String getForeColor();

	/**
	 * @param foreColor the foreColor to set
	 */
	public void setForeColor(String foreColor);
	
}
