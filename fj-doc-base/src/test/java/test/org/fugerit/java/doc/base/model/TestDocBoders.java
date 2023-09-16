package test.org.fugerit.java.doc.base.model;

import java.util.Properties;

import org.fugerit.java.doc.base.model.DocBorders;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocBoders extends HelperDocT {

	private static final String DEFAULT_WIDTH_AND_PADDING = "2";
	
	private String worker( DocBorders element ) {
		log.info( "test {}", element );
		return element.toString();
	}
	
	@Test
	public void testElement() {
		DocBorders element = new DocBorders();
		Assert.assertNotNull( this.worker( element ) );
	}
	
	@Test
	public void testFrom() {
		Properties atts = new Properties();
		atts.setProperty( DocBorders.ATTRIBUTE_NAME_BORDER_WIDTH , DEFAULT_WIDTH_AND_PADDING );
		DocBorders element = DocBorders.createBorders(atts, DEFAULT_WIDTH_AND_PADDING );
		DocBorders copyFull = new DocBorders( element );
		DocBorders copyVoid = new DocBorders( null );
		Assert.assertEquals( DEFAULT_WIDTH_AND_PADDING , String.valueOf( copyFull.getBorderWidthBottom() ) );
		Assert.assertNotEquals( DEFAULT_WIDTH_AND_PADDING , String.valueOf( copyVoid.getBorderWidthBottom() ) );
	}
	
}
