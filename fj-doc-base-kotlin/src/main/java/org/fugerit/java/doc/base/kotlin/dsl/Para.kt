package org.fugerit.java.doc.base.kotlin.dsl

class Para( text: String = "" ) : HelperDSL.TagWithText( "para" ) {

   init { setText(text) }

   fun setText( value: String ) { addKid( HelperDSL.TextElement( value ) ) }

   fun phrase( text: String = "", init: Phrase.() -> Unit = {} ): Phrase {
       return initTag(Phrase(text), init);
   }
   fun para( text: String = "", init: Para.() -> Unit = {} ): Para {
       return initTag(Para(text), init);
   }
   fun h( text: String = "", init: H.() -> Unit = {} ): H {
       return initTag(H(text), init);
   }

   fun id( value: String ): Para = idType( this, "id", value )
   fun style( value: String ): Para = styleType( this, "style", value )
   fun align( value: String ): Para = alignType( this, "align", value )
   fun fontName( value: String ): Para = fontNameType( this, "font-name", value )
   fun leading( value: Int ): Para = leadingType( this, "leading", value )
   fun backColor( value: String ): Para = colorType( this, "back-color", value )
   fun foreColor( value: String ): Para = colorType( this, "fore-color", value )
   fun type( value: String ): Para = dataType( this, "type", value )
   fun format( value: String ): Para = formatType( this, "format", value )
   fun size( value: Int ): Para = fontSizeType( this, "size", value )
   fun textIndent( value: Int ): Para = textIndentType( this, "text-indent", value )
   fun spaceBefore( value: Int ): Para = spaceType( this, "space-before", value )
   fun spaceAfter( value: Int ): Para = spaceType( this, "space-after", value )
   fun spaceLeft( value: Int ): Para = spaceType( this, "space-left", value )
   fun spaceRight( value: Int ): Para = spaceType( this, "space-right", value )
   fun whiteSpaceCollapse( value: Boolean ): Para = whiteSpaceCollapsType( this, "white-space-collapse", value )

}
