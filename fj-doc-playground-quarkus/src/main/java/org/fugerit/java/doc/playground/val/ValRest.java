package org.fugerit.java.doc.playground.val;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.fugerit.java.doc.val.pdf.box.PdfboxValidator;
import org.fugerit.java.doc.val.poi.DocValidator;
import org.fugerit.java.doc.val.poi.DocxValidator;
import org.fugerit.java.doc.val.poi.XlsValidator;
import org.fugerit.java.doc.val.poi.XlsxValidator;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Path("/val")
public class ValRest {

	private final static Logger logger = LoggerFactory.getLogger(ValRest.class);

	private static final DocValidatorFacade facade = DocValidatorFacade.newFacadeStrict(ImageValidator.JPG_VALIDATOR,
			ImageValidator.PNG_VALIDATOR, ImageValidator.TIFF_VALIDATOR, PdfboxValidator.DEFAULT, DocxValidator.DEFAULT,
			DocValidator.DEFAULT, XlsxValidator.DEFAULT, XlsValidator.DEFAULT);

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/check")
	public Response check(@MultipartForm ValInput input) {
		Response res = Response.status(Response.Status.BAD_REQUEST).build();
		try {
			ValOutput output = null;
			if (StringUtils.isNotEmpty(input.fileName) && input.file != null) {
				DocTypeValidationResult result = facade.validate(input.fileName, input.file);
				if (result.isResultOk()) {
					output = new ValOutput(true, "Input is valid");
				} else {
					output = new ValOutput(false, "Input is not valid");
				}
			} else {
				output = new ValOutput(false, "No input provided");
			}
			if (output != null) {
				if (output.isValid()) {
					res = Response.ok().entity(output).build();
				} else {
					res = Response.status(Response.Status.BAD_REQUEST).entity(output).build();
				}
			}
		} catch (Exception e) {
			logger.info("Error : " + e, e);
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return res;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/supported_extensions")
	public Response supportedExtensions() {
		return Response.ok().entity( facade.getSupportedExtensions() ).build();
	}

}