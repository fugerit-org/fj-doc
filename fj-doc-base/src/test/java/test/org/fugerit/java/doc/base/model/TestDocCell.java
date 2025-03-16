package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocCell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestDocCell {

	@Test
	void test1() {
		DocCell model = new DocCell();
		model.setRSpan(0);
		model.setCSpan(0);
		log.info( "model -> {}", model );
		Assertions.assertEquals( 1 , model.getRowSpan() );
		Assertions.assertEquals( 1 , model.getColumnSpan() );
		Assertions.assertFalse( model.isHeader() );
	}
	
}
