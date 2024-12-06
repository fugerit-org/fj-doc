package test.org.fugerit.java.doc.mod.openpdf.ext;

import com.lowagie.text.Document;
import org.fugerit.java.doc.base.model.DocLi;
import org.fugerit.java.doc.base.model.DocList;
import org.fugerit.java.doc.base.model.DocTable;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPdfHelper;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPpfDocHandler;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.PhraseParent;
import org.junit.Assert;
import org.junit.Test;

import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

import java.io.IOException;

public class TestOpenPdfHelper {

	@Test
	public void test001() throws Exception {
		PhraseParent parent = new PhraseParent( new Phrase() );
		parent.add( new Paragraph() );
		Assert.assertNotNull( parent );
	}

	@Test
	public void testList() {
		OpenPdfHelper helper = new OpenPdfHelper();
		try ( Document document = new Document() ) {
			DocList list1 = new DocList();
			list1.addElement( new DocTable() );
			list1.setListType(DocList.LIST_TYPE_OL);
			Assert.assertThrows( IOException.class, () ->  OpenPpfDocHandler.getElement( document, list1, false, helper ) );
			DocList list2 = new DocList();
			list2.setListType(DocList.LIST_TYPE_UL);
			DocLi li2 = new DocLi();
			li2.addElement( new DocTable() );
			list2.addElement( li2 );
			Assert.assertThrows( IOException.class, () ->  OpenPpfDocHandler.getElement( document, list2, false, helper ) );
		}
	}

}
