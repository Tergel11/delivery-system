import {getApiUrl} from '../../base'
import {toQueryString} from '../../util/queryString'
import {jsonRequestWithToken} from '../../util/request'

const baseUrl = '/v1/locale'

export async function list(params, token) {
  return jsonRequestWithToken(getApiUrl() + `${baseUrl}?${toQueryString(params)}`, 'GET', token)
}

export async function select(params, token) {
  return jsonRequestWithToken(getApiUrl() + `${baseUrl}/select?${toQueryString(params)}`, 'GET', token)
}

export async function get(params, token) {
  return jsonRequestWithToken(getApiUrl() + `${baseUrl}/${params.id}`, 'GET', token)
}

export async function getWithCode(params, token) {
  return jsonRequestWithToken(getApiUrl() + `${baseUrl}/get-with-code/${params.code}`, 'GET', token)
}

export async function create(params, token) {
  return jsonRequestWithToken(getApiUrl() + `${baseUrl}/create`, 'POST', token, JSON.stringify(params))
}

export async function update(params, token) {
  return jsonRequestWithToken(getApiUrl() + `${baseUrl}/update`, 'POST', token, JSON.stringify(params))
}

export async function deleteOne (params, token) {
  return jsonRequestWithToken(getApiUrl() + `${baseUrl}/delete?${toQueryString(params)}`, 'POST', token)
}
