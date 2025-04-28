package org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.*;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.maxxq.maven.dependency.ModelIO;

import java.io.*;
import java.util.*;

@Slf4j
public class BasicVenusFacade {

    protected BasicVenusFacade() {}

    protected static final String GROUP_ID = VenusConsts.GROUP_ID;

    protected static final String KEY_VERSION = VenusConsts.KEY_VERSION;

    private static final String PROJECT_LOMBOK = "org.projectlombok:lombok";

    private static void addOrOverwrite( List<Dependency> deps, Dependency d ) {
        Iterator<Dependency> it = deps.iterator();
        while ( it.hasNext() ) {
            Dependency dep = it.next();
            if ( ( dep.getGroupId()+":"+dep.getArtifactId() ).equals( d.getGroupId()+":"+d.getArtifactId() ) ) {
                it.remove();
            }
        }
        deps.add( d );
    }

    private static void addCurrentModule( VenusContext context, String currentModule, List<Dependency> dependencies)  {
        addCurrentModule( context, currentModule, dependencies, null );
    }

    private static void addCurrentModule( VenusContext context, String currentModule, List<Dependency> dependencies, String version)  {
        Dependency d = new Dependency();
        d.setArtifactId( currentModule );
        d.setGroupId( GROUP_ID );
        if (StringUtils.isNotEmpty( context.getAddExclusions() ) ) {
            for ( String current : context.getAddExclusions().split( "," ) ) {
                String[] parts = current.split( ":" );
                Exclusion e = new Exclusion();
                e.setGroupId( parts[0] );
                e.setArtifactId( parts[1] );
                d.getExclusions().add( e );
                if ( version != null ) {
                    d.setVersion(version);
                }
            }
        }
        addOrOverwrite( dependencies, d );
    }

    private static Model readModel(ModelIO modelIO, File pomFile ) throws IOException {
        try (InputStream pomStream = new FileInputStream( pomFile ) ) {
            return modelIO.getModelFromInputStream( pomStream );
        }
    }

    protected static void addErrorAndLog( String message, VenusContext context ) {
        log.warn( message );
        context.getErrors().add( message );
    }

    protected static Dependency createDependency( String groupId, String artifactId, String version, String scope ) {
        Dependency dependency = new Dependency();
        dependency.setGroupId( groupId );
        dependency.setArtifactId( artifactId );
        dependency.setVersion( version );
        dependency.setScope( scope );
        return dependency;
    }

    protected static void addJunit5( Model model, VenusContext context ) throws IOException  {
        if ( context.isAddJunit5() ) {
            Dependency junit5 = createDependency( "org.junit.jupiter", "junit-jupiter", null, "test" );
            addOrOverwrite( model.getDependencies(), junit5 );
        } else {
            log.debug( "skip addJunit5" );
        }
    }

    protected static void addLombok( Model model, VenusContext context ) throws IOException  {
        if ( context.isAddLombok() ) {
            Dependency lombok = createDependency( "org.projectlombok", "lombok", null, "provided" );
            addOrOverwrite( model.getDependencies(), lombok );
            Dependency slf4jSimple = createDependency( "org.slf4j", "slf4j-simple", null, "test" );
            addOrOverwrite( model.getDependencies(), slf4jSimple );
        } else {
            log.debug( "skip addLombok" );
        }
    }

