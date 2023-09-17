package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocImage;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocImage extends HelperDocT {
	
	@Test
	public void testElement1() {
		DocImage image = new DocImage();
		image.setUrl( DocImage.TYPE_JPG );
		Assert.assertEquals( DocImage.TYPE_JPG, image.getResolvedType() );
		image.setType( DocImage.TYPE_PNG );
		Assert.assertEquals( DocImage.TYPE_PNG, image.getResolvedType() );
		log.info( "accepted types : {}", DocImage.getAcceptedImageTypes() );
	}
	
}
