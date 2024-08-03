package org.fugerit.java.doc.mod.fop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.FopFactory;

@AllArgsConstructor
public class FopConfigWrap {

    @Getter
    private FopFactory fopFactory;

    @Getter private FOUserAgent foUserAgent;

}