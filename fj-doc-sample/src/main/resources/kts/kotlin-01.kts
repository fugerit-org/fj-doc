import org.fugerit.java.doc.base.kotlin.dsl.dslDoc

dslDoc {
    meta {
        info( "DSL Kotlin Sample" ).name( "doc-title" )
    }
    body {
        table {
            row {
                cell { para( "Name" ) }.align( "center" )
                cell { para( "Surname" ) }.align( "center" )
                cell { para( "Title" ) }.align( "center" )
            }.header( true )
            attListMap( data, "userList" ).forEach( { e -> row {
                cell { para( attStr( e, "name" ) ) }
                cell { para( attStr( e, "surname" ) ) }
                cell { para( attStr( e, "title" ) ) }
            } } )
        }.width( 100 ).columns( 3 ).colwidths( "30;30;40" ).id( "data-table" ).padding( 2 )
    }
}