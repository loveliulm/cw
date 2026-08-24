<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="客户名称" prop="customerName">
        <el-input
          v-model="queryParams.customerName"
          placeholder="请输入客户名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="记账会计" prop="bookkeepingAccountingUserId">
        <el-select
          v-model="queryParams.bookkeepingAccountingUserId"
          clearable
          placeholder="请选择记账会计"
        >
          <el-option
            v-for="dict in accountings"
            :key="dict.userId"
            :label="dict.userName"
            :value="parseInt(dict.userId)"
          ></el-option>
        </el-select>
      </el-form-item>
      <!--<el-form-item label="服务类型"
                    prop="tcustomerServiceTypes">
        <el-select v-model="queryParams.tcustomerServiceTypes"
                   placeholder="请选择服务类型"
                   clearable
                   size="small">
          <el-option v-for="item in serviceTypes"
                     :key="item.serviceTypeId"
                     :label="item.serviceTypeName"
                     :value="item.serviceTypeId" />
        </el-select>
      </el-form-item>-->
      <!--<el-form-item label="账套状态"
                    prop="accountingStatus">
        <el-select v-model="queryParams.accountingStatus"
                   placeholder="请选择账套状态"
                   clearable
                   size="small">
          <el-option v-for="dict in accountingStatusOptions"
                     :key="dict.dictValue"
                     :label="dict.dictLabel"
                     :value="dict.dictValue" />
        </el-select>
      </el-form-item>-->
      <!--<el-form-item label="联系人电话"
                    prop="normalContactPhone">
        <el-input v-model="queryParams.normalContactPhone"
                  placeholder="请输入联系人电话"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery" />
      </el-form-item>-->
      <!--<el-form-item label="是否办理税盘"
                    prop="isopentaxplate">
        <el-input v-model="queryParams.isopentaxplate"
                  placeholder="请输入是否办理税盘"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery" />
      </el-form-item>-->
      <!--<el-form-item label="国税密码"
                    prop="countryTaxPass">
        <el-input v-model="queryParams.countryTaxPass"
                  placeholder="请输入国税密码"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery" />
      </el-form-item>-->
      <!--<el-form-item label="个税申报密码"
                    prop="incomeTaxPass">
        <el-input v-model="queryParams.incomeTaxPass"
                  placeholder="请输入个税申报密码"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery" />
      </el-form-item>-->
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['customer:customer:add']"
          >新增</el-button
        >
      </el-col>
      <!--后期进行开发，批量分配功能-->
      <!--<el-col :span="1.5">
        <el-button type="success"
                   icon="el-icon-edit"
                   size="mini"
                   :disabled="multiple"
                   @click="handleDistribute"
                   v-hasPermi="['customer:customer:edit']">分配</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['customer:customer:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['invoice:buyer:export']"
          >导入</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['customer:customer:export']"
          >导出</el-button
        >
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="customerList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <!--<el-table-column label="客户id"
                       align="center"
      prop="id" />-->
      <el-table-column label="客户编码" align="center" prop="customerCode" />
      <el-table-column label="客户名称" align="center" prop="customerName" />
      <el-table-column
        label="服务类型"
        align="left"
        prop="tcustomerServiceTypes"
      >
        <template slot-scope="scope">
          <span
            v-for="item in scope.row.tcustomerServiceTypes"
            :key="item.index"
            :class="
              item.serviceTypeName === '代理记账'
                ? 'booking'
                : item.serviceTypeName === '工商注册'
                ? 'registered'
                : item.serviceTypeName === '工商变更'
                ? 'change'
                : 'cancel'
            "
            >{{
              item.serviceTypeName.substr(item.serviceTypeName.length - 1, 1)
            }}</span
          >
        </template>
      </el-table-column>
      <el-table-column
        label="纳税性质"
        align="center"
        prop="vatType"
        :formatter="vatTypeFormat"
      />
      <!--<el-table-column label="记账会计"
                       align="center"
      prop="bookkeepingAccounting" />-->
      <!--<el-table-column label="账套状态"
                       align="center"
                       prop="accountingStatus"
      :formatter="accountingStatusFormat" />-->
      <el-table-column label="联系人电话" align="center" prop>
        <template slot-scope="scope">
          <span v-show="scope.row.normalContactPhone == ''">暂无</span>
          <span v-show="scope.row.normalContactPhone != ''">{{
            scope.row.normalContactPhone
          }}</span>
        </template>
      </el-table-column>
      <el-table-column label="记账会计" align="center" prop>
        <template slot-scope="scope">
          <span v-show="scope.row.bookkeepingAccounting == null">暂无</span>
          <span v-show="scope.row.bookkeepingAccounting != null">{{
            scope.row.bookkeepingAccounting
          }}</span>
        </template>
      </el-table-column>

      <!--<el-table-column label="是否办理税盘"
                       align="center"
                       prop="isopenTaxplate"
      :formatter="isopenTaxplateFormat" />-->
      <!--<el-table-column label="国税密码"
                       align="center"
      prop="countryTaxPass" />-->
      <!--<el-table-column label="个税申报密码"
                       align="center"
      prop="incomeTaxPass" />-->
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['customer:customer:edit']"
            >修改</el-button
          >
          <!--<el-button size="mini"
                     type="text"
                     icon="el-icon-delete"
                     @click="handleDelete(scope.row)"
          v-hasPermi="['customer:customer:remove']">删除</el-button>-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-user"
            @click="handleDistribute(scope.row)"
            v-hasPermi="['customer:customer:dispatch']"
            v-show="scope.row.statuss == 1 ? true : false"
            >分配</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改客户对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="1300px"
      @close="customerClose('form')"
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
          <el-col :xs="24" :md="8" :xl="8">
            <el-form-item label="客户编码" prop="customerCode">
              <el-input
                v-model="form.customerCode"
                placeholder="请输入客户编码"
                readonly
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="8" :xl="8">
            <el-form-item label="客户名称" prop="customerName">
              <el-input
                v-model="form.customerName"
                maxlength="20"
                placeholder="请输入客户名称"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="8" :xl="8">
            <el-form-item label="社会统一信用码" prop="unifiedSocialCreditCode">
              <el-input
                v-model="form.unifiedSocialCreditCode"
                maxlength="30"
                placeholder="请输入社会统一信用码"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :xs="24" :md="8" :xl="8">
            <el-form-item label="纳税性质" prop="vatType">
              <el-select
                v-model="form.vatType"
                placeholder="请选择纳税性质"
                style="width: 302.6px"
              >
                <el-option
                  v-for="dict in vatTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="8" :xl="8">
            <el-form-item label="法定代表人" prop="representative">
              <el-input
                maxlength="20"
                v-model="form.representative"
                placeholder="请输入法定代表人"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="8" :xl="8">
            <el-form-item label="成立日期" prop="establishmentDate">
              <el-date-picker
                clearable
                size="small"
                style="width: 302.6px"
                v-model="form.establishmentDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择成立日期"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="10">
          <el-col :xs="24" :md="8" :xl="8">
            <el-form-item label="注册地址" prop="registrationAddress">
              <el-input
                v-model="form.registrationAddress"
                placeholder="请输入注册地址"
                maxlength="50"
              />
            </el-form-item>
          </el-col>
          <!--<el-col :xs="24"
                  :md="12"
                  :xl="12">
            <el-form-item label="账套状态">
              <el-radio-group v-model="form.accountingStatus">
                <el-radio v-for="dict in accountingStatusOptions"
                          :key="dict.dictValue"
                          :label="parseInt(dict.dictValue)">{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>-->
          <el-col :xs="24" :md="8" :xl="8">
            <el-form-item label="联系人电话" prop="normalContactPhone">
              <el-input
                maxlength="30"
                v-model="form.normalContactPhone"
                placeholder="请输入联系人电话"
              />
            </el-form-item> 
          </el-col>
          <el-col :xs="24" :md="8" :xl="8">
            <el-form-item label="法人身份证" prop="representativeCard">
              <el-input
                maxlength="18"
                v-model="form.representativeCard"  
                placeholder="请输入法人身份证"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="10">
          <el-col :xs="24" :md="8" :xl="8">
            <el-form-item label="是否有税控盘">
              <el-select
                v-model="form.isopenTaxplate"
                placeholder="请选择"
                style="width: 302.6px"
              >
                <el-option
                  v-for="dict in taxplateOptions"
                  :key="dict.id"
                  :label="dict.name"
                  :value="parseInt(dict.id)"
                  >{{ dict.name }}</el-option
                >
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="8" :xl="8">
            <el-form-item label="国税密码" prop="countryTaxPass">
              <el-input
                v-model="form.countryTaxPass"
                placeholder="请输入国税密码"
                maxlength="30"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="8" :xl="8">
            <el-form-item label="个税申报密码" prop="incomeTaxPass">
              <el-input
                maxlength="10"
                v-model="form.incomeTaxPass"
                placeholder="请输入个税申报密码"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="10">
          <el-col :xs="24" :md="12" :xl="12">
            <el-form-item label="服务类型" prop="tcustomerServiceTypes">
              <el-checkbox-group
                v-model="form.tcustomerServiceTypes"
                @change="selectAccounting"
              >
                <el-checkbox
                  v-for="label in serviceTypes"
                  :key="label.serviceTypeId"
                  :label="label.serviceTypeId"
                  >{{ label.serviceTypeName }}</el-checkbox
                >
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          
        </el-row>
        <el-row :gutter="10">
          <el-col :xs="24" :md="24" :xl="24">
            <el-form-item label="记账会计" v-if="isAccounting">
              <el-select
                v-model="form.bookkeepingAccountingUserId"
                placeholder="请选择记账会计"
                style="width: 302.6px"
              >
                <el-option
                  v-for="dict in accountings"
                  :key="dict.userId"
                  :label="dict.userName"
                  :value="parseInt(dict.userId)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer el-row">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--分配-->
    <el-dialog
      :title="title"
      :visible.sync="openDistrbution"
      width="500px"
      @close="distrutionClose('distrbutionInfo')"
      append-to-body
    >
      <el-form
        ref="distrbutionInfo"
        :model="distrbutionInfo"
        :rules="distbutionrules"
        label-width="80px"
      >
        <el-row>
          <el-col>
            <el-form-item label="客户名称" prop="customerName">
              <el-input v-model="customerName" readonly />
            </el-form-item>
          </el-col>
          <el-col>
            <el-form-item label="记账会计" prop="bookkeepingAccountingUserId">
              <el-select
                v-model="distrbutionInfo.bookkeepingAccountingUserId"
                placeholder="请选择记账会计"
              >
                <el-option
                  v-for="dict in accountings"
                  :key="dict.userId"
                  :label="dict.userName"
                  :value="parseInt(dict.userId)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col>
            <el-form-item label="主管会计" prop="managerAccountingUserId">
              <el-select
                v-model="distrbutionInfo.managerAccountingUserId"
                placeholder="请选择主管会计"
              >
                <el-option
                  v-for="dict in accountManagers"
                  :key="dict.userId"
                  :label="dict.userName"
                  :value="parseInt(dict.userId)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col>
            <el-form-item label="助理会计" prop="assistantAccountingUserId">
              <el-select
                v-model="distrbutionInfo.assistantAccountingUserId"
                placeholder="请选择助理会计"
              >
                <el-option
                  v-for="dict in accountClerks"
                  :key="dict.userId"
                  :label="dict.userName"
                  :value="parseInt(dict.userId)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer el-row">
        <el-button type="primary" @click="submitDistruteForm">确 定</el-button>
        <el-button @click="cancelDistrute">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 用户导入对话框 -->
    <el-dialog
      :title="upload.title"
      :visible.sync="upload.open"
      width="400px"
      append-to-body
    >
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" slot="tip">
          <!-- <el-checkbox
            v-model="upload.updateSupport"
          />是否更新已经存在的用户数据 -->
          <el-link type="info" style="font-size: 12px" @click="importTemplate"
            >下载模板</el-link
          >
        </div>
        <div class="el-upload__tip" style="color: red" slot="tip">
          提示：仅允许导入“xls”或“xlsx”格式文件！
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listCustomer,
  getCustomer,
  delCustomer,
  addCustomer,
  updateCustomer,
  exportCustomer,
  getServerTypes,
  getPersonList,
  addDistrution,
  getCustomerNo,
  importTemplate,
} from "@/api/customer/customer";
import { getToken } from "@/utils/auth";
export default {
  name: "Customer",
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
      // 客户表格数据
      customerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 纳税性质字典
      vatTypeOptions: [],
      // 账套状态字典
      accountingStatusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        customerName: "",
        tcustomerServiceTypes: [],
        accountingStatus: null,
        normalContactPhone: "",
        isopenTaxplate: [],
        countryTaxPass: "",
        incomeTaxPass: "",
        bookkeepingAccountingUserId: null,
      },
      // 表单参数
      form: {
        
      },
      // 表单校验
      rules: {
        customerName: [
          { required: true, message: "客户名称不能为空", trigger: "blur" },
        ],
        vatType: [
          { required: true, message: " 纳税性质不能为空", trigger: "blur" },
        ],
        tcustomerServiceTypes: [
          { required: true, message: "请选择服务类型", trigger: "blur" },
        ],
        bankName: [
          { required: true, message: "请填写开户银行", trigger: "blur" },
        ],
        bankAcount: [
          { required: true, message: "请输入银行账号", trigger: "blur" },
        ],
        drawer: [{ required: true, message: "请输入开票人", trigger: "blur" }],
        representative: [
          {
            pattern: /^[\u4E00-\u9FA5]+$/,
            message: "法定代表人只能是中文",
            trigger: "blur",
          },
        ],
        normalContactPhone: [
          {
            pattern: /^1[3|4|5|7|8][0-9]{9}$/,
            message: "请输入正确的手机格式",
            trigger: "blur",
          },
        ],
        representativeCard: [
          {
            pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
            message: "你的身份证格式不正确",
            trigger: "blur",
          },
        ],
      },
      taxplateOptions: [
        { id: 0, name: "是" },
        { id: 1, name: "否" },
      ],
      // 标签对其方式
      labelPosition: "right",
      // 服务类型列表
      serviceTypes: [],
      // 分配
      openDistrbution: false,
      // 会计主管
      accountManagers: [],
      // 会计助理
      accountClerks: [],
      // 记账会计
      accountings: [],
      // 分配信息
      distrbutionInfo: [],
      // 分配表单校验规则
      distbutionrules: {
        bookkeepingAccountingUserId: [
          { required: true, message: "记账会计不能为空", trigger: "blur" },
        ],
      },
      // 分配时的客户名称
      customerName: "",
      // 新增或修改客户时
      isAccounting: false,
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/customer/customer/importData",
        tableData: [],
      },
      isOpenInvoice: 0,
    };
    fen: [];
  },
  created() {
    this.getList();
    this.getDicts("vat_type").then((response) => {
      this.vatTypeOptions = response.data;
    });
    this.getDicts("accounting_status").then((response) => {
      this.accountingStatusOptions = response.data;
    });
    this._getServerTypes();
    this.getAccounting()
    //this.getAccountManager(); 
    //this.getAccountClerk();
  },
  methods: {
    // 查询客户列表
    getList() {
      this.loading = true;
      listCustomer(this.queryParams).then((response) => {
        this.customerList = response.rows;
        for (var i = 0; i < this.customerList.length; i++) {
          this.customerList[i].statuss = 0;
          for (
            var j = 0;
            j < this.customerList[i].tcustomerServiceTypes.length;
            j++
          ) {
            if (
              this.customerList[i].tcustomerServiceTypes[j].serviceTypeName ===
              "代理记账"
            ) {
              this.customerList[i].statuss = 1;
              break;
            } else {
              this.customerList[i].statuss = 0;
            }
          }
        }
        this.total = response.total;
        this.loading = false;
      });
    },

    // 纳税性质字典翻译  
    vatTypeFormat(row, column) {
      return this.selectDictLabel(this.vatTypeOptions, row.vatType);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
    },

    // 账套状态字典翻译
    accountingStatusFormat(row, column) {
      return this.selectDictLabel(
        this.accountingStatusOptions,
        row.accountingStatus
      );
    },

    // table格式化是否有税控盘
    isopenTaxplateFormat(row) {
      return row.isopenTaxplate == 0 ? "是" : "否";
    },

    // 获取客户编码
    _getCustomerNo() {
      getCustomerNo().then((response) => {
        this.form.customerCode = response;
      });
    },

    // 获取服务类型
    _getServerTypes: function () {
      getServerTypes().then((response) => {
        this.serviceTypes = response.rows;
      });
    },

    // 获取会计主管列表
    getAccountManager: function () {
      getPersonList({ roleKey: "accountManager" }).then((response) => {
        this.accountManagers = response.data;
      });
    },

    // 获取会计助理列表
    getAccountClerk: function () {
      getPersonList({ roleKey: "accountClerk" }).then((response) => {
        this.accountClerks = response.data;
      });
    },

    // 获取记账会计列表
    getAccounting: function () {
      getPersonList({ roleKey: "accounting" }).then((response) => {
        this.accountings = response.data;
      });
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
      this.customerClose("form");
    },
    cancelDistrute() {
      this.distrutionClose("distrbutionInfo");
    },

    // 表单重置
    reset() {
      this.form = {
        id: 0,
        customerCode: null,
        customerName: "",
        unifiedSocialCreditCode: "",
        vatType: null,
        representative: "",
        establishmentDate: "",
        registrationAddress: "",
        bookkeepingAccounting: "",
        bookkeepingAccountingUserId:'',
        accountingStatus: null,
        normalContactPhone: "",
        representativeCard: "",
        remark: "",
        createTime: "",
        updateTime: "",
        isopenTaxplate: "",
        countryTaxPass: "",
        incomeTaxPass: "",
        tcustomerServiceTypes: [],
        isOpenInvoice: 0,
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
      this.queryParams.tcustomerServiceTypes = [];
      this.queryParams.bookkeepingAccountingUserId = "";
      this.handleQuery();
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.idss = selection.map((item) => item.customerName);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    // 新增按钮操作
    handleAdd() {
      this.reset();
      this.isAccounting = false;
      this.isOpenInvoice = false;
      this.open = true;
      this.title = "添加客户";
      this._getCustomerNo();
      this.getAccounting();
    },

    // 修改按钮操作
    handleUpdate(row) {
      this.reset();
      this.isAccounting = false;
      this.getAccounting();
      const id = row.id || this.ids;
      getCustomer(id).then((response) => {
        //this.form = response.data;
        this.form = Object.assign({}, response.data);

        for (var i = 0; i < this.accountings.length; i++) {
          if (
            this.accountings[i].userId !=
            response.data.bookkeepingAccountingUserId
          ) {
            this.form.bookkeepingAccountingUserId = undefined;
          } else {
            this.form.bookkeepingAccountingUserId =
              response.data.bookkeepingAccountingUserId;
            break;
          }
        }

        // console.log(response.data);
        // for (let i = 0; i < this.accountings.length; i++) {

        //   if ( this.accountings[i].userId  == response.data.bookkeepingAccountingUserId   || this.accountings[i].userId == null) {
        //     console.log(11);
        //      //this.form.bookkeepingAccountingUserId = response.data.bookkeepingAccountingUserId;
        //     // console.log(this.form.bookkeepingAccountingUserId);
        //      break;
        //   } else {
        //     console.log(22);
        //      //this.form.bookkeepingAccountingUserId = null;
        //     // console.log(this.form.bookkeepingAccountingUserId);
        //   }
        // }
        // 将返回的服务类型数据(tcustomerServiceTypes)[{serviceTypeId:1,serviceTypeName:'代理记账'}，{serviceTypeId:1,serviceTypeName:'代理记账'}]格式
        // 转化为[1,2]格式，因此调用spliteArray()方法对数据进行处理
        this.form.tcustomerServiceTypes = this.spliteArray(
          response.data.tcustomerServiceTypes
        );
        this.selectAccounting(this.form.tcustomerServiceTypes);
        this.open = true;
        this.title = "修改客户";

        // console.log(this.form);
      });
    },

    // 客户信息提交按钮
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          // 服务类型选中后，保存的数组(tcustomerServiceTypes)数据格式为[1,2],但接口需要的数据格式为
          // [{serviceTypeId:1,serviceTypeName:'代理记账'}，{serviceTypeId:1,serviceTypeName:'代理记账'}],因此调用spliceObject()方法对数据进行处理
          let newObj = this.spliceArray(
            this.serviceTypes,
            this.form.tcustomerServiceTypes
          );
          this.form.tcustomerServiceTypes = newObj;
          if (this.form.id != 0) {
            
           

            updateCustomer(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addCustomer(this.form).then((response) => {
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

    // 添加或修改客服弹框关闭
    customerClose(el) {
      this.clearFormValidate(el);
      this.isAccounting = false;
    },

    // 分配弹框关闭
    distrutionClose(el) {
      this.clearFormValidate(el);
      this.openDistrbution = false;
    },

    // 删除按钮操作
    handleDelete(row) {
      const ids = row.id || this.ids;
      const idss = row.customerName || this.idss;
      this.$confirm('是否确认删除客户名称为"' + idss + '"的数据项?', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delCustomer(ids);
        })  
        .then(() => {  
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },

    // 分配按钮操作
    handleDistribute: function (row) {
      this.getAccountManager(); 
      this.getAccountClerk();
      this.getAccounting();
      getCustomer(row.id).then((response) => {
        //this.distrbutionInfo = response.data;
        this.customerName = response.data.customerName;
        this.openDistrbution = true;
        this.title = "分配";

        this.distrbutionInfo = Object.assign({}, response.data);

        for (var i = 0; i < this.accountings.length; i++) {
          if (
            this.accountings[i].userId !=
            response.data.bookkeepingAccountingUserId
          ) {
            this.distrbutionInfo.bookkeepingAccountingUserId = undefined;
          } else {
            this.$set(
              this.distrbutionInfo,
              "bookkeepingAccountingUserId",
              response.data.bookkeepingAccountingUserId
            );
            break;
          }
        }

        for (var i = 0; i < this.accountManagers.length; i++) {
          if (
            this.accountManagers[i].userId !=
            response.data.managerAccountingUserId
          ) {
            this.distrbutionInfo.managerAccountingUserId = undefined;
          } else {
            this.$set(
              this.distrbutionInfo,
              "managerAccountingUserId",
              response.data.managerAccountingUserId
            );
            break;
          }
        }

        for (var i = 0; i < this.accountClerks.length; i++) {
          if (
            this.accountClerks[i].userId !=
            response.data.assistantAccountingUserId
          ) {
            this.distrbutionInfo.assistantAccountingUserId = undefined;
          } else {
            this.$set(
              this.distrbutionInfo,
              "assistantAccountingUserId",
              response.data.assistantAccountingUserId
            );
            break;
          }
        }
      });
    },

    // 分配信息提交
    submitDistruteForm: function () {
      this.$refs["distrbutionInfo"].validate((valid) => {
        if (valid) {
          addDistrution(this.distrbutionInfo).then((response) => {
            if (response.code === 200) {
              this.msgSuccess("分配成功");
              this.openDistrbution = false;
              this.getList();
            }
          });
        }
      });
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
          return exportCustomer(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        })
        .catch(function () {});
    },

    // 处理服务类型数据
    spliceArray: function (templatearr, arr) {
      let array = [];
      if (arr.length > 0) {
        arr.map((item) => {
          templatearr.map((itemm) => {
            itemm.serviceTypeId == item ? array.push(itemm) : "";
          });
        });
        return Array.from(new Set(array));
      }
    },
    // 拆分用户选中的数据类型
    spliteArray: function (arr) {
      let array = [];
      if (arr.length > 0) {
        arr.map((item) => {
          array.push(item.serviceTypeId);
        });
        return Array.from(new Set(array));
      }
    },
    // 服务类型选择代账服务时，记账会计显示
    selectAccounting(selVal) {
      let values = selVal;
      if (values.indexOf(1) > -1) {
        this.isAccounting = true;
        //this.form.bookkeepingAccountingUserId = undefined;
      } else {
        this.isAccounting = false;
        this.form.bookkeepingAccountingUserId = undefined;
        //console.log(this.form); 
      }
    },
    // 导入按钮操作
    handleImport() {
      this.upload.title = "用户导入";
      this.upload.open = true;
    },
    // 下载模板操作
    importTemplate() {
      importTemplate().then((response) => {
        this.download(response.msg);
      });
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
  },
  watch: {
    
  },
};
</script>
<style lang='scss' scoped>
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

/deep/.el-switch {
  top: -2px;
}
</style>
