<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="客户名称" prop="customerName">
        <el-input
          v-model="queryParams.customerName"
          placeholder="请输入客户名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="服务类型" prop="serviceTypeId">
        <el-select v-model="queryParams.serviceTypeId" placeholder="请输入服务类型" clearable size="small">
          <el-option
            v-for="item in commercegong"
            :key="item.value"
            :label="item.serviceTypeName"
            :value="item.serviceTypeId"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="服务状态" prop="serverStatus">
        <el-select
          v-model="queryParams.serverStatus"
          clearable
          placeholder="请选择服务状态"
        >
          <el-option
            v-for="dict in serverTypes"
            :key="dict.id"
            :label="dict.status"
            :value="dict.id"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="任务状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择任务状态" clearable size="small">
          <el-option
            v-for="item in  statusList"
            :key="item.value"
            :label="item.payment"
            :value="item.payMent"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <div style="display:flex;flex-flow:row-reverse;justify-content:space-between;">
      <el-row :gutter="10" class="mb8">
        <!-- <el-col :span="1.5">
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['commerce:commerce:add']"
          >新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['commerce:commerce:edit']"
          >修改</el-button>
        </el-col> -->
        <!-- <el-col :span="1.5">
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['commerce:commerce:remove']"
          >删除</el-button>
        </el-col>-->
        <!-- <el-col :span="1.5">
          <el-button
            type="warning"
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['commerce:commerce:export']"
          >导出</el-button>
        </el-col> -->
        <el-col :span="1.5">
          <el-button
            type="primary"
            icon="el-icon-user"
            size="mini"
            :disabled="single"
            v-hasPermi="['commerce:commerce:assign']"
            @click="handleDistribute()"
          >派工</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="primary"
            size="mini"
            :disabled="single"
            v-hasPermi="['commerce:commerce:stopService']"
            @click="stopService()"
          >停止服务</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="primary"
            size="mini"
            :disabled="single"
            v-hasPermi="['commerce:commerce:restoreService']"
            @click="resumeService()"
          >恢复服务</el-button>
        </el-col>
      </el-row>
    </div>

    <el-table v-loading="loading" :data="commerceList" style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工商合同" align="center" width="150px" prop="contractNo" />
      <el-table-column label="客户名称" align="center" width="150px" prop="customerName" />
      <el-table-column label="服务类型" align="center" width="130px" prop="serviceTypeName" />
      <el-table-column label="服务项目" align="center" width="130px" prop="serviceItemName" />
      <el-table-column label="服务资料" align="center" width="230px" prop>
        <template slot-scope="scope">
          <div
            v-show="scope.row.processData != null "
            class="datum"
            :title="scope.row.processData"
            style="color:#1890ff"
          >{{scope.row.processData}}</div>
          <div style="color:#ff4949" v-show="scope.row.processData == null " class="datum">暂无资料</div>
        </template>
      </el-table-column>
      <el-table-column label="任务进度" align="center" width="130px" prop>
        <template slot-scope="scope">
          <div style="color:#ff4949" v-show="scope.row.status == 0">暂未开始</div>
          <div style="color:#11b95c" v-show="scope.row.status == 1">{{scope.row.taskStatus}}</div>
          <div style="color:#1890ff" v-show="scope.row.status == 2">服务完成</div>
        </template>
      </el-table-column>
      <el-table-column label="优先级" align="center" width="130px" prop>
        <template slot-scope="scope">
          <el-tag style="cursor: pointer; width: 40px" v-show="scope.row.status == 0" effect="plain" type="success">低</el-tag>
          <el-tag style="cursor: pointer; width: 40px" v-show="scope.row.status == 1" effect="plain" type="primary">中</el-tag>
          <el-tag style="cursor: pointer; width: 40px" v-show="scope.row.status == 2" effect="plain" type="warning">高</el-tag>
          <el-tag style="cursor: pointer; width: 40px" v-show="scope.row.status == 3" effect="plain" type="danger">急</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="任务进度" width="300px" align="center" prop>
        <template slot-scope="scope">
          <div style="display: flex">
            <el-tooltip class="item" effect="dark" content="暂未开始" placement="top">
              <div class="tip-class">
                <div v-if="scope.row.status == 0" class="tip-circle circle-active-run">
                  <span>1</span>
                </div>
                <div v-else class="tip-circle circle-active-finish">
                  <span>✔</span>
                </div>
                <div class="tip-line"></div>
              </div>
            </el-tooltip>
            <el-tooltip v-for="(item, index) in scope.row.serviceStepList" :key="index" class="item" effect="dark" :content="item.stepName" placement="top">
              <div class="tip-class">
                <div v-if="item.stepStatus == 0" class="tip-circle"  :class="{'circle-active-run': scope.row.nowStep >= index}">
                  <span v-if="scope.row.serviceStepList.length == 0">{{ index + 1 }}</span>
                  <span v-else>{{ index + 2 }}</span>
                </div>
                <div v-else class="tip-circle circle-active-finish">
                  <span>✔</span>
                </div>
                <div class="tip-line"></div>
              </div>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="服务完成" placement="top">
              <div class="tip-class">
                <div class="tip-circle" :class="{'circle-active-finish': scope.row.status == 2}">
                  <span v-if="scope.row.status != 2">{{ scope.row.serviceStepList.length + 2 }}</span>
                  <span v-else>✔</span>
                </div>
              </div>
            </el-tooltip>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="派工信息" align="center" width="150px" prop>
        <template slot-scope="scope">
          <div v-show="scope.row.operatorUserName != null ">{{scope.row.operatorUserName}}</div>
          <div v-show="scope.row.operatorUserName == null ">暂未派工</div>
        </template>
      </el-table-column>

     <el-table-column label="服务状态" align="center" width="150px" prop class-name="small-padding fixed-width">
       <template align="center" slot-scope="scope">
          <span v-if="scope.row.serviceStatus == 2"
                class="text-danger">停止服务</span>
          <span v-else
                class="text-success">正常服务</span>
       </template>
     </el-table-column>

      <el-table-column label="任务状态" align="center" fixed="right" width="130px" prop class-name="small-padding fixed-width">
        <template align="center" slot-scope="scope">
          <el-button
            size="small"
            v-show="scope.row.status == 0 && scope.row.serviceStatus == 0"
            type="primary"
            @click="startGame(scope.row)"
          >开始服务</el-button>
          <el-button
            size="small"
            v-show="scope.row.status == 1 && scope.row.serviceStatus == 0"
            type="success"
            @click="proceedGame(scope.row)"
          >&nbsp;&nbsp;服务中&nbsp;&nbsp;</el-button>
          <el-button
            size="small"
            v-show="scope.row.status == 2 && scope.row.serviceStatus == 0"
            type="info"
            @click="proceedGame(scope.row)"
          >服务完成</el-button>
          <span v-show="scope.row.serviceStatus == 2">--</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改工商服务任务对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="670px"
      @close="contractClose('form')"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="110px"
        :label-position="labelPosition"
      >
        <el-row :gutter="10">
          <el-col :xs="24" :md="12" :xl="12">
            <el-form-item label="客户姓名" prop="customerId">
              <el-select
                v-model="form.customerId"
                placeholder="请选择客户"
                clearable
                filterable
                :filter-method="dataFilter"
                :disabled="isDisabled"
              >
                <el-option
                  v-for="dict in customers"
                  :key="dict.id"
                  :label="dict.customerName"
                  :value="dict.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12" :xl="12">
            <el-form-item label="合同编号" prop="contractNo">
              <el-input
                v-model="form.contractNo"
                placeholder="请输入合同编号"
                readonly
                :disabled="isDisabled"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :xs="24" :md="12" :xl="12">
            <el-form-item label="服务类型" prop="serviceTypeId">
              <el-select
                v-model="form.serviceTypeId"
                @change="getServiceItem"
                placeholder="请选择服务类型"
                :disabled="isDisabled"
              >
                <el-option
                  v-for="label in serviceTypes"
                  :key="label.serviceTypeId"
                  :label="label.serviceTypeName"
                  :value="parseInt(label.serviceTypeId)"
                >{{label.serviceTypeName}}</el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12" :xl="12">
            <el-form-item label="服务项目" prop="serviceItemId">
              <el-select v-model="form.serviceItemId" placeholder="请选择服务项目" :disabled="aaum">
                <el-option
                  v-for="label in serviceItems"
                  :key="label.serviceItemId"
                  :label="label.serviceItemName"
                  :value="parseInt(label.serviceItemId)"
                >{{label.serviceItemName}}</el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :md="12" :xl="12">
            <el-form-item label="合同开始时间" prop="contractStartDate">
              <el-date-picker
                clearable
                size="small"
                style="width: 200px"
                v-model="form.contractStartDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择合同开始时间"
                :picker-options="pickerOptionsStart"
                :disabled="agency"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12" :xl="12">
            <el-form-item label="合同结束时间" prop="contractEndDate">
              <el-date-picker
                clearable
                size="small"
                style="width: 200px"
                v-model="form.contractEndDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择合同结束时间"
                :picker-options="pickerOptionsEnd"
                :disabled="agency"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :md="12" :xl="12">
            <el-form-item label="付款方式" prop="payType">
              <el-select v-model="form.payType" placeholder="请选择付款方式" :disabled="agency">
                <el-option
                  v-for="dict in payTypes"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                >{{dict.dictLabel}}</el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12" :xl="12">
            <el-form-item label="合同金额" prop="receivedMoney">
              <el-input v-model="form.receivedMoney" placeholder="请输入合同金额" :disabled="agency" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :md="12" :xl="12">
            <el-form-item label="签单人" prop="signStaffUserId">
              <el-select v-model="form.signStaffUserId" placeholder="请选择签单人">
                <el-option
                  v-for="dict in staffs"
                  :key="dict.userId"
                  :label="dict.userName"
                  :value="parseInt(dict.userId)"
                >{{dict.userName}}</el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12" :xl="12"></el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加分配人员 -->
    <el-dialog
      :title="title"
      :visible.sync="allocation"
      width="400px"
      @close="distrutionClose('distrbutionInfo')"
      append-to-body
    >
      <el-form
        ref="distrbutionInfo"
        :model="distrbutionInfo"
        :rules="distbutionrules"
        label-width="80px"
      >
        <el-form-item label="工商合同" prop="contractNo" style="width: 280px">
          <el-input v-model="distrbutionInfo.contractNo" readonly />
        </el-form-item>

        <el-form-item label="客户名称" prop="customerName" style="width: 280px">
          <el-input v-model="distrbutionInfo.customerName" readonly />
        </el-form-item>

        <el-form-item label="工商外勤" prop="managerAccountingUserId" style="width: 280px">
          <el-select v-model="distrbutionInfo.managerAccountingUserId" placeholder="请选择工商外勤">
            <el-option
              v-for="dict in accountManagers"
              :key="dict.userId"
              :label="dict.userName"
              :value="parseInt(dict.userId)"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer el-row">
        <el-button type="primary" @click="submitDistruteForm">确 定</el-button>
        <el-button @click="cancelDistrute">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listCommerce,
  getCommerce,
  delCommerce,
  addCommerce,
  updateCommerce,
  exportCommerce,
  gongCommerce,
  gongZhiixng,
  addDistrution,
  stopService,
  resumeService
} from "@/api/commerce/commerce";
import { listCustomer, getServerTypes } from "@/api/customer/customer";
import {
  getServiceItems,
  getContractNo,
  updateContract,
  addContract,
  getContract,
} from "@/api/customer/contract";
import { getCompanyStaff } from "@/api/system/user";
import { getPersonList, getCustomer } from "@/api/customer/customer";
export default {
  name: "Commerce",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],

      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 工商服务任务表格数据
      commerceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //分配
      allocation: false,
      // 分配信息
      // 工商外勤
      accountManagers: {},
      // 会计助理

      distrbutionInfo: {
        customerName: undefined,
        managerAccountingUserId: undefined,
        contractNo: undefined,
        id: undefined,
      },
      distbutionrules: {
        managerAccountingUserId: [
          { required: true, message: "记账会计不能为空", trigger: "blur" },
        ],
      },
      //分配客户
      //customerName: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        customerName: undefined,
        serviceTypeName: undefined,
        status: undefined,
        serviceStatus: undefined,
        taskStatus: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        customerId: [
          { required: true, message: "客户名称不能为空", trigger: "blur" },
        ],
        contractNo: [
          { required: true, message: "合同编号不能为空", trigger: "blur" },
        ],
        serviceTypeId: [
          { required: true, message: "服务类型不能为空", trigger: "blur" },
        ],
        serviceItemId: [
          { required: true, message: "服务项目不能为空", trigger: "blur" },
        ],
        contractStartDate: [
          { required: true, message: "合同开始时间不能为空", trigger: "blur" },
        ],
        contractEndDate: [
          { required: true, message: "合同结束时间不能为空", trigger: "blur" },
        ],
        payType: [
          { required: true, message: "付款方式不能为空", trigger: "blur" },
        ],
        receivedMoney: [
          {
            pattern: /^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/,
             required: true,
            message: "请输入正确金额,金额最小到分",
            trigger: "blur",
          },
        ],
        signStaffUserId: [
          { required: true, message: "签单人不能为空", trigger: "blur" },
        ],
      },
      //工商服务类型列表
      gong: {
        serviceClassId: 2,
      },
      //工商服务类型列表
      commercegong: {},
      // value: "",
      // 客户
      customers: {},
      // 服务项目
      serviceItems: {},
      // 服务类型
      serviceTypes: {},
      // 拷贝客户
      copyCustomers: {},
      // 标签对其方式
      labelPosition: "right",
      // pickerOptionsStart 与 pickerOptionsEnd 设置结束日期大于开始日期并且开始日期小于结束日期
      pickerOptionsStart: {
        disabledDate: (time) => {
          if (this.form.contractEndDate) {
            return (
              time.getTime() > new Date(this.form.contractEndDate).getTime()
            );
          }
        },
      },
      pickerOptionsEnd: {
        disabledDate: (time) => {
          if (this.form.contractStartDate) {
            return (
              time.getTime() <
              new Date(this.form.contractStartDate).getTime() - 86400000
            );
          }
        },
      },
      // 支付方式
      payTypes: {},
      // 该公司所有员工
      staffs: {},
      isDisabled: false,
      //工商服务可修改
      aaum: false,
      //代账服务可修改
      agency: false,
      idss: [],
      //分配客户姓名
      idsss: [],
      //任务状态
      statusList: [
        {
          payment: "开始服务",
          payMent: 0,
        },
        {
          payment: "服务中",
          payMent: 1,
        },
        {
          payment: "服务完成",
          payMent: 2,
        },
      ],
      // 服务类型
      serverTypes: [
        { id: 0, status: "正常服务" },
        { id: 2, status: "停止服务" }
      ],
    };
  },
  created() {
    this.getList();
    this.gonglist();
    this.getCustomer();
    this._getServerTypes();
    // 字典获取支付方式
    this.getDicts("pay_type").then((response) => {
      this.payTypes = response.data;
    });
    this._getCompanyStaff();
    this.getAccountManager();
  },
  methods: {
    // 停止服务
    stopService(){
      stopService(this.ids).then(res=>{
        if(res.code == '200'){
          this.msgSuccess("停止服务成功");
          this.getList();
        }
      })
    },
    // 恢复服务
    resumeService(){
      resumeService(this.ids).then(res=>{
        if(res.code == '200'){
          this.msgSuccess("恢复服务成功");
          this.getList();
        }
      })
    },
    // 获取工商外勤列表
    getAccountManager: function () {
      getPersonList({ roleKey: "legwork" }).then((response) => {
        this.accountManagers = response.data;
      });
    },

    /** 查询工商服务任务列表 */
    getList() {
      this.loading = true;
      listCommerce(this.queryParams).then((response) => {
        this.commerceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 获取客户数据
    getCustomer() {
      listCustomer().then((response) => {
        this.customers = response.rows;
        // 拷贝客户数据，便于新增合同选择客户时过滤数据
        this.copyCustomers = response.rows;
      });
    },
    // 获取服务类型
    _getServerTypes() {
      getServerTypes().then((response) => {
        this.serviceTypes = response.rows;
      });
    },
    // 获取该公司的所有员工
    _getCompanyStaff() {
      getCompanyStaff().then((response) => {
        this.staffs = response.data;
      });
    },
    // 获取合同编号
    _getContractNo() {
      getContractNo().then((response) => {
        this.form.contractNo = response;
      });
    },
    // 根据选择的服务类型获取服务项目
    getServiceItem(selVal) {
      let serviceTypeId = selVal;
      getServiceItems({ serviceTypeId }).then((response) => {
        this.serviceItems = response.rows;
      });
    },
    // 新增合同下拉可搜索客户
    dataFilter(val) {
      if (val) {
        this.customers = this.copyCustomers.filter((item) => {
          if (
            !!~item.customerName.indexOf(val) ||
            !!~item.customerName.toUpperCase().indexOf(val.toUpperCase())
          ) {
            return true;
          } else {
            return false;
          }
        });
      } else {
        //val为空时，还原数组
        this.customers = this.copyCustomers;
      }
    },
    // 付款方式字典翻译
    payTypesFormat(row, column) {
      return this.selectDictLabel(this.payTypes, row.payType);
    },
    //查询工商服务类型列表
    gonglist() {
      gongCommerce(this.gong).then((response) => {
        this.commercegong = response.rows;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    contractClose(el) {
      this.clearFormValidate(el);
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        contractId: null,
        contractNo: null,
        deptId: null,
        customerId: null,
        customerName: "",
        serviceTypeId: null,
        serviceTypeName: "",
        serviceItemId: null,
        serviceItemName: "",
        contractStartDate: "",
        contractEndDate: "",
        payType: "",
        receivedMoney: null,
        signStaffUserId: null,
        signStaffName: "",
        createTime: "",
        updateTime: "",
        createBy: null,
        remark: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");

      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.idss = selection.map((item) => item.contractId);
      this.idsss = selection.map((item) => item.customerName);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加工商服务任务";
      this._getContractNo();
      this.isDisabled = false;
      this.aaum = false;
      this.agency = false;
    },
    // 修改按钮操作
    handleUpdate(row) {
      this.reset();
      this.isDisabled = true;
      const contractId = row.contractId || this.idss;
      this.getCustomer();
      getContract(contractId).then((response) => {
        this.form = response.data;
        if (response.type == 0) {
          if (response.serviceStatus == 0) {
            this.aaum = false;
            this.agency = false;
          } else {
            this.aaum = true;
            this.agency = false;
          }
        } else {
          if (response.serviceStatus == 0) {
            this.aaum = false;
            this.agency = false;
          } else {
            this.aaum = true;
            this.agency = true;
          }
        }

        // console.log(this.form);
        this.getServiceItem(response.data.serviceTypeId);
        this.open = true;
        this.title = "修改合同";
      });
    },
    // 提交按钮
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.contractId != undefined) {
            updateContract(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addContract(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      const customerName = this.idsss;
      this.$confirm(
        '是否确认删除客户名称为"' + customerName + '"的数据项?',
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delCommerce(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有工商服务任务数据项?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportCommerce(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        })
        .catch(function () {});
    },
    // 分配按钮操作
    handleDistribute: function () {
      this.allocation = true;
      this.title = "分配";
      this.customerName = [];
      for (let i = 0; i < this.commerceList.length; i++) {
        for (let j = 0; j < this.ids.length; j++) {
          if (this.commerceList[i].id == this.ids[j]) {
            this.distrbutionInfo.customerName = this.commerceList[
              i
            ].customerName;
            this.distrbutionInfo.contractNo = this.commerceList[i].contractNo;
            this.distrbutionInfo.id = this.commerceList[i].id;
          }
        }
      }


      //console.log(this.customerName);
      // getCustomer(contractId).then((response) => {
      //   this.distrbutionInfo = response.data;
      //   this.customerName = response.data.customerName;
      //   this.allocation = true;
      //
      // });
    },

    // 分配信息提交
    submitDistruteForm: function () {
      let distrbutionInfoList = {
        id: this.distrbutionInfo.id,
        operatorUserId: this.distrbutionInfo.managerAccountingUserId,
      };

      this.$refs["distrbutionInfo"].validate((valid) => {
        if (valid) {
          addDistrution(distrbutionInfoList).then((response) => {
            if (response.code === 200) {
              this.msgSuccess("分配成功");
              this.allocation = false;
              this.getList();
            }
          });
        }
      });
    },
    // 分配弹框关闭
    distrutionClose(el) {
      this.clearFormValidate(el);
      this.allocation = false;
    },
    cancelDistrute() {
      this.distrutionClose("distrbutionInfo");
    },
    // handleDistribute() {
    //   this.allocation = true;
    //   this.customerNamee = [];
    //   for (let i = 0; i < this.commerceList.length; i++) {
    //     for (let j = 0; j < this.ids.length; j++) {
    //       if (this.commerceList[i].id == this.ids[j]) {
    //         this.customerNamee.push(this.commerceList[i].customerName);
    //       }
    //     }
    //   }
    // },
    //开始服务
    startGame(row) {
      const tableId = row.id; //|| this.ids[0];
      // this.$router.push({ path: "/businessServe/sever" });
      if (row.operatorUserName == null) {
        this.$message({
          showClose: true,
          message: "派工人员为空，服务无法执行",
          type: "error",
          duration: 1500,
        });
      } else {
        gongZhiixng(tableId).then((response) => {
          this.getList();
        });
      }
    },
    //服务中
    proceedGame(row) {
      const tableId = row.id; //|| this.ids[0];

      this.$router.push({
        path: "/businessServe/sever",
        query: { tableId: tableId },
      });
    },
  },
  watch: {
    $route: "getList",
  },
};
</script>
<style lang="scss" scoped>
.datum {
  text-overflow: -o-ellipsis-lastline;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}
/deep/.el-button--small {
  margin-left: 10px;
}

.tip-class {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}

.tip-circle {
  width: 25px;
  height: 25px;
  border: 2px solid rgb(192, 192, 192);
  border-radius: 25px;
  justify-content: center;
  display: flex;
  align-items: center;

  span {
    font-weight: bold;
  }
}
.circle-active-run {
  border: 2px solid #0076F6;
  color: #0076F6;
}
.circle-active-finish {
  border: 2px solid #13ce66;
  color: #13ce66;
}
.tip-line {
  height: 3px;
  width: 8px;
  background-color: rgb(192, 192, 192);
}

/* 进行中状态：圈线 */
/deep/.el-step__head.is-process {
  color: #0076F6;
  border-color: #0076F6;
}
/* 进行中状态：圈内 */
/*/deep/.el-step__head.is-process > .el-step__icon {*/
/*  background: #0076F6;*/
/*  color: #fff;*/
/*}*/
/* 进行中状态：title（文字） */

/deep/.el-step__title.is-process {
  color: #0076F6;
  font-size: 14px;
}

</style>
