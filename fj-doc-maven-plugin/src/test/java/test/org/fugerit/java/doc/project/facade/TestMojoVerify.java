package test.org.fugerit.java.doc.project.facade;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.fugerit.java.core.function.SimpleValue;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.maven.MojoVerify;
import org.junit.Assert;
import org.junit.Test;

public class TestMojoVerify {

    private static final String PATH_OK = "src/test/resources/fj_doc_test/template";

    private static final String PATH_KO = "src/test/resources/fj_doc_test/template-fail";

    private static final String OUTPUT_1 = "target/report-1";

    private static final String OUTPUT_2 = "target/report-2";


    @Test
    public void testMojoVerifyOk() throws MojoExecutionException, MojoFailureException {
        SimpleValue<Boolean> res = new SimpleValue<>( Boolean.FALSE );
        MojoVerify mojoVerify = new MojoVerify() {
            @Override
            public void execute() throws MojoExecutionException, MojoFailureException {
                this.failOnErrors=true;
                this.templateFilePattern = ".{0,}[.]ftl";
                this.templateBasePath = PATH_OK;
                this.generateReport = true;
                this.reportOutputFolder = OUTPUT_1;
                super.execute();
                res.setValue( Boolean.TRUE );
            }
        };
        mojoVerify.execute();
        Assert.assertTrue( res.getValue() );
    }

    @Test
    public void testMojoVerifyKo() throws MojoExecutionException, MojoFailureException {
        SimpleValue<Boolean> res = new SimpleValue<>( Boolean.FALSE );
        MojoVerify mojoVerify = new MojoVerify() {
            @Override
            public void execute() throws MojoExecutionException, MojoFailureException {
                this.failOnErrors=false;
                this.templateBasePath = PATH_KO;
                super.execute();
                res.setValue( Boolean.TRUE );
            }
        };
        mojoVerify.execute();
        Assert.assertTrue( res.getValue() );
    }

    @Test
    public void testMojoVerifyKoFail() {
        SimpleValue<Boolean> res = new SimpleValue<>( Boolean.FALSE );
        MojoVerify mojoVerify = new MojoVerify() {
            @Override
            public void execute() throws MojoExecutionException, MojoFailureException {
                this.failOnErrors=true;
                this.templateBasePath = PATH_KO;
                this.generateReport = true;
                this.reportOutputFolder = OUTPUT_2;
                this.reportOutputFormat = DocConfig.TYPE_CSV;
                super.execute();
                res.setValue( Boolean.TRUE );
            }
        };
        Assert.assertThrows( MojoFailureException.class, () -> mojoVerify.execute() );
    }

    @Test
    public void testMojoVerifyKoPathNoFolder() {
        SimpleValue<Boolean> res = new SimpleValue<>( Boolean.FALSE );
        MojoVerify mojoVerify = new MojoVerify() {
            @Override
            public void execute() throws MojoExecutionException, MojoFailureException {
                this.failOnErrors=true;
                this.templateBasePath = "pom.xml";
                super.execute();
                res.setValue( Boolean.TRUE );
            }
        };
        Assert.assertThrows( MojoFailureException.class, () -> mojoVerify.execute() );
    }

}
