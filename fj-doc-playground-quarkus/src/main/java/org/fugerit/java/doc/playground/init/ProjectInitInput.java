package org.fugerit.java.doc.playground.init;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectInitInput {

    @Pattern(regexp = "[A-Za-z0-9-\\.]+", flags = Pattern.Flag.DOTALL)
    private String groupId;

    @Pattern(regexp = "[A-Za-z0-9-]+", flags = Pattern.Flag.DOTALL)
    private String artifactId;

    @Pattern(regexp = "[A-Za-z0-9-\\.]+", flags = Pattern.Flag.DOTALL)
    private String projectVersion;

    @Min(8)
    @Max(21)
    private Long javaVersion;

    @Pattern(regexp = "[A-Za-z0-9-\\.]+", flags = Pattern.Flag.DOTALL)
    private String venusVersion;

    @Pattern(regexp = "[A-Za-z0-9- ]+", flags = Pattern.Flag.DOTALL)
    private String flavour;

    @Pattern(regexp = "[A-Za-z0-9-\\.]+", flags = Pattern.Flag.DOTALL)
    private String flavourVersion;

    private List<String> extensionList;

    private boolean addVerifyPlugin;

    private boolean addDirectPlugin;

    private boolean addJacoco;

    private boolean addFormatting;

    private boolean addChangelog;

    private String withCI;

}
