package org.fugerit.java.doc.playground.val;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.ws.rs.core.MediaType;

public class ValInput {

    @RestForm
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private FileUpload file;

	public FileUpload getFile() {
		return file;
	}

	public void setFile(FileUpload file) {
		this.file = file;
	}
    
}