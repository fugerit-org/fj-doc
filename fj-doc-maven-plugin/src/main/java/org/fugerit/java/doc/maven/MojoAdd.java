package org.fugerit.java.doc.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.fugerit.java.doc.project.facade.AddVenusFacade;
import org.fugerit.java.doc.project.facade.VenusContext;
import org.fugerit.java.doc.project.facade.VersionCheck;

import java.io.File;

@Mojo( name = "add" )
public class MojoAdd extends AbstractMojo {

    @Parameter(property = "version", defaultValue = "latest", required = true)
    protected String version;

    @Parameter(property = "extensions", defaultValue = "base,freemarker", required = true)
    protected String extensions;

    @Parameter(property = "projectFolder", defaultValue = ".", required = true)
    protected String projectFolder;

    @Parameter(property = "addDocFacade", defaultValue = "true", required = true)
    protected boolean addDocFacade;

    @Parameter(property = "force", defaultValue = "false", required = true)
    protected boolean force;

    @Parameter(property = "excludeXmlApis", defaultValue = "false", required = true)
    protected boolean excludeXmlApis;

    @Parameter(property = "addExclusions", required = false)
    protected String addExclusions;

    @Parameter(property = "addVerifyPlugin", defaultValue = "true", required = true)
    protected boolean addVerifyPlugin;

    @Parameter(property = "addJunit5", defaultValue = "false", required = false)
    protected boolean addJunit5;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String foundVersion = VersionCheck.findVersion( this.version );
        this.getLog().info( String.format( "version parameter : %s found : %s", this.version, foundVersion ) );
        VenusContext context = new VenusContext( new File( this.projectFolder ), foundVersion ,this.extensions );
        context.setAddDocFacace( this.addDocFacade );
        context.setForce( this.force );
        context.setAddExclusions( addExclusions );
        context.setExcludeXmlApis( this.excludeXmlApis );
        context.setAddVerifyPlugin( this.addVerifyPlugin );
        context.setAddJunit5( this.addJunit5 );
        this.getLog().info( String.format( "add execute() context : %s", context ) );
        AddVenusFacade.addVenusToMavenProject( context );
    }

}
