package org.fugerit.java.doc.playground;

import java.io.IOException;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.playground.config.InitPlayground;
import org.fugerit.java.doc.playground.doc.GenerateFacade;
import org.fugerit.java.doc.playground.doc.GenerateInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestGenerateFacade {

	private GenerateFacade facade = new GenerateFacade();
	
	@Test
	void testFacadeCsv() throws ConfigException, IOException {
		GenerateInput input = new GenerateInput();
		input.setOutputFormat( DocConfig.TYPE_CSV );
		Assertions.assertNotNull( facade.findHandler(input) );
	}
	
	@Test
	void testFacadePDFA() throws ConfigException, IOException {
		GenerateInput input = new GenerateInput();
		input.setOutputFormat( InitPlayground.OUTPUT_FORMAT_PDF_A );
		Assertions.assertNotNull( facade.findHandler(input) );
	}
		
}
