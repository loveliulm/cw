import request from "@/utils/request";
import { praseStrEmpty } from "@/utils/ruoyi";

export const SUPPLIER = 1; // 供应商 vendors
export const CUSTOMER = 2; // 客户 customers
export const PROJECT = 3; // 项目 auxitems
export const DEPARTMENT = 4; // 部门 departments
export const PERSON = 5; // 职员 personnels
export const STOCK = 6; // 库存

// 获取辅助核算类型
export function auxiliaryType(query) {
  return request({
    url: "/accounting/auxiliaryType/list",
    method: "get",
    params: query
  });
}

// 获取辅助核算列表
export function auxiliaryList(data) {
  return request({
    url: "/accounting/auxiliary/list",
    method: "get",
    params: data
  });
}

// 新增获取编号
export function auxiliaryCode(data) {
  return request({
    url: "/accounting/auxiliary/getCode",
    method: "get",
    params: data
  });
}

// 新增辅助核算
export function addAuxiliary(data) {
  return request({
    url: "/accounting/auxiliary",
    method: "post",
    data: data
  });
}

// 修改科目
export function updateAuxiliary(data) {
  return request({
    url: "/accounting/auxiliary",
    method: "put",
    data: data
  });
}

// 删除用户
export function delAuxiliary(auxiliaryIds) {
  return request({
    url: "/accounting/auxiliary/" + auxiliaryIds,
    method: "delete"
  });
}

// 获取辅助核算详情
export function auxiliaryDetail(auxiliaryId) {
  return request({
    url: "/accounting/auxiliary/" + auxiliaryId,
    method: "get",
  });
}
// 筛选拼接辅助核算数据
export function getAssistAList(arr) {
  let vendors = [];
  let customers = [];
  let auxitems = [];
  let departments = [];
  let personnels = [];
  let assistAList = {};
  let array = arr;
  array.forEach(item => {
    if (item.auxiliaryTypeId == SUPPLIER) {
      vendors.push(item);
    } else if (item.auxiliaryTypeId == CUSTOMER) {
      customers.push(item);
    } else if (item.auxiliaryTypeId == PROJECT) {
      auxitems.push(item);
    } else if (item.auxiliaryTypeId == DEPARTMENT) {
      departments.push(item);
    } else if (item.auxiliaryTypeId == PERSON) {
      personnels.push(item);
    }
  });
  assistAList = { vendors, customers, auxitems, departments, personnels };
  return assistAList;
}
