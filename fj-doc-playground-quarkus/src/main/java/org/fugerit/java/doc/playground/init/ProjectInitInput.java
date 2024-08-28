package org.fugerit.java.doc.playground.init;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ProjectInitInput {

    @Getter @Setter private String groupId;
    @Getter @Setter private String artifactId;
    @Getter @Setter private String projectVersion;
    @Getter @Setter private String javaVersion;
    @Getter @Setter private String venusVersion;
    @Getter @Setter private List<String> extensionList;

}
