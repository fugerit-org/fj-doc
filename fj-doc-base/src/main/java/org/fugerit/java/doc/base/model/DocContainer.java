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
/*
 * @(#)DocContainer.java
 *
 * @project    : org.fugerit.java.doc.base
 * @package    : org.fugerit.java.doc.base
 * @creation   : 06/set/06
 * @license	   : META-INF/LICENSE.TXT
 */
package org.fugerit.java.doc.base.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 *
 * @author mfranci
 *
 */
public class DocContainer extends DocElement {

	private List<DocElement> elementList;
	
	public DocContainer() {
		this.elementList = new ArrayList<DocElement>();
	}
	
	public Iterator<DocElement> docElements() {
		return this.elementList.iterator();
	}
	
	public int containerSize() {
		return this.elementList.size();
	}
	
	public void addElement( DocElement docElement ) {
		this.elementList.add( docElement );
	}
	
	public List<DocElement> getElementList() {
		return elementList;
	}
	
}
