package org.fugerit.java.doc.base.model;

public class DocLi extends DocContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8555789702608296306L;

	private DocPhrase liLabel;
	
	private DocPara liBody;

	public DocPhrase getLiLabel() {
		return liLabel;
	}

	public void setLiLabel(DocPhrase liLabel) {
		this.liLabel = liLabel;
	}

	public DocPara getLiBody() {
		return liBody;
	}

	public void setLiBody(DocPara liBody) {
		this.liBody = liBody;
	}
	
	public void endElement() {
		for ( DocElement element :this.getElementList() ) {
			if ( element instanceof DocPhrase ) {
				this.setLiLabel( (DocPhrase) element );
			} else if ( element instanceof DocPara ) { 
				this.setLiBody( (DocPara) element );
			}
		}
	}
	
}
