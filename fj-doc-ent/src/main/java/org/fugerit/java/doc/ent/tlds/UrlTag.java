/****************************************************************
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
 * @(#)UrlTag.java
 *
 * @project     : org.fugerit.java.doc.ent
 * @package     : org.fugerit.java.doc.ent.tlds
 * @creation	: 16/lug/07
 * @release		: xxxx.xx.xx
 */
package org.fugerit.java.doc.ent.tlds;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/*
 * 
 *
 * @author Fugerit
 *
 */
public class UrlTag extends TagSupport {

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
		StringBuffer buffer = new StringBuffer();
		if ( "true".equalsIgnoreCase( this.getRelative() ) ) {
			HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
			buffer.append( request.getScheme() );
			buffer.append( "://" );
			buffer.append( request.getServerName() );
			buffer.append( ":" );
			buffer.append( request.getServerPort() );
			buffer.append( request.getContextPath() );
		}
		buffer.append( this.getUrl() );
		try {
			this.pageContext.getOut().print( buffer.toString() );
		} catch (Exception e) {}
		return EVAL_PAGE;
	}

	/*
	 * 
	 */
	private static final long serialVersionUID = -6000737096593291817L;

	private String url;
	
	private String relative;

	/*
	 * @return the relative
	 */
	public String getRelative() {
		return relative;
	}

	/*
	 * @param relative the relative to set
	 */
	public void setRelative(String relative) {
		this.relative = relative;
	}

	/*
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/*
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
}
