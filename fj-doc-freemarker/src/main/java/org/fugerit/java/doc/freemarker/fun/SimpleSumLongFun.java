package org.fugerit.java.doc.freemarker.fun;

import java.math.BigDecimal;
import java.util.List;

import freemarker.template.SimpleNumber;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class SimpleSumLongFun implements TemplateMethodModelEx {

	public static final String DEFAULT_NAME = "sumLong";

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		BigDecimal res = new BigDecimal( 0 );
		for ( Object current : arguments ) {
			BigDecimal bd = new BigDecimal( current.toString() );
			res = res.add( bd );
		}
		return new SimpleNumber( res.longValue() );
	}
	
}
