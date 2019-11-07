package org.fugerit.java.doc.ent.tlds;

import javax.servlet.jsp.JspException;

import org.fugerit.java.core.web.tld.helpers.TagSupportHelper;

public class TypeTag extends TagSupportHelper {

	/*
	 * 
	 */
	private static final long serialVersionUID = 2342342342L;

	public int doStartTag() throws JspException {
		this.pageContext.getRequest().setAttribute( this.getId() , this.pageContext.getRequest().getAttribute( "doc.render.type" ) );
		return EVAL_PAGE;
	}

}
