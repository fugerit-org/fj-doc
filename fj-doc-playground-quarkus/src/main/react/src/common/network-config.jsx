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

export const onResponse = function (response) {
  return response;
}

export const onResponseError = function (error) {
  switch (error.response?.status) {
    case 403:
      window.location.href = import.meta.env.BASE_URL + 'fj-doc-playground/home'
      break
    default:
      window.location.href = import.meta.env.BASE_URL
      break
  }
}
