<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { userApi } from '@/api'

const router = useRouter()
const form = reactive({ username: '', password: '' })

async function submit() {
  const res = await userApi.login(form)
  localStorage.setItem('loginUser', JSON.stringify(res.data))
  ElMessage.success('登录成功')
  router.push('/posts')
}
</script>

<template>
  <div class="auth-page">
    <el-card class="auth-card">
      <h2>登录论坛</h2>
      <el-form label-position="top" @keyup.enter="submit">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-button type="primary" class="full" @click="submit">登录</el-button>
        <div class="switch">没有账号？<a @click="router.push('/register')">去注册</a></div>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.auth-page { min-height: 100vh; display: grid; place-items: center; background: #eef6f5; }
.auth-card { width: min(420px, calc(100vw - 32px)); }
h2 { margin-bottom: 22px; font-weight: 800; }
.full { width: 100%; }
.switch { margin-top: 14px; text-align: center; color: #6b7280; }
.switch a { color: #0f766e; cursor: pointer; }
</style>
