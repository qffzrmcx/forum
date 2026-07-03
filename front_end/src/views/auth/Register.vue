<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { userApi } from '@/api'

const router = useRouter()
const form = reactive({ username: '', password: '', confirmPassword: '', email: '' })

async function submit() {
  await userApi.register(form)
  ElMessage.success('注册成功，请登录')
  router.push('/login')
}
</script>

<template>
  <div class="auth-page">
    <el-card class="auth-card">
      <h2>注册账号</h2>
      <el-form label-position="top">
        <el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="form.password" type="password" show-password /></el-form-item>
        <el-form-item label="确认密码"><el-input v-model="form.confirmPassword" type="password" show-password /></el-form-item>
        <el-button type="primary" class="full" @click="submit">注册</el-button>
        <div class="switch">已有账号？<a @click="router.push('/login')">去登录</a></div>
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
