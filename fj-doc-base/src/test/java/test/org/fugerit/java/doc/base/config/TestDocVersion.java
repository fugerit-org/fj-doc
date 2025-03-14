package test.org.fugerit.java.doc.base.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fugerit.java.doc.base.config.DocVersion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestDocVersion {

	private static final Logger logger = LoggerFactory.getLogger( TestDocVersion.class );
	
	private boolean testCompareWorker( int expected, DocVersion v1, DocVersion v2 ) {
		int result = DocVersion.compare( v1, v2 );
		logger.info( "Test compare {} to {} , expected:{}, result:{}", v1, v2, expected, result );
		Assertions.assertEquals( expected, result );
		return result == expected;
	}
	
	@Test
	void testCompareMajorThan01() {
		Assertions.assertTrue( this.testCompareWorker( 9, DocVersion.VERSION_1_10, DocVersion.VERSION_1_1 ) );
	}
	
	@Test
	void testCompareMinorThan01() {
		Assertions.assertTrue( this.testCompareWorker( -2, DocVersion.VERSION_1_1, DocVersion.VERSION_1_3 ) );
	}
	
	@Test
	void testCompareEqual01() {
		Assertions.assertTrue( this.testCompareWorker( 0, DocVersion.VERSION_1_5, DocVersion.VERSION_1_5 ) );
	}
	
	@Test
	void testHashCode() {
		Assertions.assertNotEquals( DocVersion.VERSION_1_5.hashCode() , DocVersion.VERSION_2_1.hashCode() );
	}
	
	@Test
	void testEquals() {
		boolean testNotEquals = DocVersion.VERSION_1_5.equals( DocVersion.VERSION_2_1 ); 
		Assertions.assertFalse( testNotEquals );
		boolean testNotEqualsAlt = DocVersion.VERSION_1_5 == null;
		Assertions.assertFalse(  testNotEqualsAlt  );
		boolean testEquals = DocVersion.VERSION_2_1.equals( DocVersion.VERSION_2_1 );
		Assertions.assertTrue( testEquals );
	}
	
	@Test
	void testCompare() {
		int testNotEquals = DocVersion.compare( DocVersion.VERSION_1_0.stringVersion() , DocVersion.VERSION_2_1.stringVersion() );
		Assertions.assertNotEquals( 0 , testNotEquals);
		int testNotEqualsAlt = DocVersion.compare( DocVersion.VERSION_2_0.stringVersion() , DocVersion.VERSION_2_1.stringVersion() );
		Assertions.assertNotEquals( 0 , testNotEqualsAlt);
		int testEquals = DocVersion.compare( DocVersion.VERSION_2_1.stringVersion() , DocVersion.VERSION_2_1.stringVersion() );
		Assertions.assertEquals( 0 , testEquals);
	}
	
	@Test
	void testSort() {
		List<DocVersion> list = new ArrayList<DocVersion>();
		list.add( DocVersion.VERSION_1_8 );
		list.add( DocVersion.VERSION_1_2 );
		list.add( DocVersion.VERSION_1_10 );
		list.add( DocVersion.VERSION_1_9 );
		logger.info( "list pre sort : {}", list );
		Collections.sort( list );
		logger.info( "list post sort : {}", list );
		Assertions.assertEquals( "[1-2, 1-8, 1-9, 1-10]", String.valueOf( list ) );
	}
	
}
