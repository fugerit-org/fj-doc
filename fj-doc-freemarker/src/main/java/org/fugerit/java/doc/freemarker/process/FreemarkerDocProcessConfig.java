package org.fugerit.java.doc.freemarker.process;

import java.io.Serializable;

import org.fugerit.java.core.cfg.xml.ListMapConfig;
import org.fugerit.java.doc.base.process.DocProcessConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;

import lombok.Getter;

public class FreemarkerDocProcessConfig extends DocProcessConfig implements Serializable {

	private static final long serialVersionUID = -6761081877582850120L;

	@Getter
	private ListMapConfig<ConfigInitModel> configInitList;
	
	@Getter
	private ListMapConfig<DocChainModel> docChainList;
	
	protected FreemarkerDocProcessConfig() {
		this.configInitList = new ListMapConfig<>();
		this.docChainList = new ListMapConfig<>();
	}
	
	public void process( String configId, String chainId, DocProcessContext context, DocProcessData data ) throws Exception {
		ConfigInitModel configInitModel = this.getConfigInitList().get( configId );
		DocChainModel docChainModel = this.getChainOrDefault(chainId);
		configInitModel.process(docChainModel, context, data);
	}
	
	private DocChainModel getChainOrDefault( String id ) {
		DocChainModel model = this.getDocChainList().get( id );
		if ( model == null ) {
			model = new DocChainModel();
			model.setId( id );
		}
		return model;
	}
	
}
