<template>
  <div class="app-container">
    <div class="subject-wrap">
      <div class="subject-title">
        <h3 style="font-weight:400;">科目期初</h3>
      </div>
      <div class="subject-tab">
        <div>
          <ul class="tabsList">
            <li :class="activeIndex == item.typeId ? 'active' : ''"
                v-for="item of subejctTypes"
                :key="item.typeId"
                @click="handleClick(item)">
              {{ item.name }}
            </li>
          </ul>
        </div>
        <div>
          <el-form :model="queryParams"
                   ref="queryForm"
                   :inline="true">
            <el-input style="width:73%;"
                      placeholder="名称/编码"
                      size="small"
                      clearable
                      v-model="searchName"
                      @keyup.enter.native="searchList">
              <el-button slot="append"
                         icon="el-icon-search"
                         @click="searchList"></el-button>
            </el-input>
            <el-button size="small"
                       style="background-color:#1890ff;color:#fff;font-size: 14px;"
                       v-hasPermi="['accounting:subject:trialBalance']"
                       @click="isOpenopenTrial">试算平衡</el-button>
          </el-form>
        </div>
      </div>
      <div class="subject-content">
        <tab-table :tableData="subjectLists"
                   :loading="loading"
                   :isChange="isChange"
                   @handleAdd="handleAdd"
                   @handleEdit="handleEdit"
                   @handleDel="handleDel"
                   @computedBalanceInit="handleBalanceInit"
                   @computedDebit="handleDebit"
                   @computedCredit="handleCredit"
                   @showAuxiliary="showAuxiliary"></tab-table>
      </div>
    </div>
    <!-- 添加或修改科目 -->
    <el-dialog :title="title"
               :visible.sync="open"
               width="650px"
               @close="close('form')"
               append-to-body>
      <el-form ref="form"
               :model="form"
               :rules="rules"
               label-width="80px"
               label-position="left">
        <el-row :gutter="10">
          <el-col :xs="24"
                  :md="12"
                  :xl="12">
            <el-form-item label="编码"
                          prop="code">
              <el-input v-model="form.code"
                        placeholder="请输入编码"
                        disabled
                        readonly />
            </el-form-item>
          </el-col>
          <el-col :xs="24"
                  :md="12"
                  :xl="12">
            <el-form-item label="名称"
                          prop="name">
              <el-input v-model="form.name"
                        placeholder="名称"
                        :disabled="disableName" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :xs="24"
                  :md="12"
                  :xl="12">
            <el-form-item label="类型"
                          prop="typeId">
              <!--@visible-change="getCategoryByType"-->
              <el-select style="width:100%;"
                         v-model="form.typeId"
                         @change="getCategoryByType"
                         placeholder="请选择类型"
                         disabled>
                <el-option v-for="dict in subejctTypes"
                           :key="dict.typeId"
                           :label="dict.name"
                           :value="parseInt(dict.typeId)"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24"
                  :md="12"
                  :xl="12">
            <el-form-item label="类别"
                          prop="categoryId">
              <el-select style="width:100%;"
                         v-model="form.categoryId"
                         placeholder="请选择类别"
                         disabled>
                <el-option v-for="dict in subjectCategory"
                           :key="dict.categoryId"
                           :label="dict.name"
                           :value="parseInt(dict.categoryId)"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :xs="24"
                  :md="24"
                  :xl="24">
            <el-form-item label="余额方向"
                          prop="balanceDirection">
              <el-radio-group v-model="form.balanceDirection">
                <el-radio v-for="dict in balanceDirections"
                          :key="dict.balanceDirection"
                          :label="dict.balanceDirection">{{ dict.balanceDirectionText }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <!--<el-row :gutter="10">
          <el-col :xs="24"
                  :md="24"
                  :xl="24">
            <el-form-item prop="isCashFlow">
              <el-checkbox v-model="form.isCashFlow">是否现金流量项目</el-checkbox>
              <span style="display:block;color:#f00;font-size:12px;">提示：现金流量项目只能从上级科目继承，如有需要请修改一级科目的现金流量项目设置</span>
            </el-form-item>
          </el-col>
        </el-row>-->
        <el-row :gutter="10">
          <el-col :xs="24"
                  :md="12"
                  :xl="12">
            <el-form-item label="数量核算"
                          prop="isQuantity">
              <el-checkbox v-model="form.isQuantity">是否数量核算</el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :xs="24"
                  :md="12"
                  :xl="12"
                  v-if="form.isQuantity">
            <el-form-item label="计量单位"
                          prop="quantityUnit">
              <el-input v-model="form.quantityUnit"
                        placeholder="计量单位" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :xs="24"
                  :md="24"
                  :xl="24">
            <el-form-item label="辅助核算">
              <el-checkbox v-model="form.isSupplier"
                           label="供应商">供应商</el-checkbox>
              <el-checkbox v-model="form.isCustomer"
                           label="客户">客户</el-checkbox>
              <el-checkbox v-model="form.isProject"
                           label="项目">项目</el-checkbox>
              <el-checkbox v-model="form.isDepartment"
                           label="部门">部门</el-checkbox>
              <el-checkbox v-model="form.isPerson"
                           label="个人">个人</el-checkbox>
              <!-- 暂不开放 -->
              <!--<el-checkbox v-model="form.isStock"
                           label="存货">存货</el-checkbox>-->
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 暂不开放 -->
        <!-- <el-row :gutter="10">
          <el-col :xs="24"
                  :md="24"
                  :xl="24">
            <el-form-item label="外币核算"
                          prop="isForCurrency">
              <el-checkbox v-model="form.isForCurrency">是否外币核算</el-checkbox>
            </el-form-item>
          </el-col>
        </el-row> -->
      </el-form>
      <div slot="footer"
           class="dialog-footer el-row">
        <el-button type="primary"
                   @click="submit" :disabled="!clickTag">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!--辅助核算弹框-->
    <el-dialog title="辅助核算"
               s
               :visible.sync="openAuxiliary"
               :close-on-click-modal="false"
               size="tiny">
      <div>
        <el-tabs @tab-click="tabClick"
                 v-model="activeAuxIndex">
          <el-tab-pane label="客户"
                       name="isCustomer"
                       v-if="showCustomer">
            <div>
              <auxiliary-table :auxiliaryName="'客户'"
                               :tableData="auxiliaryData.customerList"
                               :auxiliaryList="auxiliaryList"
                               :backData="backData.backCustomer"
                               @backList="getCustomerList"
                               @addtr="addtr"
                               @deltr="deltr"></auxiliary-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="库存"
                       name="isStock"
                       v-if="showStock">
            <div>
              <auxiliary-table :auxiliaryName="'库存'"
                               :tableData="auxiliaryData.stockList"
                               :auxiliaryList="auxiliaryList"
                               :backData="backData.backStock"
                               @backList="getStockList"
                               @addtr="addtr"
                               @deltr="deltr"></auxiliary-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="供应商"
                       name="isSupplier"
                       v-if="showSupplier">
            <div>
              <auxiliary-table :auxiliaryName="'供应商'"
                               :tableData="auxiliaryData.supplierList"
                               :auxiliaryList="auxiliaryList"
                               :backData="backData.backSupplier"
                               @backList="getSupplierList"
                               @addtr="addtr"
                               @deltr="deltr">
              </auxiliary-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="部门"
                       name="isDepartment"
                       v-if="showDepartment">
            <div>
              <auxiliary-table :auxiliaryName="'部门'"
                               :tableData="auxiliaryData.departmentList"
                               :auxiliaryList="auxiliaryList"
                               :backData="backData.backDepartment"
                               @backList="getDepartmentList"
                               @addtr="addtr"
                               @deltr="deltr">
              </auxiliary-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="人员"
                       name="isPerson"
                       v-if="showPerson">
            <div>
              <auxiliary-table :auxiliaryName="'人员'"
                               :tableData="auxiliaryData.personList"
                               :auxiliaryList="auxiliaryList"
                               :backData="backData.backPerson"
                               @backList="getPersonList"
                               @addtr="addtr"
                               @deltr="deltr">
              </auxiliary-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="项目"
                       name="isProject"
                       v-if="showProject">
            <div>
              <auxiliary-table :auxiliaryName="'项目'"
                               :tableData="auxiliaryData.projectList"
                               :auxiliaryList="auxiliaryList"
                               :backData="backData.backProject"
                               @backList="getProjectList"
                               @addtr="addtr"
                               @deltr="deltr">
              </auxiliary-table>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div slot="footer"
           class="dialog-footer el-row">
        <el-button type="primary"
                   @click="submitAux">确 定</el-button>
        <el-button @click="cancelAux">取 消</el-button>
      </div>
    </el-dialog>
    <!--试算平衡检查弹框-->
    <el-dialog :title="title"
               :visible.sync="openTrialBalance"
               :close-on-click-modal="false">
      <alert-message v-if="isBalance"
                     :alertTitle="'恭喜您' +','+'您录入的初始余额平衡'+'!'"
                     :showAlert="isAlert"
                     :alertSuccess="true"
                     @closeAlert="closeAlert"></alert-message>
      <alert-message v-if="!isBalance"
                     :alertTitle="'您录入的初始余额不平衡' +','+'请检查'+'!'"
                     :showAlert="isAlert"
                     :alertSuccess="false"
                     @closeAlert="closeAlert"></alert-message>
      <div class="trial-balance">
        <table class="trial-table"
               cellpadding="0"
               cellspacing="0">
          <th>项目</th>
          <th>借方金额</th>
          <th>贷方金额</th>
          <th>差额</th>
          <tr>
            <td>{{trialBalanceData.node1}}</td>
            <td>{{trialBalanceData.debitTotal1}}</td>
            <td>{{trialBalanceData.creditTotal1}}</td>
            <td>{{trialBalanceData.imbalance1}}</td>
          </tr>
          <tr>
            <td>{{trialBalanceData.node2}}</td>
            <td>{{trialBalanceData.debitTotal2}}</td>
            <td>{{trialBalanceData.creditTotal2}}</td>
            <td>{{trialBalanceData.imbalance2}}</td>
          </tr>
        </table>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  subjectType,
  subjectList,
  subjectDetail,
  subjectCategory,
  subjectCode,
  addSubject,
  updateSubject,
  delSubject,
  editBalance,
  saveAuxiliary,
  editAuxiliary,
  queryAuxBalance,
  trialBalance,
  checkSubject,
  isEdit
} from "@/api/system/subject";
import { mapState } from "vuex";
import TabTable from "@/components/TabTable";
import AlertMessage from "@/components/AlertMessage";
import { trimStr } from "@/utils/index";
import AuxiliaryTable from "@/components/AuxiliaryTable";
import auxiliaryTrObj from "@/assets/js/auxiliaryTrObj";
import {
  SUPPLIER,
  CUSTOMER,
  PROJECT,
  DEPARTMENT,
  PERSON,
  STOCK,
  auxiliaryList
} from "@/api/system/auxiliary";

