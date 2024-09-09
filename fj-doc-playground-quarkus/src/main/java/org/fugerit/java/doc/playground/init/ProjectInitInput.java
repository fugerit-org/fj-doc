package org.fugerit.java.doc.playground.init;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ProjectInitInput {

    @Pattern( regexp = "[A-Za-z0-9-\\.]+", flags = Pattern.Flag.DOTALL )
    @Getter @Setter private String groupId;

    @Pattern( regexp = "[A-Za-z0-9-]+", flags = Pattern.Flag.DOTALL )
    @Getter @Setter private String artifactId;

    @Pattern( regexp = "[A-Za-z0-9-\\.]+", flags = Pattern.Flag.DOTALL )
    @Getter @Setter private String projectVersion;

    @Min( 8 ) @Max( 21 )
    @Getter @Setter private Long javaVersion;

    @Pattern( regexp = "[A-Za-z0-9-\\.]+", flags = Pattern.Flag.DOTALL )
    @Getter @Setter private String venusVersion;

    @Pattern( regexp = "[A-Za-z0-9- ]+", flags = Pattern.Flag.DOTALL )
    @Getter @Setter private String flavour;

    @Getter @Setter private List<String> extensionList;

}
