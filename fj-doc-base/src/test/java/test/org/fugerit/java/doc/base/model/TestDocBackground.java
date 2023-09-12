package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocBackground;
import org.junit.Assert;
import org.junit.Test;

public class TestDocBackground extends HelperDocT {

	private String worker( DocBackground element ) {
		this.baseTest(element);
		return element.getId();
	}
	
	@Test
	public void testElement() {
		DocBackground element = new DocBackground();
		element.setId( TEST_ID );
		Assert.assertNotNull( this.worker( element ) );
	}
	
}
