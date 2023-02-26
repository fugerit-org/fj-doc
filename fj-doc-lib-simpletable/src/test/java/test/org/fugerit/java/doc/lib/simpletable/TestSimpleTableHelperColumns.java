package test.org.fugerit.java.doc.lib.simpletable;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.fugerit.java.doc.lib.simpletable.SimpleTableFacade;
import org.fugerit.java.doc.lib.simpletable.SimpleTableHelper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSimpleTableHelperColumns {

	private static final Logger logger = LoggerFactory.getLogger( TestSimpleTableHelperColumns.class );
	
	private static final SimpleTableHelper HELPER = SimpleTableFacade.newHelper();
	
	private void test( int columnNumber ) {
		int size = 0;
		List<Integer> colWidths = HELPER.newFixedColumns( columnNumber );
		logger.info( "Column numbers {}, Column widths : {}", columnNumber, colWidths );
		for ( int current : colWidths ) {
			size+= current;
		}
		logger.info( "Total size is {}", size );
		assertEquals( "Wrong columns total size", 100 , size );
	}

	@Test
	public void columns_12() {
		this.test( 12 );
	}
	
	@Test
	public void columns_10() {
		this.test( 10 );
	}
	
	@Test
	public void columns_8() {
		this.test( 8 );
	}
	
}
