import React, {useState, Fragment, useEffect} from 'react';
import { FormControl, Select, MenuItem, Grid, FormLabel, Button } from "@mui/material";
import DocCatalog from './DocCatalog';
import appService from '../common/app-service';

import AceEditor from "react-ace";

import "ace-builds/src-noconflict/mode-xml";
import "ace-builds/src-noconflict/mode-json";
import "ace-builds/src-noconflict/mode-yaml";
import "ace-builds/src-noconflict/theme-xcode";
import "ace-builds/src-noconflict/ext-language_tools";

const DocConversion = ({handleOpenDialog, key, from, to, setHelpContent}) => {

	useEffect(() => {
		setHelpContent('doc-conversion');
	}, [])

	const [inputFormat, setInputFormat] = useState(from == null ? 'XML' : from)
	const [outputFormat, setOutputFormat] = useState(from == null ? 'JSON' : from)
	const [docContent, setDocContent] = useState('')
	const [docOutput, setDocOutput] = useState('')

	const handleGenerateOnlyFormat = (e) => {
		handleGenerate( e, false )
	};
	
	const handleGeneratePrettyPrint = (e) => {
		handleGenerate( e, true )
	};

	const handleGenerate = (e, prettyPrint) => {
		e.preventDefault();
		if (outputFormat == null) {
			handleOpenDialog("Select an output format");
		} else {
			let payload = {}
			payload.inputFormat = inputFormat
			payload.outputFormat = outputFormat
			payload.docContent = docContent
			payload.prettyPrint = prettyPrint
				appService.doAjaxJson('POST', '/convert/doc', payload).then(response => {
					if (response.success) {
						setDocOutput(response.result.docOutput)
					} else if ( response.result.message != null ) {
						handleOpenDialog( response.result.message + ' (' + response.status+")" );
					} else {
						handleOpenDialog( 'Generic error (' + response.status+")" );
					}
				})
		}
	};

	const handleInputFormat = (e) => {
		e.preventDefault()
		setInputFormat( e.target.value )
	}

	const handleFormat = (e) => {
		e.preventDefault()
		setOutputFormat( e.target.value )
	}

	const handleDoc = ( newValue ) => {
		setDocContent( newValue )
	};

	const handleEditorContent = ( content ) => {
		setDocContent( content )
	}

	const handleContent = () => {

		let editorInFormat = 'xml';
		if ( inputFormat != null ) {
			editorInFormat = inputFormat.toLowerCase()
		}

		let editorOutFormat = 'xml';
		if ( outputFormat != null ) {
			editorOutFormat = outputFormat.toLowerCase()
		}
	
		return <Fragment>

			<Grid container spacing={1}>
			  <Grid item xs={12}>
						<DocCatalog key={inputFormat}
							currentType={inputFormat}
							handleEditorContent={handleEditorContent}
						/>
			  </Grid>
			  <Grid item xs={6} md={3}>
			    <FormLabel>Convert from</FormLabel>
			  </Grid>
			  <Grid item xs={6} md={3}>
					<FormControl fullWidth>
					  <Select
					    id="input-type-select"
					    onChange={handleInputFormat}
						value={inputFormat}
					  >
					    <MenuItem value='XML'>XML</MenuItem>
					    <MenuItem value='JSON'>JSON</MenuItem>
					    <MenuItem value='YAML'>YAML</MenuItem>
					  </Select>
					</FormControl>	
			  </Grid>
			  <Grid item xs={6} md={3}>
			    <FormLabel>Convert to</FormLabel>
			  </Grid>
			  <Grid item xs={6} md={3}>
					<FormControl fullWidth>
					  <Select
					    id="output-type-select"
					    onChange={handleFormat}
						value={outputFormat}
						displayEmpty
					  >
					    <MenuItem value='JSON'>JSON</MenuItem>
					    <MenuItem value='YAML'>YAML</MenuItem>
					    <MenuItem value='XML'>XML</MenuItem>
					  </Select>
					</FormControl>	
			  </Grid>
			  <Grid item xs={6}>
				<Button variant="contained" onClick={handleGeneratePrettyPrint}>Convert and pretty print</Button>		
			  </Grid>
			  <Grid item xs={6}>
				<Button variant="contained" onClick={handleGenerateOnlyFormat}>Only convert</Button>		
			  </Grid>
			  <Grid item xs={12} md={6}>
						<AceEditor
							mode={editorInFormat}
							theme="xcode"
							name="DOC_INPUT"
							editorProps={{ $blockScrolling: true }}
							enableBasicAutocompletion={true}
							enableLiveAutocompletion={true}
							enableSnippets={true}
							value={docContent}
							onChange={handleDoc}
							width='100%'
						/>
			  </Grid>
			  <Grid item xs={12} md={6}>
					<AceEditor
						mode={editorOutFormat}
						theme="xcode"
						name="DOC_OUTPUT"
						editorProps={{ $blockScrolling: true }}
						readOnly={true}
						value={docOutput}
						width='100%'
					/>
			  </Grid>
			</Grid>

		</Fragment>
	}

	return handleContent();

}

export default DocConversion;