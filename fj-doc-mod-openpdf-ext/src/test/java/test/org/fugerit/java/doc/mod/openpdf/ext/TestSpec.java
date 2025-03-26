package test.org.fugerit.java.doc.mod.openpdf.ext;

import org.fugerit.java.doc.base.config.DocConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestSpec extends TestDocBase {

	private static final String DEFAULT_DOC = "test";
	
	@Test
	void testOpenPDF() {
		boolean ok = this.testDocWorker( DEFAULT_DOC ,  DocConfig.TYPE_PDF );
		Assertions.assertTrue(ok);
	}

}
