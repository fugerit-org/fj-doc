package org.fugerit.java.doc.base.kotlin.dsl

class Metadata : HelperDSL.TagWithText( "metadata" ) {
    /**
     * Creates a new default Info instance.
     * @return the new instance.
     */
   fun info( text: String = "", init: Info.() -> Unit = {} ): Info {
       return initTag(Info(text), init);
   }
    /**
     * Creates a new default Header instance.
     * @return the new instance.
     */
   fun header( init: Header.() -> Unit = {} ): Header {
       return initTag(Header(), init);
   }
    /**
     * Creates a new default Footer instance.
     * @return the new instance.
     */
   fun footer( init: Footer.() -> Unit = {} ): Footer {
       return initTag(Footer(), init);
   }
    /**
     * Creates a new default HeaderExt instance.
     * @return the new instance.
     */
   fun headerExt( init: HeaderExt.() -> Unit = {} ): HeaderExt {
       return initTag(HeaderExt(), init);
   }
    /**
     * Creates a new default FooterExt instance.
     * @return the new instance.
     */
   fun footerExt( init: FooterExt.() -> Unit = {} ): FooterExt {
       return initTag(FooterExt(), init);
   }
    /**
     * Creates a new default Background instance.
     * @return the new instance.
     */
   fun background( init: Background.() -> Unit = {} ): Background {
       return initTag(Background(), init);
   }
    /**
     * Creates a new default BookmarkTree instance.
     * @return the new instance.
     */
   fun bookmarkTree( init: BookmarkTree.() -> Unit = {} ): BookmarkTree {
       return initTag(BookmarkTree(), init);
   }


}
