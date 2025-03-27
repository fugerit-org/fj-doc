package test.org.fugerit.java.doc.sample.freemarker;

import org.fugerit.java.doc.mod.fop.FreeMarkerFopTypeHandler;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;

public class TestFreeMarker01FullFacade extends BasicFreeMarkerTest {

	public TestFreeMarker01FullFacade() {
		super.setupFreemMarker( "full-facade-01",
			FreeMarkerFopTypeHandler.HANDLER.getKey(), 
			PdfFopTypeHandler.HANDLER.getKey()  );
	}

}
