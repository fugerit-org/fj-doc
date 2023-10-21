import React, { Component, Fragment } from 'react';
import { FormControl, Select, MenuItem, Grid, FormLabel } from "@mui/material";
import appService from '../common/app-service';

class DocCatalog extends Component {

	constructor(props) {
		super(props);
		this.handleSelectDocument = this.handleSelectDocument.bind(this);
		this.state = {
			currentEntryId: this.props.defaultDocId,
			currentType: this.props.currentType,
			updateType: null,
			entryList: null
		}
	}	

	componentDidMount() {
		this.loadList( this.state.currentType );
	}

    loadList( type ) {
		let reactState = this;
		let catalogUrl = '/catalog/list';
		if ( type != null ) {
			catalogUrl+= '/type/' + type;
		}
		appService.doAjaxMultipart('GET', catalogUrl, null).then(response => {
			if (response.success) {
				reactState.setState({
					entryList: response.result
				}, () => this.loadDocument() )
			} else {
				reactState.setState({
					entryList: null
				})
			}
		})
	}
	
	loadDocument( id ) {
		if ( id == null ) {
			id = this.state.entryList[0].key
		}
		this.setState({ currentEntryId:id })	
		let reactState = this;
		appService.doAjaxMultipart('GET', '/catalog/entry/'+id, null).then(response => {
			if (response.success) {
				reactState.props.handleEditorContent( response.result.docOutput );
				if ( (reactState.props.handleJsonDataContent) && (response.result.jsonData) ) {
					reactState.props.handleJsonDataContent( response.result.jsonData );
				}
			} else {
				reactState.setState({
					currentEntryData: null
				})
			}
		})
	}

	handleSelectDocument = (e) => {
		e.preventDefault();
		this.loadDocument( e.target.value );
	};

	render() {
		let catalogSelection = <Fragment>Loading</Fragment>;
		if ( this.state.entryList != null && this.state.currentEntryId != null ) {
			catalogSelection = <Fragment>
			<Grid container spacing={1}>
			  <Grid item xs={6}>
			    <FormLabel>Document samples catalog</FormLabel>
			  </Grid>
			  <Grid item xs={6}>
					<FormControl fullWidth>
					  <Select
					    id="doc-catalog-select"
					    onChange={this.handleSelectDocument}
					    value={this.state.currentEntryId}
					    defaultValue={this.state.currentEntryId}
					  >
						{this.state.entryList.map( (current) =>  <MenuItem key={current.key} selected={current.key===this.state.currentEntryId} value={current.key}>{current.label}</MenuItem>)}					    
					  </Select>
					</FormControl>	
			  </Grid>
			 </Grid>
			</Fragment>
		}
		return catalogSelection
	}

}

export default DocCatalog;