    protected static void addExtensionList( File pomFile, VenusContext context ) throws IOException  {
        ModelIO modelIO = new ModelIO();
        Model model = readModel( modelIO, pomFile );
        // add pom data
        context.setMavenModel( model );
        if ( model.getDependencyManagement() == null ) {
            model.setDependencyManagement( new DependencyManagement() );
        }
        DependencyManagement dm = model.getDependencyManagement();
        Properties props = model.getProperties();
        props.setProperty( KEY_VERSION, context.getVersion() );
        Dependency fjDocBom = new Dependency();
        fjDocBom.setArtifactId( "fj-doc" );
        fjDocBom.setGroupId( GROUP_ID );
        fjDocBom.setVersion( VenusConsts.KEY_VERSION_VAR );
        fjDocBom.setType( "pom" );
        fjDocBom.setScope( "import" );
        addOrOverwrite( dm.getDependencies(), fjDocBom );
        log.info( "start dependencies size : {}, version : {}", model.getDependencies().size(), context.getVersion() );
        // check if dependencies are already present
        model.getDependencies().forEach( d -> checkDependencies( context.isForce(), d ) );
        // configure dependencies
        List<String> moduleList = ModuleFacade.toModuleListOptimizedOrder( context.getExtensions() );
        log.info( "moduleList : {}", moduleList );
        for ( String currentModule :  moduleList ) {
            String moduleName = ModuleFacade.toModuleName( currentModule );
            log.info( "Adding module : {}", moduleName );
            // no need to check if module is supported , ModuleFacade.toModuleList() already does it
            addCurrentModule( context, moduleName, model.getDependencies() );
            context.getModules().add( moduleName );
        }
        // set fj-doc modules on top?
        if ( context.isAddDependencyOnTop() ) {
            model.getDependencies().sort( ( d1, d2 ) -> {
                String artifact1 = d1.getArtifactId();
                String artifact2 = d2.getArtifactId();
                // if both are Venus modules, order is preserved
                if ( artifact1.startsWith( ModuleFacade.MODULE_PREFIX ) && artifact2.startsWith( ModuleFacade.MODULE_PREFIX ) ) {
                    return 0;
                // if the first dependency is a Venus module, it goes first
                } else if ( artifact1.startsWith( ModuleFacade.MODULE_PREFIX ) ) {
                    return -1;
                // otherwise it goes after, order preserved
                } else {
                    return 1;
                }
            } );
        }
        if ( context.isSimpleModel() ) {
            log.warn( "simpleModel true : skipping extra dependencies and plugin" );
            handleSimpleModel( pomFile, context, moduleList );
        } else {
            // addJunit5 parameter
            addJunit5( model, context );
            // addLombok parameter
            addLombok( model, context );
            addVerifyPlugin( context, model );
            addDirectPlugin( context, model );
            log.info( "end dependencies size : {}", model.getDependencies().size() );
            try (OutputStream pomStream = new FileOutputStream( pomFile ) ) {
                modelIO.writeModelToStream( model, pomStream );
            }
        }
    }

    private static void handleSimpleModel( File pomFile, VenusContext context, List<String> moduleList ) throws IOException {
        String pomText = FileIO.readString( pomFile );
        // add property fj-doc-version
        String properties = "</properties>";
        int endPropertiesIndex = pomText.indexOf( properties );
        if ( endPropertiesIndex != -1 ) {
            pomText = String.format( "%s    <fj-doc-version>%s</fj-doc-version>%n    %s", pomText.substring( 0, endPropertiesIndex ), context.getVersion(), pomText.substring( endPropertiesIndex ) );
        } else {
            String modelVersion = "</modelVersion>";
            int endArtifactIdIndex = pomText.indexOf( modelVersion )+modelVersion.length();
            pomText = String.format( "%s%n%n    <properties>%n      <fj-doc-version>%s</fj-doc-version>%n    </properties>%n%s", pomText.substring( 0, endArtifactIdIndex ), context.getVersion(), pomText.substring( endArtifactIdIndex ) );
        }
        // add dependencies
        String dependencies = "</dependencies>";
        int endDependenciesIndex = pomText.indexOf( dependencies );
        StringBuilder builder = new StringBuilder();
        for ( String moduleName : moduleList ) {
            builder.append( String.format( "%n      <dependency>%n        <groupId>org.fugerit.java</groupId>%n        <artifactId>%s</artifactId>%n        <version>${fj-doc-version}</version>%n      </dependency>", moduleName ) );
        }
        pomText = String.format( "%s    %s%n%n    %s", pomText.substring( 0, endDependenciesIndex ), builder, pomText.substring( endDependenciesIndex ) );
        // write pom file
        FileIO.writeString( pomText, pomFile );
        log.warn( "simpleModel enabled, consider adding fj-doc-maven-plugin:verify and other configurations. (https://venusdocs.fugerit.org/guide/#maven-plugin-entry)" );
    }

