import request from "@/utils/request";

// 查询accBook列表
export function listAccTotal(query) {
  return request({
    url: "/accounting/totalAccount/getList",
    method: "get",
    params: query
  });
}

// 导出
export function exportTotalAccount(query) {
  return request({
    url: "/accounting/totalAccount/export",
    method: "get",
    params: query
  });
}
