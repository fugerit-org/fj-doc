import org.fugerit.java.doc.base.kotlin.dsl.dslDoc

dslDoc {
    meta {
        info( ( "DSL Kotlin Document From JUnit 2" ) ).name( "doc-title" )
        header {
            align( "center" )
            borderWidth( 1 )
            numbered( true )
            expectedSize( 200 )
            phrase( "test" )
            para( "test" )
        }
        footer() {
            align( "right" )
            borderWidth( 1 )
            numbered( true )
            expectedSize( 200 )
            phrase( "test" )
            para( "test" )
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