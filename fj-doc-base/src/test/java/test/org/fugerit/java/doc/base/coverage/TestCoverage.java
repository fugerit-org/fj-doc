package test.org.fugerit.java.doc.base.coverage;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.function.SimpleValue;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.xml.DocXmlParser;
import org.junit.Assert;
import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import test.org.fugerit.java.BasicTest;

@Slf4j
public class TestCoverage extends BasicTest {

	private final static String[] TEST_LIST = { "default_doc", "default_doc_alt", "default_doc_fail1" };
	
	private static final Configuration CFG = SafeFunction.get( () -> {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
		//cfg.setDirectoryForTemplateLoading(new File("src/test/resources/coverage/template"));
		cfg.setClassForTemplateLoading( TestCoverage.class , "/coverage/template/" );
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		cfg.setWrapUncheckedExceptions(true);
		cfg.setFallbackOnNullLoopVariable(false);
		cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
		return cfg;
	} );
	
	private boolean worker( String path ) {
		SimpleValue<Boolean> res = new SimpleValue<>(false);
		runTestEx( () -> {
			Template temp = CFG.getTemplate("/html_doc.ftl");
			Map<String, Object> root = new HashMap<>();
			try ( StringWriter writer = new StringWriter();
					InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader(path) ) ) {
				DocXmlParser parser = new DocXmlParser();
				DocBase docBase = parser.parse( reader );
				root.put( "docBase" , docBase );
				root.put( "docType" , DocConfig.TYPE_HTML );
				temp.process(root, writer);
				String html = writer.toString();
				log.info( "html -> {}", html );
				res.setValue( html.length() > 0 );
			}
		} );
		return res.getValue();
	}
	
	@Test
	public void test01() {
		Arrays.asList( TEST_LIST ).stream().forEach( c -> {
			Assert.assertTrue( this.worker( "coverage/"+c+".xml" ) );
		} );
	}
	
}