    private static final String CONST_IMPLEMENTATION = "implementation";

    private static String formatGroovy( String dependency, String version, boolean kts ) {
        if ( kts ) {
            return String.format( "\\(\"%s:%s\"\\)", dependency, version );
        } else {
            return String.format( " '%s:%s'", dependency, version );
        }
    }

    protected static void addExtensionGradleList( File gradleFile, VenusContext context, boolean kts ) throws IOException  {
        // note, this will currently only work for very simple build.gradle.kts files
        String gradleFileContent = FileIO.readString( gradleFile );
        String fjDocVersion = context.getVersion();
        if ( kts ) {
            String valVersion = String.format( "val fjDocVersion = \"%s\"%n%ndependencies", context.getVersion() );
            gradleFileContent = gradleFileContent.replaceFirst( "dependencies", valVersion );
            fjDocVersion = "\\$fjDocVersion";
        }
        List<String> moduleListGradle = ModuleFacade.toModuleListOptimizedOrder( context.getExtensions() );
        Collections.reverse( moduleListGradle );
        log.info( "moduleListGradle : {}", moduleListGradle );
        for ( String currentModule :  moduleListGradle ) {
            String moduleNameGradle = ModuleFacade.toModuleName( currentModule );
            String formatGroovy = formatGroovy( "org.fugerit.java:"+moduleNameGradle, fjDocVersion, kts );
            String currentImplementation = String.format( "implementation %s%n    implementation", formatGroovy );
            log.info( "Adding module to gradle file : {}, substitution : {}", moduleNameGradle, currentImplementation );
            gradleFileContent = gradleFileContent.replaceFirst( CONST_IMPLEMENTATION, currentImplementation );
            context.getModules().add( moduleNameGradle );
        }
        if (context.isAddLombok() ) {
            String lombokVersion = "1.18.36";
            gradleFileContent = gradleFileContent.replaceFirst( CONST_IMPLEMENTATION, String.format( "compileOnly%s%n    %s",  formatGroovy( PROJECT_LOMBOK, lombokVersion, kts ), CONST_IMPLEMENTATION ) );
            gradleFileContent = gradleFileContent.replaceFirst( CONST_IMPLEMENTATION, String.format( "annotationProcessor%s%n    %s",  formatGroovy( PROJECT_LOMBOK, lombokVersion, kts ), CONST_IMPLEMENTATION ) );
            gradleFileContent = gradleFileContent.replaceFirst( CONST_IMPLEMENTATION, String.format( "testCompileOnly%s%n    %s",  formatGroovy( PROJECT_LOMBOK, lombokVersion, kts ), CONST_IMPLEMENTATION ) );
            gradleFileContent = gradleFileContent.replaceFirst( CONST_IMPLEMENTATION, String.format( "testAnnotationProcessor%s%n    %s",  formatGroovy( PROJECT_LOMBOK, lombokVersion, kts ), CONST_IMPLEMENTATION ) );
        }
        FileIO.writeString( gradleFileContent, gradleFile );
    }

