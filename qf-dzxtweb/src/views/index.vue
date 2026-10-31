<template>
  <div class="app-main">
    <div class="dashboard-editor-container">
      <div class="container-wrap">

        <div class="container-top">
          <el-row :span="24" :gutter="20">
            <el-col :span="14">
              <div class="h-panel" style="height: 210px">
                <div class="h-panel-bar">
                  <div class="h-panel-title" style="font-size: 17px; color: black; font-weight: bold;">常用功能</div>
                </div>
                <el-row :span="24" style="margin: 10px">
                  <el-col :span="4">
                    <div class="first-one" @click="goToReport('accountBalance')">
                      <img src="../assets/image/index/余额表.png" class="first-one-img"/>
                      <p>余额表</p>
                    </div>
                  </el-col>
                  <el-col :span="4">
                    <div class="first-one" @click="goToReport('detailAcc')">
                      <img src="../assets/image/index/明细账.png" class="first-one-img"/>
                      <p>明细账</p>
                    </div>
                  </el-col>
                  <el-col :span="4">
                    <div class="first-one" @click="goToReport('assetsReport')">
                      <img src="../assets/image/index/资产负债表.png" class="first-one-img"/>
                      <p>资产负债表</p>
                    </div>
                  </el-col>
                  <el-col :span="4">
                    <div class="first-one" @click="goToReport('cashFlowReport')">
                      <img src="../assets/image/index/现金流量表.png" class="first-one-img"/>
                      <p>现金流量表</p>
                    </div>
                  </el-col>
                  <el-col :span="4">
                    <div class="first-one" @click="goToReport('profit')">
                      <img src="../assets/image/index/利润表.png" class="first-one-img"/>
                      <p>利润表</p>
                    </div>
                  </el-col>
                  <el-col :span="4">
                    <div class="first-one" @click="goToReport('auxiliaryTotalAcc')">
                      <img src="../assets/image/index/辅助核算余额表.png" class="first-one-img"/>
                      <p>辅助核算余额表</p>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </el-col>
            <el-col :span="5">
              <div class="h-panel" style="height: 210px">
                <div class="h-panel-bar">
                  <div class="h-panel-title" style="font-size: 17px; color: black; font-weight: bold;">凭证</div>
                  <div class="h-panel-title" style="font-size: 10px;">（{{book.currentPeriodData.str_before?book.currentPeriodData.str_before:currentPeriodData.str_before}}/{{book.currentPeriodData.str_after?book.currentPeriodData.str_after:currentPeriodData.str_after}}）</div>
                </div>
                <div style="text-align: center; margin-top: 10px">
                  <el-row :span="24" style="margin: 0 30px">
                    <el-col :span="12">
                      <span style="font-size: 14px; color: #666666">总数</span>
                      <p style="font-size: 20px; color: #666666">{{ voucherCount }}<b style="font-size: 12px; color: #666666">张</b></p>
                    </el-col>
                    <el-col :span="12">
                      <span style="font-size: 14px; color: #666666">未审核</span>
                      <p style="font-size: 20px; color: #f86549">{{ unVoucherCount }}<b style="font-size: 12px; color: #666666">张</b></p>
                    </el-col>
                    <div>
                      <el-button @click="goToReport('Voucher')" style="background-color: #4265d6; color: white; border-radius: 18px; width: 90%; height: 35px">新增凭证</el-button>
                    </div>
                    <div>
                      <el-button @click="goToReport('ViewVoucher')" style="color: #4265d6; border-color: #4265d6; border-radius: 18px; width: 90%; height: 35px; margin-top: 5px">查看凭证</el-button>
                    </div>
                  </el-row>
                </div>
              </div>
            </el-col>
            <el-col :span="5">
              <div class="h-panel" style="height: 210px">
                <div class="h-panel-bar">
                  <div class="h-panel-title" style="font-size: 17px; color: black; font-weight: bold;">发票管理</div>
                </div>
                <div>
                  <el-row :span="24" style="margin: 0 10px">
                    <el-col :span="12" style="padding: 12px">
                      <div style="margin-top: 5px">
                        <div class="home-show-invoice">
                          <img src="../assets/image/index/进项发票.png" align="center">
                          <span style="font-size: 14px; color: #666666">进项发票</span>
                        </div>
                        <p class="show-invoice-p">{{ this.invoiceDataInfo.inputInvoice }}<b style="font-size: 12px; color: #666666">张</b></p>
                        <div class="show-invoice-footer">
                          <span style="float: left; color: #666666">合计</span>
                          <span style="float: right; font-weight: bold">{{ this.invoiceDataInfo.inputInvoiceT }}</span>
                        </div>
                      </div>
                    </el-col>
                    <el-col :span="12" style="padding: 12px">
                      <div style="margin-top: 5px">
                        <div class="home-show-invoice">
                          <img src="../assets/image/index/销项发票.png">
                          <span style="font-size: 14px; color: #666666">销项发票</span>
                        </div>
                        <p class="show-invoice-p">{{ this.invoiceDataInfo.outputInvoice }}<b style="font-size: 12px; color: #666666">张</b></p>
                        <div class="show-invoice-footer">
                          <span style="float: left; color: #666666">合计</span>
                          <span style="float: right; font-weight: bold">{{ this.invoiceDataInfo.outputInvoiceT }}</span>
                        </div>
                      </div>
                    </el-col>
                  </el-row>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>

        <div class="h-panel" style="margin-top: 20px">
          <el-row :span="24" class="amount-row">
            <el-col :span="3" v-for="(item, index) in financeList" :key="index">
              <div :class="'home-show-image bg-light' + index">
                <span class="panel-title">{{ item.subjectName }}</span>
                <p :class="item.balance < 0 ? 'panel-amount fcolor' : 'panel-amount'">{{ item.balance }}</p>
              </div>
            </el-col>
          </el-row>
        </div>

        <div class="container-footer">
          <div class="container-footer-left">
            <div class="h-panel">
              <div class="h-panel-bar">
                <div class="h-panel-title" style="font-size: 17px; color: black; font-weight: bold;">营收统计({{ lineTitle }})</div>
              </div>
              <div style="width:99%;padding-top:10px;background-color:#fff;border-box:box-sizing;">
                <line-chart :chart-data="taxsData" :className="lineTitle" :height="'320px'"/>
              </div>
            </div>
          </div>
          <div class="container-footer-right">
            <div class="h-panel">
              <div class="h-panel-bar">
                <div class="h-panel-title" style="font-size: 17px; color: black; font-weight: bold;">费用分析</div>
              </div>
              <div style="width:100%;padding-top:10px;background-color:#fff;border-box:box-sizing;">
                <pie-charts :chart-data="costData" :height="'320px'"/>
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
  import LineChart from './dashboard/LineChart'
  import PieCharts from './dashboard/PieCharts'
  import {getInfo, BookInfo} from "@/api/login";
  import {setToken} from "@/utils/auth";
  import {voucherList, latestVouchers, financeTarget, taxs} from "@/api/index";
  import {currentPeriod} from "@/api/voucher/viewvoucher";
  import {mapState} from "vuex";

  export default {
    name: 'Index',
    components: {
      LineChart,
      PieCharts,
    },
    data() {
      return {
        // 凭证数量
        voucherCount: 0,
        unVoucherCount: 0,
        // 发票管理
        invoiceDataInfo: {
          inputInvoice: '0',
          inputInvoiceT: '0.00',
          outputInvoice: '0',
          outputInvoiceT: '0.00',
        },
        // 当前账期
        yearPeriod: null,
        // 财务指标
        financeList: [],
        // 税收折线图数据
        taxsData: {},
        // 费用分析数据
        costData: [{name: '销售费用', value: 0}, {name: '管理费用', value: 0}, {name: '财务费用', value: 0}],
        // 折线图标题
        lineTitle: '',
        bookId: null,
        currentPeriodData: ''
      }
    },
    created() {
      this.getParams();
      //this._getInfo();
      this.initData();
      this.getBookInfo();
    },
    computed: {
      ...mapState([
        // 映射 this.name 为 store.state.name
        'user',
        'book'
      ]),
    },
    methods: {
      // 获取bookId并同步缓存到vuex
      async getParams() {
        let bookId = await new Promise((resolve) => {
          let bookId = this.getUrlBookId();
          this.bookId = bookId;
          resolve(bookId);
          //reslove(this.getUrlParams().bookId)
        });
        // 不使用缓存的原因：需要打开多个账套，前一个账套的bookId会被后打开的bookId覆盖，导致数据不正确
        // if (Object.prototype.toString.call(bookId) !== '[object Undefined]') {
        //   this.setVuexBookId(bookId);
        //   this.localStorage.setItem('bookId', bookId)
        // } else {
        //   let bookId = this.localStorage.getItem('bookId');
        //   this.setVuexBookId(bookId)
        // }
      },
      // 获取url携带的参数
      // getUrlParams () {
      //   let query = window.location.search.substring(1);
      //   let values = query.split('&');
      //   let params = {};
      //   values.forEach(item => {
      //     let pos = item.indexOf('=');
      //     if (pos == -1) return true;
      //     let paramsname = item.substring(0, pos);
      //     let value = item.substring(pos + 1);
      //     params[paramsname] = value;
      //   })
      //   return params;
      // },
      // // 获取bookId
      // getUrlBookId(){
      //   let query = window.location.href;
      //   let index1 = this.findStringIndex(query, '/', 2) + 1;
      //   let index2 = this.findStringIndex(query, '/', 3) - 1;
      //   let bookId = query.substring(index1,index2);
      //   return bookId;
      // },
      // vuex缓存bookId
      // setVuexBookId (bookId) {
      //   this.$store.commit('SET_BOOKID', bookId);
      // },
      // 获取用户信息
      _getInfo() {
        return new Promise((resolve, reject) => {
          getInfo()
            .then(res => {
              const user = res.user;
              if (res.roles && res.roles.length > 0) {
                // 验证返回的roles是否是一个非空数组
                this.$store.commit("SET_ROLES", res.roles);
                this.$store.commit("SET_PERMISSIONS", res.permissions);
              } else {
                this.$store.commit("SET_ROLES", ["ROLE_DEFAULT"]);
              }
              this.$store.commit("SET_NAME", user.userName);
              // 用户信息 - deptId
              this.$store.commit("SET_DEPTID", user.dept.deptId);
              this.$store.commit("SET_NICKNAME", user.dept.deptName);
              this.$store.commit("SET_TOKEN", res.token);
              setToken(res.token);

              resolve(res);
            })
            .catch(error => {
              reject(error);
            });
        });
      },
      // 获取当前账套信息
      getBookInfo(bokId) {
        let bookId = this.bookId;//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId
        BookInfo({bookId}).then(res => {
          let data = res.data;

          this.customerName = res.data.customerName;
          this.currentPeriodData = this.getStr(res.data.currentPeriod, '-');

          this.$store.commit('SET_CURRENTPERIOD', data.currentPeriod);
          this.localStorage.setItem('currentPeriod', data.currentPeriod);
          this.$store.commit('SET_CURRENTPERIODDATA', this.getStr(data.currentPeriod, '-'));
          this.localStorage.setItem('currentPeriodData', this.getStr(data.currentPeriod, '-'));
          this.$store.commit('SET_CUSTOMERNAME', data.customerName);
          this.localStorage.setItem('customerName', data.customerName);
          this.$store.commit('SET_ACCOUNTTEMPLATEID', data.accountTemplateId);
          this.localStorage.setItem('accountTemplateId', data.accountTemplateId);
          this.$store.commit('SET_ACCOUNTTEMPLATENAME', data.accountTemplateName);
          this.localStorage.setItem('accountTemplateName', data.accountTemplateName);
        })
      },
      async initData() {
        let param = await this.getParams();
        let result = await this.getCurrentPeriod();
        //this.getReportBalance(result.yearPeriod);
        this.getBookInfo(param);
        this.getVoucherCount(result.yearPeriod);
        this.getFinance(result.yearPeriod);
        this.getTaxslist(result.yearPeriod);
      },
      //获取当前期间
      getCurrentPeriod() {
        let bookId = this.bookId;//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId;
        return new Promise(resolve => {
          currentPeriod({bookId}).then(res => {
            this.yearPeriod = res.data.yearPeriod;
            this.lineTitle = res.data.year + '年应交税金情况';
            resolve(res.data);
          })
        })
      },

      // 报表跳转
      goToReport(str) {
        switch (str) {
          case 'balancesheet':
            this.$router.push('/accBook/accountBalance');
            break;
          case 'vouchertotal':
            this.$router.push('/accBook/vouchTotal');
            break;
          case 'assets':
            this.$router.push('/report/assetsReport');
            break;
          case 'Voucher':
            this.$router.push('/voucher');
            break;
          case 'ViewVoucher':
            this.$router.push('/voucher/viewvoucher');
            break;
          case 'accountBalance':
            this.$router.push('/accBook/accountBalance');
            break;
          case 'detailAcc':
            this.$router.push('/accBook/detailAcc');
            break;
          case 'assetsReport':
            this.$router.push('/report/assetsReport');
            break;
          case 'cashFlowReport':
            this.$router.push('/report/cashFlow');
            break;
          case 'profit':
            this.$router.push('/report/profit');
            break;
          case 'auxiliaryTotalAcc':
            this.$router.push('/accBook/auxiliaryTotalAcc');
            break;
        }
      },

      // 获取凭证数据
      getVoucherCount(yearPeriod) {
        let bookId = this.bookId;//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId;
        // latestVouchers({bookId, yearPeriod}).then(res => {
        // })
        voucherList({bookId}).then(res => {
          res.rows.forEach(item => {
            if (item.status === 0) {
              this.unVoucherCount += 1;
            }
          })
          this.voucherCount = res.total;
        })

      },

      // 获取本期财务指标
      getFinance(yearPeriod) {
        let bookId = this.bookId;//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId;
        financeTarget({bookId, yearPeriod}).then(res => {
          this.financeList = res.rows;

          this.costData.map(item => {
            this.financeList.forEach(info => {
              if (info.subjectName === item.name) {
                item.value = info.balance;
              }
            })
          })
        })

      },
      // 刷新本期财务指标数据
      financeRefresh() {
        this.getFinance(this.yearPeriod);
      },
      // 获取税收折现图图表数据
      getTaxslist(yearPeriod) {
        let bookId = this.bookId;//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId;
        taxs({bookId, yearPeriod}).then(res => {
          this.taxsData = this.lineData(res.rows);
        })
      },
      // 折现表数据处理
      lineData(arr) {
        let list = arr;
        let taxsData = {
          taxVat: {name: '增值税', type: 'line', data: []}, taxBuilding: {name: '城市维护建设税', type: 'line', data: []},
          taxEducation: {name: '教育附加税', type: 'line', data: []}, taxPersonal: {name: '个人所得税', type: 'line', data: []},
          taxCorporate: {name: '企业所得税', type: 'line', data: []}
        };
        list.forEach((item) => {
          taxsData.taxVat.data.push(item.taxVat);
          taxsData.taxBuilding.data.push(item.taxBuilding);
          taxsData.taxEducation.data.push(item.taxEducation);
          taxsData.taxPersonal.data.push(item.taxPersonal);
          taxsData.taxCorporate.data.push(item.taxCorporate);
        });
        return taxsData;
      },
      // 分割字符串
      getStr(string, str) {
        let str_before = string.split(str)[0];
        let str_after = string.split(str)[1];
        return {str_before, str_after};
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "~@/assets/styles/variables.scss";

  .app-main {
    background-color: rgb(240, 242, 245);

    .dashboard-editor-container {
      padding: 20px;
      height: 100%;
      //position: relative;
      .container-wrap {
        margin: auto;

        .container-top,
        .container-footer {
          margin: auto;
        }

        .h-panel {
          border: none;
          background: #fff;
          border-radius: 2px;
          position: relative;
          border: 1px solid #eee;
        }

        .h-panel-bar {
          border-top: 0 solid #eee;
          padding: 10px 20px;
          border-bottom: 1px solid #eee;
          position: relative;
        }

        .h-panel-title {
          line-height: 35px;
          font-size: 18px;
          display: inline-block;
          vertical-align: middle;
        }

        .first-one {
          cursor: pointer;
          text-align: center;
          padding: 12px;

          &:hover {
            color: #f60 !important;
          }

          p {
            font-size: 14px;
          }
        }

        p {
          margin: 8px 0;
        }

        .first-one-img {
          margin: auto;
        }

        .home-show-invoice {
          text-align: center;
          img {
            height: 18px;
            display: inline;
            vertical-align: sub;
          }
        }

        .show-invoice-p {
          font-size: 20px;
          color: #666666;
          text-align: center;
          margin-top: 15px;
        }

        .show-invoice-footer {
          font-size: 14px;
          background-color: #f5f5f5;
          border-radius: 4px;
          height: 20px;
          margin-top: 25px;
          padding: 6px 10px
        }

        .home-show-image.bg-light0 {
          background-image: url("../assets/image/index/库存现金.png");
        }
        .home-show-image.bg-light1 {
          background-image: url("../assets/image/index/银行存款.png");
        }
        .home-show-image.bg-light2 {
          background-image: url("../assets/image/index/应收账款.png");
        }
        .home-show-image.bg-light3 {
          background-image: url("../assets/image/index/应付账款.png");
        }
        .home-show-image.bg-light4 {
          background-image: url("../assets/image/index/库存现金.png");
        }
        .home-show-image.bg-light5 {
          background-image: url("../assets/image/index/银行存款.png");
        }
        .home-show-image.bg-light6 {
          background-image: url("../assets/image/index/应收账款.png");
        }

        .home-show-image {
          width: 90%;
          /*height: 140px;*/
          display: inline-block;
          border-radius: 4px;
          box-shadow: 0px 1px 8px 0px rgba(196, 196, 196, 0.5);
          box-sizing: border-box;
          padding: 24px 10px;
          background-size: 40%;
          background-position: 90% 50%;
          background-repeat: no-repeat;
          cursor: pointer;
          transition: all 0.3s;
          margin: 0 20px;
        }

        .panel-title {
          font-size: 16px;
          color: #999;
        }

        .panel-amount {
          font-size: 22px;
          color: #999;
          margin-top: 20px;
        }

        .fcolor {
          color: #f00 !important;
        }

        .home-show-image:hover {
          transform: scale(1.08);
        }

        .amount-row {
          .el-col {
            /*width: 14.28571429%;*/
            width: 14.1571428%;
            padding: 20px 0;
          }
        }

        .container-top {

        }

        .container-footer {
          display: flex;
          flex-flow: row wrap;
          justify-content: space-between;
          align-items: center;
          margin-top: 20px;

          .container-footer-left {
            width: 63.6%;
            min-height: 320px !important;
          }

          .container-footer-right {
            width: 35%;
            min-height: 320px;
          }
        }

        .chart-wrapper {
          background: #fff;
          padding: 16px 16px 0;
          margin-bottom: 32px;
        }
      }
    }
  }

</style>
