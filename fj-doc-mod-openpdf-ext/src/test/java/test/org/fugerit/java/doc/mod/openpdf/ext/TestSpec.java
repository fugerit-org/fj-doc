package test.org.fugerit.java.doc.mod.openpdf.ext;

import org.fugerit.java.doc.base.config.DocConfig;
import org.junit.Assert;
import org.junit.Test;

public class TestSpec extends TestDocBase {

	private static final String DEFAULT_DOC = "test";
	
	@Test
	public void testOpenPDF() {
		boolean ok = this.testDocWorker( DEFAULT_DOC ,  DocConfig.TYPE_PDF );
		Assert.assertTrue(ok);
	}

}
