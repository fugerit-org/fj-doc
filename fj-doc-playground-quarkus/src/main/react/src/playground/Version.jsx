import React, { useState, useEffect, Fragment } from 'react';
import appService from '../common/app-service';

const Version = () => {

	const [versionInfo, setVersionInfo] = useState(null)

	useEffect(() => {
		appService.doAjaxMultipart('GET', '/meta/version', null).then(response => {
			if (response.success) {
				setVersionInfo( response.result )
			}
		})
	}, []);

	return <Fragment>
		<p>fj-doc-version : {!versionInfo && 'loading...'} {versionInfo && versionInfo.version &&  (versionInfo.version + ' ('+versionInfo.revision+')')}</p>
	</Fragment>

}

export default Version;