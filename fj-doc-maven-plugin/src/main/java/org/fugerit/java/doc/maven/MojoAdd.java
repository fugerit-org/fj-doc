package org.fugerit.java.doc.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.fugerit.java.doc.project.facade.AddVenusFacade;
import org.fugerit.java.doc.project.facade.VenusContext;

import java.io.File;

@Mojo( name = "add" )
public class MojoAdd extends AbstractMojo {

    @Parameter(property = "version", defaultValue = "8.5.2", required = true)
    protected String version;

    @Parameter(property = "extensions", defaultValue = "base,freemarker", required = true)
    protected String extensions;

    @Parameter(property = "projectFolder", defaultValue = ".", required = true)
    protected String projectFolder;

    @Parameter(property = "addDocFacade", defaultValue = "true", required = true)
    protected boolean addDocFacade;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        VenusContext context = new VenusContext( new File( this.projectFolder ), this.version ,this.extensions );
        context.setAddDocFacace( this.addDocFacade );
        this.getLog().info( String.format( "add execute() context : %s", context ) );
        AddVenusFacade.addVenusToMavenProject( context );
    }

}
