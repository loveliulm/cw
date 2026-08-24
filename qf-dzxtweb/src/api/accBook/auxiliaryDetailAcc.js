import request from "@/utils/request";

// 获取科目列表
export function subjectListByQuery(query) {
  return request({
    url: "/accounting/auxiliaryDetail/getSubjectList",
    method: "get",
    params: query
  });
}

// 获取辅助核算明细账列表
export function auxDetailList(data) {
  return request({
    url: "/accounting/auxiliaryDetail/getList",
    method: "get",
    params: data
  });
}

// 获取辅助核算类型
export function auxiliaryTypeList(data) {
  return request({
    url: "/accounting/auxiliaryDetail/getAuxiliaryTypeList",
    method: "get",
    params: data
  });
}

// 获取辅助核算项目
export function auxiliaryItemList(data) {
  return request({
    url: "/accounting/auxiliaryDetail/getAuxiliary",
    method: "get",
    params: data
  });
}

// 导出核算明细账
export function exportAuxDetailAccount(data) {
  return request({
    url: "/accounting/auxiliaryDetail/export",
    method: "get",
    params: data
  });
}
