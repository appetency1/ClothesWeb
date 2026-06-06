<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-tabs">
        <button
          :class="['tab-btn', { active: activeTab === 'login' }]"
          @click="activeTab = 'login'"
        >
          登录
        </button>
        <button
          :class="['tab-btn', { active: activeTab === 'register' }]"
          @click="activeTab = 'register'"
        >
          注册
        </button>
      </div>

      <!-- Login Form -->
      <form v-if="activeTab === 'login'" class="auth-form" @submit.prevent="handleLogin">
        <h2 class="form-title">欢迎回来</h2>
        <div class="form-group">
          <label>用户名</label>
          <input
            v-model="loginForm.username"
            type="text"
            class="form-input"
            placeholder="请输入用户名"
            required
          />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input
            v-model="loginForm.password"
            type="password"
            class="form-input"
            placeholder="请输入密码"
            required
          />
        </div>
        <button type="submit" class="btn btn-primary submit-btn">登录</button>
      </form>

      <!-- Register Form -->
      <form v-else class="auth-form" @submit.prevent="handleRegister">
        <h2 class="form-title">创建账户</h2>
        <div class="form-group">
          <label>用户名</label>
          <input
            v-model="registerForm.username"
            type="text"
            class="form-input"
            placeholder="2-10个字符"
            required
          />
        </div>
        <div class="form-group">
          <label>手机号</label>
          <input
            v-model="registerForm.phone"
            type="tel"
            class="form-input"
            placeholder="11位手机号"
            required
          />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input
            v-model="registerForm.password"
            type="password"
            class="form-input"
            placeholder="6-16个字符"
            required
          />
        </div>
        <div class="form-group">
          <label>确认密码</label>
          <input
            v-model="registerForm.confirmPassword"
            type="password"
            class="form-input"
            placeholder="再次输入密码"
            required
          />
        </div>
        <button type="submit" class="btn btn-primary submit-btn">注册</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login, register } from '../api/user.js'

const router = useRouter()
const activeTab = ref('login')

const loginForm = reactive({
  username: '',
  password: ''
})

const registerForm = reactive({
  username: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

const handleLogin = async () => {
  try {
    const data = await login(loginForm)
    localStorage.setItem('token', data.token)
    localStorage.setItem('user', JSON.stringify(data))
    ElMessage.success('登录成功')
    router.push('/')
  } catch (error) {
    ElMessage.error(error.message || '登录失败')
  }
}

const handleRegister = async () => {
  // Validate username length
  if (registerForm.username.length < 2 || registerForm.username.length > 10) {
    ElMessage.error('用户名长度应为2-10个字符')
    return
  }

  // Validate password length
  if (registerForm.password.length < 6 || registerForm.password.length > 16) {
    ElMessage.error('密码长度应为6-16个字符')
    return
  }

  // Validate phone format
  if (!/^1[3-9]\d{9}$/.test(registerForm.phone)) {
    ElMessage.error('手机号格式不正确')
    return
  }

  if (registerForm.password !== registerForm.confirmPassword) {
    ElMessage.error('两次密码输入不一致')
    return
  }

  try {
    await register({
      username: registerForm.username,
      phone: registerForm.phone,
      password: registerForm.password
    })
    ElMessage.success('注册成功')
    activeTab.value = 'login'
  } catch (error) {
    ElMessage.error(error.message || '注册失败')
  }
}
</script>

<style scoped>
.auth-page {
  min-height: calc(100vh - var(--nav-height));
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-8) 0;
}

.auth-container {
  width: 100%;
  max-width: 420px;
  background: var(--color-card);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-md);
  padding: var(--spacing-8);
}

.auth-tabs {
  display: flex;
  margin-bottom: var(--spacing-8);
  border-bottom: 1px solid var(--color-divider);
}

.tab-btn {
  flex: 1;
  padding: var(--spacing-4) 0;
  font-size: var(--text-lg);
  font-weight: var(--font-medium);
  color: var(--color-text-secondary);
  border-bottom: 2px solid transparent;
  transition: all 0.2s ease;
}

.tab-btn.active {
  color: var(--color-primary);
  border-bottom-color: var(--color-primary);
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-5);
}

.form-title {
  font-size: var(--text-2xl);
  font-weight: var(--font-medium);
  text-align: center;
  margin-bottom: var(--spacing-4);
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-2);
}

.form-group label {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text);
}

.submit-btn {
  margin-top: var(--spacing-4);
}
</style>