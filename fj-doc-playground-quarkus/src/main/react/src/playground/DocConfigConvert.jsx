import React, { useState, useEffect, Fragment } from 'react';
import { TextField, Grid, Button } from "@mui/material";
import appService from '../common/app-service';

import AceEditor from "react-ace";

import "ace-builds/src-noconflict/mode-xml";
import "ace-builds/src-noconflict/mode-kotlin";
import "ace-builds/src-noconflict/mode-ftl";
import "ace-builds/src-noconflict/mode-markdown";
import "ace-builds/src-noconflict/theme-xcode";
import "ace-builds/src-noconflict/ext-language_tools";

const DocConfigConvert = ({handleOpenDialog}) => {

	const [docContent, setDocContent] = useState('')
	const [docOutput, setDocOutput] = useState('')
	const [freemarkerJsonData, setFreemarkerJsonData] = useState('')
	const [outputMessage, setOutputMessage] = useState(null)

	useEffect(() => {
		loadStub()
	}, []);

	const loadStub = () => {
		let catalogUrl = '/catalog/res/';
		appService.doAjaxMultipart('GET', catalogUrl+'convert-config-stub.properties', null).then(response => {
			if (response.success) {
				setFreemarkerJsonData( response.result )
			}
		})
		appService.doAjaxMultipart('GET', catalogUrl+'doc-process-stub.xml', null).then(response => {
			if (response.success) {
				setDocContent( response.result )
			}
		})
	}

	const handleGenerate = (e) => {
		e.preventDefault();
		if (docContent == null) {
			handleOpenDialog("No input to convert");
		} else {
			let payload = {}
			payload.docContent = docContent;
			payload.freemarkerJsonData = freemarkerJsonData;
			appService.doAjaxJson('POST', '/config/convert', payload).then(response => {
				if (response.success) {
					setDocOutput( response.result.docOutputBase64 )
					setGenerationTime( response.result.generationTime )
					setOutputMessage( response.result.message )
				}
			})
		}
	};

	const handleDoc = ( newValue ) => {
		setDocContent( newValue )
	}

	const handleFreemarkerData = ( newValue ) => {
		setFreemarkerJsonData( newValue )
	}

	const handleEditorContent = ( content ) => {
		setDocContent( content )
	}

	const handleJsonDataContent = ( content ) => {
		setFreemarkerJsonData( content )
	}

	 const myAtob = (code) => {
	    try {
	      return decodeURIComponent(atob(code)
	      .split('')
	      .map(function(c) {
	        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
	      })
	      .join(''))
	    }
	    catch( e ) {
	      return atob(code)
	    }
	  }

	const handleContent = () => {
		let freemarkerJsonDataSection = '';
		let editorInFormat = 'xml';
		freemarkerJsonDataSection = <Fragment>
			<p style={{paddingTop: "20px"}}>Configuration conversion parameters, see <a href={'https://github.com/fugerit-org/fj-doc/blob/main/fj-doc-tool/README.md'}>fj-doc-tool</a></p>
			<AceEditor
				mode="properties"
				theme="xcode"
				name="DOC_JSON_EDITOR"
				editorProps={{$blockScrolling: true}}
				enableBasicAutocompletion={true}
				enableLiveAutocompletion={true}
				enableSnippets={true}
				value={freemarkerJsonData}
				onChange={handleFreemarkerData}
				height='200px'
				width='100%'
			/>
		</Fragment>

		let outputData = <Fragment>Here will be the output</Fragment>
		if ( docOutput ) {
			let decodedStringAtoB = myAtob(docOutput);
			outputData = <AceEditor
				mode='xml'
				theme="xcode"
				name="DOC_XML_OUTPUT"
				editorProps={{ $blockScrolling: true }}
				enableBasicAutocompletion={true}
				enableLiveAutocompletion={true}
				enableSnippets={true}
				value={decodedStringAtoB}
				width='100%'
			/>
		} else if ( outputMessage != null ) {
			outputData =  <Grid id="firstRow" container spacing={1} style={{ paddingLeft: 5, paddingTop: 15 }}>
 				<Grid item sx={{ width: '100%' }}>
 					<TextField sx={{ width: '100%' }} 
 						id="outputMessage" 
 						rows={20}
 						multiline={true}
 						label="output message" 
 						variant="outlined" value={outputMessage}/>
 				</Grid>
 			</Grid>
		}
		
		return <Fragment>
		    
			<Grid container spacing={1}>
			  <Grid item xs={12}>
			    <Button variant="contained" onClick={handleGenerate}>Convert configuration</Button>
			  </Grid>
			  <Grid item xs={12} xl={6}>
						<AceEditor
							mode={editorInFormat}
							theme="xcode"
							name="DOC_ACE_EDITOR"
							editorProps={{ $blockScrolling: true }}
							enableBasicAutocompletion={true}
							enableLiveAutocompletion={true}
							enableSnippets={true}
							value={docContent}
							onChange={handleDoc}
							width='100%'
						/>
					{freemarkerJsonDataSection}
			  </Grid>
			  <Grid item xs={12} xl={6}>
			    {outputData}
			  </Grid>
			</Grid>
			
		</Fragment>
	}

	return handleContent()

}

export default DocConfigConvert;