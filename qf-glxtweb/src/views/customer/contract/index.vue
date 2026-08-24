<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="客户姓名" prop="customerName">
        <el-input
          v-model="queryParams.customerName"
          placeholder="请输入客户姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="合同编号" prop="contractNo">
        <el-input
          v-model="queryParams.contractNo"
          placeholder="请输入合同编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="服务类型" prop="serviceTypeName">
        <el-select v-model="queryParams.serviceTypeId" clearable placeholder="请选择服务类型">
          <el-option
            v-for="label in serviceTypes"
            :key="label.serviceTypeId"
            :label="label.serviceTypeName"
            :value="parseInt(label.serviceTypeId)"
          >{{label.serviceTypeName}}</el-option>
        </el-select>
      </el-form-item>
      <!--<el-form-item label="服务项目"
                    prop="serviceItemName">
        <el-input v-model="queryParams.serviceItemName"
                  placeholder="请输入服务项目"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery" />
      </el-form-item>-->
      <!--<el-form-item label="合同开始时间"
                    prop="contractStartDate">
        <el-date-picker clearable
                        size="small"
                        style="width: 200px"
                        v-model="queryParams.contractStartDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="选择合同开始时间">
        </el-date-picker>
      </el-form-item>-->
      <!--<el-form-item label="合同结束时间"
                    prop="contractEndDate">
        <el-date-picker clearable
                        size="small"
                        style="width: 200px"
                        v-model="queryParams.contractEndDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="选择合同结束时间">
        </el-date-picker>
      </el-form-item>-->
      <!--<el-form-item label="付款方式:一次性付款，分期付款"
                    prop="payType">
        <el-select v-model="queryParams.payType"
                   placeholder="请选择付款方式:一次性付款，分期付款"
                   clearable
                   size="small">
          <el-option label="请选择字典生成"
                     value="" />
        </el-select>
      </el-form-item>-->
      <!--<el-form-item label="金额"
                    prop="receivedMoney">
        <el-input v-model="queryParams.receivedMoney"
                  placeholder="请输入金额"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery" />
      </el-form-item>-->
      <!--<el-form-item label="签单人"
                    prop="signStaffName">
        <el-input v-model="queryParams.signStaffName"
                  placeholder="请输入签单人"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery" />
      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['customer:contract:add']"
        >新增</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['customer:contract:edit']"
        >修改</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['customer:contract:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['customer:contract:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="contractList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="合同编号" align="center" prop="contractNo" />
      <el-table-column label="客户姓名" align="center" prop="customerName" />
      <el-table-column label="服务类型" align="center">
        <template slot-scope="scope">
          <span
            :class="scope.row.serviceTypeName === '代理记账'?'booking':scope.row.serviceTypeName === '工商注册'?'registered':scope.row.serviceTypeName === '工商变更' ? 
          'change': 'cancel'"
          >{{scope.row.serviceTypeName.substr(scope.row.serviceTypeName.length-1, 1)}}</span>
        </template>
      </el-table-column>
      <el-table-column label="服务项目" align="center" prop="serviceItemName" />
      <!--<el-table-column label="合同开始时间"
                       align="center"
                       prop="contractStartDate"
                       width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.contractStartDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>-->
      <!--<el-table-column label="合同结束时间"
                       align="center"
                       prop="contractEndDate"
                       width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.contractEndDate}}</span>
        </template>
      </el-table-column>-->
      <el-table-column label="合同期限" align="center" prop="合同日期" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.contractStartDate,'{y}-{m}-{d}')}} - {{parseTime(scope.row.contractEndDate, '{y}-{m}-{d}')}}</span>
        </template>
      </el-table-column>
      <el-table-column label="付款方式" align="center" prop="payType">
        <template slot-scope="scope">
          <span v-show="scope.row.payType  == 1">一次性付款</span>
          <span v-show="scope.row.payType  == 2">按月分期</span>
          <span v-show="scope.row.payType  == 3">按季分期</span>
          <span v-show="scope.row.payType  == 4">按半年分期</span>
          <span v-show="scope.row.payType  == 5">按年分期</span>
        </template>
      </el-table-column>
      <el-table-column label="合同金额" align="center" prop="receivedMoney" />
      <el-table-column label="签单人" align="center" prop="signStaffName" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['customer:contract:edit']"
          >修改</el-button>
          <!--<el-button size="mini"
                     type="text"
                     icon="el-icon-delete"
                     @click="handleDelete(scope.row)"
          v-hasPermi="['system:contract:remove']">删除</el-button>-->
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

    <!-- 添加或修改【请填写功能名称】对话框 -->
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
  </div>
</template>

<script>
import {
  listContract,
  getContract,
  delContract,
  addContract,
  updateContract,
  exportContract,
  getServiceItems,
  getContractNo,
} from "@/api/customer/contract";
import { listCustomer, getServerTypes } from "@/api/customer/customer";
import { getCompanyStaff } from "@/api/system/user";
import { dformatDate } from "@/utils/index";

