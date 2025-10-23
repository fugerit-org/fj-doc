package org.fugerit.java.doc.val.imageio.tiff;

import com.twelvemonkeys.imageio.metadata.tiff.TIFF;
import com.twelvemonkeys.imageio.metadata.tiff.TIFFEntry;
import com.twelvemonkeys.imageio.plugins.tiff.TIFFImageMetadata;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.fugerit.java.doc.val.imageio.core.ImageIOCoreUtils;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ImageIOTiffValidator extends AbstractDocTypeValidator {

    public static final String EXTENSION = ImageValidator.FORMAT_TIFF;

    public static final String MIME_TYPE = ImageValidator.MIME_TIFF;

    private static final Integer[] DEFAULT_ID_TAGS = { TIFF.TAG_X_RESOLUTION,  TIFF.TAG_Y_RESOLUTION,
            TIFF.TAG_ICC_PROFILE, TIFF.TAG_IMAGE_WIDTH, TIFF.TAG_IMAGE_HEIGHT, TIFF.TAG_SAMPLES_PER_PIXEL };

    public static final List<Integer> DEFAULT_IT_TAG_LIST = Collections.unmodifiableList( Arrays.asList( DEFAULT_ID_TAGS ) );

    private Integer[] idTags;

    public ImageIOTiffValidator( Integer[] idTags ) {
        super( MIME_TYPE, EXTENSION );
        this.idTags = idTags;
    }

    public ImageIOTiffValidator() {
        this( DEFAULT_ID_TAGS );
    }

    @Override
    public DocTypeValidationResult validate(InputStream is) {
        return ImageIOCoreUtils.validateMetadata( is, "TIFF", (meta, missingTags ) -> {
            if (meta instanceof TIFFImageMetadata) {
                for ( int idTag : idTags ) {
                    TIFFImageMetadata tiffMeta = (TIFFImageMetadata) meta;
                    TIFFEntry entry = (TIFFEntry) tiffMeta.getTIFFField(idTag);
                    if (entry == null) {
                        missingTags.add( ImageIOTiffTags.tagDescription(idTag) );
                    }
                }
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } );
    }

}
