<template>
  <div class="app-container"
       @click="periodstate=false">
    <div class="general">
      <!-- 头 -->
      <div class="general-top">
        <div class="general-top-left">
          <!-- 期数 -->
          <div class="accout-general">
            <span class="fz4 general-span">数量金额明细账</span>
            <div class="ui-btn-menu "
                 :class="periodstate==true? 'ui-btn-menu-cur' : ''"
                 @click.stop="pullDown">
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
                             @click="resetForm">重置</el-button>
                  <el-button type="primary"
                             size="small"
                             @click="submitForm">确定</el-button>
                </div>
              </div>
            </el-form>
          </div>
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
      <div class="general-context el-card--custom">
        <template>
          <el-table :data="numberTotalList"
                    border
                    stripe
                    height="600"
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
            <el-table-column label="单位"
                             header-align="center"
                             align="center">
              <template slot-scope="scope">
                <p v-text="scope.row.subjectUnit"></p>
              </template>
            </el-table-column>
            <el-table-column label="期初余额"
                             header-align="center">
              <el-table-column label="数量"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.quantityStartBalance==0?'':scope.row.quantityStartBalance"></p>
                </template>
              </el-table-column>
              <el-table-column label="单价"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.priceStartBalance==0?'':scope.row.priceStartBalance"></p>
                </template>
              </el-table-column>
              <el-table-column label="金额"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.startBalance==0?'':scope.row.startBalance"></p>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="本期借方"
                             header-align="center">
              <el-table-column label="数量"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.quantityPeriodDebit==0?'':scope.row.quantityPeriodDebit"></p>
                </template>
              </el-table-column>
              <el-table-column label="单价"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.pricePeriodDebit==0?'':scope.row.pricePeriodDebit"></p>
                </template>
              </el-table-column>
              <el-table-column label="金额"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.periodDebit==0?'':scope.row.periodDebit"></p>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="本期贷方"
                             header-align="center">
              <el-table-column label="数量"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.quantityPeriodCredit==0?'':scope.row.quantityPeriodCredit"></p>
                </template>
              </el-table-column>
              <el-table-column label="单价"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.pricePeriodCredit==0?'':scope.row.pricePeriodCredit"></p>
                </template>
              </el-table-column>
              <el-table-column label="金额"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.periodCredit==0?'':scope.row.periodCredit"></p>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="本年累计借方"
                             header-align="center">
              <el-table-column label="数量"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.quantityYearDebit==0?'':scope.row.quantityYearDebit"></p>
                </template>
              </el-table-column>
              <el-table-column label="单价"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.priceYearDebit==0?'':scope.row.priceYearDebit"></p>
                </template>
              </el-table-column>
              <el-table-column label="金额"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.yearDebit==0?'':scope.row.yearDebit"></p>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="本年累计贷方"
                             header-align="center">
              <el-table-column label="数量"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.quantityYearCredit==0?'':scope.row.quantityYearCredit"></p>
                </template>
              </el-table-column>
              <el-table-column label="单价"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.priceYearCredit==0?'':scope.row.priceYearCredit"></p>
                </template>
              </el-table-column>
              <el-table-column label="金额"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.yearCredit==0?'':scope.row.yearCredit"></p>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="期末余额"
                             header-align="center">
              <el-table-column label="数量"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.quantityEndBalance==0?'':scope.row.quantityEndBalance"></p>
                </template>
              </el-table-column>
              <el-table-column label="单价"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.priceEndBalance==0?'':scope.row.priceEndBalance"></p>
                </template>
              </el-table-column>
              <el-table-column label="金额"
                               header-align="center"
                               align="right">
                <template slot-scope="scope">
                  <p v-text="scope.row.endBalance==0?'':scope.row.endBalance"></p>
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
                    @pagination="getNumTotalList" />
        <!--<div class="general-top-right">共{{generalData.length}}条</div>-->
        <!-- 科目尾 -->
      </div>
    </div>
  </div>
</template>
<script>
import { currentPeriod, periodList } from "@/api/voucher/viewvoucher";
import { numTotalList, exportNumberTotalAccount } from "@/api/accBook/numberTotalAcc"
import { subjectListByQuery } from "@/api/accBook/numberDetailAcc"

export default {
  name: 'NumberTotalAcc',
  data () {
    return {
      // 标志，判断查询下拉框是否出现
      periodstate: false,
      // 查询条件
      submitAccountForm: {
        //bookId
        bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId,
        //会计开始时间
        startPeriod: "",
        //至结束时间
        endPeriod: "",
        // 显示余额不为0
        showBalNotZero: false,
        // 科目级次起始值
        startLevel: 1,
        // 科目级次结束值
        endLevel: 1,
        pageNum: 1,
        pageSize: 10,
      },
      // 当前显示账期
      yearPeriod: null,
      // 默认期间开始日期
      periodBegin: null,
      // 默认时间结束日期
      periodEnd: null,
      // 标志，至结束日期是否显示
      periodEndshow: false,
      // 会计期间列表
      periodsList: [],
      // 数量金额总账列表
      numberTotalList: [],
      // 数据是否正在加载中
      loadingData: true,
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
      this.getNumTotalList();
    },
    // 打印
    printVoucher () { },
    // 导出
    exportExcel () {
      const queryParams = this.submitAccountForm;
      this.$confirm('是否确认导出核算数量金额总账所有数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportNumberTotalAccount(queryParams);
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

      //默认科目级次
      this.submitAccountForm.startLevel = 1;
      this.submitAccountForm.endLevel = 1;
    },
    // 控制查询下拉框状态
    pullDown () {
      this.periodstate = !this.periodstate;
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
    // 获取数量金额总账列表
    getNumTotalList () {
      this.loadingData = true;
      numTotalList(this.submitAccountForm).then(res => {
        this.numberTotalList = res.rows;
        this.total = res.total;
        this.loadingData = false;
      })
    },
    // 刷新
    updatedTableDate () {
      this.resetForm();
    },
    // 重置
    resetForm () {
      this.defaultSelect();
      this.getNumTotalList();
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
      this.getNumTotalList();
      this.periodstate = false;
      this.periodChange();
    }
  },
  watch: {
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
        left: 49px;
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