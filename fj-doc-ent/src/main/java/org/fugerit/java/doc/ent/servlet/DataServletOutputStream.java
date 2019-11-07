package org.fugerit.java.doc.ent.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

public class DataServletOutputStream extends ServletOutputStream {

	private ByteArrayOutputStream baos;
	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	public void write(int b) throws IOException {
		baos.write( b );
	}

	/*
	 * <jdl:section>
	 * 		<jdl:text lang='it'><p>Crea una nuova istanza di DataServletOutputStream.</p></jdl:text>
	 * 		<jdl:text lang='en'><p>Creates a new instance of DataServletOutputStream.</p></jdl:text>
	 * </jdl:section>
	 *
	 * @param baos
	 */
	public DataServletOutputStream(ByteArrayOutputStream baos) {
		this.baos = baos;
	}
	
}