package test.org.fugerit.java.doc.sample.freemarker;

import java.io.Reader;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;

import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

public class BasicFreeMarkerTest extends BasicFacadeTest {

	public BasicFreeMarkerTest() {
		this( "basic", DocConfig.TYPE_PDF, DocConfig.TYPE_XLS, DocConfig.TYPE_HTML, DocConfig.TYPE_CSV );
	}

	protected BasicFreeMarkerTest(String nameBase, String... typeList) {
		super(nameBase, typeList);
	}

	@Override
	protected Reader getXmlReader() throws Exception {
		return this.process( this.getNameBase() );
	}

	public Reader process( String chainId ) throws Exception {
		DocProcessContext context = new DocProcessContext();
		DocProcessData data = new DocProcessData();
		PROCESSCONFIG.process( chainId, context, data );
		return data.getCurrentXmlReader();
	}

}