// 供应商、客户、项目、部门、职员
export default {
  name: "Subject",
  components: {
    TabTable,
    AuxiliaryTable,
    AlertMessage
  },
  data () {
    return {
      // 科目类型
      subejctTypes: [],
      // 查询条件
      queryParams: {
        bookId: null,
        typeId: null
      },
      // 科目列表
      subjectLists: [],
      // 选中的科目类型
      activeIndex: null,
      // dialog标题名称
      title: "",
      // dialog是否弹出
      open: false,
      // 科目表单
      form: {},
      // 表单校验
      rules: {
        code: [{ required: true, message: "编号不能为空", trigger: "blur" }],
        name: [{ required: true, message: " 名称不能为空", trigger: "blur" }],
        typeId: [{ required: true, message: "请选择类型", trigger: "blur" }],
        categoryId: [{ required: true, message: "请选择类别", trigger: "blur" }]
      },
      // 类别
      subjectCategory: [],
      // 余额方向
      balanceDirections: [
        { balanceDirection: "DEBIT", balanceDirectionText: "借" },
        { balanceDirection: "CREDIT", balanceDirectionText: "贷" }
      ],
      // 科目新增或修改
      addOrEdit: "add",
      // 科目新增或修改防抖
      clickTag:true,
      // 一级科目名称是否允许修改
      disableName: false,
      // 搜索内容
      searchName: null,
      // 数据加载状态
      loading: true,
      // 是否打开辅助核算弹框
      openAuxiliary: false,
      // 辅助核算-客户
      showCustomer: false,
      // 辅助核算-库存
      showStock: false,
      // 辅助核算-供应商
      showSupplier: false,
      // 辅助核算-部门
      showDepartment: false,
      // 辅助核算-人员
      showPerson: false,
      // 辅助核算-项目
      showProject: false,
      // 辅助核算弹框数据
      auxiliaryData: {
        customerList: [],
        departmentList: [],
        personList: [],
        projectList: [],
        supplierList: []
      },
      // bookId
      bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId"): this.$store.state.bookId,
      // 辅助核算数据
      auxiliaryList: [],
      backData: {
        backCustomer: false,
        backStock: false,
        backDepartment: false,
        backPerson: false,
        backProject: false,
        backSupplier: false
      },
      // 保存辅助核算需要的subjectId
      auxSubjectId: null,
      // 辅助核算弹出框默认显示的tab
      activeAuxIndex: 0,
      // 标志，判断科目辅助核算余额为新增或修改
      auxAddOrEdit: 'add',
      // 标志，判断辅助核算期初数据有值时保存的辅助核算数据是否可以为空
      //isEmpty: false,
      // 标志，判断试算平衡弹框是否弹出
      openTrialBalance: false,
      // 试算平衡数据
      trialBalanceData: {},
      // 标志，判断试算是否平衡
      isBalance: false,
      // 标志，判断试算平衡alert是否显示
      isAlert: true,
      // 标志，判断期初余额、本年累计借方、本年累计贷方是否可以更改
      isChange: true,
    };
  },
  created () {
    this.getSubjectTypes();
    this.getSubjectList();
    for (let i = 0; i < 1; i++) {
      this.addtr({ arr: this.auxiliaryData.customerList });
      this.addtr({ arr: this.auxiliaryData.departmentList });
      this.addtr({ arr: this.auxiliaryData.personList });
      this.addtr({ arr: this.auxiliaryData.projectList });
      this.addtr({ arr: this.auxiliaryData.supplierList });
    }
    this.judeIsChange();
  },
  methods: {
    // 辅助核算tab点击切换
    tabClick (tab) {
      let auxiliaryName = tab.name;
      switch (auxiliaryName) {
        case "isCustomer":
          this.getAuxiliaryList(CUSTOMER);
          break;
        case "isSupplier":
          this.getAuxiliaryList(SUPPLIER);
          break;
        case "isDepartment":
          this.getAuxiliaryList(DEPARTMENT);
          break;
        case "isPerson":
          this.getAuxiliaryList(PERSON);
          break;
        case "isProject":
          this.getAuxiliaryList(PROJECT);
          break;
      }
    },
    getAuxiliaryList (typeId) {
      let data = { bookId: this.bookId, auxiliaryTypeId: typeId };
      auxiliaryList(data).then(res => {
        this.auxiliaryList = res.rows;
      });
    },

    // 获取辅助核算 - 客户列表
    getCustomerList (data) {
      let customerList = data;
      return customerList;
    },
    // 获取辅助核算 - 库存列表
    getStockList (data) {
      let stockList = data;
      return stockList;
    },
    // 获取辅助核算 - 供应商列表
    getSupplierList (data) {
      let supplierList = data;
      return supplierList;
    },
    // 获取辅助核算 - 部门列表
    getDepartmentList (data) {
      let departmentList = data;
      return departmentList;
    },
    // 获取辅助核算 - 客户列表
    getPersonList (data) {
      let personList = data;
      return personList;
    },
    // 获取辅助核算 - 项目列表
    getProjectList (data) {
      let projectList = data;
      return projectList;
    },
    // 辅助核算数据保存
    submitAux () {
      let submitAuxData = {};
      //let auxDataError = "";
      submitAuxData.subjectId = this.auxSubjectId;
      this.backData.backCustomer = this.showCustomer;
      this.backData.backStock = this.showStock;
      this.backData.backSupplier = this.showSupplier;
      this.backData.backDepartment = this.showDepartment;
      this.backData.backPerson = this.showPerson;
      this.backData.backProject = this.showProject;

      if (this.showProject) {
        let projectList = this.auxiliaryData.projectList;
        submitAuxData.projectList = this.auxiliaryFormat(projectList);
        // let judge = this.judgeAuxData(projectList, '项目');
        // judge.status ? auxDataError = judge.message : '';
      }

      if (this.showPerson) {
        let personList = this.auxiliaryData.personList;
        submitAuxData.personList = this.auxiliaryFormat(personList);
        // let judge = this.judgeAuxData(personList, '人员');
        // judge.status ? auxDataError = judge.message : '';
      }

      if (this.showDepartment) {
        let departmentList = this.auxiliaryData.departmentList;
        submitAuxData.departmentList = this.auxiliaryFormat(departmentList);
        // let judge = this.judgeAuxData(departmentList, '部门');
        // judge.status ? auxDataError = judge.message : '';
      }

      if (this.showSupplier) {
        let supplierList = this.auxiliaryData.supplierList;
        submitAuxData.supplierList = this.auxiliaryFormat(supplierList);
        // let judge = this.judgeAuxData(supplierList, '供应商');
        // judge.status ? auxDataError = judge.message : '';
      }

      if (this.showStock) {
        let stockList = this.auxiliaryData.stockList;
        submitAuxData.stockList = this.auxiliaryFormat(stockList);
        // let judge = this.judgeAuxData(stockList, '库存');
        // judge.status ? auxDataError = judge.message : '';
      }

      if (this.showCustomer) {
        let customerList = this.auxiliaryData.customerList;
        submitAuxData.customerList = this.auxiliaryFormat(customerList);
        // let judge = this.judgeAuxData(customerList, '客户');
        // judge.status ? auxDataError = judge.message : '';
      }

      if (this.auxAddOrEdit == 'add') {
        saveAuxiliary(submitAuxData).then(res => {
          if (res.code == "200") {
            this.msgSuccess("保存成功");
            this.openAuxiliary = false;
            this.getSubjectList();

          } else {
            this.msgError(res.msg);
          }
        });
      }
      else {
        editAuxiliary(submitAuxData).then(res => {
          if (res.code == "200") {
            this.msgSuccess("保存成功");
            this.openAuxiliary = false;
            this.getSubjectList();
          } else {
            this.msgError(res.msg);
          }
        })
      }
    },
    // 关闭科目辅助余额弹框
    cancelAux () {
      this.openAuxiliary = false;
    },
    // 格式化辅助核算弹框数据
    auxiliaryFormat (arr) {
      if (Object.prototype.toString.call(arr) != "[object Undefined]") {
        let arr1 = arr;
        let newArr = [];
        arr1.forEach((item, index) => {
          let obj = {};
          //obj.auxiliaryId = item.auxiliary.auxiliaryId;

          obj.auxiliaryId = (item.auxiliary.value == null || item.auxiliary.value == '') ? '' : item.auxiliary.auxiliaryId;
          obj.auxiliaryTypeId = item.auxiliary.auxiliaryTypeId;
          obj.balance = item.balance == null || item.balance == '' ? 0 : item.balance;
          obj.credit = item.credit == null || item.credit == '' ? 0 : item.credit;
          obj.debit = item.debit == null || item.debit == '' ? 0 : item.debit;

          newArr.push(obj);
        });
        return newArr;
      }
    },
    // 判断有无错误
    // judgeAuxData (arr, str) {
    //   if (Object.prototype.toString.call(arr) != "[object Undefined]") {
    //     var flag = { status: false, message: '' };
    //     let n = 0;
    //     for (let item of arr) {
    //       /*if (
    //         item.auxiliary.auxiliaryId == null ||
    //         item.balance == null ||
    //         item.credit == null ||
    //         item.debit == null
    //       ) {
    //         flag = true;
    //         break;
    //       }*/
    //       if (item.auxiliary.value == null || item.auxiliary.value == '') {
    //         flag.status = true;
    //         flag.message = str + '不能为空';
    //         return flag;
    //       }
    //       if (item.auxiliary.value != null && item.auxiliary.value != '' && ((item.balance != null && item.balance != 0) || (item.credit != null && item.credit != 0) || (item.debit != null && item.debit != 0))) {
    //         n++;
    //       } else {
    //         flag.status = true;
    //         flag.message = '期初数据不能全部为空或者0';
    //         return flag;
    //       }
    //     }
    //     if (n = arr.length) {
    //       flag.status = false;
    //       flag.message = '';
    //       return flag;
    //     }
    //   }
    // },
    //添加行
    addtr (data) {
      const { i, arr } = data;
      let tr = auxiliaryTrObj();
      let array = arr;
      if (i !== undefined) {
        tr.key = Math.random() + (i + 1);
        array.splice(i + 1, 0, tr);
      } else {
        tr.key = Math.random();
        array.push(tr);
      }
    },

    //删除行
    deltr (data) {
      const { i, arr } = data;
      let array = arr;
      if (arr.length <= 1) {
        this.$message.warning("至少保留1条分录！");
      } else {
        arr.splice(i, 1);
      }
    },
    // 重置辅助重置弹出框数据
    resetAuxTabData (row) {
      if (row.isCustomer) {
        this.auxiliaryData.customerList = [];
        this.addtr({ arr: this.auxiliaryData.customerList });
      };
      if (row.isStock) {
        this.auxiliaryData.stockList = [];
        this.addtr({ arr: this.auxiliaryData.stockList });
      };
      if (row.isSupplier) {
        this.auxiliaryData.supplierList = [];
        this.addtr({ arr: this.auxiliaryData.supplierList });
      };
      if (row.isDepartment) {
        this.auxiliaryData.departmentList = [];
        this.addtr({ arr: this.auxiliaryData.departmentList });
      }
      if (row.isPerson) {
        this.auxiliaryData.personList = [];
        this.addtr({ arr: this.auxiliaryData.personList });
      };
      if (row.isProject) {
        this.auxiliaryData.projectList = [];
        this.addtr({ arr: this.auxiliaryData.projectList });
      }
    },
    // 辅助核算弹出框
    showAuxiliary (row) {
      this.resetAuxTabData(row);
      if (row.isAuxiliary) {
        this.openAuxiliary = true;
        this.auxSubjectId = row.subjectId;
        this.showCustomer = row.isCustomer; // 客户
        this.showStock = row.isStock; // 库存
        this.showSupplier = row.isSupplier; // 供应商
        this.showDepartment = row.isDepartment; // 部门
        this.showPerson = row.isPerson; // 人员
        this.showProject = row.isProject; // 项目


        // 科目辅助核算余额查询
        queryAuxBalance({ subjectId: row.subjectId }).then(res => {
          let result = res;
          if (result.rows.length == 0) {
            this.defaultAuxiliary(row);
            this.auxAddOrEdit = 'add';
          } else {
            this.auxiliaryData = this.dataFormat(res.rows);
            this.defaultAuxiliary(row);
            this.auxAddOrEdit = 'edit';
            // if ((row.balance != null && row.balance != 0) || (row.credit != null && row.credit != 0) || (row.debit != null && row.debit != 0)) {
            //   this.isEmpty = true;
            // }
          }
        })
      }

    },
    // 辅助核算弹出框初始数据
    defaultAuxiliary (row) {
      if (row.isAuxiliary) {
        let array = [
          { name: "isCustomer", status: row.isCustomer },
          { name: "isStock", status: row.isStock },
          { name: "isSupplier", status: row.isSupplier },
          { name: "isSupplier", status: row.isSupplier },
          { name: "isDepartment", status: row.isDepartment },
          { name: "isPerson", status: row.isPerson },
          { name: "isProject", status: row.isProject }
        ];
        for (let object of array) {
          if (object.status) {
            this.activeAuxIndex = object.name;
            this.tabClick(object);
            break;
          }
        }
      }
    },
    // 将查询返回数据格式化
    dataFormat (arr) {
      let array = arr;
      let data = {
        customerList: [],
        stockList: [],
        supplierList: [],
        departmentList: [],
        personList: [],
        projectList: []
      }
      array.forEach((item, index) => {
        let itemm = { auxiliary: {} };
        itemm.auxiliary.auxiliaryId = item.auxiliaryId;
        itemm.auxiliary.auxiliaryTypeId = item.auxiliaryTypeId;
        itemm.auxiliary.value = item.auxiliaryNameBy;
        itemm.balance = item.balance;
        itemm.credit = item.credit;
        itemm.debit = item.debit;
        switch (item.auxiliaryTypeId) {
          case CUSTOMER: data.customerList.push(itemm);
            break;
          case STOCK: data.stockList.push(itemm);
            break;
          case SUPPLIER: data.supplierList.push(itemm);
            break;
          case DEPARTMENT: data.departmentList.push(itemm);
            break;
          case PERSON: data.personList.push(itemm);
            break;
          case PROJECT: data.projectList.push(itemm);
            break;
        }
      })
      return data;
    },
    // 获取科目类型
    getSubjectTypes () {
      let bookId = this.getUrlBookId();//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId;
      subjectType({ bookId }).then(res => {
        this.subejctTypes = res.rows;
        this.activeIndex = res.rows[0].typeId;
      });
    },
    // 获取科目列表
    getSubjectList (data) {
      this.loading = true;
      let bookId = this.getUrlBookId(); //this.$store.state.bookId == null? this.localStorage.getItem("bookId") : this.$store.state.bookId;
      let typeId = this.activeIndex;
      let obj = { bookId, typeId };
      let query = Object.assign(obj, data);
      subjectList(query).then(res => {
        this.subjectLists = this.splicingSubjectList(res.rows);
        this.loading = false;
      });
    },
    // 获取科目类别
    getSubjectCategory (typeId) {
      return new Promise(resolve => {
        subjectCategory({ typeId }).then(res => {
          this.subjectCategory = res.rows;
          resolve("success");
        });
      });
    },
    // 科目列表添加字段用于图标的显示或消失
    splicingSubjectList (arr) {
      arr.forEach(item => {
        item.isShow = false;
        item.balanceInit = item.balanceInit == 0 ? "" : item.balanceInit;
        item.debit = item.debit == 0 ? "" : item.debit;
        item.credit = item.credit == 0 ? "" : item.credit;
      });
      return arr;
    },
    // 点击科目
    handleClick (item) {
      this.activeIndex = item.typeId;
    },
    // 重置科目表单
    reset () {
      this.form = {
        bookId: null,
        code: null,
        name: null,
        typeId: null,
        categoryId: null,
        balanceDirection: "DEBIT",
        isQuantity: false,
        isSupplier: false,
        isCustomer: false,
        isDepartment: false,
        isPerson: false,
        isProject: false,
        isStock: false,
        isForCurrency: false
      };
      this.resetForm("form");
    },
    // 新增科目
    handleAdd (data) {
      this.reset();
      this.addOrEdit = "add";
      this.disableName = false;
      this.clickTag = true;
      let subjectId = data.subjectId;
      subjectCode({ subjectId }).then(res => {
        this.form.code = res;
      });
      this.form.bookId = data.bookId;
      this.form.typeId = data.typeId;
      this.getSubjectCategory(data.typeId).then(res => {
        if (res) {
          // 后期功能改动，如果类型，类别可选，取消注释去掉类型、类别的disable即可
          //this.form.categoryId = this.subjectCategory[0].categoryId;
          this.form.categoryId = data.categoryId;
          this.title = "新增科目";
          this.open = true;
        }
      });
    },
    // 修改科目
    handleEdit (data) {
      this.reset();
      let data1 = Object.assign({}, data);
      this.addOrEdit = "edit";
      this.clickTag = true;
      this.disableName = data.isModel ? true : false;
      this.form = data1;
      this.title = "修改科目";
      this.open = true;
      /*this.getSubjectCategory(data.typeId).then(res => {
        if (res) {
          this.form = data;
          this.title = "修改科目";
          this.open = true;
        }
      });*/
    },
    // 删除科目
    handleDel (data) {
      const subjectId = data.subjectId;
      this.$confirm('是否确认删除科目为"' + data.name + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function () {
          return delSubject(subjectId);
        })
        .then(() => {
          this.msgSuccess("删除成功");
          this.getSubjectList();
        })
        .catch(() => { });
    },
    // 根据类型显示对应的类别
    getCategoryByType (selVal) {
      this.getSubjectCategory(selVal).then(res => {
        if (res) {
          this.form.categoryId = this.subjectCategory[0].categoryId;
        }
      });
    },
    // 科目表单信息提交
    submit () {
      this.$refs["form"].validate(valid => {
        if (valid) {
            this.clickTag = false;
            if (this.addOrEdit == "add") {
              checkSubject(this.form).then(res=>{
                if(res.data.Tips){
                  this.$confirm(res.data.TipsContent, "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                  }).then(()=>{
                    addSubject(this.form).then(response => {
                      if (response.code === 200) {
                        this.msgSuccess("新增成功");
                        this.open = false;
                        this.getSubjectList();
                      }
                      this.clickTag = true;
                    });
                  }).catch((e)=>{
                    console.log(e);
                    this.clickTag = true;
                  })
                }else{
                  addSubject(this.form).then(response => {
                      if (response.code === 200) {
                        this.msgSuccess("新增成功");
                        this.open = false;
                        this.getSubjectList();
                      }
                      this.clickTag = true;
                    });
                }
              }); 
            } else {
              updateSubject(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getSubjectList();
                } else {
                  this.msgError(response.msg);
                  this.open = true;
                }
                this.clickTag = true;
              }).catch((e)=>{
                this.clickTag = true;
              });
            }
        
          
        }
      });
    },
    // 新增或修改科目取消
    cancel () {
      this.open = false;
      this.clearFormValidate("form");
    },
    // 新增或修改科目弹框关闭
    close (el) {
      this.open = false;
      this.clearFormValidate(el);
    },
    // 改变期初余额
    handleBalanceInit (data) {
      data.balanceInit = data.balanceInit == "" ? 0 : data.balanceInit;
      let reg = /^(\-)?\d+(\.\d{1,9})?$/; // 当前小数点可以输入9位，如果想要不限制小数点位数可写成 /^[+-]?(0|([1-9]\d*))(\.\d+)?$/；
      if(reg.test(data.balanceInit)){
        let params = { balanceInit: data.balanceInit, subjectId: data.subjectId };
        if (data.isAuxiliary == false) {
          editBalance(params).then(res => {
            if (res.code === 200) {
              this.msgSuccess("期初余额修改成功");
              this.getSubjectList();
            }
          });
        }
      }else{
        data.balanceInit = "";
      }
    },
    // 改变本年累计-借方金额
    handleDebit (data) {
      data.debit = data.debit == "" ? 0 : data.debit;
      let reg = /^(\-)?\d+(\.\d{1,9})?$/;
       if(reg.test(data.debit)){
         let params = { debit: data.debit, subjectId: data.subjectId };
         if (data.isAuxiliary == false) {
            editBalance(params).then(res => {
              if (res.code === 200) {
                this.msgSuccess("本年累计-借方金额修改成功");
                this.getSubjectList();
              }
            });
         }
       } else{
           data.debit = "";
       }
      
    },
    // 改变本年累计-贷方金额
    handleCredit (data) {
      data.credit = data.credit == "" ? 0 : data.credit;
      let reg = /^(\-)?\d+(\.\d{1,9})?$/;
      if(reg.test(data.credit)){
        let params = { credit: data.credit, subjectId: data.subjectId };
        if (data.isAuxiliary == false) {
          editBalance(params).then(res => {
            if (res.code === 200) {
              this.msgSuccess("本年累计-贷方金额修改成功");
              this.getSubjectList();
            }
          });
        }
      }else{
        data.credit = "";
      }  
    },
    // 根据科目名称查询列表
    searchList (query) {
      let bookId = this.getUrlBookId();//this.$store.state.bookId == null? this.localStorage.getItem("bookId") : this.$store.state.bookId;
      let object = { bookId: bookId, name: this.searchName };
      let queryParams = {};
      if (Object.prototype.toString.call(query) !== "[object Undefined]") {
        queryParams =
          Object.keys(query).length == 0
            ? object
            : Object.assign(object, query);
      } else {
        queryParams = object;
      }
      this.loading = true;
      subjectList(queryParams).then(res => {
        if (res.rows.length != 0) {
          this.subjectLists = this.splicingSubjectList(res.rows);
          this.activeIndex = res.rows[0].typeId;
        } else {
          this.subjectLists = res.rows;
        }
        this.loading = false;
      });
    },
    // 打开试算平衡弹框
    isOpenopenTrial () {
      trialBalance({ bookId: this.bookId }).then(res => {
        this.trialBalanceData = res.data;
        this.title = '试算平衡检查';
        this.isAlert = true;
        this.isBalance = res.data.isImbalance;
        this.openTrialBalance = true;
      });
    },
    // 试算平衡提示是否显示
    closeAlert () {
      this.isAlert = false;
    },
    //  期初余额、本年累计借方、本年累计贷方是否可以改变
    judeIsChange(){
      isEdit({bookId:this.bookId}).then(res=>{
        this.isChange = res.balanceIsEdit;
      })
    }
  },
  watch: {
    activeIndex () {
      this.getSubjectList();
    },
    searchName () {
      /*if (this.searchName == "" || this.searchName == null || this.searchName.length == 0) {
        let typeId = this.subjectLists[0].typeId;
        this.searchList({ typeId });
      } else {
       this.searchList();
      }*/
      this.searchList();
    }
  }
};
</script>
<style lang="scss" scoped>
@import "~@/assets/styles/variables.scss";

