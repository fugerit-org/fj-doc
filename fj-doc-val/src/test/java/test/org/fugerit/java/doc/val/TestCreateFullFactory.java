package test.org.fugerit.java.doc.val;

import org.fugerit.java.doc.val.DocValidatorFacadeFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestCreateFullFactory {

	@Test
	void test() {
		Assertions.assertNotNull( new DocValidatorFacadeFactory().createFullDocValidatorFacade() );
	}
	
}
