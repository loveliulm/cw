import request from "@/utils/request";
import { praseStrEmpty } from "@/utils/ruoyi";

// 查询科目类型
export function subjectType(query) {
  return request({
    url: "/accounting/subjectType/list",
    method: "get",
    params: query
  });
}

// 获取科目
export function subjectList(data) {
  return request({
    url: "/accounting/subject/list",
    method: "get",
    params: data
  });
}

// 获取科目详情
export function subjectDetail(subjectId) {
  return request({
    url: "/accounting/subject/" + praseStrEmpty(subjectId),
    method: "get"
  });
}

// 获取科目类别
export function subjectCategory(query) {
  return request({
    url: "/accounting/subjectCategory/list",
    method: "get",
    params: query
  });
}

// 新增获取科目编码
export function subjectCode(data) {
  return request({
    url: "/accounting/subject/subjectCode",
    method: "get",
    params: data
  });
}

// 新增科目
export function addSubject(data) {
  return request({
    url: "/accounting/subject",
    method: "post",
    data: data
  });
}

// 修改科目
export function updateSubject(data) {
  return request({
    url: "/accounting/subject",
    method: "put",
    data: data
  });
}

// 科目新增校验
export function checkSubject(data) {
  debugger;
  return request({
    url: "/accounting/subject/add/check",
    method: "post",
    data: data
  });
}

// 删除用户
export function delSubject(subjectId) {
  return request({
    url: "/accounting/subject/" + subjectId,
    method: "delete"
  });
}

// 修改期初余额
export function editbalanceInit(params) {
  return request({
    url: "/accounting/subject/balance",
    method: "put",
    params: params
  });
}

// 修改期初余额、本年累计-借方金额、本年累计-贷方金额
export function editBalance(params) {
  return request({
    url: "/accounting/subject/balance",
    method: "put",
    params: params
  });
}

// 新增辅助核算保存
export function saveAuxiliary(data) {
  return request({
    url: "/accounting/subjectAuxiliary",
    method: "post",
    data: data
  });
}

// 辅助核算余额查询
export function queryAuxBalance(params) {
  return request({
    url: "/accounting/subjectAuxiliary/list",
    method: "get",
    params: params
  });
}

// 编辑辅助核算
export function editAuxiliary(data) {
  return request({
    url: "/accounting/subjectAuxiliary",
    method: "put",
    data: data
  });
}

// 试算平衡
export function trialBalance(data) {
  return request({
    url: "/accounting/subject/trialBalance",
    method: "get",
    params: data
  });
}

// 期初余额、本年累计借方、本年累计贷方是否可以修改
export function isEdit(data) {
  return request({
    url: "/accounting/subject/subjectBalanceIsEdit",
    method: "get",
    params: data
  });
}
