package org.fugerit.java.doc.freemarker.config;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.base.process.DocProcessorBasic;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerProcessStep extends DocProcessorBasic {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2313658506839366841L;

	@Override
	public int process(DocProcessContext context, DocProcessData data) throws Exception {
		int res = super.process(context, data);
		Configuration cfg = (Configuration) context.getAttribute( FreeMarkerConstants.ATT_FREEMARKER_CONFIG );
		Template template = cfg.getTemplate( this.getParam01() );
		@SuppressWarnings("unchecked")
		Map<String, Object> map = FreeMarkerConstants.getFreeMarkerMap( context );
		FreemarkerApplyHelper.setupFreemarkerMap(cfg, map);
		try ( Writer out = new StringWriter() ) {
			template.process( map, out);
			data.setCurrentXmlData( out.toString() );
		}
		return res;
	}

}
