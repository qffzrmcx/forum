<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { userApi } from '@/api'

const router = useRouter()
const form = reactive({ nickname: '', avatar: '', email: '', bio: '', password: '' })
const posts = ref([])
const collects = ref([])

async function load() {
  const info = await userApi.info()
  Object.assign(form, info.data || {})
  form.password = ''
  posts.value = (await userApi.posts({ page: 1, size: 20 })).data.records || []
  collects.value = (await userApi.collects()).data || []
}

async function save() {
  await userApi.update(form)
  const loginUser = JSON.parse(localStorage.getItem('loginUser') || 'null')
  if (loginUser?.user) {
    loginUser.user.nickname = form.nickname
    loginUser.user.avatar = form.avatar
    localStorage.setItem('loginUser', JSON.stringify(loginUser))
  }
  ElMessage.success('保存成功')
  router.go(0)
}

onMounted(load)
</script>

<template>
  <div class="page">
    <el-row :gutter="16">
      <el-col :xs="24" :md="10">
        <el-card>
          <template #header>个人资料</template>
          <el-form label-position="top">
            <el-form-item label="昵称"><el-input v-model="form.nickname" /></el-form-item>
            <el-form-item label="头像地址"><el-input v-model="form.avatar" /></el-form-item>
            <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
            <el-form-item label="简介"><el-input v-model="form.bio" type="textarea" :rows="4" /></el-form-item>
            <el-form-item label="新密码"><el-input v-model="form.password" type="password" show-password placeholder="不修改请留空" /></el-form-item>
            <el-button type="primary" @click="save">保存资料</el-button>
          </el-form>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="14">
        <el-card>
          <el-tabs>
            <el-tab-pane label="我的帖子">
              <el-table :data="posts" @row-click="row => router.push(`/post/${row.id}`)">
                <el-table-column prop="title" label="标题" />
                <el-table-column prop="sectionName" label="板块" width="120" />
                <el-table-column prop="createTime" label="发布时间" width="180" />
              </el-table>
            </el-tab-pane>
            <el-tab-pane label="我的收藏">
              <el-table :data="collects" @row-click="row => router.push(`/post/${row.id}`)">
                <el-table-column prop="title" label="标题" />
                <el-table-column prop="authorName" label="作者" width="120" />
                <el-table-column prop="createTime" label="发布时间" width="180" />
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
