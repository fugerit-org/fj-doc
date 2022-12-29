package org.fugerit.java.doc.base.model;

public class DocList extends DocContainer {

	public static final String TAG_NAME = "list";
	
	public final static String LIST_TYPE_UL = "ul";
	
	public final static String LIST_TYPE_ULD = "uld";
	
	public final static String LIST_TYPE_ULM = "ulm";
	
	public final static String LIST_TYPE_OL = "ol";
	
	public final static String LIST_TYPE_OLN = "oln";
	
	public final static String LIST_TYPE_OLL = "oll";
	
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
	
	/*
	 * Return canonical list type
	 */
	public String getClt() {
		String clt = this.getListType();
		if (clt.equalsIgnoreCase( LIST_TYPE_UL ) ) {
			clt = LIST_TYPE_ULD;
		} else if ( clt == null || clt.equalsIgnoreCase(  LIST_TYPE_OL) ) {
			clt = LIST_TYPE_OLN;
		}
		return clt;
	}
	
	/*
	 * Return html list type
	 */
	public String getHtmlType() {
		String clt = this.getListType();
		if ( clt.equalsIgnoreCase( LIST_TYPE_ULM ) || clt.equalsIgnoreCase( LIST_TYPE_ULD ) ) {
			clt = LIST_TYPE_UL;
		} else if ( clt == null || clt.equalsIgnoreCase(  LIST_TYPE_OLN ) || clt.equalsIgnoreCase(  LIST_TYPE_OLL ) ) {
			clt = LIST_TYPE_OL;
		}
		return clt;
	}

}
