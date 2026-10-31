import request from '@/utils/request'

// 查询测试用户列表
export function listAccount(query) {
  return request({
    url: '/acount/account/list',
    method: 'get',
    params: query
  })
}

// 查询测试用户详细
export function getAccount(id) {
  return request({
    url: '/acount/account/' + id,
    method: 'get'
  })
}

// 新增测试用户
export function addAccount(data) {
  return request({
    url: '/acount/account',
    method: 'post',
    data: data
  })
}

// 修改测试用户
export function updateAccount(data) {
  return request({
    url: '/acount/account',
    method: 'put',
    data: data
  })
}

// 删除测试用户
export function delAccount(id) {
  return request({
    url: '/acount/account/' + id,
    method: 'delete'
  })
}

// 导出测试用户
export function exportAccount(query) {
  return request({
    url: '/acount/account/export',
    method: 'get',
    params: query
  })
}