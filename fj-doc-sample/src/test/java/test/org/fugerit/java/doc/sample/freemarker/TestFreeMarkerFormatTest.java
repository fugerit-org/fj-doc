package test.org.fugerit.java.doc.sample.freemarker;

import org.fugerit.java.doc.base.config.DocConfig;

public class TestFreeMarkerFormatTest extends BasicFreeMarkerTest {

	public TestFreeMarkerFormatTest() {
		super.setupFreemMarker( "free-marker-format-test", DocConfig.TYPE_XML, DocConfig.TYPE_PDF, DocConfig.TYPE_XLS, DocConfig.TYPE_HTML, DocConfig.TYPE_XLSX );
	}

	
	
}
