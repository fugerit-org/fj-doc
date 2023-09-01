package test.org.fugerit.java.doc.base.xml;

import java.io.IOException;
import java.io.Reader;

public class ReaderFail extends Reader {

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		if ( cbuf != null ) {
			throw new IOException( "Must fail for test" );
		}
		return 0;
	}

	@Override
	public void close() throws IOException {
		// no need to do something
	}

}
