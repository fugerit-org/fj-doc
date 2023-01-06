package org.fugerit.java.doc.lib.autodoc.fun;

import java.util.List;

import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocUtils;
import org.xmlet.xsdparser.xsdelements.XsdAnnotation;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

public class AnnotationAsSingleStringFun implements TemplateMethodModelEx {

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		if ( arguments.isEmpty() ) {
			throw new TemplateModelException( "At least one parameter is needed" );
		}
		Object param = arguments.get( 0 );
		XsdAnnotation xsdAnnotation = (XsdAnnotation) DeepUnwrap.unwrap( (TemplateModel)param );
		return new SimpleScalar( AutodocUtils.annotationAsSingleStringHelper( xsdAnnotation ) );
	}
	
}
