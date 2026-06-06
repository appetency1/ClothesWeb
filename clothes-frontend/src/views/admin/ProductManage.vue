<template>
  <div class="product-manage">
    <!-- Search and Filter -->
    <div class="toolbar">
      <div class="filter-group">
        <select v-model="filterType" @change="handleFilter">
          <option value="">全部分类</option>
          <option v-for="type in productTypes" :key="type.id" :value="type.id">
            {{ type.name }}
          </option>
        </select>
        <select v-model="filterStatus" @change="handleFilter">
          <option value="">全部状态</option>
          <option value="1">上架</option>
          <option value="0">下架</option>
        </select>
        <input
          v-model="searchKeyword"
          type="text"
          placeholder="搜索商品名称..."
          @input="handleSearch"
        />
        <button class="btn-search" @click="handleSearch">🔍 搜索</button>
      </div>
      <button class="btn-add" @click="showAddDialog = true">
        <span>+</span>
        <span>上架商品</span>
      </button>
    </div>

    <!-- Product Table -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>图片</th>
            <th>商品名称</th>
            <th>分类</th>
            <th>风格</th>
            <th>价格</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in productList" :key="product.id">
            <td>{{ product.id }}</td>
            <td>
              <img 
                :src="product.imageUrl || '/default-product.png'" 
                :alt="product.name" 
                class="product-thumb" 
              />
            </td>
            <td>{{ product.name }}</td>
            <td>{{ getTypeName(product.typeId) }}</td>
            <td>{{ product.style || '-' }}</td>
            <td class="price">¥{{ product.price }}</td>
            <td>
              <span :class="['status-tag', product.status === 1 ? 'active' : 'inactive']">
                {{ product.status === 1 ? '上架' : '下架' }}
              </span>
            </td>
            <td>
              <div class="actions">
                <button class="btn-edit" @click="editProduct(product)">编辑</button>
                <button class="btn-delete" @click="deleteProduct(product)">删除</button>
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

    <!-- Add/Edit Dialog -->
    <div v-if="showAddDialog || showEditDialog" class="dialog-overlay" @click.self="closeDialog">
      <div class="dialog">
        <h2>{{ showEditDialog ? '编辑商品' : '上架商品' }}</h2>
        <form @submit.prevent="saveProduct">
          <div class="form-group">
            <label>商品名称</label>
            <input v-model="form.name" type="text" required />
          </div>
          <div class="form-group">
            <label>分类</label>
            <select v-model="form.typeId" required @change="handleTypeChange">
              <option v-for="type in productTypes" :key="type.id" :value="type.id">
                {{ type.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>风格</label>
            <input v-model="form.style" type="text" placeholder="如：简约、优雅、休闲" />
          </div>
          <div class="form-group">
            <label>价格</label>
            <input v-model.number="form.price" type="number" step="0.01" required />
          </div>
          <div class="form-group">
            <label>商品图片</label>
            <div class="image-upload">
              <input 
                type="file" 
                accept=".jpg,.png" 
                @change="handleImageUpload"
                ref="fileInput"
              />
              <div v-if="imagePreview" class="image-preview">
                <img :src="imagePreview" alt="预览" />
              </div>
            </div>
            <small>支持 .jpg, .png 格式</small>
          </div>
          <div class="form-group">
            <label>描述</label>
            <textarea v-model="form.description" rows="3"></textarea>
          </div>
          <div class="form-group">
            <label>状态</label>
            <select v-model="form.status">
              <option :value="1">上架</option>
              <option :value="0">下架</option>
            </select>
          </div>
          <div class="dialog-actions">
            <button type="button" class="btn-cancel" @click="closeDialog">取消</button>
            <button type="submit" class="btn-save">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminClothesList, addClothes, updateClothes, deleteClothes, uploadImage } from '../../api/clothes.js'
import { getTypeList } from '../../api/type.js'

const productList = ref([])
const productTypes = ref([])
const searchKeyword = ref('')
const filterType = ref('')
const filterStatus = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const totalCount = ref(0)
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const imagePreview = ref('')
const fileInput = ref(null)

const form = ref({
  id: null,
  name: '',
  typeId: '',
  style: '',
  price: 0,
  imageUrl: '',
  description: '',
  status: 1
})

const totalPages = computed(() => Math.ceil(totalCount.value / pageSize.value))

onMounted(() => {
  loadProductTypes()
  loadProducts()
})

const loadProductTypes = async () => {
  try {
    productTypes.value = await getTypeList()
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

const loadProducts = async () => {
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      ...(searchKeyword.value && { keyword: searchKeyword.value }),
      ...(filterType.value && { typeId: filterType.value }),
      ...(filterStatus.value !== '' && { status: filterStatus.value })
    }
    
    const result = await getAdminClothesList(params)
    productList.value = result.list || []
    totalCount.value = result.total || 0
  } catch (error) {
    ElMessage.error('加载商品列表失败')
  }
}

const getTypeName = (typeId) => {
  const type = productTypes.value.find(t => t.id === typeId)
  return type ? type.name : '-'
}

const handleSearch = () => {
  currentPage.value = 1
  loadProducts()
}

const handleFilter = () => {
  currentPage.value = 1
  loadProducts()
}

const handleTypeChange = () => {
  // 可以在这里加载对应分类的尺码
}

const handleImageUpload = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  // 验证文件类型
  const allowedTypes = ['image/jpeg', 'image/png']
  if (!allowedTypes.includes(file.type)) {
    ElMessage.error('只支持 .jpg 和 .png 格式')
    return
  }

  // 预览图片
  const reader = new FileReader()
  reader.onload = (e) => {
    imagePreview.value = e.target.result
  }
  reader.readAsDataURL(file)

  // 上传图片到服务器
  try {
    ElMessage.info('正在上传图片...')
    const result = await uploadImage(file)
    form.value.imageUrl = result.url
    ElMessage.success('图片上传成功')
  } catch (error) {
    ElMessage.error('图片上传失败: ' + error.message)
  }
}

