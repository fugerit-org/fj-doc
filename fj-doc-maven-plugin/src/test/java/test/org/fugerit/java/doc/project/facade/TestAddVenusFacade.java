package test.org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.doc.maven.MojoAdd;
import org.fugerit.java.doc.project.facade.BasicVenusFacade;
import org.fugerit.java.doc.project.facade.VenusContext;
import org.fugerit.java.doc.project.facade.AddVenusFacade;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
public class TestAddVenusFacade {

    private String getVersion() {
        return "8.6.1";
    }

    private File initConfigWorker(String configId ) throws IOException {
        File outputFolder = new File( "target", configId+"_"+UUID.randomUUID().toString() );
        outputFolder.mkdir();
        File pomFile = new File( outputFolder, "pom.xml" );
        FileIO.writeString( FileIO.readString( new File( "src/test/resources/"+configId+"/pom.xml" ) ), pomFile );
        return outputFolder;
    }

    @Test
    public void testAddVenus() throws IOException {
        int count = 0;
        for ( String currentConfig : Arrays.asList( "ok1-pom", "ok2-pom", "ok3-pom", "ok4-pom" ) ) {
            File projectDir = this.initConfigWorker( currentConfig );
            log.info( "projectDir: {}, exists:{}", projectDir, projectDir.exists() );
            Assert.assertTrue( projectDir.exists() );
            String moduleList = "fj-doc-base,base-json,mod-fop,mod-opencsv,mod-poi";
            boolean addFacade = false;
            boolean excludeXmlApis = false;
            boolean addVerifyPlugin = false;
            boolean addJunit5 = true;
            boolean addLombok = true;
            boolean addDependencyOnTop = true;
            if ( count == 0 ) {
                moduleList = "base,freemarker";
                addFacade = true;
                addVerifyPlugin = true;
                addJunit5 = false;
                addLombok =  false;
                addDependencyOnTop = false;
            } else if ( count == 3 ) {
                excludeXmlApis = true;
            }
            VenusContext context = new VenusContext( projectDir, this.getVersion(), moduleList );
            context.setExcludeXmlApis( excludeXmlApis );
            context.setAddDocFacace( addFacade );
            context.setAddVerifyPlugin( addVerifyPlugin );
            context.setAddJunit5( addJunit5 );
            context.setAddLombok( addLombok );
            context.setAddDependencyOnTop( addDependencyOnTop );
            boolean result = AddVenusFacade.addToProject( context );
            Assert.assertTrue( result );
            Assert.assertThrows( ConfigRuntimeException.class, () -> AddVenusFacade.addToProject( context ) );
            count++;
        }
    }

    @Test
    public void testMojoAdd() throws IOException, MojoExecutionException, MojoFailureException {
        for ( String currentConfig : Arrays.asList( "ok3-pom" ) ) {
            File projectDir = this.initConfigWorker( currentConfig );
            MojoAdd mojoAdd = new MojoAdd() {
                @Override
                public void execute() throws MojoExecutionException, MojoFailureException {
                    this.version = VenusContext.VERSION_NA_VERIFY_PLUGIN;
                    this.extensions = "fj-doc-base,fj-doc-base-json,fj-doc-base-yaml,fj-doc-freemarker,fj-doc-mod-fop,fj-doc-mod-poi,fj-doc-mod-opencsv";
                    this.projectFolder = projectDir.getAbsolutePath();
                    this.addDocFacade = true;
                    this.force = true;
                    this.excludeXmlApis = true;
                    this.addVerifyPlugin = true;
                    this.addJunit5 = true;
                    this.addLombok = true;
                    super.execute();
                }
            };
            mojoAdd.execute();
            Assert.assertTrue( projectDir.exists() );
        }
    }

    @Test
    public void testMojoAddSimpleModel() throws IOException, MojoExecutionException, MojoFailureException {
        for ( String current : Arrays.asList( "ok3-pom", "ok5-pom" ) ) {
            for ( String currentConfig : Arrays.asList( current ) ) {
                File projectDir = this.initConfigWorker( currentConfig );
                MojoAdd mojoAdd = new MojoAdd() {
                    @Override
                    public void execute() throws MojoExecutionException, MojoFailureException {
                        this.version = "8.12.5";
                        this.extensions = "fj-doc-base,fj-doc-base-json,fj-doc-base-yaml,fj-doc-freemarker,fj-doc-mod-fop,fj-doc-mod-poi,fj-doc-mod-opencsv";
                        this.projectFolder = projectDir.getAbsolutePath();
                        this.addDocFacade = true;
                        this.force = true;
                        this.excludeXmlApis = true;
                        this.simpleModel = true;
                        super.execute();
                    }
                };
                mojoAdd.execute();
                Assert.assertTrue( projectDir.exists() );
            }
        }
    }


    @Test
    public void testFail() {
        VenusContext context = new VenusContext( new File( "target" ), this.getVersion(),"base" );
        boolean result = AddVenusFacade.addToProject( context );
        Assert.assertFalse( result );
    }

    @Test
    public void testFailNoModule() throws IOException {
        for ( String currentConfig : Arrays.asList( "ko1-pom" ) ) {
            File projectDir = this.initConfigWorker(currentConfig);
            VenusContext context = new VenusContext( projectDir, this.getVersion(), "base,not-exists" );
            Assert.assertThrows( ConfigRuntimeException.class, () -> AddVenusFacade.addToProject( context ) );
        }
    }

    @Test
    public void testAdditional() {
        Assert.assertNotNull( new BasicVenusFacade() {});
        Dependency d = new Dependency();
        d.setGroupId( "org.fugerit.java" );
        d.setArtifactId( "fj-core" );
        BasicVenusFacade.checkDependencies( true, d );
        d.setArtifactId( "fj-doc-base" );
        BasicVenusFacade.checkDependencies( true, d );
        Assert.assertThrows( ConfigRuntimeException.class, () -> BasicVenusFacade.checkDependencies( false, d ) );
        d.setGroupId( "junit" );
        d.setArtifactId( "junit" );
        BasicVenusFacade.checkDependencies( true, d );
    }

}
