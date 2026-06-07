import { getMultipart } from '../common/httpClient';

/** GET /meta/version */
export const getVersion = () => getMultipart('/meta/version');

/** GET /meta/info */
export const getInfo = () => getMultipart('/meta/info');

/** GET /meta/available-versions */
export const getAvailableVersions = () => getMultipart('/meta/available-versions');
