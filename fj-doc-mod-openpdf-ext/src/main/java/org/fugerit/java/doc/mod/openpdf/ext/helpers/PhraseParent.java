package org.fugerit.java.doc.mod.openpdf.ext.helpers;

import org.fugerit.java.doc.base.config.DocException;

import com.lowagie.text.Element;
import com.lowagie.text.Phrase;

public class PhraseParent implements ParentElement {

	private Phrase phrase;
	
	public PhraseParent( Phrase phrase ) {
		this.phrase = phrase;
	}
	
	/* (non-Javadoc)
	 * @see org.fugerit.java.doc.mod.itext.ParentElement#add(com.lowagie.text.Element)
	 */
	public void add(Element element) throws DocException {
		this.phrase.add( element );
	}
	
}
