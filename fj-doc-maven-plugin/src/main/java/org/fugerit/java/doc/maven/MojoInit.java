package org.fugerit.java.doc.maven;

import org.apache.maven.model.Model;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.fugerit.java.core.function.UnsafeVoid;
import org.maxxq.maven.dependency.ModelIO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Mojo( name = "init", requiresProject = false, defaultPhase = LifecyclePhase.NONE )
public class MojoInit extends MojoAdd {

    @Parameter(property = "groupId", required = true)
    protected String groupId;

    @Parameter(property = "artifactId", required = true)
    protected String artifactId;

    @Parameter(property = "projectVersion", defaultValue = "1.0.0-SNAPSHOT", required = true)
    protected String projectVersion;

    @Parameter(property = "javaRelease", defaultValue = "21", required = true)
    protected String javaRelease;

    protected String baseInitFolder;

    public MojoInit() {
        this.baseInitFolder = ".";
    }

    public void apply(UnsafeVoid<Exception> fun) throws MojoFailureException {
        try {
            fun.apply();
        } catch (Exception e) {
            throw new MojoFailureException( String.format( "Project init failed: %s", e.getMessage() ), e );
        }
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File initFolder = new File( this.baseInitFolder, this.artifactId );
        if ( initFolder.exists() ) {
            throw new MojoFailureException( String.format( "Folder %s already exists.", initFolder.getAbsolutePath() ) );
        } else {
            this.getLog().info( String.format( "project folder %s -> %s", initFolder.getAbsolutePath(), initFolder.mkdir() ) );
            File pomFile = new File( initFolder, "pom.xml" );
            Model model = new Model();
            model.setGroupId( this.groupId );
            model.setArtifactId( this.artifactId );
            model.setVersion( this.projectVersion );
            model.setModelVersion( "4.0.0" );
            Properties props = new Properties();
            props.setProperty( "maven.compiler.release", this.javaRelease );
            props.setProperty( "project.build.sourceEncoding", StandardCharsets.UTF_8.name() );
            model.setProperties( props );
            this.apply( () -> {
                this.projectFolder = initFolder.getCanonicalPath();
                ModelIO modelIO = new ModelIO();
                try (OutputStream pomStream = new FileOutputStream( pomFile ) ) {
                    modelIO.writeModelToStream( model, pomStream );
                }
            } );
        }
        super.execute();
    }

}
