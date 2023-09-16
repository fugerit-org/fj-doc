package org.fugerit.java.doc.mod.fop;

import java.io.Serializable;

import org.apache.fop.apps.FopFactory;
import org.fugerit.java.core.cfg.ConfigException;

public interface FopConfig extends Serializable {

		public FopFactory newFactory() throws ConfigException;
	
}
