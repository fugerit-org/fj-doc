import org.fugerit.java.doc.base.kotlin.dsl.dslDoc

dslDoc {
    val docTitle = attStr( data, "docTitle" )
    meta {
        info( ( docTitle ) ).name( "doc-title" )
        info( ( "10;10;10;30" ) ).name( "margins" )
        info( ( "fj doc venus sample source Kotlin Template - kts" ) ).name( "doc-subject" )
        info( ( "fugerit79" ) ).name( "dock-author" )
        info( ( "en" ) ).name( "doc-language" )
        info( ( "TitilliumWeb" ) ).name( "default-font-name" )
        info( ( "data-table=print" ) ).name( "excel-table-id" )
        info( ( "data-table" ) ).name( "csv-table-id" )
        footerExt {
            para( '$'+"{currentPage} / "+'$'+"{pageCount}" ).align( "right" )
        }
    }
    body {
        h( docTitle ).headLevel( 1 )
        table {
            row {
                cell { para( "Name" ) }.align( "center" )
                cell { para( "Surname" ) }.align( "center" )
                cell { para( "Title" ) }.align( "center" )
            }.header( true )
            attListMap( data, "listPeople" ).forEach( { e -> row {
                cell { para( attStr( e, "name" ) ) }
                cell { para( attStr( e, "surname" ) ) }
                cell { para( attStr( e, "title" ) ) }
            } } )
        }.columns( 3 ).colwidths( "30;30;40" ).width( 100 ).id( "data-table" ).padding( 2 )
    }
}