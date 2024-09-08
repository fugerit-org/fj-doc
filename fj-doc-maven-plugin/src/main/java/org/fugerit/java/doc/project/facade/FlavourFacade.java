package org.fugerit.java.doc.project.facade;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.project.facade.flavour.FlavourConfig;
import org.fugerit.java.doc.project.facade.flavour.ProcessEntry;

import java.io.*;
import java.util.*;

@Slf4j
public class FlavourFacade {

    private FlavourFacade() {}

    public static final String FLAVOUR_VANILLA = "vanilla";

    public static final String FLAVOUR_QUARKUS_3 = "quarkus-3";

    public static final String FLAVOUR_QUARKUS_LATEST = "quarkus-latest";

    public static final Set<String> SUPPORTED_FLAVOURS = Collections.unmodifiableSet( new HashSet<>( Arrays.asList( FLAVOUR_VANILLA, FLAVOUR_QUARKUS_3 ) ) );

    private static final Properties MAP_FLAVOURS = SafeFunction.get( () -> {
        Properties prop = new Properties();
        prop.setProperty( FLAVOUR_QUARKUS_LATEST, FLAVOUR_QUARKUS_3 );
        return prop;
    });

    public static void initProject( FlavourContext context ) throws IOException, TemplateException {
        log.info( "generate flavour : {}", context.getFlavour() );
        String actualFlavour = MAP_FLAVOURS.getProperty( context.getFlavour(), context.getFlavour() );
        if ( SUPPORTED_FLAVOURS.contains( actualFlavour ) ) {
            initFlavour( context, actualFlavour );
        } else {
            throw new ConfigRuntimeException( String.format( "flavour not supported : %s", context.getFlavour() ) );
        }
    }

    private static void initFlavour( FlavourContext context, String actualFlavour ) throws IOException, TemplateException {
        // copy all resources
        String listFilePath = String.format( "config/flavour/%s-copy.txt", actualFlavour );
        String baseFlavourPath = String.format( "config/flavour/%s/", actualFlavour );
        log.info( "loading list file {}, base flavour path {}", listFilePath, baseFlavourPath );
        try (BufferedReader reader = new BufferedReader( new InputStreamReader(ClassHelper.loadFromDefaultClassLoader( listFilePath ) ) ) )  {
            reader.lines().forEach( s -> copyFlavourFile( s, context.getProjectFolder(), baseFlavourPath ) );
        }
        // freemarker resources
        Map<String, Object> data = new HashMap<>();
        data.put( "context", context );
        String freemarkerProcessYamlPath = String.format( "flavours/%s-fm-yml.ftl", actualFlavour );
        log.info( "freemarkerProcessYamlPath process {}", freemarkerProcessYamlPath );
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try ( StringWriter writer = new StringWriter() ) {
            FreemarkerTemplateFacade.processFile( freemarkerProcessYamlPath, writer, data );
            FlavourConfig flavourConfig = mapper.readValue( writer.toString(), FlavourConfig.class );
            log.info( "floavourConfig {}", flavourConfig.getFlavour() );
            flavourConfig.getProcess().forEach( entry -> processEntry( entry, data ) );
        }
    }

    private static void insureParent( File file ) throws IOException {
        File parentFile = file.getParentFile();
        if ( !parentFile.exists() ) {
            log.info( "creates parent directory {}, mkdirs:?", parentFile.getCanonicalPath(), parentFile.mkdirs() );
        }
    }

    private static void copyFlavourFile( String path, File baseFolder, String basePath ) {
        SafeFunction.apply( () -> {
            File outputFile = new File( baseFolder, path );
            insureParent( outputFile );
            String fullPath = basePath+path;
            log.info( "copy path '{}' to file '{}'", fullPath, outputFile.getCanonicalPath() );
            try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( fullPath );
                    FileOutputStream os = new FileOutputStream( outputFile ) ) {
                StreamIO.pipeStream( is, os, StreamIO.MODE_CLOSE_NONE );
            }
        } );
    }

    private static void processEntry( ProcessEntry entry, Map<String, Object> data ) {
        log.info( "process entry : {}", entry );
        SafeFunction.apply( () -> {
            File toFile = new File( entry.getTo() );
            insureParent( toFile );
            FreemarkerTemplateFacade.processFile( entry.getFrom(), toFile, data );
        } );
    }

}
