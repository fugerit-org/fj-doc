package test.org.fugerit.java.doc.base.xml;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.doc.base.xml.SAXUtils;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestSAXUtils {

	private void failEx( Exception e ) {
		String message = "Error : "+e.getMessage();
		log.error( message, e );
		fail( message );
	}
	
	@Test
	public void testNewSafeInstance() {
		try {
			SAXParserFactory factory = SAXUtils.newSafeFactory();
			Assert.assertTrue( factory != null );
		} catch (Exception e) {
			this.failEx(e);
		}
	}
	
	@Test
	public void testNewInstanceFail() {
		try {
			Map<String, Boolean> features = new HashMap<>();
			features.put( "feature-not-exists" , false);
			SAXParserFactory factory = SAXUtils.newFactory(features);
			fail( "Should not arrive here "+factory );
		} catch (Exception e) {
			Assert.assertTrue( e instanceof ConfigRuntimeException );
		}
	}

}

