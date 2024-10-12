package org.fugerit.java.doc.playground.init;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
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
import java.util.Base64;
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/init")
    public Response init( @Valid ProjectInitInput data ) {
        return RestHelper.defaultHandle( () -> {
            long time = System.currentTimeMillis();
            ProjectInitOutput output = new ProjectInitOutput();
            String groupIdData = data.getGroupId();
            String artifactIdData = data.getArtifactId();
            try ( ByteArrayOutputStream buffer = new ByteArrayOutputStream() ) {
                String tempDir = System.getProperty("java.io.tmpdir");
                File projectDir = new File( tempDir, artifactIdData+"_"+ System.currentTimeMillis() );
                projectDir.mkdir();
                log.info( "tempDir : {}, outputFolder : {}", tempDir, projectDir);
                checkIfInTempFolder( projectDir );    // security check
                File realDir = new File( projectDir, artifactIdData );
                checkIfInTempFolder( realDir );    // security check
                log.info( "project init folder : {}", realDir.getAbsolutePath() );
                MojoInit mojoInit = new MojoInit() {
                    @Override
                    public void execute() throws MojoExecutionException, MojoFailureException {
                        this.baseInitFolder = projectDir.getAbsolutePath();
                        this.projectVersion = data.getProjectVersion();
                        this.groupId = groupIdData;
                        this.version = data.getVenusVersion();
                        this.artifactId = artifactIdData;
                        this.javaRelease = String.valueOf( data.getJavaVersion() );
                        this.extensions = StringUtils.concat( ",", data.getExtensionList() );
                        this.addDocFacade = true;
                        this.force = true;
                        this.addVerifyPlugin = true;
                        this.addJunit5 = true;
                        this.addLombok = true;
                        this.flavour = data.getFlavour();
                        this.flavourVersion = data.getFlavourVersion();
                        super.execute();
                    }
                };
                mojoInit.execute();
                zipFolder( realDir, buffer );
                byte[] byteArray = buffer.toByteArray();
                output.setContent( Base64.getEncoder().encodeToString( byteArray ) );
                log.info( "zip size : {}", byteArray.length );
                checkIfInTempFolder( projectDir );    // security check
                FileUtils.deleteDirectory( projectDir );
                output.setMessage( String.format( "Project init OK : %s:%s, time:%s",
                        groupIdData, artifactIdData,
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
        if ( !file.toPath().normalize().startsWith(tempDir.toPath().normalize()) ) {
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
