package org.fugerit.java.doc.project.facade;

import org.fugerit.java.core.cfg.ConfigRuntimeException;

import java.util.*;

public class ModuleFacade {

    private ModuleFacade() {}

    private static final String MODULE_PREFIX = "fj-doc-";

    public static String toModuleName( String module ) {
        if ( module.contains( MODULE_PREFIX ) ) {
            return module;
        } else {
            return MODULE_PREFIX + module;
        }
    }

    private static final Set<String> MODULES = new HashSet<>( Arrays.asList(
            toModuleName( "base" ),
            toModuleName( "base-json" ),
            toModuleName( "base-yaml" ),
            toModuleName( "freemarker" ),
            toModuleName( "mod-fop" ),
            toModuleName( "mod-poi" ),
            toModuleName( "mod-opencsv" ),
            toModuleName( "mod-openpdf-ext" ),
            toModuleName( "mod-openrtf-ext" )
    ) );

    public static boolean isModuleSupported( String moduleName ) {
        return MODULES.contains( toModuleName( moduleName ) );
    }

    public static Collection<String> getModules() {
        return Collections.unmodifiableCollection( MODULES );
    }

    public static List<String> toModuleList( String extensions ) {
        List<String> list = new ArrayList<>();
        for ( String currentModule : extensions.split( "," ) ) {
            String currentModuleName = toModuleName( currentModule );
            if ( isModuleSupported( currentModuleName ) ) {
                list.add( currentModuleName );
            } else {
                throw new ConfigRuntimeException( String.format( "Module not supported : %s", currentModule ) );
            }
        }
        return list;
    }


}
