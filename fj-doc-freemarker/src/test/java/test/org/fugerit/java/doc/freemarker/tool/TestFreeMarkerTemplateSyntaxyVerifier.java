package test.org.fugerit.java.doc.freemarker.tool;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.util.result.Result;
import org.fugerit.java.doc.freemarker.tool.FreeMarkerTemplateSyntaxVerifier;
import org.fugerit.java.doc.freemarker.tool.verify.VerifyTemplateInfo;
import org.fugerit.java.doc.freemarker.tool.verify.VerifyTemplateOutput;
import org.junit.Assert;
import org.junit.Test;
import test.org.fugerit.java.BasicTest;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class TestFreeMarkerTemplateSyntaxyVerifier extends BasicTest {

	private VerifyTemplateOutput verifyWorker(String basePath) {
		return this.verifyWorker( basePath, f -> FreeMarkerTemplateSyntaxVerifier.doCreateConfigurationAndVerify( f ) );
	}

	private VerifyTemplateOutput verifyWorker(String basePath, Function<File, VerifyTemplateOutput> verifyFun ) {
		return SafeFunction.get( () -> {
			File baseFolder = new File( basePath );
			log.info( "basic verify for template path : {}", baseFolder.getCanonicalPath() );
			VerifyTemplateOutput output = verifyFun.apply( baseFolder );
			for ( VerifyTemplateInfo info : output.getInfos() ) {
				log.info( "check info : {}", info );
			}
			log.info( "total number of checked template : {}, resultCode : {}", output.getInfos().size(), output.getResultCode() );
			log.info( "Templates with errors : {}", output.getErrorsTemplateIds() );
			return output;
		} );
	}

	@Test
	public void verifyTestOk() {
		Arrays.asList( "src/test/resources/fj_doc_test/template",
				"src/test/resources/fj_doc_test/template-macro" ).forEach(
				basePath -> {
					VerifyTemplateOutput output = verifyWorker( basePath );
					Assert.assertEquals(Result.RESULT_CODE_OK, output.getResultCode());
				}
		);
	}

	@Test
	public void verifyTestKo() {
		Arrays.asList( "src/test/resources/fj_doc_test/template-fail" ).forEach(
				basePath -> {
					VerifyTemplateOutput output = verifyWorker( basePath );
					Assert.assertEquals(Result.RESULT_CODE_KO, output.getResultCode());
				}
		);
	}

	@Test
	public void verifyTemplateFilePattern() {
		Properties params = new Properties();
		params.setProperty( FreeMarkerTemplateSyntaxVerifier.PARAM_TEMPLATE_FILE_PATTERN, ".{0,}[.]ftl" );
		FreeMarkerTemplateSyntaxVerifier verifier = new FreeMarkerTemplateSyntaxVerifier();
		Arrays.asList( "src/test/resources/fj_doc_test/template-fail",
				"src/test/resources/fj_doc_test/template-macro" ).forEach(
				basePath -> {
					VerifyTemplateOutput output = this.verifyWorker( basePath,
							f -> verifier.createConfigurationAndVerify( f, params ) );
					Assert.assertEquals(Result.RESULT_CODE_OK, output.getResultCode());
				}
		);
	}

	@Test
	public void checkPojo() {
		Assert.assertThrows( NullPointerException.class, () -> new VerifyTemplateInfo( Result.RESULT_CODE_OK, null ) );
		Assert.assertThrows( NullPointerException.class, () -> new VerifyTemplateInfo( Result.RESULT_CODE_OK, null, null ) );
		Assert.assertThrows( NullPointerException.class, () -> new VerifyTemplateInfo( null, "1" ) );
		Assert.assertThrows( NullPointerException.class, () -> new VerifyTemplateInfo( null, "2", null ) );
		Assert.assertThrows( NullPointerException.class, () -> new VerifyTemplateInfo( null, null ) );
		Assert.assertThrows( NullPointerException.class, () -> new VerifyTemplateInfo( null, null, null ) );
	}

}
