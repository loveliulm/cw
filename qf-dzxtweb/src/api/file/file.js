import request from "@/utils/request";

export function savePdf(file) {
    return request ({
        url: '/file/savePDF',
        method: 'post',
        params: file
    })
}