package org.fugerit.java.doc.lib.simpletableimport;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.lib.simpletable.SimpleTableDocConfig;
import org.fugerit.java.doc.lib.simpletable.SimpleTableFacade;
import org.fugerit.java.doc.lib.simpletable.SimpleTableHelper;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class ConvertCsvToSimpleTableFacade {
	
	public SimpleTable convertCsv( InputStream csvStream, Properties params ) throws ConfigException {
		SimpleTable table = null;
		try ( CSVReader reader = new CSVReader( new InputStreamReader( csvStream ) ) ) {
			int columnCount = -1;
			SimpleTableHelper helper = SimpleTableFacade.newHelper();
			String[] currentLine = reader.readNext();
			while ( currentLine != null ) {
				if ( columnCount != -1 && columnCount != currentLine.length ) {
					throw new ConfigException( "Wrong column count : "+currentLine.length );
				} else {
					columnCount = currentLine.length;
				}
				if ( table == null ) {
					// creates a fixed columns table
					table = helper.newTable( helper.newFixedColumns( columnCount ) );
				}
				table.addRow( helper.newNormalRow( currentLine ) );
				currentLine = reader.readNext();
			}
		} catch (IOException | CsvValidationException e) {
			table = CommonConvertUtils.handleConvertException(table, e, params);
		}
		return table;
	}
	
	public void processCsv( InputStream xlsxStream, OutputStream os, DocTypeHandler handler, Properties params ) throws ConfigException, DocException {
		SimpleTable table = this.convertCsv(xlsxStream, params);
		SimpleTableDocConfig docConfig = SimpleTableDocConfig.newConfig();
		docConfig.processSimpleTable(table, handler, os);
	}
	
}
