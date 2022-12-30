import React, { Component, Fragment } from 'react';
import { Form, Button, ListGroup } from 'react-bootstrap';
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


class DocXmlEditor extends Component {

	constructor(props) {
		super(props);
		this.handleGenerate = this.handleGenerate.bind(this);
		this.handleFormat = this.handleFormat.bind(this);
		this.handleDoc = this.handleDoc.bind(this);
		this.state = {
			outputFormat: null,
			docContent: placeholterXml,
			validationResult:null
		}
	}

	componentDidMount() {
	}

	handleGenerate = (e) => {
		e.preventDefault();
			if ( this.state.outputFormat == 'HTML' ) {
				appService.doAjaxJson('POST', '/generate/'+this.state.outputFormat, this.state).then(response => {
					if (response.success) {
						var myWindow = window.open("", "response", "resizable=yes");
	   				    myWindow.document.write(response.result);
					}
				})
			} else {
				appService.doAjaxJsonToBlob('POST', '/generate/'+this.state.outputFormat, this.state).then(response => {
					if (response.success) {
						let contentType = "application/pdf";
						if ( this.state.outputFormat = 'XLSX' ) {
							contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
						}
	   				    const file = new Blob([response.result], {
	     					type: contentType,
	   					 });
	    				const fileURL = URL.createObjectURL(file);
	    				window.open(fileURL);
					}
				})
			}
	};

	handleFormat = (e) => {
		e.preventDefault();
		this.state = {
			outputFormat: e.target.value,
			docContent: this.state.docContent
		}
	};

	handleDoc = (e) => {
		e.preventDefault();
		this.state = {
			outputFormat: this.state.outputFormat,
			docContent: e.target.value
		}
	};

	render() {
		return <Fragment>

			<Form>
				<Form.Select aria-label="Default select example" onChange={this.handleFormat}>
					<option>Select the output format</option>
					<option value="PDF">PDF</option>
					<option value="XLSX">XLSX</option>
					<option value="HTML">HTML</option>
				</Form.Select>
				<Form.Group className="mb-3" controlId="xmlarea">
					<Form.Label>Example textarea</Form.Label>
					<Form.Control type="text" as="textarea" rows={20} onChange={this.handleDoc} defaultValue={this.state.docContent}  />
				</Form.Group>
				<Button variant="primary" onClick={this.handleGenerate}>Generate document</Button>
			</Form>

		</Fragment>
	}

}

export default DocXmlEditor;