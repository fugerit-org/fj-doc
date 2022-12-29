package test.org.fugerit.java.doc.sample.facade;

import org.fugerit.java.doc.base.config.DocConfig;

public class TestIntro01Yaml extends BasicFacadeTest {

	public TestIntro01Yaml() {
		super( "intro_01.yaml",  DocConfig.TYPE_MD, DocConfig.TYPE_HTML, DocConfig.TYPE_PDF, DocConfig.TYPE_FO );
		//super( "intro_01.json",  DocConfig.TYPE_MD );
	}
	
}
