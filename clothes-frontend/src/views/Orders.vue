<template>
  <div class="orders-page">
    <div class="container">
      <h1 class="page-title">我的订单</h1>
      
      <!-- Status Filter -->
      <div class="filter-tabs">
        <button
          v-for="tab in statusTabs"
          :key="tab.value"
          :class="['filter-tab', { active: currentStatus === tab.value }]"
          @click="selectStatus(tab.value)"
        >
          {{ tab.label }}
        </button>
      </div>
      
      <div v-if="loading" class="loading-state">
        <el-skeleton :rows="3" animated />
      </div>
      
      <div v-else-if="orders.length === 0" class="empty-state">
        <h3>暂无订单</h3>
        <p>去挑选心仪的商品吧</p>
        <router-link to="/" class="btn btn-primary">去逛逛</router-link>
      </div>
      
      <div v-else class="orders-list">
        <div v-for="order in orders" :key="order.id" class="order-card">
          <div class="order-header">
            <span class="order-no">订单号: {{ order.orderNo }}</span>
            <span class="order-date">{{ formatDate(order.createTime) }}</span>
            <span :class="['status-tag', `status-${order.status}`]">{{ getStatusText(order.status) }}</span>
          </div>
          <div class="order-items">
            <div v-for="(item, index) in parseClothesDetails(order.clothesDetails)" :key="index" class="order-item">
              <span>{{ item.name }}</span>
              <span class="item-count">× {{ item.quantity }}</span>
            </div>
          </div>
          <div class="order-footer">
            <span class="order-total">合计: ¥ {{ order.totalAmount.toFixed(2) }}</span>
            <div class="order-actions">
              <button v-if="order.status === 0" class="btn btn-primary btn-sm" @click="payOrder(order)">立即付款</button>
              <button v-if="order.status === 2" class="btn btn-primary btn-sm" @click="confirmOrder(order)">确认收货</button>
              <button v-if="order.status === 0 || order.status === 3" class="btn btn-outline btn-sm" @click="deleteOrder(order)">删除订单</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderList, payOrder as apiPayOrder, confirmOrder as apiConfirmOrder, deleteOrder as apiDeleteOrder } from '../api/order.js'

const orders = ref([])
const loading = ref(false)
const currentStatus = ref('')

const statusTabs = [
  { label: '全部', value: '' },
  { label: '待付款', value: 0 },
  { label: '待发货', value: 1 },
  { label: '已发货', value: 2 },
  { label: '已收货', value: 3 }
]

const loadOrders = async () => {
  loading.value = true
  try {
    const params = {}
    if (currentStatus.value !== '') {
      params.status = currentStatus.value
    }
    
    const data = await getOrderList(params)
    orders.value = data || []
  } catch (error) {
    console.error('Failed to load orders:', error)
    ElMessage.error('加载订单失败')
  } finally {
    loading.value = false
  }
}

const getStatusText = (status) => {
  const statusMap = {
    0: '待付款',
    1: '待发货',
    2: '已发货',
    3: '已收货'
  }
  return statusMap[status] || '未知'
}

const parseClothesDetails = (details) => {
  try {
    return JSON.parse(details) || []
  } catch {
    return []
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

const selectStatus = (status) => {
  currentStatus.value = status
  loadOrders()
}

const payOrder = async (order) => {
  try {
    await apiPayOrder(order.id)
    ElMessage.success('支付成功')
    loadOrders()
  } catch (error) {
    ElMessage.error('支付失败')
  }
}

const confirmOrder = async (order) => {
  try {
    await apiConfirmOrder(order.id)
    ElMessage.success('确认收货成功')
    loadOrders()
  } catch (error) {
    ElMessage.error('确认收货失败')
  }
}

const deleteOrder = async (order) => {
  try {
    await ElMessageBox.confirm('确定要删除这个订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await apiDeleteOrder(order.id)
    ElMessage.success('删除成功')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.orders-page {
  padding: var(--spacing-8) 0;
}

.page-title {
  font-size: var(--text-2xl);
  font-weight: var(--font-medium);
  margin-bottom: var(--spacing-8);
}

.filter-tabs {
  display: flex;
  gap: var(--spacing-2);
  margin-bottom: var(--spacing-6);
  flex-wrap: wrap;
}

.filter-tab {
  padding: var(--spacing-2) var(--spacing-4);
  border-radius: var(--radius-full);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-secondary);
  background: transparent;
  border: 1px solid transparent;
  transition: all 0.2s ease;
}

.filter-tab:hover {
  color: var(--color-primary);
  background: var(--color-primary-light);
}

.filter-tab.active {
  color: var(--color-primary);
  background: var(--color-primary-light);
  border-color: var(--color-primary);
}

.loading-state {
  padding: var(--spacing-12) 0;
}

.empty-state {
  text-align: center;
  padding: var(--spacing-16) 0;
}

.empty-state h3 {
  font-size: var(--text-lg);
  color: var(--color-text-secondary);
  margin-bottom: var(--spacing-2);
}

.empty-state p {
  color: var(--color-text-placeholder);
  margin-bottom: var(--spacing-6);
}

.orders-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-4);
}

.order-card {
  background: var(--color-card);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  padding: var(--spacing-6);
}

.order-header {
  display: flex;
  align-items: center;
  gap: var(--spacing-4);
  margin-bottom: var(--spacing-4);
  padding-bottom: var(--spacing-4);
  border-bottom: 1px solid var(--color-divider);
  flex-wrap: wrap;
}

.order-no {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
}

.order-date {
  font-size: var(--text-sm);
  color: var(--color-text-placeholder);
}

.order-items {
  margin-bottom: var(--spacing-4);
}

.order-item {
  display: flex;
  justify-content: space-between;
  font-size: var(--text-base);
  padding: var(--spacing-2) 0;
}

.item-count {
  color: var(--color-text-secondary);
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: var(--spacing-4);
  border-top: 1px solid var(--color-divider);
  flex-wrap: wrap;
  gap: var(--spacing-4);
}

.order-total {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
}

.order-actions {
  display: flex;
  gap: var(--spacing-3);
}

.status-tag {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.status-0 {
  background: #FFF5E6;
  color: #B8844A;
}

.status-1 {
  background: #EDF2F8;
  color: #5A7A9A;
}

.status-2 {
  background: #EFF0F8;
  color: #6E7EAA;
}

.status-3 {
  background: #EDF5F0;
  color: #4A8A72;
}
</style>