package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocHeaderFooter;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocHeaderFooter {

	@Test
	public void test1() {
		DocHeaderFooter model = new DocHeaderFooter();
		model.setNumbered( true );
		log.info( "model {} - {} - {}", model.getExpectedSize(), model.getAlign(), model.getBorderWidth() );
		Assert.assertTrue( model.isNumbered() );
		Assert.assertTrue( model.isBasic() );
	}
	
}
