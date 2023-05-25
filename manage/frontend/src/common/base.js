export function getLocale() {
  // TODO stores and impl later
  return 'mn'
}

export function getApiUrl() {
  return process.env.NEXT_PUBLIC_MANAGE_API_URL
}

export function getCdnUploadUrl() {
  return process.env.NEXT_PUBLIC_CDN_URL + '/upload'
}
