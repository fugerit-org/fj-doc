package test.org.fugerit.java.doc.sample.freemarker;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.sample.facade.SampleFacade;

public class TestFreeMarker01FreeMarkerHtml extends BasicFreeMarkerTest {

	public TestFreeMarker01FreeMarkerHtml() {
		super( "free-marker-01-fop", DocConfig.TYPE_HTML, DocConfig.TYPE_HTML_FRAGMENT );
		this.setFacadeId( SampleFacade.ALT_HTML_FM );
	}

}
