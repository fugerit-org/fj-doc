import React, { Component } from 'react';
import { Form, Button } from 'react-bootstrap';
import axios from "axios";

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
			
			var reactState = this;
			
            formData.append('file', this.state.fileToValidate);
			axios.post("/playground/api/val/check", formData, {
  				headers: {
    				"Content-Type": "multipart/form-data",
  				},
			}).then((res) => {
		        console.log(res);
		        if ( res.data != null ) {
					reactState.setState({
						validationResult: res.data.message
					})	
				}
		      })
		      .catch((err) => {
				 console.log(err);
				if ( (err.response != null && err.response.data != null) ) {
					reactState.setState({
						validationResult: err.response.data.message
					})					
				} else {
					reactState.setState({
						validationResult: err.message
					})
				}
		      });
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