package org.fugerit.java.doc.base.model;

import java.util.Properties;

import org.fugerit.java.doc.base.parser.DocParserUtil;

import lombok.Data;

/**
 * 
 *
 * @author Matteo Franci a.k.a. Fugerit
 *
 */
@Data
public class DocBorders {

	public static final String ATTRIBUTE_NAME_BORDER_WIDTH = "border-width";
	
	public static final String ATTRIBUTE_NAME_BORDER_WIDTH_BOTTOM = "border-width-bottom";
	
	public static final String ATTRIBUTE_NAME_BORDER_WIDTH_TOP = "border-width-top";
	
	public static final String ATTRIBUTE_NAME_BORDER_WIDTH_LEFT = "border-width-left";
	
	public static final String ATTRIBUTE_NAME_BORDER_WIDTH_RIGHT = "border-width-right";
	
	public static final String ATTRIBUTE_NAME_BORDER_COLOR = "border-color";
	
	public static final String ATTRIBUTE_NAME_BORDER_COLOR_BOTTOM = "border-color-bottom";
	
	public static final String ATTRIBUTE_NAME_BORDER_COLOR_TOP = "border-color-top";
	
	public static final String ATTRIBUTE_NAME_BORDER_COLOR_LEFT = "border-color-left";
	
	public static final String ATTRIBUTE_NAME_BORDER_COLOR_RIGHT = "border-color-right";
	
	public static final String ATTRIBUTE_NAME_PADDING = "padding";
	
	public static final String ATTRIBUTE_NAME_PADDING_BOTTOM = "padding-bottom";
	
	public static final String ATTRIBUTE_NAME_PADDING_TOP = "padding-top";
	
	public static final String ATTRIBUTE_NAME_PADDING_LEFT = "padding-left";
	
	public static final String ATTRIBUTE_NAME_PADDING_RIGHT = "padding-right";
	
	public Object clone() throws CloneNotSupportedException {
		DocBorders borders = new DocBorders();
		borders.setBorderWidthBottom( this.getBorderWidthBottom() );
		borders.setBorderWidthTop( this.getBorderWidthTop() );
		borders.setBorderWidthRight( this.getBorderWidthRight() );
		borders.setBorderWidthLeft( this.getBorderWidthLeft() );
		return borders;
	}

	private int paddingBottom;
	
	private int paddingTop;
	
	private int paddingRight;
	
	private int paddingLeft;
	
	private String borderColorTop;
	
	private String borderColorBottom;
	
	private String borderColorLeft;
	
	private String borderColorRight;
	
	private int borderWidthTop;

	private int borderWidthBottom;
	
	private int borderWidthLeft;

	private int borderWidthRight;

	public static DocBorders createBorders( Properties atts, String defaultBorderWidth ) {
		return createBorders(atts, defaultBorderWidth, DocElement.UNSET);
	}
	
	public static DocBorders createBorders( Properties atts, String defaultBorderWidth, String defaultPadding ) {
		DocBorders docBorders = new DocBorders();
		docBorders.setBorderColorBottom( DocParserUtil.doubleNested(atts, ATTRIBUTE_NAME_BORDER_COLOR_BOTTOM, ATTRIBUTE_NAME_BORDER_COLOR) );
		docBorders.setBorderColorTop( DocParserUtil.doubleNested(atts, ATTRIBUTE_NAME_BORDER_COLOR_TOP, ATTRIBUTE_NAME_BORDER_COLOR) );
		docBorders.setBorderColorLeft( DocParserUtil.doubleNested(atts, ATTRIBUTE_NAME_BORDER_COLOR_LEFT, ATTRIBUTE_NAME_BORDER_COLOR) );
		docBorders.setBorderColorRight( DocParserUtil.doubleNested(atts, ATTRIBUTE_NAME_BORDER_COLOR_RIGHT, ATTRIBUTE_NAME_BORDER_COLOR) );
		docBorders.setBorderWidthBottom( DocParserUtil.doubleNestedWithDefaultInt(atts, ATTRIBUTE_NAME_BORDER_WIDTH_BOTTOM, ATTRIBUTE_NAME_BORDER_WIDTH, defaultBorderWidth) );
		docBorders.setBorderWidthTop( DocParserUtil.doubleNestedWithDefaultInt(atts, ATTRIBUTE_NAME_BORDER_WIDTH_TOP, ATTRIBUTE_NAME_BORDER_WIDTH, defaultBorderWidth) );
		docBorders.setBorderWidthLeft( DocParserUtil.doubleNestedWithDefaultInt(atts, ATTRIBUTE_NAME_BORDER_WIDTH_LEFT, ATTRIBUTE_NAME_BORDER_WIDTH, defaultBorderWidth) );
		docBorders.setBorderWidthRight( DocParserUtil.doubleNestedWithDefaultInt(atts, ATTRIBUTE_NAME_BORDER_WIDTH_RIGHT, ATTRIBUTE_NAME_BORDER_WIDTH, defaultBorderWidth) );
		docBorders.setPaddingBottom( DocParserUtil.doubleNestedWithDefaultInt(atts, ATTRIBUTE_NAME_PADDING_BOTTOM, ATTRIBUTE_NAME_PADDING, defaultPadding) );
		docBorders.setPaddingTop( DocParserUtil.doubleNestedWithDefaultInt(atts, ATTRIBUTE_NAME_PADDING_TOP, ATTRIBUTE_NAME_PADDING, defaultPadding) );
		docBorders.setPaddingLeft( DocParserUtil.doubleNestedWithDefaultInt(atts, ATTRIBUTE_NAME_PADDING_LEFT, ATTRIBUTE_NAME_PADDING, defaultPadding) );
		docBorders.setPaddingRight( DocParserUtil.doubleNestedWithDefaultInt(atts, ATTRIBUTE_NAME_PADDING_RIGHT, ATTRIBUTE_NAME_PADDING, defaultPadding) );
		return docBorders;
	}
	
}
