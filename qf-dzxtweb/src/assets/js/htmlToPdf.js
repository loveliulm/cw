//导出页面为pdf格式
import html2Canvas from 'html2canvas'
import JsPDF from 'jspdf'
import request from "@/utils/request"
import { savePdf } from '@/api/file/file'
import qs from 'qs'
import axios from 'axios'
import { getToken } from '@/utils/auth'
export default {
    install(Vue, options) {
        Vue.prototype.getPdf = function (dom) {
            // debugger;
            let e = document.querySelector(dom)
            let width = e.offsetWidth//获取容器宽度
            let height = e.offsetHeight//获取容器高度
            var c = document.createElement("canvas")
            var scale = 2 
            c.width = width * scale //缩放2倍
            c.height = height* scale 
            c.style.width = width + 'px'
            c.style.height = height + 'px'
            c.getContext("2d").scale(scale, scale)
            var opts = {
                // scale: scale,
                canvas: c,
                logging: false,
                useCORS: true,
                // width: width,
                // height: height,
            };
            html2Canvas(document.querySelector(dom), opts
                // allowTaint: true//允许被污染，可以将跨域的图片的html转为canvas
           ).then(function (canvas) {
                let contentWidth = canvas.width
                let contentHeight = canvas.height
                //一页pdf显示html页面生成的canvas高度; 
                let pageHeight = contentWidth / 595.28 * 841.89
                //未生成pdf的html页面高度
                let leftHeight = contentHeight
                //页面偏移
                let position = 0
                //a4纸的尺寸[595.28,841.89]，html页面生成的canvas在pdf中图片的宽高
                let imgWidth = 595.28
                let imgHeight = 595.28 / contentWidth * contentHeight
                let pageData = canvas.toDataURL('image/jpeg', 1.0)
                // 三个参数，第一个方向，第二个尺寸，第三个尺寸格式
                let PDF = new JsPDF('', 'pt', 'a4')
                //有两个高度需要区分，一个是html页面的实际高度，和生成pdf的页面高度(841.89)
                //当内容未超过pdf一页显示的范围，无需分页 
                if(leftHeight < pageHeight) {
                    PDF.addImage(pageData, 'JPEG', 0, 0, imgWidth, imgHeight)
                } else {
                    while (leftHeight > 0) {
                        PDF.addImage(pageData, 'JPEG', 0, position, imgWidth, imgHeight)
                        leftHeight -= pageHeight
                        position -= 841.49
                        //避免添加空白页 
                        if(leftHeight > 0) {
                            PDF.addPage()
                        }
                    }
                }
                // savePdf({file:qs.stringify({file:PDF.output('datauristring')})}).then(function(res) {
                    
                //     window.open(process.env.VUE_APP_BASE_API + '/file/showPDF/' + res.data)
                // });
                let instance = axios.create({
                    timeout: 10000,
                    headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8','Authorization': 'Bearer ' + getToken()}
                });

                instance.post(process.env.VUE_APP_BASE_API + '/file/savePDF', qs.stringify({
                    file:PDF.output('datauristring')//output输出文件流，PDF.output('datauristring')输出base64文件流
                })).then(function(res) {
                    var newWindow = window.open()
                    let url = process.env.VUE_APP_BASE_API + '/file/showPDF/' + res.data.data
                    // window.open(process.env.VUE_APP_BASE_API + '/file/showPDF/' + res.data.data)
                    newWindow.location.href = url
                }).catch(function (error) {

                });
            })
        }
    }
}