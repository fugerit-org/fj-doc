package org.fugerit.java.doc.base.model;

public class DocLi extends DocContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8555789702608296306L;
	
	public static final String TAG_NAME = "li";
	
	public DocElement getContent() {
		return this.getElementList().get( 0 ); 
	}
	
	public DocElement getSecondContent() {
		return this.getElementList().get( 1 ); 
	}
	
	public boolean isContentList() {
		return this.getContent().getClass().equals( DocList.class );
	}

	public boolean isSecondList() {
		return this.getElementList().size() > 1 && this.getSecondContent().getClass().equals( DocList.class );
	}
	
}
