package test.org.fugerit.java.doc.lib.autodoc.fun;

import java.util.ArrayList;

import org.fugerit.java.doc.lib.autodoc.fun.AnnotationAsSingleStringFun;
import org.junit.Assert;
import org.junit.Test;

import freemarker.template.TemplateModelException;

public class TestAnnotationAsSingleStringFun {

	@Test
	public void testNoParam() {
		AnnotationAsSingleStringFun fun = new AnnotationAsSingleStringFun();
		Assert.assertThrows( TemplateModelException.class , () -> fun.exec( new ArrayList<String>() ) );
	}
	
}
