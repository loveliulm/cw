import request from "@/utils/request";

// 获取数量金额总账列表
export function numTotalList(data) {
  return request({
    url: "/accounting/quantityTotal/getList",
    method: "get",
    params: data
  });
}

// 导出数量金额总账
export function exportNumberTotalAccount(data) {
  return request({
    url: "/accounting/quantityTotal/export",
    method: "get",
    params: data
  });
}
