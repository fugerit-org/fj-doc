import React, { Component, Fragment } from 'react';

import {
	BrowserRouter as Router,
	Routes,
	Route,
	Link
} from "react-router-dom";

import Home from './playground/Home';
import Version from './playground/Version';
import DocXmlEditor from './playground/DocXmlEditor';
import DocConversion from './playground/DocConversion';
import DevValTestForm from './playground/DocValTestForm';
import { Dialog, DialogTitle, Button, Grid } from "@mui/material";

const homepage = '/fj-doc-playground/home';

class Playground extends Component {

	constructor(props) {
		super(props);
		this.handleOpenDialog = this.handleOpenDialog.bind(this);
		this.handleCloseDialog = this.handleCloseDialog.bind(this);
		this.state =  {  dialogMessage: null }
	}

	handleCloseDialog = () => {
		this.setState(
			{ 
				dialogMessage: null				 
			}
		);
	};

	handleOpenDialog = ( message ) => {
		this.setState(
			{ 
				dialogMessage: message				 
			}
		);
	};

	render() {
		
		let dialog = <Fragment></Fragment>;
		if ( this.state?.dialogMessage != null) {
			dialog = <Dialog open={true}>
        		<DialogTitle>{this.state.dialogMessage}</DialogTitle>
				<Button  color="primary" onClick={this.handleCloseDialog} autoFocus>Close</Button>        		
        	</Dialog>
		}
		
		return (
			<Router>
				<div className="App">

					<Version/>

					<Grid container spacing={4} columns={{ xs: 16 }}>
					  <Grid item xs={4}><Link to={homepage}><Button color="primary">Home</Button></Link></Grid>
					  <Grid item xs={4}><Link to={homepage + "/doc_fun/doc_xml_editor"}><Button color="primary">Doc Xml Editor</Button></Link></Grid>
					  <Grid item xs={4}><Link to={homepage + "/doc_fun/doc_conversion"}><Button color="primary">Doc Conversion (XML/JSON/YAML)</Button></Link></Grid>
					  <Grid item xs={4}><Link to={homepage + "/doc_fun/doc_type_validator"}><Button color="primary">Doc Type Validator</Button></Link></Grid>
					</Grid>

					{dialog}
	
					<Routes>
						<Route path={homepage + "/doc_fun/doc_xml_editor"} element={<DocXmlEditor handleOpenDialog={this.handleOpenDialog} />} />
						<Route path={homepage + "/doc_fun/doc_conversion"} element={<DocConversion handleOpenDialog={this.handleOpenDialog} />} />
						<Route path={homepage + "/doc_fun/doc_type_validator"} element={<DevValTestForm handleOpenDialog={this.handleOpenDialog} />} />
						<Route path="*" element={<Home />} />
					</Routes>

				</div>
			</Router>
		);
	}

}

export default Playground;