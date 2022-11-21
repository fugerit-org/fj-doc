package test.org.fugerit.java.doc.base.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fugerit.java.doc.base.config.DocVersion;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDocVersion {

	private static final Logger logger = LoggerFactory.getLogger( TestDocVersion.class );
	
	private void testCompareWorker( int expected, DocVersion v1, DocVersion v2 ) {
		int result = DocVersion.compare( v1, v2 );
		logger.info( "Test compare {} to {} , expected:{}, result:{}", v1, v2, expected, result );
		Assert.assertEquals( "Wrong compare result", expected, result );
	}
	
	@Test
	public void testCompareMajorThan01() {
		this.testCompareWorker( 9, DocVersion.VERSION_1_10, DocVersion.VERSION_1_1 );
	}
	
	@Test
	public void testCompareMinorThan01() {
		this.testCompareWorker( -2, DocVersion.VERSION_1_1, DocVersion.VERSION_1_3 );
	}
	
	@Test
	public void testCompareEqual01() {
		this.testCompareWorker( 0, DocVersion.VERSION_1_5, DocVersion.VERSION_1_5 );
	}
	
	@Test
	public void testSort() {
		List<DocVersion> list = new ArrayList<DocVersion>();
		list.add( DocVersion.VERSION_1_8 );
		list.add( DocVersion.VERSION_1_2 );
		list.add( DocVersion.VERSION_1_10 );
		list.add( DocVersion.VERSION_1_9 );
		logger.info( "list pre sort : {}", list );
		Collections.sort( list );
		logger.info( "list post sort : {}", list );
		Assert.assertEquals( "Version list sort", "[1-2, 1-8, 1-9, 1-10]", String.valueOf( list ) );
	}
	
}
