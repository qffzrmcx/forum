<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminApi } from '@/api'

const users = ref([])
const posts = ref([])
const comments = ref([])
const sections = ref([])
const sectionDialog = ref(false)
const sectionForm = reactive({ id: null, name: '', description: '', sort: 0, status: 1 })
const query = reactive({ keyword: '', page: 1, size: 10 })

async function loadUsers() { users.value = (await adminApi.users(query)).data.records || [] }
async function loadPosts() { posts.value = (await adminApi.posts(query)).data.records || [] }
async function loadComments() { comments.value = (await adminApi.comments(query)).data.records || [] }
async function loadSections() { sections.value = (await adminApi.sections()).data || [] }
async function refreshAll() { await Promise.all([loadUsers(), loadPosts(), loadComments(), loadSections()]) }

async function toggleUser(row) {
  if (row.role === 'ADMIN') {
    ElMessage.warning('不能禁用管理员账号')
    return
  }
  row.status === 1 ? await adminApi.disableUser(row.id) : await adminApi.enableUser(row.id)
  ElMessage.success('Done')
  loadUsers()
}

async function deletePost(id) {
  await ElMessageBox.confirm('Delete this post?')
  await adminApi.deletePost(id)
  ElMessage.success('Deleted')
  loadPosts()
}

async function deleteComment(id) {
  await ElMessageBox.confirm('Delete this comment?')
  await adminApi.deleteComment(id)
  ElMessage.success('Deleted')
  loadComments()
}

function editSection(row) {
  Object.assign(sectionForm, row || { id: null, name: '', description: '', sort: 0, status: 1 })
  sectionDialog.value = true
}

async function saveSection() {
  sectionForm.id ? await adminApi.updateSection(sectionForm.id, sectionForm) : await adminApi.addSection(sectionForm)
  sectionDialog.value = false
  ElMessage.success('Saved')
  loadSections()
}

async function deleteSection(id) {
  await ElMessageBox.confirm('Delete this section?')
  await adminApi.deleteSection(id)
  ElMessage.success('Deleted')
  loadSections()
}

onMounted(refreshAll)
</script>

<template>
  <div class="page">
    <div class="toolbar">
      <el-input v-model="query.keyword" clearable placeholder="Keyword" style="max-width: 320px" />
      <el-button type="primary" @click="refreshAll">Search</el-button>
    </div>
    <el-card>
      <el-tabs>
        <el-tab-pane label="Users">
          <el-table :data="users">
            <el-table-column prop="id" label="ID" width="70" />
            <el-table-column prop="username" label="Username" />
            <el-table-column prop="nickname" label="Nickname" />
            <el-table-column prop="role" label="Role" width="100" />
            <el-table-column label="Status" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? 'Active' : 'Disabled' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="Action" width="130">
              <template #default="{ row }">
                <el-button v-if="row.role !== 'ADMIN'" size="small" @click="toggleUser(row)">{{ row.status === 1 ? 'Disable' : 'Enable' }}</el-button>
                <el-tag v-else type="info">Admin</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="Posts">
          <el-table :data="posts">
            <el-table-column prop="title" label="Title" />
            <el-table-column prop="authorName" label="Author" width="120" />
            <el-table-column prop="sectionName" label="Section" width="120" />
            <el-table-column label="Action" width="260">
              <template #default="{ row }">
                <el-button size="small" @click="adminApi.topPost(row.id, row.isTop ? 0 : 1).then(loadPosts)">{{ row.isTop ? 'Untop' : 'Top' }}</el-button>
                <el-button size="small" @click="adminApi.essencePost(row.id, row.isEssence ? 0 : 1).then(loadPosts)">{{ row.isEssence ? 'Unstar' : 'Star' }}</el-button>
                <el-button size="small" type="danger" @click="deletePost(row.id)">Delete</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="Comments">
          <el-table :data="comments">
            <el-table-column prop="content" label="Content" />
            <el-table-column prop="authorName" label="Author" width="120" />
            <el-table-column prop="createTime" label="Time" width="180" />
            <el-table-column label="Action" width="100">
              <template #default="{ row }">
                <el-button size="small" type="danger" @click="deleteComment(row.id)">Delete</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="Sections">
          <el-button type="primary" style="margin-bottom: 12px" @click="editSection()">New Section</el-button>
          <el-table :data="sections">
            <el-table-column prop="name" label="Name" />
            <el-table-column prop="description" label="Description" />
            <el-table-column prop="sort" label="Sort" width="90" />
            <el-table-column label="Action" width="160">
              <template #default="{ row }">
                <el-button size="small" @click="editSection(row)">Edit</el-button>
                <el-button size="small" type="danger" @click="deleteSection(row.id)">Delete</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-dialog v-model="sectionDialog" title="Section" width="420px">
      <el-form label-position="top">
        <el-form-item label="Name"><el-input v-model="sectionForm.name" /></el-form-item>
        <el-form-item label="Description"><el-input v-model="sectionForm.description" /></el-form-item>
        <el-form-item label="Sort"><el-input-number v-model="sectionForm.sort" :min="0" /></el-form-item>
        <el-form-item label="Status">
          <el-switch v-model="sectionForm.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="sectionDialog = false">Cancel</el-button>
        <el-button type="primary" @click="saveSection">Save</el-button>
      </template>
    </el-dialog>
  </div>
</template>
