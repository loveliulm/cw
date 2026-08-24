import request from '@/utils/request'

// 查询accBook列表
export function listTotal(query) {
  return request({
    url: '/accounting/voucherTotal/list',
    method: 'get',
    params: query
  })
}

// 查询accBook详细
export function getTotal(id) {
  return request({
    url: '/accBook/total/' + id,
    method: 'get'
  })
}

// 新增accBook
export function addTotal(data) {
  return request({
    url: '/accBook/total',
    method: 'post',
    data: data
  })
}

// 修改accBook
export function updateTotal(data) {
  return request({
    url: '/accBook/total',
    method: 'put',
    data: data
  })
}

// 删除accBook
export function delTotal(id) {
  return request({
    url: '/accBook/total/' + id,
    method: 'delete'
  })
}

// 导出accBook
export function exportTotal(query) {
  return request({
    url: '/accBook/total/export',
    method: 'get',
    params: query
  })
}