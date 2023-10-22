package test.org.fugerit.java.doc.lib.autodoc;

import org.fugerit.java.doc.lib.autodoc.AutodocModule;
import org.junit.Assert;
import org.junit.Test;

public class TestAutodocModule {

	@Test
	public void testModule() {
		Assert.assertEquals( AutodocModule.CURRENT_VERSION , new AutodocModule().getVersion() );
	}
	
}
