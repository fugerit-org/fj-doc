export const onRequestConfig = function (config) {
  const token = sessionStorage.getItem("token");
  if (token) config.headers.token = token;
}

export const onRequestConfigError = function (error) {
  console.log(error)
}

export const onNoServerResponse = function (error) {
  console.log(error)
}

export const onResponseError = function (error) {
  switch (error.response.status) {
    case 403:
      window.location.href = process.env.CONTEXT_PATH + '/fj-doc-playground/home'
      break
    default:
      window.location.href = process.env.CONTEXT_PATH + '/'
      break
  }
}
