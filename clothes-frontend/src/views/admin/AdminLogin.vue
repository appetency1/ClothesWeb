<template>
  <div class="admin-login-page">
    <div class="login-container">
      <div class="login-header">
        <h1>管理员登录</h1>
        <p>网上衣橱后台管理系统</p>
      </div>

      <form class="login-form" @submit.prevent="handleLogin">
        <div class="form-group">
          <label>用户名</label>
          <input
            v-model="form.username"
            type="text"
            placeholder="请输入管理员用户名"
            required
          />
        </div>

        <div class="form-group">
          <label>密码</label>
          <input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            required
          />
        </div>

        <div v-if="error" class="error-message">
          {{ error }}
        </div>

        <button type="submit" class="btn-login" :disabled="loading">
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </form>

      <div class="login-footer">
        <router-link to="/">返回前台首页</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()

const form = ref({
  username: '',
  password: ''
})

const loading = ref(false)
const error = ref('')

const handleLogin = async () => {
  loading.value = true
  error.value = ''

  try {
    // Mock admin login - in real app, call API
    if (form.value.username === 'admin' && form.value.password === 'admin123') {
      localStorage.setItem('adminToken', 'mock-admin-token')
      localStorage.setItem('isAdmin', 'true')
      ElMessage.success('登录成功')
      router.push('/admin')
    } else {
      error.value = '用户名或密码错误'
    }
  } catch (err) {
    error.value = '登录失败，请重试'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.admin-login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-bg);
}

.login-container {
  width: 100%;
  max-width: 420px;
  padding: 40px;
  background: var(--color-card);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.login-header h1 {
  font-size: var(--text-2xl);
  font-weight: 600;
  color: var(--color-text);
  margin-bottom: 8px;
}

.login-header p {
  color: var(--color-text-secondary);
  font-size: var(--text-sm);
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-group label {
  font-size: var(--text-sm);
  font-weight: 500;
  color: var(--color-text);
}

.form-group input {
  height: 44px;
  padding: 0 16px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font-size: var(--text-base);
  transition: all 0.2s;
}

.form-group input:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px rgba(196, 154, 108, 0.15);
}

.error-message {
  color: var(--color-danger);
  font-size: var(--text-sm);
  text-align: center;
}

.btn-login {
  height: 48px;
  background: var(--color-primary);
  color: white;
  border: none;
  border-radius: var(--radius-md);
  font-size: var(--text-base);
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-login:hover:not(:disabled) {
  background: var(--color-primary-dark);
}

.btn-login:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.login-footer {
  margin-top: 24px;
  text-align: center;
}

.login-footer a {
  color: var(--color-primary);
  font-size: var(--text-sm);
  text-decoration: none;
}

.login-footer a:hover {
  text-decoration: underline;
}
</style>