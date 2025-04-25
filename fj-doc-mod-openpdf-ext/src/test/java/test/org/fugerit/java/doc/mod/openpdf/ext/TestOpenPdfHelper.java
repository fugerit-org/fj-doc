package test.org.fugerit.java.doc.mod.openpdf.ext;

import com.lowagie.text.Document;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.doc.base.model.DocLi;
import org.fugerit.java.doc.base.model.DocList;
import org.fugerit.java.doc.base.model.DocTable;
import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPdfHelper;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPpfDocHandler;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.PhraseParent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

import java.io.IOException;

class TestOpenPdfHelper {

	@Test
	void test001() throws Exception {
		PhraseParent parent = new PhraseParent( new Phrase() );
		parent.add( new Paragraph() );
		Assertions.assertNotNull( parent );
	}

	@Test
	void testList() {
		OpenPdfHelper helper = new OpenPdfHelper();
		try ( Document document = new Document() ) {
			DocList list1 = new DocList();
			list1.addElement( new DocTable() );
			list1.setListType(DocList.LIST_TYPE_OL);
			Assertions.assertThrows( IOException.class, () ->  OpenPpfDocHandler.getElement( document, list1, false, helper ) );
			DocList list2 = new DocList();
			list2.setListType(DocList.LIST_TYPE_UL);
			DocLi li2 = new DocLi();
			li2.addElement( new DocTable() );
			list2.addElement( li2 );
			helper.getParams().setProperty(GenericConsts.DOC_SUPPRESS_WRONG_TYPE_ERROR, BooleanUtils.BOOLEAN_1 );
			SafeFunction.apply( () -> OpenPpfDocHandler.getElement( document, list2, false, helper ) );
		}
	}

}
