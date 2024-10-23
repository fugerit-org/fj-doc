package org.fugerit.java.doc.base.kotlin.dsl

/**
 * H represents the h element.
 *
 * This class will provide function to handle all h attributes and kids 
 */
class H( text: String = "" ) : HelperDSL.TagWithText( "h" ) {

   init { setText(text) }

   fun setText( value: String ) { addKid( HelperDSL.TextElement( value ) ) }

    /**
     * Creates a new default Phrase instance.
     * @return the new instance.
     */
   fun phrase( text: String = "", init: Phrase.() -> Unit = {} ): Phrase {
       return initTag(Phrase(text), init);
   }
    /**
     * Creates a new default Para instance.
     * @return the new instance.
     */
   fun para( text: String = "", init: Para.() -> Unit = {} ): Para {
       return initTag(Para(text), init);
   }
    /**
     * Creates a new default H instance.
     * @return the new instance.
     */
   fun h( text: String = "", init: H.() -> Unit = {} ): H {
       return initTag(H(text), init);
   }

    /**
     * Function handling id attribute of the H with specific check on type.
     * @return the value for the id attribute.
     */
   fun id( value: String ): H = idType( this, "id", value )
    /**
     * Function handling style attribute of the H with specific check on type.
     * @return the value for the style attribute.
     */
   fun style( value: String ): H = styleType( this, "style", value )
    /**
     * Function handling align attribute of the H with specific check on type.
     * @return the value for the align attribute.
     */
   fun align( value: String ): H = alignType( this, "align", value )
    /**
     * Function handling fontName attribute of the H with specific check on type.
     * @return the value for the fontName attribute.
     */
   fun fontName( value: String ): H = fontNameType( this, "font-name", value )
    /**
     * Function handling leading attribute of the H with specific check on type.
     * @return the value for the leading attribute.
     */
   fun leading( value: Int ): H = leadingType( this, "leading", value )
    /**
     * Function handling backColor attribute of the H with specific check on type.
     * @return the value for the backColor attribute.
     */
   fun backColor( value: String ): H = colorType( this, "back-color", value )
    /**
     * Function handling foreColor attribute of the H with specific check on type.
     * @return the value for the foreColor attribute.
     */
   fun foreColor( value: String ): H = colorType( this, "fore-color", value )
    /**
     * Function handling type attribute of the H with specific check on type.
     * @return the value for the type attribute.
     */
   fun type( value: String ): H = dataType( this, "type", value )
    /**
     * Function handling format attribute of the H with specific check on type.
     * @return the value for the format attribute.
     */
   fun format( value: String ): H = formatType( this, "format", value )
    /**
     * Function handling size attribute of the H with specific check on type.
     * @return the value for the size attribute.
     */
   fun size( value: Int ): H = fontSizeType( this, "size", value )
    /**
     * Function handling textIndent attribute of the H with specific check on type.
     * @return the value for the textIndent attribute.
     */
   fun textIndent( value: Int ): H = textIndentType( this, "text-indent", value )
    /**
     * Function handling spaceBefore attribute of the H with specific check on type.
     * @return the value for the spaceBefore attribute.
     */
   fun spaceBefore( value: Int ): H = spaceType( this, "space-before", value )
    /**
     * Function handling spaceAfter attribute of the H with specific check on type.
     * @return the value for the spaceAfter attribute.
     */
   fun spaceAfter( value: Int ): H = spaceType( this, "space-after", value )
    /**
     * Function handling spaceLeft attribute of the H with specific check on type.
     * @return the value for the spaceLeft attribute.
     */
   fun spaceLeft( value: Int ): H = spaceType( this, "space-left", value )
    /**
     * Function handling spaceRight attribute of the H with specific check on type.
     * @return the value for the spaceRight attribute.
     */
   fun spaceRight( value: Int ): H = spaceType( this, "space-right", value )
    /**
     * Function handling whiteSpaceCollapse attribute of the H with specific check on type.
     * @return the value for the whiteSpaceCollapse attribute.
     */
   fun whiteSpaceCollapse( value: Boolean ): H = whiteSpaceCollapsType( this, "white-space-collapse", value )
    /**
     * Function handling headLevel attribute of the H with specific check on type.
     * @return the value for the headLevel attribute.
     */
   fun headLevel( value: Int ): H = headLevelType( this, "head-level", value )

}
