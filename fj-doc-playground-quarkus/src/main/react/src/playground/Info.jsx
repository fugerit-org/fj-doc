import React, { Component, Fragment } from 'react';
import appService from '../common/app-service';

class Info extends Component {

	constructor(props) {
		super(props);
		this.state = {
			metaInfo: null
		}
	}

	componentDidMount() {
		let reactState = this;
		appService.doAjaxMultipart('GET', '/meta/info', null).then(response => {
			if (response.success) {
				reactState.setState({
					metaInfo: response.result
				})
			} else {
				reactState.setState({
					supportedExtensions: null
				})
			}
		})
	}

	render() {
		let printInfo = 'loading info...';
		if ( this.state.metaInfo != null ) {
			printInfo = Object.keys(this.state.metaInfo).map((key, index) => <span key={index}><strong>{key}</strong>={this.state.metaInfo[key]} </span>)
		}
		return <Fragment>
			<p>Info : {printInfo}</p>
		</Fragment>
	}

}

export default Info;