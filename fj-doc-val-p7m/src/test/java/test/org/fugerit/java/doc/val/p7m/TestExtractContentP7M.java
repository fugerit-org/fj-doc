package test.org.fugerit.java.doc.val.p7m;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import org.bouncycastle.cms.CMSException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.doc.val.p7m.P7MUtils;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestExtractContentP7M {
	
	@Test
	public void testP7MKo() {
		Assert.assertThrows( CMSException.class , () -> {
			String path = "src/test/resources/sample/png_as_p7m.p7m";
			File testP7M = new File( path );
			log.info( "test extract ko : {}", testP7M.getCanonicalPath() );
			try ( FileInputStream is = new FileInputStream( testP7M );
					ByteArrayOutputStream os = new ByteArrayOutputStream() ) {
				P7MUtils.extractContent(is, os);
			}
		}) ;
	}
	
	@Test
	public void testP7MOk() {
		Assert.assertTrue( SafeFunction.get( () -> {
			String path = "src/test/resources/sample/pdf_as_pdf.p7m";
			File testP7M = new File( path );
			log.info( "test extract : {}", testP7M.getCanonicalPath() );
			File outputBase = new File( "target" );
			File outputContent = new File( outputBase, "content.pdf" );
			outputContent.delete();
			try ( FileInputStream is = new FileInputStream( testP7M );
					ByteArrayOutputStream os = new ByteArrayOutputStream() ) {
				P7MUtils.extractContent(is, os);
				FileIO.writeBytes( os.toByteArray() , outputContent );
			}
			return outputContent.exists();
		} ) );
	}
	
}
