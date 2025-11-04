package org.fugerit.java.doc.mod.poi;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocPara;

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
	protected WorkbookHelper newWorkbook(DocInput docInput, InputStream is ) throws IOException {
		return PoiUtils.newHelper(false, is, docInput.getDoc());
	}

	@Override
	protected void closeWorkbook(Workbook workbook, DocOutput docOutput) throws IOException {
		PoiUtils.closeWorkbook(workbook, docOutput);
	}
	
	@Override
	protected void setFormatStyle( WorkbookHelper helper, Font font, CellStyle style, DocCell cell, DocPara para) {
		PoiUtils.xlsFormatStyle(helper, style, cell);
	}

	@Override
	protected void setFontStyle( WorkbookHelper helper, Font font, CellStyle style, DocCell cell, DocPara para) {
		PoiUtils.xlsFontStyle(helper, font, cell);
	}

}