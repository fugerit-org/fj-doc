package test.org.fugerit.java.doc.sample.freemarker;

import org.fugerit.java.doc.base.config.DocConfig;

public class TestFreeMarker01 extends BasicFreeMarkerTest {

	public TestFreeMarker01() {
		super.setupFreemMarker( "free-marker-01", DocConfig.TYPE_FO,
				DocConfig.TYPE_XML, DocConfig.TYPE_PDF, 
			    DocConfig.TYPE_HTML);
	}

	
	
}
