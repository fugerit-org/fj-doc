package test.org.fugerit.java.doc.sample.freemarker;

import java.io.InputStreamReader;
import java.io.Reader;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;

import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

public class BasicFreeMarkerTest extends BasicFacadeTest {

	public BasicFreeMarkerTest() {
		this( "basic", DocConfig.TYPE_PDF, DocConfig.TYPE_XLS, DocConfig.TYPE_HTML, DocConfig.TYPE_CSV );
	}

	protected BasicFreeMarkerTest(String nameBase, String... typeList) {
		super(nameBase, typeList);
	}

	private static FreemarkerDocProcessConfig init() {
		FreemarkerDocProcessConfig config = null;
		try ( InputStreamReader xmlReader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "config/freemarker-doc-process.xml" ) ) ) {
			config = FreemarkerDocProcessConfigFacade.loadConfig( xmlReader );
		} catch (Exception e) {
			throw new RuntimeException( e ); 
		}
		return config;
	}
	
	private static FreemarkerDocProcessConfig PROCESS_CONFIG = init();

	@Override
	protected Reader getXmlReader() throws Exception {
		return this.process( this.getNameBase() );
	}

	public Reader process( String chainId ) throws Exception {
		DocProcessContext context = new DocProcessContext();
		DocProcessData data = new DocProcessData();
		PROCESS_CONFIG.process( "FJ_SAMPLE_TEST" , chainId, context, data );
		return data.getCurrentXmlReader();
	}

}
