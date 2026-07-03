<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { commentApi, postApi } from '@/api'

const route = useRoute()
const router = useRouter()
const post = ref(null)
const comments = ref([])
const commentForm = reactive({ postId: Number(route.params.id), parentId: null, content: '' })
const loginUser = computed(() => JSON.parse(localStorage.getItem('loginUser') || 'null'))
const currentUser = computed(() => loginUser.value?.user)
const canManage = computed(() => currentUser.value && (currentUser.value.role === 'ADMIN' || currentUser.value.id === post.value?.userId))

async function load() {
  const res = await postApi.detail(route.params.id)
  post.value = res.data
  const commentRes = await commentApi.list(route.params.id)
  comments.value = commentRes.data || []
}

async function submitComment() {
  await commentApi.add(commentForm)
  commentForm.content = ''
  commentForm.parentId = null
  ElMessage.success('Comment posted')
  load()
}

async function removePost() {
  await ElMessageBox.confirm('Delete this post?')
  await postApi.delete(post.value.id)
  ElMessage.success('Deleted')
  router.push('/posts')
}

async function removeComment(id) {
  await commentApi.delete(id)
  ElMessage.success('Deleted')
  load()
}

async function toggleLike() {
  await postApi.like(post.value.id)
  load()
}

async function toggleCollect() {
  await postApi.collect(post.value.id)
  load()
}

onMounted(load)
</script>

<template>
  <div class="page" v-if="post">
    <el-card>
      <h1>{{ post.title }}</h1>
      <div class="muted">{{ post.authorName }} / {{ post.sectionName }} / {{ post.createTime }}</div>
      <div class="post-content">{{ post.content }}</div>
      <div v-if="post.images" class="images">
        <el-image v-for="img in post.images.split(',')" :key="img" :src="img.trim()" fit="cover" />
      </div>
      <div class="actions">
        <el-button :type="post.liked ? 'primary' : 'default'" @click="toggleLike">Like {{ post.likeCount }}</el-button>
        <el-button :type="post.collected ? 'warning' : 'default'" @click="toggleCollect">Collect {{ post.collectCount }}</el-button>
        <el-button v-if="canManage" @click="router.push(`/post-edit/${post.id}`)">Edit</el-button>
        <el-button v-if="canManage" type="danger" @click="removePost">Delete</el-button>
      </div>
    </el-card>

    <el-card style="margin-top: 16px">
      <template #header>Comments</template>
      <el-input v-model="commentForm.content" type="textarea" :rows="3" maxlength="500" show-word-limit placeholder="Write a comment" />
      <el-button type="primary" style="margin-top: 10px" @click="submitComment">Submit</el-button>
      <el-divider />
      <div v-for="item in comments" :key="item.id" class="comment">
        <div><b>{{ item.authorName }}</b><span class="muted"> / {{ item.createTime }}</span></div>
        <p>{{ item.content }}</p>
        <el-button text size="small" @click="commentForm.parentId = item.id">Reply</el-button>
        <el-button text size="small" @click="commentApi.like(item.id).then(load)">Like {{ item.likeCount }}</el-button>
        <el-button v-if="currentUser?.role === 'ADMIN' || currentUser?.id === item.userId" text size="small" type="danger" @click="removeComment(item.id)">Delete</el-button>
      </div>
      <el-empty v-if="!comments.length" description="No comments" />
    </el-card>
  </div>
</template>

<style scoped>
h1 { font-size: 28px; font-weight: 800; margin-bottom: 8px; }
.post-content { margin: 22px 0; }
.actions { display: flex; gap: 8px; flex-wrap: wrap; }
.images { display: flex; gap: 10px; flex-wrap: wrap; margin-bottom: 16px; }
.images .el-image { width: 180px; height: 120px; border-radius: 6px; }
.comment { padding: 12px 0; border-bottom: 1px solid #edf0f5; }
.comment p { margin: 8px 0; white-space: pre-wrap; }
</style>
