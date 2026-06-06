<template>
  <div class="profile-page">
    <div class="container">
      <h1 class="page-title">个人中心</h1>
      <div class="profile-card">
        <div class="profile-header">
          <div class="avatar">
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
              <circle cx="12" cy="7" r="4"></circle>
            </svg>
          </div>
          <div class="profile-info">
            <h2>{{ user.username }}</h2>
            <p>{{ user.phone }}</p>
          </div>
        </div>
        <div class="profile-form">
          <div class="form-group">
            <label>用户名</label>
            <input v-model="form.username" type="text" class="form-input" />
          </div>
          <div class="form-group">
            <label>手机号</label>
            <input v-model="form.phone" type="tel" class="form-input" />
          </div>
          <div class="form-group">
            <label>地址</label>
            <input v-model="form.address" type="text" class="form-input" placeholder="请输入收货地址" />
          </div>
          <button class="btn btn-primary" @click="handleUpdate">保存修改</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserInfo, updateUser } from '../api/user.js'

const user = ref({
  username: '用户123',
  phone: '138****8888'
})

const form = reactive({
  username: '',
  phone: '',
  address: ''
})

const loading = ref(false)

const loadUserInfo = async () => {
  try {
    const data = await getUserInfo()
    user.value = data
    form.username = data.username || ''
    form.phone = data.phone || ''
    form.address = data.address || ''
  } catch (error) {
    console.error('Failed to load user info:', error)
    // Fallback to localStorage
    const userData = JSON.parse(localStorage.getItem('user') || '{}')
    if (userData.username) {
      user.value = userData
      form.username = userData.username
      form.phone = userData.phone || ''
      form.address = userData.address || ''
    }
  }
}

const handleUpdate = async () => {
  // Validate username length
  if (form.username.length < 2 || form.username.length > 10) {
    ElMessage.error('用户名长度应为2-10个字符')
    return
  }

  // Validate phone format
  if (!/^1[3-9]\d{9}$/.test(form.phone)) {
    ElMessage.error('手机号格式不正确')
    return
  }

  loading.value = true
  try {
    await updateUser({
      username: form.username,
      phone: form.phone,
      address: form.address
    })
    
    // Update localStorage
    const userData = JSON.parse(localStorage.getItem('user') || '{}')
    userData.username = form.username
    userData.phone = form.phone
    userData.address = form.address
    localStorage.setItem('user', JSON.stringify(userData))
    
    user.value = { ...user.value, ...form }
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error(error.message || '保存失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.profile-page {
  padding: var(--spacing-8) 0;
}

.page-title {
  font-size: var(--text-2xl);
  font-weight: var(--font-medium);
  margin-bottom: var(--spacing-8);
}

.profile-card {
  background: var(--color-card);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  padding: var(--spacing-8);
  max-width: 600px;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: var(--spacing-4);
  margin-bottom: var(--spacing-8);
  padding-bottom: var(--spacing-6);
  border-bottom: 1px solid var(--color-divider);
}

.avatar {
  width: 64px;
  height: 64px;
  border-radius: var(--radius-full);
  background: var(--color-primary-light);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-primary);
}

.profile-info h2 {
  font-size: var(--text-xl);
  font-weight: var(--font-medium);
  margin-bottom: var(--spacing-1);
}

.profile-info p {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
}

.profile-form {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-5);
}

.profile-form .form-group {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-2);
}

.profile-form label {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
}
</style>