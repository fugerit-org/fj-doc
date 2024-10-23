package org.fugerit.java.doc.base.kotlin.dsl

/**
 * Para represents the para element.
 *
 * This class will provide function to handle all para attributes and kids 
 */
class Para( text: String = "" ) : HelperDSL.TagWithText( "para" ) {

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
     * Function handling id attribute of the Para with specific check on type.
     * @return the value for the id attribute.
     */
   fun id( value: String ): Para = idType( this, "id", value )
    /**
     * Function handling style attribute of the Para with specific check on type.
     * @return the value for the style attribute.
     */
   fun style( value: String ): Para = styleType( this, "style", value )
    /**
     * Function handling align attribute of the Para with specific check on type.
     * @return the value for the align attribute.
     */
   fun align( value: String ): Para = alignType( this, "align", value )
    /**
     * Function handling fontName attribute of the Para with specific check on type.
     * @return the value for the fontName attribute.
     */
   fun fontName( value: String ): Para = fontNameType( this, "font-name", value )
    /**
     * Function handling leading attribute of the Para with specific check on type.
     * @return the value for the leading attribute.
     */
   fun leading( value: Int ): Para = leadingType( this, "leading", value )
    /**
     * Function handling backColor attribute of the Para with specific check on type.
     * @return the value for the backColor attribute.
     */
   fun backColor( value: String ): Para = colorType( this, "back-color", value )
    /**
     * Function handling foreColor attribute of the Para with specific check on type.
     * @return the value for the foreColor attribute.
     */
   fun foreColor( value: String ): Para = colorType( this, "fore-color", value )
    /**
     * Function handling type attribute of the Para with specific check on type.
     * @return the value for the type attribute.
     */
   fun type( value: String ): Para = dataType( this, "type", value )
    /**
     * Function handling format attribute of the Para with specific check on type.
     * @return the value for the format attribute.
     */
   fun format( value: String ): Para = formatType( this, "format", value )
    /**
     * Function handling size attribute of the Para with specific check on type.
     * @return the value for the size attribute.
     */
   fun size( value: Int ): Para = fontSizeType( this, "size", value )
    /**
     * Function handling textIndent attribute of the Para with specific check on type.
     * @return the value for the textIndent attribute.
     */
   fun textIndent( value: Int ): Para = textIndentType( this, "text-indent", value )
    /**
     * Function handling spaceBefore attribute of the Para with specific check on type.
     * @return the value for the spaceBefore attribute.
     */
   fun spaceBefore( value: Int ): Para = spaceType( this, "space-before", value )
    /**
     * Function handling spaceAfter attribute of the Para with specific check on type.
     * @return the value for the spaceAfter attribute.
     */
   fun spaceAfter( value: Int ): Para = spaceType( this, "space-after", value )
    /**
     * Function handling spaceLeft attribute of the Para with specific check on type.
     * @return the value for the spaceLeft attribute.
     */
   fun spaceLeft( value: Int ): Para = spaceType( this, "space-left", value )
    /**
     * Function handling spaceRight attribute of the Para with specific check on type.
     * @return the value for the spaceRight attribute.
     */
   fun spaceRight( value: Int ): Para = spaceType( this, "space-right", value )
    /**
     * Function handling whiteSpaceCollapse attribute of the Para with specific check on type.
     * @return the value for the whiteSpaceCollapse attribute.
     */
   fun whiteSpaceCollapse( value: Boolean ): Para = whiteSpaceCollapsType( this, "white-space-collapse", value )

}
