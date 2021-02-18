package org.fugerit.java.doc.freemarker.config;

import java.util.Map;
import java.util.Properties;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.base.process.DocProcessorBasic;

import freemarker.template.TemplateMethodModelEx;

public class FreeMarkerFunctionStep extends DocProcessorBasic {

	public static final String ATT_DEFAULT = FreeMarkerFunctionStep.class.getSimpleName()+".DEFAULT";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -23134236839366841L;
	
	
	@Override
	public int process(DocProcessContext context, DocProcessData data) throws Exception {
		int res = super.process(context, data);
		Properties funConfig = this.getCustomConfig();
		if ( funConfig != null && !funConfig.isEmpty() ) {
			Map<String, Object> map = FreeMarkerConstants.getFreeMarkerMap( context );
			for ( Object key : funConfig.keySet() ) {
				String k = key.toString();
				String v = funConfig.getProperty( k );
				TemplateMethodModelEx fun = (TemplateMethodModelEx) ClassHelper.newInstance( v );
				map.put( k , fun );
			}
		}
		return res;
	}

}
