package org.fugerit.java.doc.lib.simpletableimport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.util.ObjectUtils;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.lib.simpletable.SimpleTableDocConfig;
import org.fugerit.java.doc.lib.simpletable.SimpleTableFacade;
import org.fugerit.java.doc.lib.simpletable.SimpleTableHelper;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConvertXlsxToSimpleTableFacade {

	private static final ConvertXlsxToSimpleTableFacade INSTANCE = new ConvertXlsxToSimpleTableFacade();
	
	private static final Properties DEFAULT_PARAMS = new Properties();
	
	public static final String PARAM_KEY_RETHROW_EXCEPTION = "rethrow-exception";
	public static final String PARAM_VALUE_RETHROW_EXCEPTION_DEFAULT = BooleanUtils.BOOLEAN_1;
	
	
	public static ConvertXlsxToSimpleTableFacade getInstance() {
		return INSTANCE;
	}
	
	public SimpleTable convertXlsx( InputStream xlsxStream, Properties params ) throws ConfigException {
		SimpleTable table = null;
		params = ObjectUtils.objectWithDefault( params, DEFAULT_PARAMS );
		try ( Workbook workbook = new XSSFWorkbook( xlsxStream ) ) {
			Sheet sheet = workbook.getSheetAt( 0 );
			Iterator<Row> itRows = sheet.iterator();
			SimpleTableHelper helper = SimpleTableFacade.newHelper();
			short columnCount = -1;
			while ( itRows.hasNext() ) {
				Row row = itRows.next();
				boolean isHeaderRow = true;
				if ( columnCount != -1 && columnCount != row.getLastCellNum() ) {
					throw new ConfigException( "Wrong column count : "+row.getLastCellNum() );
				} else {
					columnCount = row.getLastCellNum();
				}
				if ( table == null ) {
					// creates a fixed columns table
					table = helper.newTable( helper.newFixedColumns( columnCount ) );
				}
				Iterator<Cell> itCells = row.iterator();
				String line[] = new String[ columnCount ];
				int colIndex = 0;
				while ( itCells.hasNext() ) {
					Cell cell = itCells.next();
					XSSFCell xssfCell = (XSSFCell) cell;
					isHeaderRow = isHeaderRow && xssfCell.getCellStyle().getFont().getBold();
					line[colIndex] = cell.getStringCellValue();
					colIndex++;
				}
				if ( isHeaderRow ) {
					table.addRow( helper.newHeaderRow( line ) );
				} else {
					table.addRow( helper.newNormalRow( line ) );
				}
			}
		} catch (IOException e) {
			if ( BooleanUtils.isTrue( params.getProperty( PARAM_KEY_RETHROW_EXCEPTION, PARAM_VALUE_RETHROW_EXCEPTION_DEFAULT ) ) ) {
				throw new ConfigException( e );	
			} else {
				table = null;
				log.warn( "Returning null table on exception : "+e, e );
			}
		}
		return table;
	}
	
	public void processXlsx( InputStream xlsxStream, OutputStream os, DocTypeHandler handler, Properties params ) throws ConfigException, DocException {
		SimpleTable table = this.convertXlsx(xlsxStream, params);
		SimpleTableDocConfig docConfig = SimpleTableDocConfig.newConfig();
		docConfig.processSimpleTable(table, handler, os);
	}
	
}
