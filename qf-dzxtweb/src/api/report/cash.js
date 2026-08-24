import request from "@/utils/request";

// 利润列表
export function profitList(data) {
  return request({
    url: "/accounting/cash/list",
    method: "get",
    params: data
  });
}

// 获取规则列表
export function profitRule(data) {
  return request({
    url: "/accounting/cashRule/list",
    method: "get",
    params: data
  });
}

// 保存规则列表
export function saveProfitRule(data) {
  return request({
    url: "/accounting/cashRule",
    method: "put",
    data: data
  });
}

// 获取季度列表
export function quarterList(data) {
  return request({
    url: "/accounting/period/quarterList",
    method: "get",
    params: data
  });
}

// 获获取本期金额
export function balanceSheet(data) {
  return request({
    url: "/accounting/cashRule/balanceSheet",
    method: "get",
    params: data
  });
}

// 导出利润表
export function exportProfit(query) {
  return request({
    url: "/accounting/cash/export",
    method: "get",
    params: query
  });
}
