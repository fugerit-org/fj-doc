package org.fugerit.java.doc.freemarker.fun;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class StringToBase64Fun implements TemplateMethodModelEx {

	public static final String DEFAULT_NAME = "stringToBase64";

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		FMFunHelper.checkParameterNumber( arguments, 1 );
		String content = ((TemplateScalarModel)arguments.get( 0 )).getAsString();
		return new SimpleScalar( Base64.getEncoder().encodeToString( content.getBytes( StandardCharsets.UTF_8 ) ) );
	}
	
}
