# Fugerit Document Generation Framework (fj-doc)

## Bundle library : simple table (fj-doc-lib-simpletable)

[back to fj-doc index](../README.md)

*Description* :  
This is the first of a series of bundled libraries using fj-doc framework for specific tasks.

*Status* :  
All basic features are implemented.  
  
  
*Quickstart* :
This module is already configured, and can be called just creating the model of the table to be rendered and providing a TypeHandler.

```

		OutputStream os = ....
		DocTypeHandler handler = ...

		// the table model
		SimpleTable simpleTableModel = SimpleTableFacade.newTable( 30, 30, 40 );
		SimpleRow headerRow = new SimpleRow( BooleanUtils.BOOLEAN_TRUE );
		headerRow.addCell( "Name" );
		headerRow.addCell( "Surname" );
		headerRow.addCell( "Title" );
		simpleTableModel.addRow( headerRow );
		SimpleRow luthienRow = new SimpleRow();
		luthienRow.addCell( "Luthien" );
		luthienRow.addCell( "Tinuviel" );
		luthienRow.addCell( "Queen" );
		simpleTableModel.addRow( luthienRow );
		SimpleRow thorinRow = new SimpleRow();
		thorinRow.addCell( "Thorin" );
		thorinRow.addCell( "Oakshield" );
		thorinRow.addCell( "King" );
		simpleTableModel.addRow( thorinRow );
		
		SimpleTableDocConfig docConfig = SimpleTableDocConfig.newConfig();
		docConfig.processSimpleTable(simpleTableModel, handler, os);
		
```