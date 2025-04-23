package org.fugerit.java.doc.lib.direct.config;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

public class VenusDirectConfigChain {

    @Getter @Setter
    private String chainId;

    @Getter @Setter
    private LinkedHashMap<String, Object> dataModel;

}
