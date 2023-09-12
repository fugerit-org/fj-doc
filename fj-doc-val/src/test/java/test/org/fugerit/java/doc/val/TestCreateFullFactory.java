package test.org.fugerit.java.doc.val;

import org.fugerit.java.doc.val.DocValidatorFacadeFactory;
import org.junit.Assert;
import org.junit.Test;

public class TestCreateFullFactory {

	@Test
	public void test() {
		Assert.assertNotNull( new DocValidatorFacadeFactory().createFullDocValidatorFacade() );
	}
	
}
