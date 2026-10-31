import request from "@/utils/request";

// 查询【请填写功能名称】列表
export function listContract(query) {
  return request({
    url: "/customer/contract/list",
    method: "get",
    params: query
  });
}

// 查询【请填写功能名称】详细
export function getContract(contractId) {
  return request({
    url: "/customer/contract/" + contractId,
    method: "get"
  });
}

// 新增【请填写功能名称】
export function addContract(data) {
  return request({
    url: "/customer/contract",
    method: "post",
    data: data
  });
}

// 修改【请填写功能名称】
export function updateContract(data) {
  return request({
    url: "/customer/contract",
    method: "put",
    data: data
  });
}

// 删除【请填写功能名称】
export function delContract(contractId) {
  return request({
    url: "/customer/contract/" + contractId,
    method: "delete"
  });
}

// 导出【请填写功能名称】
export function exportContract(query) {
  return request({
    url: "/customer/contract/export",
    method: "get",
    params: query
  });
}
// 获取服务项目
export function getServiceItems(query) {
  return request({
    url: "/base/item/list",
    method: "get",
    params: query
  });
}

// 获取合同编号
export function getContractNo() {
  return request({
    url: "/customer/contract/getContractNo",
    method: "get"
  });
}
