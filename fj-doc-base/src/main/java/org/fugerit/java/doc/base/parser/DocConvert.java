package org.fugerit.java.doc.base.parser;

import org.fugerit.java.core.cfg.ConfigException;

import java.io.Reader;
import java.io.Writer;

public interface DocConvert {

    void convert(Reader from, Writer to) throws ConfigException;

}
