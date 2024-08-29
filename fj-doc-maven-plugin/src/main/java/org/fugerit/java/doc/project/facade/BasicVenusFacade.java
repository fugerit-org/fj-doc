package org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.*;
import org.apache.maven.plugin.lifecycle.Execution;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.io.helper.HelperIOException;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.freemarker.tool.FreeMarkerTemplateSyntaxVerifier;
import org.maxxq.maven.dependency.ModelIO;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class BasicVenusFacade {

    protected BasicVenusFacade() {}

    protected static final String GROUP_ID = "org.fugerit.java";

    protected static final String KEY_VERSION = "fj-doc-version";

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

    protected static void addExtensionList( File pomFile, VenusContext context ) throws IOException  {
        String[] extensionList = context.getExtensions().split( "," );
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
        fjDocBom.setVersion( "${"+KEY_VERSION+"}" );
        fjDocBom.setType( "pom" );
        fjDocBom.setScope( "import" );
        addOrOverwrite( dm.getDependencies(), fjDocBom );
        log.info( "start dependencies size : {}, version : {}", model.getDependencies().size(), context.getVersion() );
        // check if dependencies are already present
        model.getDependencies().forEach( d -> checkDependencies( context.isForce(), d ) );
        // configure dependencies
        for ( String currentModule :  Arrays.asList( extensionList ) ) {
            String moduleName = ModuleFacade.toModuleName( currentModule );
            log.info( "Adding module : {}", moduleName );
            if ( ModuleFacade.isModuleSupported( moduleName ) ) {
                addCurrentModule( context, moduleName, model.getDependencies() );
                context.getModules().add( currentModule );
            } else {
                String message = String.format( "Module not supported : %s", moduleName );
                log.warn( "{}, supported modules are : ", message );
                ModuleFacade.getModules().forEach( log::warn );
                throw new ConfigRuntimeException( message );
            }
        }
        log.info( "end dependencies size : {}", model.getDependencies().size() );
        addPlugin( context, model );
        try (OutputStream pomStream = new FileOutputStream( pomFile ) ) {
            modelIO.writeModelToStream( model, pomStream );
        }
    }

    private static void addPlugin( VenusContext context, Model model ) throws IOException {
        // addVerifyPlugin?
        if ( context.isAddVerifyPlugin() ) {
            if ( context.isVerifyPluginNotAvailable() ) {
                log.warn( "addVerifyPlugin skipped, version {} has been selected, minimum required version is : {}", context.getVersion(), VenusContext.VERSION_NA_VERIFY_PLUGIN );
            } else {
                log.info( "addVerifyPlugin true, version {} has been selected, minimum required version is : {}", context.getVersion(), VenusContext.VERSION_NA_VERIFY_PLUGIN );
                Build build = model.getBuild();
                if ( build == null ) {
                    build = new Build();
                    model.setBuild( build );
                }
                List<Plugin> plugins = model.getBuild().getPlugins();
                Plugin plugin = new Plugin();
                plugin.setGroupId( GROUP_ID );
                plugin.setArtifactId( "fj-doc-maven-plugin" );
                plugin.setVersion( "${"+KEY_VERSION+"}" );
                PluginExecution execution = new PluginExecution();
                execution.setId( "freemarker-verify" );
                execution.setPhase( "compile" );
                execution.addGoal( "verify" );
                plugin.getExecutions().add( execution );
                String xml = "<configuration>\n" +
                        "      <templateBasePath>${project.basedir}/src/main/resources/"+context.getArtificatIdForFolder()+"/template</templateBasePath>\n" +
                        "      <generateReport>true</generateReport>\n" +
                        "      <failOnErrors>true</failOnErrors>\n" +
                        "      <reportOutputFolder>${project.build.directory}/freemarker-syntax-verify-report</reportOutputFolder>\n" +
                        "    </configuration>";
                HelperIOException.apply( () -> {
                    try ( StringReader sr = new StringReader( xml ) ) {
                        Xpp3Dom dom = Xpp3DomBuilder.build( sr );
                        plugin.setConfiguration( dom );
                    }
                });
                plugins.add( plugin );
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
