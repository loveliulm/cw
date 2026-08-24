<template>
  <div class="app-container">
    <div class="viewvoucher-wrap">
      <div class="viewvoucher-title">
        <h3 style="font-weight:400;">查看凭证</h3>
        <p>
          当前账期：{{ currentPeriod.str_before }}年{{
            currentPeriod.str_after
          }}期
        </p>
      </div>
      <div class="viewvoucher-tab">
        <div class="tab-left">
          <el-popover placement="bottom-start"
                      width="400"
                      transition="fade-in-linear"
                      v-model="visible">
            <el-form ref="queryForm"
                     :model="queryParams"
                     label-position="left"
                     label-width="80px">
              <el-form-item label="会计期间">
                <el-col :span="11">
                  <el-date-picker v-model="queryParams.startDate"
                                  type="month"
                                  value-format="yyyy-MM"
                                  :picker-options="pickerOptions1"
                                  placeholder="开始月份"
                                  style=" width:100%;">
                  </el-date-picker>
                </el-col>
                <el-col :span="2"><span style="display:flex;justify-content:center;">至</span></el-col>
                <el-col :span="11">
                  <el-date-picker v-model="queryParams.endDate"
                                  type="month"
                                  value-format="yyyy-MM"
                                  :picker-options="pickerOptions2"
                                  placeholder="结束月份"
                                  style=" width:100%;">
                  </el-date-picker>
                </el-col>
              </el-form-item>
              <el-form-item label="科目">
                <el-select v-model="queryParams.subjectId"
                           placeholder="科目"
                           clearable
                           size="small">
                  <el-option v-for="dict in subejctTypes"
                             :key="dict.subjectId"
                             :label="dict.code + ' ' + dict.name"
                             :value="dict.subjectId" />
                </el-select>
              </el-form-item>
              <el-form-item label="摘要">
                <el-input v-model="queryParams.summary"
                          placeholder="请输入摘要"
                          clearable
                          size="small"
                          @keyup.enter.native="handleQuery"></el-input>
              </el-form-item>
              <el-form-item label="辅助核算">
                <el-col :span="10">
                  <div style="margin-right:5px;">
                    <el-select v-model="queryParams.auxiliaryTypeId"
                               placeholder="辅助核算"
                               clearable
                               @change="getAuxiliarys"
                               size="small">
                      <el-option label="(空)"
                                 value=""></el-option>
                      <el-option v-for="dict in auxiliaryTypes"
                                 :key="dict.auxiliaryTypeId"
                                 :label="dict.auxiliaryTypeText"
                                 :value="dict.auxiliaryTypeId" />
                    </el-select>
                  </div>
                </el-col>
                <el-col :span="14">
                  <el-select v-model="queryParams.auxiliaryId"
                             placeholder="辅助核算名称"
                             clearable
                             size="small">
                    <el-option label="全部"
                               value="全部"></el-option>
                    <el-option v-for="dict in auxiliarys"
                               :key="dict.auxiliaryId"
                               :label="dict.name"
                               :value="dict.auxiliaryId" />
                  </el-select>
                </el-col>
              </el-form-item>
              <el-form-item label="金额">
                <el-col :span="11">
                  <el-input v-model="queryParams.smallMoney"
                            placeholder="最低金额"
                            clearable
                            size="small"
                            @keyup.enter.native="handleQuery"></el-input>
                </el-col>
                <el-col :span="2"><span style="display:flex;justify-content:center;">-</span></el-col>
                <el-col :span="11">
                  <el-input v-model="queryParams.bigMoney"
                            placeholder="最高金额"
                            clearable
                            size="small"
                            @keyup.enter.native="handleQuery"></el-input>
                </el-col>
              </el-form-item>
              <el-form-item label="凭证字">
                <el-col :span="10">
                  <div style="margin-right:5px;">
                    <el-select v-model="voucherWord"
                               placeholder="凭证字"
                               clearable
                               size="small">
                      <el-option label="全部"
                                 value=""></el-option>
                      <el-option v-for="dict in voucherWords"
                                 :key="dict.id"
                                 :label="dict.name"
                                 :value="dict.name" />
                    </el-select>
                  </div>
                </el-col>
                <el-col :span="14">
                  <el-input v-model="voucherNo"
                            placeholder="请输入凭证号"
                            clearable
                            size="small"
                            @keyup.enter.native="handleQuery"></el-input>
                </el-col>
              </el-form-item>
            </el-form>
            <div style="text-align: right; margin: 0">
              <el-button type="default"
                         size="mini"
                         @click="resetQuery">重置</el-button>
              <el-button type="primary"
                         size="mini"
                         @click="handleQuery">搜索</el-button>
            </div>
            <el-button slot="reference" v-hasPermi="['accounting:voucher:info']">查询更多</el-button>
            <el-button slot="reference" @click="backTo">返回</el-button>
          </el-popover>
        </div>
        <div class="tab-right">
          <el-button type="success"
                     icon="el-icon-check"
                     size="mini"
                     :disabled="multiple"
                     v-hasPermi="['accounting:voucher:review']"
                     @click="handleApprove">审核</el-button>
          <el-button type="info"
                     icon="el-icon-close"
                     size="mini"
                     :disabled="multiple"
                     v-hasPermi="['accounting:voucher:review']"
                     @click="handleUnApprove">反审核</el-button>
          <el-button type="warning"
                     icon="el-icon-delete"
                     size="mini"
                     :disabled="multiple"
                     v-hasPermi="['accounting:voucher:del']"
                     @click="handleDel">删除</el-button>
          <el-button type="primary"
                     icon="el-icon-notebook-2"
                     size="mini"
                     v-hasPermi="['accounting:voucher:copy']"
                     :disabled="multiple"
                     @click="handleCopy">复制</el-button>
          <el-button type="primary"
                     size="mini"
                     v-hasPermi="['accounting:voucher:print']"
                     @click="printVoucher">打印</el-button>
        </div>
      </div>
      <div class="viewvoucher-content">
        <el-table :data="voucherlist"
                  style="width: 100%"
                  max-height="680"
                  border
                  v-loading="loading"
                  :row-style="tableRowClassName"
                  @sort-change="sortChange"
                  @selection-change="handleSelectionChange">
          <el-table-column type="selection"
                           width="55"> </el-table-column>
          <el-table-column label="日期"
                           :sortable="'custom'"
                           width="100">
            <template slot-scope="scope">
              <span class="common">{{ scope.row.voucherDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="凭证字号"
                           width="100">
            <template slot-scope="scope">
              <span class="common">{{ scope.row.voucherCode }}</span>
            </template>
          </el-table-column>
          <el-table-column label="摘要"
                           style="padding:0;">
            <template slot-scope="scope">
              <p :class="
                  i === scope.row.details.length - 1 ? 'lastdetail' : 'detail'
                "
                 v-for="(item, i) of scope.row.details"
                 :key="item.voucherRecordId">
                {{ item.summary }}
              </p>
            </template>
          </el-table-column>
          <el-table-column label="科目"
                           min-width="400">
            <template slot-scope="scope">
              <p :class="
                  i === scope.row.details.length - 1 ? 'lastdetail' : 'detail'
                "
                 v-for="(item, i) of scope.row.details"
                 :key="item.voucherRecordId">
                {{ item.subjectNameBy }}
                <span v-for="auxiliary of item.auxiliarys"
                      class="auxiliary-group"
                      :key="auxiliary.auxiliaryId"><span class="auxiliary-name">{{
                    auxiliary.auxiliaryTypeText
                  }}</span><span class="auxiliary-project">{{
                    auxiliary.name
                  }}</span></span>
              </p>
            </template>
          </el-table-column>
          <el-table-column label="借方金额">
            <template slot-scope="scope">
              <p :class="
                  i === scope.row.details.length - 1 ? 'lastdetail' : 'detail'
                "
                 v-for="(item, i) of scope.row.details"
                 :key="item.voucherRecordId">
                {{ item.debit == 0 ? "" : item.debit }}
              </p>
            </template>
          </el-table-column>
          <el-table-column label="贷方金额">
            <template slot-scope="scope">
              <p :class="
                  i === scope.row.details.length - 1 ? 'lastdetail' : 'detail'
                "
                 v-for="(item, i) of scope.row.details"
                 :key="item.voucherRecordId">
                {{ item.credit == 0 ? "" : item.credit }}
              </p>
            </template>
          </el-table-column>
          <el-table-column label="审核状态">
            <template slot-scope="scope">
              <span :class="scope.row.status == 1 ? 'successColor' : 'common'">{{ scope.row.status == 1 ? "已审核" : "未审核" }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作"
                           align="center"
                           width="180"
                           class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini"
                         type="text"
                         icon="el-icon-edit"
                         v-hasPermi="['accounting:voucher:info']"
                         @click="handleUpdate(scope.row)">修改</el-button>
              <el-button size="mini"
                         type="text"
                         icon="el-icon-delete"
                         v-hasPermi="['accounting:voucher:del']"
                         @click="handleDel(scope.row)">删除</el-button>
              <el-button size="mini"
                         type="text"
                         icon="el-icon-key"
                         v-hasPermi="['accounting:voucher:info']"
                         @click="handleUpdate(scope.row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0"
                    :total="total"
                    :page.sync="queryParams.pageNum"
                    :limit.sync="queryParams.pageSize"
                    @pagination="getVoucherList" />
      </div>
    </div>
    <all-print-voucher :allData='allVoucher'
                       ref="printAll">

    </all-print-voucher>
  </div>
</template>
<script>
import {
  voucherList,
  delVoucher,
  approveVoucher,
  unApproveVoucher,
  copyVoucher,
  currentPeriod,
  periodList,
  getSubjectVoucher,
  voucherListByIds
} from "@/api/voucher/viewvoucher";
import { auxiliaryType, auxiliaryList } from "@/api/system/auxiliary";
import allPrintVoucher from "@/components/print/allPrintVoucher";

export default {
  name: "Viewvoucher",
  components: {
    allPrintVoucher
  },
  data () {
    return {
      // 凭证列表
      voucherlist: [],
      // 查询条件框
      visible: false,
      //
      // 总条数
      total: 0,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId,
        startDate: null,
        endDate: null,
        auxiliaryTypeId: "",
        auxiliaryId: "全部",
        voucherCode: "",
        order: "asc",
      },
      //需要打印的凭证
      needPrintData: [],
      //拼凑打印的数据
      allVoucher: [],
      // 选中数组
      ids: [],
      // 选中数组凭证字号
      vcodes:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 当前账期
      currentPeriod: {},
      // 账期列表
      avaiableDateList: [],
      // 会计账期选择
      pickerOptions1: {
        disabledDate: time => {
          let date = this.parseTime(time, "{y}-{m}");
          return !this.avaiableDateList.includes(date);
        }
      },
      pickerOptions2: {
        disabledDate: time => {
          return (
           time.getTime() < new Date(this.queryParams.startDate).getTime()
          );
        }
      },
      // 是否加载
      loading: true,
      // 辅助核算类型
      auxiliaryTypes: [],
      // 辅助核算
      auxiliarys: [],
      // 凭证字列表
      voucherWords: [{ id: 1, name: "记" }],
      // 凭证字
      voucherWord: "",
      // 凭证号
      voucherNo: null,
      selectRow: [],
      selectData: [],
      // 科目类型
      subejctTypes: []
    };
  },
  created () {
    this.getSubjectList();
    //this.initData();
    this.getPickerDate();
    this.getSubjectType();
    this.getAuxiliaryType();
  },
  // 判断路由：是从哪个页面跳转过来的
  beforeRouteEnter (to, from, next) {
    console.log(from.path);
    next(vm => {
      // 如果从工资路由跳转过来的，显示计提工资或发放工资凭证模板数据
      if (from.path == "/settleAccounts") {
        let voucherIds = vm.$route.query.voucherIds;
        vm.initData(voucherIds);
      } else if (from.path == '/index') {
        Object.prototype.toString.call(vm.$route.query.voucherIds) == '[object Undefined]' ? vm.initData() : vm.initData(vm.$route.query.voucherIds);
      }
      else {
        vm.initData();
      }
    });
  },
  methods: {
    //凭证打印
    printVoucher () {
      if (this.needPrintData.length == 0) {
        this.$message.warning("请选择需要打印的凭证")
      } else {
        this.needPrintData.forEach((a, j) => {
          let trData = a.details;
          let listData = [];//每条凭证的列数据
          let hasCur = false;//是否外币
          let total = a.creditTotal; //合计
          //需要打印的数据过滤
          trData.forEach((n, i) => {
            // total += n.creditAmount;
            let curName = '';
            //先判断是否有外币，如果没有外币则显示空，有外币匹配过滤出外币名称eg: RMB
            if (n.currentcy != null) {
              //外币，只要一个凭证里面有一行有外币，其他都带外币
              hasCur = true;
              //TODO 查询外币名称

            } else {
              curName = "";
            };
            if (n.auxiliarys.length > 0) {
              n.auxiliarys.forEach((m, k) => {
                listData[i] = {
                  abstract: n.summary,//摘要
                  subject: n.subjectNameBy + "_" + m.name,
                  currentcyName: curName,//是否有外币，有的话匹配过滤壶外币名称eg:RMB
                  exchangeRate: "",//TODO汇率
                  original: "", //原币
                  crVal: n.credit == null ? "" : n.credit,
                  deVal: n.debit == null ? "" : n.debit
                };
              })
            } else {
              listData[i] = {
                abstract: n.summary,//摘要
                subject: n.subjectNameBy,
                currentcyName: curName,//是否有外币，有的话匹配过滤壶外币名称eg:RMB
                exchangeRate: "",//TODO汇率
                original: "", //原币
                crVal: n.credit == null ? "" : n.credit,
                deVal: n.debit == null ? "" : n.debit
              };
            }


          });
          this.allVoucher[j] = {
            //单个凭证打印数据
            voucherHFdata: {
              voucherTitle: '记账凭证',//暂时只有记账凭证，后期需要后台设计出凭证类别，返回不同的凭证打印标题
              billNum: "", //暂时没有设计附件数，默认为0
              accbookName: '',//账套名称  暂时为返回，后期后台返回保存入this.$store.state.accbookName ,直接取
              date: a.voucherDate,//日期
              voucherNumber: a.voucherWord + "-" + a.voucherNo,//凭证号 记-1
              total: total,
              //尾部
              supervisor: '',//主管
              cashier: '',//出纳
              auditor: '',//审核
              producer: a.createUserName //制单

            },
            listData: listData,
            hasCurL: hasCur
          };
        });
        this.$nextTick(() => {
          this.$refs.printAll.printvoucher();
        })
      }
    },
    async initData (voucherIds) {
      let currentPeriod = await this.getCurrentPeriod();
      this.queryParams.startDate = currentPeriod.yearPeriod;
      this.queryParams.endDate = currentPeriod.yearPeriod;
      this.currentPeriod = this.getStr(this.queryParams.startDate, "-");
      Object.prototype.toString.call(voucherIds) != '[object Undefined]' ? this.getVoucherList(voucherIds) : this.getVoucherList();
    },
    // 修改凭证
    handleUpdate (row) {
      this.$router.push({
        path: '/voucherDetail/voucherDetail',
        query: {
          voucherId: row.voucherId
        }
      })
    },
    // 查询凭证列表
    getVoucherList (voucherIds) {
      this.loading = true;
      Object.prototype.toString.call(voucherIds) == '[object Object]' ? voucherIds = undefined : '';
      voucherList(this.queryParams, voucherIds).then(res => {
        this.voucherlist = res.rows;
        this.total = res.total;
        this.loading = false;
      });
    },
    // 获取当前账期
    getCurrentPeriod () {
      return new Promise(resolve => {
        currentPeriod({ bookId: this.queryParams.bookId }).then(res => {
          resolve(res.data);
        });
      });
    },
    // 获取账期列表
    getPeriodList () {
      return new Promise(resolve => {
        periodList({ bookId: this.queryParams.bookId }).then(res => {
          resolve(res.rows);
        });
      });
    },
    // 获取可选择的日期、当前账期
    pickerDate () {
      return new Promise(resolve => {
        this.getPeriodList().then(res => {
          let array = [];
          res.forEach(item => {
            array.push(item.yearPeriod);
          });
          resolve(array);
        });
      });
    },
    // 获取科目类型
    getSubjectType () {
      getSubjectVoucher({ bookId: this.queryParams.bookId }).then(res => {
        this.subejctTypes = res.rows;
      });
    },
    // 拼凑数据
    getPickerDate () {
      this.pickerDate().then(res => {
        this.avaiableDateList = res;
      });
    },

    // 获取辅助核算类型
    getAuxiliaryType () {
      auxiliaryType().then(res => {
        this.auxiliaryTypes = res.rows;
      });
    },

    // 获取辅助核算
    getAuxiliarys (selVal) {
      let data = Object.assign({
        bookId: this.queryParams.bookId,
        auxiliaryTypeId: selVal
      });
      if (selVal != "") {
        auxiliaryList(data).then(res => {
          this.auxiliarys = res.rows;
        });
      } else {
        this.auxiliarys = [];
      }
    },
    // 日期排序
    sortChange (column) {
      let order = column.order === "descending" ? "asc" : "desc";
      this.queryParams.order = order;
      this.getVoucherList();
    },
    // 搜索按钮操作
    handleQuery (str) {
      this.queryParams.page = 1;
      if (str == "reset") {
        this.voucherNo = "";
        this.queryParams.voucherCode = "";
      } else {
        this.queryParams.voucherCode = this.voucherCode;
      }
      this.getVoucherList();
      this.visible = false;
    },
    // 重置按钮操作
    resetQuery () {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId,
        startDate: null,
        endDate: null,
        auxiliaryTypeId: "",
        auxiliaryId: "全部",
        smallMoney: null,
        bigMoney: null,
        voucherCode: ""
      };
      this.resetForm("queryForm");
      this.handleQuery("reset");
    },
    // 多选框选中数据
    handleSelectionChange (selection) {
      this.ids = selection.map(item => item.voucherId);
      this.vcodes = selection.map(item => item.voucherCode);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
      this.selectData = selection;

      this.needPrintData = [...selection]
    },

    // 删除凭证
    // 删除按钮操作
    handleDel (row) {
      const vouchesrIds = row.voucherId || this.ids;
      const voucherCodes = row.voucherCode || this.vcodes;
      this.$confirm(
        '是否确认删除凭证字号为"' + voucherCodes + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function () {
          return delVoucher(vouchesrIds);
        })
        .then(() => {
          this.getVoucherList();
          this.msgSuccess("删除成功");
        })
        .catch(function () { });
    },

    // 审核
    handleApprove (row) {
      const vouchesrIds = row.voucherId || this.ids;
      approveVoucher(vouchesrIds).then(res => {
        if (res.code === 200) {
          this.msgSuccess("审核成功");
          this.getVoucherList();
        }
      });
    },

    // 反审核
    handleUnApprove (row) {
      const vouchesrIds = row.voucherId || this.ids;
      unApproveVoucher(vouchesrIds).then(res => {
        if (res.code === 200) {
          this.msgSuccess("反审核成功");
          this.getVoucherList();
        }
      });
    },

    // 复制
    handleCopy (row) {
      const vouchesrIds = row.voucherId || this.ids;
      copyVoucher(vouchesrIds).then(res => {
        if (res.code === 200) {
          this.msgSuccess("复制成功");
          this.getVoucherList();
        }
      });
    },

    // 分割字符串
    getStr (string, str) {
      let str_before = string.split(str)[0];
      let str_after = string.split(str)[1];
      return { str_before, str_after };
    },
    // 变色样式监听
    tableRowClassName ({ row, rowIndex }) {
      if (this.selectRow.includes(rowIndex)) {
        return { "background-color": "#dbedfd!important" };
      }
    },
    // 关闭窗口
    backTo () {
      this.$router.push({
        path: "/settleAccounts"
      });
    },
  },
  computed: {
    voucherCode () {
      if (this.voucherWord !== "") {
        return this.voucherWord + "-" + this.voucherNo;
      } else {
        return "";
      }
    }
  },
  watch: {
    selectData (data) {
      this.selectRow = [];
      if (data.length > 0) {
        data.forEach((item, index) => {
          this.selectRow.push(this.voucherlist.indexOf(item));
        });
      }
    }
  }
};
</script>
<style lang="scss" scoped>
@import "~@/assets/styles/variables.scss";

/deep/ .el-table td {
  padding: 0 !important;
}
/deep/ .el-table .cell {
  padding-left: 0 !important;
  padding-right: 0 !important;
}
/deep/ .el-table th > .cell {
  padding-left: 10px !important;
  padding-right: 10px !important;
}
/deep/ .el-table th:nth-child(1) > .cell {
  padding-left: 0px !important;
  padding-right: 10px !important;
}
/deep/ .el-table__body tr:hover > td {
  background-color: #dbedfd !important;
}
.pagination-container {
  margin-bottom: 15px !important;
}
/deep/ .cell .el-checkbox {
  padding-left: 10px !important;
}

.viewvoucher-wrap {
  display: flex;
  -ms-flex-direction: column;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  overflow: hidden;
  box-sizing: border-box;
  .viewvoucher-title {
    width: 98%;
    margin: 3px 0;
    display: flex;
    flex-flow: column nowrap;
    justify-content: center;
    align-items: center;
    h3 {
      height: 20px;
      margin: 3px 0;
    }
    p {
      margin: 3px 0;
    }
  }
  .viewvoucher-tab {
    width: 98%;
    height: 40px;
    margin: 3px 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .tab-left {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
  .viewvoucher-content {
    width: 98%;
    margin: 3px 10px;
    .common {
      padding-left: 10px;
      padding-right: 10px;
    }
    .lastdetail {
      margin: 0;
      width: 100%;
      height: 32px;
      line-height: 32px;
      padding-left: 10px;
      padding-right: 10px;
    }
    .detail {
      margin: 0;
      width: 100%;
      height: 32px;
      line-height: 32px;
      border-bottom: 1px solid #dfe6ec;
      padding-left: 10px;
      padding-right: 10px;
    }
  }
}
.successColor {
  color: $colorSucess;
  padding-left: 10px;
  padding-right: 10px;
}
.auxiliary-group {
  font-size: 8px;
  margin-right: 2px;
  .auxiliary-name {
    background: #dbedfd;
    padding: 4px;
    border-top: 1px solid $gray;
    border-left: 1px solid $gray;
    border-bottom: 1px solid $gray;
    border-top-left-radius: 4px;
    border-bottom-left-radius: 4px;
  }
  .auxiliary-project {
    padding: 4px;
    border-top: 1px solid $gray;
    border-right: 1px solid $gray;
    border-bottom: 1px solid $gray;
    border-top-right-radius: 4px;
    border-bottom-right-radius: 4px;
  }
}
</style>
