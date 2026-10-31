import request from "@/utils/request";
//import { praseStrEmpty } from "@/utils/ruoyi";

// 结转列表
export function carryoverList(data) {
  return request({
    url: "/accounting/carryover/list",
    method: "get",
    params: data
  });
}

// 获取科目列表
export function subjectList(data) {
  return request({
    url: "/accounting/subject/searchOriginSubject",
    method: "get",
    params: data
  });
}

// 结转新增
export function saveCarryover(data) {
  return request({
    url: "/accounting/carryover",
    method: "post",
    data: data
  });
}

// 结转详情
export function carryoverDetail(data) {
  return request({
    url: "/accounting/carryoverRule/list",
    method: "get",
    params: data
  });
}

//结转删除
export function deleteCarryover(carryoverId) {
  return request({
    url: "/accounting/carryover/" + carryoverId,
    method: "delete"
  });
}

// 结转编辑
export function editCarryover(data) {
  return request({
    url: "/accounting/carryover",
    method: "put",
    data: data
  });
}

// 结转 - 测算 -所有
export function carryoverCalculation(data) {
  return request({
    url: "/accounting/carryover/calculation/all",
    method: "get",
    params: data
  });
}

// 结转 - 测算 - 多选
export function carryoverCalculationCheck(data) {
  return request({
    url:
      "/accounting/carryover/calculation?bookId=" +
      data.bookId +
      "&carryoverIds= " +
      data.carryoverIds,
    method: "get",
    data: data
  });
}

// 结转销售成本查看百分比
export function saleOfPercentage(data) {
  return request({
    url: "/accounting/carryover/select/percentage",
    method: "get",
    params: data
  });
}

// 结转销售成本设置百分比
export function setSaleOfPercentage(data) {
  return request({
    url: "/accounting/carryover/salesCost/percentage",
    method: "put",
    params: data
  });
}

// 结转损益状态查询
export function selProfitLossOpen(data) {
  return request({
    url: "/accounting/carryover/select/profitLossOpen",
    method: "get",
    params: data
  });
}
// 结转损益设置状态
export function setProfitLoss(data) {
  return request({
    url: "/accounting/carryover/profitLossOpen",
    method: "put",
    params: data
  });
}

// 生成凭证
export function carryoverGenerate(data) {
  return request({
    url: "/accounting/carryover/generate/voucher",
    method: "post",
    params: data
  });
}

// 结账
export function carryoverCheckOut(data) {
  return request({
    url: "/accounting/carryover/nextPeriod",
    method: "post",
    params: data
  });
}

// 反结账
export function antiSettlement(data) {
  return request({
    url: "/accounting/carryover/backPeriod",
    method: "post",
    params: data
  });
}
// 获取当前账期凭证个数
export function voucherNumber(data) {
  return request({
    url: "/accounting/carryover/voucherNum",
    method: "get",
    params: data
  });
}
