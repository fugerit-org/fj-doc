package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocBorders;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocBoders extends HelperDocT {

	private String worker( DocBorders element ) {
		log.info( "test {}", element );
		return element.toString();
	}
	
	@Test
	public void testElement() {
		DocBorders element = new DocBorders();
		Assert.assertNotNull( this.worker( element ) );
	}
	
}
