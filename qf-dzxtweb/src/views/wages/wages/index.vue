<template>
  <div class="app-container">
    <div class="wages-wrap">
      <div class="wages-tab">
        <div class="tab" v-hasPermi="['accounting:wage:info']">
          <!--日期限制暂时不知道-->
          <!--:picker-options="pickerOptions"-->
          <!--<el-date-picker v-model="pickerOptionsValue"
                          size="mini"
                          type="month"
                          :editable="false"
                          :clearable="false"
                          value-format="yyyy-MM"
                          placeholder="选择日期"
                          :default-value="currentPeriod"
                          @change="dateChange"
                          style="width:140px;">
          </el-date-picker>-->
          <span class="fz4 general-span">日期</span>
          <template>
            <el-select v-model="pickerOptionsValue"
                       size="small">
              <el-option v-for="(item,index) in periodsList"
                         :key="index"
                         :label="item.label"
                         :value="item.value">
              </el-option>
            </el-select>
          </template>
        </div>
        <div class="tab">
          <el-button size="mini"
                     type="primary"
                     v-hasPermi="['accounting:wage:add']"
                     @click="handleAdd">新增</el-button>
          <el-button size="mini"
                     type="primary"
                     :disabled="single"
                     v-hasPermi="['accounting:wage:edit']"
                     @click="handleUpdate">修改</el-button>
          <el-button size="mini"
                     type="primary"
                     :disabled="multiple"
                     v-hasPermi="['accounting:wage:del']"
                     @click="handleDel">删除</el-button>
          <!-- <el-button size="mini"
                     type="primary"
                     @click="handleUpdate">导入</el-button> -->
          <!-- <el-button size="mini"
                     type="primary"
                     @click="handleUpdate">导出</el-button> -->
        </div>
        <div class="tab">
          <el-button size="mini"
                     v-hasPermi="['accounting:wageVoucherModel:list']"
                     @click="goToWagesModel">凭证模板</el-button>
          <el-button size="mini"
                     type="primary"
                     v-hasPermi="['accounting:wage:provisionWage']"
                     @click="provisionWages">计提工资</el-button>
          <el-button size="mini"
                     type="primary"
                     v-hasPermi="['accounting:wage:grantWage']"
                     @click="grantWages">发放工资</el-button>
          <span>[ 计提工资:<span>{{provision?'已生成':'未生成'}}</span> 发放工资:<span>{{grant?'已生成':'未生成'}}</span> ]</span>
        </div>
      </div>
      <div class="wages-content">
        <el-table :data="wagesList"
                  border
                  style="width: 100%"
                  v-loading="loading"
                  @selection-change="handleSelectionChange">
          <el-table-column type="selection"
                           width="55">
          </el-table-column>
          <el-table-column prop="deptmentName"
                           label="部门">
          </el-table-column>
          <el-table-column prop="personName"
                           label="职员">
          </el-table-column>
          <el-table-column prop="identityCard"
                           label="身份证号">
          </el-table-column>
          <el-table-column prop="wage"
                           label="工资">
          </el-table-column>
          <el-table-column prop="bonus"
                           label="奖金">
          </el-table-column>
          <el-table-column prop="allowance"
                           label="津贴">
          </el-table-column>
          <el-table-column prop="subsidy"
                           label="补贴">
          </el-table-column>
          <el-table-column prop="absent"
                           label="缺勤扣款">
          </el-table-column>
          <el-table-column prop="grossPay"
                           label="应发工资">
          </el-table-column>
          <el-table-column prop="endowmentInsurance"
                           label="基本养老保险费">
          </el-table-column>
          <el-table-column prop="medicareInsurance"
                           label="基本医疗保险费">
          </el-table-column>
          <el-table-column prop="unemployedInsurance"
                           label="失业保险费">
          </el-table-column>
          <el-table-column prop="sicknessInsurance"
                           label="大病保险">
          </el-table-column>
          <el-table-column prop="housingFund"
                           label="住房公积金">
          </el-table-column>
          <el-table-column prop="otherDeductions"
                           label="其他扣除">
          </el-table-column>
          <el-table-column prop="accOtherDeduction"
                           label="累计其他扣除">
          </el-table-column>
          <el-table-column prop="withHoldingTax"
                           label="本月应缴个税">
          </el-table-column>
          <el-table-column prop="netPay"
                           label="实发工资">
          </el-table-column>
        </el-table>
        <div class="content-footer">
          <div class="wagestotal">
            <span>应发工资合计: {{grossPayTotal}}</span>
            <span>实发工资合计: {{netPayTotal}}</span>
          </div>

          <pagination v-show="total > 0"
                      :total="total"
                      :page.sync="queryParams.pageNum"
                      :limit.sync="queryParams.pageSize"
                      @pagination="getWagesList" />
        </div>
      </div>
    </div>
    <!--新增工资-->
    <el-dialog :title="title"
               :visible.sync="openWages"
               :close-on-click-modal="false">
      <el-form ref="form"
               :model="form"
               :rules="rules"
               label-width="100px"
               label-position="left">
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item label="部门"
                          prop="deptmentId">
              <el-select size="mini"
                         v-model="form.deptmentId">
                <el-option v-for="(item, index) in departmentList"
                           :key="index"
                           :label="item.name"
                           :value="item.auxiliaryId"
                           size="small">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="职员"
                          prop="personId">
              <el-select size="mini"
                         v-model="form.personId">
                <el-option v-for="(item, index) in personList"
                           :key="index"
                           :label="item.name"
                           :value="item.auxiliaryId"
                           size="small">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="身份证号"
                          prop="identityCard">
              <el-input v-model="form.identityCard"
                        placeholder="请输入身份证号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item label="工资"
                          prop="wage">
              <el-input-number class="iptnumner"
                               v-model="form.wage"
                               placeholder="请输入工资"
                               :min="0"
                               :controls="false"
                               :precision="2"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="奖金"
                          prop="bonus">
              <el-input-number class="iptnumner"
                               v-model="form.bonus"
                               placeholder="请输入奖金"
                               :min="0"
                               :controls="false"
                               :precision="2"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="津贴"
                          prop="allowance">
              <el-input-number class="iptnumner"
                               v-model="form.allowance"
                               placeholder="请输入津贴"
                               :min="0"
                               :controls="false"
                               :precision="2"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item label="缺勤扣款"
                          prop="absent">
              <el-input-number class="iptnumner"
                               v-model="form.absent"
                               placeholder="请输入缺勤扣款金额"
                               :min="0"
                               :controls="false"
                               :precision="2"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="应发工资"
                          prop="grossPay">
              <el-input-number class="iptnumner"
                               v-model="grossPay"
                               placeholder="请输入应发工资"
                               :min="0"
                               :controls="false"
                               :precision="2"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="基本养老保险"
                          prop="endowmentInsurance">
              <el-input-number class="iptnumner"
                               v-model="form.endowmentInsurance"
                               placeholder="请输入基本养老保险金额"
                               :min="0"
                               :controls="false"
                               :precision="2"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item label="基本医疗保险"
                          prop="medicareInsurance">
              <el-input-number class="iptnumner"
                               v-model="form.medicareInsurance"
                               placeholder="请输入基本医疗保险金额"
                               :min="0"
                               :controls="false"
                               :precision="2"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="失业保险"
                          prop="unemployedInsurance">
              <el-input-number class="iptnumner"
                               v-model="form.unemployedInsurance"
                               placeholder="请输入失业保险金额"
                               :min="0"
                               :controls="false"
                               :precision="2"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="大病保险"
                          prop="sicknessInsurance">
              <el-input-number class="iptnumner"
                               v-model="form.sicknessInsurance"
                               placeholder="请输入大病保险金额"
                               :min="0"
                               :controls="false"
                               :precision="2"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item label="住房公积金"
                          prop="housingFund">
              <el-input-number class="iptnumner"
                               v-model="form.housingFund"
                               placeholder="请输入住房公积金"
                               :min="0"
                               :controls="false"
                               :precision="2"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="其他扣除"
                          prop="otherDeductions">
              <el-input-number class="iptnumner"
                               v-model="form.otherDeductions"
                               placeholder="请输入其他扣除金额"
                               :min="0"
                               :controls="false"
                               :precision="2"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="累计其他扣除"
                          prop="accOtherDeduction">
              <el-input-number class="iptnumner"
                               v-model="form.accOtherDeduction"
                               placeholder="请输入累计其他扣除金额"
                               :min="0"
                               :controls="false"
                               :precision="2"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item label="本月应缴个税"
                          prop="withHoldingTax">
              <el-input-number class="iptnumner"
                               v-model="form.withHoldingTax"
                               placeholder="本月应缴个税"
                               :min="0"
                               :controls="false"
                               :precision="2"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="实发工资"
                          prop="netPay">
              <el-input-number class="iptnumner"
                               v-model="netPay"
                               placeholder="实发工资"
                               :min="0"
                               :controls="false"
                               :precision="2"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer"
           class="dialog-footer">
        <el-button type="primary"
                   @click="submitForm">保 存</el-button>
        <el-button @click="close">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { auxiliaryList, DEPARTMENT, PERSON } from "@/api/system/auxiliary";
