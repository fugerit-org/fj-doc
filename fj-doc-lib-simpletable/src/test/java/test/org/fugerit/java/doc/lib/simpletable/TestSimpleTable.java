package test.org.fugerit.java.doc.lib.simpletable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandler;
import org.fugerit.java.doc.lib.simpletable.SimpleTableDocConfig;
import org.fugerit.java.doc.lib.simpletable.SimpleTableFacade;
import org.fugerit.java.doc.lib.simpletable.SimpleTableHelper;
import org.fugerit.java.doc.lib.simpletable.model.SimpleCell;
import org.fugerit.java.doc.lib.simpletable.model.SimpleRow;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;
import org.junit.Assert;
import org.junit.Test;

public class TestSimpleTable {

	private void testTable( SimpleTable table, SimpleTableHelper helper ) {
		table.addRow( helper.newHeaderRow( "H1", "H2" ) );
		table.addRow( helper.newNormalRow( "C1", "C2" ) );
		table.withDocLanguage( "it" ).withSheetName( "a" ).withTableWidth( "1" );
		table.setDocLanguage( "en" );
		table.setSheetName( "b" );
		table.getDefaultBorderWidth();
		table.setDefaultBorderWidth( SimpleCell.BORDER_WIDTH_UNSET );
		SimpleCell cell = new SimpleCell( "a", SimpleCell.BORDER_WIDTH_UNSET );
		SimpleRow row = new SimpleRow();
		row.addCell( cell );
		table.addRow( row );
		SafeFunction.apply( () -> {
			try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
				SimpleTableDocConfig.newConfigLatest().processSimpleTable( table , FreeMarkerHtmlTypeHandler.HANDLER, baos );
				Assert.assertTrue( baos.toByteArray().length > 0 );
			}
		} );
		Assert.assertThrows( DocException.class , () -> {
			try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
				SimpleTableDocConfig.newConfigLatest().processSimpleTable( table , new FailTypeHandler(), baos );
				Assert.assertTrue( baos.toByteArray().length > 0 );
			}
		} );
	}
	
	@Test
	public void testHelper() {
		Integer defaultBorder = 0;
		SimpleTableHelper helper = SimpleTableFacade.newHelper().withDefaultBorderWidth( defaultBorder );
		Assert.assertEquals( defaultBorder , helper.getDefaultBorderWidth() );
		List<Integer> lcw = new ArrayList<Integer>();
		Assert.assertThrows( ConfigRuntimeException.class, () -> helper.newTable( lcw ) );
		Assert.assertThrows( ConfigRuntimeException.class, () -> helper.newTable() );
		List<Integer> colWidths = null;
		Assert.assertThrows( ConfigRuntimeException.class, () -> helper.newTable( colWidths ) );
		Integer[] colWidthsA = null;
		Assert.assertThrows( ConfigRuntimeException.class, () -> helper.newTable( colWidthsA ) );
		Integer[] colWidths50 = { 20, 30 };
		Assert.assertThrows( ConfigRuntimeException.class, () -> helper.newTable( colWidths50 ) );
		// new table ok
		Assert.assertNotNull( helper.newTableSingleColumn() );
		SimpleTable table = helper.newTable( 50, 50 );
		this.testTable(table, helper);
	}
	
	@Test
	public void testFacade() {
		Assert.assertNotNull( SimpleTableFacade.newTable( 100 ) );
		Assert.assertNotNull( SimpleTableFacade.newTableSingleColumn() );
		List<Integer> colW = new ArrayList<>();
		colW.add( 100 );
		Assert.assertNotNull( SimpleTableFacade.newTable( colW ) );
	}
	
	@Test
	public void testCell() {
		SimpleCell cell = new SimpleCell( "test", 10 ).bold().bolditalic().italic().left().rigt().underline();
		cell.setContent( "aaaa" );
		Assert.assertNotNull( SimpleCell.newCell( "bbb" ) );
		SimpleRow row = new SimpleRow();
		row.addCell( "cccc" );
		row.setHead( BooleanUtils.BOOLEAN_TRUE );
	}
	
}

class FailTypeHandler extends DocTypeHandlerDefault {

	private static final long serialVersionUID = -938363784671460227L;

	public FailTypeHandler() {
		super( DocConfig.TYPE_PDF, "fail-pdf");
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		if ( DocConfig.TYPE_PDF.equalsIgnoreCase( docInput.getType() ) ) {
			throw new DocException( "Scenario exception" );
		}
	}

}