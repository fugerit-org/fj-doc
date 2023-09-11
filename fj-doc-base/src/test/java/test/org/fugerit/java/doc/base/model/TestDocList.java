package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocList;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocList extends HelperDocT {

	
	private String worker( DocList element ) {
		this.baseTest(element);
		log.info( "test 1 -> {}", element.getClt() );
		log.info( "test 2 -> {}", element.getHtmlType() );
		return element.getId();
	}
	
	@Test
	public void testElement() {
		DocList element = new DocList();
		element.setId( TEST_ID );
		element.setListType( DocList.LIST_TYPE_OL );
		Assert.assertNotNull( this.worker( element ) );
		element.setListType( DocList.LIST_TYPE_OLL );
		Assert.assertNotNull( this.worker( element ) );
		element.setListType( DocList.LIST_TYPE_OLN );
		Assert.assertNotNull( this.worker( element ) );
		element.setListType( DocList.LIST_TYPE_UL );
		Assert.assertNotNull( this.worker( element ) );
		element.setListType( DocList.LIST_TYPE_ULD );
		Assert.assertNotNull( this.worker( element ) );
		element.setListType( DocList.LIST_TYPE_ULM );
		Assert.assertNotNull( this.worker( element ) );
	}
	
}
