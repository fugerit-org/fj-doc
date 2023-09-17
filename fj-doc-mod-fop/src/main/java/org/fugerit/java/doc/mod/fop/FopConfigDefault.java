package org.fugerit.java.doc.mod.fop;

import java.io.File;
import java.io.Serializable;

import org.apache.fop.apps.FopFactory;
import org.fugerit.java.core.cfg.ConfigException;

public class FopConfigDefault implements Serializable, FopConfig {

	/**
	 * 
	 */
	private static final long serialVersionUID = 142423475475L;

	@Override
	public FopFactory newFactory() throws ConfigException {
		return FopFactory.newInstance(new File(".").toURI());
	}

	public static final FopConfig DEFAULT = new FopConfigDefault();

}
