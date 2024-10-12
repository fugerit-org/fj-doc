package org.fugerit.java.doc.freemarker.process;

import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Setter;
import org.fugerit.java.core.cfg.xml.ListMapConfig;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.util.filterchain.MiniFilterChain;
import org.fugerit.java.core.util.filterchain.MiniFilterMap;
import org.fugerit.java.core.xml.sax.SAXParseResult;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.facade.DocHandlerFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.process.DocProcessConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.base.xml.DocValidator;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FreemarkerDocProcessConfig implements Serializable, MiniFilterMap {

	private static final long serialVersionUID = -6761081877582850120L;

	public static final boolean DEFAULT_VALIDATING = false;

	public static final boolean DEFAULT_FAIL_ON_VALIDATE = false;

	public static final boolean DEFAULT_CLEAN_SOURCE = false;

	@Getter
	private ListMapConfig<DocChainModel> docChainList;
	
	@Getter
	private DocHandlerFacade facade;
	
	private DocProcessConfig docProcessConfig;

	@Getter @Setter( AccessLevel.PACKAGE )
	private boolean validating;

	@Getter @Setter( AccessLevel.PACKAGE )
	private boolean failOnValidate;

	@Getter @Setter( AccessLevel.PACKAGE )
	private boolean cleanSource;

	private transient DocInputProcess docInputProcess;

	protected FreemarkerDocProcessConfig() {
		super();
		this.docChainList = new ListMapConfig<>();
		this.facade = new DocHandlerFacade();
		this.docProcessConfig = new DocProcessConfig();
		this.validating = DEFAULT_VALIDATING;
		this.failOnValidate = DEFAULT_FAIL_ON_VALIDATE;
		this.cleanSource = DEFAULT_CLEAN_SOURCE;
		this.initDocInputProcess();
	}

	protected void initDocInputProcess() {
		this.docInputProcess = DocInputProcess.newDocInputProcess( validating, failOnValidate, cleanSource );
	}

	private transient DefaultChainProvider defaultChain;
	
	protected void setDefaultChain( DefaultChainProvider defaultChain ) {
		this.defaultChain = defaultChain;
	}
	
	protected DefaultChainProvider getDefaultChain() {
		return this.defaultChain;
	}

	public DocProcessData fullProcess( String chainId, DocProcessContext context, String handlerId, OutputStream os ) {
		return this.fullProcess( chainId, context, handlerId, DocOutput.newOutput( os ) );
	}

	public DocProcessData fullProcess( String chainId, DocProcessContext context, String handlerId, DocOutput docOutput ) {
		return SafeFunction.get( () -> this.fullProcess( chainId, context, this.facade.findHandlerRequired( handlerId ), docOutput ) );
	}

	public DocProcessData fullProcess( String chainId, DocProcessContext context, DocTypeHandler handler, DocOutput docOutput ) throws Exception {
		DocProcessData data = new DocProcessData();
		this.process(chainId, context, data);
		DocInput docInput = this.docInputProcess.process( DocInput.newInput( handler.getType() , data.getCurrentXmlReader(), context.getSourceType() ) );
		handler.handle( docInput , docOutput );
		return data;
	}
	
	public void process( String chainId, DocProcessContext context, DocProcessData data ) throws Exception {
		MiniFilterChain chain = this.getChainCache( chainId );
		chain.apply( context , data );
	}
	
	public void process( String chainId, DocProcessContext context, DocProcessData data, DocTypeHandler handler, DocOutput docOutput ) throws Exception {
		this.process(chainId, context, data);
		handler.handle( DocInput.newInput( handler.getType() , data.getCurrentXmlReader() ) , docOutput );
	}
	
	public SAXParseResult process( String chainId, String type, DocProcessContext context, OutputStream os, boolean validate ) throws Exception {
		SAXParseResult result = null;
		MiniFilterChain chain = this.getChainCache( chainId );
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
		this.getFacade().handle(input, output);
		return result;
	}
	
	@Override
	public MiniFilterChain getChain(String id) throws Exception {
		return this.docProcessConfig.getChain( id );
	}
	
	@Override
	public MiniFilterChain getChainCache(String id) throws Exception {
		MiniFilterChain chain = null;
		if ( this.docProcessConfig.getKeys().contains( id ) ) {
			chain = this.docProcessConfig.getChain(id);
		} else  if ( this.getDefaultChain() != null ) {
			chain = this.getDefaultChain().newDefaultChain(id);
			this.setChain(id, chain);
		}
		return chain;
	}

	@Override
	public Set<String> getKeys() {
		return this.docProcessConfig.getKeys();
	}

	@Override
	public void setChain(String id, MiniFilterChain chain) {
		this.docProcessConfig.setChain(id, chain);
	}
	
}
