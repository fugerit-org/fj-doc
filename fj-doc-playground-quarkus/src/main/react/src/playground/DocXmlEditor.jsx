import React, { Component, Fragment } from 'react';
import { FormControl, Select, MenuItem, TextField, Grid, FormLabel, Button } from "@mui/material";
import DocCatalog from './DocCatalog';
import appService from '../common/app-service';

import AceEditor from "react-ace";

import MarkdownEditor from '@uiw/react-markdown-editor';

import "ace-builds/src-noconflict/mode-xml";
import "ace-builds/src-noconflict/mode-ftl";
import "ace-builds/src-noconflict/mode-markdown";
import "ace-builds/src-noconflict/theme-xcode";
import "ace-builds/src-noconflict/ext-language_tools";
import "ace-builds/webpack-resolver";

class DocXmlEditor extends Component {

	constructor(props) {
		super(props);
		this.handleGenerate = this.handleGenerate.bind(this);
		this.handleValidate = this.handleValidate.bind(this);
		this.handleFormat = this.handleFormat.bind(this);
		this.handleDoc = this.handleDoc.bind(this);
		this.handleFreemarkerData = this.handleFreemarkerData.bind(this);
		this.handleEditorContent = this.handleEditorContent.bind(this);
		this.handleJsonDataContent = this.handleJsonDataContent.bind(this);
		this.handleInputFormat = this.handleInputFormat.bind(this);
		this.state = {
			renderCatalog: true,
			inputFormat: 'FTLX',			
			outputFormat: 'HTML',
			docContent: '',
			freemarkerJsonData: '{"docTitle":"My FreeMarker Template Sample Doc Title"}',
			docOutput: null,
			docFormat: null,
			generationTime: null,
			outputMessage: null,
			validationEnabled: false
		}
	}

