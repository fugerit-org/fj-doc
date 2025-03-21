package test.org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.project.facade.flavour.extra.FlavourExtraConfig;
import org.fugerit.java.doc.project.facade.flavour.extra.FlavourExtraConfigFacade;
import org.fugerit.java.doc.project.facade.flavour.extra.ParamConfig;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class TestFlavourExtraConfigFacade {

    @Test
    public void testReadConfig() throws IOException {
        // test quarkus-3 config
        try (InputStream is = ClassHelper.loadFromDefaultClassLoader( "config/flavour-extra-config/quarks-3-config.yml" ) ) {
            FlavourExtraConfig configQuarkus3 = FlavourExtraConfigFacade.readConfigBlankDefault( is );
            Assert.assertTrue( ((ParamConfig)configQuarkus3.getParamConfig().get( "addLombok" )).getAcceptOnly().contains( "true" ) );
        }
        // test config does not exist
        try (InputStream is = ClassHelper.loadFromDefaultClassLoader( "config/flavour-extra-config/do-not-exist.yml" ) ) {
            FlavourExtraConfig configNotExists = FlavourExtraConfigFacade.readConfig( is );
            Assert.assertNull( configNotExists );
        }
    }

}
