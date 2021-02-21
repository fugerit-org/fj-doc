package org.fugerit.java.doc.base.helper;

import java.io.Serializable;

import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocContainer;
import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocImage;
import org.fugerit.java.doc.base.model.DocList;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.model.DocPhrase;
import org.fugerit.java.doc.base.model.DocTable;

public abstract class DocTypeFacadeAbstract implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4041236992735125446L;

	public abstract void handleDoc( DocBase docBase ) throws Exception;
	
	public abstract void handlePara( DocPara docPara, DocContainer parent, DocTypeFacadeHelper helper ) throws Exception;
	
	public abstract void handlePhrase( DocPhrase docPhrase, DocContainer parent, DocTypeFacadeHelper helper ) throws Exception;
	
	public abstract void handleList( DocList docList, DocContainer parent, DocTypeFacadeHelper helper ) throws Exception;
	
	public abstract void handleImage( DocImage docImage, DocContainer parent, DocTypeFacadeHelper helper ) throws Exception;
	
	public abstract void handleTable( DocTable docTable, DocContainer parent, DocTypeFacadeHelper helper ) throws Exception;
	
	public void handleElements( DocContainer docContainer, DocTypeFacadeHelper helper ) throws Exception {
		helper.depthPlusOne();
		for ( DocElement element : docContainer.getElementList() ) {
			this.handleElement( element, docContainer, helper );
		}
		helper.depthMinusOne();
	}
	
	public void handleElement( DocElement element, DocContainer parent, DocTypeFacadeHelper helper ) throws Exception {
		if ( element instanceof DocPara ) {
			this.handlePara( (DocPara) element, parent, helper );
		} else if ( element instanceof DocPhrase ) {
			this.handlePhrase( (DocPhrase) element, parent, helper );
		} else if ( element instanceof DocTable ) {
			this.handleTable( (DocTable) element, parent, helper );
		} else if ( element instanceof DocList ) {
			this.handleList( (DocList) element, parent, helper );
		} else if ( element instanceof DocImage ) {
			this.handleImage( (DocImage) element, parent, helper );			
		}
	}
	
}
