package org.fugerit.java.doc.base.parser;

import java.io.Reader;
import java.util.Map;

public interface DocEvalWithDataModel {

    /**
     * Function to eval data model
     *
     * @param reader        reader on the script or template to be evaluated
     * @param dataModel     the data model to evaluate
     * @return              the content evaluated
     */
    String evalWithDataModel(Reader reader, Map<String, Object> dataModel);

}
