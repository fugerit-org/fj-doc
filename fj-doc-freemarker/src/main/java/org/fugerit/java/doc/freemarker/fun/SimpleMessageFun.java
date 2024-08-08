package org.fugerit.java.doc.freemarker.fun;

import java.text.MessageFormat;
import java.util.List;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

public class SimpleMessageFun implements TemplateMethodModelEx {

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		FMFunHelper.checkFirstRequred( arguments );
		String key = ((TemplateScalarModel)arguments.get( 0 )).getAsString();
		Object[] args = new Object[ arguments.size()-1 ];
		for ( int k=1; k<arguments.size(); k++ ) {
			args[k-1] = ((TemplateScalarModel)arguments.get( k )).getAsString();
		}
		return new SimpleScalar( MessageFormat.format( key, args ) );
	}
	
}
