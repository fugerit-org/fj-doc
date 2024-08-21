package org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
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

    private static void addCurrentModule(String currentModule, List<Dependency> dependencies) {
        Dependency d = new Dependency();
        d.setArtifactId( currentModule );
        d.setGroupId( GROUP_ID );
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
        for ( String currentModule :  Arrays.asList( extensionList ) ) {
            String moduleName = ModuleFacade.toModuleName( currentModule );
            log.info( "Adding module : {}", moduleName );
            if ( ModuleFacade.isModuleSupported( moduleName ) ) {
                addCurrentModule( moduleName, model.getDependencies() );
                context.getModules().add( currentModule );
            } else {
                String message = String.format( "Module not supported : %s", moduleName );
                log.warn( "{}, supported modules are : ", message );
                ModuleFacade.getModules().forEach( log::warn );
                throw new ConfigRuntimeException( message );
            }
        }
        log.info( "end dependencies size : {}", model.getDependencies().size() );
        try (OutputStream pomStream = new FileOutputStream( pomFile ) ) {
            modelIO.writeModelToStream( model, pomStream );
        }
    }

}
