package org.fugerit.java.doc.playground.init;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.checkpoint.CheckpointUtils;
import org.fugerit.java.core.util.collection.OptionItem;
import org.fugerit.java.doc.maven.MojoInit;
import org.fugerit.java.doc.playground.RestHelper;
import org.fugerit.java.doc.project.facade.ModuleFacade;

import java.io.*;
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

    private File initConfigWorker() {
        String tempDir = System.getProperty("java.io.tmpdir");
        File outputFolder = new File( tempDir, "init_"+ UUID.randomUUID().toString() );
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
                File projectDir = this.initConfigWorker();
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
                zipFolder( realDir.getCanonicalPath(), buffer );
                byte[] data = buffer.toByteArray();
                output.setContent( Base64.getEncoder().encodeToString( data ) );
                log.info( "zip size : {}", data.length );
                projectDir.delete();
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

    /**
     * Zips the contents of a folder.
     *
     * @param sourceFolderPath The path to the folder to be zipped.
     * @param fos the stream to write the zip.
     * @throws IOException If an I/O error occurs.
     */
    public static void zipFolder(String sourceFolderPath, OutputStream fos) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(fos);

        File sourceFile = new File(sourceFolderPath);

        // Ensure the source folder exists
        if (!sourceFile.exists()) {
            throw new IOException("The source folder does not exist: " + sourceFolderPath);
        }

        zipFile(sourceFile, sourceFile.getName(), zos);
        zos.flush();
        zos.close();
        fos.close();
    }

    /**
     * Recursively zips a file or folder.
     *
     * @param fileToZip The file or folder to zip.
     * @param fileName The name of the file or folder to zip.
     * @param zos The ZIP output stream.
     * @throws IOException If an I/O error occurs.
     */
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zos) throws IOException {
        if (fileToZip.isHidden()) {
            return; // Skip hidden files
        }

        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zos.putNextEntry(new ZipEntry(fileName));
                zos.closeEntry();
            } else {
                zos.putNextEntry(new ZipEntry(fileName + "/"));
                zos.closeEntry();
            }

            File[] children = fileToZip.listFiles();
            if (children != null) {
                for (File childFile : children) {
                    zipFile(childFile, fileName + "/" + childFile.getName(), zos);
                }
            }
            return;
        }

        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        fis.close();
    }

}
