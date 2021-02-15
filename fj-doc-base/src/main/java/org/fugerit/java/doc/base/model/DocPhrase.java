package org.fugerit.java.doc.base.model;

public class DocPhrase extends DocElement implements DocStyle {

	public DocPhrase() {
		this.text = "";
	}
	
	private int style;
	
	private int size;
	
	private String text;
	
	private String foreColor;
	
	private String backColor;	
		
	private String fontName;
	
	private Float leading;
	
	private String link;
	
	private String anchor;
	
	private String whiteSpaceCollapse;
	
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
	
	
	
}
