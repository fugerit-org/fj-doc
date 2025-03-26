package test.org.fugerit.java.doc.lib.autodoc.meta;

import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestGenerateDocMetaInfo extends GenerateDocHelper {

	@Test
	void generateHtml() {
		boolean ok = this.docWorker( "docs/meta_xml/adm_standard_meta_info.xml" , "../docs/html/doc_meta_info.html", FreeMarkerHtmlTypeHandler.HANDLER_UTF8 );
		Assertions.assertTrue( ok );
	}
	
}
