package org.fugerit.java.doc.base.enums;

public enum EnumDocStyle {

    STYLE_NORMAL(1, "normal"),
    STYLE_BOLD(2, "bold"),
    STYLE_UNDERLINE(3, "underline"),
    STYLE_ITALIC(4, "italic"),
    STYLE_BOLDITALIC(5, "bolditalic"),
    STYLE_UNSET(-1, "unset");

    private final int id;
    private final String value;

    EnumDocStyle(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public static EnumDocStyle fromValue(String value) {
        for (EnumDocStyle style : EnumDocStyle.values()) {
            if (style.value.equals(value)) {
                return style;
            }
        }
        return null;
    }

    public static int idFromValueWithDefault(String value, int defaultStyle) {
        EnumDocStyle docStyle = EnumDocStyle.fromValue( value );
        if( docStyle == null ) {
            return defaultStyle;
        } else {
            return docStyle.getId();
        }
    }

}
