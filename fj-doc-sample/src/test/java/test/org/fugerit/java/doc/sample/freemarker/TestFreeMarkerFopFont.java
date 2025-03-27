package test.org.fugerit.java.doc.sample.freemarker;

import org.fugerit.java.doc.base.config.DocConfig;

public class TestFreeMarkerFopFont extends BasicFreeMarkerTest {

	public TestFreeMarkerFopFont() {
		super.setupFreemMarker( "free-marker-test-fop-font", DocConfig.TYPE_FO, DocConfig.TYPE_PDF, DocConfig.TYPE_HTML );
	}

}
