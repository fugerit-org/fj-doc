import React, { useState, useEffect, Fragment } from 'react';
import { Input, Grid, FormLabel, Button, List, ListItem, ListItemText } from "@mui/material";
import appService from '../common/app-service';

const DocValTestForm = ({setHelpContent}) => {

	const [fileToValidate, setFileToValidate] = useState(null)
	const [supportedExtensions, setSupportedExtensions] = useState(null)
	const [validationResult, swtValidationResult] = useState(null)

	useEffect(() => {
		setHelpContent('doc-type-validator');
		appService.doAjaxMultipart('GET', '/val/supported_extensions', null).then(response => {
			if (response.success) {
				setSupportedExtensions( response.result )
			} else {
				setSupportedExtensions( null )
			}
		})
	}, []);

	const sendForValidation = (e) => {
		e.preventDefault();
		if (fileToValidate == null) {
			alert('File non provided');
		} else {
			let formData = new FormData();
			formData.append('file', fileToValidate)
			appService.doAjaxMultipart('POST', '/val/check', formData).then(response => {
				if (response.success) {
					swtValidationResult( response.result?.message )
				} else {
					swtValidationResult( response.status )
				}
			})
		}
	};

	const prepareSend = (e) => {
		e.preventDefault();
		setFileToValidate( e.target.files[0] )
	};

	const handleContent = () => {
		let message = <Fragment></Fragment>
		if (validationResult != null) {
			message = <b>Validation result : {validationResult} </b>
		}
		let supportedExtensionsShow = <Fragment></Fragment>;
		if (supportedExtensions != null) {
			supportedExtensionsShow = <List variant="secondary">Supported extensions : {supportedExtensions.map(d => <ListItem key={d}><ListItemText>{d}</ListItemText></ListItem>)}</List>
		}

		return (
			<Fragment>
				<Grid container spacing={1}>
				  <Grid item xs={8}>
				  	<Grid container spacing={1}>
				  		<Grid item xs={12}>
				  			{message}
				  		</Grid>
				  		<Grid item xs={12}>
				  			<FormLabel>Doc Type File validation example:</FormLabel>
				  		</Grid>
				  		<Grid item xs={12}>
				  			<Input type='file' onChange={prepareSend}/>
				  		</Grid>
				  		<Grid item xs={12}>
				  			<Button variant="contained" component="label" onClick={sendForValidation}>Send</Button>
				  		</Grid>
				  	</Grid>
				  </Grid>
				  <Grid item xs={6} md={3}>
				    {supportedExtensionsShow}
				  </Grid>
				 </Grid>
			</Fragment>
		);
	}

	return handleContent()

}

export default DocValTestForm;