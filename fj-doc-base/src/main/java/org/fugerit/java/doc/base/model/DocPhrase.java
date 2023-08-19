package org.fugerit.java.doc.base.model;

public class DocPhrase extends DocElement implements DocStyle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -880687263688988196L;

	public static final String TAG_NAME = "phrase";
	
	public DocPhrase() {
		this.text = "";
	}
	
	private int style;		// style with default value
	
	private int size;
	
	private String text;
	
	private String foreColor;
	
	private String backColor;	
		
	private String fontName;
	
	private Float leading;
	
	private String link;
	
	private String anchor;
	
	private String whiteSpaceCollapse;
	
	private int originalStyle;		// style with unset value
	
	public String getWhiteSpaceCollapse() {
		return whiteSpaceCollapse;
	}

	public void setWhiteSpaceCollapse(String whiteSpaceCollapse) {
		this.whiteSpaceCollapse = whiteSpaceCollapse;
	}

	public Float getLeading() {
		return leading;
	}

	public void setLeading(Float leading) {
		this.leading = leading;
	}
	
	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	
	public String getForeColor() {
		return foreColor;
	}



	public void setForeColor(String foreColor) {
		this.foreColor = foreColor;
	}



	public String getBackColor() {
		return backColor;
	}



	public void setBackColor(String backColor) {
		this.backColor = backColor;
	}



	public int getStyle() {
		return style;
	}



	public void setStyle(int style) {
		this.style = style;
	}



	public int getSize() {
		return size;
	}



	public void setSize(int size) {
		this.size = size;
	}



	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return super.toString()+"[text:"+this.getText()+"]";
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	public int getOriginalStyle() {
		return originalStyle;
	}

	public void setOriginalStyle(int originalStyle) {
		this.originalStyle = originalStyle;
	}
	
	public String getInternalLink() {
		return this.getLink().substring( 1 );
	}
	
	public boolean isInternal() {
		return this.getLink() != null && this.getLink().startsWith( "#" );
	}
	
}
