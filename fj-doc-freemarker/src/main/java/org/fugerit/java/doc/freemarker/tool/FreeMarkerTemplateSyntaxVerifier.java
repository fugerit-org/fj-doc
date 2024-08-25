package org.fugerit.java.doc.freemarker.tool;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.result.Result;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;
import org.fugerit.java.doc.freemarker.helper.FreeMarkerDocProcess;
import org.fugerit.java.doc.freemarker.tool.verify.VerifyTemplateInfo;
import org.fugerit.java.doc.freemarker.tool.verify.VerifyTemplateOutput;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;

/**
 * Tool to check FreeMarker template syntax for all templates in a FreeMarker Configuration.
 *
 * Only syntax is checked, by calling Configuration.getTemplate() and catching freemarker.core.ParseException.
 *
 * In brief this tool can find "build" time syntax error in FreeMarker templates.
 *
 * Quickstart :
 *
 *  <code>
 *      File baseFolder = nw File( "..." ); // path to FreeMarker base folder template
 *      FreeMarkerTemplateSyntaxVerifier.doCreateConfigurationAndVerify( baseFolder );
 *  </code>
 *
 * NOTE: For customized uses check : createConfigurationAndVerify() and verify() methods.
 */
@Slf4j
public class FreeMarkerTemplateSyntaxVerifier {

    /**
     * If a FreeMarker Configuration should be created, it will use this FreeMarker version
     */
    public static final String PARAM_VERSION = FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION;

    /**
     * It will check only templates with this pattern, (regex check on fileName, i.e. ".{0,}[.]ftl")
     */
    public static final String PARAM_TEMPLATE_FILE_PATTERN = "templateFilePattern";

    /**
     * If set to true a report will be generated (and property 'reportOutputFolder' will be olso required).
     */
    public static final String PARAM_GENERATE_REPORT = "generateReport";

    /**
     * Output folder for the generated report.
     */
    public static final String PARAM_REPORT_OUTPUT_FOLDER = "reportOutputFolder";

    public static VerifyTemplateOutput doCreateConfigurationAndVerify(File baseFolder) {
        return new FreeMarkerTemplateSyntaxVerifier().createConfigurationAndVerify( baseFolder, new Properties() );
    }

    protected  void verifyCurrentTemplate( VerifyTemplateOutput output, File baseFolder, Configuration cfg, String templateId ) throws IOException {
        try {
            Template template = cfg.getTemplate( templateId );
            log.debug( "check template OK, id: {}, name:{}, base folder : {}", templateId, template.getName(), baseFolder );
            output.getInfos().add( new VerifyTemplateInfo(Result.RESULT_CODE_OK, templateId) );
        } catch ( ParseException e ) {
            log.warn( "check template KO:{}, id: {}, base folder : {}", e.getMessage(), templateId, baseFolder );
            output.getInfos().add( new VerifyTemplateInfo(Result.RESULT_CODE_KO, templateId, e) );
        }
    }

    protected void iterateVerify( VerifyTemplateOutput output, File baseFolder, Configuration cfg, File currentFolder ) throws IOException {
        for ( File currentFile : currentFolder.listFiles( output.getFileFilter() ) ) {
            if ( currentFile.isDirectory() ) {
                this.iterateVerify( output, baseFolder, cfg, currentFile );
            } else {
                String templateId = currentFile.getCanonicalPath().substring( baseFolder.getCanonicalPath().length()+1 );
                this.verifyCurrentTemplate( output, baseFolder, cfg, templateId );
            }
        }
    }

    /**
     * It will verify all templates for a FreeMarker configuration :
     * All templates in baseFolder params will be verified
     *
     * NOTE: Given FreemarkerConfiguration cfg should be able to resolve such templates.
     *
     * @param baseFolder    all templates in this folder will be checked
     * @param cfg           FreeMarker Configuration used to load templates
     * @param params        additional params
     * @return
     */
    public VerifyTemplateOutput verify(File baseFolder, Configuration cfg, Properties params) {
        return SafeFunction.get( () -> {
            VerifyTemplateOutput output = new VerifyTemplateOutput();
            // check additional options
            if ( params.containsKey( PARAM_TEMPLATE_FILE_PATTERN ) ) {
                String templateFilePattern = params.getProperty( PARAM_TEMPLATE_FILE_PATTERN );
                log.info( "setting file filter with templateFilePattern: {}", templateFilePattern );
                output.setFileFilter( f -> f.isDirectory() || f.getName().matches( templateFilePattern ) );
            }
            // setup iteration with baseFolder as currentFolder
            this.iterateVerify( output, baseFolder, cfg, baseFolder );
            Collections.sort( output.getInfos(), Comparator.comparing(VerifyTemplateInfo::getTemplateId) );
            // generate report
            this.generateReport( output, params );
            return output;
        } );
    }

    public void generateReport( VerifyTemplateOutput output, Properties params ) throws IOException {
        boolean generateReport = BooleanUtils.isTrue( params.getProperty( PARAM_GENERATE_REPORT ) );
        if ( generateReport ) {
            String reportOutputFolder = params.getProperty( PARAM_REPORT_OUTPUT_FOLDER );
            if (StringUtils.isEmpty( reportOutputFolder )) {
                throw new ConfigRuntimeException( String.format( "Parameter '%s' is required", PARAM_REPORT_OUTPUT_FOLDER ) );
            } else {
                File reportOutputFolderFile = new File( reportOutputFolder );
                if ( !reportOutputFolderFile.exists() ) {
                    log.info( "{} does not exist, create directories : {}", reportOutputFolder, reportOutputFolderFile.mkdirs() );
                }
                String chainId = "freemarker-verify-syntax-report";
                String type = DocConfig.TYPE_HTML;
                File reportFile = new File( reportOutputFolderFile, chainId+"."+type );
                try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                    FreeMarkerDocProcess.getInstance().fullProcess( chainId, DocProcessContext.newContext( "output", output ), type, buffer );
                    FileIO.writeBytes( buffer.toByteArray(), reportFile );
                } catch ( Exception e ) {
                    throw new ConfigRuntimeException(
                            String.format( "Fail to generate report in folder %s, error : %s", reportOutputFolder, e.getMessage() ), e );
                }
            }
        }
    }

    /**
     * It will create a new FreeMarker configuration, loading templates from given base folder.
     *
     * If available, parameters "version" will be used to specific FreeMarker configuration version.
     *
     * Then it will verify all templates for a FreeMarker configuration :
     * All templates in baseFolder params will be verified
     *
     * @param baseFolder    it will be used for Configuration.setDirectoryForTemplateLoading() and to find templates path.
     * @param params        additional configuration parameters
     * @return              the output of the verify operation
     */
    public VerifyTemplateOutput createConfigurationAndVerify(File baseFolder, Properties params) {
        return SafeFunction.get( () -> {
            String version = params.getProperty( PARAM_VERSION, FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION_LATEST );
            log.info( "creating FreeMarker configuration, version : {}", version );
            Configuration cfg = new Configuration( new Version( version ) );
            cfg.setDirectoryForTemplateLoading( baseFolder );
            return this.verify( baseFolder, cfg, params );
        } );
    }

}
