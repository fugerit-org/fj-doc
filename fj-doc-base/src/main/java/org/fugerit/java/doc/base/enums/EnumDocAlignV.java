package org.fugerit.java.doc.base.enums;

public enum EnumDocAlignV {

    ALIGN_TOP(4, "top"),
    ALIGN_MIDDLE(5, "middle"),
    ALIGN_BOTTOM(6, "bottom"),
    ALIGN_UNSET(-1, "unset");

    private final int id;
    private final String value;

    EnumDocAlignV(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public static EnumDocAlignV fromValue(String value) {
        for (EnumDocAlignV style : EnumDocAlignV.values()) {
            if (style.value.equals(value)) {
                return style;
            }
        }
        return null;
    }

    public static int idFromValueWithDefault(String value, int defaultStyle) {
        EnumDocAlignV docStyle = EnumDocAlignV.fromValue( value );
        if( docStyle == null ) {
            return defaultStyle;
        } else {
            return docStyle.getId();
        }
    }

}
