import { get } from './request.js'

export function getTypeList() {
  return get('/api/type/list')
}

export function getSizeList(typeId) {
  return get('/api/size/list', { typeId })
}