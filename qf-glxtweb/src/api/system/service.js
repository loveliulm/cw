import request from "@/utils/request";

// 查询服务流程
export function serviceTreeSelect() {
  return request({
    url: '/system/service/type',
    method: "get"
  });
}

// 获取服务类型详细信息
export function getBaseTypeInfo(id) {
  return request({
    url: '/base/type/' + id,
    method: "get"
  });
}

// 新增服务类型
export function addBaseType(data) {
  return request({
    url: '/base/type',
    method: 'post',
    data: data
  })
}

// 修改服务类型
export function updateBaseType(data) {
  return request({
    url: '/base/type',
    method: 'put',
    data: data
  })
}

// 删除服务类型
export function deleteBaseType(id) {
  return request({
    url: '/base/type/' + id,
    method: "delete"
  });
}

// 获取服务项目详细信息
export function getBaseItemInfo(id) {
  return request({
    url: '/base/item/' + id,
    method: "get"
  });
}

// 新增服务项目
export function addBaseItem(data) {
  return request({
    url: '/base/item',
    method: 'post',
    data: data
  })
}

// 修改服务项目
export function updateBaseItem(data) {
  return request({
    url: '/base/item',
    method: 'put',
    data: data
  })
}

// 删除服务项目
export function deleteBaseItem(id) {
  return request({
    url: '/base/item/' + id,
    method: "delete"
  });
}

// 查询服务流程
export function getProcessSteps(query) {
  return request({
    url: '/system/service/process',
    method: "get",
    params: query
  });
}

// 查询服务流程
export function getServiceProcess(id) {
  return request({
    url: '/system/service/' + id,
    method: "get"
  });
}


// 新增服务流程
export function addServiceProcess(data) {
  return request({
    url: '/system/service',
    method: 'post',
    data: data
  })
}

// 修改服务流程
export function updateServiceProcess(data) {
  return request({
    url: '/system/service',
    method: 'put',
    data: data
  })
}

// 删除服务流程
export function deleteServiceProcess(id) {
  return request({
    url: '/system/service/' + id,
    method: "delete"
  });
}
