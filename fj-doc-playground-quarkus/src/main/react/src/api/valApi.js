import { getMultipart, postMultipart } from '../common/httpClient';

/** GET /val/supported_extensions */
export const getSupportedExtensions = () => getMultipart('/val/supported_extensions');

/** POST /val/check — multipart/form-data con 'file' */
export const checkFile = (formData) => postMultipart('/val/check', formData);
