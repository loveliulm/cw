import request from "@/utils/request";
//import { praseStrEmpty } from "@/utils/ruoyi";

// 获取工资列表
export function wagesList(data) {
  return request({
    url: "/accounting/wage/list",
    method: "get",
    params: data
  });
}

// 保存工资
export function addWages(data) {
  return request({
    url: "/accounting/wage",
    method: "post",
    data: data
  });
}

// 修改工资
export function editWages(data) {
  return request({
    url: "/accounting/wage",
    method: "put",
    data: data
  });
}

// 删除工资
export function deleteWages(ids) {
  return request({
    url: "/accounting/wage/" + ids,
    method: "delete"
  });
}

// 工资详情
export function wagesDetail(id) {
  return request({
    url: "/accounting/wage/" + id,
    method: "get"
  });
}

// 计提工资
export function wagesProvision(data) {
  return request({
    url: "/accounting/wage/provision",
    method: "post",
    params: data
  });
}

// 发放工资
export function wagesGrant(data) {
  return request({
    url: "/accounting/wage/grant",
    method: "post",
    params: data
  });
}

// 应发工资合计、实发工资合计
export function pgWages(data) {
  return request({
    url: "/accounting/wage/getWagesTotalPay",
    method: "get",
    params: data
  });
}

// 计提工资凭证模板、发放工资凭证模板是否生成
export function modelGenerate(data) {
  return request({
    url: "/accounting/wage/checkWages",
    method: "get",
    params: data
  });
}
