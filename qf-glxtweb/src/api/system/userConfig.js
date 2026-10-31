import request from '@/utils/request'

// 查询系统配置列表
export function selectUserConfigList(query) {
  return request({
    url: '/config/system/list',
    method: 'get',
    params: query
  })
}

// 查询系统配置列表-不分页
export function getUserConfigList(query) {
  return request({
    url: '/config/system/getList',
    method: 'get',
    params: query
  })
}

// 查询系统配置详细
export function selectUserConfigById(id) {
  return request({
    url: '/config/system/' + id,
    method: 'get'
  })
}

// 新增系统配置
export function addUserConfig(data) {
  return request({
    url: '/config/system',
    method: 'post',
    data: data
  })
}

// 修改系统配置
export function updateUserConfig(data) {
  return request({
    url: '/config/system',
    method: 'put',
    data: data
  })
}

// 删除系统配置
export function delUserConfig(id) {
  return request({
    url: '/config/system/' + id,
    method: 'delete'
  })
}

// 导出系统配置
export function exportUserConfig(query) {
  return request({
    url: '/config/system/export',
    method: 'get',
    params: query
  })
}
