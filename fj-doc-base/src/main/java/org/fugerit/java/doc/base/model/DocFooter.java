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
 * @(#)DocFooter.java
 *
 * @project     : org.fugerit.java.doc.base
 * @package     : org.fugerit.java.doc.base
 * @creation	: 16/lug/07
 * @release		: xxxx.xx.xx
 */
package org.fugerit.java.doc.base.model;

/**
 * 
 *
 * @author Fugerit
 *
 */
public class DocFooter extends DocHeaderFooter {

	public DocFooter() {
		this.useFooter = false;
	}
	
	private boolean useFooter;

	/**
	 * @return the useFooter
	 */
	public boolean isUseFooter() {
		return useFooter;
	}

	/**
	 * @param useFooter the useFooter to set
	 */
	public void setUseFooter(boolean useFooter) {
		this.useFooter = useFooter;
	}
	
}
