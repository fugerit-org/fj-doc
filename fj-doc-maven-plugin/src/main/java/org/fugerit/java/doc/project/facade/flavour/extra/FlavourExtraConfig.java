package org.fugerit.java.doc.project.facade.flavour.extra;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class FlavourExtraConfig {

    @Getter @Setter
    private Map<String, ParamConfig> paramConfig;

}
