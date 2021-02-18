package org.fugerit.java.doc.base.model;

public class DocList extends DocContainer {

	public final static String LIST_TYPE_UL = "ul";
	
	public final static String LIST_TYPE_OL = "ol";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8555789702608296306L;

	private String listType;

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}
	
}
