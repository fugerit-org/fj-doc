package org.fugerit.java.doc.base.feature;

import lombok.Getter;
import org.fugerit.java.core.lang.ex.CodeException;
import org.fugerit.java.core.lang.ex.CodeRuntimeException;
import org.fugerit.java.core.lang.ex.ExConverUtils;
import org.fugerit.java.core.util.result.Result;

import java.util.ArrayList;
import java.util.List;

public class DocFeatureRuntimeException extends CodeRuntimeException {

    /**
     * <p>Default value for the code field in a DocFeatureRuntimeException.</p>
     */
    public static final int DEFAULT_CODE = CodeException.DEFAULT_CODE;

    @Getter
    private final List<String> messages;

    public DocFeatureRuntimeException(String message, Throwable cause, int code) {
        this(message, cause, code, new ArrayList<>());
    }

    public DocFeatureRuntimeException(String message, Throwable cause, int code, List<String> messages) {
        super(message, cause, code);
        this.messages = messages;
    }

    public DocFeatureRuntimeException(String message, int code, List<String> messages) {
        this(message, null, code, messages);
    }

    public static DocFeatureRuntimeException standardExceptionWrapping(Exception e ) throws DocFeatureRuntimeException {
        throw convertEx( "Doc Feature runtime error", e );
    }

    public static DocFeatureRuntimeException convertEx( String baseMessage, Exception e ) {
        DocFeatureRuntimeException res = null;
        if ( e instanceof DocFeatureRuntimeException ) {
            res = (DocFeatureRuntimeException)e;
        } else {
            res = new DocFeatureRuntimeException( ExConverUtils.defaultMessage(baseMessage, e), e, Result.RESULT_CODE_KO );
        }
        return res;
    }

    public static DocFeatureRuntimeException convertExMethod( String method, Exception e ) {
        return convertEx( ExConverUtils.defaultMethodMessage(method), e );
    }

    public static DocFeatureRuntimeException convertEx( Exception e ) {
        return convertEx( ExConverUtils.DEFAULT_CAUSE_MESSAGE, e );
    }

    /**
     * Convert the exception to DocFeatureRuntimeException
     *
     * RuntimeException are left unchanged
     *
     * @param baseMessage	the base message
     * @param e				the exception
     * @return				the runtime exception
     */
    public static RuntimeException convertToRuntimeEx( String baseMessage, Exception e ) {
        RuntimeException res = null;
        if ( e instanceof RuntimeException ) {
            res = (RuntimeException)e;
        } else {
            res = new DocFeatureRuntimeException( ExConverUtils.defaultMessage(baseMessage, e), e, Result.RESULT_CODE_KO );
        }
        return res;
    }


    /**
     * Convert the exception to DocFeatureRuntimeException
     *
     * RuntimeException are left unchanged
     *
     * @param e				the exception
     * @return				the runtime exception
     */
    public static RuntimeException convertToRuntimeEx( Exception e ) {
        return convertToRuntimeEx( ExConverUtils.DEFAULT_CAUSE_MESSAGE, e );
    }

}
