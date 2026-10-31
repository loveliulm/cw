import request from '@/utils/request'

// 查询工商服务任务列表
export function listCommerce(query) {
  return request({
    url: '/commerce/commerce/list',
    method: 'get',
    params: query
  })
}

// 查询工商服务任务详细
export function getCommerce(id) {
  return request({
    url: '/commerce/commerce/' + id,
    method: 'get'
  })
}

// 新增工商服务任务
export function addCommerce(data) {
  return request({
    url: '/commerce/commerce',
    method: 'post',
    data: data
  })
}

// 修改工商服务任务
export function updateCommerce(data) {
  return request({
    url: '/commerce/commerce',
    method: 'put',
    data: data
  })
}

// 删除工商服务任务
export function delCommerce(id) {
  return request({
    url: '/commerce/commerce/' + id,
    method: 'delete'
  })
}

// 导出工商服务任务
export function exportCommerce(query) {
  return request({
    url: '/commerce/commerce/export',
    method: 'get',
    params: query
  })
}
//查询工商服务类型列表
export function gongCommerce(query){
  return request({
    url:'/base/type/list',
    method:'get',
    params: query
  })
}

//工商任务执行
export function gongZhiixng(query){
  return request({
    url:'/commerce/step/serviceDo/' +query,
    method:'get',
    
  })
}

//工商外勤派工
export function addDistrution(query){
  return request({
    url:'/commerce/commerce/assign',
    method:'post',
    params: query
  })
}

//停止服务
export function stopService(id){
  return request({
    url:'/commerce/commerce/stopService/' + id,
    method:'get',
  })
}

//恢复服务
export function resumeService(id){
  return request({
    url:'/commerce/commerce/restoreService/' + id,
    method:'get',
  })
}