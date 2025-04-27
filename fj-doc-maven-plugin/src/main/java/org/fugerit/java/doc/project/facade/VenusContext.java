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

    public static String toArtificatIdForFolder( String artifactId ) {
        return artifactId.toLowerCase();
    }

    public static String toArtificatIdForName( String artifactId ) {
        return toArtificatIdForFolder( artifactId ).replace( "-", "" );
    }

    public static String toResourcePathFmConfigXml( String artifactId ) {
        return toArtificatIdForFolder( artifactId )+"/fm-doc-process-config.xml";
    }

    public static final String VERSION_ASCIIDOC_FREEMARKER_HANDLER = "8.8.6";

    public static final String VERSION_NA_VERIFY_PLUGIN = "8.7.2";

    public static final String VERSION_NA_DIRECT_PLUGIN = "8.13.4";

    public static final String VERSION_NA_FULL_PROCESS = "8.6.2";

    public static final String VERSION_NA_FREEMARKER_NATIVE = "8.11.8";

    private static final String VENUS_DIRECT_CONFIG_DEFAULT = "venus-direct-config";

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
    private boolean addDirectPlugin;

    @Getter @Setter
    private boolean addJunit5;

    @Getter @Setter
    private boolean addLombok;

    @Getter @Setter
    private boolean addDependencyOnTop;

    @Getter @Setter
    private String freemarkerVersion;

    @Getter @Setter
    private String groupIdOverride;

    @Getter @Setter
    private String artifactIdOverride;

    @Getter @Setter
    private boolean simpleModel;

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
        this.freemarkerVersion = FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION_DEFAULT;
    }

    public String getGroupId() {
        if ( this.getMavenModel().getGroupId() != null ) {
            return this.getMavenModel().getGroupId();
        } else {
            return this.getMavenModel().getParent().getGroupId();
        }
    }

    public String getArtificatIdForFolder() {
        return toArtificatIdForFolder( this.getMavenModel().getArtifactId() );
    }

    public String getGroupIdForName() {
        return toArtificatIdForName( this.getGroupId() );
    }

    public String getArtificatIdForName() {
        return toArtificatIdForName( this.getMavenModel().getArtifactId() );
    }

    public String getResourcePathFmConfigXml() {
        return toResourcePathFmConfigXml( this.getMavenModel().getArtifactId() );
    }

    public String getDocConfigPackage() {
        return this.getGroupIdForName()+"."+this.getArtificatIdForName();
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

    public boolean isDirectPluginNotAvailable() {
        return VersionCheck.isMajorThan( VERSION_NA_DIRECT_PLUGIN, this.getVersion() );
    }

    public boolean isAsciidocFreemarkerHandlerAvailable() {
        return VersionCheck.isMajorThan( this.getVersion(), VERSION_ASCIIDOC_FREEMARKER_HANDLER );
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

    public String getVenusDirectConfig() {
        return VENUS_DIRECT_CONFIG_DEFAULT;
    }

}
