import request from "@/utils/request";
import { praseStrEmpty } from "@/utils/ruoyi";

// 凭证摘要
export function getSummaryVoucher(data) {
  return request({
    url: "/accounting/summary/list",
    method: "get",
    params: data
  });
}

// 修改凭证摘要
export function editSummary(data) {
  return request({
    url: "/accounting/summary",
    method: "put",
    data: data
  });
}

// 新增凭证摘要
export function addSummary(data) {
  return request({
    url: "/accounting/summary",
    method: "post",
    data: data
  });
}

// 删除凭证摘要
export function delSummary(id) {
  return request({
    url: "/accounting/summary/" + id,
    method: "delete"
  });
}

// 保存凭证
export function saveVoucher(data) {
  return request({
    url: "/accounting/voucher",
    method: "post",
    data: data
  });
}

// 获取科目余额
export function subjectBalance(subjectId) {
  return request({
    url: "/accounting/subject/balance/" + subjectId,
    method: "get"
  });
}

// 保存凭证模板
export function saveVoucherModel(data) {
  return request({
    url: "/accounting/voucherModel",
    method: "post",
    data: data
  });
}

// 获取凭证号
export function getVoucherCode(data) {
  return request({
    url: "/accounting/voucher/getCode",
    method: "get",
    params: data
  });
}

//查询凭证详情
export function getVoucherDetail(voucherId) {
  return request({
    url: "/accounting/voucher/" + voucherId,
    method: "get"
  });
}

// 修改凭证
export function editVoucher(data) {
  return request({
    url: "/accounting/voucher",
    method: "put",
    data: data
  });
}
