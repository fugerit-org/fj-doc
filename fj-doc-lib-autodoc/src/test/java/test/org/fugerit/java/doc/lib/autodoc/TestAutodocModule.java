package test.org.fugerit.java.doc.lib.autodoc;

import org.fugerit.java.doc.lib.autodoc.AutodocModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestAutodocModule {

	@Test
	void testModule() {
		Assertions.assertEquals( AutodocModule.CURRENT_VERSION , new AutodocModule().getVersion() );
	}
	
}
