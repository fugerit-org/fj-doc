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
 * @(#)DocImage.java
 *
 * @project     : org.fugerit.java.doc.base
 * @package     : org.fugerit.java.doc.base
 * @creation	: 15/lug/07
 * @release		: xxxx.xx.xx
 */
package org.fugerit.java.doc.base.model;

import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.helper.SourceResolverHelper;

/**
 * 
 *
 * @author Fugerit
 *
 */
public class DocImage extends DocElement {

	private Integer scaling;
	
	private String url;
	
	private String base64;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the scaling
	 */
	public Integer getScaling() {
		return scaling;
	}

	/**
	 * @param scaling the scaling to set
	 */
	public void setScaling(Integer scaling) {
		this.scaling = scaling;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}
	
	public String getResolvedBase64() throws Exception {
		String res = this.getBase64();
		if ( StringUtils.isEmpty( res ) ) {
			res = SourceResolverHelper.resolveImageToBase64( this );
		}
		return res;
	}
	
}
