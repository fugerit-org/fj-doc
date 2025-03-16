package test.org.fugerit.java.doc.base.xml;

import java.awt.Color;

import org.fugerit.java.doc.base.xml.DocModelUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestDocModelUtils {
	
	@Test
	void test1() {
		Assertions.assertEquals( Color.WHITE , DocModelUtils.parseHtmlColor("#FFFFFF") );
	}
	
}
