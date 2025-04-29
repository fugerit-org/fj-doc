package test.org.fugerit.java.doc.sample;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.lib.direct.VenusDirectFacade;
import org.fugerit.java.doc.lib.direct.config.VenusDirectConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

class TestSampleDirect {

    @Test
    void testDirect() throws IOException {
        try (Reader reader = new InputStreamReader(ClassHelper.loadFromDefaultClassLoader( "venus-config-direct/venus-direct-config.yaml" ) )) {
            Map<String, String> envMap = new HashMap<>();
            envMap.put( "projectBasedir", "." );
            VenusDirectConfig config = VenusDirectFacade.readConfig( reader, envMap );
            Assertions.assertThrows( ConfigRuntimeException.class, ()-> VenusDirectFacade.handleAllOutput( config ) );
        }
        File testOutputMd = new File( "./target/direct-sample/issue-426.md" );
        Assertions.assertTrue( testOutputMd.exists() );
    }

}
