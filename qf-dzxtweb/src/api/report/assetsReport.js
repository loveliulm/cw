import request from "@/utils/request";

// 科目余额列表
export function assetsReportList(data) {
  return request({
    url: "/accounting/assetsReport/list",
    method: "get",
    params: data
  });
}

// 获取规则列表
export function assetsReportRule(data) {
  return request({
    url: "/accounting/assetsReportRule/list",
    method: "get",
    params: data
  });
}

// 获取期末数、年初数
export function balanceSheet(data) {
  return request({
    url: "/accounting/assetsReportRule/balanceSheet",
    method: "get",
    params: data
  });
}

// 保存规则列表
export function saveReportRule(data) {
  return request({
    url: "/accounting/assetsReportRule",
    method: "put",
    data: data
  });
}

// 导出资产负债表
export function exportAssetsReport(query) {
  return request({
    url: "/accounting/assetsReport/export",
    method: "get",
    params: query
  });
}
