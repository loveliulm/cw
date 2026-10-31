import request from "@/utils/request";

// 获取科目列表
export function subjectListByQuery(query) {
  return request({
    url: "/accounting/auxiliaryTotal/getSubjectList",
    method: "get",
    params: query
  });
}

// 获取辅助核算明总账列表
export function auxTotalList(data) {
  return request({
    url: "/accounting/auxiliaryTotal/getList",
    method: "get",
    params: data
  });
}

// 导出核算明总账
export function exportAuxTotalAccount(data) {
  return request({
    url: "/accounting/auxiliaryTotal/export",
    method: "get",
    params: data
  });
}
