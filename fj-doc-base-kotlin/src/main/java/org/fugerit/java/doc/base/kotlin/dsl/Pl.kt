package org.fugerit.java.doc.base.kotlin.dsl

class Pl : HelperDSL.TagWithText( "pl" ) {
    /**
     * Creates a new default Phrase instance.
     * @return the new instance.
     */
   fun phrase( text: String = "", init: Phrase.() -> Unit = {} ): Phrase {
       return initTag(Phrase(text), init);
   }


}
