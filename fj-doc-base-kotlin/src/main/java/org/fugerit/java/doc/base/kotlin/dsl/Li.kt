package org.fugerit.java.doc.base.kotlin.dsl

class Li : HelperDSL.TagWithText( "li" ) {
   fun list( init: List.() -> Unit = {} ): List {
       return initTag(List(), init);
   }
   fun para( text: String = "", init: Para.() -> Unit = {} ): Para {
       return initTag(Para(text), init);
   }
   fun h( text: String = "", init: H.() -> Unit = {} ): H {
       return initTag(H(text), init);
   }
   fun pl( init: Pl.() -> Unit = {} ): Pl {
       return initTag(Pl(), init);
   }

   fun id( value: String ): Li = idType( this, "id", value )

}
