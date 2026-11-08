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

	private static int resolveFontSize( int fontSize, OpenPdfHelper docHelper ) {
		if ( fontSize > 0 ) {
			return fontSize;
		}
		if ( docHelper.getDefFontSize() != null ) {
			return Integer.parseInt( docHelper.getDefFontSize() );
		}
		return 10;
	}

	private static String resolveFontName( String inputFontName, OpenPdfHelper docHelper ) {
		if ( inputFontName != null ) {
			return inputFontName;
		}
		if ( docHelper.getDefFontName() != null ) {
			return docHelper.getDefFontName();
		}
		return "helvetica";
	}
	
	public static Font createFont( String inputFontName, String fontPath, int fontSize, int fontStyle, OpenPdfHelper docHelper, String color ) throws DocumentException, IOException {
		int size = resolveFontSize( fontSize, docHelper );
		int style = handleFontStyle( Font.NORMAL, fontStyle );
		String fontName = resolveFontName( inputFontName, docHelper );
		BaseFont bf = OpenPpfDocHandler.findFont( fontName );
		int bfV = -1;
		if ( bf == null ) {
			bfV = resolveStandardFont( fontName );
			bf = (bfV == -1) ? resolveCustomFont( fontName, fontPath ) : null;
		}
		Color c = color != null ? DocModelUtils.parseHtmlColor( color ) : Color.BLACK;
		return (bfV == -1) ? new Font( bf, (float) size, style, c ) : new Font( bfV, (float) size, style, c );
	}
	
}
