import React, { useState, Fragment } from 'react';

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
import DocConfigConvert from './playground/DocConfigConvert';
import DocProjectInit from './playground/DocProjectInit';
import { Dialog, DialogTitle, Button, Grid, MenuItem, Select } from "@mui/material";

const homepage = '/fj-doc-playground/home';

const Playground = () => {

	const [dialogMessage, setDialogMessage] = useState(null)

	const handleCloseDialog = () => {
		setDialogMessage( null )
	};

	const handleOpenDialog = ( message ) => {
		setDialogMessage( message )
	};

	const handleContent = () => {
		
		let dialog = <Fragment></Fragment>;
		if ( dialogMessage != null) {
			dialog = <Dialog open={true}>
        		<DialogTitle>{dialogMessage}</DialogTitle>
				<Button  color="primary" onClick={handleCloseDialog} autoFocus>Close</Button>        		
        	</Dialog>
		}
		
		return (
			<Router>
				<div className="App">

					<Version/>

					<Grid container spacing={4} columns={{ xs: 16 }}>
					  <Grid item xs={3}><Link to={homepage}><Button color="primary">Home</Button></Link></Grid>
					  <Grid item xs={4}><Link to={homepage + "/doc_fun/doc_xml_editor"}><Button color="primary">Doc editor and generator</Button></Link></Grid>
					  <Grid item xs={4}>
						  <Select id='conversionSelect' defaultValue='def'>
							  <MenuItem id='def' selected={true} value='def'><Link to={homepage + "/doc_fun/doc_conversion"}><Button color="primary">Doc Conversion (XML/JSON/YAML)</Button></Link></MenuItem>
							  <MenuItem id='xmlToXml' selected={true} value='xmlToXml'><Link to={homepage + "/doc_fun/doc_conversion_x2x"}><Button color="primary">Doc Conversion (XML to XML)</Button></Link></MenuItem>
							  <MenuItem id='jsonToJson' selected={true} value='jsonToJson'><Link to={homepage + "/doc_fun/doc_conversion_j2j"}><Button color="primary">Doc Conversion (JSON to JSON)</Button></Link></MenuItem>
							  <MenuItem id='yamlToYaml' selected={true} value='yamlToYaml'><Link to={homepage + "/doc_fun/doc_conversion_y2y"}><Button color="primary">Doc Conversion (YAML to YAML)</Button></Link></MenuItem>
						  </Select>
					  </Grid>
					  <Grid item xs={4}>
							<Select id='toolSelect' defaultValue='def'>
								<MenuItem id='docProjectInit' selected={true} value='def'><Grid item xs={4}><Link to={homepage + "/doc_fun/doc_project_init"}><Button color="primary">Doc project init</Button></Link></Grid></MenuItem>
								<MenuItem id='docValidator' selected={true} value='docValidator'><Grid item xs={4}><Link to={homepage + "/doc_fun/doc_type_validator"}><Button color="primary">Doc Type Validator</Button></Link></Grid></MenuItem>
								<MenuItem id='docConfigConvert' selected={true} value='xmlToXml'><Grid item xs={4}><Link to={homepage + "/doc_fun/doc_config_convert"}><Button color="primary">Doc config convert</Button></Link></Grid></MenuItem>
							</Select>
					  </Grid>


					</Grid>

					{dialog}
	
					<Routes>
						<Route path={homepage + "/doc_fun/doc_xml_editor"} element={<DocXmlEditor handleOpenDialog={handleOpenDialog} />} />
						<Route path={homepage + "/doc_fun/doc_conversion"} element={<DocConversion handleOpenDialog={handleOpenDialog} />} key='def' />
						<Route path={homepage + "/doc_fun/doc_conversion_x2x"} element={<DocConversion handleOpenDialog={handleOpenDialog} key='x2x' from='XML' to='XML' />} />
						<Route path={homepage + "/doc_fun/doc_conversion_j2j"} element={<DocConversion handleOpenDialog={handleOpenDialog} key='j2j' from='JSON' to='JSON' />} />
						<Route path={homepage + "/doc_fun/doc_conversion_y2y"} element={<DocConversion handleOpenDialog={handleOpenDialog} key='y2y' from='YAML' to='YAML' />} />
						<Route path={homepage + "/doc_fun/doc_type_validator"} element={<DevValTestForm handleOpenDialog={handleOpenDialog} />} />
						<Route path={homepage + "/doc_fun/doc_config_convert"} element={<DocConfigConvert handleOpenDialog={handleOpenDialog} />} />
						<Route path={homepage + "/doc_fun/doc_project_init"} element={<DocProjectInit handleOpenDialog={handleOpenDialog} />} />
						<Route path="*" element={<Home />} />
					</Routes>

				</div>
			</Router>
		);
	}

	return handleContent()

}

export default Playground;