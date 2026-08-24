import request from "@/utils/request";

// 查询代账服务列表
export function listBook(query) {
  return request({
    url: "/customer/book/list",
    method: "get",
    params: query
  });
}

// 查询代账服务详细
export function getBook(bookId) {
  return request({
    url: "/system/book/" + bookId,
    method: "get"
  });
}

// 新增代账服务
export function addBook(data) {
  return request({
    url: "/customer/book/createAccount",
    method: "put",
    data: data
  });
}

// 修改代账服务
export function updateBook(data) {
  return request({
    url: "/system/book",
    method: "put",
    data: data
  });
}

// 删除代账服务
export function delBook(bookId) {
  return request({
    url: "/system/book/" + bookId,
    method: "delete"
  });
}

// 导出代账服务
export function exportBook(query) {
  return request({
    url: "/system/book/export",
    method: "get",
    params: query
  });
}

// 获取会计制度
export function listAccountSystem() {
  return request({
    url: "/customer/book/account/template",
    method: "get"
  });
}
//报税年份请求
export function listtaxValue(data) {
  return request({
    url: "customer/declaration/getTaxByYear",
    method: "get",
    params:data
  });
}
//新增报税
export function taxReturn(data) {
  return request({
    url: "/customer/declaration",
    method: "post",
    data:data
  });
}
//获取报税详情
export function acquireDetails(data) {
  return request({
    url: "/customer/declaration/getDetail",
    method: "get",
    params:data
  });
}
// 报税修改
export function modiFication(data) {
  return request({
    url: "/customer/declaration",
    method: "put",
    data:data
  });
}
//停止服务
export function stopService(id){
  return request({
    url:'/customer/book/stopService/' + id,
    method:'get',
  })
}
//恢复服务
export function resumeService(id){
  return request({
    url:'/customer/book/restoreService/' + id,
    method:'get',
  })
}