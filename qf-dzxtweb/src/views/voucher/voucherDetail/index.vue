<template>
  <div class="app-container">
    <div id="Voucher">
      <el-row class="el-card--custom"
              style="border:0;">
        <el-row style="margin-left:25px;margin-bottom:30px;">
          <!--左侧-->
          <div class="f1">
            <template>
              <el-button type="primary"
                         @click="saveClick">保存</el-button>
              <el-button @click="saveVoucherTemplate">保存为凭证模板</el-button>
              <el-button @click="backTo">关闭窗口</el-button>
            </template>
          </div>
          <!--end-->
        </el-row>

        <el-row>
          <el-row class="voucher_head fz5"
                  style="margin-left:25px;">
            <div class="f1">
              <span>凭证字</span>
              <el-select size="mini"
                         v-model="voucherWord"
                         style="width:65px;">
                <el-option v-for="(item, index) in voucherWordOptions"
                           :key="index"
                           :label="item.label"
                           :value="item.label"
                           size="small">
                </el-option>
              </el-select>
              <el-input readonly
                        disabled
                        size="mini"
                        style="width:50px;"
                        v-model="voucherNumberValue" />
              <!--<span v-text="voucherNumberValue"></span>-->
              <span>号</span>
            </div>
            <!--日期-->
            <div class="fl"
                 style="margin-left: 20px;">
              <span>日期</span>
              <el-date-picker v-model="pickerOptionsValue"
                              size="mini"
                              type="date"
                              :editable="false"
                              :clearable="false"
                              value-format="yyyy-MM-dd"
                              placeholder="选择日期"
                              :default-value="pickerDefaultValue"
                              :picker-options="pickerOptions"
                              style="width:140px;">
              </el-date-picker>
            </div>
            <!--凭证头-->
            <div class="title fl">
              <div class="voucher_title">
                <span>记账凭证</span>
                <span style="font-size:14px;">{{ currentPeriod.str_before }}年{{
                    currentPeriod.str_after
                  }}期</span>
              </div>
            </div>
          </el-row>
          <!--表格子组件 -->
          <div ref="wrapper"
               class="box">
            <div class="tablebox">
              <!--表格子组件-->
              <Voucher-table @dialogAbsShow="dialogAbsShow"
                             @dialogSubShow="dialogSubShow"
                             @dialogAuxShow="dialogAuxShow"
                             @dialogAuxHide="dialogAuxHide"
                             @addtr="addtr"
                             @deltr="deltr"
                             @auxCheck="auxCheck"
                             @prevInput="prevInput"
                             @nextInput="nextInput"
                             @getNowEditindex="getNowEditindex"
                             @subClear="subClear"
                             @getBalance="getBalance"
                             :childData="childData"
                             :abstractData="dialogAbsData.tableData"
                             :auxiliarytxt="auxiliarytxt">
              </Voucher-table>
            </div>
          </div>
          <!--尾部-->
          <el-row class="voucher_bottom">
            <div style="margin-bottom: 10px;">
              <div class="f1">
                制单人:
                <!--绑定制单人TODO-->
                <span>{{ user.name }}</span>
              </div>
            </div>

            <!--<div :key="1"
               style="text-align: right;">
            <el-button ref="saveAndAdd"
                       type="primary"
                       @click="saveClick(true)">保存并新增</el-button>
            <el-button @click="saveClick(false)">保存</el-button>
            <el-button @click="saveVoucherTemplate">保存为凭证模板</el-button>
          </div>-->
          </el-row>
        </el-row>
      </el-row>

      <!--摘要对话框-->
      <el-dialog title="凭证摘要库"
                 :visible.sync="dialogAbsVisible"
                 :close-on-click-modal="false"
                 size="tiny">
        <el-input type="textarea"
                  :autosize="{ minRows: 3, maxRows: 3 }"
                  :maxlength="100"
                  resize="none"
                  placeholder="请输入摘要内容"
                  v-model="dialogAbsData.textarea">
        </el-input>
        <el-row style="margin: 10px 5px;">
          <div class="fr">
            <el-button size="small"
                       @click="absReset">重置</el-button>
            <el-button size="small"
                       type="primary"
                       @click="absSave"
                       v-text="dialogAbsData.edit ? '保存' : '新增'"></el-button>
          </div>
        </el-row>
        <el-table id="absTable"
                  :data="dialogAbsData.tableData"
                  v-loading="dialogAbsData.loading"
                  element-loading-text="加载中"
                  ref="absTable"
                  @current-change="AbsCurrentChange"
                  @cell-click="cellClick"
                  highlight-current-row
                  stripe
                  border
                  height="320">
          <el-table-column label="操作"
                           width="100"
                           align="center">
            <template slot-scope="scope">
              <el-button v-if="!scope.row.isDefault"
                         type="text"
                         size="small"
                         @click.stop="absEdit(scope.$index, scope.row)"
                         class="el-icon-edit">
              </el-button>
              <el-button v-if="!scope.row.isDefault"
                         type="text"
                         size="small"
                         @click.stop="absDel(scope.$index, scope.row)"
                         class="el-icon-delete">
              </el-button>
            </template>
          </el-table-column>
          <el-table-column show-overflow-tooltip
                           prop="summary"
                           label="摘要内容"
                           header-align="center">
          </el-table-column>
        </el-table>
      </el-dialog>

      <!--辅助核算对话框-->
      <el-popover v-if="dialogAuxRefreshVisible"
                  ref="popover"
                  placement="bottom-start"
                  width="320"
                  v-model="dialogAuxVisible"
                  @show="dialogAuxOpen"
                  @hide="dialogAuxClose">
        <el-form id="auxForm"
                 :model="dialogAuxForm"
                 ref="AuxForm"
                 label-position="right"
                 label-width="100px">
          <el-form-item v-for="(n, i) in dialogAuxData"
                        :key="i"
                        :label="n.name"
                        :prop="i + ''"
                        :rules="[{ required: true, message: '辅助核算项目不能为空' }]">
            <el-select v-model="n.value"
                       filterable
                       default-first-option
                       size="mini"
                       placeholder="请选择"
                       @change="dialogAuxChange"
                       v-auxInputEven="{ index: i }">
              <el-option v-for="no of n.options"
                         :key="no.auxiliaryId"
                         :label="no.code + ' ' + no.name"
                         :value="no.auxiliaryId"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </el-popover>
      <!-- 保存凭证模板配置对话框 -->
      <el-dialog title="新增模板"
                 :visible.sync="addTemplate"
                 width="500px"
                 @close="closeTemplate"
                 append-to-body>
        <el-form ref="form"
                 label-width="80px">
          <el-form-item label="模板名称"
                        :rules="[{ required: true, message: '凭证模板名称不能为空' }]">
            <el-input v-model="templateName"
                      placeholder="请输入凭证模板名称" />
          </el-form-item>
          <el-form-item label="保存金额">
            <el-checkbox v-model="isAmount"></el-checkbox>
            <span v-if="iswarning"
                  style="color:red">借方或贷方金额为空,不能保存金额</span>
          </el-form-item>
        </el-form>
        <div slot="footer"
             class="dialog-footer">
          <el-button type="primary"
                     @click="submitForm">保 存</el-button>
          <el-button @click="closeTemplate">取 消</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import VoucherTable from "@/components/Voucher_table";
