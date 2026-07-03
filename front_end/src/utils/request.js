import axios from 'axios'
import router from '../router'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  timeout: 180000
})

request.interceptors.request.use(
  (config) => {
    const loginUser = JSON.parse(localStorage.getItem('loginUser') || 'null')
    if (loginUser?.token) {
      config.headers.token = loginUser.token
    }
    return config
  },
  (error) => {
    ElMessage.error('请求发送失败')
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  (response) => {
    const result = response.data
    if (result?.code && result.code !== 200) {
      ElMessage.error(result.msg || '接口访问异常')
      return Promise.reject(result)
    }
    return result
  },
  (error) => {
    if (error.response?.status === 401) {
      ElMessage.error(error.response?.data?.msg || '登录超时，请重新登录')
      localStorage.removeItem('loginUser')
      router.push('/login')
    } else {
      const message = error.response?.data?.msg || error.response?.data?.error || '接口访问异常'
      ElMessage.error(message)
    }
    return Promise.reject(error)
  }
)

export default request
