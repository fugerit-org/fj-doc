package org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.VersionCompare;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.PropsIO;
import org.fugerit.java.core.util.mvn.FJCoreMaven;
import org.fugerit.java.core.util.mvn.MavenProps;

import java.util.Properties;

@Slf4j
public class VersionCheck {

    private VersionCheck() {}

    public static final String LATEST = "latest";

    public static final String FALLBACK = "8.6.2";

    public static String findVersion(  String version ) {
        if ( version == null || version.equals(LATEST) ) {
            return StringUtils.valueWithDefault( MavenProps.getProperty(FJCoreMaven.FJ_CORE_GROUP_ID, "fj-doc-maven-plugin", MavenProps.VERSION ), FALLBACK );
        } else {
            log.info( "findVersion() input : {}", version );
            return version;
        }
    }

    public static boolean isMajorThan( String v1, String v2 ) {
        return VersionCompare.isGreaterThan( v1, v2 );
    }

}
