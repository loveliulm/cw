import request from "@/utils/request";
import { praseStrEmpty } from "@/utils/ruoyi";

// 获取凭证列表
export function voucherList(data, voucherIds) {
  if (Object.prototype.toString.call(voucherIds) == "[object Undefined]") {
    return request({
      url: "/accounting/voucher/list",
      method: "get",
      params: data
    });
  } else {
    return request({
      url: "/accounting/voucher/list?voucherIds=" + voucherIds,
      method: "get",
      params: data
    });
  }
}
// 凭证审核
export function approveVoucher(voucherIds) {
  return request({
    url: "/accounting/voucher/review/" + voucherIds,
    method: "put"
  });
}

// 凭证反审核
export function unApproveVoucher(voucherIds) {
  return request({
    url: "/accounting/voucher/reviewBack/" + voucherIds,
    method: "put"
  });
}

// 删除凭证
export function delVoucher(voucherIds) {
  return request({
    url: "/accounting/voucher/" + voucherIds,
    method: "delete"
  });
}

// 复制凭证
export function copyVoucher(voucherIds) {
  return request({
    url: "/accounting/voucher/copy/" + voucherIds,
    method: "post"
  });
}

// 当前账期
export function currentPeriod(data) {
  return request({
    url: "/accounting/period/getCurrentPeriod",
    method: "get",
    params: data
  });
}

// 获取账期
export function periodList(data) {
  return request({
    url: "/accounting/period/list",
    method: "get",
    params: data
  });
}

// 凭证获取科目
export function getSubjectVoucher(data) {
  return request({
    url: "/accounting/subject/searchLeaf",
    method: "get",
    params: data
  });
}
