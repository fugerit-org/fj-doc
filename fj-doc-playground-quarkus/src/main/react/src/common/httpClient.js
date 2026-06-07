/**
 * Lightweight HTTP client using native fetch.
 * Replaces axios dependency.
 */

const BASE_API = '/fj-doc-playground/api';

/**
 * Core fetch wrapper that returns { success, status, result }.
 */
async function request(method, path, { body, headers = {}, blob = false } = {}) {
  // Validate path to prevent open redirect and client-side SSRF/request forgery
  if (typeof path !== 'string' || !path.startsWith('/') || path.startsWith('//') || path.includes('://')) {
    throw new Error(`[httpClient] Safe path validation failed for: ${path}`);
  }

  // Safely resolve the API endpoint relative to the current origin
  const resolvedUrl = new URL(`${BASE_API}${path}`, window.location.origin);
  if (method === 'GET') {
    resolvedUrl.searchParams.set('v', String(Date.now()));
  }
  const url = resolvedUrl.toString();

  const config = { method, headers };
  if (body !== undefined) {
    config.body = body;
  }

  // Auth token from session
  const token = sessionStorage.getItem('token');
  if (token) config.headers['token'] = token;

  try {
    const response = await fetch(url, config);
    if (blob) {
      const data = await response.blob();
      return { success: response.ok, status: response.status, result: data };
    }
    // Try JSON, fall back to text
    const contentType = response.headers.get('content-type') || '';
    let data;
    if (contentType.includes('application/json')) {
      data = await response.json();
    } else {
      data = await response.text();
    }
    return { success: response.ok, status: response.status, result: data };
  } catch (err) {
    console.error('[httpClient] Network error:', err);
    return { success: false, status: 0, result: null, error: err };
  }
}

export function getJson(path) {
  return request('GET', path, { headers: { 'Content-Type': 'application/json' } });
}

export function getText(path) {
  return request('GET', path, {});
}

export function postJson(path, data) {
  return request('POST', path, {
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  });
}

export function postMultipart(path, formData) {
  // Do NOT set Content-Type — browser sets it with boundary automatically
  return request('POST', path, { body: formData });
}

export function getMultipart(path) {
  return request('GET', path, {});
}
