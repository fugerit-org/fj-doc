package org.fugerit.java.doc.freemarker.fun;

import freemarker.ext.beans.GenericObjectModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

public class FormatLocalDateTimeFun implements TemplateMethodModelEx {

	public static final String DEFAULT_NAME = "formatDateTime";

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		FMFunHelper.checkParameterNumber( arguments, 2 );
		GenericObjectModel obj = (GenericObjectModel)arguments.get( 0 );
		String format = ((TemplateScalarModel)arguments.get( 1 )).getAsString();
		TemporalAccessor ta = (TemporalAccessor)obj.getWrappedObject();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return new SimpleScalar( formatter.format(ta) );
	}
	
}
