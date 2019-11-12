package test.org.fugerit.java.doc.sample.freemarker;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.sample.facade.SampleFacade;

public class TestFreeMarker01Fop extends BasicFreeMarkerTest {

	public TestFreeMarker01Fop() {
		super( "free-marker-01-fop", DocConfig.TYPE_FO, DocConfig.TYPE_PDF );
		this.setFacadeId( SampleFacade.ALT_FOP_FACTORY );
	}

}
