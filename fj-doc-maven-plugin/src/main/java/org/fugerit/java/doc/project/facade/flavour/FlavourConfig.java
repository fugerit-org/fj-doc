package org.fugerit.java.doc.project.facade.flavour;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class FlavourConfig {

    @Getter @Setter
    private String flavour;

    @Getter @Setter
    private List<ProcessEntry> process;

}
