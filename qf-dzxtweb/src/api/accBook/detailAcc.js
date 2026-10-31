import request from "@/utils/request";

// 查询明细账列表
export function detailAccList(query) {
  return request({
    url: "/accounting/detailAccount/getList",
    method: "get",
    params: query
  });
}

// 获取科目列表
export function subjectListByQuery(query) {
  return request({
    url: "/accounting/detailAccount/getSubjectList",
    method: "get",
    params: query
  });
}

// 明细账导出
export function exportDetailAccount(query) {
  return request({
    url: "/accounting/detailAccount/export",
    method: "get",
    params: query
  });
}
