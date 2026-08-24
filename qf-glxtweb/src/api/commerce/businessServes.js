import request from '@/utils/request'

// 查询工商服务任务列表
export function listbusiness(id) {
  return request({
    url: '/commerce/step/list/' + id,
    method: 'get',
  })
}

// 工商服务任步骤完成
export function alterbusiness(qurey) {
  return request({
    url: '/commerce/step',
    method: 'put',
    data:qurey
  })
}
//工商服务完成撤销
export function deletebusiness(id) {
  return request({
    url: '/commerce/step/revocation/' + id,
    method: 'get',
  })
}

//获取附件
export function getStepFile(stepId) {
  return request({
    url: '/commerce/step/list/getStepFile/' + stepId,
    method: 'get',
  })
}

// 上传附件
export function uploadStepFile(params) {
  return request({
    url: `/commerce/step/list/file`,
    method: 'post',
    data: params
  })
}

// 附件预览
export function previewStepFile(params) {
  return request({
    url: `/commerce/step/list/file/preview`,
    responseType: 'blob',
    params
  })
}

//附件删除
export function deleteStepFile(id) {
  return request({
    url: '/commerce/step/list/deleteStepFile/' + id,
    method: 'get',
  })
}
