package test.org.fugerit.java.doc.base.model;

import java.util.Properties;

import org.fugerit.java.doc.base.model.DocBorders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestDocBoders extends HelperDocT {

	private static final String DEFAULT_WIDTH_AND_PADDING = "2";
	
	private String worker( DocBorders element ) {
		log.info( "test {}", element );
		return element.toString();
	}
	
	@Test
	void testElement() {
		DocBorders element = new DocBorders();
		Assertions.assertNotNull( this.worker( element ) );
	}
	
	@Test
	void testFrom() {
		Properties atts = new Properties();
		atts.setProperty( DocBorders.ATTRIBUTE_NAME_BORDER_WIDTH , DEFAULT_WIDTH_AND_PADDING );
		DocBorders element = DocBorders.createBorders(atts, DEFAULT_WIDTH_AND_PADDING );
		DocBorders copyFull = new DocBorders( element );
		DocBorders copyVoid = new DocBorders( null );
		Assertions.assertEquals( DEFAULT_WIDTH_AND_PADDING , String.valueOf( copyFull.getBorderWidthBottom() ) );
		Assertions.assertNotEquals( DEFAULT_WIDTH_AND_PADDING , String.valueOf( copyVoid.getBorderWidthBottom() ) );
	}
	
}
