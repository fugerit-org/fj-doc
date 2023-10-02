package org.fugerit.java.doc.base.model;

import lombok.Getter;
import lombok.Setter;

public class DocBarcode extends DocElement {
	
	public static final String TAG_NAME = "barcode";

	/**
	 * 
	 */
	private static final long serialVersionUID = 887515352642661380L;

	@Getter @Setter private int size;
	
	@Getter @Setter private String text;
	
	@Getter @Setter private String type;
	
}
