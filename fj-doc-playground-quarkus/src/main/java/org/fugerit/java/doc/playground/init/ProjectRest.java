package org.fugerit.java.doc.playground.init;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.checkpoint.CheckpointUtils;
import org.fugerit.java.core.util.collection.OptionItem;
import org.fugerit.java.doc.maven.MojoInit;
import org.fugerit.java.doc.playground.RestHelper;
import org.fugerit.java.doc.project.facade.ModuleFacade;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Setter
@ApplicationScoped
@Slf4j
@Path("/project")
public class ProjectRest {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/extensions-list")
    public Response extensionsList() {
        return RestHelper.defaultHandle( () -> Response.ok().entity(
                ModuleFacade.getModules().stream().sorted()
                        .map(
                                m -> new OptionItem(m, m)
                        ).toList() ).build()
        );
    }

    private File initConfigWorker( String base ) {
        String tempDir = System.getProperty("java.io.tmpdir");
        File outputFolder = new File( tempDir, base+"_"+ System.currentTimeMillis() );
        outputFolder.mkdir();
        log.info( "tempDir : {}, outputFolder : {}", tempDir, outputFolder);
        return outputFolder;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/init")
    public Response init( ProjectInitInput input) {
        return RestHelper.defaultHandle( () -> {
            long time = System.currentTimeMillis();
            ProjectInitOutput output = new ProjectInitOutput();
            try ( ByteArrayOutputStream buffer = new ByteArrayOutputStream() ) {
                File projectDir = this.initConfigWorker( input.getArtifactId() );
                File realDir = new File( projectDir, input.getArtifactId() );
                log.info( "project init folder : {}", realDir.getAbsolutePath() );
                MojoInit mojoInit = new MojoInit() {
                    @Override
                    public void execute() throws MojoExecutionException, MojoFailureException {
                        this.baseInitFolder = projectDir.getAbsolutePath();
                        this.projectVersion = input.getProjectVersion();
                        this.groupId = input.getGroupId();
                        this.version = input.getVenusVersion();
                        this.artifactId = input.getArtifactId();
                        this.javaRelease = input.getJavaVersion();
                        this.extensions = StringUtils.concat( ",", input.getExtensionList() );
                        this.addDocFacade = true;
                        this.force = true;
                        this.addVerifyPlugin = true;
                        super.execute();
                    }
                };
                mojoInit.execute();
                zipFolder( realDir, buffer );
                byte[] data = buffer.toByteArray();
                output.setContent( Base64.getEncoder().encodeToString( data ) );
                log.info( "zip size : {}", data.length );
                checkIfInTempFolder( projectDir );    // security check
                FileUtils.deleteDirectory( projectDir );
                output.setMessage( String.format( "Project init OK : %s:%s, time:%s",
                        input.getGroupId(), input.getArtifactId(),
                        CheckpointUtils.formatTimeDiffMillis( time , System.currentTimeMillis() ) ) );
            } catch ( Exception e ) {
                log.warn( "Error generating document : "+e , e );
                Throwable te = RestHelper.findCause(e);
                output.setMessage( te.getClass().getName()+" :\n"+te.getMessage() );
            }
            return Response.ok().entity( output ).build();
        } );
    }
    public static String ensureEndWithSlash( String name ) {
        if ( name.endsWith( "/" ) ) {
            return name;
        } else {
            return name+"/";
        }
    }
    public static void checkIfInTempFolder( File file ) throws IOException {
        File tempDir = new File( System.getProperty("java.io.tmpdir") );
        if ( !file.getCanonicalPath().startsWith( tempDir.getCanonicalPath() ) ) {
            throw new IOException( file.getCanonicalPath() + " is not in temp folder" );
        }
    }
    public static void zipFolder(File sourceFolder, OutputStream fos) throws IOException {
        checkIfInTempFolder( sourceFolder );    // security check
        try (ZipOutputStream zos = new ZipOutputStream(fos)) {
            zipFile(sourceFolder, sourceFolder.getName(), zos);
            zos.flush();
            fos.flush();
        }
    }
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zos) throws IOException {
        checkIfInTempFolder( fileToZip );    // security check
        if (fileToZip.isDirectory()) {
            zos.putNextEntry(new ZipEntry( ensureEndWithSlash( fileName ) ));
            zos.closeEntry();
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zos);
            }
            return;
        }
        try (FileInputStream fis = new FileInputStream(fileToZip)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);
            StreamIO.pipeStream( fis, zos, StreamIO.MODE_CLOSE_NONE );
        }

    }

}
