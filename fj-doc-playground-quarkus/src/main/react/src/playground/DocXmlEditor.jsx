import React, { Component, Fragment } from 'react';
import { Form, Button, Row, Col } from 'react-bootstrap';
import DocCatalog from './DocCatalog';
import appService from '../common/app-service';

class DocXmlEditor extends Component {

	constructor(props) {
		super(props);
		this.handleGenerate = this.handleGenerate.bind(this);
		this.handleValidate = this.handleValidate.bind(this);
		this.handleFormat = this.handleFormat.bind(this);
		this.handleDoc = this.handleDoc.bind(this);
		this.handleEditorContent = this.handleEditorContent.bind(this);
		this.state = {
			inputFormat: 'XML',			
			outputFormat: null,
			docContent: '',
			docOutput: null,
			docFormat: null
		}
	}

	componentDidMount() {
	}

	handleGenerate = (e) => {
		e.preventDefault();
		if (this.state.outputFormat == null) {
			this.props.handleOpenDialog("Select an output format");
		} else {
			var reactState = this
			var payload = {}
			payload.inputFormat = this.state.inputFormat;
			payload.outputFormat = this.state.outputFormat;
			payload.docContent = this.state.docContent;
			appService.doAjaxJson('POST', '/generate/document', payload).then(response => {
				if (response.success) {
					reactState.setState({
						docOutput: response.result.docOutputBase64,
						docFormat: this.state.outputFormat
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
			var reactState = this
			var payload = {}
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
			inputFormat: e.target.value,
		});
	};

	handleFormat = (e) => {
		e.preventDefault();
		this.setState({
			outputFormat: e.target.value,
			docContent: this.state.docContent
		});
	};

	handleDoc = (e) => {
		e.preventDefault();
		this.setState({
			outputFormat: this.state.outputFormat,
			docContent: e.target.value
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
		
		let outputData = <Fragment>Here will be the output</Fragment>
		if ( this.state.docOutput != null && this.state.docFormat != null ) {
			if ( this.state.docFormat === 'HTML' ) {
				var decodedStringAtoB = atob(this.state.docOutput);
				outputData = <div contentEditable='true' dangerouslySetInnerHTML={{ __html: decodedStringAtoB }}></div>
			}  else if ( this.state.docFormat === 'PDF' ) {
				let srcData = 'data:application/pdf;base64,'+ this.state.docOutput;
				outputData = outputData = <embed width="100%" height="600" src={srcData}/>
			}  else if ( this.state.docFormat === 'XLSX' ) {
				let srcData = 'data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,'+ this.state.docOutput;
				outputData = <a href={srcData} download='generated_document.xlsx'>generated_document.xlsx</a>			
			}
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
						<Form.Label>Source type</Form.Label>
					</Col>
					<Col>
						<Form.Select aria-label="Select output format" onChange={this.handleInputFormat}>
							<option value="XML">XML</option>
							<option value="JSON">JSON</option>
							<option value="YAML">YAML</option>
						</Form.Select>
					</Col>
				</Row>
				<Row>				
					<Col>				
						<Form.Label>Output format</Form.Label>
					</Col>
					<Col>									
						<Form.Select aria-label="Select output format" onChange={this.handleFormat}>
							<option>Select the output format</option>
							<option value="PDF">PDF</option>
							<option value="XLSX">XLSX</option>
							<option value="HTML">HTML</option>
						</Form.Select>
					</Col>
				</Row>		
				<Row>
					<Col>						
				<Form.Group className="mb-3" controlId="xmlarea">
					<Form.Label>Source area</Form.Label>
					<Form.Control type="text" as="textarea" rows={20} onChange={this.handleDoc} defaultValue={this.state.docContent} />
				</Form.Group>
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
			</Form>

		</Fragment>
	}

}

export default DocXmlEditor;