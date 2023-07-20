package org.fugerit.java.doc.mod.poi5;

import java.awt.Color;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.xml.DocModelUtils;

public class XlsxPoi5TypeHandler extends BasicPoiTypeHandler {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4886914985225735861L;

	public static DocTypeHandler HANDLER = new XlsxPoi5TypeHandler();
	
	public XlsxPoi5TypeHandler() {
		super( DocConfig.TYPE_XLSX );
	}

	@Override
	protected WorkbookHelper newWorkbook( DocInput docInput , InputStream is ) throws Exception {
		Workbook workbook = null;
		if ( is == null ) {
			workbook = new XSSFWorkbook();
		} else {
			workbook = new XSSFWorkbook( is );
		}
		return new WorkbookHelper( workbook, new DefaultIndexedColorMap() );
	}

	@Override
	protected void closeWorkbook(Workbook workbook, DocOutput docOutput) throws Exception {
		workbook.write( docOutput.getOs() );
		workbook.close();
	}
	
	@Override
	protected void setFormatStyle( WorkbookHelper helper, Font font, CellStyle style, DocCell cell, DocPara para ) throws Exception {
		if ( style instanceof XSSFCellStyle ) {
			XSSFCellStyle realStyle = (XSSFCellStyle) style;
			if ( cell != null ) {
				if ( StringUtils.isNotEmpty( cell.getBackColor() ) ) {
					Color c = DocModelUtils.parseHtmlColor( cell.getBackColor() );
					realStyle.setFillForegroundColor( new XSSFColor( c , helper.getIndexedColorMap() ) );
					realStyle.setFillPattern( FillPatternType.SOLID_FOREGROUND );
				}
			}
		}
	}

	@Override
	protected void setFontStyle( WorkbookHelper helper, Font font, CellStyle style, DocCell cell, DocPara para) throws Exception {
		if ( font instanceof XSSFFont ) {
			XSSFFont realFont = (XSSFFont) font;
			if ( StringUtils.isNotEmpty( cell.getForeColor() ) ) {
				Color c = DocModelUtils.parseHtmlColor( cell.getForeColor() );
				realFont.setColor( new XSSFColor( c, helper.getIndexedColorMap() ) );
			}
		}
	}	

}