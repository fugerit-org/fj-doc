package test.org.fugerit.java.doc.sample.facade;

import org.fugerit.java.doc.base.config.DocConfig;

class TestAutodocHelper extends BasicFacadeTest {

	public TestAutodocHelper() {
		super.setup( "autodoc_helper", DocConfig.TYPE_XML, DocConfig.TYPE_PDF, DocConfig.TYPE_XLSX, DocConfig.TYPE_HTML );
	}
	
}
