package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocLi;
import org.fugerit.java.doc.base.model.DocList;
import org.fugerit.java.doc.base.model.DocPara;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocLi extends HelperDocT {

	
	private String worker( DocLi element ) {
		this.baseTest(element);
		if ( !element.getElementList().isEmpty() ) {
			log.info( "test 1 -> {} -> {}", element.getContent(), element.isContentList() );
			if ( element.getElementList().size() > 1 ) {
				log.info( "test 1 -> {} -> {}", element.getSecondContent(), element.isSecondList() );
			}	
		}
		return element.getId();
	}
	
	@Test
	public void testElement1() {
		DocLi element = new DocLi();
		element.setId( TEST_ID );
		Assert.assertNotNull( this.worker( element ) );
		element.getElementList().add( new DocPara() );
		Assert.assertNotNull( this.worker( element ) );
		element.getElementList().add( new DocList() );
		Assert.assertNotNull( this.worker( element ) );
	}
	
	@Test
	public void testElement2() {
		DocLi element = new DocLi();
		element.setId( TEST_ID );
		Assert.assertNotNull( this.worker( element ) );
		element.getElementList().add( new DocPara() );
		Assert.assertNotNull( this.worker( element ) );
		element.getElementList().add( new DocPara() );
		Assert.assertNotNull( this.worker( element ) );
	}
	
	@Test
	public void testElement3() {
		DocLi element = new DocLi();
		element.setId( TEST_ID );
		Assert.assertNotNull( this.worker( element ) );
		element.getElementList().add( new DocList() );
		Assert.assertNotNull( this.worker( element ) );
		element.getElementList().add( new DocList() );
		Assert.assertNotNull( this.worker( element ) );
	}
	
}
