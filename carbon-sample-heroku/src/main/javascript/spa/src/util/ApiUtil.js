import config from '../config/config';

const { host } = config;
const baseUrl = `${host}/api`;

const createUrlParam = (param) => {
  const toQuery = (k, v) => `${k}=${v}`;
  const params = [];
  Object.keys(param)
    .forEach((key) => {
      const value = param[key];
      if (value) {
        if (value.length > 0) {
          value.forEach(el => params.push(toQuery(key, el)));
        }

        params.push(toQuery(key, value));
      }
    });
  return `?${params.join('&')}`;
};

const createOption = (method, authToken, body) => {
  const { token } = authToken;
  const headers = {};
  if (authToken) {
    headers.Authorization = token;
  }

  if (method !== 'GET') {
    headers['Content-Type'] = 'application/json;';
  }

  return {
    method,
    mode: 'cors',
    cache: 'no-cache',
    headers,
    body: JSON.stringify(body),
  };
};

const pageHandler = (json) => {
  if (!json) {
    return {};
  }

  const { page } = json;
  if (!page) {
    return json;
  }

  const total = page.total;
  const current = page.current + 1;

  return Object.assign({}, json, {
    page: {
      total,
      current,
      hasNext: current < total,
      hasPrev: current > 1,
    },
  });
};

export const handleError = (error) => {
  const exception =  {
    message: error.message,
    detail: error.stack,
  };

  throw exception;
};

export const handleResponse = (data) => {
  const status = data.status;
  if (status === 200) {
    return data.json()
      .then(pageHandler);
  }

  return data.json()
    .then(handleError);
};

export const getJson = (path, authToken = { token: null }, param) => {
  const op = createOption('GET', authToken);
  const urlParam = param ? createUrlParam(param) : '';
  return fetch(baseUrl + path + urlParam, op)
    .catch(handleError)
    .then(handleResponse);
};

export const postJson = (
  path,
  authToken = { token: null },
  body,
) => {
  const op = createOption('POST', authToken, body);
  return fetch(baseUrl + path, op)
    .catch(handleError)
    .then(handleResponse);
};

export const putJson = (
  path,
  authToken = { token: null },
  body,
) => {
  const op = createOption('PUT', authToken, body);
  return fetch(baseUrl + path, op)
    .catch(handleError)
    .then(handleResponse);
};
