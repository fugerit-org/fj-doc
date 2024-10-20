package org.fugerit.java.doc.freemarker.config;

import java.util.Map;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

public class FreemarkerApplyHelper {

	private FreemarkerApplyHelper() {}
	
	public static void setupFreemarkerMap( DocProcessContext context, Map<String, Object> map ) throws TemplateModelException {
		setupFreemarkerMap( (Configuration) context.getAttribute( FreeMarkerConstants.ATT_FREEMARKER_CONFIG ), map);
	}
	
	public static void setupFreemarkerMap( Configuration cfg, Map<String, Object> map ) throws TemplateModelException {
		addStaticAccess(cfg, map, DocConfig.class.getSimpleName(), DocConfig.class);
	}
	
	public static void addStaticAccess( Configuration cfg, Map<String, Object> map, String key, Class<?> c ) throws TemplateModelException {
		BeansWrapper wrapper = new BeansWrapperBuilder( cfg.getIncompatibleImprovements() ).build();
		TemplateHashModel staticModels = wrapper.getStaticModels();
		TemplateHashModel docConfigStatic = (TemplateHashModel) staticModels.get( c.getName() );
		map.put( key , docConfigStatic);
	}
	
}
