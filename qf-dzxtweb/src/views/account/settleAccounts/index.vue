<template>
  <div class="app-container">
    <div class="account-wrap">
      <!-- <div class="account-title">
        <h3>结账</h3>
      </div> -->
      <div class="account-tab">
        <div class="tab-left">
          <div class="tab-message">
            <span>{{currentPeriod.str_before}}年第{{currentPeriod.str_after}}期共录入凭证{{voucherNum}}张,</span>
            <span v-if="!isBalance">财务初始余额试算<span style="color:#f00;padding-left:0;">不平衡</span></span>
            <span v-else>财务初始余额试算平衡</span>
          </div>
          <input v-model="checkAll"
                 type="checkbox"
                 @change="changeState" />全选
        </div>
        <div class="tab-right">
          <el-button type="info"
                     size="small"
                     :disabled="!multiple"
                     @click="calculationCheck">重新测算</el-button>
          <!--<el-button type="primary"
                     size="small"
                     :disabled="multiple"
                     @click="handleDelete">生成凭证</el-button>-->
          <el-button type="primary"
                     size="small"
                     v-hasPermi="['accounting:carryover:carryoverNextPeriod']"
                     @click="checkOut">结账</el-button>
          <el-button type="primary"
                     size="small"
                     v-hasPermi="['accounting:carryover:backPeriod']"
                     @click="reserveCheckOut">反结账</el-button>
        </div>
      </div>
      <div class="account-content"
           v-loading='loading'>
        <ul>
          <li class="item"
              v-for="(carryover,index) of carryoverList"
              :key="carryover.carryoverId">
            <div class="box-title">
              <input type="checkbox"
                     style="color:#00b8ec;"
                     v-model="checkModel"
                     :value="carryover.carryoverId">
              <span class="title-name">{{carryover.text}}</span>
              <div>
                <span class="el-icon-edit setting"
                      v-if="carryover.showRule"
                      v-hasPermi="['accounting:carryover:edit']"
                      @click="editCarrayover(carryover.carryoverId,carryover.code)"></span>

                <span class="el-icon-setting setting"
                      v-if="!carryover.showRule && (carryover.code =='salesCost'||  carryover.code=='carryForward')"
                      slot="reference"
                      @click="settingCarrayovery(carryover.carryoverId,carryover.code)"></span>
                <span class="el-icon-delete setting"
                      v-if="!carryover.isModel"
                      v-hasPermi="['accounting:carryover:del']"
                      @click="delCarryover(carryover.carryoverId,carryover.text)"></span>
                <div v-if="!carryover.showRule && carryover.code =='salesCost'"
                     style="width:100%;">
                  <!--结转销售成本设置百分比-->
                  <el-popover placement="top"
                              width="180"
                              v-model="openSales">
                    <div class="sales">
                      <span style="font-size:13px;padding-right:10px;">结转百分比:</span>
                      <el-input-number v-model="salesPercentage"
                                   :min="0"
                                   :controls="false"
                                   :precision="0"
                                   style="width:40%;"
                                   size="mini"></el-input-number>
                      <span>%</span>
                    </div>
                    <div style="text-align: right; margin: 0">
                      <el-button size="mini"
                                 type="text"
                                 @click="cancelSales">取消</el-button>
                      <el-button type="primary"
                                 size="mini"
                                 @click="submitSales(carryover.carryoverId)">确定</el-button>
                    </div>
                  </el-popover>
                </div>
                <div v-if="!carryover.showRule && carryover.code =='carryForward'"
                     style="width:100%;">
                  <!--结转利润-->
                  <el-popover placement="top"
                              width="180"
                              v-model="openProfitLoss">
                    <div class="sales">
                      <p style="font-size:13px;padding-right:10px;">
                        按收益和损失分别转入本年利润科目
                        <el-checkbox v-model="profitLossOpen"></el-checkbox>
                      </p>
                    </div>
                    <div style="text-align: right; margin: 0">
                      <el-button size="mini"
                                 type="text"
                                 @click="cancelProfitLoss">取消</el-button>
                      <el-button type="primary"
                                 size="mini"
                                 @click="submitProfitLoss(carryover.carryoverId)">确定</el-button>
                    </div>
                  </el-popover>
                </div>
              </div>
            </div>
            <div class="box-content">
              <div class="content-money">
                <span>金额：￥{{carryover.money}}</span>
                <div>
                  <span>{{carryover.voucher1 == null?'':carryover.voucher1.voucherCode}}</span>
                  <span>{{carryover.voucher2 == null?'':carryover.voucher2.voucherCode}}</span>
                </div>
                <span v-if="carryover.money == 0"
                      style="font-size:10px;color:#1890ff;padding-top:8px;">无需生成凭证</span>
              </div>
            </div>
            <a v-if="carryover.voucher1!=null || carryover.voucher2!=null"
               class="btn-viewvoucher"
               target="_blank"
               @click="viewCarrayover(carryover)">查看凭证</a>
            <a v-else
               class="btn-voucher"
               v-hasPermi="['accounting:carryover:generate']"
               @click="generateCarrayover(carryover.carryoverId,index)">生成凭证</a>

          </li>
          <li class="item-add"
              v-hasPermi="['accounting:carryover:add']"
              @click="addCarryover">
            <div class="plus-wrap">
              <div class="plus">
                <span class="el-icon-plus"
                      style="font-size:50px;"></span>
              </div>
            </div>
            <p>自定义结转方案</p>
          </li>
        </ul>
      </div>
    </div>
    <!-- 添加期末结转凭证模板-->
    <!-- 保存凭证模板配置对话框 -->
    <el-dialog title="期末结转凭证模板"
               :visible.sync="open"
               width="1000px"
               @close="close"
               append-to-body>
      <el-form ref="form"
               :model="form"
               :rules="rules"
               label-width="120px"
               label-position="left">
        <el-form-item label="结转方案名称"
                      prop="carryoverName">
          <el-input v-model="form.carryoverName"
                    :disabled="isDisabled"
                    placeholder="请输入结转方案名称" />
        </el-form-item>
        <div>
          <carryover-rule :tableData="tableData"
                          :subjectList="subjectList"
                          :sourceSubjectList="sourceSubjectList"
                          :isProfit="isProfit"
                          :carryoverCode="carryoverCode"
                          @addtr="addtr"
                          @deltr="deltr"></carryover-rule>
        </div>
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
import {
  carryoverList, subjectList, saveCarryover, carryoverDetail, deleteCarryover, editCarryover, carryoverCalculation, saleOfPercentage, setSaleOfPercentage,
  setProfitLoss, carryoverGenerate, selProfitLossOpen, carryoverCheckOut, antiSettlement, carryoverCalculationCheck, voucherNumber
} from "@/api/account/settleAccounts"
import carryoverRule from "@/components/CarryoverRule"
import carryoverTrObj from "@/assets/js/carryoverTrObj";
import { getSubjectVoucher, currentPeriod } from "@/api/voucher/viewvoucher";


