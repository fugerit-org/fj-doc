package org.fugerit.java.doc.freemarker.fun;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

import java.util.Base64;
import java.util.List;

public class Base64ToStringFun implements TemplateMethodModelEx {

	public static final String DEFAULT_NAME = "base64ToString";

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		FMFunHelper.checkParameterNumber( arguments, 1 );
		String base64 = ((TemplateScalarModel)arguments.get( 0 )).getAsString();
		return new SimpleScalar( new String( Base64.getDecoder().decode( base64 ) ) );
	}
	
}
