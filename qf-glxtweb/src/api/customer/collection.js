import request from '@/utils/request'

// 查询收款列表
export function listCollection(query) {
  return request({
    url: '/customer/collection/list',
    method: 'get',
    params: query
  })
}

// 查询收款详细
export function getCollection(id) {
  return request({
    url: '/customer/collection/' + id,
    method: 'get'
  })
}

// 新增收款
export function addCollection(data) {
  return request({
    url: '/customer/collection',
    method: 'post',
    data: data
  })
}

// 修改收款
export function updateCollection(data) {
  return request({
    url: '/customer/collection',
    method: 'put',
    data: data
  })
}

// 删除收款
export function delCollection(id) {
  return request({
    url: '/customer/collection/' + id,
    method: 'delete'
  })
}

// 导出收款
export function exportCollection(query) {
  return request({
    url: '/customer/collection/export',
    method: 'get',
    params: query
  })
}

// 查询收款支付情况列表
// export function payment(query) {
//   return request({
//     url: '/customer/collection/record/list',
//     method: 'get',
//     params: query
//   })
// }
//提交付款数据
export function paymentList(query) {
  return request({
    url: '/customer/collection/record',
    method: 'post',
    data: query
  })
}
//查询付款结束时间

export function SearchList(id) {
  return request({
    url: '/customer/collection/' + id,
    method: 'get'
  })
}
//收款详情删除
export function removeDelete(id) {
  return request({
    url: '/customer/collection/record/' + id,
    method: 'delete'
  })
}

//收款详情修改
export function detailAmend(query) {
  return request({
    url: '/customer/collection/record' ,
    method: 'put',
    data: query
  })
}