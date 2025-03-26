package test.org.fugerit.java.doc.mod.openpdf.ext;

import java.util.Properties;

import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPdfHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestPhraseParent {

	@Test
	void test001() throws Exception {
		OpenPdfHelper helper = new OpenPdfHelper();
		helper.setParams( new Properties() );
		log.info( "helper.getDefFontStyle() -> {}", helper.getDefFontStyle() );
		Assertions.assertNotNull( helper.getParams() );
	}
	
}
