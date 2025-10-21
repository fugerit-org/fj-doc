package org.fugerit.java.doc.project.facade;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.project.facade.flavour.FlavourConfig;
import org.fugerit.java.doc.project.facade.flavour.ProcessEntry;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FeatureFacade {

    private FeatureFacade() {}

    /**
     * Checks that the given file is inside the baseFolder after normalization.
     * Throws IOException if not.
     */
    public static void checkIfInBaseFolder(File baseFolder, File file) throws IOException {
        Path base = baseFolder.getCanonicalFile().toPath().normalize();
        Path target = file.getCanonicalFile().toPath().normalize();
        if (!target.startsWith(base)) {
            throw new IOException( String.format( "File path %s is not within permitted base folder %s", file.getCanonicalPath(), baseFolder.getCanonicalPath() ) );
        }
    }

    public static void copyFlavourList( File baseFolder, String actualFlavour ) throws IOException {
        copyResourcesList( baseFolder, "flavour", actualFlavour );
    }

    public static void copyFeatureList( File baseFolder, String featureId ) throws IOException {
        copyResourcesList( baseFolder, "feature", featureId );
    }

    private static void copyResourcesList( File baseFolder, String mode, String id ) throws IOException {
        // copy all resources
        String listFilePath = String.format( "config/%s/%s-copy.txt", mode, id );
        String baseFlavourPath = String.format( "config/%s/%s/", mode, id );
        log.info( "loading list file {}, base flavour path {}", listFilePath, baseFlavourPath );
        try (BufferedReader reader = new BufferedReader( new InputStreamReader(ClassHelper.loadFromDefaultClassLoader( listFilePath ) ) ) )  {
            reader.lines().forEach( s -> copyFile( s, baseFolder, baseFlavourPath ) );
        }
    }

    public static void insureParent( File file ) throws IOException {
        File parentFile = file.getParentFile();
        // Defensive: check parent is within project's root as well
        if (parentFile != null) {
            File baseFolder = file.getParentFile().getParentFile();
            if (baseFolder != null) {
                checkIfInBaseFolder(baseFolder, parentFile);
            }
            if ( !parentFile.exists() ) {
                log.info( "creates parent directory {}, mkdirs:? {}", parentFile.getCanonicalPath(), parentFile.mkdirs() );
            }
        }
    }

    protected static void copyFile(String path, File baseFolder, String basePath ) {
        SafeFunction.apply( () -> {
            File outputFile = new File( baseFolder, path );
            // Validate that the output file is inside the intended base folder
            checkIfInBaseFolder(baseFolder, outputFile);
            insureParent( outputFile );
            String fullPath = basePath+path;
            log.info( "copy path '{}' to file '{}'", fullPath, outputFile.getCanonicalPath() );
            try (InputStream is = ClassHelper.loadFromDefaultClassLoader( fullPath );
                 FileOutputStream os = new FileOutputStream( outputFile ) ) {
                StreamIO.pipeStream( is, os, StreamIO.MODE_CLOSE_NONE );
            }
        } );
    }

    public static void processFeature( VenusContext context, String featureId ) {
        SafeFunction.apply( () -> {
            Map<String, Object> data = new HashMap<>();
            data.put( "context", context );
            data.put( "featureId", featureId );
            String freemarkerProcessYamlPath = String.format( "feature/%s-fm-yml.ftl", featureId );
            log.info( "freemarkerProcessYamlPath feature process {}", freemarkerProcessYamlPath );
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            try ( StringWriter writer = new StringWriter() ) {
                FreemarkerTemplateFacade.processFile( freemarkerProcessYamlPath, writer, data );
                FlavourConfig featureConfig = mapper.readValue( writer.toString(), FlavourConfig.class );
                log.info( "featureConfig {}", featureConfig.getFlavour() );
                featureConfig.getProcess().forEach( entry -> processEntry( entry, data ) );
            }
        });
    }

    public static void processEntry(ProcessEntry entry, Map<String, Object> data ) {
        log.info( "process entry : {}", entry );
        SafeFunction.apply( () -> {
            File toFile = new File( entry.getTo() );
            FeatureFacade.insureParent( toFile );
            FreemarkerTemplateFacade.processFile( entry.getFrom(), toFile, data );
        } );
    }

}
