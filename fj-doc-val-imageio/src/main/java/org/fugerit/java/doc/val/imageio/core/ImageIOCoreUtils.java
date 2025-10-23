package org.fugerit.java.doc.val.imageio.core;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

@Slf4j
public class ImageIOCoreUtils {

    private ImageIOCoreUtils() {}

    public static DocTypeValidationResult validateMetadata(InputStream is, String imageFormat, BiFunction<IIOMetadata, List<String>, Boolean> tagFunction) {
        ImageReader reader = null;
        try (ImageInputStream iis = ImageIO.createImageInputStream(is)) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (!readers.hasNext()) {
                log.warn("No ImageReader available for input stream");
                return DocTypeValidationResult.newFail().withValidationMessage( "No ImageReader available" );
            }

            reader = readers.next();
            reader.setInput(iis, true);

            IIOMetadata meta = reader.getImageMetadata(0);

            String[] formatNames = meta.getMetadataFormatNames();
            for (String format : formatNames) {
                log.debug("Metadata format: {}", format);
                Node root = meta.getAsTree(format);
                dumpNode(root, 0);
            }

            List<String> missingTags = new ArrayList<>();

            if ( !tagFunction.apply( meta, missingTags ) ) {
                return DocTypeValidationResult.newFail().withValidationMessage( String.format( "No %s metadata available", imageFormat ) );
            }

            if (missingTags.isEmpty()) {
                return DocTypeValidationResult.newOk();
            } else {
                return DocTypeValidationResult.newFail().withValidationMessage( String.format( "Missing tags : %s", missingTags ) );
            }

        } catch (Exception e) {
            String message = String.format( "Error reading %s metadata from stream %s", imageFormat, e );
            log.error( message, e );
            return DocTypeValidationResult.newFail().withMainException( e );
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
    }

    public static void dumpNode(Node node, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }
        String indent = sb.toString();
        log.debug("{}Node: {}", indent, node.getNodeName());

        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attr = attributes.item(i);
                log.info("{}  @{} = {}", indent, attr.getNodeName(), attr.getNodeValue());
            }
        }

        Node child = node.getFirstChild();
        while (child != null) {
            dumpNode(child, level + 1);
            child = child.getNextSibling();
        }
    }

}
