import React, { Component, Fragment } from 'react';
import { Form, Button, Col, Row } from 'react-bootstrap';
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
			outputFormat: null,
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
		console.log( this.state );
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

			<Form>
				<Row>
					<Col>
						<DocCatalog handleEditorContent={this.handleEditorContent} />
					</Col>
				</Row>
				<Row>
					<Col>
						<Form.Label>Convert from</Form.Label>
					</Col>
					<Col>
						<Form.Select aria-label="Select output format" onChange={this.handleInputFormat}>
							<option value="XML">XML</option>
							<option value="JSON">JSON</option>
							<option value="YAML">YAML</option>
						</Form.Select>
					</Col>
				
					<Col>				
						<Form.Label>Convert to</Form.Label>
					</Col>
					<Col>									
						<Form.Select aria-label="Select output format" onChange={this.handleFormat}>
							<option>Select output format</option>
							<option value="JSON">JSON</option>
							<option value="YAML">YAML</option>
							<option value="XML">XML</option>
						</Form.Select>
					</Col>
				</Row>
				<Row>
					<Col>
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
				</Col>
				<Col>
					<AceEditor
						mode={editorOutFormat}
						theme="xcode"
						name="DOC_OUTPUT"
						editorProps={{ $blockScrolling: true }}
						readOnly={true}
						value={this.state.docOutput}
						width='100%'
					/>
					</Col>
				</Row>
				<Button variant="primary" onClick={this.handleGenerate}>Convert</Button>			
			</Form>
			

		</Fragment>
	}

}

export default DocConversion;