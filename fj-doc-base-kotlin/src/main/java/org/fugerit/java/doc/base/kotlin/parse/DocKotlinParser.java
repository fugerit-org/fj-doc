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
import org.fugerit.java.doc.base.parser.DocEvalWithDataModel;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.doc.base.xml.DocXmlParser;
import org.fugerit.java.script.helper.EvalScript;
import org.fugerit.java.script.helper.EvalScriptWithJsonDataModel;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class DocKotlinParser extends AbstractDocParser implements DocEvalWithDataModel {

    private DocXmlParser docXmlParser;

    public DocKotlinParser() {
        super( DocFacadeSource.SOURCE_TYPE_KOTLIN );
        this.docXmlParser = new DocXmlParser();
    }

    /*
     * This will create an eval script utility.
     * The data model will be transformed to a json model with default name 'data'.
     */
    private static EvalScript evalScript = EvalScriptWithJsonDataModel.newEvalScriptWithJsonDataModel( "kts" );

    @Override
    public String evalWithDataModel(Reader reader, Map<String, Object> dataModel) {
        return dslDocToXml(reader, dataModel);
    }

    public static String dslDocToXml(Reader reader, Map<String, Object> dataModel ) {
        Object res = evalScript.handle( reader, dataModel );
        String xml = res.toString();
        log.debug( "evalWithDataModel xml content : \n{}", xml );
        return xml;
    }

    @Override
    protected DocValidationResult validateWorker(Reader reader, boolean parseVersion) throws DocException {
        try ( StringReader xmlReader = new StringReader( evalWithDataModel( reader, null) ) ) {
            if ( parseVersion ) {
                return this.docXmlParser.validateVersionResult( xmlReader );
            } else {
                return this.docXmlParser.validateResult( xmlReader );
            }
        }
    }

    @Override
    protected DocBase parseWorker(Reader reader) throws DocException {
        try ( StringReader xmlReader = new StringReader( evalWithDataModel( reader, null) ) ) {
            return this.docXmlParser.parse( xmlReader );
        }
    }

}
