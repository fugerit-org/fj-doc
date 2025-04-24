package test.org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.fugerit.java.doc.maven.MojoDirect;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
class TestDirect {

    private static final File CONFIG_FILE_EXISTS = new File( "./src/test/resources/direct/config/venus-direct-config-1.yaml" );

    @Test
    void testCheckConfig() {
        File configNotExists = new File( "do-not-exists.yaml" );
        Assertions.assertThrows(MojoExecutionException.class, () ->
                MojoDirect.checkConfiguration(configNotExists, Boolean.TRUE, null ));
        Assertions.assertThrows(MojoExecutionException.class, () ->
                MojoDirect.checkConfiguration(CONFIG_FILE_EXISTS, Boolean.FALSE, null ));
        Assertions.assertThrows(MojoExecutionException.class, () ->
                MojoDirect.checkConfiguration(CONFIG_FILE_EXISTS, Boolean.TRUE, Arrays.asList( "1" ) ));
    }

    @Test
    void mojoDirectAll() throws MojoExecutionException, MojoFailureException {
        MojoDirect mojoDirect = new MojoDirect() {
            @Override
            public void execute() throws MojoExecutionException, MojoFailureException {
                this.configPath = CONFIG_FILE_EXISTS.getAbsolutePath();
                this.outputAll = Boolean.TRUE;
                this.outputId = new ArrayList<>();
                super.execute();
            }
        };
        mojoDirect.execute();
        Assertions.assertTrue( new File( "./target/test-doc.html").exists() );
    }

    @Test
    void mojoDirectId() throws MojoExecutionException, MojoFailureException {
        MojoDirect mojoDirect = new MojoDirect() {
            @Override
            public void execute() throws MojoExecutionException, MojoFailureException {
                this.configPath = CONFIG_FILE_EXISTS.getAbsolutePath();
                this.outputAll = Boolean.FALSE;
                this.outputId = Arrays.asList( "test-doc-md" );
                super.execute();
            }
        };
        mojoDirect.execute();
        Assertions.assertTrue( new File( "./target/test-doc.md").exists() );
    }

}
