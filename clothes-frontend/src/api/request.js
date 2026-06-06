// API请求封装
const BASE_URL = ''

// 请求拦截
async function request(url, options = {}) {
  const token = localStorage.getItem('token') || localStorage.getItem('adminToken')
  
  const config = {
    headers: {
      'Content-Type': 'application/json',
      ...(token && { 'token': token }),
      ...options.headers
    },
    ...options
  }

  if (config.body && typeof config.body === 'object') {
    config.body = JSON.stringify(config.body)
  }

  try {
    const response = await fetch(`${BASE_URL}${url}`, config)
    const data = await response.json()
    
    if (data.code !== 200) {
      throw new Error(data.message || '请求失败')
    }
    
    return data.data
  } catch (error) {
    console.error('API Error:', error)
    throw error
  }
}

// GET请求
export function get(url, params = {}) {
  const queryString = new URLSearchParams(params).toString()
  const fullUrl = queryString ? `${url}?${queryString}` : url
  return request(fullUrl, { method: 'GET' })
}

// POST请求
export function post(url, body = {}) {
  return request(url, { method: 'POST', body })
}

// PUT请求
export function put(url, body = {}) {
  return request(url, { method: 'PUT', body })
}

// DELETE请求
export function del(url, params = {}) {
  const queryString = new URLSearchParams(params).toString()
  const fullUrl = queryString ? `${url}?${queryString}` : url
  return request(fullUrl, { method: 'DELETE' })
}