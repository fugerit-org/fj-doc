import { getMultipart } from '../common/httpClient';

export const getCatalogList = (type) => {
  const path = type ? `/catalog/list/type/${type}` : '/catalog/list';
  return getMultipart(path);
};

export const getCatalogEntry = (id) => getMultipart(`/catalog/entry/${id}`);

export const getCatalogRes = (id) => getMultipart(`/catalog/res/${id}`);
