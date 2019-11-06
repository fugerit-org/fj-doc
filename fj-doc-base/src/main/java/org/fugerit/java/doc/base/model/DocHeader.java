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
 * @(#)DocHeader.java
 *
 * @project     : org.fugerit.java.doc.base
 * @package     : org.fugerit.java.doc.base
 * @creation	: 16/lug/07
 * @release		: xxxx.xx.xx
 */
package org.fugerit.java.doc.base.model;

/**
 * <p>/p>
 *
 * @author Fugerit
 *
 */
public class DocHeader extends DocHeaderFooter {

	public DocHeader() {
		this.useHeader = false;
	}
	
	private boolean useHeader;

	/**
	 * @return the useHeader
	 */
	public boolean isUseHeader() {
		return useHeader;
	}

	/**
	 * @param useHeader the useHeader to set
	 */
	public void setUseHeader(boolean useHeader) {
		this.useHeader = useHeader;
	}
	
}
