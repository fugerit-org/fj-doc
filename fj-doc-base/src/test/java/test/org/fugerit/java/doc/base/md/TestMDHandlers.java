package test.org.fugerit.java.doc.base.md;

import java.io.IOException;
import java.io.PrintWriter;

import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocList;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.typehandler.markdown.MarkdownBasicDocFacade;
import org.fugerit.java.doc.base.typehandler.markdown.MarkdownExtDocFacade;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownBasicTypeHandlerNoCommentsUTF8;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandlerNoCommentsUTF8;
import org.junit.Assert;
import org.junit.Test;

public class TestMDHandlers {

	@Test
	public void testSimpleMarkdownBasicTypeHandlerNoCommentsUTF8() throws IOException {
		Assert.assertNotNull( new SimpleMarkdownBasicTypeHandlerNoCommentsUTF8() );
	}
	
	@Test
	public void testSimpleMarkdownExtTypeHandlerNoCommentsUTF8() throws IOException {
		Assert.assertNotNull( new SimpleMarkdownExtTypeHandlerNoCommentsUTF8() );
	}
	
	@Test
	public void testMarkdownBasicDocFacade1() throws IOException, DocException {
		try ( PrintWriter writer = new PrintWriter( System.out ) ) {
			MarkdownBasicDocFacade facade = new MarkdownBasicDocFacade( writer );
			Assert.assertNotNull( facade );
			DocBase docBase = new DocBase();
			facade.handleDoc(docBase);
		}
	}
	
	@Test
	public void testMarkdownBasicDocFacade2() throws IOException, DocException {
		try ( PrintWriter writer = new PrintWriter( System.out ) ) {
			MarkdownBasicDocFacade facade = new MarkdownBasicDocFacade( writer, false );
			Assert.assertNotNull( facade );
			DocBase docBase = new DocBase();
			DocList docList = new DocList();
			docList.addElement( new DocPara() ); // impossible situation, just for coverage
			docBase.getDocBody().addElement( docList );
			facade.handleDoc(docBase);
		}
	}	
	
	@Test
	public void testMarkdownExtDocFacade1() throws IOException, DocException {
		try ( PrintWriter writer = new PrintWriter( System.out ) ) {
			MarkdownExtDocFacade facade = new MarkdownExtDocFacade( writer );
			Assert.assertNotNull( facade );
			DocBase docBase = new DocBase();
			facade.handleDoc(docBase);
		}
	}
	
}
