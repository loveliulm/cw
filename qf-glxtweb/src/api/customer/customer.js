import request from "@/utils/request";

// 查询客户列表
export function listCustomer(query) {
  return request({
    url: "/customer/customer/list",
    method: "get",
    params: query
  });
}

// 查询客户详细
export function getCustomer(id) {
  return request({
    url: "/customer/customer/" + id,
    method: "get"
  });
}

// 新增客户
export function addCustomer(data) {
  return request({
    url: "/customer/customer",
    method: "post",
    data: data
  });
}

// 修改客户
export function updateCustomer(data) {
  return request({
    url: "/customer/customer",
    method: "put",
    data: data
  });
}
// 修改销方
export function updateCustomers(data) {
  return request({
    url: "/customer/customer/update",
    method: "put",
    data: data
  });
}

// 删除客户
export function delCustomer(id) {
  return request({
    url: "/customer/customer/" + id,
    method: "delete"
  });
}

// 导出客户
export function exportCustomer(query) {
  return request({
    url: "/customer/customer/export",
    method: "get",
    params: query
  });
}

// 获取服务类型列表
export function getServerTypes(serviceClassId) {
  return request({
    url: "/base/type/list",
    method: "get",
    serviceClassId: serviceClassId
  });
}

// 获取可分配的员工信息
export function getPersonList(query) {
  return request({
    url: "/customer/customer/user/list",
    method: "get",
    params: query
  });
}
// 保存派工信息
export function addDistrution(data) {
  return request({
    url: "/customer/customer/dispatch",
    method: "put",
    data: data
  });
}
// 获取客户编码
export function getCustomerNo() {
  return request({
    url: "/customer/customer/getCustomerNo",
    method: "get"
  });
}
// 下载用户导入模板
export function importTemplate() {
  return request({
    url: "/customer/customer/importTemplate",
    method: "get"
  });
}