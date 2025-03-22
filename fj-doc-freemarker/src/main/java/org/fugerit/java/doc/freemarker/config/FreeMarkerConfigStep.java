package org.fugerit.java.doc.freemarker.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.base.process.DocProcessorBasic;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import org.fugerit.java.doc.freemarker.fun.*;

public class FreeMarkerConfigStep extends DocProcessorBasic {

	public static final String ATT_DEFAULT = "FreeMarkerConfigStep.DEFAULT";
	
	public static final String ATT_FREEMARKER_CONFIG_KEY_VERSION = "version";
	public static final String ATT_FREEMARKER_CONFIG_KEY_VERSION_2_3_34 = "2.3.34";
	public static final String ATT_FREEMARKER_CONFIG_KEY_VERSION_2_3_33 = "2.3.33";
	public static final String ATT_FREEMARKER_CONFIG_KEY_VERSION_2_3_32 = "2.3.32";
	public static final String ATT_FREEMARKER_CONFIG_KEY_VERSION_2_3_31 = "2.3.31";
	public static final String ATT_FREEMARKER_CONFIG_KEY_VERSION_2_3_30 = "2.3.30";
	public static final String ATT_FREEMARKER_CONFIG_KEY_VERSION_2_3_29 = "2.3.29";
	public static final String ATT_FREEMARKER_CONFIG_KEY_VERSION_DEFAULT = ATT_FREEMARKER_CONFIG_KEY_VERSION_2_3_32;
	public static final String ATT_FREEMARKER_CONFIG_KEY_VERSION_LATEST = ATT_FREEMARKER_CONFIG_KEY_VERSION_2_3_33;
	
	public static final String ATT_FREEMARKER_CONFIG_KEY_MODE = "mode";
	public static final String ATT_FREEMARKER_CONFIG_KEY_MODE_CLASS = "class";
	public static final String ATT_FREEMARKER_CONFIG_KEY_MODE_FOLDER = "folder";
	
	public static final String ATT_FREEMARKER_CONFIG_KEY_PATH = "path";
	public static final String ATT_FREEMARKER_CONFIG_KEY_CLASS = "class";
	
	public static final String ATT_FREEMARKER_CONFIG_KEY_ENCODING = "encoding";
	public static final String ATT_FREEMARKER_CONFIG_KEY_ENCODING_UTF8 = "UTF-8";
	public static final String ATT_FREEMARKER_CONFIG_KEY_ENCODING_DEFAULT = ATT_FREEMARKER_CONFIG_KEY_ENCODING_UTF8;
	
	public static final String ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER = "exception-handler";
	public static final String ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_RETHROW = "RETHROW_HANDLER";
	public static final String ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_IGNORE = "IGNORE_HANDLER";
	public static final String ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_DEBUG = "DEBUG_HANDLER";
	public static final String ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_HTML_DEBUG = "HTML_DEBUG_HANDLER";
	public static final String ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_DEFAULT = ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_RETHROW;

	public static final String ATT_FREEMARKER_CONFIG_KEY_LOG_EXCEPTION = "log-exception";
	public static final String ATT_FREEMARKER_CONFIG_KEY_LOG_EXCEPTION_DEFAULT = "false";
	
	public static final String ATT_FREEMARKER_CONFIG_KEY_WRAP_UNCHECKED_EXCEPTION = "wrap-unchecked-exceptions";
	public static final String ATT_FREEMARKER_CONFIG_KEY_WRAP_UNCHECKED_EXCEPTION_DEFAULT = "true";
	
	public static final String ATT_FREEMARKER_CONFIG_KEY_FALLBACK_ON_NULL_LOOP_VARIABLE = "fallback-on-null-loop-variable";
	public static final String ATT_FREEMARKER_CONFIG_KEY_FALLBACK_ON_NULL_LOOP_VARIABLE_DEFAULT = "false";

	public static final String ATT_FREEMARKER_CONFIG_KEY_LOAD_BUNDLED_FUN = "load-bundled-functions";

	/**
	 * 
	 */
	private static final long serialVersionUID = -2313658506839366841L;

	private static Map<String, Configuration> mapConfig = new HashMap<>();
	
	private static final HashMap<String, TemplateExceptionHandler> DEF_TEH = new HashMap<>();
	static {
		DEF_TEH.put( ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_RETHROW , TemplateExceptionHandler.RETHROW_HANDLER );
		DEF_TEH.put( ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_IGNORE , TemplateExceptionHandler.IGNORE_HANDLER );
		DEF_TEH.put( ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_DEBUG, TemplateExceptionHandler.DEBUG_HANDLER );
		DEF_TEH.put( ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_HTML_DEBUG, TemplateExceptionHandler.HTML_DEBUG_HANDLER );
	}
	
