import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'

const routes = [
  { path: '/', redirect: '/posts' },
  { path: '/login', component: () => import('@/views/auth/Login.vue') },
  { path: '/register', component: () => import('@/views/auth/Register.vue') },
  {
    path: '/',
    component: () => import('@/views/layout/index.vue'),
    children: [
      { path: 'posts', component: () => import('@/views/post/PostList.vue') },
      { path: 'section/:id', component: () => import('@/views/post/PostList.vue') },
      { path: 'post/:id', component: () => import('@/views/post/PostDetail.vue') },
      { path: 'post-edit/:id?', component: () => import('@/views/post/PostEdit.vue'), meta: { login: true } },
      { path: 'profile', component: () => import('@/views/user/Profile.vue'), meta: { login: true } },
      { path: 'admin', component: () => import('@/views/admin/AdminHome.vue'), meta: { login: true, admin: true } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  const loginUser = JSON.parse(localStorage.getItem('loginUser') || 'null')
  if (to.meta.login && !loginUser?.token) {
    ElMessage.warning('请先登录')
    return '/login'
  }
  if (to.meta.admin && loginUser?.user?.role !== 'ADMIN') {
    ElMessage.warning('无管理员权限')
    return '/posts'
  }
  return true
})

export default router
