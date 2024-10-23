package org.fugerit.java.doc.freemarker.config;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.parser.DocEvalWithDataModel;
import org.fugerit.java.doc.base.parser.DocParser;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.base.process.DocProcessorBasic;

import java.io.InputStreamReader;

import java.util.Map;

/**
 * When using this step, freemarker apply template will be skipped. It uses kotlin script, kts-path must be set, dataModel map can still be used and will be bound to the script
 *
 * @since 8.10.0
 */
@Slf4j
public class FreeMarkerKotlinStep extends DocProcessorBasic {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2313658506839366841L;

	public static final String ATT_KTS_PATH = "kts-path";

	public static DocEvalWithDataModel convertOrExceptiopn( Object obj ) {
		if ( obj instanceof DocEvalWithDataModel ) {
			return (DocEvalWithDataModel) obj;
		} else {
			throw new ConfigRuntimeException(String.format( "Connect cast to %s : %s", DocEvalWithDataModel.class, obj ) );
		}
	}

	private int sourceType;

	public FreeMarkerKotlinStep(int sourceType) {
		this.sourceType = sourceType;
	}

	public FreeMarkerKotlinStep() {
		this(DocFacadeSource.SOURCE_TYPE_KOTLIN);
	}

	@Override
	public int process(DocProcessContext context, DocProcessData data) throws Exception {
		int res = super.process(context, data);
		Map<String, Object> dataModel = FreeMarkerConstants.getFreeMarkerMap( context );
		DocParser docParser = DocFacadeSource.getInstance().getParserForSource( this.sourceType );
		if ( docParser == null ) {
			throw new ConfigRuntimeException( String.format( "Unsupported source type : %s", this.sourceType ) );
		}
		log.info( "this.getCustomConfig() {}", this.getCustomConfig() );
		// kts-path (template-path can be used as a replacement)
		String ktsPath = this.getCustomConfig().getProperty(ATT_KTS_PATH);
		if ( ktsPath == null ) {
			ktsPath = this.getCustomConfig().getProperty(FreeMarkerComplexProcessStep.ATT_TEMPLATE_PATH);
		}
		if (StringUtils.isEmpty( ktsPath )) {
			throw new ConfigRuntimeException( String.format( "Required property '%s' and '%s' not set", ATT_KTS_PATH, FreeMarkerComplexProcessStep.ATT_TEMPLATE_PATH ) );
		} else {
			log.debug( "{} : {}", ATT_KTS_PATH, ktsPath );
			try (InputStreamReader reader = new InputStreamReader(ClassHelper.loadFromDefaultClassLoader( ktsPath ))) {
				DocEvalWithDataModel eval = convertOrExceptiopn( docParser );
				String xml = eval.evalWithDataModel( reader, dataModel );
				data.setCurrentXmlData( xml );
			}
		}
		return res;
	}

}
