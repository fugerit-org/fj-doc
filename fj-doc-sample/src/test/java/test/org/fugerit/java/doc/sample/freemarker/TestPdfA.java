package test.org.fugerit.java.doc.sample.freemarker;

import org.fugerit.java.doc.base.config.DocConfig;

public class TestPdfA extends BasicFreeMarkerTest {

	public TestPdfA() {
		super.setupFreemMarker( "pdf_a_test", DocConfig.TYPE_FO, DocConfig.TYPE_PDF, PDF_A_FOP, PDF_UA_FOP );
	}

}
