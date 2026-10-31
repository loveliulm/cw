<template>
  <div class="app-container"
       @click="periodstate=false">
    <div class="general">
      <!-- 头 -->
      <div class="general-top">
        <div class="general-top-left">
          <!-- 期数 -->
          <div class="accout-general">
            <span class="fz4 general-span">科目余额表</span>
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
            <el-form :model="submitAccountForm"
                     ref="submitAccountForm">
              <div class="con"
                   v-show="periodstate"
                   @click.stop>
                <ul>
                  <li>
                    <span>会计期间：</span>
                    <template>
                      <el-select style="width:30%;"
                                 v-model="submitAccountForm.startPeriod"
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
                                 v-model="submitAccountForm.endPeriod"
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
                  <!-- 币别功能暂时搁置-->
                  <!--<li>
                    <span style="padding-right:28px;">币别：</span>
                    <template>
                      <el-select v-model="submitAccountForm.Currency"
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
                  </li>-->
                  <li>
                    <span>科目级次：</span>
                    <template>
                      <el-input-number style="width:30%;"
                                       v-model="submitAccountForm.startLevel"
                                       controls-position="right"
                                       :min="1"
                                       :max="4"></el-input-number>
                    </template>
                    <span>至</span>
                    <template>
                      <el-input-number style="width:30%;"
                                       v-model="submitAccountForm.endLevel"
                                       controls-position="right"
                                       :min="1"
                                       :max="4"></el-input-number>
                    </template>
                  </li>
                  <li>
                    <el-checkbox label="余额为0不显示"
                                 name="type"
                                 v-model="submitAccountForm.showBalNotZero"></el-checkbox>
                  </li>
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
          </div>
          <!--刷新数据 -->
          <div class="general-upload">
            <el-button size="small"
                       @click="updatedTableDate">刷新</el-button>
          </div>
        </div>
        <div class="general-top-right">
          <!-- <el-button size="small"
                     type="primary"
                     @click="printVoucher">打印</el-button> -->
          <el-button size="small"
                     @click="exportExcel">导出</el-button>
        </div>
      </div>
      <!-- 头结束 -->
      <!-- 头结束 -->
      <div class="general-context el-card--custom">
        <!-- table开始 -->
        <template>
          <!--人民币、综合本位币-->
          <el-table :data="accBalanceData"
                    border
                    stripe
                    height="600"
                    v-if="styleshow===1"
                    v-loading="loadingData"
                    element-loading-text="拼命加载中"
                    :key="0">
            <el-table-column label="科目编码"
                             width="180"
                             header-align="center">
              <template slot-scope="scope">
                <p v-text="scope.row.subjectCode"></p>
              </template>
            </el-table-column>
            <el-table-column label="科目名称"
                             width="200"
                             header-align="center">
              <template slot-scope="scope">
                <p v-text="scope.row.subjectName"></p>
              </template>
            </el-table-column>
            <el-table-column label="期初余额"
                             header-align="center">
              <el-table-column label="借方"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.startDebit == 0?'':scope.row.startDebit"></p>
                </template>
              </el-table-column>
              <el-table-column label="贷方"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.startCredit == 0?'':scope.row.startCredit"></p>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="本期发生额"
                             header-align="center">
              <el-table-column label="借方"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.periodDebit == 0?'':scope.row.periodDebit"></p>
                </template>
              </el-table-column>
              <el-table-column label="贷方"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.periodCredit == 0?'':scope.row.periodCredit"></p>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="本年累计发生额"
                             header-align="center">
              <el-table-column label="借方"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.yearDebit == 0?'':scope.row.yearDebit"></p>
                </template>
              </el-table-column>
              <el-table-column label="贷方"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.yearCredit == 0?'':scope.row.yearCredit"></p>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="期末余额"
                             header-align="center">
              <el-table-column label="借方"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.endDebit == 0?'':scope.row.endDebit"></p>
                </template>
              </el-table-column>
              <el-table-column label="贷方"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.endCredit == 0?'':scope.row.endCredit"></p>
                </template>
              </el-table-column>
            </el-table-column>
          </el-table>
          <!-- 外币的时候显示 -->
          <el-table :data="accBalanceData"
                    border
                    stripe
                    height="600"
                    v-if="styleshow===2"
                    v-loading="loadingData"
                    element-loading-text="拼命加载中"
                    :key="0">
            <el-table-column label="科目编码"
                             width="180"
                             header-align="center">
              <template slot-scope="scope">
                <p v-text="scope.row.subjectCode"></p>
              </template>
            </el-table-column>
            <el-table-column label="科目名称"
                             width="200"
                             header-align="center">
              <template slot-scope="scope">
                <p v-text="scope.row.subjectName"></p>
              </template>
            </el-table-column>
            <el-table-column label="期初余额"
                             header-align="center">
              <el-table-column label="借方(原币)"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.startDebit"></p>
                </template>
              </el-table-column>
              <!--本位币暂时没有返回的字段-->
              <el-table-column label="借方(本位币)"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.startDebit"></p>
                </template>
              </el-table-column>
              <el-table-column label="贷方(原币)"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.startCredit"></p>
                </template>
              </el-table-column>
              <!--本位币暂时没有返回的字段-->
              <el-table-column label="贷方(本位币)"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.startCredit"></p>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="本期发生额"
                             header-align="center">
              <el-table-column label="借方(原币)"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.periodDebit"></p>
                </template>
              </el-table-column>
              <!--本位币暂时没有返回的字段-->
              <el-table-column label="借方(本位币)"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.periodDebit"></p>
                </template>
              </el-table-column>
              <el-table-column label="贷方(原币)"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.periodCredit"></p>
                </template>
              </el-table-column>
              <!--本位币暂时没有返回的字段-->
              <el-table-column label="贷方(本位币)"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.periodCredit"></p>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="本年累计发生额"
                             header-align="center">
              <el-table-column label="借方(原币)"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.yearDebit"></p>
                </template>
              </el-table-column>
              <!--本位币暂时没有返回的字段-->
              <el-table-column label="借方(本位币)"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.yearDebit"></p>
                </template>
              </el-table-column>
              <el-table-column label="贷方(原币)"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.yearCredit"></p>
                </template>
              </el-table-column>
              <!--本位币暂时没有返回的字段-->
              <el-table-column label="贷方(本位币)"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.yearCredit"></p>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="期末余额"
                             header-align="center">
              <el-table-column label="借方(原币)"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.endDebit"></p>
                </template>
              </el-table-column>
              <!--本位币暂时没有返回的字段-->
              <el-table-column label="借方(本位币)"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.endDebit"></p>
                </template>
              </el-table-column>
              <el-table-column label="贷方(原币)"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.endCredit"></p>
                </template>
              </el-table-column>
              <!--本位币暂时没有返回的字段-->
              <el-table-column label="贷方(本位币)"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.endCredit"></p>
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
                    @pagination="getAccBalanceList" />
        <!--<div class="general-top-right">共{{generalData.length}}条</div>-->
        <!-- 科目尾 -->
      </div>
    </div>
  </div>
