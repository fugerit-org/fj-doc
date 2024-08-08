package org.fugerit.java.doc.freemarker.fun;

import freemarker.template.TemplateModelException;

import java.util.List;

public class FMFunHelper {

    private FMFunHelper() {}

    public static void checkFirstRequredWithMessage( List<Object> arguments, String message ) throws TemplateModelException {
        if ( arguments.isEmpty() ) {
            throw new TemplateModelException( message );
        }
    }

    public static void checkFirstRequred( List<Object> arguments ) throws TemplateModelException {
        checkFirstRequredWithMessage( arguments, "At least one parameter is needed" );
    }

}
