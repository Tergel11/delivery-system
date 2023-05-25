import {getApiUrl} from '../../base'
import {jsonRequest} from '../../util/request'

const baseUrl = '/v1/auth'

export async function login(username, password) {
  return jsonRequest(`${getApiUrl()}${baseUrl}/login`, 'POST', JSON.stringify({username, password}))
}
