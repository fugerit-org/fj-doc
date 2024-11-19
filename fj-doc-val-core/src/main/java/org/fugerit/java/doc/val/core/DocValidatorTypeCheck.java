package org.fugerit.java.doc.val.core;

import lombok.Getter;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * This facade tries to retrieve a document's type.
 *
 */
public class DocValidatorTypeCheck {

    @Getter
    private DocValidatorFacade facade;

    private DocValidatorTypeCheck( DocValidatorFacade facade ) {
        this.facade = facade;
    }

    /**
     * Type check of a byte stream against the configured DocTypeValidator facade.
     *
     * It will return <code>null</code> if the stream is not valid for all the validators, otherwise DocTypeValidator.getMimeType() of the first validator matching the type.
     *
     * NOTE: This method always buffer the input, so it can be memory consuming.
     *
     * @param is    the byte stream to check
     * @return      <code>null</code> if the stream is not valid for all the validators, otherwise DocTypeValidator.getMimeType() of the first validator matching the type
     */
    public String checkType(InputStream is) {
        return SafeFunction.get( () -> this.checkType( StreamIO.readBytes( is ) ) );
    }

    /**
     * Type check for an input document against the configured DocTypeValidator facade.
     *
     * It will return <code>null</code> if the stream is not valid for all the validators, otherwise DocTypeValidator.getMimeType() of the first validator matching the type.
     *
     * NOTE: This method always buffer the input, so it can be memory consuming.
     *
     * @param buffer    the document to check
     * @return      <code>null</code> if the input is not valid for all the validators, otherwise DocTypeValidator.getMimeType() of the first validator matching the type
     */
    public String checkType(byte[]  buffer) {
        return SafeFunction.get( () -> {
            for ( DocTypeValidator validator : this.facade.validators() ) {
                try (ByteArrayInputStream input = new ByteArrayInputStream( buffer )) {
                    if ( validator.check( input ) ) {
                        return validator.getMimeType();
                    }
                }
            }
            // default case, type not found
            return null;
        } );
    }

    public static DocValidatorTypeCheck newInstance( DocTypeValidator... validator ) {
        return newInstance( DocValidatorFacade.newFacadeStrict( validator ) );
    }

    public static DocValidatorTypeCheck newInstance( DocValidatorFacade facade ) {
        return new DocValidatorTypeCheck( facade );
    }

}
