package test.org.fugerit.java.doc.val.p7m;

import java.io.*;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cms.CMSObjectIdentifiers;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.openssl.PEMParser;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.val.p7m.P7MUtils;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestExtractContentP7M {

	@Test
	public void testDataProcess() throws IOException {
		Assert.assertThrows( CMSException.class , () -> P7MUtils.extractContentCMSSignedDataProcess( null, null ) ) ;
	}

	@Test
	public void testContentInfo() throws IOException {
		Assert.assertThrows( CMSException.class , () -> P7MUtils.checkContentInfo( null ) ) ;
		try (InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/pkcs7_test_ok1.p7m" ) ) {
			String in = new String(StreamIO.readBytes( is ));
			try ( PEMParser pp = new PEMParser(new StringReader(in) ) ) {
				ContentInfo ciTest = (ContentInfo) pp.readObject();
				ContentInfo ci = new ContentInfo( ciTest.getContentType(), ciTest.getContent() ){
					@Override
					public ASN1ObjectIdentifier getContentType() {
						return CMSObjectIdentifiers.authenticatedData;
					}
				};
				Assert.assertThrows( CMSException.class , () -> P7MUtils.checkContentInfo( ci ) ) ;
			}
		}
	}

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
