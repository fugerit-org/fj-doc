package org.fugerit.java.doc.freemarker.process;

import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.fugerit.java.core.cfg.xml.IdConfigType;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.util.collection.KeyString;
import org.fugerit.java.core.util.regex.ParamFinder;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.base.process.DocProcessorBasic;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConstants;
import org.fugerit.java.doc.freemarker.config.FreemarkerApplyHelper;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class ConfigInitModel implements IdConfigType, KeyString, Serializable {

	public static final String VERSION_2_3_31 = "2.3.31";
	public static final String DEFAULT_VERSION = VERSION_2_3_31;

	public static final String DEFAULT_CLASS_NAME = FreemarkerDocProcessConfigFacade.class.getName();
	
	public static final String DEFAULT_MODE = "class";
	
	public static final String DEFAULT_EXCEPTION_HANDLER = "RETHROW_HANDLER";
	
	public static final String DEFAULT_LOG_EXCEPTION = BooleanUtils.BOOLEAN_FALSE;
	
	public static final String DEFAULT_WRAP_UNCHECKED_EXCEPTION = BooleanUtils.BOOLEAN_TRUE;
	
	public static final String DEFAULT_FALL_BACK_ON_NULL_LOOP_VARIABLE = BooleanUtils.BOOLEAN_FALSE;
	
	private static final long serialVersionUID = -59587465058736934L;

	private String id;
	
	private String version = DEFAULT_VERSION;
	
	private String path;
	
	private String mode = DEFAULT_MODE;
	
	private String className = DEFAULT_CLASS_NAME;
	
	private String exceptionHandler = DEFAULT_EXCEPTION_HANDLER;
	
	private String logException = DEFAULT_LOG_EXCEPTION;
	
	private String wrapUncheckedExceptions = DEFAULT_WRAP_UNCHECKED_EXCEPTION;
	
	private String fallbackOnNullLoopVariable = DEFAULT_FALL_BACK_ON_NULL_LOOP_VARIABLE;

	private Configuration freemarkerConfiguration;	
	
	private Map<String, Object> generalContext = new HashMap<>();

	public static final String CHAIN_ID_PARAM = "chainId";
	
	protected void process( DocChainModel model, DocProcessContext context, DocProcessData data ) throws Exception {
		// override template path
		String templatePath = model.getTemplatePath();
		ParamFinder finder = ParamFinder.newFinder();
		Properties params = new Properties();
		params.setProperty( CHAIN_ID_PARAM , model.getId() );
		templatePath = finder.substitute( templatePath , params );
		// map attributes
		Map<String, Object> map = FreeMarkerConstants.getFreeMarkerMap( context );
		if ( map == null ) {
			map = new HashMap<>();
		}
		map.putAll( this.getGeneralContext() );
		for ( ChainStepModel step : model.getChainStepList() ) {
			DocProcessorBasic docStep = (DocProcessorBasic) ClassHelper.newInstance( step.getStepType() );
			int res = docStep.apply(context, data);
			log.debug( "docStep {} -> {}", docStep, res );
		}
		log.debug( "mapsAtt mode -> {} -> {}", model.getId(), model.getMapAtts() );
		if ( DocChainModel.MAP_ATTS_ENUM.equalsIgnoreCase( model.getMapAtts() ) ) {
			Enumeration<Object> ea = model.getMapAttsEnum().keys();
			while ( ea.hasMoreElements() ) {
				String key = ea.nextElement().toString();
				String value = model.getMapAttsEnum().getProperty( key );
				map.put( value , context.getAttribute( key ) );
				log.info( "map att enum -> {} -> {}", key, value );
			}
		} else {
			map.putAll( context.toMap() );
		}
		Template template = this.getFreemarkerConfiguration().getTemplate( templatePath );
		FreemarkerApplyHelper.setupFreemarkerMap( this.freemarkerConfiguration, map);
		Writer out = new StringWriter();
		template.process( map, out);
		data.setCurrentXmlData( out.toString() );
	}
	
	@Override
	public String getKey() {
		return this.getId();
	}

	@Override
	public String toString() {
		return "ConfigInitModel [id=" + id + ", version=" + version + ", path=" + path + ", mode=" + mode
				+ ", className=" + className + ", exceptionHandler=" + exceptionHandler + ", logException="
				+ logException + ", wrapUncheckedExceptions=" + wrapUncheckedExceptions
				+ ", fallbackOnNullLoopVariable=" + fallbackOnNullLoopVariable + ", freemarkerConfiguration="
				+ freemarkerConfiguration + "]";
	}
	
}
