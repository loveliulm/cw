import request from "@/utils/request";

// 删除资产类别
export function delAssetCategory(categoryIds) {
  return request({
    url: "/accounting/fixedAssertCategroy/" + categoryIds,
    method: "delete"
  });
}

// 资产类别获取编号
export function AssetCategoryCode(data) {
  return request({
    url: "/accounting/fixedAssertCategroy/getCode",
    method: "get",
    params: data
  });
}

// 新增资产类别
export function addAssetCategory(data) {
  return request({
    url: "/accounting/fixedAssertCategroy",
    method: "post",
    data: data
  });
}

// 修改资产类别
export function editAssetCategory(data) {
  return request({
    url: "/accounting/fixedAssertCategroy",
    method: "put",
    data: data
  });
}

// 资产类别详情
export function assetCategoryDetail(categoryId) {
  return request({
    url: "/accounting/fixedAssertCategroy/" + categoryId,
    method: "get"
  });
}
