package test.org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.doc.maven.MojoInit;
import org.fugerit.java.doc.project.facade.FlavourContext;
import org.fugerit.java.doc.project.facade.FlavourFacade;
import org.fugerit.java.doc.project.facade.ModuleFacade;
import org.fugerit.java.doc.project.facade.VenusContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
public class TestInit {

    private static final String FREEMARKER_NATIVE_AVAILABLE = "8.11.9";

    private String getVersion() {
        return "8.10.9";
    }

    private File initConfigWorker( String flavour ) {
        File outputFolder = new File( "target",  String.format( "init_%s_%s" , flavour, UUID.randomUUID() ) );
        outputFolder.mkdir();
        return outputFolder;
    }

    private MojoInit createMojoInit( File projectDir, String currentFlavour ) {
        return new MojoInit() {
            @Override
            public void execute() throws MojoExecutionException, MojoFailureException {
                this.baseInitFolder = projectDir.getAbsolutePath();
                this.projectVersion = "1.0.0-SNAPSHOT";
                this.groupId = "org.fugerit.java.test.gradle";
                this.artifactId = "fugerit-test-"+currentFlavour;
                this.javaRelease = "21";
                this.version = getVersion();
                this.extensions = "fj-doc-base,fj-doc-base-json,fj-doc-base-yaml,fj-doc-base-kotlin,fj-doc-freemarker,fj-doc-mod-fop,fj-doc-mod-poi,fj-doc-mod-opencsv";
                this.addDocFacade = true;
                this.force = true;
                this.excludeXmlApis = true;
                this.addVerifyPlugin = true;
                this.addJunit5 = true;
                this.addLombok = true;
                this.flavour = currentFlavour;
                super.execute();
            }
        };
    }

    @Test
    public void testMojoQuarkus3GradleGroovyAndKts() throws MojoExecutionException, MojoFailureException {
        for ( String currentFlavour : Arrays.asList( FlavourFacade.FLAVOUR_QUARKUS_3_GRADLE, FlavourFacade.FLAVOUR_QUARKUS_3_GRADLE_KTS ) ) {
            File projectDir = this.initConfigWorker(currentFlavour);
            createMojoInit( projectDir, currentFlavour ).execute();
            Assert.assertTrue( projectDir.exists() );
        }
    }

    @Test
    public void testMojoInit() throws MojoExecutionException, MojoFailureException {
        for ( String currentFlavour : FlavourFacade.SUPPORTED_FLAVOURS ) {
            if ( FlavourFacade.isGradleKtsFlavour( currentFlavour ) ) {
                log.info( "skip gradle flavour {}", currentFlavour );
            } else {
                File projectDir = this.initConfigWorker(currentFlavour);
                MojoInit mojoInit = createMojoInit( projectDir, currentFlavour );
                mojoInit.execute();
                Assert.assertTrue( projectDir.exists() );
                Assert.assertThrows( MojoFailureException.class, () -> mojoInit.execute() );
                Assert.assertThrows( MojoFailureException.class, () -> mojoInit.apply( () -> {
                    if ( Boolean.TRUE ) {
                        throw new ConfigException( "Scenario excetion" );
                    }
                } ) );
            }
        }
    }

    @Test
    public void testFlavourContext() {
        File testFile = new File( "target" );
        Assert.assertThrows( NullPointerException.class, () -> new FlavourContext( null, null, null, null, null, null ) );
        Assert.assertThrows( NullPointerException.class, () -> new FlavourContext( testFile, null, null, null, null, null ) );
        Assert.assertThrows( NullPointerException.class, () -> new FlavourContext( testFile, "group-id1", null, null, null, null ) );
        Assert.assertThrows( NullPointerException.class, () -> new FlavourContext( testFile, "group-id2", "artifact-id2", null, null, null ) );
        Assert.assertThrows( NullPointerException.class, () -> new FlavourContext( testFile, "group-id3", "artifact-id3", "1.0.0-SNAPSHOT", null, null ) );
        Assert.assertThrows( NullPointerException.class, () -> new FlavourContext( testFile, "group-id4", "artifact-id4", "2.0.0-SNAPSHOT", "21", null ) );
        FlavourContext context = new FlavourContext( testFile, "group-id5", "artifact-id5", "3.0.0-SNAPSHOT", "8", "unsupported" );
        Assert.assertThrows( ConfigRuntimeException.class, () -> FlavourFacade.initProject( context ) );
        Assert.assertThrows( ConfigRuntimeException.class, () -> ModuleFacade.toModuleList( "base,freemarker,unsupported" ) );
        Assert.assertThrows( ConfigRuntimeException.class, () -> FlavourFacade.checkFlavour( context, FlavourFacade.FLAVOUR_QUARKUS_3 ) );
        FlavourFacade.checkFlavour( context, FlavourFacade.FLAVOUR_QUARKUS_2 );
        FlavourFacade.checkFlavour( new FlavourContext( testFile, "group-id5", "artifact-id5", "3.0.0-SNAPSHOT", "11", "unsupported" ), FlavourFacade.FLAVOUR_QUARKUS_2 );
        context.setFlavourVersion(  "test" );
        FlavourFacade.checkFlavourVersion( context, FlavourFacade.FLAVOUR_QUARKUS_2  );
        Assert.assertEquals( "test", context.getFlavourVersion() );
        context.setVersion( VenusContext.VERSION_NA_FREEMARKER_NATIVE );
        Assert.assertFalse( context.isFreeMarkerNativeAvailable() );
        context.setVersion( FREEMARKER_NATIVE_AVAILABLE );
        Assert.assertTrue( context.isFreeMarkerNativeAvailable() );
    }

}
