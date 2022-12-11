package org.fugerit.java.doc.lib.simpletable.model;

import org.fugerit.java.doc.base.config.DocConstants;

public class SimpleCell {

	public static final int BORDER_WIDTH_UNSET = -1;
	
	private String content;
	
	private int borderWidth;
	
	private String style;
	
	private String align;

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

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}
	
	private SimpleCell alignWorker( String align ) {
		this.setAlign( align );
		return this;
	}
	
	private SimpleCell styleWorker( String style ) {
		this.setStyle( style );
		return this;
	}
	
	public SimpleCell bold() {
		return this.styleWorker( DocConstants.STYLE_BOLD );
	}
	
	public SimpleCell bolditalic() {
		return this.styleWorker( DocConstants.STYLE_BOLDITALIC );
	}
	
	public SimpleCell italic() {
		return this.styleWorker( DocConstants.STYLE_ITALIC );
	}
	
	public SimpleCell underline() {
		return this.styleWorker( DocConstants.STYLE_UNDERLINE );
	}
	
	public SimpleCell center() {
		return this.alignWorker( DocConstants.ALIGN_CENTER );
	}
	
	public SimpleCell rigt() {
		return this.alignWorker( DocConstants.ALIGN_RIGHT );
	}
	
	public SimpleCell left() {
		return this.alignWorker( DocConstants.ALIGN_LEFT );
	}
	
	public static SimpleCell newCell( String content ) {
		return new SimpleCell( content );
	}
	
}
