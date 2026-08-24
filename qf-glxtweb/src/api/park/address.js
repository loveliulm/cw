import request from '@/utils/request'

// 查询园区管理-园区地址列表
export function selectParkAddressList(query) {
  return request({
    url: '/park/address/list',
    method: 'get',
    params: query
  })
}

// 查询园区管理-园区地址列表-不分页
export function getParkAddressList(query) {
  return request({
    url: '/park/address/getList',
    method: 'get',
    params: query
  })
}

// 查询园区管理-园区地址详细
export function selectParkAddressById(id) {
  return request({
    url: '/park/address/' + id,
    method: 'get'
  })
}

// 新增园区管理-园区地址
export function addParkAddress(data) {
  return request({
    url: '/park/address',
    method: 'post',
    data: data
  })
}

// 修改园区管理-园区地址
export function updateParkAddress(data) {
  return request({
    url: '/park/address',
    method: 'put',
    data: data
  })
}

// 删除园区管理-园区地址
export function delParkAddress(id) {
  return request({
    url: '/park/address/' + id,
    method: 'delete'
  })
}

// 导出园区管理-园区地址
export function exportParkAddress(query) {
  return request({
    url: '/park/address/export',
    method: 'get',
    params: query
  })
}

// 查询【合同】列表
export function getContractList(query) {
  return request({
    url: "/park/address/getContractList",
    method: "get",
    params: query
  });
}

// 使用/取消使用地址
export function doParkContract(query) {
  return request({
    url: "/park/address/doParkContract",
    method: "get",
    params: query
  });
}
