import request from "@/utils/request";
import { encrypt } from '@/utils/entode';

// 登录方法
export function login(phonenumber, password, code, uuid) {
  const data = {
    phonenumber,
    password,
    code,
    uuid
  };
  return request({
    url: "/login",
    method: "post",
    data: encrypt(JSON.stringify(data))
    //data: data
  });
}

// 手机验证码登录方法
export function phoneLogin(phonenumber, phonecode) {
  const data = {
    phonenumber,
    phonecode,
  };
  return request({
    url: "/phoneLogin",
    method: "post",
    data: encrypt(JSON.stringify(data))
    //data: data
  });
}

// 手机号登录发送验证码
export function sendSmsCodeL(data) {
  return request({
    url: '/sendSmsCodeL',
    method: 'post',
    params: data
  })
}

// 注册发送验证码
export function sendSmsCodeR(data) {
  return request({
    url: '/sendSmsCodeR',
    method: 'post',
    params: data
  })
}

// 注册方法
export function register(data) {
  return request({
    url: "/register",
    method: "post",
    data: encrypt(JSON.stringify(data))
    //data: data
  });
}

// 修改密码发送验证码
export function sendSmsCodeU(data) {
  return request({
    url: '/sendSmsCodeU',
    method: 'post',
    params: data
  })
}

// 修改密码
export function updatePassword(data) {
  return request({
    url: "/updatePassword",
    method: "post",
    data: encrypt(JSON.stringify(data))
    //data: data
  });
}

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

// 获取验证码
export function getCodeImg() {
  return request({
    url: "/captchaImage",
    method: "get"
  });
}
// 首页折线图
export function zheList() {
  return request({
    url: "customer/customer/getReportData",
    method: "get"
  });
}
//首页工商服务

export function btecList(id) {
  return request({
    url: "commerce/commerce/getIndexData?type="+id,
    method: "get"
  });
}
// 首页代账服务
export function payList(id) {
  return request({
    url: "customer/book/getIndexData?type="+id,
    method: "get",

  });
}

// 首页代账时间 账套
export function zhangList() {
  return request({
    url: "/getDeptMessage",
    method: "get"
  });
}
