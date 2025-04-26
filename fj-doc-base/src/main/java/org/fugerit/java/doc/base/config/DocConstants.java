package org.fugerit.java.doc.base.config;

import org.fugerit.java.doc.base.enums.EnumDocAlignH;
import org.fugerit.java.doc.base.enums.EnumDocAlignV;
import org.fugerit.java.doc.base.enums.EnumDocStyle;

public class DocConstants {

	public static final String EURO_CONST = "€";
	
	private static final DocConstants DEF = new DocConstants();
	
	public static DocConstants getInstance() {
		return DEF;
	}
	
	public String getEuro() {
		return EURO_CONST;
	}

	public static final String STYLE_BOLD = EnumDocStyle.STYLE_BOLD.getValue();
	public static final String STYLE_UNDERLINE = EnumDocStyle.STYLE_UNDERLINE.getValue();
	public static final String STYLE_ITALIC = EnumDocStyle.STYLE_ITALIC.getValue();
	public static final String STYLE_BOLDITALIC = EnumDocStyle.STYLE_BOLDITALIC.getValue();
	public static final String STYLE_NORMAL = EnumDocStyle.STYLE_NORMAL.getValue();

	public static final String ALIGN_CENTER = EnumDocAlignH.ALIGN_CENTER.getValue();
	public static final String ALIGN_RIGHT = EnumDocAlignH.ALIGN_RIGHT.getValue();
	public static final String ALIGN_LEFT = EnumDocAlignH.ALIGN_LEFT.getValue();
	public static final String ALIGN_JUSTIFY = EnumDocAlignH.ALIGN_JUSTIFY.getValue();
	public static final String ALIGN_JUSTIFYALL = EnumDocAlignH.ALIGN_JUSTIFY_ALL.getValue();
	
	public static final String VALIGN_MIDDLE = EnumDocAlignV.ALIGN_MIDDLE.getValue();
	public static final String VALIGN_TOP = EnumDocAlignV.ALIGN_TOP.getValue();
	public static final String VALIGN_BOTTOM = EnumDocAlignV.ALIGN_BOTTOM.getValue();
	
}
