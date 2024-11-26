package test.org.fugerit.java.doc.mod.fop;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import org.junit.Assert;
import org.junit.Test;

public class TestInitFreeMarkerConfig {

    @Test
    public void restError() {
        try {
            FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://fj-test-error-config.xml" );
        } catch (ConfigRuntimeException e) {
            Assert.assertTrue( e.getMessage().contains( "Cannot find fop config path" ) );
        }

    }

}
