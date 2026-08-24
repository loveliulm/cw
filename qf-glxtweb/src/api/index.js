import request from "@/utils/request";

// 获取首页报表-四个金额
export function getIndexdMoneny(data) {
  return request({
    url: "/accounting/firstPage/report/getInfo",
    method: "get",
    params: data
  });
}
