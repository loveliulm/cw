import request from "@/utils/request";

// 查询服务类型
export function serviceTypeTree() {
  return request({
    url: "/system/service/type/tree",
    method: "get"
  });
}
// 获取服务项目
export function getServiceItems(data) {
  return request({
    url: "/system/service/item",
    method: "get",
    params: data
  });
}
