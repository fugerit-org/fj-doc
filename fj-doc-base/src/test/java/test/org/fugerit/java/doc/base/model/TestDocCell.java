package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocCell;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocCell {

	@Test
	public void test1() {
		DocCell model = new DocCell();
		model.setRSpan(0);
		model.setCSpan(0);
		log.info( "model -> {}", model );
		Assert.assertEquals( 1 , model.getRowSpan() );
		Assert.assertEquals( 1 , model.getColumnSpan() );
		Assert.assertFalse( model.isHeader() );
	}
	
}
