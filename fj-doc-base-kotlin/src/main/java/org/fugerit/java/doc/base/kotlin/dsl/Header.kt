package org.fugerit.java.doc.base.kotlin.dsl

class Header : HelperDSL.TagWithText( "header" ) {
   fun para( text: String = "", init: Para.() -> Unit = {} ): Para {
       return initTag(Para(text), init);
   }
   fun image( init: Image.() -> Unit = {} ): Image {
       return initTag(Image(), init);
   }
   fun phrase( text: String = "", init: Phrase.() -> Unit = {} ): Phrase {
       return initTag(Phrase(text), init);
   }
   fun h( text: String = "", init: H.() -> Unit = {} ): H {
       return initTag(H(text), init);
   }

   fun align( value: String ): Header = alignType( this, "align", value )
   fun numbered( value: Boolean ): Header = setAtt( this, "numbered", value )
   fun borderWidth( value: Int ): Header = borderWidthType( this, "border-width", value )
   fun expectedSize( value: Int ): Header = setAtt( this, "expected-size", value )

}
