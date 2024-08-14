package org.fugerit.java.doc.freemarker.fun;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import org.fugerit.java.doc.base.helper.TextWrapHelper;

import java.util.List;

public class TextWrapFun implements TemplateMethodModelEx {

	public static final String DEFAULT_NAME = "textWrap";

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		FMFunHelper.checkFirstRequred( arguments );
		String input = ((TemplateScalarModel)arguments.get( 0 )).getAsString();
		return new SimpleScalar(TextWrapHelper.padZeroWithSpace( input ));
	}
	
}
