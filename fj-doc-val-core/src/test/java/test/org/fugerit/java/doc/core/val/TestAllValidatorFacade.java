package test.org.fugerit.java.doc.core.val;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.JavaVersionHelper;
import org.fugerit.java.core.util.result.Result;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;
import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestAllValidatorFacade extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			ImageValidator.JPG_VALIDATOR,
			ImageValidator.PNG_VALIDATOR,
			ImageValidator.TIFF_VALIDATOR
	);
	
	@Test
	void testPngAsPdf() {
		boolean ok = this.worker(FACADE, "png_as_pdf.pdf", false );
		Assertions.assertTrue( ok );
	}
	
	@Test
	void testJpgAsJpg() {
		boolean ok = this.worker(FACADE, "jpg_as_jpg.jpg", true );
		Assertions.assertTrue( ok );
	}
	
	@Test
	void testJpgAsJpgExtJpeg() {
		boolean ok = this.worker(FACADE, "jpg_as_jpg.jpeg", true );
		Assertions.assertTrue( ok );
	}
	
	@Test
	void testPdfAsJpg() {
		boolean ok = this.worker(FACADE, "pdf_as_jpg.jpg", false );
		Assertions.assertTrue( ok );
	}

	@Test
	void testPngAsPng() {
		boolean ok = this.worker(FACADE, "png_as_png.png", true );
		Assertions.assertTrue( ok );
	}

	// note : only supported for java 9+
	@Test
	void testTiffAsTiff() {
		boolean ok = this.worker(FACADE, "tiff_as_tiff.tiff", true );
		Assertions.assertTrue( ok );
	}
	@Test
	void testTiffAsTiffExtTif() {
		boolean ok = this.worker(FACADE, "tiff_as_tiff.tif", true );
		Assertions.assertTrue( ok );
	}
	
	@Test
	void testDocValidatorFacadeDouble() {
		DocTypeValidator notCompValidator = new HelperDocValidator() {
			@Override
			public boolean checkCompatibility() {
				return false;
			}
		};
		DocValidatorFacade facade = DocValidatorFacade.newFacade( ImageValidator.JPG_VALIDATOR, 
				ImageValidator.JPG_VALIDATOR,
				notCompValidator );
		log.info( "facade {}", facade );
		Assertions.assertTrue( facade.findAllByMimeType( HelperDocValidator.MIME_TEST ).isEmpty() );
		Assertions.assertTrue( facade.findAllExtension( HelperDocValidator.FORMAT_TEST ).isEmpty() );
		Assertions.assertNull( facade.findByMimeType( HelperDocValidator.MIME_TEST ) );
		Assertions.assertNull( facade.findByExtension( HelperDocValidator.FORMAT_TEST ) );
		Assertions.assertFalse( facade.getSupportedMimeTypes().isEmpty() );
		Assertions.assertFalse( facade.getSupportedExtensions().isEmpty() );
		Assertions.assertFalse( facade.isMimeTypeSupprted( HelperDocValidator.MIME_TEST ) );
		Assertions.assertFalse( facade.isExtensionSupported( HelperDocValidator.FORMAT_TEST ) );
		Assertions.assertFalse( facade.isMimeTypeSupprted( HelperDocValidator.MIME_TEST ) );
		Assertions.assertFalse( facade.isExtensionSupported( HelperDocValidator.FORMAT_TEST ) );
		Assertions.assertFalse( facade.isMimeTypeSupprted( "aaaa" ) );
		Assertions.assertFalse( facade.isExtensionSupported( "bbbb/test" ) );
		Assertions.assertTrue( facade.isMimeTypeSupprted( ImageValidator.JPG_VALIDATOR.getMimeType() ) );
		Assertions.assertTrue( facade.isExtensionSupported( ImageValidator.FORMAT_JPG ) );
		// test path
		String testPath = "src/test/resources/sample/jpg_as_jpg.jpeg";
		SafeFunction.apply( () -> { 
			Assertions.assertTrue( facade.check( new File( testPath ) ) );	
			try ( InputStream is = new FileInputStream( testPath ) ) {
				facade.checkByMimeType( HelperDocValidator.FORMAT_TEST , is);
			}
			try ( InputStream is = new FileInputStream( testPath ) ) {
				facade.checkByExtension( HelperDocValidator.MIME_TEST , is );
			}
		} );
	}
	
	@Test
	void finalTests() {
		DocTypeValidator notCompValidator = new HelperDocValidator() {
			@Override
			public boolean checkCompatibility() {
				return false;
			}
		};
		String testPath = "src/test/resources/sample/jpg_as_jpg.jpeg";
		// facade strict
		Assertions.assertThrows( ConfigRuntimeException.class, () -> DocValidatorFacade.newFacadeStrict( notCompValidator ) );
		// result
		Assertions.assertEquals( Result.RESULT_CODE_OK , DocTypeValidationResult.newOk().getResultCode() );
		Assertions.assertFalse( ImageValidator.javaVersionSupportHelper( JavaVersionHelper.MAJOR_VERSION_JAVA_8 , JavaVersionHelper.MAJOR_VERSION_JAVA_11 ) );
		// test
		Assertions.assertNotNull( new HelperDocValidator( HelperDocValidator.MIME_TEST, HelperDocValidator.FORMAT_TEST ) );
		SafeFunction.apply( () -> { 
			try ( InputStream is = new FileInputStream( testPath ) ) {
				Assertions.assertTrue( notCompValidator.check( is ) );
				Assertions.assertTrue( new HelperDocValidator().checkCompatibility() );
			}
		} );
	}
	
}
