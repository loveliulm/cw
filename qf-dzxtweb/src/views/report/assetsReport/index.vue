<template>
  <div class="app-container"
       @click="periodstate=false">
    <div class="general">
      <!-- 头 -->
      <div class="general-top">
        <div class="general-top-left">
          <!-- 期数 -->
          <div class="accout-general">
            <span class="fz4 general-span">资产负债表</span>
            <template>
              <el-select v-model="queryParams.currentPeriod"
                         size="small">
                <el-option v-for="(item,index) in periodsList"
                           :key="index"
                           :label="item.label"
                           :value="item.value">
                </el-option>
              </el-select>
            </template>
          </div>
          <!-- 重分类 -->
          <div class="general-subject">
            <span class="fz4 general-span">报表重分类:</span>
            <el-select style="width:60%;"
                       v-model="queryParams.isClassify"
                       placeholder="报表重分类"
                       size="small"
                       @change="classifyChange">
              <el-option v-for="dict in isClassifyOptions"
                         :key="dict.value"
                         :label="dict.name"
                         :value="dict.value" />
            </el-select>
            <el-tooltip placement="right-end"
                        effect="light">
              <div slot="content">
                <p>重分类：重分类指会计报表的重分类，主要是往来项目。</p>
                <p>根据会计明细科目的期末余额而非总账余额（净值）而定，当资产类往来会计科目期末出现贷方余额时，</p>
                <p>这时不再是债权而是一种债务，应重新分类到负债类科目；反之，当负债类往来科目期末出现借方余额时，</p>
                <p>这时不再是一种债务而是一种债权，应重新分类到资产类科目中去。</p>
              </div>
              <svg-icon icon-class="question"
                        style="color:#f00;font-size:20px;margin-left:5px;" />
            </el-tooltip>
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
      <div class="general-context el-card--custom"
           id="assetsReport">
        <!-- table开始 -->
        <div>
          <el-table :data="assReportList"
                    border
                    stripe
                    height="700"
                    v-loading="loadingData"
                    element-loading-text="拼命加载中"
                    @cell-mouse-enter="showEdit"
                    @cell-mouse-leave="disappearEdit">
            <el-table-column label="资产名称"
                             header-align="center"
                             align="left"
                             min-width="180">
              <template slot-scope="scope">
                <span v-text="scope.row.assetsName"
                      :class="scope.row.assetsLevel == 0?'levelStyle0':scope.row.assetsLevel == 1?'levelStyle1':'levelStyle2'"></span>
                <i class="el-icon-edit"
                   v-if="scope.row.isShow && scope.row.isAssetsRule"
                   @click="handleEdit(scope.row,'AssetsRule')"></i>
              </template>
            </el-table-column>
            <el-table-column label="行次"
                             header-align="center"
                             align="center">
              <template slot-scope="scope">
                <p v-text="scope.row.assetsRowNum"></p>
              </template>
            </el-table-column>
            <el-table-column label="期末数"
                             header-align="center"
                             align="right">
              <template slot-scope="scope">
                <p v-text="scope.row.assetsEndBalance == 0?'':scope.row.assetsEndBalance"></p>
              </template>
            </el-table-column>
            <el-table-column label="年初数"
                             header-align="center"
                             align="right">
              <template slot-scope="scope">
                <p v-text="scope.row.assetsYearInitBalance == 0?'':scope.row.assetsYearInitBalance"></p>
              </template>
            </el-table-column>
            <el-table-column label="负债和所有者(或股东)权益"
                             min-width="200"
                             header-align="center"
                             align="left">
              <template slot-scope="scope">
                <span v-text="scope.row.debtName"
                      :class="scope.row.debtLevel == 0?'levelStyle0':scope.row.debtLevel == 1?'levelStyle1':'levelStyle2'"></span>
                <i class="el-icon-edit"
                   v-if="scope.row.isShow && scope.row.isDebtRule"
                   @click="handleEdit(scope.row,'DebtRule')"></i>
              </template>
            </el-table-column>
            <el-table-column label="行次"
                             header-align="center"
                             align="center">
              <template slot-scope="scope">
                <p v-text="scope.row.debtRowNum"></p>
              </template>
            </el-table-column>
            <el-table-column label="期末数"
                             header-align="center"
                             align="right">
              <template slot-scope="scope">
                <p v-text="scope.row.debtEndBalance == 0?'':scope.row.debtEndBalance"></p>
              </template>
            </el-table-column>
            <el-table-column label="年初数"
                             header-align="center"
                             align="right">
              <template slot-scope="scope">
                <p v-text="scope.row.debtYearInitBalance == 0?'':scope.row.debtYearInitBalance"></p>
              </template>
            </el-table-column>
          </el-table>
        </div>
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
                       @click="addReportData">添加</el-button>
          </div>
          <div class="rule-table">
            <el-table :data="assetsReportRuleList"
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
              <el-table-column label="期末数"
                               prop="endBalance"
                               header-align="center"
                               align="left">
                <template slot-scope="scope">
                  <span v-text="scope.row.endBalance == 0?'':scope.row.endBalance"></span>
                </template>
              </el-table-column>
              <el-table-column label="年初数"
                               prop="yearInitBalance"
                               header-align="center"
                               align="left">
                <template slot-scope="scope">
                  <span v-text="scope.row.yearInitBalance == 0?'':scope.row.yearInitBalance"></span>
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
      <!-- 资产负债平衡提示框 -->
      <!-- <el-dialog title="资产负债表不平衡"
                 :visible.sync="showReport">
        <div class="tip-group">
          <div>
            <svg-icon icon-class="point"
                      title="不平衡"
                      style="color:#f00;font-size:55px;margin:5px 8px 0 0;">
            </svg-icon>
          </div>
          <div>
            <p>{{initialBalance}}</p>
            <p>{{yearInitBalance}}</p>
          </div>

        </div>
        <span slot="footer"
              class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary"
                     @click="dialogVisible = false">确 定</el-button>
        </span>
      </el-dialog> -->
    </div>
    <print-assets v-if="printTableShow" :allTableData="assReportList" :assetsHeadData ="assetsHData" ref="printassetsReport"></print-assets>
  </div>
