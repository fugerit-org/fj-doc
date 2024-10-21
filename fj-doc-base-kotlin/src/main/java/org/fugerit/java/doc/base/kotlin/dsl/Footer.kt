package org.fugerit.java.doc.base.kotlin.dsl

class Footer : HelperDSL.TagWithText( "footer" ) {
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

   fun align( value: String ): Footer = alignType( this, "align", value )
   fun numbered( value: Boolean ): Footer = setAtt( this, "numbered", value )
   fun borderWidth( value: Int ): Footer = borderWidthType( this, "border-width", value )
   fun expectedSize( value: Int ): Footer = setAtt( this, "expected-size", value )

}