export default {
  components: {
    carryoverRule
  },
  data () {
    return {
      // 全选
      checkAll: true,
      // 保存选中数组
      checkModel: [],
      bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId,
      // 结转列表
      carryoverList: [],
      // 期末转结凭证模板是否弹出
      open: false,
      // 结转数据
      tableData: {
        trData: [],
      },
      // 科目列表
      subjectList: [],
      // 取数来源科目
      sourceSubjectList: [],
      // 期末结转拼争模板名称
      //carryoverName: '',
      form: {
        carryoverName: ''
      },
      // 结转名称是否禁用
      isDisabled: false,
      // 表单校验
      rules: {
        carryoverName: [
          { required: true, message: '请输入名称' }
        ]
      },
      // 结转Id
      carryoverId: null,
      // 标志，判断是否为结转本年利润
      isProfit: false,
      // 结转code
      carryoverCode: null,
      // 标志，是否显示结转销售成本设置百分比弹框
      openSales: false,
      // 标志，是否显示结转损益设置年利润科目
      openProfitAndloss: false,
      // 结转销售成本设置百分比
      salesPercentage: 0,
      // 标志，是否显示结转利润
      openProfitLoss: false,
      // 是否结转本年利润
      profitLossOpen: false,
      // 提示试算是否平衡
      isBalance: true,
      // 是否多选
      multiple: true,
      // 当前账期凭证个数
      voucherNum: 0,
      // 当前账期
      currentPeriod: {},
      yearPeriod: null,
      // 加载中
      loading: true,
    }
  },
  created () {
    this.getCurrentPeriod();
    this.getVoucherNumber();
    this.calculation();
    this.getSubjectList();
    this.getSourceSubjectList();
    for (let i = 0; i < 2; i++) {
      this.addtr();
    }
    this.initCheckAll();
  },
  methods: {
    // 初始默认全选状态
    async initCheckAll(){
      let result = await this.getCarryoverList();
      this.changeState();
    },
    // 全选状态
    changeState (item) {
      this.checkModel = [];
      if (this.checkAll == true) {
        this.carryoverList.forEach((value, index) => {
          this.checkModel.push(value.carryoverId);
        })
      }
    },
    // 获取当前账期
    getCurrentPeriod () {
      return new Promise(resolve => {
        currentPeriod({ bookId: this.bookId }).then(res => {
          this.yearPeriod = res.data.yearPeriod;
          this.currentPeriod = this.getStr(res.data.yearPeriod, "-")
          resolve(res.data);
        });
      });
    },
    // 获取当前账期凭证个数
    getVoucherNumber () {
      voucherNumber({ bookId: this.bookId }).then(res => {
        this.voucherNum = res.data.voucherNum;
      })
    },
    // 获取结转列表
    getCarryoverList () {
      return new Promise((resolve)=>{
        carryoverList({ bookId: this.bookId }).then(res => {
          this.carryoverList = res.rows;
          resolve(res.rows);
        });
      })
    },
    // 获取科目列表
    getSubjectList () {
      getSubjectVoucher({ bookId: this.bookId }).then(res => {
        this.subjectList = res.rows;
      });
    },
    // 获取取数来源科目列表
    getSourceSubjectList () {
      subjectList({ bookId: this.bookId }).then(res => {
        this.sourceSubjectList = res.rows;
      })
    },
    // 结转 - 测算 - 所有
    calculation (carryoverIds) {
      this.loading = true;
      let params = Object.assign({ bookId: this.bookId }, { carryoverIds });
      carryoverCalculation(params).then(res => {
        this.carryoverList = res.data;
        this.isBalance = res.success;
        this.loading = false;
      })
    },
    // 结转 - 测算 - 多选
    calculationCheck () {
      const carryoverIds = this.checkModel;
      let params = Object.assign({ bookId: this.bookId }, { carryoverIds });
      carryoverCalculationCheck(params).then(res => {
        this.carryoverList = res.data;
        if (res.success) {
          this.msgSuccess("重新测算成功");
        } else {
          this.msgError(res.msg);
        }
      })
    },
    // 重置表单
    reset () {
      this.form = {
        carryoverName: null,
        carryoverId: null
      }
      this.resetForm("form");
    },
    // 点击期末结转凭证模板
    addCarryover () {
      this.reset();
      this.tableData.trData = [];
      this.carryoverId = null;
      for (let i = 0; i < 2; i++) {
        this.addtr();
      }
      this.open = true;
      this.isDisabled = false;
    },
    // 编辑结转凭证模板
    editCarrayover (carryoverId, code) {
      this.reset();
      carryoverDetail({ carryoverId, bookId: this.bookId }).then(res => {
        this.tableData.trData = this.dataCarryover(res.rows);
        this.form.carryoverName = res.carryoverName;
        this.form.carryoverId = carryoverId;
        this.carryoverCode = code;
        this.open = true;
        this.isDisabled = true;
      })
    },
    // 设置结转凭证模板
    settingCarrayovery (carryoverId, code) {
      let carrayCode = code;
      switch (carrayCode) {
        case 'salesCost': this.costOfSales(carryoverId);
          break;
        case 'carryForward': this.profitLoss(carryoverId);
          break;
      }
    },
    // 结转销售成本
    costOfSales (carryoverId) {
      // 查询结转销售成本百分比
      saleOfPercentage({ carryoverId }).then(res => {
        this.salesPercentage = res.data.percentage;
        this.openSales = true;
      })
    },
    // 结转销售成本百分比提交
    submitSales (carryoverId) {
      setSaleOfPercentage({ carryoverId, percentage: this.salesPercentage }).then(res => {
        if (res.code == '200') {
          this.msgSuccess("设置成功");
          this.openSales = false;
        }
      })
    },
    // 结转销售成本百分比取消
    cancelSales () {
      this.openSales = false;
      this.salesPercentage = 0;
    },
    // 结转损益
    profitLoss (carryoverId) {
      selProfitLossOpen({ carryoverId }).then(res => {
        this.profitLossOpen = res.data.profitLossOpen;
        this.openProfitLoss = true;
      })
    },
    // 结转损益设置
    submitProfitLoss (carryoverId) {
      setProfitLoss({ carryoverId, profitLossOpen: this.profitLossOpen }).then(res => {
        if (res.code == '200') {
          this.msgSuccess("设置成功");
          this.openProfitLoss = false;
        }
      })
    },
    // 结转损益取消
    cancelProfitLoss () {
      this.openProfitLoss = false;
      this.profitLossOpen = false;
    },
    // 生成凭证
    generateCarrayover (carryoverId, index) {
      let carryIndex = index;
      carryoverGenerate({ carryoverId }).then(res => {
        if (res.success) {
          this.msgSuccess("成功生成凭证");
          // this.carryoverList[index].voucher1 = res.data;
          // this.carryoverList[index].voucher2 = res.data2;
          this.calculation();
          this.getVoucherNumber();
        } else {
          this.msgError(res.msg);
        }
      })
    },
    // 查看凭证
    viewCarrayover (carryover) {
      let carryoverObj = carryover;
      let voucher1Id = Object.prototype.toString.call(carryoverObj.voucher1) == '[object Undefined]' || carryoverObj.voucher1 == null ? '' : carryoverObj.voucher1.voucherId;
      let voucher2Id = Object.prototype.toString.call(carryoverObj.voucher1) == '[object Undefined]' || carryoverObj.voucher2 == null ? '' : carryoverObj.voucher2.voucherId;
      let voucherIds = [];
      if (voucher1Id != '' || voucher2Id != '') {
        voucher1Id != "" ? voucherIds.push(voucher1Id) : '';
        voucher2Id != "" ? voucherIds.push(voucher2Id) : '';
      }
      this.$router.push({
        path: '/voucher/viewvoucher',
        query: { voucherIds }
      })
      // this.$router.push({
      //   name: 'Viewvoucher',
      //   params: { voucherIds }
      // })
    },
    // 结账
    checkOut () {
      carryoverCheckOut({ bookId: this.bookId }).then(res => {
        if (res.code == '200') {
          this.msgSuccess("结账成功");
          this.getCurrentPeriod().then(res=>{
            this.$store.commit('SET_CURRENTPERIOD', res.yearPeriod);
            this.localStorage.setItem('currentPeriod', res.yearPeriod);
            this.$store.commit('SET_CURRENTPERIODDATA', this.getStr(res.yearPeriod, '-'));
            this.localStorage.setItem('currentPeriodData', this.getStr(res.yearPeriod, '-'));
          });
          this.calculation();
          this.getVoucherNumber();
        }
      })
    },
    // 反结账
    reserveCheckOut () {
      antiSettlement({ bookId: this.bookId }).then(res => {
        if (res.code == '200') {
          this.msgSuccess("反结账成功");
          this.getCurrentPeriod().then(res=>{
            this.$store.commit('SET_CURRENTPERIOD', res.yearPeriod);
            this.localStorage.setItem('currentPeriod', res.yearPeriod);
            this.$store.commit('SET_CURRENTPERIODDATA', this.getStr(res.yearPeriod, '-'));
            this.localStorage.setItem('currentPeriodData', this.getStr(res.yearPeriod, '-'));
          });
          this.calculation();
          this.getVoucherNumber();
        }
      })
    },
    // 结转详情数据格式化
    dataCarryover (data) {
      let arr = data;
      let array = [];
      arr.forEach(detail => {
        let item = { abstract: {}, subject: {}, sourceSubject: {}, percentage: {} };
        item.abstract.value = detail.summary;
        item.direction = detail.direction;
        item.subject.value = detail.subject;
        item.subject.subjectCode = detail.subjectCode;
        item.sourceSubject.value = detail.originSubject;
        item.sourceSubject.sourceSubjectCode = detail.originSubjectCode;
        item.dateRule = detail.dateRule;
        item.percentage.value = detail.percentage;

        if (this.trimStr(detail.dateRule) == '自动平衡') {
          item.sourceSubject.isDisabled = true;
          item.percentage.isDisabled = true;
        } else {
          item.sourceSubject.isDisabled = false;
          item.percentage.isDisabled = false;
        }
        this.carryoverId = detail.carryoverId;
        array.push(item);
      })
      return array;
    },
    // 期末转结凭证模板点击保存
    submitForm () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          let data = this.carryoverData();
          let carryoverData = data.data;
          let saveData = this.warningInfo(data.messages);
          if (saveData.save) {
            if (this.form.carryoverId) {
              editCarryover(carryoverData).then(res => {
                this.msgSuccess("修改成功");
                this.open = false;
                //this.getCarryoverList();
                this.calculation();
              })
            } else {
              saveCarryover(carryoverData).then(res => {
                this.msgSuccess("保存成功");
                this.open = false;
                //this.getCarryoverList();
                this.calculation();
              })
            }
          } else {
            this.msgError(saveData.result);
            this.open = true;
            return;
          }
        }
      })
    },
    // 处理保存时存在的警告信息
    warningInfo (arr) {
      let array = arr;
      let count = 0;
      let save = true;
      let result = '';
      array.forEach(item => {
        if (item.message != "") {
          result += "第" + parseInt(item.id + 1) + "行" + item.message + "<br/>";
          // 获取result 最后一个字符，如果为逗号将逗号去掉
          result = result.charAt(result.length - 6).indexOf(",") != -1 ? this.sliceLastSymbol(result) : result;
          save = false;
        } else {
          count++;
        }
        if (count == array.length) {
          save = true;
        }
      });
      return { save, result };
    },
    // 关闭弹窗
    close () {
      this.clearFormValidate("form");
      this.open = false;
    },
    // 期末转结凭证模板数据
    carryoverData () {
      let arr1 = this.tableData.trData;
      let data = {};
      let rules = [];
      let messages = [];
      arr1.forEach((item, index) => {
        let rule = {};
        let tip = { message: '' };
        rule.summary = item.abstract.value;
        tip.message += item.abstract.value == '' ? '请输入摘要,' : '';
        rule.subject = item.subject.value;
        rule.subjectCode = item.subject.subjectCode;
        tip.message += item.subject.value == '' ? '请选择科目,' : '';
        rule.direction = item.direction;
        tip.message += item.direction == '' ? '请选择方向,' : '';
        rule.dateRule = item.dateRule;
        tip.message += item.dateRule == '' ? '请选择取值类型,' : '';
        rule.originSubject = item.sourceSubject.value;
        rule.originSubjectCode = item.sourceSubject.sourceSubjectCode;
        tip.message += item.sourceSubject.value == '' ? '请选择取数来源科目,' : '';
        rule.percentage = item.percentage.value;
        tip.message += item.percentage.value == '' ? '请输入百分比,' : '';
        rules.push(rule);
        tip.id = index;
        messages.push(tip);
      })
      data.bookId = this.bookId;
      data.text = this.form.carryoverName;
      data.rules = rules;
      data.carryoverId = this.form.carryoverId;
      //this.carryoverId ? data.carryoverId == this.carryoverId : '';
      return { data, messages };
    },
    // 删除新增的结转模板
    delCarryover (carryoverId,carrayoverName) {
      let id = carryoverId;
      let name = carrayoverName;
      this.$confirm(
        '是否确认删除结转模板名称为"' + name + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function () {
          return deleteCarryover(carryoverId);
        })
        .then(() => {
          this.msgSuccess("删除成功");
          this.getCarryoverList();
        })
        .catch(function () { });
    },
    //添加行
    addtr (i) {
      let tr = carryoverTrObj();
      let tableData = this.tableData;
      if (i !== undefined) {
        tr.key = Math.random() + (i + 1);
        tableData.trData.splice(i + 1, 0, tr);
      } else {
        tr.key = Math.random();
        tableData.trData.push(tr);
      }
    },
    // 删除行
    deltr (n) {
      if (this.tableData.trData.length <= 2) {
        this.$message.warning("至少保留2条数据！");
      } else {
        this.tableData.trData.splice(n, 1);
      }
    },
    // 分割字符串
    getStr (string, str) {
      let str_before = string.split(str)[0];
      let str_after = string.split(str)[1];
      return { str_before, str_after };
    },

  },
  watch: {
    carryoverCode () {
      if (this.carryoverCode == 'yearProfit')
        this.carryoverCode == 'yearProfit' ? this.isProfit = true : this.isProfit = false;
    },
    checkModel: {
      handler () {
        if (this.checkModel.length == this.carryoverList.length) {
          this.checkAll = true;
          this.multiple = true;
        } else {
          this.multiple = this.checkModel.length == 0 ? false : true;
          this.checkAll = false;
        }
      },
      deep: true
    }
  }
}
</script>
<style lang="scss" scoped>
@import "~@/assets/styles/variables.scss";

