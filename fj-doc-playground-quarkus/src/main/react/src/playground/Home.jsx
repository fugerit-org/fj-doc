import React, { Component, Fragment } from 'react';

import { Link } from "react-router-dom";

import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Grid from '@mui/material/Grid';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import Avatar from '@mui/material/Avatar';
import ModeEditOutlineIcon from '@mui/icons-material/ModeEditOutline';
import ArticleIcon from '@mui/icons-material/Article';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import DoneIcon from '@mui/icons-material/Done';
import {CopyToClipboard} from 'react-copy-to-clipboard';


const homepage = '/fj-doc-playground/home';

const cmdDockerRun = 'docker run -it -p 8080:8080 --name fj-doc-playground-quarkus fugeritorg/fj-doc-playground-quarkus:latest';

class Home extends Component {

	render() {
		return <Fragment>
			<h1>This is a playground for <a href="https://github.com/fugerit-org/fj-doc">Venus (fj-doc)</a> project.</h1>
			<List sx={{ width: '100%', maxWidth: 800 }}>
				<ListItem key='1'>
					<ListItemAvatar>
						<Avatar>
							<ModeEditOutlineIcon />
						</Avatar>
					</ListItemAvatar>
					<Link to={homepage + "/doc_fun/doc_xml_editor"}>
						<ListItemText primary="Doc editor (FTL, XML, JSON or YAML) and generator (HTML, PDF, XLSX or Markdwon)"
							secondary='Demonstrates the Venus (fj-doc) core capabilities for generating documents in various formats' />
					</Link>
				</ListItem>
				<ListItem key='2'>
					<ListItemAvatar>
						<Avatar>
							<ArticleIcon />
						</Avatar>
					</ListItemAvatar>
					<Link to={homepage + "/doc_fun/doc_conversion"}>
						<ListItemText primary="Source document conversion from/to XML/JSON/YAML"
							secondary='Allow conversion of the Venus Doc source documents in supported formats.' />
					</Link>
				</ListItem>
				<ListItem key='3'>
					<ListItemAvatar>
						<Avatar>
							<DoneIcon />
						</Avatar>
					</ListItemAvatar>
					<Link to={homepage + "/doc_fun/doc_type_validator"}>
						<ListItemText primary="Doc validation testing playground (fj-doc-val-*)"
							secondary='Allow upload and validation in supported formats.' />
					</Link>
				</ListItem>
			</List>
 			<Grid id="firstRow" container spacing={1} style={{ paddingLeft: 15, paddingTop: 15 }}>
 				<Grid item sx={8}>
 					<p>To run locally, <a href="https://github.com/fugerit-org/fj-doc/tree/main/fj-doc-playground-quarkus">go to the project page</a> or use a public image : </p>
 				</Grid>
 			</Grid>
 			<Grid id="secondRow" container spacing={1} style={{ paddingLeft: 15 }}>
 	 			<Grid item sx={8}>
 					<TextField sx={{ width: '800px' }} id="outlined-basic" label="docker run" variant="outlined" value={cmdDockerRun}/>
 					<CopyToClipboard text={cmdDockerRun}><Button><ContentCopyIcon /></Button></CopyToClipboard>
 				</Grid>
    		</Grid>
   			<Grid id="secondRow" container spacing={1} style={{ paddingLeft: 15 }}>
 	 			<Grid item sx={8}>
 					And open <a href="http://localhost:8080/fj-doc-playground/home/">http://localhost:8080/fj-doc-playground/home/</a> 
 				</Grid>
    		</Grid>
		</Fragment>
	}

}

export default Home;