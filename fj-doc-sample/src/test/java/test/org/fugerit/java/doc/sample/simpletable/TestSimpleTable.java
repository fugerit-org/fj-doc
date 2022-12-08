package test.org.fugerit.java.doc.sample.simpletable;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.lib.simpletable.SimpleTableDocConfig;
import org.fugerit.java.doc.lib.simpletable.SimpleTableFacade;
import org.fugerit.java.doc.lib.simpletable.model.SimpleRow;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.fugerit.java.doc.mod.opencsv.OpenCSVTypeHandler;
import org.fugerit.java.doc.mod.poi.XlsxPoiTypeHandler;
import org.junit.Before;
import org.junit.Test;

import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

public class TestSimpleTable  {

	private static final DocTypeHandler[] HANDLERS = { XlsxPoiTypeHandler.HANDLER, OpenCSVTypeHandler.HANDLER, new PdfFopTypeHandler() };
	
	private SimpleTableDocConfig docConfig;
	
	private File baseDir = new File( BasicFacadeTest.BASIC_OUTPUT_PATH );
	
	@Before
	public void init() throws ConfigException {
		this.docConfig = SimpleTableDocConfig.newConfig();
		if ( !baseDir.exists() ) {
			this.baseDir.mkdirs();
		}
	}
	
	@Test
	public void testSimpleTable01() {
		SimpleTable simpleTableModel = SimpleTableFacade.newTable( 30, 30, 40 );
		SimpleRow headerRow = new SimpleRow( BooleanUtils.BOOLEAN_TRUE );
		headerRow.addCell( "Name" );
		headerRow.addCell( "Surname" );
		headerRow.addCell( "Title" );
		simpleTableModel.addRow( headerRow );
		SimpleRow luthienRow = new SimpleRow();
		luthienRow.addCell( "Luthien" );
		luthienRow.addCell( "Tinuviel" );
		luthienRow.addCell( "Queen" );
		simpleTableModel.addRow( luthienRow );
		SimpleRow thorinRow = new SimpleRow();
		thorinRow.addCell( "Thorin" );
		thorinRow.addCell( "Oakshield" );
		thorinRow.addCell( "King" );
		simpleTableModel.addRow( thorinRow );
		for ( int k=0; k<HANDLERS.length; k++ ) {
			DocTypeHandler handler = HANDLERS[k];
			File file = new File( this.baseDir, "simple_table_01."+handler.getType() );
			try ( FileOutputStream fos = new FileOutputStream( file ) )  {
				this.docConfig.processSimpleTable(simpleTableModel, handler, fos);
			} catch (Exception e) {
				fail( "Error : "+e );
			}
		}
	}

}
