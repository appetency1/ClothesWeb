<template>
  <nav class="navbar">
    <div class="container">
      <div class="nav-content">
        <!-- Logo -->
        <router-link to="/" class="logo">
          <span class="logo-text">极简衣橱</span>
        </router-link>

        <!-- Search Bar -->
        <div class="search-bar">
          <input
            v-model="searchQuery"
            type="text"
            placeholder="搜索商品..."
            @keyup.enter="handleSearch"
          />
          <button class="search-btn" @click="handleSearch">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="11" cy="11" r="8"></circle>
              <path d="M21 21l-4.35-4.35"></path>
            </svg>
          </button>
        </div>

        <!-- Nav Actions -->
        <div class="nav-actions">
          <router-link to="/cart" class="action-btn cart-btn">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M6 6h15l-1.5 9h-12z"></path>
              <path d="M6 6l-1-4H3"></path>
              <circle cx="9" cy="19" r="1"></circle>
              <circle cx="18" cy="19" r="1"></circle>
            </svg>
            <span v-if="cartCount > 0" class="cart-badge">{{ cartCount }}</span>
          </router-link>

          <template v-if="isLoggedIn">
            <el-dropdown>
              <span class="action-btn">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                  <circle cx="12" cy="7" r="4"></circle>
                </svg>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="$router.push('/orders')">我的订单</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/profile')">个人中心</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <router-link v-else to="/login" class="action-btn">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
              <circle cx="12" cy="7" r="4"></circle>
            </svg>
          </router-link>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const searchQuery = ref('')
const cartUpdateKey = ref(0)

const isLoggedIn = computed(() => !!localStorage.getItem('token'))
const cartCount = computed(() => {
  // Use key to force re-computation
  cartUpdateKey.value
  const cart = JSON.parse(localStorage.getItem('cart') || '[]')
  return cart.reduce((sum, item) => sum + item.quantity, 0)
})

let cartUpdateInterval = null

onMounted(() => {
  // Poll for cart updates every 1 second
  cartUpdateInterval = setInterval(() => {
    cartUpdateKey.value++
  }, 1000)
})

onUnmounted(() => {
  if (cartUpdateInterval) {
    clearInterval(cartUpdateInterval)
  }
})

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    router.push(`/?search=${encodeURIComponent(searchQuery.value.trim())}`)
  }
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/')
  location.reload()
}
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: var(--nav-height);
  background: var(--color-card);
  border-bottom: 1px solid var(--color-divider);
  z-index: 100;
}

.nav-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: var(--nav-height);
}

.logo {
  font-size: 22px;
  font-weight: var(--font-bold);
  color: var(--color-primary);
  letter-spacing: -0.5px;
}

.search-bar {
  flex: 1;
  max-width: 400px;
  margin: 0 var(--spacing-8);
  position: relative;
}

.search-bar input {
  width: 100%;
  height: 40px;
  border-radius: var(--radius-full);
  border: 1px solid var(--color-border);
  background: #F5EDED;
  padding: 0 var(--spacing-10) 0 var(--spacing-5);
  font-size: var(--text-sm);
  transition: all 0.2s ease;
}

.search-bar input:focus {
  outline: none;
  border-color: var(--color-primary);
  background: var(--color-card);
}

.search-btn {
  position: absolute;
  right: var(--spacing-3);
  top: 50%;
  transform: translateY(-50%);
  color: var(--color-text-secondary);
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: var(--spacing-4);
}

.action-btn {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: var(--radius-full);
  color: var(--color-text);
  transition: all 0.2s ease;
}

.action-btn:hover {
  background: var(--color-primary-light);
  color: var(--color-primary);
}

.cart-badge {
  position: absolute;
  top: 2px;
  right: 2px;
  min-width: 18px;
  height: 18px;
  padding: 0 5px;
  background: var(--color-danger);
  color: white;
  font-size: 11px;
  font-weight: var(--font-medium);
  border-radius: var(--radius-full);
  display: flex;
  align-items: center;
  justify-content: center;
}

@media (max-width: 768px) {
  .search-bar {
    display: none;
  }
}
</style>