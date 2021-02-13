package test.org.fugerit.java.doc.sample.facade;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.sample.facade.SampleFacade;

public class TestIntro01PdfBox extends BasicFacadeTest {

	public TestIntro01PdfBox() {
		super( "intro_01", DocConfig.TYPE_XML, DocConfig.TYPE_PDF );
		this.setFacadeId( SampleFacade.ALT_COMPLETE_FACTORY );
	}
	
}
