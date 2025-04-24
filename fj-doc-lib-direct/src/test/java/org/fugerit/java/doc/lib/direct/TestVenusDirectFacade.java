package org.fugerit.java.doc.lib.direct;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.lib.direct.config.VenusDirectConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

class TestVenusDirectFacade {

    @Test
    void testDoc() throws IOException {
        try (Reader reader = new InputStreamReader(ClassHelper.loadFromDefaultClassLoader( "config/venus-direct-config-1.yaml" ) )) {
            VenusDirectConfig config = VenusDirectFacade.readConfig( reader );
            VenusDirectFacade.handleAllOutput( config );
            Assertions.assertThrows( ConfigRuntimeException.class, () -> VenusDirectFacade.handleOutput( config, "not-existing-output-id" ) );
            config.getChainMap().remove( "test-doc" );
            Assertions.assertThrows( ConfigRuntimeException.class, () -> VenusDirectFacade.handleOutput( config, "test-doc-html" ) );
        }
    }

    @Test
    void testSubstitute() throws IOException {
        try (Reader reader = new InputStreamReader(ClassHelper.loadFromDefaultClassLoader( "config/venus-direct-config-2.yaml" ) )) {
            Map<String, String> envMap = new HashMap<>();
            envMap.put( "baseDir", "/config" );
            VenusDirectConfig config = VenusDirectFacade.readConfig( reader, envMap );
            Assertions.assertEquals( "/config/src/test/resources/template/", config.getTemplatePath() );
        }
    }

}
