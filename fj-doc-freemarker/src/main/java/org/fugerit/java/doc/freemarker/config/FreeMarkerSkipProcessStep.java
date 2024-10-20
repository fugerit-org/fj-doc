package org.fugerit.java.doc.freemarker.config;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class FreeMarkerSkipProcessStep extends FreeMarkerProcessStep {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7009153804877056158L;

	public static final String ATT_TEMPLATE_PATH = "template-path";
	public static final String CHAIN_ID_PARAM = "chainId";
	
	public static final String ATT_MAP_ALL = "map-all";
	public static final String ATT_MAP_ATTS = "map-atts";
	
	@Override
	public int process(DocProcessContext context, DocProcessData data) throws Exception {
		Properties atts = this.getCustomConfig();
		// override template path
		if ( StringUtils.isEmpty( this.getParam01() ) ) {
			this.setParam01( FreeMarkerComplexProcessStep.overrideTemplatePath( atts, this.getChainId() ) );
		}
		Configuration cfg = (Configuration) context.getAttribute( FreeMarkerConstants.ATT_FREEMARKER_CONFIG );
		TemplateLoader templateLoader = cfg.getTemplateLoader();
		String path = this.getParam01();
		Object templateSource = templateLoader.findTemplateSource( path );
		try (Reader reader  = templateLoader.getReader( templateSource, StandardCharsets.UTF_8.name() ) ) {
			data.setCurrentXmlData(StreamIO.readString(reader));
		}
		return CONTINUE;
	}
	
}
