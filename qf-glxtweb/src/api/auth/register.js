import request from '@/utils/request'

export function register(username, password, phoneNumber, phoneCode, code, uuid) {
  return request({
    url: 'auth/register',
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
