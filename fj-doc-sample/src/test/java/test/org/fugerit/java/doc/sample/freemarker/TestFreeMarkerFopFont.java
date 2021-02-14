package test.org.fugerit.java.doc.sample.freemarker;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.sample.facade.SampleFacade;

public class TestFreeMarkerFopFont extends BasicFreeMarkerTest {

	public TestFreeMarkerFopFont() {
		super( "free-marker-test-fop-font", DocConfig.TYPE_FO, DocConfig.TYPE_PDF );
		this.setFacadeId( SampleFacade.ALT_FOP_FACTORY );
	}

}
