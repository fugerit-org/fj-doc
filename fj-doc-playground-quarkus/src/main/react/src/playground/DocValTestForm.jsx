import React, { Component, Fragment } from 'react';
import { Form, Button, ListGroup } from 'react-bootstrap';
import appService from '../common/app-service';

class DocValTestForm extends Component {

	constructor(props) {
		super(props);
		this.sendForValidation = this.sendForValidation.bind(this);
		this.prepareSend = this.prepareSend.bind(this);
		this.state = {
			fileToValidate: null,
			supportedExtensions: null,
			validationResult: null
		}
	}

	componentDidMount() {
		let reactState = this;
		appService.doAjaxMultipart('GET', '/val/supported_extensions', null).then(response => {
			if (response.success) {
				reactState.setState({
					supportedExtensions: response.result
				})
			} else {
				reactState.setState({
					supportedExtensions: null
				})
			}
		})
	}

	sendForValidation = (e) => {
		e.preventDefault();
		if (this.state.fileToValidate == null) {
			alert('File non provided');
		} else {
			let formData = new FormData();
			formData.append('file', this.state.fileToValidate);

			let reactState = this;
			appService.doAjaxMultipart('POST', '/val/check', formData).then(response => {
				if (response.success) {
					reactState.setState({
						validationResult: response.result.message
					})
				} else if (response.result != null && response.result.message != null) {
					reactState.setState({
						validationResult: response.result.message
					})
				} else {
					reactState.setState({
						validationResult: response.status
					})
				}
			})
		}
	};

	prepareSend = (e) => {
		e.preventDefault();
		this.setState({
			fileToValidate: e.target.files[0]
		})
	};

	render() {
		let message = <Fragment></Fragment>
		if (this.state.validationResult != null) {
			message = <b>Validation result : {this.state.validationResult} </b>
		}
		let supportedExtensions = <Fragment></Fragment>;
		if (this.state.supportedExtensions != null) {
			supportedExtensions = <ListGroup variant="secondary">Supported extensions : {this.state.supportedExtensions.map(d => <ListGroup.Item key={d}>{d}</ListGroup.Item>)}</ListGroup>
		}

		return (
			<div>
				{message}
				<Form.Group controlId="For" className="mb-3">
					<Form.Label>Doc Type File validation example: </Form.Label>
					<Form.Control type="file" onChange={this.prepareSend} />
				</Form.Group>
				<Button variant="primary" onClick={this.sendForValidation}>Send</Button>
				{supportedExtensions}
			</div>
		);
	}

}

export default DocValTestForm;