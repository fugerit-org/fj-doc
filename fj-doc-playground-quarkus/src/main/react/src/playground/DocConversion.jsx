import React, { Component, Fragment } from 'react';
import { Form, Button, Col, Row } from 'react-bootstrap';
import axios from 'axios';
import appService from '../common/app-service';

import XMLData from "./samples_doc_xml/default_doc.xml";

let placeholterXml = '';

axios.get(XMLData, {
	"Content-Type": "application/xml; charset=utf-8"
})
	.then((response) => {
		placeholterXml = response.data;
	});


class DocConversion extends Component {

	constructor(props) {
		super(props);
		this.handleGenerate = this.handleGenerate.bind(this);
		this.handleFormat = this.handleFormat.bind(this);
		this.handleDoc = this.handleDoc.bind(this);
		this.state = {
			inputFormat: 'XML',			
			outputFormat: null,
			docContent: placeholterXml,
			docOutput: ''
		}
	}

	componentDidMount() {
	}

	handleGenerate = (e) => {
		e.preventDefault();
		if (this.state.outputFormat == null) {
			this.props.handleOpenDialog("Select an output format");
		} else {
			var reactState = this;
				appService.doAjaxJson('POST', '/convert/doc', this.state).then(response => {
					if (response.success) {
						reactState.setState({
							docOutput: response.result.docOutput
						})
					} else {
						if ( response.result.message != null ) {
							this.props.handleOpenDialog( response.result.message + ' (' + response.status+")" );
						} else {
							this.props.handleOpenDialog( 'Generic error (' + response.status+")" );
						}
					}
				})
		}
	};

	handleInputFormat = (e) => {
		e.preventDefault();
		this.setState({
			inputFormat: e.target.value,
			outputFormat: this.state.outputFormat,			
			docContent: this.state.docContent,
			docOutput: this.state.docOutput
		});
	};

	handleFormat = (e) => {
		e.preventDefault();
		this.setState({
			inputFormat: this.state.inputFormat,			
			outputFormat: e.target.value,
			docContent: this.state.docContent,
			docOutput: this.state.docOutput
		});
	};

	handleDoc = (e) => {
		e.preventDefault();
		this.setState({
			outputFormat: this.state.outputFormat,
			docContent: e.target.value,
			docOutput: this.state.docOutput			
		});
	};

	render() {
		return <Fragment>

			<Form>
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
				</Row>
				<Row>
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
				<Form.Group className="mb-3" controlId="inputarea">
					<Form.Label>Input area</Form.Label>
					<Form.Control type="text" as="textarea" rows={20} onChange={this.handleDoc} defaultValue={this.state.docContent} />
				</Form.Group>
				<Button variant="primary" onClick={this.handleGenerate}>Convert</Button>
				<Form.Group className="mb-3" controlId="outputarea">
					<Form.Label>Output area</Form.Label>
					<Form.Control type="text" as="textarea" rows={20} readOnly value={this.state.docOutput} />
				</Form.Group>				
			</Form>

		</Fragment>
	}

}

export default DocConversion;