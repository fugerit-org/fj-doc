package org.fugerit.java.doc.base.kotlin.gen;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocModel;

import java.io.File;
import java.util.*;

@RequiredArgsConstructor
public class GenerateKotlinConfig {

    public static final String CONFIG_ROOT_ELEMENT = "root-element";

    public static final String CONFIG_SOURCE_OUTPUT_FOLDER = "source-output-folder";

    public static final String CONFIG_PACKAGE = "dsl-package";

    public static final String CONFIG_MAIN_FUN = "dsl-main-fun";

    public static final String CONFIG_ROOT_INIT = "root-init";

    @NonNull
    private Properties config;

    @NonNull @Getter
    private AutodocModel autodocModel;

    @Getter
    private Set<String> extraFun = new LinkedHashSet<>();

    @Getter
    private List<String> checkFun = new ArrayList<>();

    public String getProperty(String key) {
        return config.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return config.getProperty(key, defaultValue);
    }

    public File getPackageFolder() {
        return new File( this.getProperty( CONFIG_SOURCE_OUTPUT_FOLDER ), this.getProperty( CONFIG_PACKAGE ).replace( '.', '/' ) );
    }

    public String toKotlinClass( String tagName ) {
        StringBuilder kotlinClass = new StringBuilder();
        Arrays.asList( tagName.split( "-" ) )
                .forEach( s -> kotlinClass.append( s.substring( 0, 1 ).toUpperCase()+s.substring( 1 ) ) );
        return kotlinClass.toString();
    }

    public String toKotlinFun( String tagName ) {
        String s = this.toKotlinClass( tagName );
        return s.substring( 0, 1 ).toLowerCase()+s.substring( 1 );
    }

    public String toCheckTypeFun( String typeName ) {
        if ( typeName.toLowerCase().endsWith( "type" ) ) {
            typeName = typeName.substring( 0, typeName.length()-4 );
        }
        String[] split = typeName.split( ":" );
        if ( split.length > 1 ) {
            return toKotlinFun( split[1] );
        } else {
            return toKotlinFun( split[0] );
        }
    }

}
