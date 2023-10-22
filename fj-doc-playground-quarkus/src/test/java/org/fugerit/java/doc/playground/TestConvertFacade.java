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
	
	@Test
	void testFacadeWrong1() throws ConfigException, IOException {
		Assertions.assertNull( facade.handleConvert( InputFacade.FORMAT_JSON, "{}" , FORMAT_NOT_EXISTS, false ) );
	}
	
	@Test
	void testFacadeWrong2() throws ConfigException, IOException {
		Assertions.assertNull( facade.handleConvert( InputFacade.FORMAT_YAML, "--" , FORMAT_NOT_EXISTS, false ) );
	}

	@Test
	void testFacadeWrong3() throws ConfigException, IOException {
		Assertions.assertNull( facade.handleConvert( InputFacade.FORMAT_XML, "<config/>", FORMAT_NOT_EXISTS, false ) );
	}
	
	@Test
	void testFacadeWrong4() throws ConfigException, IOException {
		Assertions.assertNull( facade.handleConvert( FORMAT_NOT_EXISTS, "..", FORMAT_NOT_EXISTS, false ) );
	}
	
}
