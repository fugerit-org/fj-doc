package test.org.fugerit.java.doc.val.p7m;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.fugerit.java.doc.val.p7m.P7MContentValidator;
import org.fugerit.java.doc.val.pdf.box.PdfboxStrictValidator;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

@Slf4j
public class TestP7MContentValidator extends TestDocValidatorFacade {

	private static final String FILENAME_PDF_AS_P7M = "pdf_as_pdf.p7m";

	private static final DocValidatorFacade FACADE_PDF = DocValidatorFacade.newFacadeStrict( 
			P7MContentValidator.newValidator( DocValidatorFacade.newFacade( PdfboxStrictValidator.DEFAULT ) )
	);
	
	private static final DocValidatorFacade FACADE_JPG = DocValidatorFacade.newFacadeStrict( 
			P7MContentValidator.newValidator( DocValidatorFacade.newFacade( ImageValidator.JPG_VALIDATOR ) )
	);
	
	private static final DocValidatorFacade FACADE_NULL = DocValidatorFacade.newFacadeStrict( 
			P7MContentValidator.newValidator( null )
	);

	private static final P7MContentValidator CONTENT_JPG_PROCEED =
			P7MContentValidator.newValidator( DocValidatorFacade.newFacade( ImageValidator.JPG_VALIDATOR ), true );

	private static final P7MContentValidator CONTENT_PDF_PROCEED =
			P7MContentValidator.newValidator( DocValidatorFacade.newFacade( PdfboxStrictValidator.DEFAULT ), true );

	protected String worker( P7MContentValidator validator, String fileName ) {
		return SafeFunction.get( () -> {
			String path = BASE_PATH+"/"+fileName;
			log.info( "test path to check {}", path );
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( path ) ) {
				return validator.checkInnerType( is );
			}
		} );
	}
	
	@Test
	public void testP7MAsP7M() {
		boolean ok = this.worker(FACADE_PDF, FILENAME_PDF_AS_P7M, true );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testP7MAsP7MNull() {
		boolean ok = this.worker(FACADE_NULL, FILENAME_PDF_AS_P7M, true );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testP7MAsP7MKo() {
		boolean ok = this.worker(FACADE_JPG, FILENAME_PDF_AS_P7M, false );
		Assert.assertTrue( ok );
	}

	@Test
	public void testKO() {
		Assert.assertNull( this.worker( CONTENT_JPG_PROCEED, "docx_as_docx.docx" ) );
	}

	@Test
	public void testProccedKo() {
		Assert.assertNull( this.worker( CONTENT_JPG_PROCEED,  FILENAME_PDF_AS_P7M ) );
	}

	@Test
	public void testProccedOk() {
		Assert.assertEquals( PdfboxStrictValidator.DEFAULT.getMimeType(), this.worker( CONTENT_PDF_PROCEED,  FILENAME_PDF_AS_P7M ) );
	}
	
}
