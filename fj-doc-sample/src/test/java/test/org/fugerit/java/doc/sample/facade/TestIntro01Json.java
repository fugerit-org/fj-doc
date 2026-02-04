package test.org.fugerit.java.doc.sample.facade;

import org.fugerit.java.doc.base.config.DocConfig;
import org.junit.jupiter.api.condition.DisabledForJreRange;

@DisabledForJreRange( minVersion = 25)
class TestIntro01Json extends BasicFacadeTest {

	public TestIntro01Json() {
		super.setup( "intro_01.json",  DocConfig.TYPE_MD, DocConfig.TYPE_HTML, DocConfig.TYPE_PDF, DocConfig.TYPE_FO );
	}
	
}
