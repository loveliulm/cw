import request from "@/utils/request";
import { praseStrEmpty } from "@/utils/ruoyi";

// 查询固定资产默认科目
export function assetConfig(data) {
  return request({
    url: "/accounting/config/list",
    method: "get",
    params: data
  });
}

// 固定资产卡片-获取编码
export function assetCode(data) {
  return request({
    url: "/accounting/fixedAssetCard/getCode",
    method: "get",
    params: data
  });
}

// 保存固定资产卡片
export function saveAsset(data) {
  return request({
    url: "/accounting/fixedAssetCard",
    method: "post",
    data: data
  });
}

// 固定资产卡片详情
export function assetDetail(cardId) {
  return request({
    url: "/accounting/fixedAssetCard/" + cardId,
    method: "get"
  });
}

// 修改固定资产卡片
export function editAsset(data) {
  return request({
    url: "/accounting/fixedAssetCard",
    method: "put",
    data: data
  });
}

// 卡片清理
export function fixedAssetCardClean(data) {
  return request({
    url: "/accounting/fixedAssetCard/clean",
    method: "put",
    params: data
  });
}
