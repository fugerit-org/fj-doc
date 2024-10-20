package org.fugerit.java.doc.base.kotlin.dsl

class Pl : HelperDSL.TagWithText( "pl" ) {
   fun phrase( text: String = "", init: Phrase.() -> Unit = {} ): Phrase {
       return initTag(Phrase(text), init);
   }


}
