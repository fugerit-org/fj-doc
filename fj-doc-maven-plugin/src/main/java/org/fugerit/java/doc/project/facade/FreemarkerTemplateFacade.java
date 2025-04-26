package org.fugerit.java.doc.project.facade;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.*;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Map;

public class FreemarkerTemplateFacade {

    private FreemarkerTemplateFacade() {}

    public static void processFile(String templatePath, Writer writer, Map<String, Object> data ) throws IOException, TemplateException {
        processFile( templatePath, writer, getConfiguration(), data );
    }

    public static void processFile(String templatePath, File outputFile, Map<String, Object> data ) throws IOException, TemplateException {
        processFile( templatePath, outputFile, getConfiguration(), data );
    }

    public static void processFile(String templatePath, Writer writer, Configuration configuration, Map<String, Object> data ) throws IOException, TemplateException {
        Template docTemplate = configuration.getTemplate( templatePath );
        data.put( "templatePath", templatePath );
        data.put( "generationTime", new Timestamp( System.currentTimeMillis() ) );
        docTemplate.process( data, writer );
    }

    public static void processFile(String templatePath, File outputFile, Configuration configuration, Map<String, Object> data ) throws IOException, TemplateException {
        try ( Writer writer = new FileWriter( outputFile) ) {
            processFile( templatePath, writer, configuration, data );
        }
    }

    private static final Configuration CONFIGURATION = SafeFunction.get( () -> {
        // freemarker configuration
        Configuration configuration = new Configuration( new Version( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION_LATEST ) );
        configuration.clearTemplateCache();
        ClassTemplateLoader loader = new ClassTemplateLoader( VenusContext.class, "/config/template/" );
        configuration.setTemplateExceptionHandler( TemplateExceptionHandler.RETHROW_HANDLER );
        configuration.setTemplateLoader( loader );
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return configuration;
    } );

    public static Configuration getConfiguration() { return CONFIGURATION; }

}
