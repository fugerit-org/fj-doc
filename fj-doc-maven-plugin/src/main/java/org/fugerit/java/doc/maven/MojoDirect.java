package org.fugerit.java.doc.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.CollectionUtils;
import org.fugerit.java.doc.lib.direct.VenusDirectFacade;
import org.fugerit.java.doc.lib.direct.config.VenusDirectConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Mojo( name = "direct" )
public class MojoDirect extends AbstractMojo {

    @Parameter(property = "configPath", required = false)
    protected String configPath;

    @Parameter(property = "outputAll", defaultValue = "true", required = false)
    protected boolean outputAll;

    @Parameter(property = "outputId")
    protected List<String> outputId;

    public static void checkConfiguration( File configFile, boolean outputAll, List<String> outputId ) throws MojoExecutionException {
        if ( !configFile.exists() ) {
            throw new MojoExecutionException( String.format( "Config file does not exist : %s", configFile.getAbsolutePath() ) );
        }
        if ( outputAll && !CollectionUtils.isEmpty( outputId ) ) {
            throw new MojoExecutionException( String.format( "If outputAll is set to 'true' no outputId can be provided : %s", outputId ) );
        }
        if ( !outputAll && CollectionUtils.isEmpty( outputId ) ) {
            throw new MojoExecutionException( String.format( "If outputAll is set to 'false' at least one outputId should be provided : %s", outputId ) );
        }
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File configFile = new File( this.configPath );
        this.getLog().info( String.format( "Direct config file : %s", configFile.getAbsolutePath() ) );
        checkConfiguration( configFile, this.outputAll, this.outputId );
        SafeFunction.apply( () -> {
            try (Reader reader = new InputStreamReader( new FileInputStream( configFile ))) {
                VenusDirectConfig config = VenusDirectFacade.readConfig( reader );
                if ( this.outputAll ) {
                    VenusDirectFacade.handleAllOutput( config );
                } else {
                    this.outputId.forEach( id -> VenusDirectFacade.handleOutput( config, id ) );
                }
            }
        } );
    }

}
