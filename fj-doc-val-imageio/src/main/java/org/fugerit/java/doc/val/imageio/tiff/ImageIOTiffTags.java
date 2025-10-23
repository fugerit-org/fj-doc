package org.fugerit.java.doc.val.imageio.tiff;

import com.twelvemonkeys.imageio.metadata.tiff.TIFF;

import java.util.HashMap;
import java.util.Map;

public class ImageIOTiffTags {

    private ImageIOTiffTags() {}

    public static String tagLabel( int idTag ) {
        return TIFF_TAG_NAMES.get( idTag );
    }

    public static String tagDescription( int idTag ) {
        String label = tagLabel( idTag );
        if ( label == null ) {
            return String.valueOf( idTag );
        } else {
            return String.format( "%s (%s)", idTag, label );
        }
    }

    private static final Map<Integer, String> TIFF_TAG_NAMES = new HashMap<>();
    static {
        TIFF_TAG_NAMES.put(TIFF.TAG_EXIF_IFD, "ExifIFD");
        TIFF_TAG_NAMES.put(TIFF.TAG_GPS_IFD, "GpsIFD");
        TIFF_TAG_NAMES.put(TIFF.TAG_INTEROP_IFD, "InteropIFD");
        TIFF_TAG_NAMES.put(TIFF.TAG_IMAGE_WIDTH, "ImageWidth");
        TIFF_TAG_NAMES.put(TIFF.TAG_IMAGE_HEIGHT, "ImageHeight");
        TIFF_TAG_NAMES.put(TIFF.TAG_BITS_PER_SAMPLE, "BitsPerSample");
        TIFF_TAG_NAMES.put(TIFF.TAG_COMPRESSION, "Compression");
        TIFF_TAG_NAMES.put(TIFF.TAG_PHOTOMETRIC_INTERPRETATION, "PhotometricInterpretation");
        TIFF_TAG_NAMES.put(TIFF.TAG_FILL_ORDER, "FillOrder");
        TIFF_TAG_NAMES.put(TIFF.TAG_ORIENTATION, "Orientation");
        TIFF_TAG_NAMES.put(TIFF.TAG_SAMPLES_PER_PIXEL, "SamplesPerPixel");
        TIFF_TAG_NAMES.put(TIFF.TAG_PLANAR_CONFIGURATION, "PlanarConfiguration");
        TIFF_TAG_NAMES.put(TIFF.TAG_SAMPLE_FORMAT, "SampleFormat");
        TIFF_TAG_NAMES.put(TIFF.TAG_YCBCR_SUB_SAMPLING, "YCbCrSubSampling");
        TIFF_TAG_NAMES.put(TIFF.TAG_YCBCR_POSITIONING, "YCbCrPositioning");
        TIFF_TAG_NAMES.put(TIFF.TAG_X_RESOLUTION, "XResolution");
        TIFF_TAG_NAMES.put(TIFF.TAG_Y_RESOLUTION, "YResolution");
        TIFF_TAG_NAMES.put(TIFF.TAG_X_POSITION, "XPosition");
        TIFF_TAG_NAMES.put(TIFF.TAG_Y_POSITION, "YPosition");
        TIFF_TAG_NAMES.put(TIFF.TAG_RESOLUTION_UNIT, "ResolutionUnit");
        TIFF_TAG_NAMES.put(TIFF.TAG_STRIP_OFFSETS, "StripOffsets");
        TIFF_TAG_NAMES.put(TIFF.TAG_ROWS_PER_STRIP, "RowsPerStrip");
        TIFF_TAG_NAMES.put(TIFF.TAG_STRIP_BYTE_COUNTS, "StripByteCounts");
        TIFF_TAG_NAMES.put(TIFF.TAG_FREE_OFFSETS, "FreeOffsets");
        TIFF_TAG_NAMES.put(TIFF.TAG_FREE_BYTE_COUNTS, "FreeByteCounts");
        TIFF_TAG_NAMES.put(TIFF.TAG_JPEG_INTERCHANGE_FORMAT, "JPEGInterchangeFormat");
        TIFF_TAG_NAMES.put(TIFF.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, "JPEGInterchangeFormatLength");
        TIFF_TAG_NAMES.put(TIFF.TAG_GROUP3OPTIONS, "Group3Options");
        TIFF_TAG_NAMES.put(TIFF.TAG_GROUP4OPTIONS, "Group4Options");
        TIFF_TAG_NAMES.put(TIFF.TAG_TRANSFER_FUNCTION, "TransferFunction");
        TIFF_TAG_NAMES.put(TIFF.TAG_PREDICTOR, "Predictor");
        TIFF_TAG_NAMES.put(TIFF.TAG_WHITE_POINT, "WhitePoint");
        TIFF_TAG_NAMES.put(TIFF.TAG_PRIMARY_CHROMATICITIES, "PrimaryChromaticities");
        TIFF_TAG_NAMES.put(TIFF.TAG_COLOR_MAP, "ColorMap");
        TIFF_TAG_NAMES.put(TIFF.TAG_INK_SET, "InkSet");
        TIFF_TAG_NAMES.put(TIFF.TAG_INK_NAMES, "InkNames");
        TIFF_TAG_NAMES.put(TIFF.TAG_NUMBER_OF_INKS, "NumberOfInks");
        TIFF_TAG_NAMES.put(TIFF.TAG_EXTRA_SAMPLES, "ExtraSamples");
        TIFF_TAG_NAMES.put(TIFF.TAG_TRANSFER_RANGE, "TransferRange");
        TIFF_TAG_NAMES.put(TIFF.TAG_YCBCR_COEFFICIENTS, "YCbCrCoefficients");
        TIFF_TAG_NAMES.put(TIFF.TAG_REFERENCE_BLACK_WHITE, "ReferenceBlackWhite");
        TIFF_TAG_NAMES.put(TIFF.TAG_DATE_TIME, "DateTime");
        TIFF_TAG_NAMES.put(TIFF.TAG_DOCUMENT_NAME, "DocumentName");
        TIFF_TAG_NAMES.put(TIFF.TAG_IMAGE_DESCRIPTION, "ImageDescription");
        TIFF_TAG_NAMES.put(TIFF.TAG_MAKE, "Make");
        TIFF_TAG_NAMES.put(TIFF.TAG_MODEL, "Model");
        TIFF_TAG_NAMES.put(TIFF.TAG_PAGE_NAME, "PageName");
        TIFF_TAG_NAMES.put(TIFF.TAG_PAGE_NUMBER, "PageNumber");
        TIFF_TAG_NAMES.put(TIFF.TAG_SOFTWARE, "Software");
        TIFF_TAG_NAMES.put(TIFF.TAG_ARTIST, "Artist");
        TIFF_TAG_NAMES.put(TIFF.TAG_HOST_COMPUTER, "HostComputer");
        TIFF_TAG_NAMES.put(TIFF.TAG_COPYRIGHT, "Copyright");
        TIFF_TAG_NAMES.put(TIFF.TAG_SUBFILE_TYPE, "SubfileType");
        TIFF_TAG_NAMES.put(TIFF.TAG_OLD_SUBFILE_TYPE, "OldSubfileType");
        TIFF_TAG_NAMES.put(TIFF.TAG_SUB_IFD, "SubIFD");
        TIFF_TAG_NAMES.put(TIFF.TAG_XMP, "XMP");
        TIFF_TAG_NAMES.put(TIFF.TAG_IPTC, "IPTC");
        TIFF_TAG_NAMES.put(TIFF.TAG_PHOTOSHOP, "Photoshop");
        TIFF_TAG_NAMES.put(TIFF.TAG_PHOTOSHOP_IMAGE_SOURCE_DATA, "PhotoshopImageSourceData");
        TIFF_TAG_NAMES.put(TIFF.TAG_PHOTOSHOP_ANNOTATIONS, "PhotoshopAnnotations");
        TIFF_TAG_NAMES.put(TIFF.TAG_ICC_PROFILE, "ICCProfile");
        TIFF_TAG_NAMES.put(TIFF.TAG_MODI_BLC, "ModiBLC");
        TIFF_TAG_NAMES.put(TIFF.TAG_MODI_VECTOR, "ModiVector");
        TIFF_TAG_NAMES.put(TIFF.TAG_MODI_PTC, "ModiPTC");
        TIFF_TAG_NAMES.put(TIFF.TAG_MODI_PLAIN_TEXT, "ModiPlainText");
        TIFF_TAG_NAMES.put(TIFF.TAG_MODI_OLE_PROPERTY_SET, "ModiOLEPropertySet");
        TIFF_TAG_NAMES.put(TIFF.TAG_MODI_TEXT_POS_INFO, "ModiTextPosInfo");
        TIFF_TAG_NAMES.put(TIFF.TAG_TILE_WIDTH, "TileWidth");
        TIFF_TAG_NAMES.put(TIFF.TAG_TILE_HEIGTH, "TileHeight");
        TIFF_TAG_NAMES.put(TIFF.TAG_TILE_OFFSETS, "TileOffsets");
        TIFF_TAG_NAMES.put(TIFF.TAG_TILE_BYTE_COUNTS, "TileByteCounts");
        TIFF_TAG_NAMES.put(TIFF.TAG_JPEG_TABLES, "JPEGTables");
        TIFF_TAG_NAMES.put(TIFF.TAG_OLD_JPEG_PROC, "OldJPEGProc");
        TIFF_TAG_NAMES.put(TIFF.TAG_OLD_JPEG_Q_TABLES, "OldJPEGQTables");
        TIFF_TAG_NAMES.put(TIFF.TAG_OLD_JPEG_DC_TABLES, "OldJPEGDCTables");
    }

}
