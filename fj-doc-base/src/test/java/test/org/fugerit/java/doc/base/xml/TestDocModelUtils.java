package test.org.fugerit.java.doc.base.xml;

import java.awt.Color;

import org.fugerit.java.doc.base.xml.DocModelUtils;
import org.junit.Assert;
import org.junit.Test;

public class TestDocModelUtils {
	
	@Test
	public void test1() {
		Assert.assertEquals( Color.WHITE , DocModelUtils.parseHtmlColor("#FFFFFF") );
	}
	
}
