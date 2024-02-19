package org.fugerit.java.doc.playground.val;

import java.io.File;
import java.io.FileInputStream;

import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.doc.playground.RestHelper;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.fugerit.java.doc.val.core.basic.XmlValidator;
import org.fugerit.java.doc.val.p7m.P7MValidator;
import org.fugerit.java.doc.val.pdf.box.PdfboxValidator;
import org.fugerit.java.doc.val.poi.DocValidator;
import org.fugerit.java.doc.val.poi.DocxValidator;
import org.fugerit.java.doc.val.poi.XlsValidator;
import org.fugerit.java.doc.val.poi.XlsxValidator;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@Path("/val")
public class ValRest {

	private static final DocValidatorFacade facade = DocValidatorFacade.newFacadeStrict(ImageValidator.JPG_VALIDATOR,
			ImageValidator.PNG_VALIDATOR, ImageValidator.TIFF_VALIDATOR, PdfboxValidator.DEFAULT, DocxValidator.DEFAULT,
			DocValidator.DEFAULT, XlsxValidator.DEFAULT, XlsValidator.DEFAULT, P7MValidator.DEFAULT, XmlValidator.DEFAULT );

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/check")
	public Response check( ValInput input) {
		return RestHelper.defaultHandle( () -> {
			FileUpload file = input.getFile();
			File tempFile = file.uploadedFile().toFile();
			return ValUtils.doIfInTmpFolder( tempFile, () -> {
				Response res = null;
				ValOutput output = null;
				try ( FileInputStream fis = new FileInputStream( tempFile ) ) {
					DocTypeValidationResult result = facade.validate( file.fileName(), fis );
					if (result.isResultOk()) {
						output = new ValOutput(true, "Input is valid");
					} else {
						output = new ValOutput(false, "Input is not valid");
					}
				}
				if (output.isValid()) {
					res = Response.ok().entity(output).build();
				} else {
					res = Response.status(Response.Status.BAD_REQUEST).entity(output).build();
				}
				return res;
			} );
		} );
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/supported_extensions")
	public Response supportedExtensions() {
		return Response.ok().entity( facade.getSupportedExtensions() ).build();
	}

}