package org.fugerit.java.doc.freemarker.process;

import java.io.Serializable;
import java.util.Set;

import org.fugerit.java.core.cfg.xml.ListMapConfig;
import org.fugerit.java.core.util.filterchain.MiniFilterChain;
import org.fugerit.java.core.util.filterchain.MiniFilterMap;
import org.fugerit.java.doc.base.facade.DocHandlerFacade;
import org.fugerit.java.doc.base.process.DocProcessConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FreemarkerDocProcessConfig implements Serializable, MiniFilterMap {

	private static final long serialVersionUID = -6761081877582850120L;

	@Getter
	private ListMapConfig<DocChainModel> docChainList;
	
	@Getter
	private DocHandlerFacade facade;
	
	private DocProcessConfig docProcessConfig;
	
	protected FreemarkerDocProcessConfig() {
		super();
		this.docChainList = new ListMapConfig<>();
		this.facade = new DocHandlerFacade();
		this.docProcessConfig = new DocProcessConfig();
	}
	
	private DefaultChainProvider defaultChain;
	
	protected void setDefaultChain( DefaultChainProvider defaultChain ) {
		this.defaultChain = defaultChain;
	}
	
	protected DefaultChainProvider getDefaultChain() {
		return this.defaultChain;
	}
	
	public void process( String chainId, DocProcessContext context, DocProcessData data ) throws Exception {
		MiniFilterChain chain = this.getChainCache( chainId );
		log.info( "chain list {}", this.docProcessConfig.getIdSet() );
		chain.apply( context , data );
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
		} else  if ( this.defaultChain != null ) {
			chain = this.defaultChain.newDefaultChain(id);
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
