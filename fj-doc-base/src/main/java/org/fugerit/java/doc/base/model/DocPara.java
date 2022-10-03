/*****************************************************************
<copyright>
	Fugerit Java Library org.fugerit.java.doc.base 

	Copyright (c) 2019 Fugerit

	All rights reserved. This program and the accompanying materials
	are made available under the terms of the Apache License v2.0
	which accompanies this distribution, and is available at
	http://www.apache.org/licenses/
	(txt version : http://www.apache.org/licenses/LICENSE-2.0.txt
	html version : http://www.apache.org/licenses/LICENSE-2.0.html)

   This product includes software developed at
   The Apache Software Foundation (http://www.apache.org/).
</copyright>
*****************************************************************/
/*
 * @(#)DocPara.java
 *
 * @project    : org.fugerit.java.doc.base
 * @package    : org.fugerit.java.doc.base
 * @creation   : 06/set/06
 * @license	   : META-INF/LICENSE.TXT
 */
package org.fugerit.java.doc.base.model;

/**
 * 
 *
 * @author mfranci
 *
 */
public class DocPara extends DocContainer implements DocStyle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4246777398259149367L;

	public static final String TAG_NAME = "para";
	public static final String TAG_NAME_H = "h";
	
	public static final int DEFAULT_HEAD_LEVEL = 0;

	public static final int STYLE_NORMAL = 1;
	public static final int STYLE_BOLD = 2;
	public static final int STYLE_UNDERLINE = 3;
	public static final int STYLE_ITALIC = 4;
	public static final int STYLE_BOLDITALIC = 5;
	public static final int STYLE_UNSET = -1;
	
	public static final int ALIGN_UNSET = 0;
	// h align
	public static final int ALIGN_LEFT = 1;
	public static final int ALIGN_CENTER = 2;
	public static final int ALIGN_RIGHT = 3;
	public static final int ALIGN_JUSTIFY = 9;
	public static final int ALIGN_JUSTIFY_ALL = 8;
	// v align
	public static final int ALIGN_TOP = 4;
	public static final int ALIGN_MIDDLE = 5;
	public static final int ALIGN_BOTTOM = 6;	
	
	private int style;		// style with default value
	
	private int size;
	
	private String text;

	private String foreColor;
	
	private String backColor;	
	
	private String fontName;
	
	private String whiteSpaceCollapse;
	
	private int headLevel;
	
	private int originalStyle;		// style with unset value
	
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
	
	private Float textIndent;
	
	private Float spaceBefore;
	
	private Float spaceAfter;	
	
	private Float spaceLeft;
	
	private Float spaceRight;	
	
	public Float getTextIndent() {
		return textIndent;
	}

	public void setTextIndent(Float textIndent) {
		this.textIndent = textIndent;
	}

	public Float getSpaceBefore() {
		return spaceBefore;
	}

	public void setSpaceBefore(Float spaceBefore) {
		this.spaceBefore = spaceBefore;
	}

	public Float getSpaceAfter() {
		return spaceAfter;
	}

	public void setSpaceAfter(Float spaceAfter) {
		this.spaceAfter = spaceAfter;
	}

	public Float getSpaceLeft() {
		return spaceLeft;
	}

	public void setSpaceLeft(Float spaceLeft) {
		this.spaceLeft = spaceLeft;
	}

	public Float getSpaceRight() {
		return spaceRight;
	}

	public void setSpaceRight(Float spaceRight) {
		this.spaceRight = spaceRight;
	}

	private Float leading;
	
	public Float getLeading() {
		return leading;
	}

	public void setLeading(Float leading) {
		this.leading = leading;
	}

	public DocPara() {
		this.setText( "" );
	}
	
	public static int parseStyle( String style ) {
		return parseStyle( style, STYLE_NORMAL );
	}
	
	public static int parseStyle( String style, int defaultStype ) {
		int result = defaultStype;
		if ( "bold".equalsIgnoreCase( style ) ) {
			result = STYLE_BOLD;
		} else if ( "underline".equalsIgnoreCase( style ) ) {
			result = STYLE_UNDERLINE;
		} else if ( "italic".equalsIgnoreCase( style ) ) {
			result = STYLE_ITALIC;
		} else if ( "bolditalic".equalsIgnoreCase( style ) ) {
			result = STYLE_BOLDITALIC;
		} else if ( "normal".equalsIgnoreCase( style ) ) {
			result = STYLE_NORMAL;
		}
		return result;
	}
	
	private int align;

	private String format;
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the align
	 */
	public int getAlign() {
		return align;
	}

	/**
	 * @param align the align to set
	 */
	public void setAlign(int align) {
		this.align = align;
	}

	public int getHeadLevel() {
		return headLevel;
	}

	public void setHeadLevel(int headLevel) {
		this.headLevel = headLevel;
	}

	public String toString() {
		return super.toString()+"[text:"+this.getText()+"]";
	}

	public String getWhiteSpaceCollapse() {
		return whiteSpaceCollapse;
	}

	public void setWhiteSpaceCollapse(String whiteSpaceCollapse) {
		this.whiteSpaceCollapse = whiteSpaceCollapse;
	}

	public int getOriginalStyle() {
		return originalStyle;
	}

	public void setOriginalStyle(int originalStyle) {
		this.originalStyle = originalStyle;
	}
	
	

}