</template>
<script>
import { currentPeriod, periodList } from "@/api/voucher/viewvoucher";
import { assetsReportList, assetsReportRule, balanceSheet, saveReportRule, exportAssetsReport } from "@/api/report/assetsReport";
import { subjectList } from "@/api/account/settleAccounts";
import printAssets from "@/components/printAssets";
export default {
  name: 'AssetsReport',
  data () {
    return {
      // 查询条件
      queryParams: {
        bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId,
        currentPeriod: null,
        // 报表重分类
        isClassify: true,
      },
      // 会计期间
      periodsList: [],
      // 资产负债列表
      assReportList: [],
      // 打印资产报表表头数据
      assetsHData:{},
      // 数据是否正在加载中
      loadingData: true,
      // 弹出框标题
      title: '',
      // 标志，判断弹框是否弹出
      open: false,
      // 规则列表
      assetsReportRuleList: [],
      // 科目列表
      subList: [],
      // 运算符号列表
      operationSoymbol: [{ id: 1, sign: '+' }, { id: 2, sign: '-' }],
      // 取数规则列表
      dataRules: [{ id: 1, name: '余额', value: '余额' }, { id: 2, name: '借方余额', value: '借方余额' }, { id: 3, name: '贷方余额', value: '贷方余额' }],
      // 编辑公式-添加数据
      submitReportForm: {
        subject: null,
        subjectId: null,
        subjectCode: null,
        sign: '+',
        dataRule: '余额'
      },
      // 规则数据是否正在加载
      loadReportRule: true,
      // 报表重分类选项列表
      isClassifyOptions: [{ name: '重分类', value: true }, { name: '不重分类', value: false }],
      // 资产负债期初余额提示
      initialBalance: '',
      // 资产负债表年初数提示
      yearInitBalance: '',
      // 资产负债弹出框是否显示
      //showReport: false,
      // 打印报表是否显示
      printTableShow:false,
    }
  },
  components:{
    printAssets
  },
  created () {
    this.initData();
    this.assetsHeadData();
    this.getSubjectList();
    this.getPeriodList();
  },
  methods: {
    async initData () {
      let result = await this.getCurrentPeriod();
      this.getAssetsReportList();
    },
    // 打印
    printVoucher () {
      this.printTableShow = true;
      this.$nextTick(() => {
        this.$refs.printassetsReport.printAssets();
      });
    },
    // 导出
    exportExcel () {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出资产负债表所有数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportAssetsReport(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(()=>{
       });
    },
    //获取当前期间
    getCurrentPeriod () {
      return new Promise(resolve => {
        currentPeriod({ bookId: this.queryParams.bookId }).then(res => {
          this.queryParams.currentPeriod = res.data.yearPeriod;
          resolve(res.data);
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
          resolve("success");
        })
      })
    },
    // 拼接打印资产报表表头数据
    assetsHeadData(){
      let headData = new Object();
      headData.customerName = this.$store.state.book.customerName || this.localStorage.getItem('customerName');
      headData.currentPeriodData = Object.keys(this.$store.state.book.currentPeriodData).length == 0?this.localStorage.getItem('currentPeriodData'):this.$store.state.book.currentPeriodData;
      this.assetsHData = headData;
      return headData;
    },
    // 获取负债资产列表
    getAssetsReportList () {
      this.loadingData = true;
      assetsReportList(this.queryParams).then(res => {
        this.assReportList = this.handleListData(res.rows);
        //this.isBalance = res.success;
        this.initialBalance = res.initialBalance;
        this.yearInitBalance = res.yearInitBalance;
        this.loadingData = false;
        //this.showReport = true;// res.success;
        if (!res.success) {
          this.$message({
            showClose: true,
            message: '资产负债表不平衡!<br/>' + res.initialBalance + '!<br/>' + res.yearInitBalance + '!<br/>',
            type: "warning",
            dangerouslyUseHTMLString: true
          });
        }
      })
    },
    // 报表重分类
    classifyChange (selVal) {
      this.queryParams.isClassify = selVal;
    },
    // 查询条件表单提交
    handleQuery () {
      if (this.queryParams.currentPeriod == "" || this.queryParams.currentPeriod == null) {
        this.msgError("请选择账期");
        return;
      }
      this.getAssetsReportList();
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
    // 资产负债平衡框是否显示
    // showMessage () {
    //   this.showReport = true;
    // },
    // 获取规则列表
    getAssetsReportRule (num) {
      this.loadReportRule = true;
      return new Promise(resolve => {
        const rowNum = num;
        let query = Object.assign(this.queryParams, { rowNum });
        assetsReportRule(query).then(res => {
          resolve(res.rows);
          this.loadReportRule = false;
        })
      })
    },
    // 编辑
    handleEdit (row, str) {
      const rowNum = str == 'AssetsRule' ? row.assetsRowNum : row.debtRowNum;
      this.rowNum = rowNum;
      this.getAssetsReportRule(rowNum).then(res => {
        this.assetsReportRuleList = res;
        this.title = str == 'AssetsRule'?'编辑公式-' + row.assetsName:'编辑公式-' + row.debtName;
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
    addReportData () {
      if (this.submitReportForm.subject == "" || this.submitReportForm.subject == null) {
        this.msgError("请选择科目");
        return;
      }
      let query = { bookId: this.queryParams.bookId, currentPeriod: this.queryParams.currentPeriod, subjectCode: this.submitReportForm.subjectCode, dataRule: this.submitReportForm.dataRule };
      this.getbalanceSheet(query).then(res => {
        let obj = Object.assign(this.submitReportForm, res);
        this.assetsReportRuleList.push(obj);
        this.resetAddReportData();
      });
    },
    // 重置添加数据
    resetAddReportData () {
      this.submitReportForm = {
        subject: null,
        subjectId: null,
        subjectCode: null,
        sign: '+',
        dataRule: '余额'
      };
    },
    // 通过className获取规则列表当前选中行的索引
    tableRowClassName ({ row, rowIndex }) {
      row.row_index = rowIndex;
    },
    // 删除数据
    delReportData (row) {
      let currIndex = row.row_index;
      this.assetsReportRuleList.splice(currIndex, 1);
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
      let data = { bookId: this.queryParams.bookId, rowNum: this.rowNum, isClassify: this.queryParams.isClassify };
      data.details = this.handleReportRuleData(this.assetsReportRuleList);
      saveReportRule(data).then(res => {
        if (res.code == '200') {
          this.msgSuccess("保存成功");
          this.open = false;
          this.getAssetsReportList();
        }
      })
    },
    // 处理规则列表数据
    handleReportRuleData (arr) {
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
      this.resetAddReportData();
    },
    // 合计(可能存在问题明早添加数据结束后进行测试)
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
        // 期末数合计
        if (column.property === 'endBalance') {
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
        } else if (column.property === 'yearInitBalance') {
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
    #assetsReport {
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
.tip-group {
  display: flex;
  flex-flow: row nowrap;
  align-items: center;
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