<template>
  <div class="order-manage">
    <!-- Filter and Search -->
    <div class="toolbar">
      <div class="filter-group">
        <select v-model="statusFilter" @change="handleFilter">
          <option value="">全部状态</option>
          <option value="0">待付款</option>
          <option value="1">待发货</option>
          <option value="2">已发货</option>
          <option value="3">已收货</option>
        </select>
        <input
          v-model="usernameFilter"
          type="text"
          placeholder="按用户姓名搜索..."
          @input="handleSearch"
        />
        <button class="btn-search" @click="handleSearch">🔍 搜索</button>
      </div>
    </div>

    <!-- Order Table -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>订单号</th>
            <th>用户</th>
            <th>商品详情</th>
            <th>总金额</th>
            <th>状态</th>
            <th>收货地址</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orderList" :key="order.id">
            <td>{{ order.orderNo }}</td>
            <td>{{ order.username }}</td>
            <td>
              <div class="order-items">
                <div v-for="(item, index) in parseClothesDetails(order.clothesDetails)" :key="index" class="order-item">
                  <span class="item-name">{{ item.name }}</span>
                  <span class="item-info">x{{ item.quantity }} | 尺码: {{ item.size }}</span>
                </div>
              </div>
            </td>
            <td class="price">¥{{ order.totalAmount }}</td>
            <td>
              <span :class="['status-tag', `status-${order.status}`]">
                {{ getStatusText(order.status) }}
              </span>
            </td>
            <td>{{ order.address }}</td>
            <td>{{ formatDate(order.createTime) }}</td>
            <td>
              <div class="actions">
                <button 
                  v-if="order.status === 1" 
                  class="btn-ship" 
                  @click="shipOrder(order)"
                >
                  发货
                </button>
                <button class="btn-detail" @click="viewDetail(order)">详情</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="pagination">
      <button :disabled="currentPage === 1" @click="currentPage--">上一页</button>
      <span class="page-info">第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</span>
      <button :disabled="currentPage >= totalPages" @click="currentPage++">下一页</button>
    </div>

    <!-- Order Detail Dialog -->
    <div v-if="showDetailDialog" class="dialog-overlay" @click.self="closeDetail">
      <div class="dialog">
        <h2>订单详情</h2>
        <div class="detail-content" v-if="selectedOrder">
          <div class="detail-row">
            <label>订单号：</label>
            <span>{{ selectedOrder.orderNo }}</span>
          </div>
          <div class="detail-row">
            <label>用户：</label>
            <span>{{ selectedOrder.username }}</span>
          </div>
          <div class="detail-row">
            <label>状态：</label>
            <span :class="['status-tag', `status-${selectedOrder.status}`]">
              {{ getStatusText(selectedOrder.status) }}
            </span>
          </div>
          <div class="detail-row">
            <label>收货地址：</label>
            <span>{{ selectedOrder.address }}</span>
          </div>
          <div class="detail-row">
            <label>创建时间：</label>
            <span>{{ formatDate(selectedOrder.createTime) }}</span>
          </div>
          <div class="detail-section">
            <h3>商品清单</h3>
            <div v-for="(item, index) in parseClothesDetails(selectedOrder.clothesDetails)" :key="index" class="detail-item">
              <span class="item-name">{{ item.name }}</span>
              <span>尺码：{{ item.size }}</span>
              <span>数量：{{ item.quantity }}</span>
              <span class="item-price">¥{{ item.price }}</span>
            </div>
          </div>
          <div class="detail-total">
            <strong>总计：¥{{ selectedOrder.totalAmount }}</strong>
          </div>
        </div>
        <div class="dialog-actions">
          <button class="btn-cancel" @click="closeDetail">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAdminOrderList, shipOrder as apiShipOrder } from '../../api/order.js'

const orderList = ref([])
const statusFilter = ref('')
const usernameFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const totalCount = ref(0)
const showDetailDialog = ref(false)
const selectedOrder = ref(null)

const totalPages = computed(() => Math.ceil(totalCount.value / pageSize.value))

onMounted(() => {
  loadOrders()
})

const loadOrders = async () => {
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      ...(statusFilter.value !== '' && { status: statusFilter.value }),
      ...(usernameFilter.value && { username: usernameFilter.value })
    }
    
    const result = await getAdminOrderList(params)
    orderList.value = result.list || []
    totalCount.value = result.total || 0
  } catch (error) {
    ElMessage.error('加载订单列表失败')
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

const handleSearch = () => {
  currentPage.value = 1
  loadOrders()
}

const handleFilter = () => {
  currentPage.value = 1
  loadOrders()
}

const shipOrder = async (order) => {
  try {
    await apiShipOrder(order.id)
    ElMessage.success('发货成功')
    loadOrders()
  } catch (error) {
    ElMessage.error('发货失败')
  }
}

const viewDetail = (order) => {
  selectedOrder.value = order
  showDetailDialog.value = true
}

const closeDetail = () => {
  showDetailDialog.value = false
  selectedOrder.value = null
}
</script>

<style scoped>
.order-manage {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.filter-group {
  display: flex;
  gap: 12px;
  align-items: center;
}

.filter-group select,
.filter-group input {
  height: 36px;
  padding: 0 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.filter-group input {
  width: 200px;
}

.btn-search {
  height: 36px;
  padding: 0 16px;
  background: #C49A6C;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.table-container {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 14px 16px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.data-table th {
  background: #f8f8f8;
  font-weight: 600;
  color: #333;
  font-size: 14px;
}

.data-table tr:hover {
  background: #f9f9f9;
}

.order-items {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.order-item {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.item-name {
  font-weight: 500;
  color: #333;
}

.item-info {
  font-size: 12px;
  color: #666;
}

.price {
  font-weight: 600;
  color: #C49A6C;
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

.actions {
  display: flex;
  gap: 8px;
}

.btn-ship,
.btn-detail {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-ship {
  background: #67A58D;
  color: white;
}

.btn-detail {
  background: #8EA9C1;
  color: white;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: white;
  border-radius: 8px;
}

.pagination button {
  padding: 8px 16px;
  background: white;
  border: 1px solid #ddd;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  color: #666;
  font-size: 14px;
}

/* Dialog */
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.dialog {
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  background: white;
  border-radius: 12px;
  padding: 32px;
}

.dialog h2 {
  margin-bottom: 24px;
  font-size: 20px;
  color: #333;
}

.detail-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-row {
  display: flex;
  gap: 8px;
}

.detail-row label {
  font-weight: 500;
  color: #666;
  min-width: 80px;
}

.detail-section {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #eee;
}

.detail-section h3 {
  margin-bottom: 12px;
  font-size: 16px;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid #f5f5f5;
}

.item-price {
  font-weight: 600;
  color: #C49A6C;
}

.detail-total {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 2px solid #eee;
  text-align: right;
  font-size: 16px;
}

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 24px;
}

.btn-cancel {
  padding: 10px 24px;
  background: #f5f5f5;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
}
</style>