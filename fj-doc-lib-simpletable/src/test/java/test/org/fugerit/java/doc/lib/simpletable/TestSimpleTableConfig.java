package test.org.fugerit.java.doc.lib.simpletable;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;
import org.fugerit.java.doc.lib.simpletable.SimpleTableDocConfig;
import org.junit.Assert;
import org.junit.Test;

import test.org.fugerit.java.BasicTest;

public class TestSimpleTableConfig extends BasicTest {
	
	@Test
	public void newConfigLegacy() {
		try {
			SimpleTableDocConfig config = SimpleTableDocConfig.newConfig();
			Assert.assertNotNull( config );
		} catch (ConfigException e) {
			this.failEx( e );
		}
	}
	
	@Test
	public void newConfigLatest() {
		try {
			SimpleTableDocConfig config = SimpleTableDocConfig.newConfigLatest();
			Assert.assertNotNull( config );
		} catch (ConfigException e) {
			this.failEx( e );
		}
	}
	
	@Test
	public void newConfigCustom() {
		try {
			SimpleTableDocConfig config = SimpleTableDocConfig.newConfig( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION_2_3_31 );
			Assert.assertNotNull( config );
			Assert.assertNotNull( config.getConfig() );
		} catch (ConfigException e) {
			this.failEx( e );
		}
	}
	
}
