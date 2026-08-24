import request from "@/utils/request";

// 科目余额列表
export function accountBalanceList(data) {
  return request({
    url: "/accounting/bookBalance/getList",
    method: "get",
    params: data
  });
}

// 导出科目余额表
export function exportAccBalance(query) {
  return request({
    url: "/accounting/bookBalance/export",
    method: "get",
    params: query
  });
}