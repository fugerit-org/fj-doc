package org.fugerit.java.doc.playground.convert;

import org.fugerit.java.doc.playground.RestHelper;
import org.fugerit.java.doc.playground.facade.InputFacade;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@Path("/convert")
public class ConvertRest {

    private static final String INVALID_FORMAT_MESSAGE = "Invalid output format : ";

    private ConvertFacade facade = new ConvertFacade();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/doc")
    public Response convertDoc(ConvertInput input) {
        return RestHelper.defaultHandle(() -> {
            ConvertOutput output = new ConvertOutput();
            String inputFormat = input.getInputFormat();
            String outputFormat = input.getOutputFormat();
            String docContent = input.getDocContent();
            String docOutput = null;
            log.info("format input : {} -> output : {} (prettyPrint:{})", inputFormat, outputFormat, input.isPrettyPrint());
            if (InputFacade.FORMAT_LIST.contains(outputFormat)) {
                docOutput = this.facade.handleConvert(inputFormat, docContent, outputFormat, input.isPrettyPrint());
            } else {
                output.setMessage(INVALID_FORMAT_MESSAGE + outputFormat);
            }
            Response res = null;
            if (docOutput != null) {
                output.setDocOutput(docOutput);
                res = Response.ok().entity(output).build();
            } else {
                res = Response.status(Response.Status.BAD_REQUEST).entity(output).build();
            }
            return res;
        });
    }

}