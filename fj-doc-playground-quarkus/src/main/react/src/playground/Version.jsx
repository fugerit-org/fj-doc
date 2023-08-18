import React, { Component, Fragment } from 'react';
import appService from '../common/app-service';

class Version extends Component {

	constructor(props) {
		super(props);
		this.state = {
			versionInfo: null
		}
	}

	componentDidMount() {
		let reactState = this;
		appService.doAjaxMultipart('GET', '/meta/version', null).then(response => {
			if (response.success) {
				reactState.setState({
					versionInfo: response.result
				})
			} else {
				reactState.setState({
					supportedExtensions: null
				})
			}
		})
	}

	render() {
		let printVersion = 'loading...';
		if ( this.state.versionInfo != null ) {
			printVersion = this.state.versionInfo.version + ' ('+this.state.versionInfo.revision+')';	
		}
		return <Fragment>
			<p>fj-doc-version : {printVersion}</p>
		</Fragment>
	}

}

export default Version;