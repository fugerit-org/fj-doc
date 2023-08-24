package org.fugerit.java.doc.mod.poi5;

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
import org.fugerit.java.doc.mod.poi.BasicPoiTypeHandler;
import org.fugerit.java.doc.mod.poi.PoiUtils;
import org.fugerit.java.doc.mod.poi.WorkbookHelper;

public class XlsPoi5TypeHandler extends BasicPoiTypeHandler {

	private static final long serialVersionUID = 7765993253384886581L;
	
	public static final DocTypeHandler HANDLER = new XlsPoi5TypeHandler();

	public XlsPoi5TypeHandler() {
		super( DocConfig.TYPE_XLS );
	}

	@Override
	protected WorkbookHelper newWorkbook(DocInput docInput, InputStream is ) throws IOException {
		return PoiUtils.newHelper(false, is);
	}

	@Override
	protected void closeWorkbook(Workbook workbook, DocOutput docOutput) throws IOException {
		PoiUtils.closeWorkbook(workbook, docOutput);
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