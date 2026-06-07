import { postJson } from '../common/httpClient';

/** POST /generate/document */
export const generateDocument = (payload) => postJson('/generate/document', payload);

/** POST /generate/validate */
export const validateDocument = (payload) => postJson('/generate/validate', payload);
