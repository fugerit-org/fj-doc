package test.org.fugerit.java.doc.sample.freemarker;

import org.fugerit.java.doc.base.config.DocConfig;

public class TestFreeMarker02 extends BasicFreeMarkerTest {

	public TestFreeMarker02() {
		super.setupFreemMarker( "free-marker-02", DocConfig.TYPE_XML, DocConfig.TYPE_PDF, DocConfig.TYPE_FO, DocConfig.TYPE_XLS, DocConfig.TYPE_HTML, DocConfig.TYPE_XLSX );
	}

	
	
}
