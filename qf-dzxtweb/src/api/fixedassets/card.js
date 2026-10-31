import request from "@/utils/request";
//import { praseStrEmpty } from "@/utils/ruoyi";

// 获取固定资产类别
export function fixedAssertCategroy(data) {
  return request({
    url: "/accounting/fixedAssertCategroy/list",
    method: "get",
    params: data
  });
}

// 获取固定资产卡片列表
export function fixedAssetCard(data) {
  return request({
    url: "/accounting/fixedAssetCard/list",
    method: "get",
    params: data
  });
}

// 删除固定资产卡片
export function delAsset(cardIds) {
  return request({
    url: "/accounting/fixedAssetCard/" + cardIds,
    method: "delete"
  });
}
