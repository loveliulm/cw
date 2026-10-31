import request from "@/utils/request";
//import { praseStrEmpty } from "@/utils/ruoyi";

// 获取工资列表
export function wagesModelList(data) {
  return request({
    url: "/accounting/voucherModel/wage/list",
    method: "get",
    params: data
  });
}
// 获取工资凭证模板详情
export function wagesModelDetail(modelId) {
  return request({
    url: "/accounting/voucherModel/wage/" + modelId,
    method: "get"
  });
}

// 保存工资凭证模板
export function saveWagesModel(data) {
  return request({
    url: "/accounting/voucherModel/wage",
    method: "put",
    data: data
  });
}
