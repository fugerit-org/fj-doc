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

import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.doc.base.enums.EnumDocAlignH;
import org.fugerit.java.doc.base.enums.EnumDocAlignV;
import org.fugerit.java.doc.base.enums.EnumDocStyle;
import org.fugerit.java.doc.base.xml.DocStyleAlignHelper;

import lombok.Getter;
import lombok.Setter;

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

	public static final int STYLE_NORMAL = EnumDocStyle.STYLE_NORMAL.getId();
	public static final int STYLE_BOLD = EnumDocStyle.STYLE_BOLD.getId();
	public static final int STYLE_UNDERLINE = EnumDocStyle.STYLE_UNDERLINE.getId();
	public static final int STYLE_ITALIC = EnumDocStyle.STYLE_ITALIC.getId();
	public static final int STYLE_BOLDITALIC = EnumDocStyle.STYLE_BOLDITALIC.getId();
	public static final int STYLE_UNSET = EnumDocStyle.STYLE_UNSET.getId();
	
	public static final int ALIGN_UNSET = 0;
	// h align
	public static final int ALIGN_LEFT = EnumDocAlignH.ALIGN_LEFT.getId();
	public static final int ALIGN_CENTER = EnumDocAlignH.ALIGN_CENTER.getId();
	public static final int ALIGN_RIGHT = EnumDocAlignH.ALIGN_RIGHT.getId();
	public static final int ALIGN_JUSTIFY = EnumDocAlignH.ALIGN_JUSTIFY.getId();
	public static final int ALIGN_JUSTIFY_ALL = EnumDocAlignH.ALIGN_JUSTIFY_ALL.getId();
	// v align
	public static final int ALIGN_TOP = EnumDocAlignV.ALIGN_TOP.getId();
	public static final int ALIGN_MIDDLE = EnumDocAlignV.ALIGN_MIDDLE.getId();
	public static final int ALIGN_BOTTOM = EnumDocAlignV.ALIGN_BOTTOM.getId();
	
	@Getter @Setter private int style;		// style with default value
	
	@Getter @Setter private int originalStyle;		// style with unset value
	
	@Getter @Setter private int size;
	
	@Getter @Setter private String text;

	@Getter @Setter private String foreColor;
	
	@Getter @Setter private String backColor;	
	
	@Getter @Setter private String fontName;
	
	@Getter @Setter private String whiteSpaceCollapse;
	
	@Getter @Setter private int headLevel;
	
	@Getter @Setter private Float textIndent;
	
	@Getter @Setter private Float spaceBefore;
	
	@Getter @Setter private Float spaceAfter;	
	
	@Getter @Setter private Float spaceLeft;
	
	@Getter @Setter private Float spaceRight;	

	@Getter @Setter private Float leading;
	
	@Getter @Setter private int align;

	@Getter @Setter private String format;
	
	@Getter @Setter private String type;

	@Getter @Setter private String className;

	public boolean isNotWhiteSpaceCollapse() {
		return BooleanUtils.BOOLEAN_FALSE.equalsIgnoreCase( this.getWhiteSpaceCollapse() );
	}
	
	public DocPara() {
		this.setText( "" );
	}
	
	public static int parseStyle( String style ) {
		return parseStyle( style, STYLE_NORMAL );
	}
	
	public static int parseStyle( String style, int defaultStype ) {
		return DocStyleAlignHelper.parseStyle(style, defaultStype);
	}
	
	@Override
	public String toString() {
		return super.toString()+"[text:"+this.getText()+"]";
	}
	
}
