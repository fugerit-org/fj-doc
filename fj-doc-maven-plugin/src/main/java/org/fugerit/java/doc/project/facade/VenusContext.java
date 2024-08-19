package org.fugerit.java.doc.project.facade;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.maven.model.Model;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
public class VenusContext {

    @Getter
    private File projectDir;

    @Getter
    private String extensions;

    @Getter
    private String version;

    @Getter @Setter
    private boolean addDocFacace;

    @Getter
    private List<String> errors;

    @Getter
    private Set<String> modules;

    @Getter @Setter
    private Model mavenModel;

    public VenusContext(File projectDir, String version, String extensions ) {
        this.projectDir = projectDir;
        this.version = version;
        this.extensions = extensions;
        this.errors = new ArrayList<>();
        this.modules = new HashSet<>();
    }

    public String getArtificatIdForFolder() {
        return this.getMavenModel().getArtifactId().toLowerCase();
    }

    public String getArtificatIdForName() {
        return this.getArtificatIdForFolder().replace( "-", "" ).toLowerCase();
    }

    public String getResourcePathFmConfigXml() {
        return this.getArtificatIdForFolder()+"/fm-doc-process-config.xml";
    }

    public String getFreemarkerVersion() {
        return FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION_LATEST;
    }

    public String getDocConfigPackage() {
        return this.getMavenModel().getGroupId()+"."+this.getArtificatIdForName();
    }

    public String getTemplateSubPath() {
        return "template";
    }

    public String getDocConfigClass() {
        return "DocHelper";
    }

}
