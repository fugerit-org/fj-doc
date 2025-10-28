package test.org.fugerit.java.doc.mod.fop;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.doc.mod.fop.utils.ConfigUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestConfigUtils {

    @Test
    void testLegacyClassLoaderModeConfigHandlerError() {
        Assertions.assertThrows(ConfigRuntimeException.class, () -> ConfigUtils.LEGACY_CLASS_LOADER_MODE_CONFIG_HANDLER.accept( Boolean.TRUE ) );
    }

    @Test
    void testLegacyClassLoaderModeConfigHandlerWarning() {
        boolean ok = Boolean.TRUE;
        ConfigUtils.LEGACY_CLASS_LOADER_MODE_CONFIG_HANDLER.accept( Boolean.FALSE );
        Assertions.assertTrue( ok );
    }

}
