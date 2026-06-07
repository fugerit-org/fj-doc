import { postJson } from '../common/httpClient';

/** POST /convert/doc */
export const convertDoc = (payload) => postJson('/convert/doc', payload);

/** POST /config/convert */
export const convertConfig = (payload) => postJson('/config/convert', payload);
