package test.org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
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
        return "8.5.2";
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
        for ( String currentConfig : Arrays.asList( "ok1-pom", "ok2-pom" ) ) {
            File projectDir = this.initConfigWorker( currentConfig );
            log.info( "projectDir: {}, exists:{}", projectDir, projectDir.exists() );
            Assert.assertTrue( projectDir.exists() );
            String moduleList = "fj-doc-base,base-json,mod-fop,mod-opencsv,mod-poi";
            if ( count == 0 ) {
                moduleList = "base,freemarker";
            }
            VenusContext context = new VenusContext( projectDir, this.getVersion(), moduleList );
            boolean result = AddVenusFacade.addVenusToMavenProject( context );
            Assert.assertTrue( result );
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
                    this.version = getVersion();
                    this.extensions = "fj-doc-base,fj-doc-base-json,fj-doc-base-yaml,fj-doc-freemarker,fj-doc-mod-fop,fj-doc-mod-poi,fj-doc-mod-opencsv";
                    this.projectFolder = projectDir.getAbsolutePath();
                    this.addDocFacade = true;
                    super.execute();
                }
            };
            mojoAdd.execute();
            Assert.assertTrue( projectDir.exists() );
        }
    }

    @Test
    public void testFail() {
        VenusContext context = new VenusContext( new File( "target" ), this.getVersion(),"base" );
        boolean result = AddVenusFacade.addVenusToMavenProject( context );
        Assert.assertFalse( result );
    }

    @Test
    public void testAdditional() {
        Assert.assertNotNull( new BasicVenusFacade() {} );
    }

}
