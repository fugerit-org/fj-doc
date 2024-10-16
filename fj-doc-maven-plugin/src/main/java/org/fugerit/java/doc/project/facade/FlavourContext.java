package org.fugerit.java.doc.project.facade;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

@Slf4j
@ToString
@RequiredArgsConstructor
public class FlavourContext {

    @Getter @NonNull
    private File projectFolder;

    @Getter @NonNull
    private String groupId;

    @Getter @NonNull
    private String artifactId;

    @Getter @NonNull
    private String projectVersion;

    @Getter @NonNull
    private String javaRelease;

    @Getter @NonNull
    private String flavour;

    @Getter @Setter
    private List<String> modules;

    @Getter @Setter
    private boolean addLombok;

    @Getter @Setter
    private String flavourVersion;

    @Getter @Setter
    private String version;

    @Getter @Setter
    private String extensions;

    private String toClassName( String base, String splitString ) {
        StringBuilder buf = new StringBuilder();
        String[] split = base.split( splitString );
        for ( String part : split ) {
            buf.append( part.substring( 0, 1 ).toUpperCase()+part.substring( 1 ) );
        }
        return buf.toString();
    }

    public String getArtifactIdAsClassName() {
        String res = toClassName( this.artifactId, "-" );
        return toClassName( res, "\\." );
    }

}
