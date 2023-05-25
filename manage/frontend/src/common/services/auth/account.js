import {getApiUrl} from '../../base'
import {jsonRequestWithToken} from '../../util/request'

const baseUrl = '/v1/account'

export async function getProfile(token) {
  return jsonRequestWithToken(`${getApiUrl()}${baseUrl}/profile`, 'GET', token)
}

export async function updateProfile(token, params) {
  return jsonRequestWithToken(`${getApiUrl()}${baseUrl}/profile`, 'POST', token, JSON.stringify(params))
}

export async function changePassword(token, params) {
  return jsonRequestWithToken(`${getApiUrl()}${baseUrl}/change-password`, 'POST', token, JSON.stringify(params))
}
