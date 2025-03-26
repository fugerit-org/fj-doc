package test.org.fugerit.java.doc.lib.autodoc.fun;

import java.util.ArrayList;

import org.fugerit.java.doc.lib.autodoc.fun.AnnotationAsSingleStringFun;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import freemarker.template.TemplateModelException;

class TestAnnotationAsSingleStringFun {

	@Test
	void testNoParam() {
		AnnotationAsSingleStringFun fun = new AnnotationAsSingleStringFun();
		Assertions.assertThrows( TemplateModelException.class , () -> fun.exec( new ArrayList<String>() ) );
	}
	
}
