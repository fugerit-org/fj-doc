package test.org.fugerit.java.doc.sample.facade;

import org.fugerit.java.doc.base.config.DocConfig;

public class TestIntro01 extends BasicFacadeTest {

	public TestIntro01() {
		super( "intro_01", DocConfig.TYPE_XML, DocConfig.TYPE_PDF, DocConfig.TYPE_XLS, DocConfig.TYPE_RTF, DocConfig.TYPE_HTML );
	}
	
}
