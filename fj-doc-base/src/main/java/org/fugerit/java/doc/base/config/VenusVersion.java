package org.fugerit.java.doc.base.config;

import org.fugerit.java.core.util.mvn.FJCoreMaven;
import org.fugerit.java.core.util.mvn.MavenProps;

import java.util.Optional;

public class VenusVersion {

    private VenusVersion() {}

    public static final String FJ_DOC_BASE_ARTIFACT_ID = "fj-doc-base";

    public static final String NOT_AVAILABLE = "NA";

    public static final String VENUS_CREATOR = String.format( "%s (https://github.com/fugerit-org/fj-doc)", DocConfig.FUGERIT_VENUS_DOC );

    public static final String VENUS_PRODUCER_FORMAT = "%s (%s) over %s (%s)";

    public static Optional<String> getFjDocCoreVersion() {
        return getFjDocModuleVersion( FJ_DOC_BASE_ARTIFACT_ID );
    }

    public static String getFjDocCoreVersionS() {
        return getFjDocCoreVersion().orElse( NOT_AVAILABLE );
    }

    public static Optional<String> getFjDocModuleVersion( String artifactId ) {
        return MavenProps.getPropertyOptional(FJCoreMaven.FJ_CORE_GROUP_ID, artifactId, MavenProps.VERSION );
    }

    public static String getFjDocModuleVersionS( String artifactId ) {
        return getFjDocModuleVersion( artifactId ).orElse( NOT_AVAILABLE );
    }

}
