package test.org.fugerit.java.doc.lib.simpletable;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;
import org.fugerit.java.doc.lib.simpletable.SimpleTableDocConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import test.org.fugerit.java.BasicTest;

class TestSimpleTableConfig extends BasicTest {
	
	@Test
	void newConfigLegacy() {
		try {
			SimpleTableDocConfig config = SimpleTableDocConfig.newConfig();
			Assertions.assertNotNull( config );
		} catch (ConfigException e) {
			this.failEx( e );
		}
	}
	
	@Test
	void newConfigLatest() {
		try {
			SimpleTableDocConfig config = SimpleTableDocConfig.newConfigLatest();
			Assertions.assertNotNull( config );
		} catch (ConfigException e) {
			this.failEx( e );
		}
	}
	
	@Test
	void newConfigCustom() {
		try {
			SimpleTableDocConfig config = SimpleTableDocConfig.newConfig( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION_2_3_31 );
			Assertions.assertNotNull( config );
			Assertions.assertNotNull( config.getConfig() );
		} catch (ConfigException e) {
			this.failEx( e );
		}
	}
	
}