import voucherTrObj from "@/assets/js/voucherTrObj.js";

import { getSubjectVoucher, currentPeriod } from "@/api/voucher/viewvoucher";
import {
  getSummaryVoucher,
  editSummary,
  addSummary,
  delSummary,
  subjectBalance,
  saveVoucherModel,
  getVoucherDetail,
  editVoucher
} from "@/api/voucher/voucher";
import { auxiliaryList, getAssistAList } from "@/api/system/auxiliary";

import { mapState } from "vuex";

export default {
  name: "voucher",
  components: {
    VoucherTable
  },
  directives: {
    auxInputEven: {
      unbind: function (el) {
        $(el)
          .off()
          .find("input")
          .off();
      },
      inserted: function (el, binding, vnode) {
        let Vue = vnode.context;
        $(el)
          .on("click", function (e) {
            Vue.nowAux = el;
          })
          .find("input")
          .on("keydown", function (e) {
            if (
              (e.keyCode === 13 && e.shiftKey) ||
              (e.keyCode === 9 && e.shiftKey)
            ) {
              e.preventDefault();
              Vue.auxPrevInput(el);
            } else if (
              (e.keyCode === 13 && !e.shiftKey) ||
              (e.keyCode === 9 && !e.shiftKey)
            ) {
              e.preventDefault();
              Vue.auxNextInput(Vue.nowAux);
            }
          })
          .on("focus", function (e) {
            Vue.nowAux = el;
          });
      }
    }
  },
  data () {
    return {
      //摘要库对话框
      dialogAbsVisible: false,
      dialogAbsData: {
        save: true,
        loading: true,
        edit: false,
        editid: "",
        textarea: "",
        tableData: []
      },

      //图片资源
      img: {
        hotKeysContent: require("@/assets/image/vchKeyBoard.jpg"),
        hotKeys: require("@/assets/image/keyboard.png")
      },

      //禁用 上一张 下一张
      prev: true,
      next: true,

      //当前编辑的科目或摘要
      nowEditindex: "",

      //科目对话框
      dialogSubVisible: false,
      dialogSubLoading: true,

      //是否已保存
      voucherSave: {
        show: false
      },

      //日期
      pickerOptions: {},
      // 选中日期
      pickerOptionsValue: "",
      // 默认日期
      pickerDefaultValue: "",
      //当前focus的辅助核算
      nowAux: "",

      //辅助核算对话框
      dialogAuxVisible: false,
      dialogAuxData: [],
      dialogAuxRefreshVisible: true,

      //凭证头信息
      voucherHeadInfo: {
        aperiodUid: "",
        iperiod: "",
        iyear: ""
      },

      // 凭证字
      voucherWordOptions: [{ value: "1", label: "记" }],
      // 凭证号
      voucherNumberValue: "",

      //childData
      childData: {
        trData: [],
        DeCr_Total: {
          allTotal: "",
          De: "",
          Cr: ""
        }
      },
      // 当前账期
      currentPeriod: {},
      yearPeriod: null,
      // 选中凭证字
      voucherWord: "记",
      // booKId
      bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId,
      // 保存模板弹出框是否弹出
      addTemplate: false,
      // 新增模板保存金额
      isAmount: false,
      // 一张凭证可否重复保存为模板
      repeatAdd: true,
      // 模板名称
      templateName: "",
      // 是否能够保存金额
      iswarning: false,
      // 选中的辅助核算的值
      auxiliarytxt: "",
      // 凭证Id
      voucherId: null,
      // 科目
      subjectList:
        this.$store.state.subjectList ||
        this.localStorage.getItem("subjectList")
    };
  },
  computed: {
    dialogAuxForm: function () {
      let form = {};
      this.dialogAuxData.forEach((n, i) => {
        form[i + ""] = n.value;
      });
      return form;
    },
    ...mapState(["user"])
  },
  created () {
    this.getSubjectList();
    this.getAuxiliaryList();
    this.initData();
  },
  methods: {
    // 初始化数据
    async initData () {
      this.childData.trData = [];
      let voucherId = this.$route.query.voucherId;
      this.voucherId = this.$route.query.voucherId;
      getVoucherDetail(voucherId).then(res => {
        this.voucherWord = res.data.voucherWord;
        this.voucherNumberValue = res.data.voucherNo;
        this.yearPeriod = res.data.yearPeriod;
        this.currentPeriod = this.getStr(res.data.yearPeriod, "-");
        this.pickerDefaultValue = res.data.voucherDate;
        this.pickerOptionsValue = res.data.voucherDate;
        /*this.getSubjectList().then(res => {
          this.subjectList = this.$store.state.subjectList.length == 0 ? res.rows : (this.$store.state.subjectList || this.localStorage.getItem('subjectList'));
        })*/
        this.childData.trData = this.dataTemplate(res.data);
      });
    },
    // 获取所有的辅助核算vuex缓存
    getAuxiliaryList () {
      auxiliaryList({ bookId: this.bookId }).then(res => {
        let data = getAssistAList(res.rows);
        this.$store.commit("getAccountingList", data);
      });
    },
    // 关闭窗口
    backTo () {
      this.$router.push({
        path: "/voucher/viewvoucher"
      });
    },
    // ctrl + s 快捷键保存凭证
    operateKeyBoard () {
      document.addEventListener("keydown", e => {
        if (
          e.keyCode == 83 &&
          (navigator.platform.match("Mac") ? e.metaKey : e.ctrlKey)
        ) {
          e.preventDefault();
          this.saveClick();
        }
      });
    },
    // 保存为凭证模板
    saveVoucherTemplate () {
      let voucher = this.voucherTemplateData();
      let voucherTemplate = voucher.voucherTemplate;
      let tips = voucher.tips;
      let result = "";
      if (tips.length == 0) {
        this.addTemplate = false;
        result = "第一条分录数据不能为空,</br>";
        result =
          result.charAt(result.length - 6).indexOf(",") != -1
            ? this.sliceLastSymbol(result)
            : result;
        this.msgError(result);
      } else {
        result = "";
        tips.forEach(tip => {
          result +=
            tip.message == ""
              ? ""
              : "第" + (tip.id + 1) + "行" + tip.message + "<br/>";
        });
        if (result != "") {
          result =
            result.charAt(result.length - 6).indexOf(",") != -1
              ? this.sliceLastSymbol(result)
              : result;
          this.msgError(result);
          this.addTemplate = false;
        } else {
          this.addTemplate = true;
        }
      }
    },
    dataTemplate (data) {
      let trData = [];
      let n = 0;
      data.detail.forEach((item, index) => {
        let detail = { abstract: {}, subject: {}, DeCr: {}, quantity: {}, currency: {} };
        let subData = this.getSubjectItem(item.subjectId);
        let subjectCode = this.getSubjextCode(subData);
        detail.abstract.val = item.summary;
        detail.subject.subjectId = item.subjectId;
        detail.subject.value =
          item.subjectName == "" ||
            Object.prototype.toString.call(item.subjectName) == "[object Null]"
            ? subjectCode + ' ' + item.subjectName
            : item.subjectNameBy;
        detail.subject.show = false;
        detail.subject.balance_show = false;
        item.balanceDirection === "DEBIT"
          ? (detail.subject.DeCr_direction = true)
          : (detail.subject.DeCr_direction = false);
        let auxs = [];
        item.auxiliarys.forEach(aux => {
          let obj = this.spliceAux(
            subData,
            aux.auxiliaryTypeText,
            aux.auxiliaryId
          );
          auxs.push(obj);
        });
        item.auxiliarys.length > 0
          ? (detail.isaux = true)
          : (detail.isaux = false);
        detail.subject.auxData = auxs;

        detail.DeCr.De_val =
          item.debit == "" ||
            Object.prototype.toString.call(item.debit) == "[object Null]"
            ? ""
            : this.toDecimal2(item.debit);
        detail.DeCr.Cr_val =
          item.credit == "" ||
            Object.prototype.toString.call(item.credit) == "[object Null]"
            ? ""
            : this.toDecimal2(item.credit);

        detail.DeCr.state =
          item.debit !== "" &&
            Object.prototype.toString.call(item.credit) == "[object Null]"
            ? true
            : false;

        detail.DeCr.show = false;

        if (item.quantity != "" && Object.prototype.toString.call(item.quantity) != "[object Null]" && item.price != "" &&
          Object.prototype.toString.call(item.price) != "[object Null]") {
          detail.quantity.show = true;
        } else {
          detail.quantity.show = false;
          n++;
        }
        // 数量
        detail.quantity.num_val = item.quantity == "" || Object.prototype.toString.call(item.quantity) == "[object Null]" ? "" : item.quantity.toString();
        // 单价
        detail.quantity.price_val = item.price == "" || Object.prototype.toString.call(item.price) == "[object Null]" ? "" : this.toDecimal2(item.price);
        // 单位
        detail.quantity.cmeasure = item.quantityUnit == "" || Object.prototype.toString.call(item.quantityUnit) == "[object Null]" ? "" : item.quantityUnit;
        detail.currency.show = false;
        trData.push(detail);
      });
      if (5 - data.detail.length > 0) {
        for (let i = 0; i < 5 - data.detail.length; i++) {
          let tr = voucherTrObj();
          trData.push(tr);
        }
      }
      if (n == data.detail.length) {
        this.childData.quantity = false;
      } else {
        this.childData.quantity = true;
      }
      return trData;
    },
    getSubjectList () {
      return new Promise(resolve => {
        getSubjectVoucher({ bookId: this.bookId }).then(res => {
          resolve(res.rows);
        });
      });
    },
    // 根据subject获取科目
    getSubjectItem (subjectId) {
      let list =
        this.$store.state.subjectList.length == 0
          ? this.localStorage.getItem("subjectList")
          : this.$store.state.subjectList;
      //let list = this.$store.state.subjectList || this.localStorage.getItem('subjectList');
      //let list = this.subjectList;
      let data = [];
      for (let item of list) {
        if (subjectId == item.subjectId) {
          data = item;
          break;
        }
      }
      return data;
    },
    // 辅助核算弹框数据拼接
    spliceAux (data, auxiliaryTypeText, val) {
      let item = {};
      this.dialogAuxData = [];
      if (data.isPerson && auxiliaryTypeText == "人员") {
        item = {
          name: "人员",
          value: val,
          options: this.$store.state.assistAList.personnels
        };
      }
      if (data.isCustomer && auxiliaryTypeText == "客户") {
        item = {
          name: "客户",
          value: val,
          options: this.$store.state.assistAList.customers
        };
      }
      if (data.isSupplier && auxiliaryTypeText == "供应商") {
        item = {
          name: "供应商",
          value: val,
          options: this.$store.state.assistAList.vendors
        };
      }
      if (data.isDepartment && auxiliaryTypeText == "部门") {
        item = {
          name: "部门",
          value: val,
          options: this.$store.state.assistAList.departments
        };
      }
      if (data.isProject && auxiliaryTypeText == "项目") {
        item = {
          name: "项目",
          value: val,
          options: this.$store.state.assistAList.auxitems
        };
      }
      return item;
    },
    // 获取凭证的code
    getSubjextCode (data) {
      return data.code;
    },
    // 凭证模板数据
    voucherTemplateData (data) {
      let voucherTemplate = {};
      let arr = this.childData.trData;
      let details = [];
      let tips = [];
      arr.forEach((item, index) => {
        let detail = {};
        if (item.abstract.val != "" || item.subject.subjectId != null) {
          let tip = { message: "" };
          index == 0
            ? item.subject.subjectId == null
              ? (tip.message += "请录入至少一条有效分录,")
              : ""
            : "";
          index == 0
            ? item.abstract.val == ""
              ? (tip.message += "第一条分录摘要不能为空,")
              : ""
            : "";
          detail.subjectId = item.subject.subjectId;
          detail.balanceDirection = item.subject.DeCr_direction
            ? "DEBIT"
            : "CREDIT";
          if (
            Object.prototype.toString.call(data) != "[object Undefined]" &&
            Object.prototype.toString.call(data.isAmount) !=
            "[object Undefined]"
          ) {
            detail.debit = item.DeCr.De_val === "" ? "" : item.DeCr.De_val;
            detail.credit = item.DeCr.Cr_val === "" ? "" : item.DeCr.Cr_val;
          }
          detail.summary = item.abstract.val;

          // 辅助核算
          if (item.isaux) {
            item.subject.auxData.forEach((aux, index) => {
              aux.name == "供应商"
                ? aux.value != ""
                  ? (detail.supplierId = aux.value)
                  : ""
                : "";
              aux.name == "客户"
                ? aux.value != ""
                  ? (detail.customerId = aux.value)
                  : ""
                : "";
              aux.name == "项目"
                ? aux.value != ""
                  ? (detail.projectId = aux.value)
                  : ""
                : "";
              aux.name == "部门"
                ? aux.value != ""
                  ? (detail.departmentId = aux.value)
                  : ""
                : "";
              aux.name == "人员"
                ? aux.value != ""
                  ? (detail.personId = aux.value)
                  : ""
                : "";
            });
            for (let aux of item.subject.auxData) {
              if (aux.value == "") {
                tip.message += "辅助核算为空,";
                break;
              }
            }
          }
          // 数量核算
          if (item.quantity.show) {
            detail.quantity = item.quantity.num_val;
            detail.price = item.quantity.price_val;
            detail.quantityUnit = item.quantity.cmeasure;
            item.quantity.num_val == '' || item.quantity.num_val == null ? tip.message += '数量不能为空,' : '';
            item.quantity.price_val == '' || item.quantity.price_val == null ? tip.message += '单价不能为空,' : '';
          }
          details.push(detail);

          tip.id = index;
          tips.push(tip);
        }
      });
      if (Object.prototype.toString.call(data) != "[object Undefined]") {
        voucherTemplate.name = data.name;
      }
      voucherTemplate.details = details;
      voucherTemplate.bookId = this.bookId;
      voucherTemplate.attachmentCount = 0;
      return { voucherTemplate, tips };
    },
    // 新增模板表单校验清空
    closeTemplate () {
      this.clearFormValidate("form");
      this.addTemplate = false;
      this.iswarning = false;
    },
    // 新增模板，保存
    submitForm () {
      let templateData = this.voucherTemplateData({
        name: this.templateName,
        isAmount: this.isAmount
      }).voucherTemplate;
      let de = this.childData.DeCr_Total.De;
      let cr = this.childData.DeCr_Total.Cr;
      if (this.isAmount) {
        if (de == "" && cr == "") {
          this.iswarning = true;
          this.addTemplate = true;
          return;
        }
      }
      if (this.repeatAdd) {
        saveVoucherModel(templateData).then(res => {
          if (res.code === 200) {
            this.$message("新增模板成功");
            this.addTemplate = false;
            this.repeatAdd = false;
            this.iswarning = false;
          } else {
            this.$message(res.msg);
            this.addTemplate = true;
            this.repeatAdd = true;
            this.iswarning = false;
          }
        });
      } else {
        this.addTemplate = true;
        this.$message("新增失败,当前页面只能生成一个凭证模板");
      }
    },
    // 获取的当前账期的最后一天
    /*getcurrentPeriodLast(currentPeriod) {
      let new_year = currentPeriod.str_before; //当前账期所在年
      let month = currentPeriod.str_after; //当前账期所在月
      let new_month = currentPeriod.str_after++; //取下一个月的第一天，方便计算（最后一天不固定）
      if (month > 12) {
        new_month -= 12; //月份减
        new_year++; //年份增
      }
      let new_date = new Date(new_year, new_month, 1); //取当年当月中的第一天
      let date = new Date(new_date.getTime() - 1000 * 60 * 60 * 24).getDate(); // 获取当月最后一天日期
      let lastDate = new_year + "-" + month + "-" + date;
      this.pickerDefaultValue = lastDate;
      this.pickerOptionsValue = lastDate;
    },*/
    // 获取所有的辅助核算vuex缓存
    getAuxiliaryList () {
      auxiliaryList({ bookId: this.bookId }).then(res => {
        let data = getAssistAList(res.rows);
        this.$store.commit("getAccountingList", data);
      });
    },
    // 分割字符串
    getStr (string, str) {
      let str_before = string.split(str)[0];
      let str_after = string.split(str)[1];
      return { str_before, str_after };
    },
    //保存摘要
    absSave () {
      if (this.dialogAbsData.edit && this.dialogAbsData.save) {
        this.dialogAbsData.save = false;
        this.editTableData(
          this.dialogAbsData.editid,
          this.dialogAbsData.textarea
        );
      } else if (!this.dialogAbsData.edit && this.dialogAbsData.save) {
        if (this.dialogAbsData.textarea !== "") {
          this.dialogAbsData.save = false;
          this.addTableData(this.dialogAbsData.textarea);
        } else {
          this.$message.warning("请输入摘要内容！");
        }
      }
    },

    //新增摘要库TODO
    addTableData (summary) {
      let bookId = this.getUrlBookId();//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId;
      addSummary({ bookId, summary }).then(res => {
        if (res.code === 200) {
          this.msgSuccess("新增成功");
          this.getSummaryData();
          this.dialogAbsData.textarea = "";
        }
      });
    },
    //编辑摘要库TODO
    editTableData (editid, textarea) {
      editSummary({ id: editid, summary: textarea }).then(res => {
        if (res.code === 200) {
          this.msgSuccess("修改成功");
          this.getSummaryData();
          this.dialogAbsData.textarea = "";
          this.dialogAbsData.edit = false;
        }
      });
    },
    //删除摘要库TODO
    delTableData (id) {
      delSummary(id).then(res => {
        if (res.code === 200) {
          this.msgSuccess("删除成功");
          this.getSummaryData();
        }
      });
    },
    //清空摘要textarea
    absReset () {
      this.dialogAbsData.textarea = "";
      this.dialogAbsData.editid = "";
      this.dialogAbsData.edit = false;
    },

    //编辑摘要
    absEdit (index, row) {
      this.dialogAbsData.edit = true;
      this.dialogAbsData.editid = row.id;
      this.dialogAbsData.textarea = row.summary;
    },
    //删除摘要
    absDel (index, row) {
      this.$confirm("此操作将删除该条摘要, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          if (
            this.dialogAbsData.edit &&
            this.dialogAbsData.editid === row.digestUid
          ) {
            this.absReset();
          }
          this.delTableData(row.id);
        })
        .catch(() => { });
    },

    //摘要弹出
    dialogAbsShow (index) {
      if (this.dialogAbsData.loading) {
        this.getSummaryData();
      }
      this.dialogAbsVisible = true;
      this.nowEditindex = index;
    },

    //摘要选择
    AbsCurrentChange (data) {
      if (this.nowEditindex !== "" && data) {
        let item = this.childData.trData[this.nowEditindex];
        item.abstract.val = data.digestText;
        this.dialogAbsVisible = false;
        /*this.$nextTick(() => {
          this.$refs.absTable.setCurrentRow(); //设置选中行
          item.abstract.show = true;
        });*/
      }
    },
    // 获取摘要列表
    getSummaryData () {
      return new Promise((resolve, reject) => {
        let bookId = this.getUrlBookId();//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId;
        getSummaryVoucher({ bookId }).then(res => {
          this.dialogAbsData.tableData = res.rows;
          this.dialogAbsData.loading = false;
          resolve("success");
        });
      });
    },
    // 点击cell选择摘要
    cellClick (row) {
      if (this.nowEditindex !== "" && row.summary) {
        let item = this.childData.trData[this.nowEditindex];
        item.abstract.val = row.summary;
        this.dialogAbsVisible = false;
      }
    },
    //科目弹出
    dialogSubShow (index) {
      if (this.dialogSubLoading) {
        this.getSubjectData(res => {
          this.dialogSubLoading = false;
        });
      }
      this.dialogSubVisible = true;
      this.nowEditindex = index;
    },
    //科目选择
    treeNodeClick (data) {
      this.treeNowData = data.children ? "" : data;
    },
    dbltreeNodeClick (data) {
      if (!data.children) {
        this.dialogSubEnter();
      }
    },

    //科目确认点击
    dialogSubEnter () {
      this.dialogSubVisible = false;
      if (this.treeNowData !== "" && this.nowEditindex !== "") {
        let item = this.childData.trData[this.nowEditindex],
          data = this.treeNowData;
        item.subject.val = data.value;
        item.subject.id = data.id;
        switch (data.accountProperty) {
          case "借":
            item.subject.DeCr_direction = true;
            break;
          case "贷":
            item.subject.DeCr_direction = false;
            break;
          default:
            break;
        }
        this.auxCheck([item, data]);
        this.$nextTick(() => {
          if (!item.isaux) {
            this.getBalance(item);
            this.nextInput(item.subject._el);
          } else {
            setTimeout(() => {
              this.dialogAuxShow(item.subject.auxData);
            }, 300);
          }
        });
      }
    },

    //清空科目数据
    subClear (item) {
      let el = item.subject._el;
      this.nowAux = "";
      item.subject = voucherTrObj().subject;
      item.subject._el = el;
      item.isaux = false;
    },

    //辅助核算检测
    auxCheck (a) {
      let item = a[0], //行数据
        data = a[1]; //科目数据
      item.isaux = false;
      item.subject.auxData = [];
      this.dialogAuxData = [];
      //item.assist = data.assist;
      if (data.isPerson) {
        item.subject.auxData.push({
          name: "人员",
          value: "",
          options: this.$store.state.assistAList.personnels
        });
      }
      if (data.isCustomer) {
        item.subject.auxData.push({
          name: "客户",
          value: "",
          options: this.$store.state.assistAList.customers
        });
      }
      if (data.isSupplier) {
        item.subject.auxData.push({
          name: "供应商",
          value: "",
          options: this.$store.state.assistAList.vendors
        });
      }
      if (data.isDepartment) {
        item.subject.auxData.push({
          name: "部门",
          value: "",
          options: this.$store.state.assistAList.departments
        });
      }
      if (data.isProject) {
        item.subject.auxData.push({
          name: "项目",
          value: "",
          options: this.$store.state.assistAList.auxitems
        });
      }
      //是否有其他辅助核算
      item.isaux = item.subject.auxData.length > 0;

      // 数量核算
      this.showQuantityColumn(item);
    },

    // 判断数量列是否显示
    showQuantityColumn (i) {
      let array = this.childData.trData;
      let isShow = false;
      let subject = this.getSubjectItem(i.subject.subjectId);
      for (let item of array) {
        let subjectData = this.getSubjectItem(item.subject.subjectId);
        if (subjectData.isQuantity) {
          isShow = true;
        } else {
          item.quantity.show = false;
          item.quantity.cmeasure = '';
        }
      }

      if (isShow) {
        if (subject.isQuantity) {
          this.childData.quantity = true;
          i.quantity.show = true;
          i.quantity.cmeasure = subject.quantityUnit && subject.quantityUnit !== "" ? "(" + subject.quantityUnit + ")" : ""
        } else {
          i.quantity.show = false;
          i.quantity.cmeasure = '';
        }
      } else {
        this.childData.quantity = false;
      }
    },
    //辅助核算弹出框
    dialogAuxShow (data) {
      this.dialogAuxData = data;
      this.dialogAuxRefreshVisible = false;
      this.dialogAuxVisible = false;
      this.$nextTick(() => {
        this.dialogAuxRefreshVisible = true;
        this.$nextTick(() => {
          this.$refs.popover.$refs.reference = this.childData.trData[
            this.nowEditindex
          ].subject._el;
          this.dialogAuxVisible = true;
        });
      });
    },
    dialogAuxOpen () {
      let Vue = this;
      this.$nextTick(() => {
        setTimeout(() => {
          $(Vue.$refs.AuxForm.$el)
            .find("input")
            .each(function () {
              let me = $(this);
              if (me.val() === "") {
                // me.focus();
                return false;
              }
            });
        }, 300);
      });
    },

    //辅助核算关闭窗口
    dialogAuxClose () {
      let item = this.childData.trData[this.nowEditindex];
      if (item.subject.auxData.length === 0) {
        $(item.subject._el)
          .find("input")
          .focus();
      }
    },
    dialogAuxHide () {
      this.dialogAuxVisible = false;
    },

    //获取当前编辑行的index
    getNowEditindex (a) {
      this.nowEditindex = a;
    },

    //添加行
    addtr (i) {
      let tr = voucherTrObj(),
        childData = this.childData;
      if (i !== undefined) {
        tr.key = Math.random() + (i + 1);
        childData.trData.splice(i + 1, 0, tr);
      } else {
        tr.key = Math.random();
        childData.trData.push(tr);
      }
    },

    //删除行
    deltr (n) {
      if (this.childData.trData.length <= 2) {
        this.$message.warning("至少保留2条分录！");
      } else {
        this.childData.trData.splice(n, 1);
      }
    },

    //选择上一个input框
    prevInput (el) {
      this.PrevOrNext(el, "voucher_table", true);
    },
    //选择下一个input框
    nextInput (el) {
      this.PrevOrNext(el, "voucher_table", false);
    },
    //辅助核算选择下一个input框
    auxNextInput (el) {
      this.PrevOrNext(el, "auxForm", false);
    },
    //辅助核算选择上一个input框
    auxPrevInput (el) {
      this.PrevOrNext(el, "auxForm", true);
    },
    //上一个或下一个input框
    PrevOrNext (el, domid, pn) {
      this.$nextTick(() => {
        let nowinput = $(el).find("input");
        let allinput = $("#" + domid + " input").not(":disabled"); //返回子组件中不带disabled的所有input
        let idx = allinput.index(nowinput);
        if (pn) {
          //上一个
          if (idx > 0) {
            allinput.eq(idx - 1).focus(); //选定的当前的input的上一个
          } else if (idx === 0) {
            //第一个
            if (domid === "voucher_table") {
              //切换到凭证头
            } else if (domid === "auxForm") {
              $(this.childData.trData[this.nowEditindex].subject._el)
                .find("input")
                .focus();
            }
          }
        } else {
          //下一个
          if (idx < allinput.length - 1) {
            allinput.eq(idx + 1).focus();
          } else if (idx === allinput.length - 1) {
            //最后一个
            if (domid === "voucher_table") {
              this.addtr();
              this.nextInput(el);
            } else if (domid === "auxForm") {
              this.dialogAuxEnter();
            }
          }
        }
      });
    },

    //辅助核算确认点击
    dialogAuxEnter () {
      let item = this.childData.trData[this.nowEditindex];
      this.$refs.AuxForm.validate(valid => {
        if (valid) {
          this.dialogAuxVisible = false;
          this.getBalance(item);
          this.$nextTick(() => {
            this.nextInput(item.subject._el);
          });
        } else {
          return false;
        }
      });
    },

    //辅助核算选择
    dialogAuxChange () {
      this.auxNext();
    },
    auxNext () {
      let check = true;
      this.$nextTick(() => {
        $(this.$refs.AuxForm.$el)
          .find("input")
          .each(function () {
            let me = $(this);
            if (me.val() === "") {
              me.focus();
              check = false;
              return check;
            }
          });
        if (check) {
          this.auxNextInput(this.nowAux);
        }
      });
    },

    //获取科目余额
    getBalance (item) {
      /*item.subject.auxData.forEach((n, i) => {
        switch (n.name) {
          case "客户":
            data.cusUid = n.value;
            break;
          case "人员":
            data.persUid = n.value;
            break;
          case "部门":
            data.depUid = n.value;
            break;
          case "供应商":
            data.venUid = n.value;
            break;
          case "项目":
            data.itemUid = n.value;
            break;
          case "存货":
            data.invUid = n.value;
            break;
        }
      });*/
      //请求后台接口传data获取余额
      if (item.subject.subjectId != null) {
        subjectBalance(item.subject.subjectId).then(res => {
          let balance = res.data.balance;
          item.subject.balance_show = true;
          item.subject.balance_val = balance;
        });
      }
    },
    // 凭证保存
    saveClick () {
      let isSave = this.judgeVoucher().save;
      let message = this.judgeVoucher().result;
      if (!isSave) {
        this.msgError(message);
        return;
      }
      if (message != "") {
        this.msgError(message);
        return;
      }
      if (this.childData.DeCr_Total.De != this.childData.DeCr_Total.Cr) {
        this.msgError("借贷合计金额不平衡，请检查后重新保存");
        return;
      }
      let voucher = this.voucherData().voucher;
      // 保存
      editVoucher(voucher).then(res => {
        if (res.code === 200) {
          this.msgSuccess("保存成功");
        }
      });
    },
    // 判断凭证保存条件
    judgeVoucher () {
      let voucher = this.voucherData().voucher;
      let tips = this.voucherData().tips;
      let result = "";
      result =
        voucher.details.length == 0 ? (result += "分录信息为空，不能保存") : "";
      let save = true;

      if (tips.length != 0) {
        let count = 0;
        tips.forEach(item => {
          if (item.message != "") {
            result +=
              "第" + parseInt(item.id + 1) + "行" + item.message + "\r\n";
            save = false;
          } else {
            count++;
          }
          if (count == voucher.details.length) {
            save = true;
          }
        });
      }
      return { save, result };
    },
    voucherData () {
      let voucher = {};
      let arr = this.childData.trData;
      let details = [];
      let tips = [];
      arr.forEach((item, index) => {
        let detail = {};
        let tip = { message: "" };
        if (
          item.abstract.val != "" ||
          item.subject.subjectId != null ||
          item.DeCr.De_val != "" ||
          item.DeCr.Cr_val != ""
        ) {
          detail.subjectId = item.subject.subjectId;
          detail.balanceDirection = item.subject.DeCr_direction
            ? "DEBIT"
            : "CREDIT";
          detail.debit = item.DeCr.De_val;
          detail.credit = item.DeCr.Cr_val;
          detail.summary = item.abstract.val;

          if (item.abstract.val == "") {
            tip.message += "凭证摘要为空,";
          }
          if (item.subject.subjectId == null) {
            tip.message += "科目为空,";
          }

          // 辅助核算
          if (item.isaux) {
            item.subject.auxData.forEach((aux, index) => {
              aux.name == "供应商"
                ? aux.value != ""
                  ? (detail.supplierId = aux.value)
                  : (tip.message += "请选择供应商辅助核算")
                : "";
              aux.name == "客户"
                ? aux.value != ""
                  ? (detail.customerId = aux.value)
                  : (tip.message += "请选择客户辅助核算")
                : "";
              aux.name == "项目"
                ? aux.value != ""
                  ? (detail.projectId = aux.value)
                  : (tip.message += "请选择项目辅助核算")
                : "";
              aux.name == "部门"
                ? aux.value != ""
                  ? (detail.departmentId = aux.value)
                  : (tip.message += "请选择部门辅助核算")
                : "";
              aux.name == "人员"
                ? aux.value != ""
                  ? (detail.personId = aux.value)
                  : (tip.message += "请选择职员辅助核算")
                : "";
            });
          }
          // 数量核算
          if (item.quantity.show) {
            detail.quantity = item.quantity.num_val;
            detail.price = item.quantity.price_val;
            detail.quantityUnit = item.quantity.cmeasure;
            if (item.quantity.num_val == "") {
              tip.message += "数量为空,";
            }
            if (item.quantity.price_val == "") {
              tip.message += "单价为空,";
            }
          }
          if (item.DeCr.De_val == "" && item.DeCr.Cr_val == "") {
            tip.message += "金额为空";
          }
          details.push(detail);
          tip.id = index;
          tips.push(tip);
        }
      });
      voucher.details = details;
      voucher.bookId = this.getUrlBookId();//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId;
      voucher.attachmentCount = 0;
      voucher.createUserName = this.user.name;
      voucher.debitTotal = this.childData.DeCr_Total.De;
      voucher.creditTotal = this.childData.DeCr_Total.Cr;
      voucher.totalAmount = this.childData.DeCr_Total.allTotal;
      voucher.voucherDate = this.pickerOptionsValue;
      voucher.yearPeriod = this.yearPeriod;
      voucher.voucherWord = this.voucherWord;
      voucher.voucherNo = this.voucherNumberValue;
      voucher.voucherId = this.voucherId;
      return { voucher: voucher, tips: tips };
    }
  }
};
</script>
<style lang="scss" scoped>
#Voucher {
  .el-dialog {
    width: 600px;
    .el-form {
      width: 440px;
      .el-select {
        width: 100%;
      }
    }
    #absTable {
      .el-table__row {
        &:hover {
          cursor: pointer;
        }
      }
    }
  }
  .el-card--custom {
    width: 1186px;
    padding: 28px 0;
    position: relative;
  }

  .fl {
    float: left;
  }
  .fr {
    float: right;
    .file {
      display: inline-block;
      width: 23px;
      height: 23px;
      &:hover {
        cursor: pointer;
      }
    }
  }
  .el-row {
    .hotkeys {
      position: relative;
      width: 30px;
      height: 30px;
      margin-right: 15px;

      > img {
        display: none;
      }
      &:hover > img {
        position: absolute;
        right: 0;
        top: 30px;
        display: block;
        z-index: 9999;
      }
    }
    .voucher_head {
      display: flex;
      margin: 10px 0 10px 0;
      padding-top: 10px;
      input {
        line-height: 30px;
      }

      > div > span,
      .title {
        line-height: 22px;
        vertical-align: middle;
      }

      .title {
        position: absolute;
        left: 506px;
        top: 0;
      }
      .voucher_title {
        display: inline;
        margin-right: 15px;
        text-align: center;
        font-size: 28px;
      }
    }

    .voucher_bottom {
      margin-top: 10px;
      margin-left: 25px;
    }
  }
}
.box {
  width: 100%;
  height: 565px !important;
  overflow-x: visible !important;
  overflow-y: auto;
  .tablebox {
    margin-left: 25px;
    margin-right: 25px;
  }
}
</style>
