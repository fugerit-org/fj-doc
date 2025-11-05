package org.fugerit.java.doc.mod.fop.utils;

import org.apache.fop.Version;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.mvn.MavenProps;

public class ApacheFopUtils {

    private ApacheFopUtils() {}

    private static final String APACHE_FOP_VERSION = StringUtils.valueWithDefault(
            Version.getVersion(),
            StringUtils.valueWithDefault(
                    MavenProps.getProperty( "org.apache.xmlgraphics", "fop", MavenProps.VERSION ),
                    "NA"    // if nothing else worked
            )
    );

    public static String getApacheFOPVersion() {
        return APACHE_FOP_VERSION;
    }

}
