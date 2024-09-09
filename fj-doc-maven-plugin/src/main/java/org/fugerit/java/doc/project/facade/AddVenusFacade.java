package org.fugerit.java.doc.project.facade;

import freemarker.template.*;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.javagen.SimpleJavaGenerator;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;

import java.io.*;
import java.util.*;

@Slf4j
public class AddVenusFacade extends BasicVenusFacade {

    private AddVenusFacade() {}

    private static final String EXAMPLE_FOLDER = "config/example/";

    private static final String LINE = "************************************************************************************************************************";

    private static File toFile( File base, String packageName, String fileName ) {
        File folder = new File( base, packageName.replace( '.', '/' ) );
        File file = new File( folder, fileName );
        boolean mkdirs = Boolean.FALSE;
        if ( !folder.exists() ) {
            mkdirs = folder.mkdirs();
        }
        log.debug( "toFile : [{}], folder create : {} - {}", file.getAbsolutePath(), folder, mkdirs );
        return file;
    }

    private static final String EXT_JAVA = ".java";

    private static void addSampleStructure( VenusContext context, Configuration configuration, Map<String, Object> data ) throws IOException, TemplateException {
        if ( context.isAddJunit5() ) {
            log.debug( "add junit5 structure" );
            String fileBase = "DocHelperTest";
            FreemarkerTemplateFacade.processFile( fileBase+".ftl",
                    toFile( context.getTestJavaFolder(), "test."+context.getDocConfigPackage(), fileBase+EXT_JAVA ),
                    configuration, data );
        } else {
            log.debug( "create DocHelperExample class" );
            String fileBase = "DocHelperExample";
            FreemarkerTemplateFacade.processFile( fileBase+".ftl",
                    toFile( context.getMainJavaFolder(), context.getDocConfigPackage(), fileBase+EXT_JAVA ),
                    configuration, data );
        }
    }

    private static void addDocFacade( VenusContext context ) throws IOException, TemplateException, ConfigException {
        // freemarker configuration
        Configuration configuration = FreemarkerTemplateFacade.getConfiguration();
        // config generation
        Map<String, Object> data = new HashMap<>();
        data.put( "context" , context );
        File fmConfigFile = new File( context.getMainResourcesFolder(), context.getResourcePathFmConfigXml() );
        log.info( "fmConfigFile : {}, mk parent? : {}", fmConfigFile.getCanonicalPath(), fmConfigFile.getParentFile().mkdirs() );
        File templateDir = new File( fmConfigFile.getParentFile(), context.getTemplateSubPath() );
        log.info( "templateDir : {}, mk parent? : {}", templateDir.getCanonicalPath(), templateDir.mkdirs() );
        FreemarkerTemplateFacade.processFile( "fm-doc-process-config-template.ftl", fmConfigFile, configuration, data );
        // copy sample template
        String fileName = "document.ftl";
        File documentExample = new File( templateDir, fileName );
        try ( InputStream documentExampleIS = ClassHelper.loadFromDefaultClassLoader( EXAMPLE_FOLDER+fileName ) ) {
            String documentContent = StreamIO.readString( documentExampleIS );
            FileIO.writeString( documentContent, documentExample );
        }
        // create doc config
        File sourceFolder = context.getMainJavaFolder();
        log.info( "sourceFolder : {}, mk parent? : {}", sourceFolder.getCanonicalPath(), sourceFolder.mkdirs() );
        File resourceFolder = new File( context.getProjectDir(), "src/main/resources" );
        log.info( "resourceFolder : {}, mk parent? : {}", resourceFolder.getCanonicalPath(), resourceFolder.mkdirs() );
        DocConfigGenerator javaGenerator = new DocConfigGenerator( context );
        Properties generatorProps = new Properties();
        javaGenerator.init( sourceFolder, context.getDocConfigPackage()+"."+context.getDocConfigClass(), SimpleJavaGenerator.STYLE_CLASS, generatorProps);
        javaGenerator.generate();
        javaGenerator.write();
        // add sample structure
        addSampleStructure( context, configuration, data );
        if ( context.getModules().contains( "fj-doc-mod-fop" ) ) {
            String fopConfigName = "fop-config.xml";
            File fopConfig = new File( new File( resourceFolder, context.getArtificatIdForFolder() ),  fopConfigName );
            try ( InputStream fopConfigExampleIS = ClassHelper.loadFromDefaultClassLoader( EXAMPLE_FOLDER+fopConfigName ) ) {
                String fopConfigContent = StreamIO.readString( fopConfigExampleIS );
                FileIO.writeString( fopConfigContent, fopConfig );
            }
        }
        // create people data
        String peopleBase = "People";
        FreemarkerTemplateFacade.processFile( peopleBase+".ftl",
                toFile( context.getMainJavaFolder(), context.getDocConfigPackage(), peopleBase+EXT_JAVA ) , data );
        // clear template cache
        configuration.clearTemplateCache();
    }

    public static boolean addVenusToMavenProject( VenusContext context ) {
        return SafeFunction.get( () -> {
            File pomFile = new File( context.getProjectDir(), "pom.xml" );
            log.info( "project dir : {}", context.getProjectDir().getCanonicalPath() );
            if ( pomFile.exists() ) {
                addExtensionList( pomFile, context );
                if ( context.isAddDocFacace() ) {
                    addDocFacade( context );
                    if ( context.isAddJunit5() ) {
                        log.info( "Generation complete:\n{}\n* For usage open the example junit : {} *\n{}", LINE, "test."+context.getDocConfigPackage()+"."+context.getDocConfigClass()+"Test", LINE );
                    } else {
                        log.info( "Generation complete:\n{}\n* For usage open the example main() : {} *\n{}", LINE, context.getDocConfigPackage()+"."+context.getDocConfigClass()+"Example", LINE );
                    }
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