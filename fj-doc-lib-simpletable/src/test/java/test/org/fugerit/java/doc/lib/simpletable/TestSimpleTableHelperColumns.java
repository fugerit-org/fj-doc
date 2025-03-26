package test.org.fugerit.java.doc.lib.simpletable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.fugerit.java.doc.lib.simpletable.SimpleTableFacade;
import org.fugerit.java.doc.lib.simpletable.SimpleTableHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestSimpleTableHelperColumns {

	private static final Logger logger = LoggerFactory.getLogger( TestSimpleTableHelperColumns.class );
	
	private static final SimpleTableHelper HELPER = SimpleTableFacade.newHelper();
	
	private boolean test( int columnNumber ) {
		int size = 0;
		List<Integer> colWidths = HELPER.newFixedColumns( columnNumber );
		logger.info( "Column numbers {}, Column widths : {}", columnNumber, colWidths );
		for ( int current : colWidths ) {
			size+= current;
		}
		logger.info( "Total size is {}", size );
		assertEquals( 100 , size );
		return 100 == size;
	}

	@Test
	void columns_12() {
		Assertions.assertTrue( this.test( 12 ) );
	}
	
	@Test
	void columns_10() {
		Assertions.assertTrue( this.test( 10 ) );
	}
	
	@Test
	void columns_8() {
		Assertions.assertTrue( this.test( 8 ) );
	}
	
}
