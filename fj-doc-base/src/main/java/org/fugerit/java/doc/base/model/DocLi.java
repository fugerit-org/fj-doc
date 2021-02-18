package org.fugerit.java.doc.base.model;

public class DocLi extends DocContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8555789702608296306L;
	
	public DocElement getContent() {
		return this.getElementList().get( 0 ); 
	}
	
}
