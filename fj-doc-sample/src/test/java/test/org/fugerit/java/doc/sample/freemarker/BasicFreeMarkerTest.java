package test.org.fugerit.java.doc.sample.freemarker;

import java.io.InputStream;
import java.io.Reader;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.util.filterchain.MiniFilterChain;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.process.DocProcessConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.freemarker.config.FreeMarkerStep;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

public class BasicFreeMarkerTest extends BasicFacadeTest {

	public BasicFreeMarkerTest() {
		super();
	}

	protected BasicFreeMarkerTest(String nameBase, String... typeList) {
		super(nameBase, typeList);
	}

	private static DocProcessConfig PROCESS_CONFIG = null;
	private static Configuration FREE_MARKER_CONFIG = new Configuration( Configuration.VERSION_2_3_29 );
	
	static {
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "config/doc-process-sample.xml" ) ) {
			PROCESS_CONFIG = DocProcessConfig.loadConfig( is );
			FREE_MARKER_CONFIG.setClassForTemplateLoading( BasicFreeMarkerTest.class , "/free_marker/" );
			//cfg.setDirectoryForTemplateLoading( new File( "src/test/resources/free_marker" ) );
			// Set the preferred charset template files are stored in. UTF-8 is
			// a good choice in most applications:
			FREE_MARKER_CONFIG.setDefaultEncoding("UTF-8");
			// Sets how errors will appear.
			// During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
			FREE_MARKER_CONFIG.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			// Don't log exceptions inside FreeMarker that it will thrown at you anyway:
			FREE_MARKER_CONFIG.setLogTemplateExceptions(false);
			// Wrap unchecked exceptions thrown during template processing into TemplateException-s:
			FREE_MARKER_CONFIG.setWrapUncheckedExceptions(true);
			// Do not fall back to higher scopes when reading a null loop variable:
			FREE_MARKER_CONFIG.setFallbackOnNullLoopVariable(false);
		} catch (Exception e) {
			throw new RuntimeException( e ); 
		}
	}

	@Override
	protected DocBase getDocBase() throws Exception {
		MiniFilterChain chain = PROCESS_CONFIG.getChainCache( this.getNameBase() );
		DocProcessContext context = new DocProcessContext();
		context.setAttribute( FreeMarkerStep.ATT_FREEMARKER_CONFIG , FREE_MARKER_CONFIG );
		DocProcessData data = new DocProcessData();
		int res = chain.apply( context , data );
		logger.info( "RES {} ", res );
		DocBase docBase = null;
		try ( Reader input = data.getCurrentXmlReader() ) {
			 docBase = DocFacade.parse( input );
		}
		return docBase;
	}
	
}
