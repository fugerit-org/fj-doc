package org.fugerit.java.doc.freemarker.fun;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.base.xml.DocXMLUtils;

import java.util.List;

@Slf4j
public class CleanTextFun implements TemplateMethodModelEx {

	public static final String DEFAULT_NAME = "cleanText";

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		FMFunHelper.checkParameterNumber( arguments, 2 );
		String input = ((TemplateScalarModel)arguments.get( 0 )).getAsString();
		String cleanRegex = ((TemplateScalarModel)arguments.get( 1 )).getAsString();
		String output = DocXMLUtils.cleanText( input, cleanRegex );
		log.debug( "cleanText apply, cleanRegex : '{}', '{}' -> '{}'", cleanRegex, input, output );
		return new SimpleScalar(output);
	}
	
}
