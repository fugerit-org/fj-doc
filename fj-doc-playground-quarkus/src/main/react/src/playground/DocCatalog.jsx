import React, { Component, Fragment } from 'react';
import { Form, Col, Row } from 'react-bootstrap';
import appService from '../common/app-service';

class DocCatalog extends Component {

	constructor(props) {
		super(props);
		this.handleSelectDocument = this.handleSelectDocument.bind(this);
		this.state = {
			initEntryId: this.props.defaultDocId,
			currentEntryId: this.props.defaultDocId,
			currentType: this.props.currentType,
			updateType: null,
			entryList: null
		}
	}	

	componentDidMount() {
		this.loadList( this.state.currentType );
	}

    loadList( type ) {
		let reactState = this;
		let catalogUrl = '/catalog/list';
		if ( type != null ) {
			catalogUrl+= '/type/' + type;
		}
		appService.doAjaxMultipart('GET', catalogUrl, null).then(response => {
			if (response.success) {
				reactState.setState({
					entryList: response.result
				}, () => this.loadDocument() )
			} else {
				reactState.setState({
					entryList: null
				})
			}
		})
	}
	
	loadDocument( id ) {
		if ( id == null ) {
			id = this.state.entryList[0].key
		}
		let reactState = this;
		appService.doAjaxMultipart('GET', '/catalog/entry/'+id, null).then(response => {
			if (response.success) {
				reactState.props.handleEditorContent( response.result.docOutput );
				if ( (reactState.props.handleJsonDataContent) && (response.result.jsonData) ) {
					reactState.props.handleJsonDataContent( response.result.jsonData );
				}
			} else {
				reactState.setState({
					currentEntryData: null
				})
			}
		})
	}

	handleSelectDocument = (e) => {
		e.preventDefault();
		this.loadDocument( e.target.value );
	};

	render() {
		let catalogSelection = <Fragment>Loading</Fragment>;
		if ( this.state.entryList != null ) {
			let options = this.state.entryList.map( (current) =>  <option key={current.key} value={current.key}>{current.label}</option>)
			catalogSelection = <Fragment>
				<Form>
				<Row>
					<Col>
						<Form.Label>Document samples catalog</Form.Label>
					</Col>
					<Col>
						<Form.Select aria-label="Select output format" onChange={this.handleSelectDocument}>
							{options}
						</Form.Select>
					</Col>
				</Row>				
			</Form>
			</Fragment>
		}
		return catalogSelection
	}

}

export default DocCatalog;