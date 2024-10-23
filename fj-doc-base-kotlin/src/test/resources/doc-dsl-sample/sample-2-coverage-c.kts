import org.fugerit.java.doc.base.kotlin.dsl.dslDoc

dslDoc {
    metadata {
        info( ( "DSL Kotlin Document From JUnit 2" ) ).name( "doc-title" )
        info {}
        header {
            align( "center" )
            borderWidth( 1 )
            numbered( true )
            expectedSize( 200 )
            h( "head 1" ).headLevel( 3 )
            image().align( "center" ).alt( "avatar" ).url( "https://avatars.githubusercontent.com/u/37816284?s=96&v=4" )
        }
        footer() {
            align( "right" )
            borderWidth( 1 )
            numbered( true )
            expectedSize( 200 )
            h( "head 1" ).headLevel( 3 )
            image().align( "center" ).alt( "avatar" ).url( "https://avatars.githubusercontent.com/u/37816284?s=96&v=4" )
        }
    }
    body {
        para( "Concise paragraph" )
            .align( "center" )
            .style( "bold" )
            .whiteSpaceCollapse( true )
            .spaceLeft( 10 )
            .leading( 1 )
            .format( "{}" )
        para {
            setText("Verbose paragraph")
            align( "left" )
            style( "italic" )
            whiteSpaceCollapse( true )
            spaceAfter( 10 )
            foreColor( "#000000" )
            backColor( "#ffffff" )
        }
        phrase( "Inline phrase" ).style( "normal" ).size( 8 )
        table {
            row {
                cell { para( "col 1" )  }
                cell { para( "col 2" )  }
            }.header( true )
            row {
                cell { para( "data 1" )  }
                cell { para( "data 2" )  }
            }
        }.width( 100 ).columns( 2 ).colwidths( "50;50" )
    }
}