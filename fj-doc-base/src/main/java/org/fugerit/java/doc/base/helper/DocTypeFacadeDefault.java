package org.fugerit.java.doc.base.helper;

import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocContainer;
import org.fugerit.java.doc.base.model.DocImage;
import org.fugerit.java.doc.base.model.DocList;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.model.DocPhrase;
import org.fugerit.java.doc.base.model.DocTable;

public class DocTypeFacadeDefault extends DocTypeFacadeAbstract {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4041236992125446L;

	@Override
	public void handleDoc(DocBase docBase) throws Exception {
		// do nothing implementation : subclass must implements this method if they want a different behavior
	}

	@Override
	public void handlePara(DocPara docPara, DocContainer parent, DocTypeFacadeHelper helper) throws Exception {
		// do nothing implementation : subclass must implements this method if they want a different behavior
	}

	@Override
	public void handlePhrase(DocPhrase docPhrase, DocContainer parent, DocTypeFacadeHelper helper) throws Exception {
		// do nothing implementation : subclass must implements this method if they want a different behavior
	}

	@Override
	public void handleList(DocList docList, DocContainer parent, DocTypeFacadeHelper helper) throws Exception {
		// do nothing implementation : subclass must implements this method if they want a different behavior
	}

	@Override
	public void handleImage(DocImage docImage, DocContainer parent, DocTypeFacadeHelper helper) throws Exception {
		// do nothing implementation : subclass must implements this method if they want a different behavior
	}

	@Override
	public void handleTable(DocTable docTable, DocContainer parent, DocTypeFacadeHelper helper) throws Exception {
		// do nothing implementation : subclass must implements this method if they want a different behavior
	}
	
}
