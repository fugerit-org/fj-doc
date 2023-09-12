package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocText;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocText extends HelperDocT {

	
	private String worker( DocText element ) {
		this.baseTest(element);
		log.info( "test 1 -> {}", element.getHAlign() );
		log.info( "test 2 -> {}", element.getVAlign() );
		return element.getId();
	}
	
	@Test
	public void testElement() {
		DocText element = new DocText();
		element.setId( TEST_ID );
		element.setHAlign( 1 );
		element.setVAlign( 2 );
		Assert.assertNotNull( this.worker( element ) );
	}
	
}
