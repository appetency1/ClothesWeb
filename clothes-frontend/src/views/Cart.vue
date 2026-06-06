<template>
  <div class="cart-page">
    <div class="container">
      <h1 class="page-title">购物车</h1>

      <div v-if="cartItems.length === 0" class="empty-cart">
        <div class="empty-icon">
          <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1">
            <path d="M6 6h15l-1.5 9h-12z"></path>
            <path d="M6 6l-1-4H3"></path>
            <circle cx="9" cy="19" r="1"></circle>
            <circle cx="18" cy="19" r="1"></circle>
          </svg>
        </div>
        <h2>购物车是空的</h2>
        <p>去挑选心仪的商品吧</p>
        <router-link to="/" class="btn btn-primary">去逛逛</router-link>
      </div>

      <div v-else class="cart-content">
        <div class="cart-items">
          <div
            v-for="item in cartItems"
            :key="item.id"
            class="cart-item"
          >
            <div class="item-image">
              <img :src="item.clothesImageUrl" :alt="item.clothesName" />
            </div>
            <div class="item-info">
              <h3 class="item-name">{{ item.clothesName }}</h3>
              <p class="item-size">尺码: {{ item.clothesSize }}</p>
              <p class="item-price">¥ {{ item.clothesPrice.toFixed(2) }}</p>
            </div>
            <div class="item-actions">
              <div class="quantity-control">
                <button class="qty-btn" @click="updateQuantity(item, -1)">-</button>
                <span class="qty-value">{{ item.quantity }}</span>
                <button class="qty-btn" @click="updateQuantity(item, 1)">+</button>
              </div>
              <p class="item-total">¥ {{ (item.clothesPrice * item.quantity).toFixed(2) }}</p>
              <button class="btn-text remove-btn" @click="removeItem(item)">删除</button>
            </div>
          </div>
        </div>

        <div class="cart-summary">
          <h3 class="summary-title">订单摘要</h3>
          <div class="summary-row">
            <span>商品小计</span>
            <span>¥ {{ subtotal.toFixed(2) }}</span>
          </div>
          <div class="summary-row">
            <span>运费</span>
            <span>¥ 0.00</span>
          </div>
          <div class="summary-row summary-total">
            <span>合计</span>
            <span>¥ {{ total.toFixed(2) }}</span>
          </div>
          <button class="btn btn-primary checkout-btn" @click="handleCheckout">
            去结算
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCartList, updateCart, deleteCart } from '../api/cart.js'
import { createOrder } from '../api/order.js'

const router = useRouter()

const cartItems = ref([])
const loading = ref(false)

const subtotal = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.clothesPrice * item.quantity, 0)
})

const total = computed(() => subtotal.value)

const loadCart = async () => {
  const token = localStorage.getItem('token')
  if (!token) {
    cartItems.value = []
    return
  }
  
  loading.value = true
  try {
    const data = await getCartList()
    cartItems.value = data || []
  } catch (error) {
    console.error('Failed to load cart:', error)
    ElMessage.error('加载购物车失败')
  } finally {
    loading.value = false
  }
}

const updateQuantity = async (item, delta) => {
  const newQuantity = item.quantity + delta
  if (newQuantity < 1) return

  try {
    await updateCart({
      cartId: item.id,
      quantity: newQuantity
    })
    
    const index = cartItems.value.findIndex(
      i => i.id === item.id
    )
    if (index !== -1) {
      cartItems.value[index] = { ...cartItems.value[index], quantity: newQuantity }
    }
    ElMessage.success('更新成功')
  } catch (error) {
    ElMessage.error('更新失败')
  }
}

const removeItem = async (item) => {
  try {
    await ElMessageBox.confirm('确定要删除这个商品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteCart(item.id)
    
    cartItems.value = cartItems.value.filter(
      i => i.id !== item.id
    )
    ElMessage.success('已删除')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleCheckout = async () => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  if (cartItems.value.length === 0) {
    ElMessage.warning('购物车为空')
    return
  }
  
  try {
    // Prepare order data
    const clothesDetails = cartItems.value.map(item => ({
      clothesId: item.clothesId,
      clothesName: item.clothesName,
      clothesSize: item.clothesSize,
      clothesPrice: item.clothesPrice,
      quantity: item.quantity
    }))
    
    const orderData = {
      totalAmount: total.value,
      clothesDetails: JSON.stringify(clothesDetails)
    }
    
    await createOrder(orderData)
    ElMessage.success('订单创建成功')
    cartItems.value = []
    router.push('/orders')
  } catch (error) {
    ElMessage.error(error.message || '结算失败')
  }
}

onMounted(() => {
  loadCart()
})
</script>

<style scoped>
.cart-page {
  padding: var(--spacing-8) 0;
}

.page-title {
  font-size: var(--text-2xl);
  font-weight: var(--font-medium);
  margin-bottom: var(--spacing-8);
}

.empty-cart {
  text-align: center;
  padding: var(--spacing-16) 0;
}

.empty-icon {
  color: var(--color-border);
  margin-bottom: var(--spacing-6);
}

.empty-cart h2 {
  font-size: var(--text-xl);
  color: var(--color-text);
  margin-bottom: var(--spacing-2);
}

.empty-cart p {
  color: var(--color-text-secondary);
  margin-bottom: var(--spacing-6);
}

.cart-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: var(--spacing-8);
}

.cart-items {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-4);
}

.cart-item {
  display: grid;
  grid-template-columns: 100px 1fr auto;
  gap: var(--spacing-5);
  padding: var(--spacing-5);
  background: var(--color-card);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  align-items: center;
}

.item-image {
  width: 100px;
  height: 100px;
  border-radius: var(--radius-md);
  overflow: hidden;
  background: linear-gradient(135deg, #E8E6E1 0%, #D4D1CB 100%);
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-name {
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  margin-bottom: var(--spacing-1);
}

.item-size {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  margin-bottom: var(--spacing-2);
}

.item-price {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-accent);
}

.item-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: var(--spacing-3);
}

.quantity-control {
  display: flex;
  align-items: center;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  overflow: hidden;
}

.qty-btn {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--text-lg);
  color: var(--color-text);
  transition: all 0.2s ease;
}

.qty-btn:hover {
  background: var(--color-primary-light);
}

.qty-value {
  width: 40px;
  text-align: center;
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
}

.item-total {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text);
}

.remove-btn {
  color: var(--color-text-secondary);
}

.remove-btn:hover {
  color: var(--color-danger);
}

.cart-summary {
  position: sticky;
  top: calc(var(--nav-height) + var(--spacing-8));
  background: var(--color-card);
  padding: var(--spacing-6);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  height: fit-content;
}

.summary-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  margin-bottom: var(--spacing-6);
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: var(--spacing-4);
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
}

.summary-total {
  border-top: 1px solid var(--color-divider);
  padding-top: var(--spacing-4);
  margin-top: var(--spacing-4);
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--color-text);
}

.checkout-btn {
  width: 100%;
  margin-top: var(--spacing-6);
}

@media (max-width: 1024px) {
  .cart-content {
    grid-template-columns: 1fr;
  }

  .cart-summary {
    position: static;
  }
}

@media (max-width: 768px) {
  .cart-item {
    grid-template-columns: 80px 1fr;
    grid-template-rows: auto auto;
  }

  .item-actions {
    grid-column: 1 / -1;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }
}
</style>