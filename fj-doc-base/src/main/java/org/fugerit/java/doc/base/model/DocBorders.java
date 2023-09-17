package org.fugerit.java.doc.base.model;

import java.io.Serializable;
import java.util.Properties;

import org.fugerit.java.doc.base.parser.DocParserUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 *
 * @author Matteo Franci a.k.a. Fugerit
 *
 */
public class DocBorders implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5243241968341771053L;

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
	
	public DocBorders() {
	}
	
	public DocBorders( DocBorders from ) {
		if ( from != null ) {
			this.setBorderWidthBottom( from.getBorderWidthBottom() );
			this.setBorderWidthTop( from.getBorderWidthTop() );
			this.setBorderWidthRight( from.getBorderWidthRight() );
			this.setBorderWidthLeft( from.getBorderWidthLeft() );
			this.setBorderColorBottom( from.getBorderColorBottom() );
			this.setBorderColorTop( from.getBorderColorTop() );
			this.setBorderColorRight( from.getBorderColorRight() );
			this.setBorderColorLeft( from.getBorderColorLeft() );
			this.setPaddingBottom( from.getPaddingBottom() );
			this.setPaddingTop( from.getPaddingTop() );
			this.setPaddingLeft( from.getPaddingLeft() );
			this.setPaddingRight( from.getPaddingRight() );
		}
	}

	@Getter @Setter private int paddingBottom;
	
	@Getter @Setter private int paddingTop;
	
	@Getter @Setter private int paddingRight;
	
	@Getter @Setter private int paddingLeft;
	
	@Getter @Setter private String borderColorTop;
	
	@Getter @Setter private String borderColorBottom;
	
	@Getter @Setter private String borderColorLeft;
	
	@Getter @Setter private String borderColorRight;
	
	@Getter @Setter private int borderWidthTop;

	@Getter @Setter private int borderWidthBottom;
	
	@Getter @Setter private int borderWidthLeft;

	@Getter @Setter private int borderWidthRight;

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
