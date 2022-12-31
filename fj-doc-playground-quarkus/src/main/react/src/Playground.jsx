import React, { Component, Fragment } from 'react';

import { ListGroup } from 'react-bootstrap';

import {
	BrowserRouter as Router,
	Routes,
	Route,
	Link
} from "react-router-dom";

import Home from './playground/Home';
import DevValTestForm from './playground/DocValTestForm';
import DocXmlEditor from './playground/DocXmlEditor';
import { Dialog, DialogTitle, Button } from "@material-ui/core";

const homepage = '/fj-doc-playground/home';

class Playground extends Component {

	constructor(props) {
		super(props);
		this.handleOpenDialog = this.handleOpenDialog.bind(this);
		this.handleCloseDialog = this.handleCloseDialog.bind(this);
		this.setState(
			{ 
				dialogMessage: ''				 
			}
		);			
	}

	componentDidMount() {
	
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
		console.log( this.state );
	};

	render() {
		
		let dialog = <Fragment></Fragment>;
		if ( this.state != null && this.state.dialogMessage != null ) {
			dialog = <Dialog open={true}>
        		<DialogTitle>{this.state.dialogMessage}</DialogTitle>
				<Button  color="primary" onClick={this.handleCloseDialog} autoFocus>Close</Button>        		
        	</Dialog>
		}
		
		return (
			<Router>
				<div className="App">

					<ListGroup>
						<ListGroup.Item><Link to={homepage}>Home</Link></ListGroup.Item>
						<ListGroup.Item><Link to={homepage + "/doc_type_validator"}>Doc Type Validator</Link></ListGroup.Item>
						<ListGroup.Item><Link to={homepage + "/doc_xml_editor"}>Doc Xml Editor</Link></ListGroup.Item>
					</ListGroup>
					
					{dialog}
	
					<Routes>
						<Route path={homepage + "/doc_type_validator"} element={<DevValTestForm handleOpenDialog={this.handleOpenDialog} />} />
						<Route path={homepage + "/doc_xml_editor"} element={<DocXmlEditor handleOpenDialog={this.handleOpenDialog} />} />
						<Route path="*" element={<Home />} />
					</Routes>

				</div>
			</Router>
		);
	}

}

export default Playground;