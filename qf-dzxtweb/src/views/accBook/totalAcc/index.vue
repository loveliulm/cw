<!-- 总账 -->
<template>
  <div class="app-container"
       @click="periodstate=false">
    <div id="general">
      <!-- 头 -->
      <div class="general-top">
        <div class="general-top-left">
          <span class="fz4 general-span">总账</span>
          <!-- 期数 -->
          <div class="accout-general">
            <div class="ui-btn-menu "
                 :class="periodstate==true? 'ui-btn-menu-cur' : ''"
                 @click.stop="pullDown()">
              <div class="ui-btn menu-btn"
                   v-cloak>
                <span>{{periodBegin}}</span>
                <span v-show="periodEndshow">至</span>
                <span v-show="periodEndshow">{{periodEnd}}</span>
                <b></b>
              </div>
            </div>
            <!--期间选择刷新数据 -->
            <el-form :model="generalForm"
                     ref="generalForm"
                     class="generalForm">
              <div class="con"
                   v-show="periodstate"
                   @click.stop>
                <ul>
                  <li>
                    <span>会计期间：</span>
                    <template>
                      <el-select style="width:30%;"
                                 v-model="generalForm.accountantBegin"
                                 size="small">
                        <el-option v-for="(item,index) in periodsList"
                                   :key="index"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                      </el-select>
                    </template>
                    <span>至</span>
                    <template>
                      <el-select style="width:30%;"
                                 v-model="generalForm.accountantEnd"
                                 size="small">
                        <el-option v-for="(item,index) in periodsList"
                                   :key="index"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                      </el-select>
                    </template>
                    <!-- <span>
                    <i class="el-icon-time"></i>
                  </span> -->
                  </li>
                  <!-- <li>
                    <span style="padding-right:28px;">币别：</span>
                    <template>
                      <el-select v-model="generalForm.Currency"
                                 placeholder="请选币别"
                                 size="small"
                                 @change="curstyle">
                        <el-option v-for="(item,index) in currencysList"
                                   :key="index"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                      </el-select>
                    </template>
                  </li> -->
                  <!-- <li>
                    <el-checkbox label="显示辅助核算"
                                 name="type"
                                 v-model="generalForm.showAuxiliary"></el-checkbox>
                  </li> -->
                  <li>
                    <el-checkbox label="余额为0不显示"
                                 name="type"
                                 v-model="generalForm.showBalNotZero"></el-checkbox>
                  </li>
                  <!-- <li>
                    <el-checkbox label="无发生额且余额为0不显示"
                                 name="type"
                                 v-model="generalForm.showBalAndActualNotZero"></el-checkbox>
                  </li> -->
                  <!-- <li>
                    <el-checkbox label="无发生额不显示本期合计、本年累计"
                                 name="type"
                                 v-model="generalForm.notShowTotalWhenNotActual"></el-checkbox>
                  </li> -->
                </ul>
                <div class="general-top-right">
                  <el-button size="small"
                             @click="resetForm()">重置</el-button>
                  <el-button type="primary"
                             size="small"
                             @click="submitForm()">确定</el-button>
                </div>
              </div>
            </el-form>
            <!--刷新数据 -->
          </div>
          <div class="general-upload">
            <el-button size="small"
                       @click="updatedTableDate()">刷新</el-button>
          </div>
        </div>
        <div class="general-top-right">
          <!-- <el-button size="small"
                     type="primary"
                     @click="printVoucher">打印</el-button> -->
          <!-- <el-button size="small"
                     @click="exportExcel">导出 </el-button> -->
        </div>
      </div>
      <!-- 头结束 -->
      <div class="general-context el-card--custom">
        <!-- table开始 -->
        <template>
          <!--人民币-->
          <el-table :data="generalData"
                    border
                    stripe
                    height="600"
                    v-if="styleshow===1"
                    v-loading="loadingData"
                    element-loading-text="拼命加载中"
                    :key="0">
            <el-table-column label="科目编号"
                             width="180"
                             header-align="center">
              <template slot-scope="scope">
                <a class="aLinkStyle"
                   v-text="scope.row.subjectCode"></a>
              </template>
            </el-table-column>
            <el-table-column label="科目名称"
                             width="200"
                             header-align="center">
              <template slot-scope="scope">
                <p v-text="scope.row.subjectName"></p>
              </template>
            </el-table-column>

            <el-table-column label="期间"
                             header-align="center"
                             align="center">
              <template slot-scope="scope">
                <p v-for="(item, index) in scope.row.details"
                   :key="index"
                   v-text="item.yearPeriod"></p>
              </template>
            </el-table-column>

            <el-table-column label="摘要"
                             header-align="center"
                             align="center">
              <template slot-scope="scope">
                <p v-for="(item, index) in scope.row.details"
                   :key="index"
                   v-text="item.summary"></p>
              </template>
            </el-table-column>
            <el-table-column label="借方"
                             header-align="center"
                             align="right">
              <template slot-scope="scope">
                <p v-for="(item, index) in scope.row.details"
                   :key="index"
                   v-text="item.debit==0 || item.debit === null?'':moneyChange.outputmoney(item.debit)"></p>
              </template>
            </el-table-column>
            <el-table-column label="贷方"
                             header-align="center"
                             align="right">
              <template slot-scope="scope">
                <p v-for="(item, index) in scope.row.details"
                   :key="index"
                   v-text="item.credit==0 || item.credit === null?'':moneyChange.outputmoney(item.credit)"></p>
              </template>
            </el-table-column>
            <el-table-column label="方向"
                             header-align="center"
                             align="center">
              <template slot-scope="scope">
                <p v-for="(item, index) in scope.row.details"
                   :key="index"
                   v-text="item.balanceDirectionText"></p>
              </template>
            </el-table-column>
            <el-table-column label="余额"
                             header-align="center"
                             align="right">
              <template slot-scope="scope">
                <p v-for="(item, index) in scope.row.details"
                   :key="index"
                   v-text="item.balance==0 || item.balance === null?'':moneyChange.outputmoney(item.balance)"></p>
              </template>
            </el-table-column>
          </el-table>
          <!-- 外币的时候显示 -->
          <el-table :data="generalData"
                    border
                    height="600"
                    stripe
                    v-else-if="styleshow === 2"
                    v-loading="loadingData"
                    element-loading-text="拼命加载中"
                    :key="1">
            <el-table-column label="科目编号"
                             width="180"
                             header-align="center">
              <template slot-scope="scope">
                <p class="aLinkStyle"
                   v-text="scope.row.accountCode"></p>
              </template>
            </el-table-column>
            <el-table-column label="科目名称"
                             width="200"
                             header-align="center">
              <template slot-scope="scope">
                <p v-cloak>{{ scope.row.accountName}}</p>
              </template>
            </el-table-column>
            <el-table-column label="期间"
                             header-align="center"
                             align="center">
              <template slot-scope="scope">
                <p v-for="(item, index) in scope.row.totalAccountContent"
                   :key="index"
                   v-cloak>{{item.yearPeriod}}</p>
              </template>
            </el-table-column>

            <el-table-column label="摘要"
                             header-align="center"
                             align="center">
              <template slot-scope="scope">
                <p v-for="(item, index) in scope.row.totalAccountContent"
                   :key="index"
                   v-cloak>{{item.voucherRemark}}</p>
              </template>
            </el-table-column>

            <el-table-column label="借方金额"
                             header-align="center">
              <el-table-column label="原币"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-for="(item, index) in scope.row.totalAccountContent"
                     :key="index"
                     v-text="item.debitAmountForeign==0?'':moneyChange.outputmoney(item.debitAmountForeign)"></p>
                </template>
              </el-table-column>
              <el-table-column label="本位币"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-for="(item, index) in scope.row.totalAccountContent"
                     :key="index"
                     v-text="item.debitAmountLocal==0?'':moneyChange.outputmoney(item.debitAmountLocal)"></p>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="贷方方金额"
                             header-align="center">
              <el-table-column label="原币"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-for="(item, index) in scope.row.totalAccountContent"
                     :key="index"
                     v-text="item.creditAmountForeign==0?'':moneyChange.outputmoney(item.creditAmountForeign)"></p>
                </template>
              </el-table-column>
              <el-table-column label="本位币"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-for="(item, index) in scope.row.totalAccountContent"
                     :key="index"
                     v-text="item.creditAmountLocal==0?'':moneyChange.outputmoney(item.creditAmountLocal)"></p>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="金额"
                             header-align="center">
              <el-table-column label="方向"
                               header-align="center"
                               align="center">
                <template slot-scope="scope">
                  <p v-for="(item, index) in scope.row.totalAccountContent"
                     :key="index"
                     v-cloak>{{item.subjectDirection}}</p>
                </template>
              </el-table-column>
              <el-table-column label="原币"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-for="(item, index) in scope.row.totalAccountContent"
                     :key="index"
                     v-text="item.balanceForeign==0?'':moneyChange.outputmoney(item.balanceForeign)"></p>
                </template>
              </el-table-column>
              <el-table-column label="本位币"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-for="(item, index) in scope.row.totalAccountContent"
                     :key="index"
                     v-text="item.balanceLocal==0?'':moneyChange.outputmoney(item.balanceLocal)"></p>
                </template>
              </el-table-column>
            </el-table-column>
          </el-table>
          <!-- 所有币别显示-->
          <el-table :data="generalData"
                    border
                    height="600"
                    stripe
                    v-else
                    v-loading="loadingData"
                    element-loading-text="拼命加载中"
                    :key="1">
            <el-table-column label="科目编号"
                             width="180"
                             header-align="center">
              <template slot-scope="scope">
                <p class="aLinkStyle"
                   v-text="scope.row.accountCode"></p>
              </template>
            </el-table-column>
            <el-table-column label="科目名称"
                             width="200"
                             header-align="center">
              <template slot-scope="scope">
                <p v-cloak>{{ scope.row.accountName}}</p>
              </template>
            </el-table-column>
            <el-table-column label="期间"
                             header-align="center"
                             align="center">
              <template slot-scope="scope">
                <p v-for="(item, index) in scope.row.totalAccountContent"
                   :key="index"
                   v-cloak>{{item.yearPeriod}}</p>
              </template>
            </el-table-column>
            <el-table-column label="币别"
                             header-align="center"
                             align="center">
              <template slot-scope="scope">
                <p v-for="(item, index) in scope.row.totalAccountContent"
                   :key="index"
                   v-cloak>{{item.yearPeriod}}</p>
              </template>
            </el-table-column>
            <el-table-column label="摘要"
                             header-align="center"
                             align="center">
              <template slot-scope="scope">
                <p v-for="(item, index) in scope.row.totalAccountContent"
                   :key="index"
                   v-cloak>{{item.voucherRemark}}</p>
              </template>
            </el-table-column>

            <el-table-column label="借方金额"
                             header-align="center">
              <el-table-column label="原币"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-for="(item, index) in scope.row.totalAccountContent"
                     :key="index"
                     v-text="item.debitAmountForeign==0?'':moneyChange.outputmoney(item.debitAmountForeign)"></p>
                </template>
              </el-table-column>
              <el-table-column label="本位币"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-for="(item, index) in scope.row.totalAccountContent"
                     :key="index"
                     v-text="item.debitAmountLocal==0?'':moneyChange.outputmoney(item.debitAmountLocal)"></p>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="贷方方金额"
                             header-align="center">
              <el-table-column label="原币"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-for="(item, index) in scope.row.totalAccountContent"
                     :key="index"
                     v-text="item.creditAmountForeign==0?'':moneyChange.outputmoney(item.creditAmountForeign)"></p>
                </template>
              </el-table-column>
              <el-table-column label="本位币"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-for="(item, index) in scope.row.totalAccountContent"
                     :key="index"
                     v-text="item.creditAmountLocal==0?'':moneyChange.outputmoney(item.creditAmountLocal)"></p>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="金额"
                             header-align="center">
              <el-table-column label="方向"
                               header-align="center"
                               align="center">
                <template slot-scope="scope">
                  <p v-for="(item, index) in scope.row.totalAccountContent"
                     :key="index"
                     v-cloak>{{item.subjectDirection}}</p>
                </template>
              </el-table-column>
              <el-table-column label="原币"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-for="(item, index) in scope.row.totalAccountContent"
                     :key="index"
                     v-text="item.balanceForeign==0?'':moneyChange.outputmoney(item.balanceForeign)"></p>
                </template>
              </el-table-column>
              <el-table-column label="本位币"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-for="(item, index) in scope.row.totalAccountContent"
                     :key="index"
                     v-text="item.balanceLocal==0?'':moneyChange.outputmoney(item.balanceLocal)"></p>
                </template>
              </el-table-column>
            </el-table-column>
          </el-table>
        </template>
        <!-- table结束 -->
        <!-- 科目尾 -->
        <pagination v-show="total > 0"
                    :total="total"
                    :page.sync="submitAccountForm.pageNum"
                    :limit.sync="submitAccountForm.pageSize"
                    @pagination="getAccTotalList" />
        <!--<div class="general-top-right">共{{generalData.length}}条</div>-->
        <!-- 科目尾 -->
      </div>
    </div>
  </div>
