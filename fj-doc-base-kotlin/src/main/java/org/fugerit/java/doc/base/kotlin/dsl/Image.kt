package org.fugerit.java.doc.base.kotlin.dsl

/**
 * Image represents the image element.
 *
 * This class will provide function to handle all image attributes and kids 
 */
class Image( text: String = "" ) : HelperDSL.TagWithText( "image" ) {

   init { setText(text) }
    /**
     * Function to set text content for this element.
     */

   fun setText( value: String ) { addKid( HelperDSL.TextElement( value ) ) }


    /**
     * Function handling url attribute of the Image with specific check on type.
     * @return the value for the url attribute.
     */
   fun url( value: String ): Image = urlType( this, "url", value )
    /**
     * Function handling type attribute of the Image with specific check on type.
     * @return the value for the type attribute.
     */
   fun type( value: String ): Image = imageType( this, "type", value )
    /**
     * Function handling scaling attribute of the Image with specific check on type.
     * @return the value for the scaling attribute.
     */
   fun scaling( value: Int ): Image = scalingType( this, "scaling", value )
    /**
     * Function handling base64 attribute of the Image with specific check on type.
     * @return the value for the base64 attribute.
     */
   fun base64( value: String ): Image = base64Type( this, "base64", value )
    /**
     * Function handling alt attribute of the Image with specific check on type.
     * @return the value for the alt attribute.
     */
   fun alt( value: String ): Image = altType( this, "alt", value )
    /**
     * Function handling align attribute of the Image with specific check on type.
     * @return the value for the align attribute.
     */
   fun align( value: String ): Image = alignType( this, "align", value )

}
