import { get, post } from './request.js'

export function getCartList() {
  return get('/api/cart/list')
}

export function addToCart(data) {
  return post('/api/cart/add', data)
}

export function updateCart(data) {
  return post('/api/cart/update', data)
}

export function deleteCart(cartId) {
  return post('/api/cart/delete', { cartId })
}

export function clearCart() {
  return post('/api/cart/clear')
}