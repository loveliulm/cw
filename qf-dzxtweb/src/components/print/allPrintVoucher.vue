<!--凭证批量打印-->
<template>
    <div id="allPrintVoucher" style="position: relative; left: -9999px;">
    <div class="printVoucher" 
        v-for="(a, b) in allPrintVoucher" 
        :key="b">
        <!--单张凭证打印模板-->
        <div class="voucher"
                v-for="(item, index) in a.tableData"
                :key="index">
            <!--主标题-->
            <div class="title">
                <div class="voucherName"
                        v-text="a.voucherHFdata.voucherTitle">
                </div>
                <div class="accessory">
                    <span v-text="'附件数：'+a.voucherHFdata.billNum"></span>
                </div>
            </div>
            <!--副标题-->
            <div class="subhead">
                <div class="companyName">
                    <span v-text="a.voucherHFdata.accbookName"></span>
                </div>
                <div class="date">
                    <span v-text="'日期：'+a.voucherHFdata.date"></span>
                </div>
                <div class="date">
                    <span v-text="'凭证号：'+a.voucherHFdata.voucherNumber+'('+(index+1)+'/'+a.tableData.length+')'"></span>
                </div>
            </div>
            <!--表格 有外币-->
            <table class="tableclass" v-if="a.hasCur">
                <thead>
                    <tr>
                        <td>摘要</td>
                        <td>科目</td>
                        <td>币别</td>
                        <td>汇率</td>
                        <td>原币</td>
                        <td>借方</td>
                        <td>贷方</td>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(item1, index1) in item"
                        :key="index1">
                        <td class="txtL"
                            v-text="item1.abstract"></td>
                        <td class="txtL"
                            v-text="item1.subject"></td>
                        <td class="txtL"
                            v-text="item1.currencyName"></td>
                        <td class="txtR"
                            v-text="item1.exchangeRate"></td>
                        <td class="txtR"
                            v-text="moneyChange.outputmoney(item1.deVal)"
                            style="width:150px"></td>
                        <td class="txtR"
                            v-text="moneyChange.outputmoney(item1.crVal)"
                            style="width:150px"></td>
                    </tr>
                    <tr>
                        <td colspan="5"
                            class="txtL"
                            v-text="'合计：' +toCapital(a.voucherHFdata.total)"></td>
                        <td class="txtR"
                            style="width:150px"
                            v-text="moneyChange.outputmoney(a.voucherHFdata.total)"></td>
                        <td class="txtR"
                            style="width:150px"
                            v-text="moneyChange.outputmoney(a.voucherHFdata.total)"></td>
                    </tr>
                </tbody>
            </table>
            <!--表格 没有外币-->
            <table class="tableclass" v-else>
                <thead>
                    <tr>
                        <td>摘要</td>
                        <td>科目</td>
                        <td>借方</td>
                        <td>贷方</td>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(item1, index1) in item"
                        :key="index1">
                        <td class="txtL"
                            v-text="item1.abstract"></td>
                        <td class="txtL"
                            v-text="item1.subject"></td>
                        <td class="txtR"
                            v-text="moneyChange.outputmoney(item1.deVal)"
                            style="width:150px"></td>
                        <td class="txtR"
                            v-text="moneyChange.outputmoney(item1.crVal)"
                            style="width:150px"></td>
                    </tr>
                    <tr> 
                        <td colspan="2"
                            class="txtL"
                            v-text="'合计:' + toCapital(a.voucherHFdata.total)"></td>
                        <td 
                            class="txtR"
                            v-text="moneyChange.outputmoney(a.voucherHFdata.total)"></td>
                        <td 
                            class="txtR"
                            v-text="moneyChange.outputmoney(a.voucherHFdata.total)"></td>
                        
                    </tr>
                </tbody>
            </table>
            <!--凭证页脚-->
            <div class="voucherFooter">
                <span v-text="'主管：'+a.voucherHFdata.supervisor"></span>
                <span v-text="'审核：'+a.voucherHFdata.cashier"></span>
                <span v-text="'出纳：'+a.voucherHFdata.auditor"></span>
                <span v-text="'制单：'+a.voucherHFdata.producer"></span>
            </div>
        </div>
    </div>
    </div>
