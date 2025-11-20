package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocImage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
class TestDocImage extends HelperDocT {
	
	@Test
	void testElement1() throws IOException {
		DocImage image = new DocImage();
		image.setUrl( DocImage.TYPE_JPG );
		Assertions.assertEquals( DocImage.TYPE_JPG, image.getResolvedType() );
		image.setType( DocImage.TYPE_PNG );
		Assertions.assertEquals( DocImage.TYPE_PNG, image.getResolvedType() );
		Assertions.assertFalse( image.isSvg() );
		image.setType( DocImage.TYPE_SVG );
		Assertions.assertTrue( image.isSvg() );
		image.setUrl( "cl://txt/test.txt" );
		Assertions.assertEquals( "test text", image.getResolvedText() );
		log.info( "accepted types : {}", DocImage.getAcceptedImageTypes() );
        String altTest = "alt test";
        image.setContent( altTest );
        Assertions.assertEquals( altTest, image.getResolvedText() );
        image.setUrl( null );
        image.setBase64( null );
        Assertions.assertEquals( altTest.length(), DocImage.resolveImage( image ).length );
	}
	
}
