<template>
  <div class="product-detail">
    <div class="container">
      <!-- Breadcrumb -->
      <nav class="breadcrumb">
        <router-link to="/">首页</router-link>
        <span class="separator">></span>
        <span>{{ product.typeName }}</span>
        <span class="separator">></span>
        <span class="current">{{ product.name }}</span>
      </nav>

      <div class="detail-content">
        <!-- Product Image -->
        <div class="image-section">
          <div class="main-image">
            <img :src="product.image || '/placeholder-clothes.png'" :alt="product.name" />
          </div>
        </div>

        <!-- Product Info -->
        <div class="info-section">
          <div class="product-header">
            <h1 class="product-name">{{ product.name }}</h1>
            <div class="product-tags">
              <span v-for="tag in product.tags" :key="tag" class="tag">{{ tag }}</span>
            </div>
          </div>

          <p class="product-price">¥ {{ product.price.toFixed(2) }}</p>

          <div class="product-description">
            <p>{{ product.description }}</p>
          </div>

          <!-- Size Selector -->
          <div class="size-section">
            <h3>选择尺码</h3>
            <div class="size-options">
              <button
                v-for="size in product.sizes"
                :key="size"
                :class="['size-btn', { active: selectedSize === size }]"
                @click="selectedSize = size"
              >
                {{ size }}
              </button>
            </div>
          </div>

          <!-- Actions -->
          <div class="action-section">
            <button class="btn btn-primary btn-large" @click="addToCart">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M6 6h15l-1.5 9h-12z"></path>
                <path d="M6 6l-1-4H3"></path>
                <circle cx="9" cy="19" r="1"></circle>
                <circle cx="18" cy="19" r="1"></circle>
              </svg>
              加入购物车
            </button>
            <button class="btn btn-outline btn-large">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path>
              </svg>
              收藏
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getClothesDetail } from '../api/clothes.js'
import { getSizeList } from '../api/type.js'
import { addToCart as apiAddToCart } from '../api/cart.js'

const route = useRoute()
const router = useRouter()

const product = ref({
  id: 1,
  name: '极简纯色衬衫',
  price: 399,
  typeName: '上衣',
  description: '采用优质棉质面料，柔软亲肤，简约设计，百搭款式。经典款式，适合各种场合穿着。',
  tags: ['夏季', '简约'],
  image: '',
  sizes: ['S', 'M', 'L', 'XL']
})

const selectedSize = ref('')
const loading = ref(false)

const loadProduct = async () => {
  const productId = parseInt(route.params.id)
  if (!productId) return
  
  loading.value = true
  try {
    const data = await getClothesDetail(productId)
    product.value = data
    
    // Load sizes for this product type
    if (data.typeId) {
      const sizes = await getSizeList(data.typeId)
      product.value.sizes = sizes.map(s => s.size) || ['S', 'M', 'L', 'XL']
    }
    
    // Set default size
    if (product.value.sizes && product.value.sizes.length > 0) {
      selectedSize.value = product.value.sizes[0]
    }
  } catch (error) {
    console.error('Failed to load product:', error)
    ElMessage.error('加载商品详情失败')
  } finally {
    loading.value = false
  }
}

const addToCart = async () => {
  if (!selectedSize.value) {
    ElMessage.warning('请选择尺码')
    return
  }

  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  try {
    await apiAddToCart({
      clothId: product.value.id,
      clothSize: selectedSize.value,
      quantity: 1
    })
    ElMessage.success('已添加到购物车')
  } catch (error) {
    ElMessage.error(error.message || '添加失败')
  }
}

onMounted(() => {
  loadProduct()
})
</script>

<style scoped>
.product-detail {
  padding: var(--spacing-8) 0;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  margin-bottom: var(--spacing-8);
}

.breadcrumb a {
  color: var(--color-text-secondary);
  transition: color 0.2s ease;
}

.breadcrumb a:hover {
  color: var(--color-primary);
}

.breadcrumb .separator {
  color: var(--color-text-placeholder);
}

.breadcrumb .current {
  color: var(--color-text);
}

.detail-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--spacing-12);
  align-items: start;
}

.image-section {
  position: sticky;
  top: calc(var(--nav-height) + var(--spacing-8));
}

.main-image {
  aspect-ratio: 3/4;
  background: linear-gradient(135deg, #E8E6E1 0%, #D4D1CB 100%);
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.main-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info-section {
  padding: var(--spacing-4) 0;
}

.product-header {
  margin-bottom: var(--spacing-6);
}

.product-name {
  font-size: var(--text-2xl);
  font-weight: var(--font-medium);
  color: var(--color-text);
  margin-bottom: var(--spacing-4);
  line-height: var(--leading-tight);
}

.product-tags {
  display: flex;
  gap: var(--spacing-2);
}

.tag {
  padding: 4px 12px;
  background: var(--color-primary-light);
  color: var(--color-primary);
  border-radius: var(--radius-sm);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
}

.product-price {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--color-primary);
  margin-bottom: var(--spacing-6);
}

.product-description {
  color: var(--color-text-secondary);
  line-height: var(--leading-relaxed);
  margin-bottom: var(--spacing-8);
  padding-bottom: var(--spacing-8);
  border-bottom: 1px solid var(--color-divider);
}

.size-section {
  margin-bottom: var(--spacing-8);
}

.size-section h3 {
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  margin-bottom: var(--spacing-4);
}

.size-options {
  display: flex;
  gap: var(--spacing-3);
}

.size-btn {
  width: 44px;
  height: 44px;
  border-radius: var(--radius-md);
  border: 1px solid var(--color-border);
  background: var(--color-card);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text);
  transition: all 0.2s ease;
}

.size-btn:hover {
  border-color: var(--color-primary);
}

.size-btn.active {
  background: var(--color-primary);
  color: white;
  border-color: var(--color-primary);
}

.action-section {
  display: flex;
  gap: var(--spacing-4);
}

.btn-large {
  padding: 14px 32px;
  font-size: var(--text-base);
}

@media (max-width: 1024px) {
  .detail-content {
    grid-template-columns: 1fr;
  }

  .image-section {
    position: static;
  }
}
</style>