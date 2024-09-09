package org.fugerit.java.doc.project.facade;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.fugerit.java.core.cfg.VersionUtils;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@ToString
public class VenusContext {

    public static final String VERSION_NA_VERIFY_PLUGIN = "8.7.2";

    public static final String VERSION_NA_FULL_PROCESS = "8.6.2";

    @Getter
    private File projectDir;

    @Getter
    private String extensions;

    @Getter
    private String version;

    @Getter @Setter
    private boolean addDocFacace;

    @Getter @Setter
    private boolean force;

    @Getter
    private List<String> errors;

    @Getter
    private Set<String> modules;

    @Getter @Setter
    private Model mavenModel;

    @Getter @Setter
    private String addExclusions;

    @Getter @Setter
    private boolean addVerifyPlugin;

    @Getter @Setter
    private boolean addJunit5;

    @Getter @Setter
    private boolean addLombok;

    @Getter @Setter
    private boolean addDependencyOnTop;

    public void setExcludeXmlApis( boolean excludeXmlApis ) {
        if ( excludeXmlApis ) {
            this.setAddExclusions( "xml-apis:xml-apis" );
        }
    }

    public VenusContext(File projectDir, String version, String extensions ) {
        this.projectDir = projectDir;
        this.version = version;
        this.extensions = extensions;
        this.errors = new ArrayList<>();
        this.modules = new HashSet<>();
        this.addDocFacace = true;
        this.force = false;
        this.addVerifyPlugin = true;
        this.addJunit5 = true;
        this.addLombok = true;
        this.addDependencyOnTop = false;
    }

    public String getGroupId() {
        if ( this.getMavenModel().getGroupId() != null ) {
            return this.getMavenModel().getGroupId();
        } else {
            return this.getMavenModel().getParent().getGroupId();
        }
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
        return this.getGroupId()+"."+this.getArtificatIdForName();
    }

    public String getTemplateSubPath() {
        return "template";
    }

    public String getDocConfigClass() {
        return "DocHelper";
    }

    public boolean isVerifyPluginNotAvailable() {
        return VersionCheck.isMajorThan( VERSION_NA_VERIFY_PLUGIN, this.getVersion() );
    }

    public boolean isPreVersion862() {
        return VersionCheck.isMajorThan( VERSION_NA_FULL_PROCESS, this.getVersion() );
    }

    private File getAndCreateFolder( File file ) {
        if ( !file.exists() ) {
            boolean result = file.mkdirs();
            log.info( "folder doesn't exists: {}, mkdirs: ", file.getAbsolutePath(), result );
        }
        return file;
    }

    public File getMainJavaFolder() {
        return this.getAndCreateFolder( new File( this.getProjectDir(), "src/main/java" ) );
    }

    public File getMainResourcesFolder() {
        return this.getAndCreateFolder( new File( this.getProjectDir(), "src/main/resources" ) );
    }

    public File getTestJavaFolder() {
        return this.getAndCreateFolder( new File( this.getProjectDir(), "src/test/java" ) );
    }

    public File getTestResourcesFolder() {
        return this.getAndCreateFolder( new File( this.getProjectDir(), "src/test/resources" ) );
    }

}
