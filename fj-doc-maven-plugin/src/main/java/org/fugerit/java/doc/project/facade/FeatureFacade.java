package org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;

import java.io.*;

@Slf4j
public class FeatureFacade {

    private FeatureFacade() {}

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

    protected static void insureParent( File file ) throws IOException {
        File parentFile = file.getParentFile();
        if ( !parentFile.exists() ) {
            log.info( "creates parent directory {}, mkdirs:?", parentFile.getCanonicalPath(), parentFile.mkdirs() );
        }
    }

    protected static void copyFile(String path, File baseFolder, String basePath ) {
        SafeFunction.apply( () -> {
            File outputFile = new File( baseFolder, path );
            insureParent( outputFile );
            String fullPath = basePath+path;
            log.info( "copy path '{}' to file '{}'", fullPath, outputFile.getCanonicalPath() );
            try (InputStream is = ClassHelper.loadFromDefaultClassLoader( fullPath );
                 FileOutputStream os = new FileOutputStream( outputFile ) ) {
                StreamIO.pipeStream( is, os, StreamIO.MODE_CLOSE_NONE );
            }
        } );
    }

}