.subject-wrap {
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
  //background-color: #eceef0;
  .subject-title {
    width: 98%;
    height: 20px;
    margin: 3px 0;
    display: flex;
    flex-flow: row nowrap;
    justify-content: center;
    align-items: center;
  }
  .subject-tab {
    width: 98%;
    height: 40px;
    margin: 3px 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .subject-content {
    width: 98%;
    margin: 3px 10px;
  }
}
.tabsList {
  width: 100%;
  min-width: 260px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-left: 0;
  li {
    padding: 5px 10px;
    box-sizing: border-box;
    list-style: none;
    font-size: 14px;
    font-weight: 200;
    margin-right: 5px;
    transition: all .3s cubic-bezier(0.645, 0.045, 0.355, 1);
    border: 1px solid $gray;
    border-radius: 3px;
    cursor: pointer;
  }
  li:last-child {
    margin-right: 0;
  }
  li.active {
    color: #fff;
    background-color: $light-blue;
    transition: transform .3s;
    z-index: 2;
  }
}
.trial-balance {
  width: 100%;
  margin: 10px auto;
  .trial-table {
    width: 100%;
    border: 1px solid $gray;
    text-align: center;
    border-collapse: collapse;
    padding: 2px;
    th {
      border: 1px solid #dfe6ec;
      padding: 10px;
      background-color: #f7f7f7;
      color: #909399;
      font-weight: 500;
    }
    tr {
      td {
        border: 1px solid #dfe6ec;
        padding: 10px;
      }
      tr td:nth-child(1) {
        width: 30%;
      }
      tr td:nth-child(2),
      tr td:nth-child(3) {
        width: 20%;
      }
      tr td:nth-child(4) {
        width: 20%;
      }
    }
  }
}
</style>
