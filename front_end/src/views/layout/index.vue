<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const loginUser = ref(JSON.parse(localStorage.getItem('loginUser') || 'null'))
const user = computed(() => loginUser.value?.user)
const avatarUrl = computed(() => user.value?.avatar || '')

function logout() {
  localStorage.removeItem('loginUser')
  loginUser.value = null
  router.push('/login')
}
</script>

<template>
  <el-container>
    <el-header class="site-header">
      <div class="page header-inner">
        <div class="brand" @click="router.push('/posts')">QFFZ Forum</div>
        <el-menu mode="horizontal" router :default-active="$route.path" class="nav-menu">
          <el-menu-item index="/posts">帖子</el-menu-item>
          <el-menu-item index="/post-edit">发帖</el-menu-item>
          <el-menu-item v-if="user" index="/profile">个人中心</el-menu-item>
          <el-menu-item v-if="user?.role === 'ADMIN'" index="/admin">后台管理</el-menu-item>
        </el-menu>
        <div class="user-actions">
          <template v-if="user">
            <el-avatar :size="32" :src="avatarUrl">{{ (user.nickname || user.username || 'U').slice(0, 1) }}</el-avatar>
            <span class="username">{{ user.nickname || user.username }}</span>
            <el-button size="small" @click="logout">退出</el-button>
          </template>
          <template v-else>
            <el-button size="small" @click="router.push('/login')">登录</el-button>
            <el-button size="small" type="primary" @click="router.push('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </el-header>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>

<style scoped>
.site-header {
  padding: 0;
  background: #ffffff;
  border-bottom: 1px solid #e5e7eb;
}
.header-inner {
  display: grid;
  grid-template-columns: auto 1fr auto;
  align-items: center;
  gap: 18px;
  height: 60px;
}
.brand {
  font-size: 20px;
  font-weight: 800;
  color: #0f766e;
  cursor: pointer;
}
.nav-menu {
  border-bottom: 0;
}
.user-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  justify-content: flex-end;
}
.username {
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-weight: 700;
  color: #111827;
}
@media (max-width: 760px) {
  .header-inner {
    grid-template-columns: 1fr;
    height: auto;
    padding: 10px 0;
  }
  .user-actions {
    justify-content: flex-start;
  }
}
</style>
