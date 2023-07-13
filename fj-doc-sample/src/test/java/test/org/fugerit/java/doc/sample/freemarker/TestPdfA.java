package test.org.fugerit.java.doc.sample.freemarker;

import org.fugerit.java.doc.base.config.DocConfig;

public class TestPdfA extends BasicFreeMarkerTest {

	public TestPdfA() {
		super( "pdf_a_test", DocConfig.TYPE_FO, DocConfig.FORMAT_PDF_A_1A, DocConfig.TYPE_PDF );
	}

}
