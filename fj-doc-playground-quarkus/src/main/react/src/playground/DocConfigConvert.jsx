import React, { Component, Fragment } from 'react';
import { FormControl, Select, MenuItem, TextField, Grid, FormLabel, Button } from "@mui/material";
import DocCatalog from './DocCatalog';
import appService from '../common/app-service';
import DOMPurify from 'isomorphic-dompurify';

import AceEditor from "react-ace";

import MarkdownEditor from '@uiw/react-markdown-editor';

import "ace-builds/src-noconflict/mode-xml";
import "ace-builds/src-noconflict/mode-kotlin";
import "ace-builds/src-noconflict/mode-ftl";
import "ace-builds/src-noconflict/mode-markdown";
import "ace-builds/src-noconflict/theme-xcode";
import "ace-builds/src-noconflict/ext-language_tools";

class DocConfigConvert extends Component {

	constructor(props) {
		super(props);
		this.handleGenerate = this.handleGenerate.bind(this);
		this.handleDoc = this.handleDoc.bind(this);
		this.handleFreemarkerData = this.handleFreemarkerData.bind(this);
		this.handleEditorContent = this.handleEditorContent.bind(this);
		this.handleJsonDataContent = this.handleJsonDataContent.bind(this);
		this.state = {
			docContent: '',
			freemarkerJsonData: '',
			generationTime: null,
			outputMessage: null,
			validationEnabled: false
		}
	}

	componentDidMount() {
		this.loadStub();
	}

	loadStub() {
		let reactState = this;
		let catalogUrl = '/catalog/res/';
		appService.doAjaxMultipart('GET', catalogUrl+'convert-config-stub.properties', null).then(response => {
			if (response.success) {
				reactState.setState({
				}, () => reactState.setState( { freemarkerJsonData: response.result } ) )
			}
		})
		appService.doAjaxMultipart('GET', catalogUrl+'doc-process-stub.xml', null).then(response => {
			if (response.success) {
				reactState.setState({
				}, () => reactState.setState( { docContent: response.result } ) )
			}
		})
	}

	handleGenerate = (e) => {
		e.preventDefault();
		if (this.state.docContent == null) {
			this.props.handleOpenDialog("No input to convert");
		} else {
			let reactState = this
			let payload = {}
			payload.docContent = this.state.docContent;
			payload.freemarkerJsonData = this.state.freemarkerJsonData;
			appService.doAjaxJson('POST', '/config/convert', payload).then(response => {
				if (response.success) {
					reactState.setState({
						docOutput: response.result.docOutputBase64,
						docFormat: this.state.outputFormat,
						generationTime:  response.result.generationTime,
						outputMessage: response.result.message
					})
				}
			})
		}
	};

	handleDoc( newValue ) {
		this.setState({
			outputFormat: this.state.outputFormat,
			docContent: newValue
		});
	};

	handleFreemarkerData( newValue ) {
		this.setState({
			outputFormat: this.state.outputFormat,
			freemarkerJsonData: newValue
		});
	};

	handleEditorContent = ( content ) => {
		this.setState(
			{ 
				docContent: content			 
			}
		);
	};

	handleJsonDataContent = ( content ) => {
		this.setState(
			{ 
				freemarkerJsonData: content			 
			}
		);
	};

	 myAtob = (code) => {
	    try {
	      return decodeURIComponent(atob(code)
	      .split('')
	      .map(function(c) {
	        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
	      })
	      .join(''))
	    }
	    catch( e ) {
	      return atob(code)
	    }
	  }

	render() {
		let freemarkerJsonData = '';
		let editorInFormat = 'xml';
		freemarkerJsonData = <Fragment>
			<p style={{paddingTop: "20px"}}>Configuration conversion parameters, see <a href={'https://github.com/fugerit-org/fj-doc/blob/main/fj-doc-tool/README.md'}>fj-doc-tool</a></p>
			<AceEditor
				mode="properties"
				theme="xcode"
				name="DOC_JSON_EDITOR"
				editorProps={{$blockScrolling: true}}
				enableBasicAutocompletion={true}
				enableLiveAutocompletion={true}
				enableSnippets={true}
				value={this.state.freemarkerJsonData}
				onChange={this.handleFreemarkerData}
				height='200px'
				width='100%'
			/>
		</Fragment>

		let outputData = <Fragment>Here will be the output</Fragment>
		if ( this.state.docOutput ) {
			let decodedStringAtoB = this.myAtob(this.state.docOutput);
			outputData = <AceEditor
				mode='xml'
				theme="xcode"
				name="DOC_XML_OUTPUT"
				editorProps={{ $blockScrolling: true }}
				enableBasicAutocompletion={true}
				enableLiveAutocompletion={true}
				enableSnippets={true}
				value={decodedStringAtoB}
				width='100%'
			/>
		} else if ( this.state.outputMessage != null ) {
			outputData =  <Grid id="firstRow" container spacing={1} style={{ paddingLeft: 5, paddingTop: 15 }}>
 				<Grid item sx={{ width: '100%' }}>
 					<TextField sx={{ width: '100%' }} 
 						id="outputMessage" 
 						rows={20}
 						multiline={true}
 						label="output message" 
 						variant="outlined" value={this.state.outputMessage}/>
 				</Grid>
 			</Grid>
		}
		
		return <Fragment>
		    
			<Grid container spacing={1}>
			  <Grid item xs={12}>
			    <Button variant="contained" onClick={this.handleGenerate}>Convert configuration</Button>
			  </Grid>
			  <Grid item xs={12} xl={6}>
						<AceEditor
							mode={editorInFormat}
							theme="xcode"
							name="DOC_ACE_EDITOR"
							editorProps={{ $blockScrolling: true }}
							enableBasicAutocompletion={true}
							enableLiveAutocompletion={true}
							enableSnippets={true}
							value={this.state.docContent}
							onChange={this.handleDoc}
							width='100%'
						/>
					{freemarkerJsonData}
			  </Grid>
			  <Grid item xs={12} xl={6}>
			    {outputData}
			  </Grid>
			</Grid>
			
		</Fragment>
	}

}

export default DocConfigConvert;