	private static void configure( Properties config, Configuration freeMarkerConfig, String path ) throws ClassNotFoundException, NoSuchMethodException, ConfigException, IOException {
		String mode = config.getProperty( ATT_FREEMARKER_CONFIG_KEY_MODE );
		if ( ATT_FREEMARKER_CONFIG_KEY_MODE_CLASS.equalsIgnoreCase( mode ) ) {
			String configClass = config.getProperty( ATT_FREEMARKER_CONFIG_KEY_CLASS );
			Class<?> c = null;
			if ( configClass == null ) {
				c = FreeMarkerConfigStep.class;
			} else {
				c = ClassHelper.newInstance( configClass ).getClass();
			}
			freeMarkerConfig.setClassForTemplateLoading( c , path );
		} else if ( ATT_FREEMARKER_CONFIG_KEY_MODE_FOLDER.equalsIgnoreCase( mode ) ) {
			freeMarkerConfig.setDirectoryForTemplateLoading( new File( path ) );
		} else {
			throw new ConfigException( "provide valid value for parameter "+ATT_FREEMARKER_CONFIG_KEY_MODE+"(used:"+mode+")" );
		}
		// Set the preferred charset template files are stored in. UTF-8 is
		// a good choice in most applications:
		freeMarkerConfig.setDefaultEncoding( config.getProperty( ATT_FREEMARKER_CONFIG_KEY_ENCODING, ATT_FREEMARKER_CONFIG_KEY_ENCODING_DEFAULT ) );
		// Sets how errors will appear.
		// During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
		String exceptionHandler = config.getProperty( ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER, ATT_FREEMARKER_CONFIG_KEY_EXCEPTION_HANDLER_DEFAULT );
		TemplateExceptionHandler templateExceptionHandler = DEF_TEH.get( exceptionHandler );
		freeMarkerConfig.setTemplateExceptionHandler( templateExceptionHandler );
		// Don't log exceptions inside FreeMarker that it will thrown at you anyway:
		freeMarkerConfig.setLogTemplateExceptions( BooleanUtils.isTrue( config.getProperty( ATT_FREEMARKER_CONFIG_KEY_LOG_EXCEPTION, ATT_FREEMARKER_CONFIG_KEY_LOG_EXCEPTION_DEFAULT ) ) );
		// Wrap unchecked exceptions thrown during template processing into TemplateException-s:
		freeMarkerConfig.setWrapUncheckedExceptions( BooleanUtils.isTrue( config.getProperty( ATT_FREEMARKER_CONFIG_KEY_WRAP_UNCHECKED_EXCEPTION, ATT_FREEMARKER_CONFIG_KEY_WRAP_UNCHECKED_EXCEPTION_DEFAULT ) ) );
		// Do not fall back to higher scopes when reading a null loop variable:
		freeMarkerConfig.setFallbackOnNullLoopVariable( BooleanUtils.isTrue( config.getProperty( ATT_FREEMARKER_CONFIG_KEY_FALLBACK_ON_NULL_LOOP_VARIABLE, ATT_FREEMARKER_CONFIG_KEY_FALLBACK_ON_NULL_LOOP_VARIABLE_DEFAULT ) ) );
	}
	
	protected static Configuration getConfig( String k, Properties config ) {
		return SafeFunction.get( () -> {
			String key = k;
			if ( key == null ) {
				key = ATT_DEFAULT;
			}
			Configuration freeMarkerConfigInstance = mapConfig.get( key );
			if ( freeMarkerConfigInstance == null ) {
				logger.info( "{} INIT - config = {}", FreeMarkerConfigStep.class.getSimpleName(), config );
				// free marker version configuration
				String versionString = config.getProperty( ATT_FREEMARKER_CONFIG_KEY_VERSION , ATT_FREEMARKER_CONFIG_KEY_VERSION_DEFAULT);
				Version version = new Version( versionString );
				Configuration freeMarkerConfig = new Configuration( version );
				// free marker template mode
				String path = config.getProperty( ATT_FREEMARKER_CONFIG_KEY_PATH );
				if ( path == null ) {
					throw new ConfigException( "provide valid value for parameter "+ATT_FREEMARKER_CONFIG_KEY_PATH );
				}
				// free marker template mode
				configure(config, freeMarkerConfig, path);
				freeMarkerConfigInstance = freeMarkerConfig;
				mapConfig.put( key , freeMarkerConfig );
			}
			return freeMarkerConfigInstance;
		} );
	}
	
	@Override
	public int process(DocProcessContext context, DocProcessData data) throws Exception {
		int res = super.process(context, data);
		Configuration cfg = getConfig( this.getParam01(), this.getCustomConfig() );
		context.setAttribute( FreeMarkerConstants.ATT_FREEMARKER_CONFIG , cfg );
		// bundle default function
		boolean loadBundledFunction = BooleanUtils.isTrue( this.getCustomConfig().getProperty( ATT_FREEMARKER_CONFIG_KEY_LOAD_BUNDLED_FUN ) );
		if ( loadBundledFunction ) {
			Map<String, Object> map = FreeMarkerConstants.getFreeMarkerMap( context );
			map.put(ImageBase64CLFun.DEFAULT_NAME, new ImageBase64CLFun());
			map.put(TextWrapFun.DEFAULT_NAME, new TextWrapFun());
			map.put(SimpleMessageFun.DEFAULT_NAME, new SimpleMessageFun());
			map.put(SimpleSumLongFun.DEFAULT_NAME, new SimpleSumLongFun());
			map.put(CleanXmlFun.DEFAULT_NAME, new CleanXmlFun());
			map.put(CleanTextFun.DEFAULT_NAME, new CleanTextFun());
			map.put(FormatLocalDateTimeFun.DEFAULT_NAME, new FormatLocalDateTimeFun());
			map.put(Base64ToStringFun.DEFAULT_NAME, new Base64ToStringFun());
		}
		return res;
	}

}
