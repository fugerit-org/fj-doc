package org.fugerit.java.doc.freemarker.fun;

import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

public class ImageBase64CLFun implements TemplateMethodModelEx {

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		if ( arguments.isEmpty() ) {
			throw new TemplateModelException( "Class loader image path param is needed" );
		}
		String path = ((TemplateScalarModel)arguments.get( 0 )).getAsString();
		String base64 = null;
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( path ) ) {
			base64 = Base64.getEncoder().encodeToString( StreamIO.readBytes( is ) );
		} catch (Exception e) {
			throw new TemplateModelException( e );
		}
		return new SimpleScalar( base64 );
	}
	
}
