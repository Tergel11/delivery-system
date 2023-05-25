export function toQueryString(params) {
  if (!params)
    return ''

  for (let param in params) {
    if (params[param] === undefined
      || params[param] === null
      || params[param] === ''
    ) {
      delete params[param]
    }
  }

  return new URLSearchParams(params).toString()
}
