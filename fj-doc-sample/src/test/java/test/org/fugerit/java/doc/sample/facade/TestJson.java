package test.org.fugerit.java.doc.sample.facade;

import org.fugerit.java.doc.base.config.DocConfig;

public class TestJson extends BasicFacadeTest {

	public TestJson() {
		super( "intro_01.json",  DocConfig.TYPE_MD, DocConfig.TYPE_HTML, DocConfig.TYPE_PDF, DocConfig.TYPE_FO );
	}
	
}
