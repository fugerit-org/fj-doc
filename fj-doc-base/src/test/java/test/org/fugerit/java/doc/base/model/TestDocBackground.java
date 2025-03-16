package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocBackground;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestDocBackground extends HelperDocT {

	private String worker( DocBackground element ) {
		this.baseTest(element);
		return element.getId();
	}
	
	@Test
	void testElement() {
		DocBackground element = new DocBackground();
		element.setId( TEST_ID );
		Assertions.assertNotNull( this.worker( element ) );
	}
	
}
