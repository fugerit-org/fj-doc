package org.fugerit.java.doc.playground.facade;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InputFacade {

    private InputFacade() {
    }

    public static final String FORMAT_XML = "XML";
    public static final String FORMAT_JSON = "JSON";
    public static final String FORMAT_YAML = "YAML";

    public static final String FORMAT_KTS = "KTS";

    public static final String FORMAT_FTLX = "FTLX";

    protected static final String[] FORMATS_A = { FORMAT_XML, FORMAT_JSON, FORMAT_YAML, FORMAT_FTLX, FORMAT_KTS };

    public static final List<String> FORMAT_LIST = Collections.unmodifiableList(Arrays.asList(FORMATS_A));

}
