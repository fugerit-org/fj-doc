import { getMultipart, postJson } from '../common/httpClient';

/** GET /project/extensions-list */
export const getExtensionsList = () => getMultipart('/project/extensions-list');

/** POST /project/init */
export const initProject = (payload) => postJson('/project/init', payload);
