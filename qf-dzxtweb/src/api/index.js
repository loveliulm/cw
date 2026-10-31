import request from "@/utils/request";

// 获取首页报表-四个金额
export function getIndexdMoneny(data) {
  return request({
    url: "/accounting/firstPage/report/getInfo",
    method: "get",
    params: data
  });
}

// 获取首页最新凭证
export function latestVouchers(data) {
  return request({
    url: "/accounting/firstPage/voucher/getInfo",
    method: "get",
    params: data
  });
}

export function voucherList(data) {
  return request({
    url: "/accounting/voucher/list",
    method: "get",
    params: data
  });
}

// 获取本期财务指标
export function financeTarget(data) {
  return request({
    url: "/accounting/firstPage/subject/getInfo",
    method: "get",
    params: data
  });
}

// 获取税收折线图数据
export function taxs(data) {
  return request({
    url: "/accounting/firstPage/tax/getInfo",
    method: "get",
    params: data
  });
}
