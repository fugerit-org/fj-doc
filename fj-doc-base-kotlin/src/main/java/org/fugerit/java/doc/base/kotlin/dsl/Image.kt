package org.fugerit.java.doc.base.kotlin.dsl

class Image : HelperDSL.TagWithText( "image" ) {

   fun url( value: String ): Image = urlType( this, "url", value )
   fun type( value: String ): Image = imageType( this, "type", value )
   fun scaling( value: Int ): Image = scalingType( this, "scaling", value )
   fun base64( value: String ): Image = base64Type( this, "base64", value )
   fun alt( value: String ): Image = altType( this, "alt", value )
   fun align( value: String ): Image = alignType( this, "align", value )

}
