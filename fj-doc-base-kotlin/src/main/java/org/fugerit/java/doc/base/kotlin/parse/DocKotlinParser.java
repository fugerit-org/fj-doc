package org.fugerit.java.doc.base.kotlin.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.util.checkpoint.SimpleCheckpoint;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.AbstractDocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.doc.base.xml.DocXmlParser;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class DocKotlinParser extends AbstractDocParser {

    private DocXmlParser docXmlParser;

    public DocKotlinParser() {
        super( DocFacadeSource.SOURCE_TYPE_KOTLIN );
        this.docXmlParser = new DocXmlParser();
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String dslDocToXml(Reader reader, Map<String, Object> dataModel ) {
        return SafeFunction.get( () -> {
            SimpleCheckpoint checkpoint = new SimpleCheckpoint();
            ScriptEngineManager manager = new ScriptEngineManager();
            log.debug( "kts create script manager : {}", checkpoint.getFormatTimeDiffMillis() );
            ScriptEngine engine =  manager.getEngineByExtension( "kts" );
            log.debug( "kts create script engine : {}", checkpoint.getFormatTimeDiffMillis() );
            if ( dataModel != null ) {
                Bindings bindings = engine.createBindings();
                log.debug( "kts create script bindings : {}", checkpoint.getFormatTimeDiffMillis() );
                LinkedHashMap<String, Object> data = MAPPER.convertValue( dataModel, LinkedHashMap.class );
                log.debug( "kts read json data : {}", checkpoint.getFormatTimeDiffMillis() );
                bindings.put( "data", data );
                engine.setBindings( bindings, ScriptContext.ENGINE_SCOPE );
            }
            log.debug( "kts set bindings : {}", checkpoint.getFormatTimeDiffMillis() );
            Object obj = engine.eval( StreamIO.readString( reader ) );
            log.debug( "kts eval script : {}", checkpoint.getFormatTimeDiffMillis() );
            String xml = obj.toString();
            log.debug( "kts toXml : {}", checkpoint.getFormatTimeDiffMillis() );
            return xml;
        } );
    }

    @Override
    protected DocValidationResult validateWorker(Reader reader, boolean parseVersion) throws DocException {
        try ( StringReader xmlReader = new StringReader( dslDocToXml( reader, null) ) ) {
            if ( parseVersion ) {
                return this.docXmlParser.validateVersionResult( xmlReader );
            } else {
                return this.docXmlParser.validateResult( xmlReader );
            }
        }
    }

    @Override
    protected DocBase parseWorker(Reader reader) throws DocException {
        try ( StringReader xmlReader = new StringReader( dslDocToXml( reader, null) ) ) {
            return this.docXmlParser.parse( xmlReader );
        }
    }

}