</template>
<script>
import { currentPeriod, periodList } from "@/api/voucher/viewvoucher";
import { accountBalanceList, exportAccBalance } from "@/api/accBook/accountBalance"
export default {
  name: 'AccountBalance',
  data () {
    return {
      // 币别
      styleshow: 1,
      // 期数下拉框是否显示
      periodstate: false,
      // 期间显示前
      periodBegin: "",
      // 期间显示后
      periodEnd: "",
      // 当前账期
      yearPeriod: null,
      //总账表格数据
      accBalanceData: [],
      // bookId
      bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId,
      //form表单提交数据
      submitAccountForm: {
        //ID
        bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId"): this.$store.state.bookId,
        //会计开始时间
        startPeriod: "",
        //至结束时间
        endPeriod: "",
        //币别uid
        Currency: "",
        //币别类型
        currencystyle: "",
        //显示余额不为0
        showBalNotZero: false,
        //科目级次起始值
        startLevel: 1,
        //科目级次结束值
        endLevel: 1,
        pageNum: 1,
        pageSize: 10,
      },
      // 期间后半段是否显示默认不显示
      periodEndshow: false,
      // 数据是否正在加载
      loadingData: true,
      // 会计账期列表
      periodsList: [],
      // 总条数
      total: 0,
    }
  },
  created () {
    this.initData();
  },
  methods: {
    async initData () {
      let result = await this.getCurrentPeriod();
      let result2 = await this.getPeriodList();
      this.getAccBalanceList();
    },
    // 打印
    printVoucher () { },
    // 导出
    exportExcel () {
      const queryParams = this.submitAccountForm;
      this.$confirm('是否确认导出科目余额表所有数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportAccBalance(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch((e)=>{
       });
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
    //默认选中
    defaultSelect () {
      //默认当前会计期间
      this.submitAccountForm.startPeriod = this.yearPeriod;
      this.submitAccountForm.endPeriod = this.yearPeriod;

      // 默认期间（当前账期）
      this.periodBegin = this.yearPeriod;
      this.periodEnd = this.yearPeriod;
      this.periodEndshow = false;

      // 币别显示数据后续开发
      // 余额为0不显示
      this.submitAccountForm.showBalNotZero = false;

      // 默认科目级次
      this.submitAccountForm.startLevel = 1;
      this.submitAccountForm.endLevel = 1;
    },
    // 获取科目余额列表
    getAccBalanceList () {
      this.loadingData = true;
      accountBalanceList(this.submitAccountForm).then(res => {
        this.accBalanceData = res.rows;
        this.total = res.total;
        this.loadingData = false;
      })
    },
    // 是否显示会计期间期间段
    periodChange () {
      if (this.equalsDate(this.submitAccountForm.startPeriod, this.submitAccountForm.endPeriod)) {
        this.periodEndshow = false;
      } else {
        this.periodEndshow = true;
      }
      this.periodBegin = this.submitAccountForm.startPeriod;
      this.periodEnd = this.submitAccountForm.endPeriod;
    },
    // 控制查询下拉框状态
    pullDown () {
      this.periodstate = !this.periodstate;
    },
    // 刷新
    updatedTableDate () {
      this.defaultSelect();
      this.getAccBalanceList();
    },
    // 重置
    resetForm () {
      this.defaultSelect();
      this.getAccBalanceList();
      this.periodstate = false;
    },
    // 查询条件表单提交
    submitForm () {
      if (!this.compareDate(this.submitAccountForm.startPeriod, this.submitAccountForm.endPeriod)) {
        this.msgError("开始期间不能超过结束期间");
        return;
      }
      if (this.submitAccountForm.startLevel > this.submitAccountForm.endLevel) {
        this.msgError("开始科目级次不能大于结束科目级次！");
        return;
      }
      this.getAccBalanceList();
      this.periodstate = false;
      this.periodChange();
    }
  }
}
</script>
<style lang="scss" scoped>
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

.general {
  .general-top {
    color: #555;
    width: 100%;
    height: 45px;
    display: flex;
    flex-flow: row nowrap;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
    .general-top-left {
      display: flex;
      flex-flow: row nowrap;
      justify-content: space-around;
      align-items: center;
      .con {
        width: 360px;
        padding: 15px;
        position: absolute;
        top: 31px;
        left: 77px;
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
      .accout-general {
        display: flex;
        flex-flow: row nowrap;
        justify-content: space-between;
        align-items: center;
        position: relative;
        span {
          padding-right: 7px;
        }
      }
      .accout-general,
      .general-upload,
      .general-subject {
        margin: 10px 10px 10px 0;
      }
    }
    .general-top-right {
      display: block;
      display: flex;
      flex-flow: row nowrap;
      justify-content: space-between;
      align-items: center;
      //padding-right: 20px;
      //float: right;
      font-size: 13px;
      .el-button {
        width: 70px;
      }
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