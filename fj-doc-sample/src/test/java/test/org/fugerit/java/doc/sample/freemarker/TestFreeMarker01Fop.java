package test.org.fugerit.java.doc.sample.freemarker;

import org.fugerit.java.doc.base.config.DocConfig;

public class TestFreeMarker01Fop extends BasicFreeMarkerTest {

	public TestFreeMarker01Fop() {
		super.setupFreemMarker( "free-marker-01-fop", DocConfig.TYPE_FO, DocConfig.TYPE_PDF );
	}

}
