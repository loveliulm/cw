<template>
  <div class="app-container"
       @click="periodstate=false">
    <div class="general">
      <!-- 头 -->
      <div class="general-top">
        <div class="general-top-left">
          <!-- 季报/月报 -->
          <div class="accout-general">
            <span class="fz4 general-span">利润表</span>
            <el-select v-model="queryParams.type"
                       placeholder="月报/季报"
                       size="small">
              <el-option v-for="dict in types"
                         :key="dict.value"
                         :label="dict.name"
                         :value="dict.value" />
            </el-select>
          </div>
          <!-- 账期 -->
          <div class="general-subject">
            <span class="fz4 general-span">会计期间:</span>
            <template>
              <el-select v-if="queryParams.type == '月报'"
                         v-model="queryParams.currentPeriod"
                         size="small">
                <el-option v-for="(item,index) in periodsList"
                           :key="index"
                           :label="item.label"
                           :value="item.value">
                </el-option>
              </el-select>
              <el-select v-else
                         v-model="queryParams.currentPeriod"
                         size="small">
                <el-option v-for="(item,index) in quartersList"
                           :key="index"
                           :label="item.label"
                           :value="item.value">
                </el-option>
              </el-select>
            </template>
          </div>
          <div class="general-upload">
            <el-button type="primary"
                       icon="el-icon-search"
                       size="small"
                       @click="handleQuery">搜索</el-button>
          </div>
        </div>
        <div class="general-top-right">
          <el-button size="small"
                     type="primary"
                     @click="printVoucher">打印</el-button>
          <el-button size="small"
                     @click="exportExcel">导出</el-button>
        </div>
      </div>
      <!-- 头结束 -->
      <!-- 头结束 -->
      <div class="general-context el-card--custom">
        <!-- table开始 -->
        <template>
          <!-- 会计制度1 -->
          <el-table v-if="accountSystem == 1"
                    :data="profitsList"
                    border
                    stripe
                    height="700"
                    v-loading="loadingData"
                    element-loading-text="拼命加载中"
                    @cell-mouse-enter="showEdit"
                    @cell-mouse-leave="disappearEdit">
            <el-table-column label="项目"
                             header-align="center"
                             align="left"
                             min-width="180">
              <template slot-scope="scope">
                <span v-text="scope.row.profitName"
                      :class="scope.row.level == 0?'levelStyle0':scope.row.level == 1?'levelStyle1':'levelStyle2'"></span>
                <i class="el-icon-edit"
                   v-if="scope.row.isShow && scope.row.isRule"
                   @click='handleEdit(scope.row)'></i>
              </template>
            </el-table-column>
            <el-table-column label="行次"
                             header-align="center"
                             align="center">
              <template slot-scope="scope">
                <p v-text="scope.row.rowNum"></p>
              </template>
            </el-table-column>
            <el-table-column label="本期金额"
                             header-align="center"
                             align="right">
              <template slot-scope="scope">
                <p v-text="scope.row.currentPeriodHappen == 0?'':scope.row.currentPeriodHappen"></p>
              </template>
            </el-table-column>
            <el-table-column label="本年累计金额"
                             header-align="center"
                             align="right">
              <template slot-scope="scope">
                <p v-text="scope.row.yearTotal == 0?'':scope.row.yearTotal"></p>
              </template>
            </el-table-column>
          </el-table>

          <!-- 会计制度2 -->
          <el-table v-if="accountSystem == 2"
                    :data="profitsList"
                    border
                    stripe
                    height="700"
                    v-loading="loadingData"
                    element-loading-text="拼命加载中"
                    @cell-mouse-enter="showEdit"
                    @cell-mouse-leave="disappearEdit">
            <el-table-column label="项目"
                             header-align="center"
                             align="left"
                             min-width="180">
              <template slot-scope="scope">
                <span v-text="scope.row.profitName"
                      :class="scope.row.level == 0?'levelStyle0':scope.row.level == 1?'levelStyle1':'levelStyle2'"></span>
                <i class="el-icon-edit"
                   v-if="scope.row.isShow && scope.row.isRule"
                   @click='handleEdit(scope.row)'></i>
              </template>
            </el-table-column>
            <el-table-column label="行次"
                             header-align="center"
                             align="center">
              <template slot-scope="scope">
                <p v-text="scope.row.rowNum"></p>
              </template>
            </el-table-column>
            <el-table-column label="本期金额"
                             header-align="center"
                             align="right">
              <template slot-scope="scope">
                <p v-text="scope.row.currentPeriodHappen == 0?'':scope.row.currentPeriodHappen"></p>
              </template>
            </el-table-column>
            <el-table-column label="本年累计"
                             header-align="center"
                             align="right">
              <template slot-scope="scope">
                <p v-text="scope.row.yearTotal == 0?'':scope.row.yearTotal"></p>
              </template>
            </el-table-column>
            <el-table-column label="上期金额"
                             header-align="center"
                             align="right">
              <template slot-scope="scope">
                <p v-text="scope.row.previousPeriodBalance == 0?'':scope.row.previousPeriodBalance"></p>
              </template>
            </el-table-column>
          </el-table>
        </template>
        <!-- table结束 -->
        <!-- 科目尾 -->
        <!-- <pagination v-show="total > 0"
                    :total="total"
                    :page.sync="submitAccountForm.pageNum"
                    :limit.sync="submitAccountForm.pageSize"
                    @pagination="getAccBalanceList" /> -->
        <!--<div class="general-top-right">共{{assReportList.length}}条</div>-->
        <!-- 科目尾 -->
      </div>
      <el-dialog :title="title"
                 :visible.sync="open"
                 :close-on-click-modal="false"
                 min-width="400">
        <div class="rule-content">
          <div class="rule-top">
            <div class="rule-top-item">
              <span>科目:</span>
              <el-autocomplete style="width:80%;"
                               type="text"
                               size="small"
                               clearable
                               v-model="submitReportForm.subject"
                               default-first-option
                               @select="subjectSelect"
                               :fetch-suggestions="subjectSearch">
              </el-autocomplete>
            </div>
            <div class="rule-top-item">
              <span>运算符号:</span>
              <el-select style="width:60%;"
                         v-model="submitReportForm.sign"
                         placeholder="运算符号"
                         size="small">
                <el-option v-for="dict in operationSoymbol"
                           :key="dict.id"
                           :label="dict.sign"
                           :value="dict.sign" />
              </el-select>
            </div>
            <div class="rule-top-item">
              <span>取数规则:</span>
              <el-select style="width:60%;"
                         v-model="submitReportForm.dataRule"
                         placeholder="取数规则"
                         size="small">
                <el-option v-for="dict in dataRules"
                           :key="dict.id"
                           :label="dict.name"
                           :value="dict.value" />
              </el-select>
            </div>
            <el-button size="small"
                       @click="addProfitRuleData">添加</el-button>
          </div>
          <div class="rule-table">
            <el-table :data="profitRuleList"
                      border
                      stripe
                      v-loading="loadReportRule"
                      element-loading-text="拼命加载中"
                      :row-class-name="tableRowClassName"
                      show-summary
                      :summary-method="getTotal">
              <el-table-column label="科目"
                               prop="subject"
                               header-align="center"
                               align="left" />
              <el-table-column label="运算符号"
                               prop="sign"
                               header-align="center"
                               align="center" />
              <el-table-column label="取数规则"
                               prop="dataRule"
                               header-align="center"
                               align="left" />
              <el-table-column label="本期金额"
                               prop="currentPeriodHappen"
                               header-align="center"
                               align="left">
                <template slot-scope="scope">
                  <span v-text="scope.row.currentPeriodHappen == 0?'':scope.row.currentPeriodHappen"></span>
                </template>
              </el-table-column>
              <el-table-column label="本年累计金额"
                               prop="yearTotal"
                               header-align="center"
                               align="left">
                <template slot-scope="scope">
                  <span v-text="scope.row.yearTotal == 0?'':scope.row.yearTotal"></span>
                </template>
              </el-table-column>
              <el-table-column label="操作"
                               header-align="center"
                               align="center">
                <template slot-scope="scope">
                  <i class="el-icon-delete"
                     @click='delReportData(scope.row)'></i>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="rule-footer"></div>
        </div>
        <div slot="footer"
             class="dialog-footer">
          <el-button type="primary"
                     @click="submitFormReportRule">保 存</el-button>
          <el-button @click="closeFormReportRule">取 消</el-button>
        </div>
      </el-dialog>
    </div>
    <print-profit v-if="printTableShow" :allTableData="profitsList" :profitHeadData ="profitHData" ref="printprofitReport"></print-profit>
  </div>
