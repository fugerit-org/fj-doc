package org.fugerit.java.doc.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.fugerit.java.core.util.result.Result;
import org.fugerit.java.doc.freemarker.tool.FreeMarkerTemplateSyntaxVerifier;
import org.fugerit.java.doc.freemarker.tool.verify.VerifyTemplateOutput;

import java.io.File;
import java.util.Properties;

@Mojo( name = "verify" )
public class MojoVerify extends AbstractMojo {

    @Parameter(property = "templateBasePath", required = true)
    protected String templateBasePath;

    @Parameter(property = "freemarkerVersion", required = false)
    protected String freemarkerVersion;

    @Parameter(property = "templateFilePattern", required = false)
    protected String templateFilePattern;

    @Parameter(property = "failOnErrors", defaultValue = "true", required = true)
    protected boolean failOnErrors;

    @Parameter(property = "generateReport", defaultValue = "false", required = false)
    protected boolean generateReport;

    @Parameter(property = "reportOutputFolder", required = false)
    protected String reportOutputFolder;

    @Parameter(property = "reportOutputFormat", defaultValue = "html", required = false)
    protected String reportOutputFormat;

    private void addIfPresent( Properties params, String key, String value ) {
        if ( value != null ) {
            this.getLog().info( String.format( "setting verifier parameter : %s -> %s", key, value ) );
            params.setProperty( key, value );
        }
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File baseFolder = new File( this.templateBasePath );
        this.getLog().info( String.format( "templateBasePath: %s", baseFolder.getAbsolutePath() ) );
        if ( !baseFolder.isDirectory() ) {
            String message = String.format( "Fatal error, templateBasePath is not a directory: '%s'", templateBasePath );
            this.getLog().error( message );
            throw new MojoFailureException( message );
        }
        Properties params = new Properties();
        this.addIfPresent( params, FreeMarkerTemplateSyntaxVerifier.PARAM_VERSION, this.freemarkerVersion );
        this.addIfPresent( params, FreeMarkerTemplateSyntaxVerifier.PARAM_TEMPLATE_FILE_PATTERN, this.templateFilePattern );
        this.addIfPresent( params, FreeMarkerTemplateSyntaxVerifier.PARAM_GENERATE_REPORT, String.valueOf( this.generateReport ) );
        this.addIfPresent( params, FreeMarkerTemplateSyntaxVerifier.PARAM_REPORT_OUTPUT_FOLDER, this.reportOutputFolder );
        this.addIfPresent( params, FreeMarkerTemplateSyntaxVerifier.PARAM_REPORT_OUTPUT_FORMAT, this.reportOutputFormat );
        FreeMarkerTemplateSyntaxVerifier verifier = new FreeMarkerTemplateSyntaxVerifier();
        VerifyTemplateOutput output = verifier.createConfigurationAndVerify( baseFolder, params );
        this.getLog().info( String.format( "verify output resultCode %s, checked templates : %s", output.getResultCode(), output.getInfos().size() ) );
        if ( output.getResultCode() == Result.RESULT_CODE_OK ) {
            this.getLog().info( "Verified OK" );
        } else {
            this.getLog().warn( String.format( "Verified KO : %s", output.getResultCode() ) );
            if ( this.failOnErrors ) {
                throw new MojoFailureException( String.format(
                        "Build failed! FreeMarker template syntax verify failed, resultCode: %s, templates with error: %s",
                        output.getResultCode(), output.getErrorsTemplateIds() ) );
            } else {
                this.getLog().warn( String.format(
                        "WARNING! (failOnErrors=false) FreeMarker template syntax verify failed, resultCode: %s, templates with error: %s",
                        output.getResultCode(), output.getErrorsTemplateIds() ) );
            }
        }
    }

}
