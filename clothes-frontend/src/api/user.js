import { get, post } from './request.js'

// 前台接口
export function register(data) {
  return post('/api/user/register', data)
}

export function login(data) {
  return post('/api/user/login', data)
}

export function getUserInfo() {
  return get('/api/user/info')
}

export function updateUser(data) {
  return post('/api/user/update', data)
}

// 后台管理接口
export function getAdminUserList(params) {
  return get('/api/admin/user/list', params)
}

export function addAdminUser(data) {
  return post('/api/admin/user/add', data)
}

export function updateAdminUser(data) {
  return post('/api/admin/user/update', data)
}

export function deleteAdminUser(id) {
  return post('/api/admin/user/delete', null, { params: { id } })
}