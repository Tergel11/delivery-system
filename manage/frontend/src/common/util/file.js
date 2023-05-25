export function getFileUrlForForm(e) {
  if (Array.isArray(e)) {
    return e
  }
  return e && e.file && e.file.response && e.file.response.data
}

export function normFile(e) {
  if (Array.isArray(e)) {
    return e
  }
  return e && e.fileList
}

export function isImage(file) {
  return file.type.startsWith('image/')
}