package org.fugerit.java.doc.freemarker.fun;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.base.xml.DocXMLUtils;

import java.util.List;

@Slf4j
public class CleanXmlFun implements TemplateMethodModelEx {

	public static final String DEFAULT_NAME = "cleanXml";

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		FMFunHelper.checkFirstRequired( arguments );
		String input = ((TemplateScalarModel)arguments.get( 0 )).getAsString();
		String output = DocXMLUtils.cleanXML( input );
		log.debug( "cleanXML apply '{}' -> '{}'", input, output );
		return new SimpleScalar(output);
	}
	
}
