// ============================================
// API client — all backend calls go through here
// ============================================

async function apiRequest(method, url, body) {
  const options = {
    method: method,
    credentials: 'same-origin',
    headers: { 'Content-Type': 'application/json' }
  };
  if (body !== undefined) {
    options.body = JSON.stringify(body);
  }

  const response = await fetch(url, options);

  if (response.status === 204) return null;

  let data = null;
  const text = await response.text();
  if (text) {
    try { data = JSON.parse(text); } catch { data = text; }
  }

  if (!response.ok) {
    const message = (data && data.message) ? data.message : ('Request failed: ' + response.status);
    const err = new Error(message);
    err.status = response.status;
    err.body = data;
    throw err;
  }
  return data;
}

const API = {
  get:    (url)       => apiRequest('GET', url),
  post:   (url, body) => apiRequest('POST', url, body),
  put:    (url, body) => apiRequest('PUT', url, body),
  delete: (url)       => apiRequest('DELETE', url)
};