</template>
<script>
export default {
    name: "printVoucher",
    props:["allData"],
    data() {
        return {
            allPrintVoucher: []
        };
    },
     methods: {
         printvoucher() {
             this.voucher5tr(
                 setTimeout(() => {
                     this.getPdf("#allPrintVoucher");
                 }, 1000)
             );
         },
         //每五列切成一张凭证
         voucher5tr() {
             this.allData.forEach((a, j) => {
                 //补齐每张凭证的5行
                 let tr5 = []; //[[{},{},{},{},{}]]多维
                 let tr = a.listData;
                 let index = 0;
                 tr.forEach((n, i) => {
                     if (!tr5[index]) {
                         tr5[index] = [];
                     }
                     tr5[index].push(n);
                     if (tr5[index].length === 5) {
                         index++;
                     }
                 });
                 let last = tr5[tr5.length -1];
                 for (let i =0, l= 5 - last.length; i < l; i++) {
                     last.push({
                         abstract: '',//摘要
                         showCur: false,
                         subject: '',//科目是否有辅助核算，有辅助核算，拼接上辅助核算
                         currencyName: '',//是否有外币，没有外币显示空，有外币匹配过滤出外币名称eg:RMB
                         exchangeRate:'',//汇率
                         original:'',//原币
                         crVal: '',//贷方
                         deVal: ''
                     });
                 }
                 //对象或者数组必须要申明
                 this.allPrintVoucher[j] = {};
                 this.allPrintVoucher[j].tableData = [];
                 this.allPrintVoucher[j].voucherHFdata = {};
                 this.allPrintVoucher[j].hasCur = ''
                 //赋值
                 this.allPrintVoucher[j].tableData = tr5;//5列一个凭证
                 this.allPrintVoucher[j].voucherHFdata =a.voucherHFdata;//每个凭证的头尾
                 this.allPrintVoucher[j].hasCur = a.hasCur;//每个凭证是否有外币
             });
             //映射  常规情况下我们在vue实例的data中设置响应数据。但当数据为对象，我们增加或删除对象属性值时，视图并不触发更新
             this.allPrintVoucher = [...this.allPrintVoucher];
         },
         //中文大写
         toCapital(n) {
            let fraction = ["角", "分"];
            let digit = ["零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"];
            let unit = [["元", "万", "亿"], ["", "拾", "佰", "仟"]];
            let head = n < 0 ? "欠" : "";
            n = Math.abs(n);
            let s = "";
            for (let i = 0, l = fraction.length; i < l; i++) {
                s += (
                    digit[Math.floor(n * 10000 * Math.pow(10, i) / 1000) % 10] +
                    fraction[i]
                ).replace(/零./, "");
            }
            s = s || "整";
            n = Math.floor(n);
            for(let i = 0, l = unit[0].length; i < l && n > 0; i++) {
                let p = "";
                for (let j = 0, jl = unit[1].length; j < jl && n > 0; j++) {
                    p = digit[n % 10] + unit[1][j] + p;
                    n = Math.floor(n / 10);
                }
                s = p.replace(/(零.)*零$/, "").replace(/^$/, "零") + unit[0][i] + s;
            }
            return (head + s.replace(/(零.)*零元/, "元").replace(/(零.)+/g, "零").replace(/^整$/, "零元整"))
         }
     }
}
</script>
<style lang="scss" scoped>
    #allPrintVoucher {
        width: 900px;
        margin: 0 auto;
        font-size: 13px;
        .printVoucher {
            width: 800px;
            margin: 0 auto;
            font-size: 13px;
            padding: 0 50px;
            .voucher {
                height: 600px;
                padding: 18px 0;
                .title {
                    text-align: center;
                    .voucherName {
                        display: inline-block;
                        line-height: 50px;
                        font-size: 25px;
                    }
                    .accessory {
                        display: inline-block;
                        box-sizing: border-box;
                        line-height: 50px;
                        float: right;
                    }
                }
                .subhead {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    margin: 30px auto;
                    .companyName {

                    }
                    .data {

                    }
                    .voucherNumber {
                        
                    }
                }
                .tableclass {
                    box-sizing: border-box;
                    thead > tr > td {
                        text-align: center;
                    }
                    width: 798px;
                    border-collapse: collapse;/*相邻边被合并;separate: 边框独立*/
                    border-left: solid #000 1px;
                    border-top: solid #000 1px;
                    td {
                        height: 50px;
                        padding: 5px 2px;
                        border-bottom: solid #000 1px;
                        border-right: solid #000 1px;
                    }
                    .txtL {
                        text-align: left;
                    }
                    .txtR {
                        text-align: right;
                    }
                }
                .voucherFooter {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    margin-top: 30px;
                }
            }
        }
    }
</style>