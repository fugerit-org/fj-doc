package org.fugerit.java.doc.lib.simpletable.model;

public class SimpleCell {

	public static final int BORDER_WIDTH_UNSET = -1;
	
	private String content;
	
	private int borderWidth;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}
	
	public SimpleCell(String content) {
		super();
		this.content = content;
		this.borderWidth = BORDER_WIDTH_UNSET;
	}

	public SimpleCell(String content, int borderWidth) {
		super();
		this.content = content;
		this.borderWidth = borderWidth;
	}
	
}
