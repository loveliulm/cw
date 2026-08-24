import request from '@/utils/request'

// 查询报税列表
export function listDeclaration(query) {
  return request({
    url: '/customer/declaration/list',
    method: 'get',
    params: query
  })
}

// 查询报税详细
export function getDeclaration(id) {
  return request({
    url: '/customer/declaration/' + id,
    method: 'get'
  })
}

// 新增报税
export function addDeclaration(data) {
  return request({
    url: '/customer/declaration',
    method: 'post',
    data: data
  })
}

// 修改报税
export function updateDeclaration(data) {
  return request({
    url: '/customer/declaration',
    method: 'put',
    data: data
  })
}

// 删除报税
export function delDeclaration(id) {
  return request({
    url: '/customer/declaration/' + id,
    method: 'delete'
  })
}

// 导出报税
export function exportDeclaration(query) {
  return request({
    url: '/customer/declaration/export',
    method: 'get',
    params: query
  })
}