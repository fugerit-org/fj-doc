package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocPhrase;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocPhrase {

	@Test
	public void testModel() {
		DocPhrase model = new DocPhrase();
		model.setForeColor( "#00000" );
		model.setBackColor( "#ffffff" );
		model.setFontName( "Arial" );
		model.setWhiteSpaceCollapse( "true" );
		Assert.assertFalse( model.isInternal() );
		model.setLink( "www.test.it" );
		Assert.assertFalse( model.isInternal() );
		model.setLink( "#test" );
		log.info( "fore-color : {} , back-color : {}", model.getForeColor(), model.getBackColor() );
		log.info( "font-name : {} , white-space-ignore : {}", model.getFontName(), model.getWhiteSpaceCollapse() );
		Assert.assertTrue( model.isInternal() );
		Assert.assertEquals( "test" , model.getInternalLink() );
		model.setLeading( 1.0F );
		log.info( "leading : {}, originaStyle", model.getLeading(), model.getOriginalStyle() );
	}
	
}
