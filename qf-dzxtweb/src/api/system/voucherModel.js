import request from "@/utils/request";

// 查询凭证模板列表
export function listVoucherModel(query) {
  return request({
    url: "/accounting/voucherModel/list",
    method: "get",
    params: query
  });
}

// 查询凭证模板详细
export function getVoucherModel(modelId) {
  return request({
    url: "/accounting/voucherModel/" + modelId,
    method: "get"
  });
}

// 新增凭证模板
export function addVoucherModel(data) {
  return request({
    url: "/accounting/voucherModel",
    method: "post",
    data: data
  });
}

// 修改凭证模板
export function updateVoucherModel(data) {
  return request({
    url: "/accounting/voucherModel",
    method: "put",
    data: data
  });
}

// 删除凭证模板
export function delVoucherModel(modelId) {
  return request({
    url: "/accounting/voucherModel/" + modelId,
    method: "delete"
  });
}

// 导出凭证模板
export function exportVoucherModel(query) {
  return request({
    url: "/accounting/voucherModel/export",
    method: "get",
    params: query
  });
}

