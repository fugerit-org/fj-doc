package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocImage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestDocImage extends HelperDocT {
	
	@Test
	void testElement1() {
		DocImage image = new DocImage();
		image.setUrl( DocImage.TYPE_JPG );
		Assertions.assertEquals( DocImage.TYPE_JPG, image.getResolvedType() );
		image.setType( DocImage.TYPE_PNG );
		Assertions.assertEquals( DocImage.TYPE_PNG, image.getResolvedType() );
		log.info( "accepted types : {}", DocImage.getAcceptedImageTypes() );
	}
	
}
