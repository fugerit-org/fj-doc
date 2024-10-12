package org.fugerit.java.doc.freemarker.process;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.parser.DocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.doc.base.process.DocProcessContext;

import javax.print.Doc;
import java.io.StringReader;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.function.Function;

@Slf4j
public class DocInputProcess {

    private static DocInputProcess newValidatingProcess( Consumer<DocValidationResult> docValidationResultConsumer,
                                                         BiFunction<String, Integer, String> sourceFun, String stategy ) {
        return new DocInputProcess( docInput ->
                SafeFunction.get( () -> {
                    log.info( "DocInputProcess - Using strategy : {}", stategy );
                    String source = StreamIO.readString( docInput.getReader() );
                    source = sourceFun.apply( source, docInput.getSource() );
                    try ( StringReader reader = new StringReader( source ) ) {
                        DocParser parser = DocFacadeSource.getInstance().getParserForSource( docInput.getSource() );
                        DocValidationResult docValidationResult = parser.validateVersionResult( reader );
                        docValidationResultConsumer.accept( docValidationResult );
                    }
                    return DocInput.newInput( docInput.getType(), new StringReader( source ), docInput.getSource() );
                } )
        );
    }

    public static DocInputProcess newDocInputProcess( boolean validating, boolean failOnValidate, boolean cleanSource ) {
        Consumer<DocValidationResult> docValidationResultConsumer = failOnValidate ? ERROR_ON_VALIDATION_FAIL : LOG_ON_VALIDATION_FAIL;
        if ( !validating && !cleanSource ) {
            return new DocInputProcess( docInput -> docInput );
        } else if ( validating && cleanSource ) {
            return newValidatingProcess( docValidationResultConsumer, DocFacadeSource::cleanSource,
                    String.format( "clean and validate (failOnValidate:%s)", failOnValidate ) );
        } else if ( validating ) {
            return newValidatingProcess( docValidationResultConsumer, ( s, i ) -> s,
                    String.format( "validate only (failOnValidate:%s)", failOnValidate ) );
        } else {
            return new DocInputProcess( docInput ->
                    SafeFunction.get( () -> {
                        log.info( "DocInputProcess - Using strategy : clean only" );
                        String source = StreamIO.readString( docInput.getReader() );
                        source = DocFacadeSource.cleanSource( source, docInput.getSource() );
                        return DocInput.newInput( docInput.getType(), new StringReader( source ), docInput.getSource() );
                    } )
            );
        }
    }

    private static void defaultLogWarning(DocValidationResult docValidationResult) {
        log.debug( "defaultLogWarning resultCode : {}, isResultOk : {}", docValidationResult.getResultCode(), docValidationResult.isResultOk() );
        if ( !docValidationResult.isResultOk() ) {
            log.warn( "DocValidationResult failed!, errors : {}", docValidationResult.getErrorList().size() );
            for ( int k=0; k<docValidationResult.getErrorList().size(); k++ ) {
                log.warn( "Validation error {}, {}", k, docValidationResult.getErrorList().get( k ) );
            }
        }
    }

    private static final Consumer<DocValidationResult> LOG_ON_VALIDATION_FAIL = r -> defaultLogWarning(r);

    private static final Consumer<DocValidationResult> ERROR_ON_VALIDATION_FAIL = r -> {
        defaultLogWarning(r);
        if ( !r.isResultOk() ) {
            throw new ConfigRuntimeException( String.format( "DocValidationResult failed!, errors : %s", r.getErrorList().size() ) );
        }
    };

    public DocInputProcess(Function<DocInput, DocInput> docInputProcessFun) {
        this.docInputProcessFun = docInputProcessFun;
    }

    private Function<DocInput, DocInput> docInputProcessFun;

    public DocInput process(DocInput input) {
        return this.docInputProcessFun.apply( input );
    }

}