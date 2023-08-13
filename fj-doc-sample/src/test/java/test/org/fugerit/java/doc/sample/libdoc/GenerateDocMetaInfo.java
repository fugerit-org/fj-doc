package test.org.fugerit.java.doc.sample.libdoc;

import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandler;
import org.junit.Test;

public class GenerateDocMetaInfo extends GenerateDocHelper {

	@Test
	public void generateHtml() {
		this.docWorker( "lib_docs/doc_meta_info.xml" , "../docs/html/doc_meta_info.html", FreeMarkerHtmlTypeHandler.HANDLER_UTF8 );
	}
	
}
