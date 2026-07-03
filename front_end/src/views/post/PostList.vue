<script setup>
import { onMounted, reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { postApi, sectionApi } from '@/api'

const route = useRoute()
const router = useRouter()
const sections = ref([])
const posts = ref([])
const total = ref(0)
const query = reactive({ page: 1, size: 10, keyword: '', sort: 'new', sectionId: null })

async function loadSections() {
  const res = await sectionApi.list()
  sections.value = res.data || []
}

async function loadPosts() {
  query.sectionId = route.params.id ? Number(route.params.id) : null
  const res = await postApi.list(query)
  posts.value = res.data.records || []
  total.value = res.data.total || 0
}

function search() {
  query.page = 1
  loadPosts()
}

onMounted(() => {
  loadSections()
  loadPosts()
})

watch(() => route.params.id, loadPosts)
</script>

<template>
  <div class="page">
    <div class="toolbar">
      <el-input
        v-model="query.keyword"
        clearable
        placeholder="搜索标题、内容、作者或板块"
        style="max-width: 360px"
        @keyup.enter="search"
      />
      <div>
        <el-select v-model="query.sort" style="width: 120px" @change="search">
          <el-option label="最新" value="new" />
          <el-option label="热度" value="hot" />
        </el-select>
        <el-button type="primary" style="margin-left: 8px" @click="router.push('/post-edit')">
          发布帖子
        </el-button>
      </div>
    </div>

    <el-row :gutter="16" class="forum-main">
      <el-col :xs="24" :md="6">
        <el-card class="section-card">
          <template #header>论坛板块</template>
          <el-menu :default-active="route.path" router>
            <el-menu-item index="/posts">全部帖子</el-menu-item>
            <el-menu-item v-for="item in sections" :key="item.id" :index="`/section/${item.id}`">
              {{ item.name }}
            </el-menu-item>
          </el-menu>
        </el-card>
      </el-col>

      <el-col :xs="24" :md="18">
        <div class="post-list-panel">
          <div class="post-list-content">
            <el-card v-for="post in posts" :key="post.id" class="post-card">
              <div class="post-title" @click="router.push(`/post/${post.id}`)">
                <el-tag v-if="post.isTop" size="small" type="danger">置顶</el-tag>
                <el-tag v-if="post.isEssence" size="small" type="warning">精华</el-tag>
                {{ post.title }}
              </div>
              <div class="muted" style="margin-top: 8px">
                {{ post.authorName }} · {{ post.sectionName }} · {{ post.createTime }}
              </div>
              <div class="muted" style="margin-top: 8px">
                浏览 {{ post.viewCount }}　评论 {{ post.commentCount }}　点赞 {{ post.likeCount }}　收藏 {{ post.collectCount }}
              </div>
            </el-card>
            <el-empty v-if="!posts.length" description="暂无帖子" />
          </div>

          <div class="pagination-wrap">
            <el-pagination
              v-model:current-page="query.page"
              layout="prev, pager, next, total"
              :total="total"
              :page-size="query.size"
              @current-change="loadPosts"
            />
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.forum-main {
  align-items: stretch;
}

.section-card,
.post-list-panel {
  min-height: 590px;
}

.post-list-panel {
  display: flex;
  flex-direction: column;
}

.post-list-content {
  flex: 1;
}

.pagination-wrap {
  display: flex;
  align-items: center;
  min-height: 44px;
  padding-top: 4px;
}

@media (max-width: 760px) {
  .section-card,
  .post-list-panel {
    min-height: auto;
  }

  .pagination-wrap {
    margin-top: 12px;
  }
}
</style>