export default {
  name: "Contract",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      idss: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 【请填写功能名称】表格数据
      contractList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        contractNo: null,
        deptId: null,
        customerId: null,
        customerName: "",
        serviceTypeId: null,
        serviceTypeName: "",
        serviceItemId: null,
        serviceItemName: "",
        contractStartDate: null,
        contractEndDate: null,
        payType: null,
        receivedMoney: null,
        signStaffUserId: null,
        signStaffName: "",
      },
      // 表单参数
      form: {
        serviceTypeId: "1",
      },
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
            message: "请输入正确金额,列如:66.66", 
            trigger: "blur",
          },
        ],
        signStaffUserId: [
          { required: true, message: "签单人不能为空", trigger: "blur" }, 
        ],
      },
      // 客户
      customers: [],
      // 拷贝客户
      copyCustomers: [],
      // 服务类型
      serviceTypes: [],
      // 服务项目
      serviceItems: [],
      // 支付方式
      payTypes: [],
      payTypess: [],
      // 该公司所有员工
      staffs: [],
      // 标签对其方式
      labelPosition: "right",
      // 编辑合同时元素是否可用
      isDisabled: false,
      //工商服务可修改
      aaum: false,
      //代账服务可修改
      agency: false,
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
    };
  },
  created() {
    this.getList();
    this.getCustomer();
    this._getServerTypes();

    this._getDicts();
    
    this.getServiceItem();
  },
  methods: {
    /** 查询【请填写功能名称】列表 */
    getList() {
      this.loading = true;
      listContract(this.queryParams).then((response) => {
        this.contractList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 字典获取支付方式
    _getDicts() {
      this.getDicts("pay_type").then((response) => {
        this.payTypes = response.data;

        this.payTypess = response.data;
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
        // this.form.serviceTypeId = parseInt(this.serviceTypes[0].serviceTypeId)
      });
    },
    // 根据选择的服务类型获取服务项目
    getServiceItem(selVal) {
      if (this.form.serviceTypeId == 1) {
        let serviceTypeId = 1;
        getServiceItems({ serviceTypeId }).then((response) => {
          this.serviceItems = response.rows;
          this.payTypes = this.payTypess;
        });
      } else {
        let serviceTypeId = selVal;

        getServiceItems({ serviceTypeId }).then((response) => {
          this.serviceItems = response.rows;

          this.payTypes = [];
          this.payTypes.push(this.payTypess[0]);
        });
      }
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
    // 付款方式字典翻译
    payTypesFormat(row, column) {
      return this.selectDictLabel(this.payTypes, row.payType);
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
        serviceTypeId: 1,
        serviceTypeName: "",
        serviceItemId: null,
        serviceItemName: "",
        contractStartDate: "",
        contractEndDate: "",
        payType: null,
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
    // 搜索按钮操作
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    // 重置按钮操作
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.serviceTypeId = null;
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.contractId);
      this.idss = selection.map((item) => item.customerName);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    // 新增按钮操作
    handleAdd() {
      this.reset();
      this._getCompanyStaff();
      this.open = true;
      this.isDisabled = false;
      this.aaum = false;
      this.agency = false;
      this._getContractNo();
      this.getCustomer();
      this.title = "新增合同";
      //console.log(this.aaum,this.agency)
      


    },
    // 修改按钮操作
    handleUpdate(row) {
      this.reset();
      this._getCompanyStaff();
      this.isDisabled = true;
      const contractId = row.contractId || this.ids;
      this.getCustomer();
      getContract(contractId).then((response) => {
        
          this.form = Object.assign({}, response.data);

        for (var i = 0; i < this.staffs.length; i++) {
          if (
            this.staffs[i].userId !=
            response.data.signStaffUserId
          ) {
            this.form.signStaffUserId = undefined;
          } else {
            this.$set(
              this.form,
              "signStaffUserId",
              response.data.signStaffUserId
            );
            break;
          }
        }








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
    // 删除按钮操作
    handleDelete(row) {
      const contractIds = row.contractId || this.ids;
      const idss = row.customerName || this.idss;
      this.$confirm('是否确认删除客户姓名为"' + idss + '"的数据项?', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delContract(contractIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
    // 导出按钮操作
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有客户数据项?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportContract(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        })
        .catch(function () {});
    },
  },
};
</script>
<style lang="scss" scope>
@import "~@/assets/styles/mixin.scss";

.booking {
  @include servicetype(rgb(0, 140, 255));
}
.registered {
  @include servicetype(rgb(89, 212, 158));
}
.change {
  @include servicetype(rgb(212, 129, 89));
}
.cancel {
  @include servicetype(rgb(67, 68, 236));
}
</style>