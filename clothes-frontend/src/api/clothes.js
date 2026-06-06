import { get, post } from './request.js'

export function getClothesList(params) {
  return get('/api/clothes/list', params)
}

export function getClothesDetail(id) {
  return get('/api/clothes/detail', { id })
}

export function uploadImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  
  return fetch('/api/upload', {
    method: 'POST',
    body: formData,
    headers: {
      'token': localStorage.getItem('adminToken') || localStorage.getItem('token') || ''
    }
  }).then(response => response.json())
   .then(data => {
     if (data.code !== 200) {
       throw new Error(data.message || '上传失败')
     }
     return data.data
   })
}

// 后台管理接口
export function getAdminClothesList(params) {
  return get('/api/admin/clothes/list', params)
}

export function getAdminClothesDetail(id) {
  return get('/api/admin/clothes/detail', { id })
}

export function addClothes(data) {
  return post('/api/admin/clothes/add', data)
}

export function updateClothes(data) {
  return post('/api/admin/clothes/update', data)
}

export function deleteClothes(id) {
  return post('/api/admin/clothes/delete', null, { params: { id } })
}