.account-wrap {
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
  .account-title {
    width: 98%;
    height: 20px;
    margin: 3px 0;
    display: flex;
    flex-flow: row nowrap;
    justify-content: center;
    align-items: center;
  }
  .account-tab {
    width: 98%;
    height: 40px;
    margin: 3px 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .tab-right,
    .tab-left {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .tab-left {
      div {
        height: 35px;
        line-height: 35px;
        background-color: #eceef0;
        background-color: #eceef0;
        border-radius: 4px;
        font-size: 14px;
        color: #909399;
        margin-right: 15px;
        padding: 0 5px;
      }
    }
  }
  .account-content {
    width: 98%;
    margin: 3px 10px;
    ul {
      list-style: none;
      .item {
        margin: 16px;
        height: 180px;
        width: 220px;
        box-sizing: border-box;
        background: #fff;
        border-radius: 8px;
        position: relative;
        float: left;
        border: 1px solid #ddd;
        .box-title {
          height: 38px;
          line-height: 38px;
          background: #f7f7f7;
          position: relative;
          text-align: center;
          display: flex;
          flex-flow: row wrap;
          justify-content: space-around;
          align-items: center;
          border-top-left-radius: 8px;
          border-top-right-radius: 8px;
          border-bottom: 1px solid #ddd;
          .title-name {
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            font-size: 16px;
            color: #909399;
          }
          .setting {
            color: #909399;
            cursor: pointer;
          }
        }
        .box-content {
          text-align: center;
          height: 142px;
          display: flex;
          justify-content: center;
          align-items: center;
          width: 100%;
          color: #909399;
          .content-money {
            display: flex;
            flex-flow: column nowrap;
            justify-content: center;
            align-items: center;
          }
        }
        .btn-voucher {
          cursor: pointer;
          color: #3cbaff;
          text-decoration: none;
          outline: none;
          position: absolute;
          bottom: -16px;
          padding: 6px 25px;
          background: #f7f7f7;
          color: #1890ff; //#00b8ec;
          border-radius: 16px;
          left: 52px;
          font-family: "微软雅黑";
          border: 1px solid #1890ff; //#00b8ec;
          letter-spacing: 2px;
        }
        .btn-viewvoucher {
          cursor: pointer;
          color: #ffa537;
          text-decoration: none;
          outline: none;
          position: absolute;
          bottom: -16px;
          padding: 6px 25px;
          background: #f7f7f7;
          color: #67c23a; //#ffa537; //#00b8ec;
          border-radius: 16px;
          left: 52px;
          font-family: "微软雅黑";
          border: 1px solid #67c23a; //#ffa537; //#00b8ec;
          letter-spacing: 2px;
        }
      }
      .item-add {
        margin: 18px;
        height: 180px;
        width: 220px;
        box-sizing: border-box;
        float: left;
        text-align: center;
        color: #b6b6b6;
        cursor: pointer;
        .plus-wrap {
          height: 130px;
          width: 180px;
          padding: 20px 20px;
          line-height: 162px;
          .plus {
            border-radius: 8px;
            border: 2px dashed #b6b6b6;
            text-align: center;
            width: 100%;
            height: 100%;
            margin-left: -4px;
            margin-top: -4px;
          }
        }
      }
    }
  }
}
.sales {
  margin: 10px;
  display: flex;
  flex-flow: row nowrap;
  justify-content: center;
  align-items: center;
}
/deep/.el-input-number.is-without-controls .el-input__inner {
  text-align: left !important;
}
</style>
