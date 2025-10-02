package test.org.fugerit.java.doc.base.config;

import org.fugerit.java.doc.base.config.VenusVersion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestVenusVersion {

    @Test
    void testVersion() {
        Assertions.assertNotNull( VenusVersion.getFjDocCoreVersionS() );
        Assertions.assertNotNull( VenusVersion.getFjDocCoreVersionS() );
    }

}
