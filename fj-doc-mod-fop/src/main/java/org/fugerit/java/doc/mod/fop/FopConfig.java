package org.fugerit.java.doc.mod.fop;

import java.io.Serializable;

import org.apache.fop.apps.FopFactory;

public interface FopConfig extends Serializable {

		public FopFactory newFactory() throws Exception;
	
}
