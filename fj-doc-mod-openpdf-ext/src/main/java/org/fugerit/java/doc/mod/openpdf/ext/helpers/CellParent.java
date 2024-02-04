package org.fugerit.java.doc.mod.openpdf.ext.helpers;

import com.lowagie.text.Cell;
import com.lowagie.text.Element;

public class CellParent implements ParentElement {
	
	private Cell cell;
	
	public CellParent( Cell cell ) {
		this.cell = cell;
	}

	/* (non-Javadoc)
	 * @see org.fugerit.java.doc.mod.itext.ParentElement#add(com.lowagie.text.Element)
	 */
	public void add(Element element) {
		this.cell.addElement( element );
	}
	
}