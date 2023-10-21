import React, { Component, Fragment } from 'react';
import { Form, Button, Row, Col } from 'react-bootstrap';
import { FormControl, Select, MenuItem } from "@mui/material";
import DocCatalog from './DocCatalog';
import appService from '../common/app-service';

import AceEditor from "react-ace";

import MarkdownEditor from '@uiw/react-markdown-editor';

import "ace-builds/src-noconflict/mode-xml";
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
			generationTime: null
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
						generationTime:  response.result.generationTime
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
						docFormat: this.state.outputFormat
					})
				} 
			})
		}
	};		
	
	handleInputFormat = (e) => {
		e.preventDefault();
		this.setState({
			renderCatalog : true,
			inputFormat: e.target.value
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
				editorInFormat = 'text'
				freemarkerJsonData = <Row>
					<Col>				
						<p style={{paddingTop: "20px"}}>Json properties will be available in FTL. each property as a freemarker variable.'.</p>
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
							width='100%'
						/>
					</Col>
					<Col>
						
					</Col>
				</Row>	
			} else {
				editorInFormat = this.state.inputFormat.toLowerCase()
			}
		}

		let outputData = <Fragment>Here will be the output</Fragment>
		if ( this.state.docOutput != null && this.state.docFormat != null ) {
			if ( this.state.docFormat === 'HTML' ) {
				let decodedStringAtoB = atob(this.state.docOutput);
				outputData = <div contentEditable='true' dangerouslySetInnerHTML={{ __html: decodedStringAtoB }}></div>
			}  else if ( this.state.docFormat === 'PDF' || this.state.docFormat === 'PDFA' ) {
				let srcData = 'data:application/pdf;base64,'+ this.state.docOutput;
				outputData = <embed width="100%" height="600" src={srcData}/>
			}  else if ( this.state.docFormat === 'XLSX' ) {
				let srcData = 'data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,'+ this.state.docOutput;
				outputData = <a href={srcData} download='generated_document.xlsx'>generated_document.xlsx</a>			
			} else if ( this.state.docFormat === 'MD' ) {
				let decodedStringAtoB = this.myAtob(this.state.docOutput);
				outputData = <MarkdownEditor  value={decodedStringAtoB} />
			}
			outputData = <Fragment>{outputData}<p>Generation time : {this.state.generationTime}</p></Fragment>
		}
		
		return <Fragment>
		    
			<Form>
				<Row>
					<Col>
						<DocCatalog key={this.state.inputFormat}
							currentType={this.state.inputFormat}
							handleEditorContent={this.handleEditorContent} 
							handleJsonDataContent={this.handleJsonDataContent} 
						/>
					</Col>
				</Row>	
				<Row>
					<Col>
						<Form.Label>Source type</Form.Label>
					</Col>
					<Col>
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
					</Col>
				</Row>
				<Row>				
					<Col>				
						<Form.Label>Output format</Form.Label>
					</Col>
					<Col>	
						<FormControl fullWidth>
						  <Select
						    id="output-type-select"
						    onChange={this.handleFormat}
							value={this.state.outputFormat}
						  >
							<MenuItem value='HTML'>HTML</MenuItem>
						    <MenuItem value='PDF'>PDF</MenuItem>
						    <MenuItem value='PDFA'>PDF/A</MenuItem>
						    <MenuItem value='XLSX'>XLSX</MenuItem>
						    <MenuItem value='MD'>Markdown (MD)</MenuItem>
						  </Select>
						</FormControl>								
					</Col>
				</Row>	

				<Row>
					<Col>						
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
					</Col>
					<Col>
						<div>{outputData}</div>
					</Col>
				</Row>				
				<Row>
					<Col>						
						<Row>
							<Col>							
								<Button variant="primary" onClick={this.handleGenerate}>Generate document</Button> 
							</Col>
							<Col>							 
								<Button variant="primary" onClick={this.handleValidate}>Validate document</Button>
							</Col>							
						</Row>							
					</Col>
					<Col>
						
					</Col>
				</Row>		
				{freemarkerJsonData}					
			</Form>

		</Fragment>
	}

}

export default DocXmlEditor;