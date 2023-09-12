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
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestAllValidatorFacade extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			ImageValidator.JPG_VALIDATOR,
			ImageValidator.PNG_VALIDATOR,
			ImageValidator.TIFF_VALIDATOR
	);
	
	@Test
	public void testPngAsPdf() {
		boolean ok = this.worker(FACADE, "png_as_pdf.pdf", false );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testJpgAsJpg() {
		boolean ok = this.worker(FACADE, "jpg_as_jpg.jpg", true );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testJpgAsJpgExtJpeg() {
		boolean ok = this.worker(FACADE, "jpg_as_jpg.jpeg", true );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testPdfAsJpg() {
		boolean ok = this.worker(FACADE, "pdf_as_jpg.jpg", false );
		Assert.assertTrue( ok );
	}

	@Test
	public void testPngAsPng() {
		boolean ok = this.worker(FACADE, "png_as_png.png", true );
		Assert.assertTrue( ok );
	}

	// note : only supported for java 9+
	@Test
	public void testTiffAsTiff() {
		boolean ok = this.worker(FACADE, "tiff_as_tiff.tiff", true );
		Assert.assertTrue( ok );
	}
	@Test
	public void testTiffAsTiffExtTif() {
		boolean ok = this.worker(FACADE, "tiff_as_tiff.tif", true );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testDocValidatorFacadeDouble() {
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
		Assert.assertTrue( facade.findAllByMimeType( HelperDocValidator.MIME_TEST ).isEmpty() );
		Assert.assertTrue( facade.findAllExtension( HelperDocValidator.FORMAT_TEST ).isEmpty() );
		Assert.assertNull( facade.findByMimeType( HelperDocValidator.MIME_TEST ) );
		Assert.assertNull( facade.findByExtension( HelperDocValidator.FORMAT_TEST ) );
		Assert.assertFalse( facade.getSupportedMimeTypes().isEmpty() );
		Assert.assertFalse( facade.getSupportedExtensions().isEmpty() );
		Assert.assertFalse( facade.isMimeTypeSupprted( HelperDocValidator.MIME_TEST ) );
		Assert.assertFalse( facade.isExtensionSupported( HelperDocValidator.FORMAT_TEST ) );
		Assert.assertFalse( facade.isMimeTypeSupprted( HelperDocValidator.MIME_TEST ) );
		Assert.assertFalse( facade.isExtensionSupported( HelperDocValidator.FORMAT_TEST ) );
		Assert.assertFalse( facade.isMimeTypeSupprted( "aaaa" ) );
		Assert.assertFalse( facade.isExtensionSupported( "bbbb/test" ) );
		Assert.assertTrue( facade.isMimeTypeSupprted( ImageValidator.JPG_VALIDATOR.getMimeType() ) );
		Assert.assertTrue( facade.isExtensionSupported( ImageValidator.FORMAT_JPG ) );
		// test path
		String testPath = "src/test/resources/sample/jpg_as_jpg.jpeg";
		SafeFunction.apply( () -> { 
			Assert.assertTrue( facade.check( new File( testPath ) ) );	
			try ( InputStream is = new FileInputStream( testPath ) ) {
				facade.checkByMimeType( HelperDocValidator.FORMAT_TEST , is);
			}
			try ( InputStream is = new FileInputStream( testPath ) ) {
				facade.checkByExtension( HelperDocValidator.MIME_TEST , is );
			}
		} );
	}
	
	@Test
	public void finalTests() {
		DocTypeValidator notCompValidator = new HelperDocValidator() {
			@Override
			public boolean checkCompatibility() {
				return false;
			}
		};
		String testPath = "src/test/resources/sample/jpg_as_jpg.jpeg";
		// facade strict
		Assert.assertThrows( ConfigRuntimeException.class, () -> DocValidatorFacade.newFacadeStrict( notCompValidator ) );
		// result
		Assert.assertEquals( Result.RESULT_CODE_OK , DocTypeValidationResult.newOk().getResultCode() );
		Assert.assertFalse( ImageValidator.javaVersionSupportHelper( JavaVersionHelper.MAJOR_VERSION_JAVA_8 , JavaVersionHelper.MAJOR_VERSION_JAVA_11 ) );
		// test
		Assert.assertNotNull( new HelperDocValidator( HelperDocValidator.MIME_TEST, HelperDocValidator.FORMAT_TEST ) );
		SafeFunction.apply( () -> { 
			try ( InputStream is = new FileInputStream( testPath ) ) {
				Assert.assertTrue( notCompValidator.check( is ) );
				Assert.assertTrue( new HelperDocValidator().checkCompatibility() );
			}
		} );
	}
	
}
