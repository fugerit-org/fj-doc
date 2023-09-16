package org.fugerit.java.doc.lib.simpletableimport;

import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.util.ObjectUtils;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonConvertUtils {

	protected static final Properties DEFAULT_PARAMS = new Properties();
	
	public static final String PARAM_KEY_RETHROW_EXCEPTION = "rethrow-exception";
	public static final String PARAM_VALUE_RETHROW_EXCEPTION_DEFAULT = BooleanUtils.BOOLEAN_1;
	
	private CommonConvertUtils() {}
	
	protected static SimpleTable handleConvertException( final SimpleTable table, Exception e, Properties props ) throws ConfigException {
		SimpleTable result;
		Properties params = ObjectUtils.objectWithDefault( props, DEFAULT_PARAMS );
		if ( BooleanUtils.isTrue( params.getProperty( PARAM_KEY_RETHROW_EXCEPTION, PARAM_VALUE_RETHROW_EXCEPTION_DEFAULT ) ) ) {
			log.debug( "table : {}", table );
			throw new ConfigException( e );	
		} else {
			result = null;
			log.warn( "Returning null table on exception : "+e, e );
		}
		return result;
	}
	
}
