package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocHeaderFooter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestDocHeaderFooter {

	@Test
	void test1() {
		DocHeaderFooter model = new DocHeaderFooter();
		model.setNumbered( true );
		log.info( "model {} - {} - {}", model.getExpectedSize(), model.getAlign(), model.getBorderWidth() );
		Assertions.assertTrue( model.isNumbered() );
		Assertions.assertTrue( model.isBasic() );
	}
	
}
