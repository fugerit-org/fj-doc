package test.org.fugerit.java.doc.sample.dev;

import org.fugerit.java.doc.base.config.DocConfig;

import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

public class AutodocDetailFullDocHelper extends BasicFacadeTest {
	
	public AutodocDetailFullDocHelper() {
		//super( "full_doc_header_footer", DocConfig.TYPE_HTML, DocConfig.TYPE_PDF, DocConfig.TYPE_FO );
		super( "full_doc_header_footer", DocConfig.TYPE_HTML );
	}	
	
}
