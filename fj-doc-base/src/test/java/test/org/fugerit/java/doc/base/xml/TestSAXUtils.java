package test.org.fugerit.java.doc.base.xml;
import java.util.HashMap;
import java.util.Map;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.doc.base.xml.SAXUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestSAXUtils {
	
	@Test
	void testNewSafeInstance() {
		Assertions.assertNotNull(SafeFunction.get( SAXUtils::newSafeFactory ) );
	}
	
	@Test
	void testNewInstanceFail() {
		Map<String, Boolean> features = new HashMap<>();
		features.put( "http://xml.org/sax/features/external-general-entities" , false);
		features.put( "feature-not-exists" , false);
		Assertions.assertThrows( ConfigRuntimeException.class, () -> SAXUtils.newSafeFactory(features) );
	}

}