</template>
<script>
import { currentPeriod, periodList } from "@/api/voucher/viewvoucher";
import { listAccTotal, exportTotalAccount } from "@/api/accBook/totalAcc";
export default {
  name: "TotalAcc",
  data () {
    return {
      //数据加载
      loadingData: true,
      //页面风格显示
      styleshow: 1,

      //当前期间
      yearPeriod: null,

      //期间显示
      periodBegin: "",
      //期间显示

      //期间显示
      periodEnd: "",
      //期间显示

      //期间后半段是否显示
      periodEndshow: false,
      //期间后半段是否显示

      //会计期间列表
      periodsList: [],
      //会计期间列表

      //币别列表
      currencysList: [],
      //币别列表

      //期数下拉框是否显示
      periodstate: false,

      //form表单的初始数据
      generalForm: {
        //会计开始时间
        accountantBegin: "",
        //至结束时间
        accountantEnd: "",

        //币别uid
        Currency: "",


        //币别类型
        currencystyle: "",


        //显示辅助核算
        showAuxiliary: false,


        //显示余额不为0
        showBalNotZero: false,


        //无发生额且余额为0不显示
        showBalAndActualNotZero: false,


        //无发生额不显示本期合计、本年合计
        notShowTotalWhenNotActual: false

      },

      //form表单提交数据
      submitAccountForm: {
        //ID
        bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId,
        pageNum: 1,
        pageSize: 10,

      },
      //总账表格数据
      generalData: [],
      bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId,
      // 总条数
      total: 0,

    };
  },

  created () {
    this.initData();
  },
  methods: {
    //打印报表
    printVoucher () {
    },
    //导出
    exportExcel () {
      const queryParams = this.submitAccountForm;
      this.$confirm('是否确认导出总账所有数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportTotalAccount(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function () { });
    },
    async initData () {
      let result = await this.getCurrentPeriod();
      let result2 = await this.getPeriodList();
      this.getAccTotalList();

    },
    // 获取总账列表
    getAccTotalList () {
      this.loadingData = true;
      return new Promise(resolve => {
        this.doSubmit();
        listAccTotal(this.submitAccountForm).then(res => {
          this.generalData = res.rows;
          this.total = res.total;
          this.loadingData = false;
          resolve("success");
        })
      })
    },

    //获取当前期间
    getCurrentPeriod () {
      return new Promise(resolve => {
        currentPeriod({ bookId: this.submitAccountForm.bookId }).then(res => {
          this.yearPeriod = res.data.yearPeriod;
          resolve(res.data);
        })
      })
    },

    //获取会计期间
    getPeriodList () {
      return new Promise(resolve => {
        periodList({ bookId: this.submitAccountForm.bookId }).then(res => {
          console.log(`期间${JSON.stringify(res.rows)}`)
          res.rows.forEach((n, i) => {
            this.periodsList.push({
              value: n.yearPeriod + "",
              label: n.yearPeriod
            })
          });
          this.defaultSelect();
          resolve("success")
        })
      })
    },

    //提交数据处理
    doSubmit () {
      //期间
      this.submitAccountForm.startPeriod = this.generalForm.accountantBegin;
      this.submitAccountForm.endPeriod = this.generalForm.accountantEnd;
      //期间
      //币别
      this.submitAccountForm.currency = this.generalForm.Currency;
      //币别
      //辅助核算
      this.submitAccountForm.showAuxiliary = this.generalForm.showAuxiliary;
      this.submitAccountForm.showBalNotZero = this.generalForm.showBalNotZero;
      this.submitAccountForm.showBalAndActualNotZero = this.generalForm.showBalAndActualNotZero;
      this.submitAccountForm.notShowTotalWhenNotActual = this.generalForm.notShowTotalWhenNotActual;
    },

    //币别类型选择
    curstyle (data) {
      this.currencysList.forEach((n, i) => {
        if (n.value === data) {
          this.generalForm.currencystyle = n.currencystyle;
        }
      });
    },

    //默认选中
    defaultSelect () {
      //这里会计期间不能用状态管理，状态异步，数据可能没有获取到就要渲染页面
      //let cur = this.currencysList;
      //默认选中当前期间
      this.generalForm.accountantBegin = this.yearPeriod;
      this.generalForm.accountantEnd = this.yearPeriod;
      this.periodBegin = this.yearPeriod;
      this.periodEndshow = false;

      //this.generalForm.Currency = cur[0].value;//TODO数据类型暂时想象处理，具体按照后台给定
      //默认选中表格显示方式,要在dom操作变化之后才执行
      /*this.$nextTick(() => {
        this.styleshow = cur[0].currencystyle;
      });*/
    },


    //这里是期间显示多少期，上面同步变化
    /*periodChange () {
      let a = this.periodsList;
      a.forEach((n, i) => {
        if (this.generalForm.accountantBegin === n.value) {
          this.periodBegin = n.label;
        }
        if (this.generalForm.accountantEnd === n.value) {
          this.periodEnd = n.label;
        }
        //选择期间不同是 显示期间段
        if (
          this.generalForm.accountantBegin === this.generalForm.accountantEnd
        ) {
          this.periodEndshow = false;
        } else {
          this.periodEndshow = true;
        }

      });
    },*/

    // 是否显示会计期间期间段
    periodChange () {
      if (this.equalsDate(this.generalForm.accountantBegin, this.generalForm.accountantEnd)) {
        this.periodEndshow = false;
      } else {
        this.periodEndshow = true;
      }
      this.periodBegin = this.generalForm.accountantBegin;
      this.periodEnd = this.generalForm.accountantEnd;
    },
    pullDown () {
      this.periodstate = !this.periodstate;
    },


    //表格数据重置  这里重置是先前默认的不重置，其他的要清空。
    resetForm () {
      //会计期间，币别，显示辅助核算默认
      this.defaultSelect();

      //多项选择
      this.generalForm.showAuxiliary = true;
      this.generalForm.showBalNotZero = false;
      this.generalForm.showBalAndActualNotZero = false;
      this.generalForm.notShowTotalWhenNotActual = false;

      this.submitForm();
    },

    //提交表单
    submitForm () {
      if (!this.compareDate(this.generalForm.accountantBegin, this.generalForm.accountantEnd)) {
        this.msgError("开始期间不能超过结束期间");
        return;
      }
      this.periodstate = false;
      this.getAccTotalList();
      this.periodChange();
    },

    //刷新按钮重新获取数据
    updatedTableDate () {
      //会计期间，币别，显示辅助核算默认
      this.defaultSelect();

      //多项选择
      this.generalForm.showAuxiliary = true;
      this.generalForm.showBalNotZero = false;
      this.generalForm.showBalAndActualNotZero = false;
      this.generalForm.notShowTotalWhenNotActual = false;
      this.submitForm();
    },

  }
};
</script>
<style lang='scss' scoped>
/* 数据渲染时的闪烁问题 */
#general .general-top {
  color: #555;
  width: 100%;
  height: 45px;
  .general-span {
    line-height: 28px;
    float: left;
  }
  .accout-general,
  .general-upload {
    float: left;
  }
  .accout-general {
    margin: 0 10px;
  }
}

