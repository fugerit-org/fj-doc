import React, { useState, useEffect, Fragment } from 'react';
import { FormControl, Select, MenuItem, TextField, Grid, FormLabel, Button } from "@mui/material";
import DocCatalog from './DocCatalog';
import appService from '../common/app-service';
import DOMPurify from 'isomorphic-dompurify';

import AceEditor from "react-ace";

import MarkdownEditor from '@uiw/react-markdown-editor';

import "ace-builds/src-noconflict/mode-xml";
import "ace-builds/src-noconflict/mode-kotlin";
import "ace-builds/src-noconflict/mode-ftl";
import "ace-builds/src-noconflict/mode-markdown";
import "ace-builds/src-noconflict/theme-xcode";
import "ace-builds/src-noconflict/ext-language_tools";

const DocXmlEditor = ({handleOpenDialog, setHelpContent}) => {

	useEffect(() => {
		setHelpContent('doc-generator');
	}, [])

	const [inputFormat, setInputFormat] = useState('FTLX')
	const [outputFormat, setOutputFormat] = useState('HTML')
	const [docContent, setDocContent] = useState('')
	const [freemarkerJsonData, setFreemarkerJsonData] = useState('{"docTitle":"My FreeMarker Template Sample Doc Title"}')
	const [docOutput, setDocOutput] = useState(null)
	const [docFormat, setDocFormat] = useState(null)
	const [generationTime, setGenerationTime] = useState(null)
	const [outputMessage, setOutputMessage] = useState(null)
	const [validationEnabled, setValidationEnabled] = useState(false)

	const handleGenerate = (e) => {
		e.preventDefault();
		if (outputFormat == null) {
			handleOpenDialog("Select an output format");
		} else {
			let payload = {}
			payload.inputFormat = inputFormat;
			payload.outputFormat = outputFormat;
			payload.docContent = docContent;
			payload.freemarkerJsonData = freemarkerJsonData;
			appService.doAjaxJson('POST', '/generate/document', payload).then(response => {
				if (response.success) {
					setDocOutput( response.result.docOutputBase64 )
					setDocFormat( outputFormat )
					setGenerationTime( response.result.generationTime )
					setOutputMessage( response.result.message )
				}
			})
		}
	};

	const handleValidate = (e) => {
		e.preventDefault();
		if (outputFormat == null) {
			handleOpenDialog("Select an output format");
		} else {
			let payload = {}
			payload.inputFormat = inputFormat;
			payload.outputFormat = outputFormat;
			payload.docContent = docContent;
			appService.doAjaxJson('POST', '/generate/validate', payload).then(response => {
				if (response.success) {
					setDocOutput( response.result.docOutputBase64 )
					setDocFormat( outputFormat )
					setOutputMessage( response.result.message )
				}
			})
		}
	};

	const handleInputFormat = (e) => {
		e.preventDefault();
		setInputFormat( e.target.value )
		setValidationEnabled( (e.target.value !== 'FTLX' && e.target.value !== 'KTS' ) )
	};

	const handleFormat = (e) => {
		e.preventDefault();
		setOutputFormat( e.target.value )
		setDocContent( docContent )
	};

	const handleDoc = ( newValue ) => {
		setOutputFormat( outputFormat )
		setDocContent( newValue )
	};

	const handleFreemarkerData = ( newValue ) => {
		setOutputFormat( outputFormat )
		setFreemarkerJsonData( newValue )
	};

	const handleEditorContent = ( content ) => {
		setDocContent( content )
	};

	const handleJsonDataContent = ( content ) => {
		setFreemarkerJsonData( content )
	};

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
		if ( inputFormat != null ) {
			if ( inputFormat === 'KTS' ) {
				editorInFormat = 'kotlin'
				freemarkerJsonDataSection = <Fragment>
					<p style={{paddingTop: "20px"}}>Json properties will be available in KTS as a map named 'data'. Kotlin module has some built in function to access the map : attStr, attList, attMap and attListMap</p>
					<p>For instance a json like {"{ \"docTitle\": \"My Kotlin Template Sample Doc Title\" }"}, will be bound as 'data' parameter and can be accessed with attStr( data, "docTitle" ) in template</p>
					<AceEditor
						mode="json"
						theme="xcode"
						name="DOC_JSON_EDITOR"
						editorProps={{ $blockScrolling: true }}
						enableBasicAutocompletion={true}
						enableLiveAutocompletion={true}
						enableSnippets={true}
						value={freemarkerJsonData}
						onChange={handleFreemarkerData}
						height='200px'
						width='100%'
					/>
				</Fragment>
			} else if ( inputFormat === 'FTLX' ) {
				editorInFormat = 'ftl'
				freemarkerJsonDataSection = <Fragment>
					<p style={{paddingTop: "20px"}}>Json properties will be available in FTL. each property as a freemarker variable.</p>
					<p>For instance a json like {"{ \"docTitle\": \"My FreeMarker Template Sample Doc Title\" }"}, can be accessed like {"$"}{"{docTitle}"} in template</p>
					<AceEditor
						mode="json"
						theme="xcode"
						name="DOC_JSON_EDITOR"
						editorProps={{ $blockScrolling: true }}
						enableBasicAutocompletion={true}
						enableLiveAutocompletion={true}
						enableSnippets={true}
						value={freemarkerJsonData}
						onChange={handleFreemarkerData}
						height='200px'
						width='100%'
					/>
				</Fragment>
			} else {
				editorInFormat = inputFormat.toLowerCase()
			}
		}

		let outputData = <Fragment>Here will be the output</Fragment>
		if ( docOutput != null && docFormat != null ) {
			if ( docFormat === 'HTML' ) {
				let decodedStringAtoB = atob(docOutput);
				outputData = <div contentEditable='true' dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(decodedStringAtoB) }}></div>
			}  else if ( docFormat === 'PDF' || docFormat === 'PDFA' || docFormat === 'PDFUA' || docFormat === 'OPENPDF' ) {
				let srcData = 'data:application/pdf;base64,'+ docOutput;
				outputData = <embed width="100%" height="600" src={srcData}/>
			}  else if ( docFormat === 'XLSX' ) {
				let srcData = 'data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,'+ docOutput;
				outputData = <a href={srcData} download='generated_document.xlsx'>generated_document.xlsx</a>
			}  else if ( docFormat === 'OPENRTF' ) {
				let srcData = 'data:application/rtf;base64,'+ docOutput;
				outputData = <a href={srcData} download='generated_document.rtf'>generated_document.rtf</a>
			} else if ( docFormat === 'MD' ) {
				let decodedStringAtoB = myAtob(docOutput);
				outputData = <MarkdownEditor key={new Date().getTime()} value={decodedStringAtoB} />
			} else if ( docFormat === 'XML' || docFormat === 'FO' ) {
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
            } else if ( docFormat === 'JSON' ) {
                let decodedStringAtoB = myAtob(docOutput);
                outputData = <AceEditor
                    mode='json'
                    theme="xcode"
                    name="DOC_JSON_OUTPUT"
                    editorProps={{ $blockScrolling: true }}
                    enableBasicAutocompletion={true}
                    enableLiveAutocompletion={true}
                    enableSnippets={true}
                    value={decodedStringAtoB}
                    width='100%'
                />
            } else if ( docFormat === 'YAML' ) {
                let decodedStringAtoB = myAtob(docOutput);
                outputData = <AceEditor
                    mode='yaml'
                    theme="xcode"
                    name="DOC_YAML_OUTPUT"
                    editorProps={{ $blockScrolling: true }}
                    enableBasicAutocompletion={true}
                    enableLiveAutocompletion={true}
                    enableSnippets={true}
                    value={decodedStringAtoB}
                    width='100%'
                />
			} else if ( docFormat === 'CSV' ) {
				let decodedStringAtoB = myAtob(docOutput);
				outputData = <AceEditor
					mode='text'
					theme="xcode"
					name="DOC_CSV_OUTPUT"
					editorProps={{ $blockScrolling: true }}
					enableBasicAutocompletion={true}
					enableLiveAutocompletion={true}
					enableSnippets={true}
					value={decodedStringAtoB}
					width='100%'
				/>
			} else if ( docFormat === 'ADOC' ) {
				let decodedStringAtoB = myAtob(docOutput);
				outputData = <AceEditor
					mode='text'
					theme="xcode"
					name="DOC_ADOC_OUTPUT"
					editorProps={{ $blockScrolling: true }}
					enableBasicAutocompletion={true}
					enableLiveAutocompletion={true}
					enableSnippets={true}
					value={decodedStringAtoB}
					width='100%'
				/>
			}
			outputData = <Fragment>{outputData}<p>Generation time : {generationTime}</p></Fragment>
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
					<DocCatalog key={inputFormat}
								currentType={inputFormat}
								handleEditorContent={handleEditorContent}
								handleJsonDataContent={handleJsonDataContent}
					/>
				</Grid>
				<Grid item xs={6} md={3}>
					<FormLabel>Source type</FormLabel>
				</Grid>
				<Grid item xs={6} md={3}>
					<FormControl fullWidth>
						<Select
							id="input-type-select"
							onChange={handleInputFormat}
							value={inputFormat}
						>
							<MenuItem value='FTLX'>FTLX (FreeMarker Template XML)</MenuItem>
							<MenuItem value='XML'>XML</MenuItem>
							<MenuItem value='JSON'>JSON</MenuItem>
							<MenuItem value='YAML'>YAML</MenuItem>
							<MenuItem value='KTS'>KTS</MenuItem>
						</Select>
					</FormControl>
				</Grid>
				<Grid item xs={6} md={3}>
					<FormLabel>Output format</FormLabel>
				</Grid>
				<Grid item xs={6} md={3}>
					<FormControl fullWidth>
						<Select
							id="output-type-select"
							onChange={handleFormat}
							value={outputFormat}
						>
							<MenuItem value='HTML'>HTML</MenuItem>
							<MenuItem value='PDF'>PDF (fop)</MenuItem>
							<MenuItem value='PDFA'>PDF/A (fop)</MenuItem>
                            <MenuItem value='PDFUA'>PDF/UA-1 (fop)</MenuItem>
							<MenuItem value='XLSX'>XLSX</MenuItem>
							<MenuItem value='MD'>Markdown (MD)</MenuItem>
							<MenuItem value='XML'>Venus XML Doc</MenuItem>
                            <MenuItem value='JSON'>Venus JSON Doc</MenuItem>
                            <MenuItem value='YAML'>Venus YAML Doc</MenuItem>
							<MenuItem value='FO'>XSL-FO</MenuItem>
							<MenuItem value='CSV'>CSV</MenuItem>
							<MenuItem value='OPENPDF'>PDF (openpdf)</MenuItem>
							<MenuItem value='OPENRTF'>RTF (openrtf)</MenuItem>
							<MenuItem value='ADOC'>AsciiDoc</MenuItem>
						</Select>
					</FormControl>
				</Grid>
				<Grid item xs={6}>
					<Button variant="contained" onClick={handleGenerate}>Generate document</Button>
				</Grid>
				<Grid item xs={6}>
					<Button variant="contained" disabled={!validationEnabled} onClick={handleValidate}>Validate (only XML, JSON and YAML sources)</Button>
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

	return handleContent();

}

export default DocXmlEditor;