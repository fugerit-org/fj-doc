package org.fugerit.java.doc.freemarker.fun;

import freemarker.template.TemplateModelException;

import java.util.List;

public class FMFunHelper {

    private FMFunHelper() {}

    public static void checkFirstRequiredWithMessage(List<Object> arguments, String message ) throws TemplateModelException {
        checkParameterNumberWithMessage( arguments, 1, message );
    }

    public static void checkFirstRequired(List<Object> arguments ) throws TemplateModelException {
        checkFirstRequiredWithMessage( arguments, "At least one parameter is needed" );
    }

    public static void checkParameterNumber(List<Object> arguments, int min ) throws TemplateModelException {
        checkParameterNumberWithMessage( arguments, min, String.format( "Minimum %s parameters are required for this function", min ) );
    }

    public static void checkParameterNumberWithMessage(List<Object> arguments, int min, String message ) throws TemplateModelException {
        if ( arguments.size() < min ) {
            throw new TemplateModelException( message );
        }
    }

}
