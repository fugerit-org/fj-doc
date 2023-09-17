package org.fugerit.java.doc.base.process;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.xml.GenericListCatalogConfig;
import org.fugerit.java.core.cfg.xml.ListMapConfig;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.core.util.filterchain.MiniFilterChain;
import org.fugerit.java.core.util.filterchain.MiniFilterConfig;
import org.fugerit.java.core.util.filterchain.MiniFilterConfigEntry;
import org.fugerit.java.core.util.filterchain.MiniFilterMap;

public class DocProcessConfig implements MiniFilterMap, Serializable {

	private static final long serialVersionUID = 6756541624973903875L;

	public static final String ATT_TAG_DATA_LIST = "chain";

	public static final String ATT_TAG_DATA = "step";

	private MiniFilterConfig miniFilterConfig;

	public DocProcessConfig() {
		this.miniFilterConfig = new MiniFilterConfig(ATT_TAG_DATA_LIST, ATT_TAG_DATA);
	}

	public static DocProcessConfig loadConfig( InputStream is, DocProcessConfig config ) {
		return SafeFunction.get( () -> {
			MiniFilterConfig.loadConfig(is, config.miniFilterConfig);
			return config;
		} );
	}
	
	public static DocProcessConfig loadConfigSafe(String configPath) {
		return SafeFunction.get( () -> {
			try (InputStream is = StreamHelper.resolveStream(configPath)) {
				return loadConfig(is);
			}	
		} );
	}

	public static DocProcessConfig loadConfig(InputStream is) throws ConfigException {
		DocProcessConfig config = new DocProcessConfig();
		config.miniFilterConfig.getGeneralProps().setProperty(GenericListCatalogConfig.ATT_TYPE,
				MiniFilterConfigEntry.class.getName());
		return ConfigException.get( () -> {
			MiniFilterConfig.loadConfigMap(is, config.miniFilterConfig);
			return config;
		} );
	}

	@Override
	public Set<String> getKeys() {
		return miniFilterConfig.getKeys();
	}

	@Override
	public void setChain(String id, MiniFilterChain chain) {
		miniFilterConfig.setChain(id, chain);
	}

	@Override
	public MiniFilterChain getChain(String id) throws Exception {
		return miniFilterConfig.getChain(id);
	}

	@Override
	public MiniFilterChain getChainCache(String id) throws Exception {
		return miniFilterConfig.getChainCache(id);
	}

	public ListMapConfig<MiniFilterConfigEntry> getListMap(String id) {
		return miniFilterConfig.getListMap(id);
	}

	public Properties getGeneralProps() {
		return miniFilterConfig.getGeneralProps();
	}

	public Set<String> getIdSet() {
		return miniFilterConfig.getIdSet();
	}

	public Collection<MiniFilterConfigEntry> getDataList(String id) {
		return miniFilterConfig.getDataList(id);
	}

}
