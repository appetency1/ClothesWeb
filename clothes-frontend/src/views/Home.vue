<template>
  <div class="home">
    <!-- Category Tabs -->
    <div class="category-section">
      <div class="container">
        <div class="category-tabs">
          <button
            v-for="type in types"
            :key="type.id"
            :class="['tab-btn', { active: currentType === type.id }]"
            @click="selectType(type.id)"
          >
            {{ type.name }}
          </button>
        </div>
      </div>
    </div>

    <!-- Products Grid -->
    <div class="products-section">
      <div class="container">
        <div v-if="loading" class="loading-state">
          <el-skeleton :rows="3" animated />
        </div>
        <div v-else-if="products.length === 0" class="empty-state">
          <div class="empty-icon">
            <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1">
              <path d="M3 3h18v18H3z"></path>
              <path d="M9 3v18"></path>
              <path d="M15 3v18"></path>
              <path d="M3 9h18"></path>
              <path d="M3 15h18"></path>
            </svg>
          </div>
          <h3>暂无商品</h3>
          <p>去看看其他分类吧</p>
        </div>
        <div v-else class="products-grid">
          <div
            v-for="product in products"
            :key="product.id"
            class="product-card"
            @click="goToDetail(product.id)"
          >
            <div class="product-image">
              <img :src="product.image || '/placeholder-clothes.png'" :alt="product.name" />
              <span class="type-tag">{{ product.typeName }}</span>
            </div>
            <div class="product-info">
              <h3 class="product-name">{{ product.name }}</h3>
              <p class="product-price">¥ {{ product.price.toFixed(2) }}</p>
            </div>
          </div>
        </div>

        <!-- Pagination -->
        <div v-if="products.length > 0" class="pagination">
          <el-pagination
            v-model:current-page="page"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            @current-change="handlePageChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getClothesList } from '../api/clothes.js'
import { getTypeList } from '../api/type.js'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const types = ref([{ id: 0, name: '全部' }])

const currentType = ref(0)
const products = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(12)
const total = ref(0)

const fetchProducts = async () => {
  loading.value = true
  try {
    const params = {
      page: page.value,
      pageSize: pageSize.value,
      status: 1, // Only show active products
      ...(currentType.value !== 0 && { typeId: currentType.value }),
      ...(route.query.search && { keyword: route.query.search })
    }
    
    const result = await getClothesList(params)
    products.value = result.list || []
    total.value = result.total || 0
  } catch (error) {
    console.error('Failed to fetch products:', error)
    ElMessage.error('加载商品失败')
  } finally {
    loading.value = false
  }
}

const fetchTypes = async () => {
  try {
    const typeList = await getTypeList()
    types.value = [{ id: 0, name: '全部' }, ...typeList]
  } catch (error) {
    console.error('Failed to fetch types:', error)
  }
}

const selectType = (typeId) => {
  currentType.value = typeId
  page.value = 1
  fetchProducts()
}

const handlePageChange = (newPage) => {
  page.value = newPage
  fetchProducts()
}

const goToDetail = (id) => {
  router.push(`/product/${id}`)
}

watch(() => route.query.search, () => {
  page.value = 1
  fetchProducts()
})

onMounted(() => {
  fetchTypes()
  fetchProducts()
})
</script>

<style scoped>
.home {
  padding: var(--spacing-8) 0;
}

.category-section {
  margin-bottom: var(--spacing-8);
}

.category-tabs {
  display: flex;
  gap: var(--spacing-2);
  flex-wrap: wrap;
  justify-content: center;
}

.tab-btn {
  padding: var(--spacing-2) var(--spacing-5);
  border-radius: var(--radius-full);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-secondary);
  background: transparent;
  border: 1px solid transparent;
  transition: all 0.2s ease;
}

.tab-btn:hover {
  color: var(--color-primary);
  background: var(--color-primary-light);
}

.tab-btn.active {
  color: var(--color-primary);
  background: var(--color-primary-light);
  border-color: var(--color-primary);
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--spacing-6);
}

.product-card {
  background: var(--color-card);
  border-radius: var(--radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  transition: all 0.25s ease;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
}

.product-image {
  position: relative;
  aspect-ratio: 3/4;
  background: linear-gradient(135deg, #E8E6E1 0%, #D4D1CB 100%);
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.type-tag {
  position: absolute;
  top: var(--spacing-3);
  left: var(--spacing-3);
  padding: 4px 10px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: var(--radius-sm);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
  color: var(--color-primary);
}

.product-info {
  padding: var(--spacing-4);
}

.product-name {
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  color: var(--color-text);
  margin-bottom: var(--spacing-2);
  line-height: var(--leading-tight);
}

.product-price {
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  color: var(--color-accent);
}

.loading-state {
  padding: var(--spacing-12) 0;
}

.empty-state {
  text-align: center;
  padding: var(--spacing-12) 0;
}

.empty-icon {
  color: var(--color-border);
  margin-bottom: var(--spacing-4);
}

.empty-state h3 {
  font-size: var(--text-lg);
  color: var(--color-text-secondary);
  margin-bottom: var(--spacing-2);
}

.empty-state p {
  color: var(--color-text-placeholder);
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: var(--spacing-10);
}

:deep(.el-pagination) {
  --el-pagination-button-bg-color: var(--color-card);
  --el-pagination-hover-color: var(--color-primary);
}

:deep(.el-pagination .el-pager li.active) {
  background: var(--color-primary);
  color: white;
}

@media (max-width: 1024px) {
  .products-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .products-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: var(--spacing-4);
  }
}
</style>