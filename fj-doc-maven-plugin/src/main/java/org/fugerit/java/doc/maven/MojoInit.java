package org.fugerit.java.doc.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.fugerit.java.core.function.UnsafeVoid;
import org.fugerit.java.doc.project.facade.FlavourFacade;
import org.fugerit.java.doc.project.facade.FlavourContext;
import org.fugerit.java.doc.project.facade.ModuleFacade;
import org.fugerit.java.doc.project.facade.VersionCheck;

import java.io.File;

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

    @Parameter(property = "flavour", defaultValue = "vanilla", required = true)
    protected String flavour;

    @Parameter(property = "flavourVersion", required = false)
    protected String flavourVersion;

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
            this.apply( () -> {
                this.projectFolder = initFolder.getCanonicalPath();
                FlavourContext context = new FlavourContext( new File( this.projectFolder ), this.groupId, this.artifactId, this.projectVersion, this.javaRelease, this.flavour );
                context.setModules( ModuleFacade.toModuleList( this.extensions ) );
                context.setAddLombok( this.addLombok );
                context.setFlavourVersion( this.flavourVersion );
                context.setVersion( VersionCheck.findVersion( this.version ) );
                context.setExtensions( this.extensions );
                this.getLog().info( String.format( "flavour context : %s", context ) );
                String actualVersion = FlavourFacade.initProject( context );
                if ( FlavourFacade.FLAVOUR_DIRECT.equals( actualVersion ) ) {
                    super.addDirectPlugin = Boolean.TRUE;
                    // for 'direct' flavour, parameter 'addDocFacade' set to 'false'
                    // https://github.com/fugerit-org/fj-doc/issues/413
                    super.addDocFacade = Boolean.FALSE;
                }
            } );
            super.groupIdOverride = this.groupId;
            super.artifactIdOverride = this.artifactId;
            super.execute();
        }

    }

}
