package org.fugerit.java.doc.base.model;

import lombok.Getter;
import lombok.Setter;

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

	@Getter @Setter private String listType;
	
	/*
	 * Return canonical list type
	 */
	public String getClt() {
		String clt = this.getListType();
		if ( LIST_TYPE_UL.equalsIgnoreCase( clt ) ) {
			clt = LIST_TYPE_ULD;
		} else if ( LIST_TYPE_OL.equalsIgnoreCase( clt ) ) {
			clt = LIST_TYPE_OLN;
		}
		return clt;
	}
	
	/*
	 * Return html list type
	 */
	public String getHtmlType() {
		String clt = this.getListType();
		if ( LIST_TYPE_ULM.equalsIgnoreCase( clt ) ||  LIST_TYPE_ULD.equalsIgnoreCase( clt ) ) {
			clt = LIST_TYPE_UL;
		} else if ( LIST_TYPE_OLN.equalsIgnoreCase( clt ) ||  LIST_TYPE_OLL.equalsIgnoreCase( clt ) ) {
			clt = LIST_TYPE_OL;
		}
		return clt;
	}

}
