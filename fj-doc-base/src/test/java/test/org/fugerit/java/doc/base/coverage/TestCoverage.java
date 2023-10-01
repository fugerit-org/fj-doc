package test.org.fugerit.java.doc.base.coverage;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerXML;
import org.fugerit.java.doc.base.config.DocTypeHandlerXMLUTF8;
import org.fugerit.java.doc.base.config.DocVersion;
import org.fugerit.java.doc.base.config.InitHandler;
import org.fugerit.java.doc.base.facade.SimpleDocFacade;
import org.fugerit.java.doc.base.helper.DocTypeFacadeDefault;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.DocParserContext;
import org.fugerit.java.doc.base.typehandler.markdown.AbstractCustomMarkdownTypeHandler;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownBasicTypeHandler;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownBasicTypeHandlerUTF8;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandler;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandlerUTF8;
import org.fugerit.java.doc.base.xml.DocXmlParser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import test.org.fugerit.java.BasicTest;

@Slf4j
public class TestCoverage extends BasicTest {

	private final static String[] TEST_LIST = { "default_doc", "default_doc_alt", "default_doc_missing_tag", "default_doc_fail1", "default_doc_fail2", "default_doc_empty" };
	
	private final static DocTypeHandler[] HANDLERS = { 
			DocTypeHandlerXML.HANDLER, 
			DocTypeHandlerXMLUTF8.HANDLER, 
			SimpleMarkdownBasicTypeHandler.HANDLER,
			SimpleMarkdownBasicTypeHandlerUTF8.HANDLER,
			SimpleMarkdownExtTypeHandler.HANDLER,
			SimpleMarkdownExtTypeHandlerUTF8.HANDLER,
			new AbstractCustomMarkdownTypeHandler() {
				private static final long serialVersionUID = 3101144368912260501L;
				@Override
				public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
					DocBase docBase = docInput.getDoc();
					DocTypeFacadeDefault facade = new DocTypeFacadeDefault(); // only to test DocTypeFacadeDefault
					facade.handlePara(null, null, null);
					facade.handlePhrase(null, null, null);
					facade.handleList(null, null, null);
					facade.handleTable(null, null, null);
					facade.handleDoc( docBase );
					docOutput.getOs().write( "OK".getBytes() );
				}
			}
		};
	
	@BeforeClass
	public static void init() {
		Arrays.asList( HANDLERS ).forEach( h -> SafeFunction.apply( () -> InitHandler.initDoc( h ) ) );
	}
	
	private boolean worker( String path ) {
		SimpleValue<Boolean> res = new SimpleValue<>(false);
		SafeFunction.apply( () -> {
			for ( int k=0; k<HANDLERS.length; k++ ) {
				DocTypeHandler handler = HANDLERS[k];
				try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader(path) );
						ByteArrayOutputStream buffer = new ByteArrayOutputStream() ) {
						handler.handle( DocInput.newInput( handler.getType() , reader ) ,  DocOutput.newOutput( buffer ) );
						res.setValue( buffer.toByteArray().length > 0 );
				}
			}
		} );
		return res.getValue();
	}
	
	@Test
	public void test02() {
		Arrays.asList( TEST_LIST ).stream().forEach( c -> {
			Assert.assertTrue( this.worker( "coverage/xml/"+c+".xml" ) );
		} );
	}
	
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
	
	private boolean workerAlt( String path ) {
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
			Assert.assertTrue( this.workerAlt( "coverage/xml/"+c+".xml" ) );
		} );
	}
	
	
	@Test
	public void test03() {
		SafeFunction.apply( () -> {
			File file = new File( "target/test_output.md" );
			SimpleDocFacade.produce( SimpleMarkdownExtTypeHandler.HANDLER , "cl://coverage/xml/default_doc.xml", file );			
			Assert.assertTrue( file.exists() );
		} );
	}

	@Test
	public void test04() {
		SafeFunction.apply( () -> {
			try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "coverage/xml/default_doc.xml" ) );
					ByteArrayOutputStream buffer = new ByteArrayOutputStream() ) {
				SimpleDocFacade.produce( SimpleMarkdownExtTypeHandler.HANDLER , reader, buffer  );				
				Assert.assertNotEquals( 0 , buffer.toByteArray().length );
			}
		} );
	}
	
	@Test
	public void testXsdVersion() {
		String version = DocParserContext.createXsdVersionXmlns( DocVersion.VERSION_2_1.stringVersion() );
		log.info( "version : {}", version );
		Assert.assertTrue( version.endsWith( "doc-2-1.xsd" ) );
	}
	
}

