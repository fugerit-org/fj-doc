package test.org.fugerit.java.doc.base.md;

import java.io.PrintWriter;

import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocList;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.typehandler.markdown.MarkdownBasicDocFacade;
import org.fugerit.java.doc.base.typehandler.markdown.MarkdownExtDocFacade;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownBasicTypeHandlerNoCommentsUTF8;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandlerNoCommentsUTF8;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestMDHandlers {

	@Test
	void testSimpleMarkdownBasicTypeHandlerNoCommentsUTF8() {
		Assertions.assertNotNull( new SimpleMarkdownBasicTypeHandlerNoCommentsUTF8() );
	}
	
	@Test
	void testSimpleMarkdownExtTypeHandlerNoCommentsUTF8() {
		Assertions.assertNotNull( new SimpleMarkdownExtTypeHandlerNoCommentsUTF8() );
	}
	
	@Test
	void testMarkdownBasicDocFacade1() throws DocException {
		try ( PrintWriter writer = new PrintWriter( System.out ) ) {
			MarkdownBasicDocFacade facade = new MarkdownBasicDocFacade( writer );
			Assertions.assertNotNull( facade );
			DocBase docBase = new DocBase();
			facade.handleDoc(docBase);
		}
	}
	
	@Test
	void testMarkdownBasicDocFacade2() throws DocException {
		try ( PrintWriter writer = new PrintWriter( System.out ) ) {
			MarkdownBasicDocFacade facade = new MarkdownBasicDocFacade( writer, false );
			Assertions.assertNotNull( facade );
			DocBase docBase = new DocBase();
			DocList docList = new DocList();
			docList.addElement( new DocPara() ); // impossible situation, just for coverage
			docBase.getDocBody().addElement( docList );
			facade.handleDoc(docBase);
		}
	}	
	
	@Test
	void testMarkdownExtDocFacade1() throws DocException {
		try ( PrintWriter writer = new PrintWriter( System.out ) ) {
			MarkdownExtDocFacade facade = new MarkdownExtDocFacade( writer );
			Assertions.assertNotNull( facade );
			DocBase docBase = new DocBase();
			facade.handleDoc(docBase);
		}
	}
	
}
