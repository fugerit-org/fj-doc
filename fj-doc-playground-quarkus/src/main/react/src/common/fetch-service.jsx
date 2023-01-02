import axios from 'axios';


const defaultApiPath= '/fj-doc-playground/api';

class FetchService {
  constructor(onRequestConfig, onRequestConfigError, onResponse, onResponseError, onNoServerResponse) {
    this.onRequestConfig = onRequestConfig && (typeof onRequestConfig === 'function') ? onRequestConfig : () => {
    }
    this.onRequestConfigError = onRequestConfigError && (typeof onRequestConfigError === 'function') ? onRequestConfigError : () => {
    }
    this.onResponse = onRequestConfig && (typeof onResponse === 'function') ? onResponse : () => {
    }
    this.onResponseError = onResponseError && (typeof onResponseError === 'function') ? onResponseError : () => {
    }
    this.onNoServerResponse = onNoServerResponse && (typeof onNoServerResponse === 'function') ? onNoServerResponse : () => {
    }
  }


  doAjaxMultipart(method, path, data) {
    let basePath = ''
    let apiPath = defaultApiPath;
     let headers = {
       	"Content-Type": "multipart/form-data"
      } 
    return this.doAjaxWorker(method, path, basePath, apiPath, headers, false, false, data, '');
  }

  doAjaxJson(method, path, data) {
    let basePath = ''
    let apiPath = defaultApiPath;
    let headers = {
        "Content-Type": "application/json"
      } 
    return this.doAjaxWorker(method, path, basePath, apiPath, headers, true, false, data, '');
  }
  
  doAjaxJsonToBlob(method, path, data) {
    let basePath = ''
    let apiPath = defaultApiPath;
    let headers = {
        "Content-Type": "application/json"
      } 
    return this.doAjaxWorker(method, path, basePath, apiPath, headers, true, true, data, '');
  }

  doAjax(method, path, headers, json, responseBlob, data) {
    let basePath = ''
    let apiPath = defaultApiPath;
    return this.doAjaxWorker(method, path, basePath, apiPath, headers, json, data, headers, '');
  }

  doAjaxWorker(method, path, basePath, apiPath, headers, json, responseBlob, data, queryParams) {
    const uri = `${basePath}${apiPath}${path}?v=${Date.now()}${queryParams}`
    let config = {
      method: method,
      headers: headers,
      url: uri,
    }
    
    if ( responseBlob ) {
	config.responseType = 'blob'
}

    if ( json )
		config.data = data ? JSON.stringify(data) : null
	else 
		config.data = data

    this.onRequestConfig.call(this, config)
    let request = axios(config);

    return request.then(response => {
      this.onResponse.call(this, response)
      return {success: true, status: response.status, result: response.data}
    })
      .catch(error => {
        if (error.response) {
          this.onResponseError.call(this, error)
          return {success: false, status: error.response.status, result: error.response.data}
        } else if (error.request) {
          this.onNoServerResponse.call(this, error)
        } else {
          this.onRequestConfigError.call(this, error)
        }
      });
  }

}

export default FetchService;
