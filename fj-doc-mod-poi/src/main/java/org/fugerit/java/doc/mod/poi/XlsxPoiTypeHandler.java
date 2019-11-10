package org.fugerit.java.doc.mod.poi;

import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;

public class XlsxPoiTypeHandler extends BasicPoiTypeHandler {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4886914985225735861L;

	public static DocTypeHandler HANDLER = new XlsxPoiTypeHandler();
	
	public XlsxPoiTypeHandler() {
		super( DocConfig.TYPE_XLSX );
	}

	@Override
	protected Workbook newWorkbook( DocInput docInput , InputStream is ) throws Exception {
		Workbook workbook = null;
		if ( is == null ) {
			workbook = new XSSFWorkbook();
		} else {
			workbook = new XSSFWorkbook( is );
		}
		return workbook;
	}

	@Override
	protected void closeWorkbook(Workbook workbook, DocOutput docOutput) throws Exception {
		workbook.write( docOutput.getOs() );
		workbook.close();
	}

}