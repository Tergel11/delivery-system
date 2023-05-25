import {getApiUrl} from '../../base'
import {jsonRequestWithToken} from '../../util/request'

const baseUrl = '/v1/application-role'

export async function list(token) {
  return jsonRequestWithToken(`${getApiUrl()}${baseUrl}`, 'GET', token)
}
