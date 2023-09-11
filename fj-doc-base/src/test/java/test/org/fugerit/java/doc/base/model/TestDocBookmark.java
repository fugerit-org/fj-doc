package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocBookmark;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocBookmark extends HelperDocT {

	private String worker( DocBookmark element ) {
		this.baseTest( element );
		log.info( "test 1 -> {}", element.getRef() );
		log.info( "test 1 -> {}", element.getTitle() );
		return element.getId();
	}
	
	@Test
	public void testElement() {
		DocBookmark element = new DocBookmark();
		element.setId( TEST_ID );
		element.setRef( "test" );
		element.setTitle( "title" );
		Assert.assertNotNull( this.worker( element ) );
	}
	
}
