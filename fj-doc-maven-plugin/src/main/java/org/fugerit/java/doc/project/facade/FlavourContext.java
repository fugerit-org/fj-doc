package org.fugerit.java.doc.project.facade;

import lombok.*;

import java.io.File;
import java.util.List;

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

}