</template>
<script>
import { currentPeriod, periodList } from "@/api/voucher/viewvoucher";
import { profitList, profitRule, saveProfitRule, quarterList, balanceSheet, exportProfit } from "@/api/report/profit";
import { subjectList } from "@/api/account/settleAccounts";
import printProfit from "@/components/printProfit";

export default {
  name: 'Profit',
  data () {
    return {
      // 会计制度
      accountSystem: 1,
      // 查询条件
      queryParams: {
        bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId,
        currentPeriod: null,
        type: '月报'
      },
      // 会计期间
      periodsList: [],
      // 季度期间
      quartersList: [],
      // 利润列表
      profitsList: [],
      // 数据是否正在加载中
      loadingData: true,
      // 弹出框标题
      title: '',
      // 标志，判断弹框是否弹出
      open: false,
      // 规则列表
      profitRuleList: [],
      // 科目列表
      subList: [],
      // 运算符号列表
      operationSoymbol: [{ id: 1, sign: '+' }, { id: 2, sign: '-' }],
      // 取数规则列表
      dataRules: [{ id: 1, name: '实际损益额', value: '实际损益额' }, { id: 2, name: '借方发生额', value: '借方发生额' }, { id: 3, name: '贷方发生额', value: '贷方发生额' }],
      // 编辑公式-添加数据
      submitReportForm: {
        subject: null,
        subjectId: null,
        subjectCode: null,
        sign: '+',
        dataRule: '实际损益额'
      },
      // 规则数据是否正在加载
      loadReportRule: true,
      // 报表重分类选项列表
      types: [{ name: '月报', value: '月报' }, { name: '季报', value: '季报' }],
      // profitId
      profitId: null,
      // 打印报表是否显示
      printTableShow:false,
      // 打印报表表头数据
      profitHData:{}
    }
  },
  components:{
    printProfit
  },
  created () {
    this.initData();
    this.profitHeadData();
    this.getSubjectList();
    this.getPeriodList();
  },
  methods: {
    async initData () {
      let result = await this.getCurrentPeriod();
      this.getProfitList();
    },
    // 打印
    printVoucher () {
      this.printTableShow = true;
      this.$nextTick(() => {
        this.$refs.printprofitReport.printAssets();
      });
     },
    // 导出
    exportExcel () {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出利润表所有数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportProfit(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch((e)=>{
      });
    },
    //获取当前期间
    getCurrentPeriod () {
      return new Promise(resolve => {
        currentPeriod({ bookId: this.queryParams.bookId }).then(res => {
          this.queryParams.currentPeriod = res.data.yearPeriod;
          resolve(res.data.yearPeriod);
        })
      })
    },
    //获取会计期间
    getPeriodList () {
      return new Promise(resolve => {
        periodList({ bookId: this.queryParams.bookId }).then(res => {
          res.rows.forEach((n, i) => {
            this.periodsList.push({
              value: n.yearPeriod + "",
              label: n.yearPeriod
            })
          });
          resolve("success")
        })
      })
    },
    // 拼接打印利润报表表头数据
    profitHeadData(){
      let headData = new Object();
      headData.customerName = this.$store.state.book.customerName || this.localStorage.getItem('customerName');
      headData.currentPeriodData = Object.keys(this.$store.state.book.currentPeriodData).length == 0?this.localStorage.getItem('currentPeriodData'):this.$store.state.book.currentPeriodData;
      this.profitHData = headData;
      return headData;
    },
    // 获取季度列表
    getQuarterList () {
      return new Promise(resolve => {
        quarterList({ bookId: this.queryParams.bookId }).then(res => {
          this.quartersList = this.quarterData(res.rows);
          resolve("success");
        });
      })
    },
    // 获取利润表
    getProfitList () {
      this.loadingData = true;
      profitList(this.queryParams).then(res => {
        this.profitsList = this.handleListData(res.rows);
        this.loadingData = false;
      })
    },
    // 查询条件表单提交
    handleQuery () {
      if (this.queryParams.currentPeriod == "" || this.queryParams.currentPeriod == null) {
        this.msgError("请选择账期");
        return;
      }
      this.getProfitList();
    },
    // 列表数据处理
    handleListData (arr) {
      let list = arr;
      list.forEach(element => {
        element.isShow = false;
      });
      return list;
    },
    // 鼠标悬停在单元格，编辑图标显示
    showEdit (row) {
      row.isShow = true;
    },
    // 鼠标移开单元格，编辑图标不显示
    disappearEdit (row) {
      row.isShow = false;
    },
    // 获取规则列表
    getProfitRule (num) {
      this.loadReportRule = true;
      return new Promise(resolve => {
        const profitId = num;
        let query = Object.assign(this.queryParams, { profitId });
        profitRule(query).then(res => {
          resolve(res.rows);
          this.loadReportRule = false;
        })
      })
    },
    // 编辑
    handleEdit (row) {
      const profitId = row.id;
      this.profitId = profitId;
      this.getProfitRule(profitId).then(res => {
        this.profitRuleList = res;
        this.title = "编辑公式-" + row.profitName;
        this.open = true;
      })
    },
    // 获取科目列表
    getSubjectList () {
      subjectList({ bookId: this.queryParams.bookId }).then(res => {
        this.subList = res.rows;
      })
    },
    // 科目选择
    subjectSelect (item) {
      this.submitReportForm.subject = item.value;
      this.submitReportForm.subjectId = item.subjectId;
      this.submitReportForm.subjectCode = item.code;
    },
    //科目搜索
    subjectSearch (queryString, callback) {
      let sub = this.subList,
        result;
      if (queryString) {
        if (sub.filter(this.subjectCodeFilter(queryString)).length === 0) {
          result = this.subjectFormat(sub.filter(this.subjectValueFilter(queryString)));
        } else {
          result = this.subjectFormat(sub.filter(this.subjectCodeFilter(queryString))); //搜索编码
        }
      } else {
        result = this.subjectFormat(sub);
      }
      callback(result);
    },
    subjectCodeFilter (queryString) {
      return res => {
        return res.code.indexOf(queryString.toLowerCase()) === 0;
      };
    },
    subjectValueFilter (queryString) {
      return res => {
        return res.name.indexOf(queryString.toLowerCase()) !== -1;
      };
    },
    // 拼接科目数据
    subjectFormat (subjectList) {
      let array = Object.prototype.toString.call(subjectList) === '[object Array]' ? [] : {};
      let arr = subjectList;
      arr.forEach(item => {
        item.value = item.subjectNameBy;//item.code + ' ' + item.name;
      })
      return arr;
    },
    // 添加规则
    addProfitRuleData () {
      if (this.submitReportForm.subject == "" || this.submitReportForm.subject == null) {
        this.msgError("请选择科目");
        return;
      }
      let query = {
        bookId: this.queryParams.bookId, currentPeriod: this.queryParams.currentPeriod, subjectCode: this.submitReportForm.subjectCode, dataRule: this.submitReportForm.dataRule,
        type: this.queryParams.type
      };
      this.getbalanceSheet(query).then(res => {
        let obj = Object.assign(this.submitReportForm, res);
        this.profitRuleList.push(obj);
        this.resetProfitRuleData();
      });
    },
    // 重置添加数据
    resetProfitRuleData () {
      this.submitReportForm = {
        subject: null,
        subjectId: null,
        subjectCode: null,
        sign: '+',
        dataRule: '实际损益额'
      };
    },
    // 通过className获取规则列表当前选中行的索引
    tableRowClassName ({ row, rowIndex }) {
      row.row_index = rowIndex;
    },
    // 删除数据
    delReportData (row) {
      let currIndex = row.row_index;
      this.profitRuleList.splice(currIndex, 1);
    },
    // 获取期末数、年初数
    getbalanceSheet (query) {
      return new Promise(resolve => {
        balanceSheet(query).then(res => {
          resolve(res.data);
        })
      })
    },
    // 保存规则列表
    submitFormReportRule () {
      let data = { bookId: this.queryParams.bookId, profitId: this.profitId };
      data.details = this.handleRuleData(this.profitRuleList);
      saveProfitRule(data).then(res => {
        if (res.code == '200') {
          this.msgSuccess("保存成功");
          this.open = false;
          this.getProfitList();
        }
      })
    },
    // 处理规则列表数据
    handleRuleData (arr) {
      let list = arr;
      let newArr = [];
      list.forEach(item => {
        let obj = {};
        obj.subjectCode = item.subjectCode;
        obj.subject = item.subject;
        obj.sign = item.sign;
        obj.dataRule = item.dataRule;
        newArr.push(obj);
      });
      return newArr;
    },
    // 取消关闭规则弹框
    closeFormReportRule () {
      this.open = false;
      this.resetProfitRuleData();
    },
    // 合计
    getTotal (param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计';
          return;
        }
        const values = data.map(item => Number(item[column.property]));
        const symbols = data.map(item => {
          return item['sign'];
        });
        // 本期合计
        if (column.property === 'currentPeriodHappen') {
          sums[index] = values.reduce((prev, curr, currIndex) => {
            const value = Number(curr);
            const symbol = symbols[currIndex];
            if (!isNaN(value)) {
              if (symbol == '+') {
                return prev + curr;
              } else {
                return prev - curr;
              }

            } else {
              return prev;
            }
          }, 0)
        } else if (column.property === 'yearTotal') {
          // 本年
          sums[index] = values.reduce((prev, curr, currIndex) => {
            const value = Number(curr);
            const symbol = symbols[currIndex];
            if (!isNaN(value)) {
              if (symbol == '+') {
                return prev + curr;
              } else {
                return prev - curr;
              }
            } else {
              return prev;
            }
          }, 0)
        } else {
          sums[index] = '';
        }
      });
      return sums;
    },
    // 分割字符串
    getStr (string, str) {
      let str_before = string.split(str)[0];
      let str_after = string.split(str)[1];
      return { str_before, str_after };
    },
    // 处理季度数据
    quarterData (arr) {
      let list = arr;
      let newArr = [];
      list.forEach((item, index) => {
        item.isCurrentPeriod ? this.queryParams.currentPeriod = item.season : '';
        let obj = {};
        obj.value = item.season;
        let obj1 = this.getStr(item.season, '-');
        obj.label = obj1.str_before + '年第' + obj1.str_after + '季度';
        newArr.push(obj);
      });
      return newArr;
    },
  },
  watch: {
    'queryParams.type' () {
      if (this.queryParams.type == '月报') {
        this.periodsList = [];
        this.getPeriodList();
        this.getCurrentPeriod().then(res => {
          this.queryParams.currentPeriod = res;
        });
      } else {
        this.quartersList = [];
        this.getQuarterList();
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import "~@/assets/styles/variables.scss";
@import "~@/assets/styles/mixin.scss";

[class^="el-icon-"] {
  line-height: 23px !important;
}
[class^="el-icon-"]:hover {
  color: $light-blue !important;
  font-size: 20px !important;
  cursor: pointer;
}
/deep/.el-table th,
.el-table td {
  padding: 5px 0 !important;
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
      .accout-general,
      .general-subject {
        display: flex;
        flex-flow: row nowrap;
        justify-content: flex-start;
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
      .prompt {
        position: relative;
        a {
          color: #fff;
          position: absolute;
          left: 13%;
          top: 40%;
        }
      }
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

  .rule-content {
    display: flex;
    flex-flow: column nowrap;
    justify-content: space-between;
    .rule-top,
    .rule-table,
    .rule-footer {
      margin-bottom: 10px;
    }
    .rule-top {
      display: flex;
      flex-flow: row nowrap;
      align-items: center;
      justify-content: space-between;
      .rule-top-item {
        display: flex;
        align-items: center;
        span {
          padding-right: 2px;
        }
      }
    }
  }
}
a:focus,
a:hover {
  text-decoration: underline !important;
}
.levelStyle0 {
  @include levelStyle(0);
}
.levelStyle1 {
  @include levelStyle(10px);
}
.levelStyle2 {
  @include levelStyle(25px);
}
</style>