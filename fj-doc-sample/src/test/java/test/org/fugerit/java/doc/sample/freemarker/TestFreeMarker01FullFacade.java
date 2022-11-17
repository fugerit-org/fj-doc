package test.org.fugerit.java.doc.sample.freemarker;

import org.fugerit.java.doc.mod.fop.FreeMarkerFopTypeHandler;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.fugerit.java.doc.sample.facade.SampleFacade;

public class TestFreeMarker01FullFacade extends BasicFreeMarkerTest {

	public TestFreeMarker01FullFacade() {
		super( "full-facade-01", 
			FreeMarkerFopTypeHandler.HANDLER.getKey(), 
			PdfFopTypeHandler.HANDLER.getKey()  );
		this.setFacadeId( SampleFacade.ALT_FULL_FACADE );
	}

}
