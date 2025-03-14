package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocBarcode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestBarcode {

	@Test
	void test1() {
		DocBarcode model = new DocBarcode();
		model.setId( "a" );
		model.setText( "text" );
		model.setType( "EAN16" );
		model.setSize( 10 );
		log.info( "model -> {}, {}, {}, {}", model.getId(), model.getText(), model.getSize(), model.getType() );
		Assertions.assertNotNull( model );
	}
	
}
