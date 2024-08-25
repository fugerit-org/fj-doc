package test.org.fugerit.java.doc.base.xml;
import java.util.HashMap;
import java.util.Map;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.doc.base.xml.SAXUtils;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestSAXUtils {
	
	@Test
	public void testNewSafeInstance() {
		Assert.assertNotNull(SafeFunction.get( () -> SAXUtils.newSafeFactory() ) );
	}
	
	@Test
	public void testNewInstanceFail() {
		Map<String, Boolean> features = new HashMap<>();
		features.put( "http://xml.org/sax/features/external-general-entities" , false);
		features.put( "feature-not-exists" , false);
		Assert.assertThrows( ConfigRuntimeException.class, () -> SAXUtils.newSafeFactory(features) );
	}

}

