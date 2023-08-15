package test.org.fugerit.java.doc.lib.autodoc.meta;

import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandler;
import org.junit.Test;

public class GenerateDocMetaInfo extends GenerateDocHelper {

	@Test
	public void generateHtml() {
		this.docWorker( "docs/meta_xml/adm_standard_meta_info.xml" , "../docs/html/doc_meta_info.html", FreeMarkerHtmlTypeHandler.HANDLER_UTF8 );
	}
	
}
