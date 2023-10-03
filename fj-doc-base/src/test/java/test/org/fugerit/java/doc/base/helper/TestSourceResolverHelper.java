package test.org.fugerit.java.doc.base.helper;

import java.io.IOException;
import java.util.Base64;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.helper.SourceResolverHelper;
import org.fugerit.java.doc.base.model.DocImage;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestSourceResolverHelper {

	@Test
	public void test1() throws IOException {
		DocImage docImage = new DocImage();
		Assert.assertThrows( IOException.class , () -> SourceResolverHelper.resolveImageToBase64( docImage ) );
		Assert.assertThrows( IOException.class , () -> SourceResolverHelper.resolveImage( docImage ) );
		String pathImg = "test/img_test_blue.png";
		String base64 = Base64.getEncoder().encodeToString( StreamIO.readBytes( ClassHelper.loadFromDefaultClassLoader( pathImg ) ) );
		docImage.setBase64(base64);
		Assert.assertNotNull( SourceResolverHelper.resolveImage(docImage) );
		log.info( "docImage : {}", docImage );
	}
	
}
