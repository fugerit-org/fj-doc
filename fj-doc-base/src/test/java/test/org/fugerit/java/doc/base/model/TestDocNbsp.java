package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocNbsp;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocNbsp extends HelperDocT {

	private String worker( DocNbsp element ) {
		this.baseTest( element );
		log.info( "test 1 -> {}", element.getLength() );
		return element.getId();
	}
	
	@Test
	public void testElement() {
		DocNbsp element = new DocNbsp();
		element.setId( TEST_ID );
		element.setLength( 10 );
		Assert.assertNotNull( this.worker( element ) );
	}
	
}
