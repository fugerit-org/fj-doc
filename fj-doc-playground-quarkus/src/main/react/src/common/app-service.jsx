import FetchService from "./fetch-service";
import {
  onRequestConfig,
  onRequestConfigError,
  onResponse,
  onResponseError,
  onNoServerResponse,
} from "./network-config";

const appService = new FetchService(onRequestConfig, onRequestConfigError, onResponse, onResponseError, onNoServerResponse);

export default appService;