package org.fugerit.java.doc.base.enums;

public enum EnumDocAlignH {

    ALIGN_LEFT(1, "left"),
    ALIGN_CENTER(2, "center"),
    ALIGN_RIGHT(3, "right"),
    ALIGN_JUSTIFY(9, "justify"),
    ALIGN_JUSTIFY_ALL(8, "justifyall"),
    ALIGN_UNSET(-1, "unset");

    private final int id;
    private final String value;

    EnumDocAlignH(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public static EnumDocAlignH fromValue(String value) {
        for (EnumDocAlignH style : EnumDocAlignH.values()) {
            if (style.value.equals(value)) {
                return style;
            }
        }
        return null;
    }

    public static int idFromValueWithDefault(String value, int defaultStyle) {
        EnumDocAlignH docStyle = EnumDocAlignH.fromValue( value );
        if( docStyle == null ) {
            return defaultStyle;
        } else {
            return docStyle.getId();
        }
    }

}
