package test.org.fugerit.java.doc.lib.simpletableimport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.fugerit.java.doc.lib.simpletableimport.ConvertXlsxToSimpleTableFacade;
import org.fugerit.java.doc.mod.poi.XlsxPoiTypeHandler;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestImportSimpleTableXlsx {
	
	private boolean testXlsxWorker( String inputPath, String outputPath ) {
		File inputFile = new File( inputPath );
		File outputFile = new File( outputPath );
		log.info( "inputPath -> {}, outputPath -> {}", inputPath, outputPath );
		try ( FileInputStream is = new FileInputStream( inputFile );
				FileOutputStream os = new FileOutputStream( outputFile )) {
			ConvertXlsxToSimpleTableFacade facade = new ConvertXlsxToSimpleTableFacade();
			facade.processXlsx(is, os, XlsxPoiTypeHandler.HANDLER, new Properties());
			return Boolean.TRUE;
		} catch (Exception e) {
			String message = "Error : "+e;
			log.error( message, e );
			return Boolean.FALSE;
		}
	}

	@Test
	public void testXlsx() {
		Assert.assertTrue( this.testXlsxWorker( "src/test/resources/xlsx/simple_table_01.xlsx", "target/simple_table_01_xlsx.xlsx" ) );
	}
	
}
