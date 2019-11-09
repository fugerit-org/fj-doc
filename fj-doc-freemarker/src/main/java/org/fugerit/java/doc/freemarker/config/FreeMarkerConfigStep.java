package org.fugerit.java.doc.freemarker.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.base.process.DocProcessorBasic;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class FreeMarkerConfigStep extends DocProcessorBasic {

	public static final String ATT_DEFAULT = "FreeMarkerConfigStep.DEFAULT";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2313658506839366841L;

	private static Map<String, Configuration> mapConfig = new HashMap<String, Configuration>();
	
	private static HashMap<String, TemplateExceptionHandler> DEF_TEH = new HashMap<String, TemplateExceptionHandler>();
	static {
		DEF_TEH.put( FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_RETHROW , TemplateExceptionHandler.RETHROW_HANDLER );
		DEF_TEH.put( FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_IGNORE , TemplateExceptionHandler.IGNORE_HANDLER );
		DEF_TEH.put( FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_DEBUG, TemplateExceptionHandler.DEBUG_HANDLER );
		DEF_TEH.put( FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_HTML_DEBUG, TemplateExceptionHandler.HTML_DEBUG_HANDLER );
	}
	
	protected static Configuration getConfig( String key, Properties config ) throws Exception {
		if ( key == null ) {
			key = ATT_DEFAULT;
		}
		Configuration freeMarkerConfigInstance = mapConfig.get( key );
		if ( freeMarkerConfigInstance == null ) {
			logger.info( FreeMarkerConfigStep.class.getSimpleName()+" INIT - config = "+config );
			// free marker version configuration
			String versionString = config.getProperty( FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_VERSION , FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_VERSION_DEFAULT);
			Version version = new Version( versionString );
			Configuration freeMarkerConfig = new Configuration( version );
			// free marker template mode
			String path = config.getProperty( FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_PATH );
			if ( path == null ) {
				throw new ConfigException( "provide valid value for parameter "+FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_PATH );
			}
			// free marker template mode
			String mode = config.getProperty( FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_MODE );
			if ( FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_MODE_CLASS.equalsIgnoreCase( mode ) ) {
				String configClass = config.getProperty( FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_CLASS );
				Class<?> c = null;
				if ( configClass == null ) {
					c = FreeMarkerConstants.class;
				} else {
					c = ClassHelper.newInstance( configClass ).getClass();
				}
				freeMarkerConfig.setClassForTemplateLoading( c , path );
			} else if ( FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_MODE_FOLDER.equalsIgnoreCase( mode ) ) {
				freeMarkerConfig.setDirectoryForTemplateLoading( new File( path ) );
			} else {
				throw new ConfigException( "provide valid value for parameter "+FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_MODE+"(used:"+mode+")" );
			}
			//cfg.setDirectoryForTemplateLoading( new File( "src/test/resources/free_marker" ) );
			// Set the preferred charset template files are stored in. UTF-8 is
			// a good choice in most applications:
			freeMarkerConfig.setDefaultEncoding( config.getProperty( FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_ENCODING, FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_ENCODING_DEFAULT ) );
			// Sets how errors will appear.
			// During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
			String exceptionHandler = config.getProperty( FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER, FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_DEFAULT );
			TemplateExceptionHandler templateExceptionHandler = DEF_TEH.get( exceptionHandler );
			freeMarkerConfig.setTemplateExceptionHandler( templateExceptionHandler );
			// Don't log exceptions inside FreeMarker that it will thrown at you anyway:
			freeMarkerConfig.setLogTemplateExceptions( BooleanUtils.isTrue( config.getProperty( FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_LOG_EXCEPTION, FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_LOG_EXCEPTION_DEFAULT ) ) );
			// Wrap unchecked exceptions thrown during template processing into TemplateException-s:
			freeMarkerConfig.setWrapUncheckedExceptions( BooleanUtils.isTrue( config.getProperty( FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_WRAP_UNCHECKED_EXCEPTION, FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_WRAP_UNCHECKED_EXCEPTION_DEFAULT ) ) );
			// Do not fall back to higher scopes when reading a null loop variable:
			freeMarkerConfig.setFallbackOnNullLoopVariable( BooleanUtils.isTrue( config.getProperty( FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_FALLBACK_ON_NULL_LOOP_VARIABLE, FreeMarkerConstants.ATT_FREEMARKER_CONFIG_KEY_FALLBACK_ON_NULL_LOOP_VARIABLE_DEFAULT ) ) );
			freeMarkerConfigInstance = freeMarkerConfig;
			mapConfig.put( key , freeMarkerConfig );
		}
		return freeMarkerConfigInstance;
	}
	
	@Override
	public int process(DocProcessContext context, DocProcessData data) throws Exception {
		int res = super.process(context, data);
		Configuration cfg = getConfig( this.getParam01(), this.getCustomConfig() );
		context.setAttribute( FreeMarkerConstants.ATT_FREEMARKER_CONFIG , cfg );
		return res;
	}



}
