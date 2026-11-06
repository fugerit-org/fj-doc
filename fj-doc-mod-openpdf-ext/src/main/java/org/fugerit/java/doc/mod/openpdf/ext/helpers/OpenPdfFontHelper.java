package org.fugerit.java.doc.mod.openpdf.ext.helpers;

import java.awt.Color;
import java.io.IOException;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.xml.DocModelUtils;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

public class OpenPdfFontHelper {

	private OpenPdfFontHelper() {}
	
	public static BaseFont createBaseFontSafe( String name, String encoding, boolean embedded) {
		BaseFont result = null;
		try {
			result = BaseFont.createFont(name, encoding, embedded);
		} catch (DocumentException | IOException e) {
			throw new ConfigRuntimeException( e );
		}
		return result;
	}
	
	private static int handleFontStyle( int styleValue, int fontStyle ) {
		int style = styleValue;
		if ( fontStyle == DocPara.STYLE_BOLD ) {
			style = Font.BOLD;
		} else if ( fontStyle == DocPara.STYLE_UNDERLINE ) {
			style = Font.UNDERLINE;
		} else if ( fontStyle == DocPara.STYLE_ITALIC ) {
			style = Font.ITALIC;
		} else if ( fontStyle == DocPara.STYLE_BOLDITALIC ) {
			style = Font.BOLDITALIC;
		}
		return style;
	}

	private static int resolveStandardFont( String fontName ) {
		if ( "helvetica".equalsIgnoreCase( fontName ) ) {
			return Font.HELVETICA;
		} else if ( "courier".equalsIgnoreCase( fontName ) ) {
			return Font.COURIER;
		} else if ( "times-roman".equalsIgnoreCase( fontName ) ) {
			return Font.TIMES_ROMAN;
		} else if ( "symbol".equalsIgnoreCase( fontName ) ) {
			return Font.SYMBOL;
		}
		return -1;
	}

	private static BaseFont resolveCustomFont( String fontName, String fontPath ) throws DocumentException, IOException {
		BaseFont bf = OpenPpfDocHandler.findFont( fontName );
		if ( bf == null ) {
			bf = OpenPpfDocHandler.registerFont( fontName, fontPath );
		}
		return bf;
	}
	
	public static Font createFont( String inputFontName, String fontPath, int fontSize, int fontStyle, OpenPdfHelper docHelper, String color ) throws DocumentException, IOException {
		int size = fontSize <= 0 
				? (docHelper.getDefFontSize() != null ? Integer.parseInt( docHelper.getDefFontSize() ) : 10) 
				: fontSize;
		int style = handleFontStyle( Font.NORMAL, fontStyle );
		String fontName = inputFontName != null ? inputFontName : (docHelper.getDefFontName() != null ? docHelper.getDefFontName() : "helvetica");
		int bfV = resolveStandardFont( fontName );
		BaseFont bf = (bfV == -1) ? resolveCustomFont( fontName, fontPath ) : null;
		Color c = color != null ? DocModelUtils.parseHtmlColor( color ) : Color.BLACK;
		return (bfV == -1) ? new Font( bf, size, style, c ) : new Font( bfV, size, style, c );
	}
	
}