/* 选择菜单卡片效果 公共样式*/
.ui-btn-menu {
  vertical-align: middle;
  zoom: 1;
  z-index: 3;
}

.ui-btn-menu-cur .menu-btn {
  &:active,
  &:hover {
    padding-bottom: 1px;
    background: #fff !important;
    border-bottom: none;
    border-bottom-left-radius: 0;
    border-bottom-right-radius: 0;
    box-shadow: none;
  }
}

.ui-btn-menu .menu-btn {
  position: relative;
  margin-right: 0;
  padding-right: 20px;
  z-index: 3;
}

.ui-btn-menu-cur .ui-btn {
  border-bottom: 1px solid #fff;
  background-color: #fff;
}

.ui-btn {
  display: inline-block;
  padding: 0 6px;
  height: 29px;
  line-height: 29px;
  font-size: 12px;
  line-height: 26px;
  border: 1px solid rgb(191, 207, 217);
  border-radius: 2px;
  background: #fff;
  color: #555;
  vertical-align: middle;
  cursor: pointer;
}

.ui-btn-menu .menu-btn b {
  font-size: 0;
  position: absolute;
  right: 4px;
  top: 50%;
  margin-top: -2px;
  border-width: 6px;
  border-color: #bfcfd9 #fff #fff;
  border-style: solid;
  height: 0;
  overflow: hidden;
  -webkit-transition: -webkit-transform 150ms ease-in;
  transition: -webkit-transform 150ms ease-in;
  transition: transform 150ms ease-in;
  transition: transform 150ms ease-in, -webkit-transform 150ms ease-in;
}

