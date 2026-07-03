<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { postApi, sectionApi } from '@/api'

const route = useRoute()
const router = useRouter()
const sections = ref([])
const form = reactive({ title: '', sectionId: null, content: '', images: '' })
const editing = !!route.params.id

async function load() {
  const sectionRes = await sectionApi.list()
  sections.value = sectionRes.data || []
  if (editing) {
    const res = await postApi.detail(route.params.id)
    Object.assign(form, {
      title: res.data.title,
      sectionId: res.data.sectionId,
      content: res.data.content,
      images: res.data.images
    })
  }
}

async function submit() {
  if (editing) {
    await postApi.update(route.params.id, form)
    ElMessage.success('修改成功')
    router.push(`/post/${route.params.id}`)
  } else {
    const res = await postApi.add(form)
    ElMessage.success('发布成功')
    router.push(`/post/${res.data}`)
  }
}

onMounted(load)
</script>

<template>
  <div class="page">
    <el-card>
      <template #header>{{ editing ? '编辑帖子' : '发布帖子' }}</template>
      <el-form label-position="top">
        <el-form-item label="标题"><el-input v-model="form.title" maxlength="120" show-word-limit /></el-form-item>
        <el-form-item label="板块">
          <el-select v-model="form.sectionId" placeholder="请选择板块" style="width: 240px">
            <el-option v-for="item in sections" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="12" /></el-form-item>
        <el-form-item label="图片地址">
          <el-input v-model="form.images" placeholder="可填写图片URL，多个可用逗号分隔" />
        </el-form-item>
        <el-button type="primary" @click="submit">{{ editing ? '保存修改' : '发布帖子' }}</el-button>
      </el-form>
    </el-card>
  </div>
</template>
