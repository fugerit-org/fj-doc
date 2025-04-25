package org.fugerit.java.doc.project.facade;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.fugerit.java.core.io.helper.HelperIOException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

public class PluginUtils {

    public static final String GOAL_DIRECT = "direct";

    public static final String FJ_DOC_MAVEN_PLUGIN = "fj-doc-maven-plugin";

    private PluginUtils() {}

    public static Object getPluginConfiguration( String xml ) throws IOException {
        return HelperIOException.get( () -> {
            try ( StringReader sr = new StringReader( xml ) ) {
                return Xpp3DomBuilder.build( sr );
            }
        });
    }

    public static Plugin findOrCreatePLugin( Model model ) {
        Plugin plugin = null;
        Build build = getBuild( model );
        for ( Plugin current : build.getPlugins() ) {
            if ( FJ_DOC_MAVEN_PLUGIN.equals( current.getArtifactId() ) ) {
                plugin = current;
            }
        }
        if ( plugin == null ) {
            plugin = new Plugin();
            plugin.setGroupId( VenusConsts.GROUP_ID );
            plugin.setArtifactId( FJ_DOC_MAVEN_PLUGIN );
            plugin.setVersion( "${"+VenusConsts.KEY_VERSION+"}" );
            build.getPlugins().add( plugin );
        }
        return plugin;
    }

    public static PluginExecution createPluginExecution(String id, String phase, String... goal ) {
        PluginExecution execution = new PluginExecution();
        execution.setId( id );
        execution.setPhase( phase );
        execution.setGoals(Arrays.asList( goal ));
        return execution;
    }

    public static Build getBuild(Model model) {
        Build build = model.getBuild();
        if ( build == null ) {
            build = new Build();
            model.setBuild( build );
        }
        return build;
    }

}
