package org.fugerit.java.doc.mod.poi;

import java.awt.Color;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.xml.DocModelUtils;

public class XlsPoiTypeHandler extends BasicPoiTypeHandler {

	public static final DocTypeHandler HANDLER = new XlsPoiTypeHandler();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7394516459275771708L;

	public XlsPoiTypeHandler() {
		super( DocConfig.TYPE_XLS );
	}

	@Override
	protected WorkbookHelper newWorkbook(DocInput docInput, InputStream is ) throws Exception {
		Workbook workbook = null;
		if ( is == null ) {
			workbook = new HSSFWorkbook();
		} else {
			workbook = new HSSFWorkbook( is );
		}
		return new WorkbookHelper( workbook, new DefaultIndexedColorMap() );
	}

	@Override
	protected void closeWorkbook(Workbook workbook, DocOutput docOutput) throws Exception {
		workbook.write( docOutput.getOs() );
		workbook.close();
	}

	public static short findClosestColorIndex( HSSFWorkbook workbook, String color ) {
		Color c = DocModelUtils.parseHtmlColor( color );
		HSSFPalette palette = workbook.getCustomPalette();
		HSSFColor current = palette.findSimilarColor( c.getRed() , c.getGreen(), c.getBlue() );
		return current.getIndex();
	}
	
	@Override
	protected void setFormatStyle( WorkbookHelper helper, Font font, CellStyle style, DocCell cell, DocPara para) throws Exception {
		PoiUtils.xlsFormatStyle(helper, style, cell);
	}

	@Override
	protected void setFontStyle( WorkbookHelper helper, Font font, CellStyle style, DocCell cell, DocPara para) throws Exception {
		PoiUtils.xlsFontStyle(helper, font, style, cell);
	}

}