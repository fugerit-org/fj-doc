package org.fugerit.java.doc.base.kotlin.dsl

class Meta : HelperDSL.TagWithText( "meta" ) {
   fun info( text: String = "", init: Info.() -> Unit = {} ): Info {
       return initTag(Info(text), init);
   }
   fun header( init: Header.() -> Unit = {} ): Header {
       return initTag(Header(), init);
   }
   fun footer( init: Footer.() -> Unit = {} ): Footer {
       return initTag(Footer(), init);
   }
   fun headerExt( init: HeaderExt.() -> Unit = {} ): HeaderExt {
       return initTag(HeaderExt(), init);
   }
   fun footerExt( init: FooterExt.() -> Unit = {} ): FooterExt {
       return initTag(FooterExt(), init);
   }
   fun background( init: Background.() -> Unit = {} ): Background {
       return initTag(Background(), init);
   }
   fun bookmarkTree( init: BookmarkTree.() -> Unit = {} ): BookmarkTree {
       return initTag(BookmarkTree(), init);
   }


}
