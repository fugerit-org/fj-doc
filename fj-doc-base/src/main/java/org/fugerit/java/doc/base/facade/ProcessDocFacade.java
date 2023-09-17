package org.fugerit.java.doc.base.facade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.core.util.filterchain.MiniFilterChain;
import org.fugerit.java.core.xml.sax.SAXParseResult;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.process.DocProcessConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.base.xml.DocValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessDocFacade {

	private DocHandlerFacade docHandlerFacade;
	
	private DocProcessConfig docProcessConfig;

	public ProcessDocFacade(DocHandlerFacade docHandlerFacade, DocProcessConfig docProcessConfig) {
		super();
		this.docHandlerFacade = docHandlerFacade;
		this.docProcessConfig = docProcessConfig;
	}
	
	public DocHandlerFacade getDocHandlerFacade() {
		return docHandlerFacade;
	}

	public DocProcessConfig getDocProcessConfig() {
		return docProcessConfig;
	}

	/**
	 * <p>Creates and configure an instance of ProcessDocFacade.</p>
	 * 
	 * <p>NOTE: starting from version 8.4.X java.lang.Exception removed in favor of org.fugerit.java.core.cfg.ConfigRuntimeException.</p>
	 * 
	 * @see <a href="https://fuzzymemory.fugerit.org/src/docs/sonar_cloud/java-S112.html">Define and throw a dedicated exception instead of using a generic one.</a>
	 * 
	 * @param pathConfigDocProcessConfig		path to config doc process config xml
	 * @param pathConfigDocHandlerFactory		path to doc handler factory xml
	 * @param useCatalog						factory catalog to use (null for default)
	 * @return									the configured ProcessDocFacade instance
	 */
	public static ProcessDocFacade newFacade( String pathConfigDocProcessConfig, String pathConfigDocHandlerFactory, String useCatalog ) {
		return SafeFunction.get( () -> {
			ProcessDocFacade docProcessDocFacade = null;
			try ( InputStream configDocProcessConfig = StreamHelper.resolveStream( pathConfigDocProcessConfig );
					InputStream configDocHandlerFaactory = StreamHelper.resolveStream( pathConfigDocHandlerFactory ) ) {
				docProcessDocFacade = newFacade(configDocProcessConfig, configDocHandlerFaactory, useCatalog);
			}
			return docProcessDocFacade;			
		} );
	}
	
	/**
	 * <p>Creates and configure an instance of ProcessDocFacade.</p>
	 * 
	 * <p>NOTE: starting from version 8.4.X java.lang.Exception removed in favor of org.fugerit.java.core.cfg.ConfigRuntimeException.</p>
	 * 
	 * @see <a href="https://fuzzymemory.fugerit.org/src/docs/sonar_cloud/java-S112.html">Define and throw a dedicated exception instead of using a generic one.</a>
	 * 
	 * @param configDocProcessConfig		stream for doc process config xml
	 * @param configDocHandlerFactory		stream for doc factory xml
	 * @param useCatalog					factory catalog to use (null for default)
	 * @return								the configured ProcessDocFacade instance
	 */
	public static ProcessDocFacade newFacade( InputStream configDocProcessConfig , InputStream configDocHandlerFactory, String useCatalog ) {
		return SafeFunction.get( () -> {
			DocHandlerFactory docHandlerFatory = DocHandlerFactory.newInstance( configDocHandlerFactory );
			DocHandlerFacade docHandlerFacade = docHandlerFatory.get( useCatalog );
			DocProcessConfig docProcessConfig = DocProcessConfig.loadConfig( configDocProcessConfig );
			return new ProcessDocFacade(docHandlerFacade, docProcessConfig);
		} );
	}
	
	public void process( String chainId, String type, DocProcessContext context, File dest ) throws Exception {
		try ( FileOutputStream fos = new FileOutputStream( dest ) ) {
			process(chainId, type, context, fos);
		}
	}
	
	public void process( String chainId, String type, DocProcessContext context, OutputStream os ) throws Exception {
		process(chainId, type, context, os, false);
	}
	
	public SAXParseResult process( String chainId, String type, DocProcessContext context, OutputStream os, boolean validate ) throws Exception {
		SAXParseResult result = null;
		MiniFilterChain chain = this.getDocProcessConfig().getChain( chainId );
		DocProcessData data = new DocProcessData();
		chain.apply(context, data);
		if ( validate ) {
			result = DocValidator.validate( data.getCurrentXmlReader() );
			if ( !result.isPartialSuccess() ) {
				DocValidator.logResult(result, log );
			}
		}
		DocBase docBase = null;
		try ( Reader reader = data.getCurrentXmlReader() ) {
			docBase = DocFacade.parse( reader );
		}
		DocInput input = DocInput.newInput( type, docBase, data.getCurrentXmlReader() );
		DocOutput output = DocOutput.newOutput( os );
		this.getDocHandlerFacade().handle(input, output);
		return result;
	}
	
}
