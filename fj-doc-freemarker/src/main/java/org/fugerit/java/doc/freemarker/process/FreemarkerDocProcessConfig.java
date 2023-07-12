package org.fugerit.java.doc.freemarker.process;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.fugerit.java.core.cfg.xml.ListMapConfig;
import org.fugerit.java.core.util.filterchain.MiniFilterChain;
import org.fugerit.java.doc.base.process.DocProcessConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FreemarkerDocProcessConfig extends DocProcessConfig implements Serializable {

	private static final long serialVersionUID = -6761081877582850120L;

	@Getter
	private ListMapConfig<DocChainModel> docChainList;
	
	private Map<String, MiniFilterChain> additionalChans;
	
	protected FreemarkerDocProcessConfig() {
		super();
		this.docChainList = new ListMapConfig<>();
		this.additionalChans = new HashMap<>();
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
		log.info( "chain list {}", this.getIdSet() );
		chain.apply( context , data );
	}
	
	@Override
	public MiniFilterChain getChain(String id) throws Exception {
		MiniFilterChain chain = null;
		if ( this.getDataList( id ) != null ) {
			chain = this.getChain( id );
		} else if ( this.additionalChans.containsKey( id ) ) {
			chain = this.additionalChans.get( id );
		} else if ( this.getDefaultChain() != null ) {
			chain = this.getDefaultChain().newDefaultChain(id);
			this.addAdditionalChain(chain);
		}
		return chain;
	}
	
	protected void addAdditionalChain( MiniFilterChain chain ) {
		this.additionalChans.put(chain.getChainId(), chain);
	}
	
}
