import React, { Component, Fragment } from 'react';
import { FormControl, Select, MenuItem, Grid, FormLabel, Button } from "@mui/material";
import DocCatalog from './DocCatalog';
import appService from '../common/app-service';

import AceEditor from "react-ace";

import "ace-builds/src-noconflict/mode-xml";
import "ace-builds/src-noconflict/mode-json";
import "ace-builds/src-noconflict/mode-yaml";
import "ace-builds/src-noconflict/theme-xcode";
import "ace-builds/src-noconflict/ext-language_tools";

class DocConversion extends Component {

	constructor(props) {
		super(props);
		this.handleGenerate = this.handleGenerate.bind(this);
		this.handleFormat = this.handleFormat.bind(this);
		this.handleDoc = this.handleDoc.bind(this);
		this.handleEditorContent = this.handleEditorContent.bind(this);
		this.state = {
			inputFormat: 'XML',			
			outputFormat: '',
			docOutput: ''
		}
	}

	handleGenerate = (e) => {
		e.preventDefault();
		if (this.state.outputFormat == null) {
			this.props.handleOpenDialog("Select an output format");
		} else {
			let reactState = this;
				appService.doAjaxJson('POST', '/convert/doc', this.state).then(response => {
					if (response.success) {
						reactState.setState({
							docOutput: response.result.docOutput
						})
					} else if ( response.result.message != null ) {
						this.props.handleOpenDialog( response.result.message + ' (' + response.status+")" );
					} else {
						this.props.handleOpenDialog( 'Generic error (' + response.status+")" );
					}
				})
		}
	};

	handleInputFormat = (e) => {
		e.preventDefault();
		this.setState({
			inputFormat: e.target.value,
		});
	};

	handleFormat = (e) => {
		e.preventDefault();
		this.setState({			
			outputFormat: e.target.value,
		});
	};

	handleDoc( newValue ) {
		this.setState({
			outputFormat: this.state.outputFormat,
			docContent: newValue
		}); 
	};

	handleEditorContent = ( content ) => {
		this.setState(
			{ 
				docContent: content			 
			}
		);
	};

	render() {

		let editorInFormat = 'xml';
		if ( this.state.inputFormat != null ) {
			editorInFormat = this.state.inputFormat.toLowerCase()
		}

		let editorOutFormat = 'xml';
		if ( this.state.outputFormat != null ) {
			editorOutFormat = this.state.outputFormat.toLowerCase()
		}
	
		return <Fragment>

			<Grid container spacing={1}>
			  <Grid item xs={12}>
						<DocCatalog key={this.state.inputFormat}
							currentType={this.state.inputFormat}
							handleEditorContent={this.handleEditorContent}
						/>
			  </Grid>
			  <Grid item xs={6} md={3}>
			    <FormLabel>Convert from</FormLabel>
			  </Grid>
			  <Grid item xs={6} md={3}>
					<FormControl fullWidth>
					  <Select
					    id="input-type-select"
					    onChange={this.handleInputFormat}
						value={this.state.inputFormat}
					  >
					    <MenuItem value='XML'>XML</MenuItem>
					    <MenuItem value='JSON'>JSON</MenuItem>
					    <MenuItem value='YAML'>YAML</MenuItem>
					  </Select>
					</FormControl>	
			  </Grid>
			  <Grid item xs={6} md={3}>
			    <FormLabel>Convert to</FormLabel>
			  </Grid>
			  <Grid item xs={6} md={3}>
					<FormControl fullWidth>
					  <Select
					    id="output-type-select"
					    onChange={this.handleFormat}
						value={this.state.outputFormat}
						displayEmpty
					  >
					  	<MenuItem value=''>Select output format</MenuItem>
					    <MenuItem value='JSON'>JSON</MenuItem>
					    <MenuItem value='YAML'>YAML</MenuItem>
					    <MenuItem value='XML'>XML</MenuItem>
					  </Select>
					</FormControl>	
			  </Grid>
			  <Grid item xs={12}>
				<Button variant="contained" onClick={this.handleGenerate}>Convert</Button>		
			  </Grid>
			  <Grid item xs={12} md={6}>
						<AceEditor
							mode={editorInFormat}
							theme="xcode"
							name="DOC_INPUT"
							editorProps={{ $blockScrolling: true }}
							enableBasicAutocompletion={true}
							enableLiveAutocompletion={true}
							enableSnippets={true}
							value={this.state.docContent}
							onChange={this.handleDoc}
							width='100%'
						/>
			  </Grid>
			  <Grid item xs={12} md={6}>
					<AceEditor
						mode={editorOutFormat}
						theme="xcode"
						name="DOC_OUTPUT"
						editorProps={{ $blockScrolling: true }}
						readOnly={true}
						value={this.state.docOutput}
						width='100%'
					/>
			  </Grid>
			</Grid>

		</Fragment>
	}

}

export default DocConversion;