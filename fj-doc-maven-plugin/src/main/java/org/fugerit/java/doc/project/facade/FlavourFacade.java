package org.fugerit.java.doc.project.facade;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.lang.helpers.reflect.MethodHelper;
import org.fugerit.java.core.util.PropsIO;
import org.fugerit.java.doc.project.facade.flavour.FlavourConfig;
import org.fugerit.java.doc.project.facade.flavour.ProcessEntry;
import org.fugerit.java.doc.project.facade.flavour.extra.FlavourExtraConfig;
import org.fugerit.java.doc.project.facade.flavour.extra.FlavourExtraConfigFacade;
import org.fugerit.java.doc.project.facade.flavour.extra.ParamConfig;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class FlavourFacade {

    private FlavourFacade() {}

    public static final String FLAVOUR_VANILLA = "vanilla";

    public static final String FLAVOUR_DIRECT = "direct";

    public static final String FLAVOUR_QUARKUS_3 = "quarkus-3";

    public static final String FLAVOUR_QUARKUS_3_GRADLE = "quarkus-3-gradle";

    public static final String FLAVOUR_QUARKUS_3_GRADLE_KTS = "quarkus-3-gradle-kts";

    public static final String FLAVOUR_QUARKUS_3_PROPERTIES = "quarkus-3-properties";

    public static final String FLAVOUR_QUARKUS_2 = "quarkus-2";

    public static final String FLAVOUR_QUARKUS_LATEST = "quarkus-latest";

    public static final String FLAVOUR_MICRONAUT_4 = "micronaut-4";

    public static final String FLAVOUR_OPENLIBERTY = "openliberty";

    public static final String FLAVOUR_SPRINGBOOT_3 = "springboot-3";

    private static final Properties FLAVOURS_DEFAULT_VERSION = PropsIO.loadFromClassLoaderSafe( "config/flavour/flavour_versions_default.properties" );

    public static final String WITH_CI_GITHUB = "github";

    public static Properties getFlavourDefaultVersion() {
        return new Properties( FLAVOURS_DEFAULT_VERSION );
    }

    public static final Set<String> SUPPORTED_FLAVOURS = Collections.unmodifiableSet(
            new HashSet<>( Arrays.asList( FLAVOUR_VANILLA, FLAVOUR_DIRECT, FLAVOUR_QUARKUS_3, FLAVOUR_QUARKUS_3_GRADLE, FLAVOUR_QUARKUS_3_GRADLE_KTS,
                    FLAVOUR_QUARKUS_3_PROPERTIES, FLAVOUR_QUARKUS_2, FLAVOUR_MICRONAUT_4, FLAVOUR_SPRINGBOOT_3, FLAVOUR_OPENLIBERTY ) ) );

    public static boolean isGradleKtsFlavour(String flavour ) {
        return FLAVOUR_QUARKUS_3_GRADLE_KTS.equals( flavour ) || FLAVOUR_QUARKUS_3_GRADLE.equals( flavour );
    }

    private static final Properties MAP_FLAVOURS = SafeFunction.get( () -> {
        Properties prop = new Properties();
        prop.setProperty( FLAVOUR_QUARKUS_LATEST, FLAVOUR_QUARKUS_3 );
        return prop;
    });

    public static final Set<String> SUPPORTED_CI =  Collections.unmodifiableSet( new HashSet<>( Arrays.asList( WITH_CI_GITHUB ) ) );

    private static void checkCI( FlavourContext context ) {
        if ( StringUtils.isNotEmpty( context.getWithCI() ) && !SUPPORTED_CI.contains( context.getWithCI().toLowerCase() ) ) {
            throw new ConfigRuntimeException( String.format( "withCI not supported : %s (allowed values are %s)", context.getWithCI(), SUPPORTED_CI ) );
        }
    }

    public static String initProject( FlavourContext context ) throws IOException, TemplateException {
        log.info( "generate flavour : {}", context.getFlavour() );
        String actualFlavour = MAP_FLAVOURS.getProperty( context.getFlavour(), context.getFlavour() );
        if ( SUPPORTED_FLAVOURS.contains( actualFlavour ) ) {
            checkCI( context );
            checkFlavour( context, actualFlavour );
            initFlavour( context, actualFlavour );
            initCI( context, actualFlavour );
        } else {
            throw new ConfigRuntimeException( String.format( "flavour not supported : %s", context.getFlavour() ) );
        }
        return actualFlavour;
    }

    public static void checkFlavour( FlavourContext context, String actualFlavour ) {
        int javaVersion = Integer.parseInt( context.getJavaRelease() );
        if ( ( FLAVOUR_QUARKUS_3.equals( actualFlavour ) || FLAVOUR_MICRONAUT_4.equals( actualFlavour )
                || FLAVOUR_SPRINGBOOT_3.equals( actualFlavour ) || FLAVOUR_OPENLIBERTY.equals( actualFlavour ) ) && javaVersion < 17 ) {
            throw new ConfigRuntimeException( String.format( "Minimum java version for %s is 17", actualFlavour ) );
        } else if ( FLAVOUR_QUARKUS_2.equals( actualFlavour ) && javaVersion != 11 ) {
            log.info( "quarkus 2 is a legacy flavour, javaRelease {} will default to '11'", javaVersion );
        }
        log.info( "checkFlavour {} done", actualFlavour );
        checkFlavourVersion( context, actualFlavour );
        checkFlavourExtraConfig( context, actualFlavour );
    }

    public static void checkFlavourVersion( FlavourContext context, String actualFlavour ) {
        // additional flavour config
        if ( StringUtils.isEmpty( context.getFlavourVersion() ) ) {
            String flavourVersionDefault = FLAVOURS_DEFAULT_VERSION.getProperty( actualFlavour );
            log.info( "using default flavourVersion : {} for flavour : {}", flavourVersionDefault, actualFlavour );
            context.setFlavourVersion( flavourVersionDefault );
        } else {
            log.info( "overriding default flavourVersion : {} for flavour : {}", context.getFlavourVersion(), actualFlavour );
        }
    }

    public static void checkFlavourExtraConfig( FlavourContext context, String actualFlavour ) {
        FlavourExtraConfig flavourExtraConfig = SafeFunction.get( () -> {
            String flavourConfigPath = String.format( "config/flavour-extra-config/%s-config.yml", actualFlavour );
            log.info( "flavourConfigPath : {}", flavourConfigPath );
            try (InputStream is = ClassHelper.loadFromDefaultClassLoader( flavourConfigPath ) ) {
                return FlavourExtraConfigFacade.readConfigBlankDefault( is );
            }
        });
        checkFlavourExtraConfig( context, actualFlavour , flavourExtraConfig );
    }

    public static void checkFlavourExtraConfigParam( String fieldName, ParamConfig paramConfig, String actualFlavour, Object value  ) {
        log.debug( "fieldName : {}, value : {}", fieldName, value );
        if ( value != null && paramConfig.getAcceptOnly() != null && !paramConfig.getAcceptOnly().contains( value.toString() ) ) {
            log.debug( "accept only list : {} -> {}", fieldName, paramConfig.getAcceptOnly() );
            String message = String.format( "Value '%s' not valid for flavour '%s' and param '%s', refer to flavour documentation.", value, actualFlavour, fieldName );
            log.warn( message );
            throw new ConfigRuntimeException( message );
        }
    }

    public static void checkFlavourExtraConfig( FlavourContext context, String actualFlavour, FlavourExtraConfig flavourExtraConfig ) {
        SafeFunction.applyIfNotNull( flavourExtraConfig.getParamConfig(), () -> {
            Field[] fields = FlavourContext.class.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                ParamConfig paramConfig = flavourExtraConfig.getParamConfig().get( fieldName );
                if ( paramConfig != null ) {
                    Object value = readField( context, field, fieldName );
                    checkFlavourExtraConfigParam( fieldName, paramConfig, actualFlavour, value );
                }
            }
        } );
    }

    public static Object readField( FlavourContext context, Field field, String fieldName ) {
        return SafeFunction.getSilent( () -> {
                    if (field.getType().getSimpleName().equalsIgnoreCase("boolean")) {
                        String methodName = String.format("is%s", MethodHelper.getUpFirstForProperty(fieldName));
                        return MethodHelper.invoke(context, methodName, MethodHelper.NO_PARAM_TYPES, MethodHelper.NO_PARAM_VALUES);
                    } else {
                        return MethodHelper.invokeGetter(context, fieldName);
                    }
                }
        );
    }

    private static void initFlavour( FlavourContext context, String actualFlavour ) throws IOException, TemplateException {
        // copy all resources
        FeatureFacade.copyFlavourList( context.getProjectFolder(), actualFlavour );
        // freemarker resources
        Map<String, Object> data = new HashMap<>();
        data.put( "context", context );
        String freemarkerProcessYamlPath = String.format( "flavour/%s-fm-yml.ftl", actualFlavour );
        log.info( "freemarkerProcessYamlPath process {}", freemarkerProcessYamlPath );
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try ( StringWriter writer = new StringWriter() ) {
            FreemarkerTemplateFacade.processFile( freemarkerProcessYamlPath, writer, data );
            FlavourConfig flavourConfig = mapper.readValue( writer.toString(), FlavourConfig.class );
            log.info( "flavourConfig {}", flavourConfig.getFlavour() );
            flavourConfig.getProcess().forEach( entry -> FeatureFacade.processEntry( entry, data ) );
        }
    }

    private static void initCI( FlavourContext context, String actualFlavour ) throws IOException, TemplateException {
        if ( StringUtils.isNotEmpty( context.getWithCI() ) && !FlavourFacade.isGradleKtsFlavour( actualFlavour ) ) {
            String withCI = context.getWithCI().toLowerCase();
            String relativeDestinationPath = context.getWithCIPath();
            String ciTemplatePath = String.format( "ci/%s-ci.ftl", withCI );
            File outputCIFile = new File( context.getProjectFolder(), relativeDestinationPath );
            log.info( "generating CI : mkdir? {} -> {}", outputCIFile.getParentFile().mkdirs(), outputCIFile.getCanonicalPath() );
            try ( FileWriter writer = new FileWriter( outputCIFile ) ) {
                Map<String, Object> data = new HashMap<>();
                data.put( "context", context );
                FreemarkerTemplateFacade.processFile( ciTemplatePath, writer, data );
            }
        }
    }



}
