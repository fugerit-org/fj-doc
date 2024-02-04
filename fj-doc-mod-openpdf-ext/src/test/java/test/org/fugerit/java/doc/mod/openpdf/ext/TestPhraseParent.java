package test.org.fugerit.java.doc.mod.openpdf.ext;

import java.util.Properties;

import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPdfHelper;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestPhraseParent {

	@Test
	public void test001() throws Exception {
		OpenPdfHelper helper = new OpenPdfHelper();
		helper.setParams( new Properties() );
		log.info( "helper.getDefFontStyle() -> {}", helper.getDefFontStyle() );
		Assert.assertNotNull( helper.getParams() );
	}
	
}
