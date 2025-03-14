package test.org.fugerit.java.doc.base.helper;

import java.io.IOException;
import java.util.Base64;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.helper.SourceResolverHelper;
import org.fugerit.java.doc.base.model.DocImage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestSourceResolverHelper {

	@Test
	void test1() throws IOException {
		DocImage docImage = new DocImage();
		Assertions.assertThrows( IOException.class , () -> SourceResolverHelper.resolveImageToBase64( docImage ) );
		Assertions.assertThrows( IOException.class , () -> SourceResolverHelper.resolveImage( docImage ) );
		String pathImg = "test/img_test_blue.png";
		String base64 = Base64.getEncoder().encodeToString( StreamIO.readBytes( ClassHelper.loadFromDefaultClassLoader( pathImg ) ) );
		docImage.setBase64(base64);
		Assertions.assertNotNull( SourceResolverHelper.resolveImage(docImage) );
		docImage.setBase64( null );
		docImage.setUrl( "https://github.com/fugerit-org/fj-doc/blob/main/fj-doc-base/src/test/resources/test/img_test_red.png" );
		Assertions.assertNotNull( SourceResolverHelper.resolveImage(docImage) );
		log.info( "docImage : {}", docImage );
	}
	
}
