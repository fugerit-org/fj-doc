package test.org.fugerit.java.doc.sample.simpletable;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileOutputStream;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerXML;
import org.fugerit.java.doc.lib.simpletable.SimpleTableDocConfig;
import org.fugerit.java.doc.lib.simpletable.SimpleTableFacade;
import org.fugerit.java.doc.lib.simpletable.model.SimpleCell;
import org.fugerit.java.doc.lib.simpletable.model.SimpleRow;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.fugerit.java.doc.mod.opencsv.OpenCSVTypeHandler;
import org.fugerit.java.doc.mod.poi.XlsxPoiTypeHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

@Slf4j
class TestSimpleTable  {

	private static final DocTypeHandler[] HANDLERS = { DocTypeHandlerXML.HANDLER_UTF8, XlsxPoiTypeHandler.HANDLER, OpenCSVTypeHandler.HANDLER, new PdfFopTypeHandler() };
	
	private SimpleTableDocConfig docConfig;
	
	private File baseDir = new File( BasicFacadeTest.BASIC_OUTPUT_PATH );
	
	@BeforeEach
	void init() throws ConfigException {
		this.docConfig = SimpleTableDocConfig.newConfig();
		log.info( "config init ok {}", this.docConfig );
		if ( !baseDir.exists() ) {
			this.baseDir.mkdirs();
		}
	}
	
	@Test
	void testSimpleTable01() {
		SimpleTable simpleTableModel = SimpleTableFacade.newTable( 30, 30, 40 );
		simpleTableModel.setDefaultBorderWidth( 1 );
		SimpleRow headerRow = new SimpleRow( BooleanUtils.BOOLEAN_TRUE );
		headerRow.addCell( "Name" );
		headerRow.addCell( "Surname" );
		headerRow.addCell( "Title" );
		simpleTableModel.addRow( headerRow );
		SimpleRow luthienRow = new SimpleRow();
		luthienRow.addCell( "Luthien" );
		luthienRow.addCell( "Tinuviel" );
		luthienRow.addCell( SimpleCell.newCell( "Queen" ).bold().center() );
		simpleTableModel.addRow( luthienRow );
		SimpleRow thorinRow = new SimpleRow();
		thorinRow.addCell( "Thorin" );
		thorinRow.addCell( "Oakshield" );
		thorinRow.addCell( SimpleCell.newCell( "King" ).bold().center() );
		SimpleRow lastRow = new SimpleRow();
		lastRow.addCell( "Bilbo" );
		lastRow.addCell( "Baggins" );
		lastRow.addCell( SimpleCell.newCell( "<Hero>" ).bold().center() );
		simpleTableModel.addRow( lastRow );
		for ( int k=0; k<HANDLERS.length; k++ ) {
			DocTypeHandler handler = HANDLERS[k];
			File file = new File( this.baseDir, "simple_table_01."+handler.getType() );
			try ( FileOutputStream fos = new FileOutputStream( file ) )  {
				this.docConfig.processSimpleTable(simpleTableModel, handler, fos);
			} catch (Exception e) {
				String message = "Error : "+e;
				log.error( message, e );
				fail( message );
			}
		}
	}

}
