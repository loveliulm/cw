import request from "@/utils/request";

// 获取科目列表
export function subjectListByQuery(query) {
  return request({
    url: "/accounting/quantityDetail/getSubjectList",
    method: "get",
    params: query
  });
}

// 获取数量金额明细账列表
export function numDetailList(data) {
  return request({
    url: "/accounting/quantityDetail/getList",
    method: "get",
    params: data
  });
}

// 导出数量金额明细账
export function exportNumDetailAccount(data) {
  return request({
    url: "/accounting/quantityDetail/export",
    method: "get",
    params: data
  });
}
