package org.fugerit.java.doc.project.facade;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigRuntimeException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ModuleFacade {

    private ModuleFacade() {}

    public static final String MODULE_PREFIX = "fj-doc-";

    public static String toModuleName( String module ) {
        if ( module.contains( MODULE_PREFIX ) ) {
            return module;
        } else {
            return MODULE_PREFIX + module;
        }
    }

    private static final List<String> MODULE_LIST = Arrays.asList(
            toModuleName( "base" ),
            toModuleName( "base-json" ),
            toModuleName( "base-yaml" ),
            toModuleName( "base-kotlin" ),
            toModuleName( "freemarker" ),
            toModuleName( "mod-poi" ),
            toModuleName( "mod-fop" ),
            toModuleName( "mod-opencsv" ),
            toModuleName( "mod-openpdf-ext" ),
            toModuleName( "mod-openrtf-ext" ),
            toModuleName( "mod-pdfbox" )
    );

    private static final Set<String> MODULES = new HashSet<>( MODULE_LIST );

    public static boolean isModuleSupported( String moduleName ) {
        return MODULES.contains( toModuleName( moduleName ) );
    }

    public static Collection<String> getModules() {
        return Collections.unmodifiableCollection( MODULES );
    }

    public static List<String> toModuleListOptimizedOrder( String extensions ) {
        return toModuleList( extensions ).stream().sorted( Comparator.comparingInt(MODULE_LIST::indexOf) ).collect(Collectors.toList());
    }

    public static List<String> toModuleList( String extensions ) {
        List<String> list = new ArrayList<>();
        for ( String currentModule : extensions.split( "," ) ) {
            String currentModuleName = toModuleName( currentModule );
            if ( isModuleSupported( currentModuleName ) ) {
                list.add( currentModuleName );
            } else {
                String message = String.format( "Module not supported : %s", currentModule );
                log.warn( "{}, supported modules are : ", message );
                ModuleFacade.getModules().forEach( log::warn );
                throw new ConfigRuntimeException( message );
            }
        }
        return list;
    }


}
