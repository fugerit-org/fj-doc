package org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.util.PropsIO;

import java.util.Properties;

@Slf4j
public class VersionCheck {

    private VersionCheck() {}

    public static final String LATEST = "latest";

    public static final String FALLBACK = "8.6.2";

    public static String findFromPropsFile( String path ) {
        Properties props = PropsIO.loadFromClassLoaderSafe( path );
        if ( props != null && props.containsKey( "version" ) ) {
            String latest = props.getProperty( "version" );
            log.info( "findVersion() latest : {}", latest );
            return latest;
        } else {
            log.info( "findVersion() fallback : {}", FALLBACK );
            return FALLBACK;
        }
    }

    public static String findVersion(  String version ) {
        if ( version == null || version.equals(LATEST) ) {
            return findFromPropsFile( "META-INF/maven/org.fugerit.java/fj-doc-maven-plugin/pom.properties" );
        } else {
            log.info( "findVersion() input : {}", version );
            return version;
        }
    }

    public static int convertVersionPart( String versionPart ) {
        if (  versionPart.contains( "-" ) ) {
            return Integer.parseInt( versionPart.substring( 0, versionPart.indexOf( "-" ) ) );
        } else {
            return Integer.parseInt( versionPart );
        }
    }

    public static boolean isMajorThan( String v1, String v2 ) {
        String[] split1 = v1.split( "\\." );
        String[] split2 = v2.split( "\\." );
        for ( int i = 0 ; i < split1.length ; i++ ) {
            if ( convertVersionPart( split1[i] ) > convertVersionPart( split2[i]) ) {
                return true;
            }
        }
        return false;
    }

}
