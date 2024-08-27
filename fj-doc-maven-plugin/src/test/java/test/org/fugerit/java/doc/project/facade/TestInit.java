package test.org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.maven.MojoInit;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.UUID;

@Slf4j
public class TestInit {

    private String getVersion() {
        return "8.7.1";
    }

    private File initConfigWorker() {
        File outputFolder = new File( "target", "init_"+UUID.randomUUID().toString() );
        outputFolder.mkdir();
        return outputFolder;
    }

    @Test
    public void testMojoInit() throws MojoExecutionException, MojoFailureException {
        File projectDir = this.initConfigWorker( );
        MojoInit mojoInit = new MojoInit() {
            @Override
            public void execute() throws MojoExecutionException, MojoFailureException {
                this.baseInitFolder = projectDir.getAbsolutePath();
                this.projectVersion = "1.0.0-SNAPSHOT";
                this.groupId = "org.fugerit.java.test";
                this.artifactId = "fugerit-test1";
                this.javaRelease = "21";
                this.version = getVersion();
                this.extensions = "fj-doc-base,fj-doc-base-json,fj-doc-base-yaml,fj-doc-freemarker,fj-doc-mod-fop,fj-doc-mod-poi,fj-doc-mod-opencsv";
                this.addDocFacade = true;
                this.force = true;
                this.excludeXmlApis = true;
                this.addVerifyPlugin = true;
                super.execute();
            }
        };
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
