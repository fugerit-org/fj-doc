package test.org.fugerit.java.doc.mod.fop;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestInitFreeMarkerConfig {

    @Test
    void restError() {
        try {
            FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://fj-test-error-config.xml" );
        } catch (ConfigRuntimeException e) {
            Assertions.assertTrue( e.getMessage().contains( "Cannot find fop config path" ) );
        }

    }

}
