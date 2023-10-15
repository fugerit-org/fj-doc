import React, { Component, Fragment } from 'react';

import Link from '@mui/material/Link';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import Avatar from '@mui/material/Avatar';
import ModeEditOutlineIcon from '@mui/icons-material/ModeEditOutline';
import ArticleIcon from '@mui/icons-material/Article';
import DoneIcon from '@mui/icons-material/Done';

const homepage = '/fj-doc-playground/home';

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
					<Link href={homepage + "/doc_fun/doc_xml_editor"}>
						<ListItemText primary="Doc editor (FTL, XML, JSON or YAML) and generator (HTML, PDF or XLSX)"
							secondary='Demonstrates the Venus (fj-doc) core capabilities for generating documents in various formats' />
					</Link>
				</ListItem>
				<ListItem key='2'>
					<ListItemAvatar>
						<Avatar>
							<ArticleIcon />
						</Avatar>
					</ListItemAvatar>
					<Link href={homepage + "/doc_fun/doc_conversion"}>
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
					<Link href={homepage + "/doc_fun/doc_type_validator"}>
						<ListItemText primary="Doc validation testing playground (fj-doc-val-*)"
							secondary='Allow upload and validation in supported formats.' />
					</Link>
				</ListItem>
			</List>
		</Fragment>
	}

}

export default Home;