    private static void addDirectPlugin(VenusContext context, Model model ) throws IOException {
        // addDirectPlugin?
        if ( context.isAddDirectPlugin() ) {
            if (context.isDirectPluginNotAvailable()) {
                log.warn("addDirectPlugin skipped, version {} has been selected, minimum required version is : {}", context.getVersion(), VenusContext.VERSION_NA_DIRECT_PLUGIN);
            } else {
                if ( context.isAddVerifyPlugin() ) {
                    addVerifyPluginExecution( model, "freemarker-verify-direct",
                            "src/main/resources/"+context.getVenusDirectConfig()+"/template",
                            "freemarker-syntax-verify-direct-report");
                }
                String featureId = "direct";
                FeatureFacade.copyFeatureList( context.getProjectDir(), featureId);
                FeatureFacade.processFeature( context, featureId );
                log.info("addDirectPlugin true, version {} has been selected, minimum required version is : {}", context.getVersion(), VenusContext.VERSION_NA_DIRECT_PLUGIN);
                Plugin plugin = PluginUtils.findOrCreatePLugin( model );
                // configure dependencies
                List<String> moduleList = ModuleFacade.toModuleListOptimizedOrder( context.getExtensions() );
                for ( String currentModule :  moduleList ) {
                    String moduleName = ModuleFacade.toModuleName( currentModule );
                    List<Dependency> dependencies = plugin.getDependencies();
                    if ( dependencies == null ) {
                        dependencies = new ArrayList<>();
                    }
                    addCurrentModule( context, moduleName, dependencies, VenusConsts.KEY_VERSION_VAR );
                }
                PluginExecution execution = PluginUtils.createPluginExecution(
                        "venus-direct", LifecyclePhase.COMPILE.id(), PluginUtils.GOAL_DIRECT );
                plugin.getExecutions().add( execution );
                String xml = "<configuration>\n" +
                        "    <configPath>${project.basedir}/src/main/resources/"+context.getVenusDirectConfig()+"/venus-direct-config.yaml</configPath>\n" +
                        "    <outputAll>true</outputAll>\n" +
                        "    <directEnv>\n" +
                        "        <projectBasedir>${project.basedir}</projectBasedir>\n" +
                        "    </directEnv>\n" +
                        "</configuration>";
                execution.setConfiguration( PluginUtils.getPluginConfiguration( xml ) );
            }
        } else {
            log.info( "addDirectPlugin : false" );
        }
    }

    private static void addVerifyPluginExecution( Model model, String id, String templateBaseDir, String reportOutputFolder ) throws IOException {
        log.info( "addVerifyPluginExecution id:{}, templateBaseDir:{}", id, templateBaseDir );
        Plugin plugin = PluginUtils.findOrCreatePLugin( model );
        PluginExecution execution = PluginUtils.createPluginExecution(
                id, LifecyclePhase.COMPILE.id(), LifecyclePhase.VERIFY.id() );
        plugin.getExecutions().add( execution );
        String xml = "<configuration>\n" +
                "      <templateBasePath>${project.basedir}/"+templateBaseDir+"</templateBasePath>\n" +
                "      <generateReport>true</generateReport>\n" +
                "      <failOnErrors>true</failOnErrors>\n" +
                "      <reportOutputFolder>${project.build.directory}/"+reportOutputFolder+"</reportOutputFolder>\n" +
                "    </configuration>";
        execution.setConfiguration( PluginUtils.getPluginConfiguration( xml ) );
    }

    private static void addVerifyPlugin(VenusContext context, Model model ) throws IOException {
        // addVerifyPlugin?
        if ( context.isAddVerifyPlugin() && context.isAddDocFacace() ) {
            if ( context.isVerifyPluginNotAvailable() ) {
                log.warn( "addVerifyPlugin skipped, version {} has been selected, minimum required version is : {}", context.getVersion(), VenusContext.VERSION_NA_VERIFY_PLUGIN );
            } else {
                log.info( "addVerifyPlugin true, version {} has been selected, minimum required version is : {}", context.getVersion(), VenusContext.VERSION_NA_VERIFY_PLUGIN );
                addVerifyPluginExecution( model, "freemarker-verify",
                        "src/main/resources/"+context.getArtificatIdForFolder()+"/template",
                        "freemarker-syntax-verify-report" );
            }
        } else {
            log.info( "addVerifyPlugin : false" );
        }
    }

    public static void checkDependencies( boolean force, Dependency d ) {
        if ( d.getGroupId().equals( GROUP_ID ) && d.getArtifactId().startsWith( "fj-doc") ) {
            log.warn( "fj-doc dependency found : {}", d );
            if ( !force ) {
                throw new ConfigRuntimeException( "Fugerit Venus Doc already configured" );
            }
        }
    }

}
