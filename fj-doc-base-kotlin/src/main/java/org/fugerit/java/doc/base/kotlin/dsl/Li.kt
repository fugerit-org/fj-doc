package org.fugerit.java.doc.base.kotlin.dsl

class Li : HelperDSL.TagWithText( "li" ) {
    /**
     * Creates a new default List instance.
     * @return the new instance.
     */
   fun list( init: List.() -> Unit = {} ): List {
       return initTag(List(), init);
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
     * Creates a new default Pl instance.
     * @return the new instance.
     */
   fun pl( init: Pl.() -> Unit = {} ): Pl {
       return initTag(Pl(), init);
   }

   fun id( value: String ): Li = idType( this, "id", value )

}