/* hover之后三角到过来 */
.ui-btn-menu-cur .menu-btn b {
  -moz-transform: rotate(180deg);
  -moz-transform-origin: 50% 30%;
  -webkit-transform: rotate(180deg);
  -webkit-transform-origin: 50% 30%;
  -o-transform: rotate(180deg);
  -o-transform-origin: 50% 20%;
  transform: rotate(180deg);
  transform-origin: 50% 30%;
  filter: progid:DXImageTransform.Microsoft.BasicImage(rotation=2);
}

/* 菜单卡片效果 公共样式*/

#general {
  .general-top .general-top-left {
    .con {
      width: 360px;
      padding: 15px;
      position: absolute;
      //   top: 27px;
      //   left: 38px;
      border: 1px solid #c1c1c1;
      border-bottom-left-radius: 2px;
      border-bottom-right-radius: 2px;
      box-shadow: 0 1px 1px rgba(0, 0, 0, 0.15);
      background-color: #fff;
      z-index: 2005;
      ul li {
        padding-bottom: 20px;
        list-style-type: none;
        font-size: 14px;
        text-align: -webkit-match-parent;
        color: #555;
      }
      .el-input--small .el-input__inner {
        height: 30px;
        width: 112px;
      }
      .el-input__icon {
        right: -4px;
      }
      ul .subject .el-input__inner {
        width: 246px;
      }
    }
    .el-input__icon + .el-input__inner,
    .el-select .el-input__inner {
      padding-right: 0px;
    }
  }
  .general-top-right {
    display: block;
    padding-right: 20px;
    float: right;
    font-size: 13px;
    .el-button {
      width: 70px;
    }
  }
  .general-context {
    tbody {
      tr {
        td {
          .cell {
            p {
              line-height: 30px;
              height: 30px;
              margin: 0 -8px;
              padding: 0 8px;
              overflow: hidden;
              &:not(:last-child) {
                border-bottom: 1px solid rgb(223, 232, 236);
              }
            }
          }
        }
      }
    }
  }
}
a:focus,
a:hover {
  text-decoration: underline !important;
}
</style>