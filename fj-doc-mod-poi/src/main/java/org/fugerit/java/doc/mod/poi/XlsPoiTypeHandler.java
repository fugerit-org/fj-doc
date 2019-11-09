package org.fugerit.java.doc.mod.poi;

import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;

public class XlsPoiTypeHandler extends BasicPoiTypeHandler {

	public static DocTypeHandler HANDLER = new XlsPoiTypeHandler();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7394516459275771708L;

	public XlsPoiTypeHandler() {
		super( DocConfig.TYPE_XLS );
	}

	@Override
	protected Workbook newWorkbook(DocInput docInput, InputStream is ) throws Exception {
		Workbook workbook = null;
		if ( is == null ) {
			workbook = new HSSFWorkbook();
		} else {
			workbook = new HSSFWorkbook( is );
		}
		return workbook;
	}

	@Override
	protected void closeWorkbook(Workbook workbook, DocOutput docOutput) throws Exception {
		workbook.write( docOutput.getOs() );
		workbook.close();
	}

}