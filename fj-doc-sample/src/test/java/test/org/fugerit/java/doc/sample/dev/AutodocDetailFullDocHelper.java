package test.org.fugerit.java.doc.sample.dev;

import org.fugerit.java.doc.base.config.DocConfig;

import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

public class AutodocDetailFullDocHelper extends BasicFacadeTest {
	
	public AutodocDetailFullDocHelper() {
		super.setup( "full_doc_header_footer", DocConfig.TYPE_HTML, DocConfig.TYPE_HTML_FRAGMENT, DocConfig.TYPE_PDF, DocConfig.TYPE_FO );
	}	
	
}
