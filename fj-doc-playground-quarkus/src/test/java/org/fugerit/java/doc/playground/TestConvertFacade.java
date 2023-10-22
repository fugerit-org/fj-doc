package org.fugerit.java.doc.playground;

import java.io.IOException;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.playground.convert.ConvertFacade;
import org.fugerit.java.doc.playground.facade.InputFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestConvertFacade {

	private ConvertFacade facade = new ConvertFacade();
	
	private static final String FORMAT_NOT_EXISTS = "not-exists";
	
	public static final String SAMPLE_JSON = "{}";
	
	public static final String SAMPLE_YAML = "--- \"test:1\"\n";
	
	public static final String SAMPLE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><doc/>";
	
	@Test
	void testFacadeWrong1() throws ConfigException, IOException {
		Assertions.assertNull( facade.handleConvert( InputFacade.FORMAT_JSON, SAMPLE_JSON , FORMAT_NOT_EXISTS, false ) );
	}
	
	@Test
	void testFacadeWrong2() throws ConfigException, IOException {
		Assertions.assertNull( facade.handleConvert( InputFacade.FORMAT_YAML, SAMPLE_YAML , FORMAT_NOT_EXISTS, false ) );
	}

	@Test
	void testFacadeWrong3() throws ConfigException, IOException {
		Assertions.assertNull( facade.handleConvert( InputFacade.FORMAT_XML, SAMPLE_XML , FORMAT_NOT_EXISTS, false ) );
	}
	
	@Test
	void testFacadeWrong4() throws ConfigException, IOException {
		Assertions.assertEquals( SAMPLE_JSON, facade.handleConvert( InputFacade.FORMAT_JSON, SAMPLE_JSON , InputFacade.FORMAT_JSON, false ) );
	}
	
	@Test
	void testFacadeWrong5() throws ConfigException, IOException {
		Assertions.assertEquals( SAMPLE_YAML, facade.handleConvert( InputFacade.FORMAT_YAML, SAMPLE_YAML , InputFacade.FORMAT_YAML, false ) );
	}

	@Test
	void testFacadeWrong6() throws ConfigException, IOException {
		Assertions.assertEquals( SAMPLE_XML, facade.handleConvert( InputFacade.FORMAT_XML, SAMPLE_XML , InputFacade.FORMAT_XML, false ) );
	}
	
	@Test
	void testFacadeWrong7() throws ConfigException, IOException {
		Assertions.assertNull( facade.handleConvert( FORMAT_NOT_EXISTS, "..", FORMAT_NOT_EXISTS, false ) );
	}
	
}
