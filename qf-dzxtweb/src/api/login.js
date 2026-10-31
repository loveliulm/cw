import request from "@/utils/request";

// 获取用户详细信息
export function getInfo() {
  return request({
    url: "/getInfo",
    method: "get"
  });
}

// 退出方法
export function logout() {
  return request({
    url: "/logout",
    method: "post"
  });
}

// 获取账套信息
export function BookInfo(data) {
  return request({
    url: "/accounting/login/book/info",
    methods: "get",
    params: data
  });
}

// 查询代账服务列表
export function listBook(query) {
  return request({
    url: "/customer/book/list",
    method: "get",
    params: query
  });
}
