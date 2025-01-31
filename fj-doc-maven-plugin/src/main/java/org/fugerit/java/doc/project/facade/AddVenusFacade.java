package org.fugerit.java.doc.project.facade;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.javagen.SimpleJavaGenerator;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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

    private static void copyTemplate( String fileName, File templateDir ) throws IOException {
        File documentExample = new File( templateDir, fileName );
        try ( InputStream documentExampleIS = ClassHelper.loadFromDefaultClassLoader( EXAMPLE_FOLDER+fileName ) ) {
            String documentContent = StreamIO.readString( documentExampleIS );
            FileIO.writeString( documentContent, documentExample );
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
        copyTemplate( "document.ftl", templateDir );
        if ( context.getModules().contains( "fj-doc-base-json" ) ) {
            copyTemplate( "document-json.ftl", templateDir );
        }
        if ( context.getModules().contains( "fj-doc-base-yaml" ) ) {
            copyTemplate( "document-yaml.ftl", templateDir );
        }
        if ( context.getModules().contains( "fj-doc-base-kotlin" ) ) {
            copyTemplate( "document-kotlin.kts", templateDir );
            copyTemplate( "document-kotlin.ftl", templateDir );
        }
        // create doc config
        File sourceFolder = context.getMainJavaFolder();
        log.info( "sourceFolder : {}, mk parent? : {}", sourceFolder.getCanonicalPath(), sourceFolder.mkdirs() );
        File resourceFolder = new File( context.getProjectDir(), "src/main/resources" );
        log.info( "resourceFolder : {}, mk parent? : {}", resourceFolder.getCanonicalPath(), resourceFolder.mkdirs() );
        DocConfigGenerator javaGenerator = new DocConfigGenerator( context );
        Properties generatorProps = new Properties();
        javaGenerator.init( sourceFolder, context.getDocConfigPackage()+"."+context.getDocConfigClass(), SimpleJavaGenerator.STYLE_CLASS, generatorProps);
        if ( javaGenerator.getJavaFile().exists() ) {
            log.info( "DocHelper generator skip, already exists : {}", javaGenerator.getJavaFile().getCanonicalPath() );
        } else {
            javaGenerator.generate();
            javaGenerator.write();
        }
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

    public static boolean addToProject( VenusContext context ) {
        return SafeFunction.get( () -> {
            File pomFile = new File( context.getProjectDir(), "pom.xml" );
            if ( pomFile.exists() ) {
                return addVenusToMavenProject( pomFile, context );
            } else {
                File gradleFile = new File( context.getProjectDir(), "build.gradle.kts" );
                if ( gradleFile.exists() ) {
                    return addVenusToGradleKtsProject( gradleFile, context );
                } else {
                    addErrorAndLog( String.format( "No pom or gradle file in project dir : %s", pomFile.getCanonicalPath() ), context );
                    return false;
                }
            }
        } );
    }

    public static boolean addVenusToMavenProject( File pomFile, VenusContext context ) {
        return SafeFunction.get( () -> {
            log.info( "maven project dir : {}", context.getProjectDir().getCanonicalPath() );
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
            return true;
        } );
    }

    public static boolean addVenusToGradleKtsProject( File gradleFile, VenusContext context ) {
        return SafeFunction.get( () -> {
            log.info( "gradle project dir : {}", context.getProjectDir().getCanonicalPath() );
            addExtensionGradleKtsList( gradleFile, context );
            if ( context.isAddDocFacace() ) {
                if ( context.getMavenModel() == null ) {
                    Model model = new Model();
                    model.setGroupId(context.getGroupIdOverride());
                    model.setArtifactId(context.getArtifactIdOverride());
                    context.setMavenModel(model);
                }
                addDocFacade( context );
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