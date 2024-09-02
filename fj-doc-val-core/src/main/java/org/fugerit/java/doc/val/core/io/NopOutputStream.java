package org.fugerit.java.doc.val.core.io;

import java.io.IOException;
import java.io.OutputStream;

public class NopOutputStream extends OutputStream {

    @Override
    public void write(byte[] b) throws IOException {
        // do nothing implementation
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        // do nothing implementation
    }

    @Override
    public void write(int b) throws IOException {
        // do nothing implementation
    }

}
