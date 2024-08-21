package org.fugerit.java.doc.project.facade;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.javagen.JavaGenerator;
import org.fugerit.java.core.javagen.SimpleJavaGenerator;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;
import org.fugerit.java.doc.freemarker.fun.SimpleMessageFun;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import org.maxxq.maven.dependency.ModelIO;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class AddVenusFacade extends BasicVenusFacade {

    private AddVenusFacade() {}

    private static final String EXAMPLE_FOLDER = "config/example/";

    private static final String LINE = "************************************************************************************************************************";

    private static void addDocFacade( VenusContext context ) throws IOException, TemplateException, ConfigException {
        // freemarker configuration
        Configuration configuration = new Configuration( new Version( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION_LATEST ) );
        configuration.clearTemplateCache();
        ClassTemplateLoader loader = new ClassTemplateLoader( AddVenusFacade.class, "/config/template/" );
        configuration.setTemplateExceptionHandler( TemplateExceptionHandler.RETHROW_HANDLER );
        configuration.setTemplateLoader( loader );
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        // config generation
        Template fmConfigTemplate = configuration.getTemplate( "fm-doc-process-config-template.ftl" );
        Map<Object, Object> data = new HashMap<>();
        data.put( "context" , context );
        File fmConfigFile = new File( context.getProjectDir(), "src/main/resources/"+context.getResourcePathFmConfigXml() );
        log.info( "fmConfigFile : {}, mk parent? : {}", fmConfigFile.getCanonicalPath(), fmConfigFile.getParentFile().mkdirs() );
        File templateDir = new File( fmConfigFile.getParentFile(), context.getTemplateSubPath() );
        log.info( "templateDir : {}, mk parent? : {}", templateDir.getCanonicalPath(), templateDir.mkdirs() );
        try ( Writer writer = new FileWriter( fmConfigFile ) ) {
            fmConfigTemplate.process( data, writer );
        }
        configuration.clearTemplateCache();
        // copy sample template
        String fileName = "document.ftl";
        File documentExample = new File( templateDir, fileName );
        try ( InputStream documentExampleIS = ClassHelper.loadFromDefaultClassLoader( EXAMPLE_FOLDER+fileName ) ) {
            String documentContent = StreamIO.readString( documentExampleIS );
            FileIO.writeString( documentContent, documentExample );
        }
        // create doc config
        File sourceFolder = new File( context.getProjectDir(), "src/main/java" );
        log.info( "sourceFolder : {}, mk parent? : {}", sourceFolder.getCanonicalPath(), sourceFolder.mkdirs() );
        File resourceFolder = new File( context.getProjectDir(), "src/main/resources" );
        log.info( "resourceFolder : {}, mk parent? : {}", resourceFolder.getCanonicalPath(), resourceFolder.mkdirs() );
        DocConfigGenerator javaGenerator = new DocConfigGenerator( context );
        Properties generatorProps = new Properties();
        javaGenerator.init( sourceFolder, context.getDocConfigPackage()+"."+context.getDocConfigClass(), SimpleJavaGenerator.STYLE_CLASS, generatorProps);
        javaGenerator.generate();
        javaGenerator.write();
        // create examples
        Template docExampleTemplate = configuration.getTemplate( "DocHelperExample.ftl" );
        File docExampleFile = new File( new File( sourceFolder, context.getDocConfigPackage().replace( '.', '/' ) ), "DocHelperExample.java" );
        try ( Writer writer = new FileWriter( docExampleFile) ) {
            docExampleTemplate.process( data, writer );
        }
        if ( context.getModules().contains( "fj-doc-mod-fop" ) ) {
            String fopConfigName = "fop-config.xml";
            File fopConfig = new File( new File( resourceFolder, context.getArtificatIdForFolder() ),  fopConfigName );
            try ( InputStream fopConfigExampleIS = ClassHelper.loadFromDefaultClassLoader( EXAMPLE_FOLDER+fopConfigName ) ) {
                String fopConfigContent = StreamIO.readString( fopConfigExampleIS );
                FileIO.writeString( fopConfigContent, fopConfig );
            }
        }
    }

    public static boolean addVenusToMavenProject( VenusContext context ) {
        return SafeFunction.get( () -> {
            File pomFile = new File( context.getProjectDir(), "pom.xml" );
            log.info( "project dir : {}", context.getProjectDir().getCanonicalPath() );
            if ( pomFile.exists() ) {
                addExtensionList( pomFile, context );
                if ( context.isAddDocFacace() ) {
                    addDocFacade( context );
                    log.info( "Generation complete:\n{}\n* For usage open the example main() : {} *\n{}", LINE, context.getDocConfigPackage()+"."+context.getDocConfigClass()+"Example", LINE );
                    log.info( "for documentation refer to https://github.com/fugerit-org/fj-doc/blob/main/fj-doc-maven-plugin/README.md" );
                }
            } else {
                addErrorAndLog( String.format( "No pom file in project dir : %s", pomFile.getCanonicalPath() ), context );
                return false;
            }
            return true;
        } );
    }

}

class DocConfigGenerator extends SimpleJavaGenerator {

    private VenusContext context;

    public DocConfigGenerator(VenusContext context) {
        this.context = context;
    }

    @Override
    public void init(File sourceFolder, String fullObjectBName, String javaStyle, Properties config) throws ConfigException {
        super.init(sourceFolder, fullObjectBName, javaStyle, config);
        this.setNoCustomComment( true );
        this.getImportList().add( FreemarkerDocProcessConfig.class.getName() );
        this.getImportList().add( FreemarkerDocProcessConfigFacade.class.getName() );
    }

    @Override
    public void generateBody() throws IOException {
        this.println( "     private FreemarkerDocProcessConfig docProcessConfig = FreemarkerDocProcessConfigFacade.loadConfigSafe( \"cl://"+this.context.getResourcePathFmConfigXml()+"\" );" );
        this.println();
        this.println( "     public FreemarkerDocProcessConfig getDocProcessConfig() { return this.docProcessConfig; }" );
        this.println();
    }

}