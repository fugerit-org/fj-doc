package org.fugerit.java.doc.project.facade;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.fugerit.java.core.io.helper.HelperIOException;

import java.io.IOException;
import java.io.StringReader;

public class PluginUtils {

    private PluginUtils() {}

    public static Object getPluginConfiguration( String xml ) throws IOException {
        return HelperIOException.get( () -> {
            try ( StringReader sr = new StringReader( xml ) ) {
                Xpp3Dom dom = Xpp3DomBuilder.build( sr );
                return dom;
            }
        });
    }

}