	handleGenerate = (e) => {
		e.preventDefault();
		if (this.state.outputFormat == null) {
			this.props.handleOpenDialog("Select an output format");
		} else {
			let reactState = this
			let payload = {}
			payload.inputFormat = this.state.inputFormat;
			payload.outputFormat = this.state.outputFormat;
			payload.docContent = this.state.docContent;
			payload.freemarkerJsonData = this.state.freemarkerJsonData;
			appService.doAjaxJson('POST', '/generate/document', payload).then(response => {
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
	
	handleValidate = (e) => {
		e.preventDefault();
		if (this.state.outputFormat == null) {
			this.props.handleOpenDialog("Select an output format");
		} else {
			let reactState = this
			let payload = {}
			payload.inputFormat = this.state.inputFormat;
			payload.outputFormat = this.state.outputFormat;
			payload.docContent = this.state.docContent;
			appService.doAjaxJson('POST', '/generate/validate', payload).then(response => {
				if (response.success) {
					reactState.setState({
						docOutput: response.result.docOutputBase64,
						docFormat: this.state.outputFormat,
						outputMessage: response.result.message
					})
				} 
			})
		}
	};		
	
	handleInputFormat = (e) => {
		e.preventDefault();
		this.setState({
			renderCatalog : true,
			inputFormat: e.target.value,
			validationEnabled: (e.target.value !== 'FTLX')
		});
	};

	handleFormat = (e) => {
		e.preventDefault();
		this.setState({
			outputFormat: e.target.value,
			docContent: this.state.docContent
		});
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
		if ( this.state.inputFormat != null ) {
			if ( this.state.inputFormat === 'FTLX' ) {
				editorInFormat = 'ftl'
				freemarkerJsonData = <Fragment>		
						<p style={{paddingTop: "20px"}}>Json properties will be available in FTL. each property as a freemarker variable.</p>
						<p>For instance a json like {"{ \"docTitle\": \"My FreeMarker Template Sample Doc Title\" }"}, can be accessed like {"$"}{"{docTitle}"} in template</p>	
						<AceEditor
							mode="json"
							theme="xcode"
							name="DOC_JSON_EDITOR"
							editorProps={{ $blockScrolling: true }}
							enableBasicAutocompletion={true}
							enableLiveAutocompletion={true}
							enableSnippets={true}
							value={this.state.freemarkerJsonData}
							onChange={this.handleFreemarkerData}
							height='200px'
							width='100%'
						/>
					</Fragment>
			} else {
				editorInFormat = this.state.inputFormat.toLowerCase()
			}
		}

		let outputData = <Fragment>Here will be the output</Fragment>
		if ( this.state.docOutput != null && this.state.docFormat != null ) {
			if ( this.state.docFormat === 'HTML' ) {
				let decodedStringAtoB = atob(this.state.docOutput);
				outputData = <div contentEditable='true' dangerouslySetInnerHTML={{ __html: decodedStringAtoB }}></div>
			}  else if ( this.state.docFormat === 'PDF' || this.state.docFormat === 'PDFA' || this.state.docFormat === 'OPENPDF' ) {
				let srcData = 'data:application/pdf;base64,'+ this.state.docOutput;
				outputData = <embed width="100%" height="600" src={srcData}/>
			}  else if ( this.state.docFormat === 'XLSX' ) {
				let srcData = 'data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,'+ this.state.docOutput;
				outputData = <a href={srcData} download='generated_document.xlsx'>generated_document.xlsx</a>		
			}  else if ( this.state.docFormat === 'OPENRTF' ) {
				let srcData = 'data:application/rtf;base64,'+ this.state.docOutput;
				outputData = <a href={srcData} download='generated_document.rtf'>generated_document.rtf</a>		
			} else if ( this.state.docFormat === 'MD' ) {
				let decodedStringAtoB = this.myAtob(this.state.docOutput);
				outputData = <MarkdownEditor key={new Date().getTime()} value={decodedStringAtoB} />
			} else if ( this.state.docFormat === 'XML' || this.state.docFormat === 'FO' ) {
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
			} else if ( this.state.docFormat === 'CSV' ) {
				let decodedStringAtoB = this.myAtob(this.state.docOutput);
				outputData = <AceEditor
							mode='text'
							theme="xcode"
							name="DOC_CSV_OUTPUT"
							editorProps={{ $blockScrolling: true }}
							enableBasicAutocompletion={true}
							enableLiveAutocompletion={true}
							enableSnippets={true}
							value={decodedStringAtoB}
							width='100%'
						/>
			}
			outputData = <Fragment>{outputData}<p>Generation time : {this.state.generationTime}</p></Fragment>
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
					<DocCatalog key={this.state.inputFormat}
						currentType={this.state.inputFormat}
						handleEditorContent={this.handleEditorContent} 
						handleJsonDataContent={this.handleJsonDataContent} 
					/>
			  </Grid>
			  <Grid item xs={6} md={3}>
			    <FormLabel>Source type</FormLabel>
			  </Grid>
			  <Grid item xs={6} md={3}>
					<FormControl fullWidth>
					  <Select
					    id="input-type-select"
					    onChange={this.handleInputFormat}
						value={this.state.inputFormat}
					  >
					    <MenuItem value='FTLX'>FTLX (FreeMarker Template XML)</MenuItem>
					    <MenuItem value='XML'>XML</MenuItem>
					    <MenuItem value='JSON'>JSON</MenuItem>
					    <MenuItem value='YAML'>YAML</MenuItem>
					  </Select>
					</FormControl>	
			  </Grid>
			  <Grid item xs={6} md={3}>
			    <FormLabel>Output format</FormLabel>
			  </Grid>
			  <Grid item xs={6} md={3}>
					<FormControl fullWidth>
					  <Select
					    id="output-type-select"
					    onChange={this.handleFormat}
						value={this.state.outputFormat}
					  >
						<MenuItem value='HTML'>HTML</MenuItem>
					    <MenuItem value='PDF'>PDF (fop)</MenuItem>
					    <MenuItem value='PDFA'>PDF/A (fop)</MenuItem>
					    <MenuItem value='XLSX'>XLSX</MenuItem>
					    <MenuItem value='MD'>Markdown (MD)</MenuItem>
					    <MenuItem value='XML'>Venus XML Doc</MenuItem>
					    <MenuItem value='FO'>XSL-FO</MenuItem>
					    <MenuItem value='CSV'>CSV</MenuItem>
					    <MenuItem value='OPENPDF'>PDF (openpdf)</MenuItem>
					    <MenuItem value='OPENRTF'>RTF (openrtf)</MenuItem>
					  </Select>
					</FormControl>	
			  </Grid>
			  <Grid item xs={6}>
			    <Button variant="contained" onClick={this.handleGenerate}>Generate document</Button> 
			  </Grid>
			  <Grid item xs={6}>
			    <Button variant="contained" disabled={!this.state.validationEnabled} onClick={this.handleValidate}>Validate (only XML, JSON and YAML sources)</Button>
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

export default DocXmlEditor;