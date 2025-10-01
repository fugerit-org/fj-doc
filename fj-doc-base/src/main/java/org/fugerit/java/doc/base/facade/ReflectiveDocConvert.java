package org.fugerit.java.doc.base.facade;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.doc.base.parser.DocConvert;

import java.io.Reader;
import java.io.Writer;

public class ReflectiveDocConvert implements DocConvert {

    @Override
    public void convert(Reader from, Writer to) throws ConfigException {
        ConfigException.apply( () -> StreamIO.pipeCharCloseBoth( from, to ) );
    }

}
