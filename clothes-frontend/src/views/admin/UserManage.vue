<template>
  <div class="user-manage">
    <!-- Search and Actions -->
    <div class="toolbar">
      <div class="search-group">
        <input
          v-model="searchName"
          type="text"
          placeholder="按姓名搜索..."
          @input="handleSearch"
        />
        <input
          v-model="searchPhone"
          type="text"
          placeholder="按手机号搜索..."
          @input="handleSearch"
        />
        <button class="btn-search" @click="handleSearch">🔍 搜索</button>
      </div>
      <button class="btn-add" @click="showAddDialog = true">
        <span>+</span>
        <span>添加用户</span>
      </button>
    </div>

    <!-- User Table -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>手机号</th>
            <th>地址</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in userList" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.phone }}</td>
            <td>{{ user.address || '-' }}</td>
            <td>{{ formatDate(user.createTime) }}</td>
            <td>
              <div class="actions">
                <button class="btn-edit" @click="editUser(user)">编辑</button>
                <button class="btn-delete" @click="deleteUser(user)">删除</button>
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
        <h2>{{ showEditDialog ? '编辑用户' : '添加用户' }}</h2>
        <form @submit.prevent="saveUser">
          <div class="form-group">
            <label>用户名</label>
            <input v-model="form.username" type="text" required />
          </div>
          <div class="form-group">
            <label>手机号</label>
            <input v-model="form.phone" type="text" required maxlength="11" />
          </div>
          <div class="form-group">
            <label>密码</label>
            <input v-model="form.password" type="password" :required="!showEditDialog" />
            <small v-if="showEditDialog">留空则不修改密码</small>
          </div>
          <div class="form-group">
            <label>地址</label>
            <textarea v-model="form.address" rows="2"></textarea>
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
import { getAdminUserList, addAdminUser, updateAdminUser, deleteAdminUser } from '../../api/user.js'

const userList = ref([])
const searchName = ref('')
const searchPhone = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const totalCount = ref(0)
const showAddDialog = ref(false)
const showEditDialog = ref(false)

const form = ref({
  id: null,
  username: '',
  phone: '',
  password: '',
  address: '',
  role: 2
})

const totalPages = computed(() => Math.ceil(totalCount.value / pageSize.value))

onMounted(() => {
  loadUsers()
})

const loadUsers = async () => {
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      ...(searchName.value && { username: searchName.value }),
      ...(searchPhone.value && { phone: searchPhone.value })
    }
    
    const result = await getAdminUserList(params)
    userList.value = result.list || []
    totalCount.value = result.total || 0
  } catch (error) {
    ElMessage.error('加载用户列表失败')
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

const handleSearch = () => {
  currentPage.value = 1
  loadUsers()
}

const editUser = (user) => {
  form.value = { ...user, password: '' }
  showEditDialog.value = true
}

const deleteUser = async (user) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户 "${user.username}" 吗？`,
      '确认删除',
      { type: 'warning' }
    )
    await deleteAdminUser(user.id)
    ElMessage.success('删除成功')
    loadUsers()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  form.value = {
    id: null,
    username: '',
    phone: '',
    password: '',
    address: '',
    role: 2
  }
}

const saveUser = async () => {
  // Validate phone number
  if (!/^1\d{10}$/.test(form.value.phone)) {
    ElMessage.error('手机号格式不正确')
    return
  }

  try {
    if (showEditDialog.value) {
      const data = { ...form.value }
      if (!data.password) {
        delete data.password
      }
      await updateAdminUser(data)
      ElMessage.success('更新成功')
    } else {
      await addAdminUser(form.value)
      ElMessage.success('添加成功')
    }
    closeDialog()
    loadUsers()
  } catch (error) {
    ElMessage.error(showEditDialog.value ? '更新失败' : '添加失败')
  }
}
</script>

<style scoped>
.user-manage {
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

.search-group {
  display: flex;
  gap: 12px;
  align-items: center;
}

.search-group input {
  width: 200px;
  height: 36px;
  padding: 0 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
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
  max-width: 500px;
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