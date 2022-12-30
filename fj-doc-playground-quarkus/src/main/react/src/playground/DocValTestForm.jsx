import React, { Component } from 'react';
import { Form, Button } from 'react-bootstrap';
import appService from '../common/app-service';

class DocValTestForm extends Component {
	
	constructor(props) {
		super(props);
		this.sendForValidation = this.sendForValidation.bind(this);
		this.prepareSend = this.prepareSend.bind(this);
      	this.state = {
            fileToValidate: null,
            validationResult: null
        }		
	}	

    sendForValidation = (e) => {
        e.preventDefault();
        if ( this.state.fileToValidate == null ) {
			alert( 'File non provided' );	
		} else {
			let formData = new FormData();
			formData.append('file', this.state.fileToValidate);
			
			var reactState = this;
			appService.doAjaxMultipart('POST', '/val/check', formData).then(response => {
				console.log( response );
     		 if (response.success) {
       			 reactState.setState({
					validationResult: response.result.message
				})	
     		 } else {
				if ( response.result != null && response.result.message != null ) {
	       			 reactState.setState({
						validationResult: response.result.message
					})	
				} else {
	       			 reactState.setState({
						validationResult: response.status
					})	
				}
      		 }
    		})
		}
    };	
    
    prepareSend = (e) => {
        e.preventDefault();
        this.setState({
			fileToValidate:e.target.files[0]
		})
    };	    
	
    render() { 
		let message = '';
		if ( this.state.validationResult != null ) {
			message = 'Validation result : '+this.state.validationResult;
		}
		return (
			<div>
			  {message}
		      <Form.Group controlId="For" className="mb-3">
		        <Form.Label>Doc Type File validation example: </Form.Label>
		        <Form.Control type="file" onChange={this.prepareSend}/>  		
		      </Form.Group>
		      <Button variant="primary" onClick={this.sendForValidation}>Send</Button>
		    </div>
		  );  		
	}	
	  
}

export default DocValTestForm;