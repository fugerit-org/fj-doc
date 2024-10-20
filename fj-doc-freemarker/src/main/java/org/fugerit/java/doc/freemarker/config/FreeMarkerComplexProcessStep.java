package org.fugerit.java.doc.freemarker.config;

import java.util.Map;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.regex.ParamFinder;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;

public class FreeMarkerComplexProcessStep extends FreeMarkerProcessStep {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7009153804877056158L;

	public static final String ATT_TEMPLATE_PATH = "template-path";
	public static final String CHAIN_ID_PARAM = "chainId";
	
	public static final String ATT_MAP_ALL = "map-all";
	public static final String ATT_MAP_ATTS = "map-atts";

	public static String overrideTemplatePath( Properties atts, String chainId ) throws ConfigException {
		String templatePath = atts.getProperty( ATT_TEMPLATE_PATH );
		if ( StringUtils.isEmpty( templatePath ) ) {
			throw new ConfigException( "Template must be provided" );
		}
		ParamFinder finder = ParamFinder.newFinder();
		Properties params = new Properties();
		params.setProperty( CHAIN_ID_PARAM , chainId );
		templatePath = finder.substitute( templatePath , params );
		return templatePath;
	}

	@Override
	public int process(DocProcessContext context, DocProcessData data) throws Exception {
		Properties atts = this.getCustomConfig();
		// override template path
		if ( StringUtils.isEmpty( this.getParam01() ) ) {
			this.setParam01( overrideTemplatePath( atts, this.getChainId() ) );
		}
		// map attributes
		Map<String, Object> map = FreeMarkerConstants.getFreeMarkerMap( context );
		boolean mapAll = BooleanUtils.isTrue( atts.getProperty( ATT_MAP_ALL ) );
		if ( mapAll ) {
			map.putAll( context.toMap() );
		} else {
			String mapAtts = atts.getProperty( ATT_MAP_ATTS );
			if ( StringUtils.isNotEmpty( mapAtts ) ) {
				String[] keys = mapAtts.split( "," );
				for ( int k=0; k<keys.length; k++ ) {
					String key = keys[k];
					Object value = context.getAttribute( key );
					if ( value != null ) {
						map.put( key , value );
					}
				}
			}
		}
		FreemarkerApplyHelper.setupFreemarkerMap(context, map);
		return super.process(context, data);
	}
	
}
