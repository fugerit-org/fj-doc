package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestDocList extends HelperDocT {

	
	private String worker( DocList element ) {
		this.baseTest(element);
		log.info( "test 1 -> {}", element.getClt() );
		log.info( "test 2 -> {}", element.getHtmlType() );
		return element.getId();
	}
	
	@Test
	void testElement() {
		DocList element = new DocList();
		element.setId( TEST_ID );
		element.setListType( DocList.LIST_TYPE_OL );
		Assertions.assertNotNull( this.worker( element ) );
		Assertions.assertFalse( element.isUnordered() );
		Assertions.assertTrue( element.isOrdered() );
		element.setListType( DocList.LIST_TYPE_OLL );
		Assertions.assertNotNull( this.worker( element ) );
		element.setListType( DocList.LIST_TYPE_OLN );
		Assertions.assertNotNull( this.worker( element ) );
		element.setListType( DocList.LIST_TYPE_UL );
		Assertions.assertNotNull( this.worker( element ) );
		element.setListType( DocList.LIST_TYPE_ULD );
		Assertions.assertNotNull( this.worker( element ) );
		element.setListType( DocList.LIST_TYPE_ULM );
		Assertions.assertNotNull( this.worker( element ) );
		Assertions.assertTrue( element.isUnordered() );
		Assertions.assertFalse( element.isOrdered() );
	}
	
}
