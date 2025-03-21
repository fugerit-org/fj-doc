package test.org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.project.facade.FlavourContext;
import org.fugerit.java.doc.project.facade.FlavourFacade;
import org.fugerit.java.doc.project.facade.flavour.extra.FlavourExtraConfig;
import org.fugerit.java.doc.project.facade.flavour.extra.FlavourExtraConfigFacade;
import org.fugerit.java.doc.project.facade.flavour.extra.ParamConfig;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

@Slf4j
public class TestFlavourExtraConfigFacade {

    private static final String FLAVOURTEST_1 = "flavourtest-1";

    @Test
    public void readConfig() throws IOException {
        // test quarkus-3 config
        String flavourConfigPath = String.format( "config/flavour-extra-config/%s-config.yml", FLAVOURTEST_1 );
        try (InputStream is = ClassHelper.loadFromDefaultClassLoader( flavourConfigPath ) ) {
            FlavourExtraConfig configQuarkus3 = FlavourExtraConfigFacade.readConfigBlankDefault( is );
            Assert.assertTrue( ((ParamConfig)configQuarkus3.getParamConfig().get( "addLombok" )).getAcceptOnly().contains( "true" ) );
        }
        // test config does not exist
        try (InputStream is = ClassHelper.loadFromDefaultClassLoader( "config/flavour-extra-config/do-not-exist.yml" ) ) {
            FlavourExtraConfig configNotExists = FlavourExtraConfigFacade.readConfig( is );
            Assert.assertNull( configNotExists );
        }
    }

    @Test
    public void testCheckFlavourExtraConfig() throws NoSuchFieldException {
        File projectFolder = new File( "target/test-flavour-extra-config" );
        String groupId = "test-group";
        String artifactId = "test-artifact";
        String projectVersion = "1.0.0";
        String javaRelease = "21";
        String flavour = FLAVOURTEST_1;
        FlavourContext context = new FlavourContext( projectFolder, groupId, artifactId, projectVersion, javaRelease, flavour );
        context.setAddLombok( Boolean.TRUE );
        FlavourFacade.checkFlavourExtraConfig( context, flavour );
        context.setAddLombok( Boolean.FALSE );
        Assert.assertThrows( ConfigRuntimeException.class, () ->  FlavourFacade.checkFlavourExtraConfig( context, flavour ) );
        String testFlavourVersion = "3.19.4";
        String propertyFlavourVersion = "flavourVersion";
        context.setFlavourVersion( testFlavourVersion );
        Field fieldFlavourVersion = FlavourContext.class.getDeclaredField( propertyFlavourVersion );
        Assert.assertEquals( testFlavourVersion, FlavourFacade.readField( context, fieldFlavourVersion, propertyFlavourVersion ) );
    }

}

