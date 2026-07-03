import request from '@/utils/request'

export const userApi = {
  register: data => request.post('/user/register', data),
  login: data => request.post('/user/login', data),
  info: () => request.get('/user/info'),
  update: data => request.put('/user/update', data),
  posts: params => request.get('/user/posts', { params }),
  collects: () => request.get('/user/collects')
}

export const sectionApi = {
  list: () => request.get('/section/list')
}

export const postApi = {
  list: params => request.get('/post/list', { params }),
  detail: id => request.get(`/post/detail/${id}`),
  add: data => request.post('/post/add', data),
  update: (id, data) => request.put(`/post/update/${id}`, data),
  delete: id => request.delete(`/post/delete/${id}`),
  like: id => request.post(`/post/like/${id}`),
  collect: id => request.post(`/post/collect/${id}`)
}

export const commentApi = {
  list: postId => request.get('/comment/list', { params: { postId } }),
  add: data => request.post('/comment/add', data),
  delete: id => request.delete(`/comment/delete/${id}`),
  like: id => request.post(`/comment/like/${id}`)
}

export const adminApi = {
  users: params => request.get('/admin/user/list', { params }),
  disableUser: id => request.put(`/admin/user/disable/${id}`),
  enableUser: id => request.put(`/admin/user/enable/${id}`),
  deleteUser: id => request.delete(`/admin/user/delete/${id}`),
  posts: params => request.get('/admin/post/list', { params }),
  deletePost: id => request.delete(`/admin/post/delete/${id}`),
  topPost: (id, value) => request.put(`/admin/post/top/${id}`, null, { params: { value } }),
  essencePost: (id, value) => request.put(`/admin/post/essence/${id}`, null, { params: { value } }),
  comments: params => request.get('/admin/comment/list', { params }),
  deleteComment: id => request.delete(`/admin/comment/delete/${id}`),
  sections: () => request.get('/admin/section/list'),
  addSection: data => request.post('/admin/section/add', data),
  updateSection: (id, data) => request.put(`/admin/section/update/${id}`, data),
  deleteSection: id => request.delete(`/admin/section/delete/${id}`)
}