import { addWages, wagesList, deleteWages, wagesDetail, editWages, wagesProvision, wagesGrant, pgWages, modelGenerate } from "@/api/wages/wages"
import { currentPeriod, periodList } from "@/api/voucher/viewvoucher";

export default {
  name: 'Wages',
  data () {
    return {
      // 查询条件
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 新增工资弹出框是否打开
      openWages: false,
      // 新增工资信息
      form: {},
      // 校验规则
      rules: {
        personId: [
          { required: true, message: "职员不能为空", trigger: "blur" }
        ],
        identityCard: [
          { required: true, message: "身份证号不能为空", trigger: "blur" }
        ],
        wage: [
          { required: true, message: "工资不能为空", trigger: "blur" }
        ],
        grossPay: [
          { required: true, message: "应发工资不能为空", trigger: "blur" }
        ]
      },
      // bookId
      bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId,
      // 部门列表
      departmentList: [],
      // 职员列表
      personList: [],
      // 工资列表
      wagesList: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 选中行的工资id
      ids: [],
      // 当前账期
      currentPeriod: null,
      // 会计账期
      periodsList: [],
      // 列表数据总条数
      total: 0,
      // 是否正在加载
      loading: true,
      // 应发工资合计
      grossPayTotal: 0,
      // 实发工资合计
      netPayTotal: 0,
      // 计提工资凭证模板是否生成
      provision: false,
      // 发放工资凭证模板是否生成
      grant: false,
      // 查询账期
      pickerOptionsValue: null,
      // 职员名称
      staffMembers: [],
      // 弹框标题
      title:'',
    }
  },
  created () {
    this.initData();
    //this.getCurrentPeriod();
    //this.getWagesList();
    this.getPeriodList();
    this.getDepartmentList();
    this.getPersonList();
    this.isModelGenerate();
  },
  methods: {
    async initData () {
      let currentPeriod = await this.getCurrentPeriod();
      this.getWagesList(currentPeriod);
    },
    // 工资凭证模板
    goToWagesModel () {
      this.$router.push({
        path: '/wagesModel',
      })
    },
    // 获取当前账期
    getCurrentPeriod () {
      return new Promise(resolve => {
        currentPeriod({ bookId: this.bookId }).then(res => {
          this.currentPeriod = res.data.yearPeriod;
          this.pickerOptionsValue = res.data.yearPeriod;
          resolve(res.data.yearPeriod);
        });
      });
    },
    //获取会计期间
    getPeriodList () {
      return new Promise(resolve => {
        periodList({ bookId: this.bookId }).then(res => {
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
    // 获取工资列表
    getWagesList (yearPeriod) {
      this.loading = true;
      let currentPeriod = yearPeriod || this.currentPeriod;
      let queryParams = Object.assign({ bookId: this.bookId }, { yearPeriod: currentPeriod });
      wagesList(queryParams).then(res => {
        this.loading = false;
        this.wagesList = res.rows;
        this.total = res.total;
        this.pgWagesTotal();
      })
    },
    // 应发工资合计、实发工资合计
    pgWagesTotal () {
      pgWages({ bookId: this.bookId }).then(res => {
        this.grossPayTotal = res.grossPayTotal;
        this.netPayTotal = res.netPayTotal;
      })
    },
    // 计提工资凭证模板、发放工资凭证模板是否生成
    isModelGenerate () {
      modelGenerate({ bookId: this.bookId }).then(res => {
        this.provision = res.provision;
        this.grant = res.grant;
      })
    },
    // 表单重置
    reset () {
      this.form = {
        id: undefined,
        deptmentId: null,
        personId: null,
        identityCard: null,
        wage: null,
        bonus: null,
        allowance: null,
        subsidy: null,
        absent: null,
        grossPay: null,
        endowmentInsurance: null,
        medicareInsurance: null,
        unemployedInsurance: null,
        sicknessInsurance: null,
        housingFund: null,
        otherDeductions: null,
        accOtherDeduction: null,
        withHoldingTax: null
      };
      this.resetForm("form");
    },
    // 根据日期查询工资列表
    dateChange (selDate) {
      this.getWagesList(selDate);
    },
    // 工资删除
    handleDel () {
      const ids = this.ids;
      const staffMembers = this.staffMembers;
      this.$confirm(
        '是否确认删除结转职员为"' + staffMembers + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function () {
          return deleteWages(ids);
        })
        .then(() => {
          this.msgSuccess("删除成功");
          this.getWagesList(this.currentPeriod);
          this.pgWagesTotal();
        })
        .catch(function () { });
    },
    // 工资新增
    handleAdd () {
      this.reset();
      this.title = "工资新增";
      this.openWages = true;
    },
    // 工资修改
    handleUpdate () {
      this.reset();
      const id = this.ids;
      wagesDetail(id).then(res => {
        this.form = res.data;
        this.title = '工资编辑';
        this.openWages = true;
      })
    },
    // 保存工资
    submitForm () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id == null || Object.prototype.toString.call(this.form.id) == '[object Undefined]') {
            this.form.bookId = this.bookId;
            this.form.yearPeriod = this.currentPeriod;
            addWages(this.form).then(res => {
              this.msgSuccess("新增成功");
              this.openWages = false;
              this.getWagesList(this.form.yearPeriod);
            })
          } else {
            editWages(this.form).then(res => {
              this.msgSuccess("修改成功");
              this.openWages = false;
              this.getWagesList(this.form.yearPeriod);
            })
          }
        }
      })
    },
    // 关闭弹窗
    close () {
      this.clearFormValidate("form");
      this.openWages = false;
    },
    // 获取部门
    getDepartmentList () {
      let data = { bookId: this.bookId, auxiliaryTypeId: DEPARTMENT };
      auxiliaryList(data).then(res => {
        this.departmentList = res.rows;
      })
    },
    // 获取职员
    getPersonList () {
      let data = { bookId: this.bookId, auxiliaryTypeId: PERSON };
      auxiliaryList(data).then(res => {
        this.personList = res.rows;
      })
    },
    // 多选框选中数据
    handleSelectionChange (selection) {
      this.ids = selection.map(item => item.id);
      this.staffMembers = selection.map(item => item.personName)
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    // 计提工资
    provisionWages () {
      wagesProvision({ bookId: this.bookId }).then(res => {
        if (res.success) {
          this.msgSuccess("计提工资凭证生成成功");
        } else {
          // 计提工资凭证模板生成失败后跳转到录凭证，此功能暂不使用
          /*this.$router.push({
            path: '/voucher'
          })*/
          this.msgError(res.msg);
        }
        this.isModelGenerate();
      })
    },
    // 发放工资
    grantWages () {
      wagesGrant({ bookId: this.bookId }).then(res => {
        if (res.success) {
          this.msgSuccess("发放工资凭证生成成功");
        } else {
          this.msgError(res.msg);
        }
        this.isModelGenerate();
      })
    },
  },
  computed: {
    grossPay: {
      get () {
        let wage = Object.prototype.toString.call(this.form.wage) == '[object Undefined]' || this.form.wage == null || this.form.wage == "" ? 0 : parseFloat(this.form.wage);
        let bonus = Object.prototype.toString.call(this.form.bonus) == '[object Undefined]' || this.form.bonus == null || this.form.bonus == "" ? 0 : parseFloat(this.form.bonus);
        let allowance = Object.prototype.toString.call(this.form.allowance) == '[object Undefined]' || this.form.allowance == null || this.form.allowance == "" ? 0 : parseFloat(this.form.allowance);
        let subsidy = Object.prototype.toString.call(this.form.subsidy) == '[object Undefined]' || this.form.subsidy == null || this.form.subsidy == "" ? 0 : parseFloat(this.form.subsidy);
        let absent = Object.prototype.toString.call(this.form.absent) == '[object Undefined]' || this.form.absent == null || this.form.absent == "" ? 0 : parseFloat(this.form.absent);
        let grossPay = (wage + bonus + allowance + subsidy - absent) == 0 ? '' : (wage + bonus + allowance + subsidy - absent);
        this.form.grossPay = grossPay;
        return grossPay;
      },
      set () { }
    },
    netPay: {
      get () {
        // 实发工资 = 应发工资 - 基本养老保险 - 基本医疗保险 - 失业保险 - 大病保险 - 住房公积金 - 其他扣除 - 本月应交个税
        let gross = this.grossPay;
        let endowmentInsurance = Object.prototype.toString.call(this.form.endowmentInsurance) == '[object Undefined]' || this.form.endowmentInsurance == null || this.form.endowmentInsurance == "" ? 0 : parseFloat(this.form.endowmentInsurance);
        let medicareInsurance = Object.prototype.toString.call(this.form.medicareInsurance) == '[object Undefined]' || this.form.medicareInsurance == null || this.form.medicareInsurance == "" ? 0 : parseFloat(this.form.medicareInsurance);
        let unemployedInsurance = Object.prototype.toString.call(this.form.unemployedInsurance) == '[object Undefined]' || this.form.unemployedInsurance == null || this.form.unemployedInsurance == "" ? 0 : parseFloat(this.form.unemployedInsurance);
        let sicknessInsurance = Object.prototype.toString.call(this.form.sicknessInsurance) == '[object Undefined]' || this.form.sicknessInsurance == null || this.form.sicknessInsurance == "" ? 0 : parseFloat(this.form.sicknessInsurance);
        let housingFund = Object.prototype.toString.call(this.form.housingFund) == '[object Undefined]' || this.form.housingFund == null || this.form.housingFund == "" ? 0 : parseFloat(this.form.housingFund);
        let otherDeductions = Object.prototype.toString.call(this.form.otherDeductions) == '[object Undefined]' || this.form.otherDeductions == null || this.form.otherDeductions == "" ? 0 : parseFloat(this.form.otherDeductions);
        let withHoldingTax = Object.prototype.toString.call(this.form.withHoldingTax) == '[object Undefined]' || this.form.withHoldingTax == null || this.form.withHoldingTax == "" ? 0 : parseFloat(this.form.withHoldingTax);

        let netPay = (gross - endowmentInsurance - medicareInsurance - unemployedInsurance - sicknessInsurance - housingFund - otherDeductions - withHoldingTax) == 0 ? '' : (gross - endowmentInsurance - medicareInsurance - unemployedInsurance - sicknessInsurance - housingFund - otherDeductions - withHoldingTax);
        this.form.netPay = netPay;
        return netPay;
      },
      set () { }
    }
  }
} 
</script>
<style lang="scss" scoped>
.wages-wrap {
  display: flex;
  -ms-flex-direction: column;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  overflow: hidden;
  padding: 12px 20px;
  box-sizing: border-box;
  .wages-tab {
    width: 98%;
    display: flex;
    flex-flow: row wrap;
    justify-content: space-between;
    align-items: center;
    margin: 3px 10px;
    span {
      font-size: 15px;
      color: #606266;
    }
  }
  .wages-content {
    width: 98%;
    margin: 3px 10px;
    .content-footer {
      display: flex;
      flex-flow: row wrap;
      justify-content: space-between;
      align-items: center;
      margin-top: 15px;
      span {
        margin-right: 40px;
        font-size: 15px;
        color: #606266;
      }
    }
  }
}
.iptnumner {
  width: 100% !important;
}
.pagination-container {
  margin-top: 0 !important;
}
/deep/.el-input-number.is-without-controls .el-input__inner {
  text-align: left !important;
}
</style>