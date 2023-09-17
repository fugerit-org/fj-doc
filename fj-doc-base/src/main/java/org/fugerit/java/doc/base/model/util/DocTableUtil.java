/*****************************************************************
<copyright>
	Fugerit Java Library org.fugerit.java.doc.base 

	Copyright (c) 2019 Fugerit

	All rights reserved. This program and the accompanying materials
	are made available under the terms of the Apache License v2.0
	which accompanies this distribution, and is available at
	http://www.apache.org/licenses/
	(txt version : http://www.apache.org/licenses/LICENSE-2.0.txt
	html version : http://www.apache.org/licenses/LICENSE-2.0.html)

   This product includes software developed at
   The Apache Software Foundation (http://www.apache.org/).
</copyright>
*****************************************************************/
package org.fugerit.java.doc.base.model.util;
/*
 * @(#)DocTable.java
 *
 * @project    : org.fugerit.java.doc.base
 * @package    : org.fugerit.java.doc.base
 * @creation   : 06/set/06
 * @license	   : META-INF/LICENSE.TXT
 */

import java.util.ArrayList;
import java.util.List;

import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocRow;
import org.fugerit.java.doc.base.model.DocTable;

/**
 * 
 *
 * @author mfranci
 *
 */
public class DocTableUtil extends DocElement {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4708466907819346L;

	private List<DocElement> headerRows;
	
	private List<DocElement> dataRows;
	
	boolean strictHeader = false;
	
	public DocTableUtil(DocTable docTable) {
		super();
		this.headerRows = new ArrayList<>();
		this.dataRows = new ArrayList<>();
		boolean headerSeparation = true;
		for ( DocElement current : docTable.getElementList() ) {
			if ( current instanceof DocRow  ) {
				DocRow row = (DocRow) current;
				if ( row.isHeader() ) {
					this.headerRows.add( row );
					headerSeparation = this.dataRows.isEmpty();
				} else {
					this.dataRows.add( row );
				}
			}
		}
		if ( headerSeparation && !this.headerRows.isEmpty() ) {
			this.strictHeader = true;
		}
	}

	public List<DocElement> getHeaderRows() {
		return headerRows;
	}

	public List<DocElement> getDataRows() {
		return dataRows;
	}

	public boolean isStrictHeader() {
		return strictHeader;
	}
	
}
