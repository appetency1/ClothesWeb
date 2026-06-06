import { get, post } from './request.js'

// 前台接口
export function getOrderList(params) {
  return get('/api/order/list', params)
}

export function createOrder(data) {
  return post('/api/order/create', data)
}

export function payOrder(orderId) {
  return post('/api/order/pay', { orderId })
}

export function confirmOrder(orderId) {
  return post('/api/order/confirm', { orderId })
}

export function deleteOrder(orderId) {
  return post('/api/order/delete', { orderId })
}

// 后台管理接口
export function getAdminOrderList(params) {
  return get('/api/admin/order/list', params)
}

export function shipOrder(orderId) {
  return post('/api/admin/order/ship', { orderId })
}