import request from '@/utils/request'

export function forgetPassword(username, password, phoneNumber, phoneCode, code, uuid) {
  return request({
    url: 'auth/forgetPassword',
    method: 'post',
    data: {
      username,
      password,
      phoneNumber,
      phoneCode,
      code,
      uuid
    }
  })
}

export function sendCode(data) {
  return request({
    url: 'auth/sendPhoneCode',
    method: 'post',
    data
  })
}

export function getCodeImg() {
  return request({
    url: 'auth/code',
    method: 'get'
  })
}
