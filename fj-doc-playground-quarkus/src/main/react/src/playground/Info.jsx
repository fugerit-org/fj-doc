import React, { useState, useEffect, Fragment } from 'react';
import appService from '../common/app-service';

const Info = () => {

	const [metaInfo, setMetaInfo] = useState(null)

	useEffect(() => {
		appService.doAjaxMultipart('GET', '/meta/info', null).then(response => {
			if (response.success) {
				setMetaInfo( response.result )
			}
		})
	}, []);

	const handleContent = () => {
		let printInfo = 'loading info...';
		if ( metaInfo != null ) {
			printInfo = Object.keys(metaInfo).map((key, index) => <span key={index}><strong>{key}</strong>={metaInfo[key]} </span>)
		}
		return <Fragment>
			<p>Info : {printInfo}</p>
		</Fragment>
	}

	return handleContent()

}

export default Info;