const editProduct = (product) => {
  form.value = { ...product }
  imagePreview.value = product.imageUrl || ''
  showEditDialog.value = true
}

const deleteProduct = async (product) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除商品 "${product.name}" 吗？`,
      '确认删除',
      { type: 'warning' }
    )
    await deleteClothes(product.id)
    ElMessage.success('删除成功')
    loadProducts()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  imagePreview.value = ''
  if (fileInput.value) {
    fileInput.value.value = ''
  }
  form.value = {
    id: null,
    name: '',
    typeId: '',
    style: '',
    price: 0,
    imageUrl: '',
    description: '',
    status: 1
  }
}

const saveProduct = async () => {
  try {
    if (showEditDialog.value) {
      await updateClothes(form.value)
      ElMessage.success('更新成功')
    } else {
      await addClothes(form.value)
      ElMessage.success('上架成功')
    }
    closeDialog()
    loadProducts()
  } catch (error) {
    ElMessage.error(showEditDialog.value ? '更新失败' : '上架失败')
  }
}
</script>

<style scoped>
.product-manage {
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

.btn-add {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 0 16px;
  height: 36px;
  background: #C49A6C;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
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

.product-thumb {
  width: 60px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
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

.status-tag.active {
  background: #e8f5e8;
  color: #4caf50;
}

.status-tag.inactive {
  background: #ffebee;
  color: #f44336;
}

.actions {
  display: flex;
  gap: 8px;
}

.btn-edit,
.btn-delete {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-edit {
  background: #2196f3;
  color: white;
}

.btn-delete {
  background: #f44336;
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

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  box-sizing: border-box;
}

.form-group small {
  color: #999;
  font-size: 12px;
}

.image-upload {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.image-preview {
  width: 120px;
  height: 160px;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #ddd;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}

.btn-cancel,
.btn-save {
  padding: 10px 24px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
}

.btn-cancel {
  background: #f5f5f5;
  color: #666;
}

.btn-save {
  background: #C49A6C;
  color: white;
}
</style>