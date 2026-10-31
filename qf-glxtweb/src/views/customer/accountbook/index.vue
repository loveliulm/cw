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
      <el-form-item label="纳税性质" prop="vatType">
        <el-select
          v-model="queryParams.vatType"
          clearable
          placeholder="请选择纳税性质"
        >
          <el-option
            v-for="dict in vatTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="parseFloat(dict.dictValue)"
          ></el-option>
        </el-select>
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
            :value="parseFloat(dict.userId)"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="建账时间" prop="createPeriod">
        <el-date-picker
          clearable
          size="small"
          style="width: 200px"
          v-model="queryParams.createPeriod"
          type="month"
          format="yyyy-MM"
          value-format="yyyy-MM"
          placeholder="选择建账时间"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="当前账期" prop="currentPeriod">
        <el-date-picker
          clearable
          size="small"
          style="width: 200px"
          v-model="queryParams.currentPeriod"
          type="month"
          format="yyyy-MM"
          value-format="yyyy-MM"
          placeholder="选择当前账期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="服务状态" prop="serverStatus">
        <el-select
          v-model="queryParams.serverStatus"
          clearable
          placeholder="请选择服务状态"
        >
          <el-option
            v-for="dict in serverStatus"
            :key="dict.id"
            :label="dict.status"
            :value="dict.status"
          ></el-option>
        </el-select>
      </el-form-item>
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

    <!-- <el-row :gutter="10"
            class="mb8">
      <el-col :span="1.5">
        <el-button type="primary"
                   icon="el-icon-plus"
                   size="mini"
                   @click="handleAdd"
                   v-hasPermi="['system:book:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success"
                   icon="el-icon-edit"
                   size="mini"
                   :disabled="single"
                   @click="handleUpdate"
                   v-hasPermi="['system:book:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger"
                   icon="el-icon-delete"
                   size="mini"
                   :disabled="multiple"
                   @click="handleDelete"
                   v-hasPermi="['system:book:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning"
                   icon="el-icon-download"
                   size="mini"
                   @click="handleExport"
                   v-hasPermi="['system:book:export']">导出</el-button>
      </el-col>
    </el-row>-->
     <div style="display:flex;flex-flow:row-reverse;justify-content:space-between;">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            size="mini"
            :disabled="single"
            v-hasPermi="['customer:book:stopService']"
            @click="stopService()"
          >停止服务</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="primary"
            size="mini"
            :disabled="single"
            v-hasPermi="['customer:book:restoreService']"
            @click="resumeService()"
          >恢复服务</el-button>
        </el-col>
      </el-row>
    </div>
    <el-table
      v-loading="loading"
      :data="bookList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="客户名称" align="center" prop="customerName" />
      <el-table-column
        label="纳税性质"
        align="center"
        prop="vatType"
        :formatter="vatTypeFormat"
      />
      <el-table-column
        label="记账会计"
        align="center"
        prop="bookkeepingAccounting"
      />
      <el-table-column
        label="建账时间"
        align="center"
        prop="createPeriod"
        width="180"
      >
        <template slot-scope="scope">
          <span v-show="scope.row.createPeriod == null">暂未建账</span>
          <span v-show="scope.row.createPeriod != null">{{
            parseTime(scope.row.createPeriod, "{y}-{m}")
          }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="当前账期"
        align="center"
        prop="currentPeriod"
        width="180"
      >
        <template slot-scope="scope">
          <span v-show="scope.row.currentPeriod == null">暂未建账</span>
          <span v-show="scope.row.currentPeriod != null">{{
            parseTime(scope.row.currentPeriod, "{y}-{m}")
          }}</span>
        </template>
      </el-table-column>
      <el-table-column label="服务状态" align="center" prop="serverStatus">
        <template slot-scope="scope">
          <span
            :class="
              scope.row.serverStatus === '正常服务'
                ? 'text-success'
                : 'text-danger'
            "
            >{{ scope.row.serverStatus }}</span
          >
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <!--<el-button size="mini"
                     type="text"
                     icon="el-icon-edit"
                     @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:book:edit']">修改</el-button>
          <el-button size="mini"
                     type="text"
                     icon="el-icon-delete"
                     @click="handleDelete(scope.row)"
          v-hasPermi="['system:book:remove']">删除</el-button>-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            v-if="scope.row.bookCount == 0 && scope.row.serverStatus === '正常服务'"
            @click="addBooks(scope.row)"
            >新建账套</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit-outline"
            v-if="scope.row.bookCount != 0 && scope.row.serverStatus === '正常服务'"
            @click="goto(scope.row)"
            >进入账套</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-date"
            v-if="scope.row.serverStatus === '正常服务'"
            @click="taxReturn(scope.row)"
            >报税</el-button
          >
          <span v-if="scope.row.serverStatus === '停止服务'">--</span>
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

    <!-- 添加或修改代账服务对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="客户id" prop="customerId">
          <el-input v-model="form.customerId" placeholder="请输入客户id" />
        </el-form-item>
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="form.customerName" placeholder="请输入客户名称" />
        </el-form-item>
        <el-form-item
          label="账套个数：0：未建账套，大于0：已建账套"
          prop="bookCount"
        >
          <el-input
            v-model="form.bookCount"
            placeholder="请输入账套个数：0：未建账套，大于0：已建账套"
          />
        </el-form-item>
        <el-form-item label="纳税性质">
          <el-select v-model="form.vatType" placeholder="请选择纳税性质">
            <el-option label="请选择字典生成" value />
          </el-select>
        </el-form-item>
        <el-form-item label="主管会计id" prop="managerAccountingUserId">
          <el-input
            v-model="form.managerAccountingUserId"
            placeholder="请输入主管会计id"
          />
        </el-form-item>
        <el-form-item label="记账会计id" prop="bookkeepingAccountingUserId">
          <el-input
            v-model="form.bookkeepingAccountingUserId"
            placeholder="请输入记账会计id"
          />
        </el-form-item>
        <el-form-item label="会计助理id" prop="assistantAccountingUserId">
          <el-input
            v-model="form.assistantAccountingUserId"
            placeholder="请输入会计助理id"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="凭证数" prop="voucherCount">
          <el-input v-model="form.voucherCount" placeholder="请输入凭证数" />
        </el-form-item>
        <el-form-item label="建账时间" prop="createPeriod">
          <el-date-picker
            clearable
            size="small"
            style="width: 200px"
            v-model="form.createPeriod"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择建账时间"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="当前账期" prop="currentPeriod">
          <el-date-picker
            clearable
            size="small"
            style="width: 200px"
            v-model="form.currentPeriod"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择当前账期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="服务状态">
          <el-radio-group v-model="form.serverStatus">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--新建账套-->
    <el-dialog
      :title="title"
      :visible.sync="isAddBook"
      width="500px"
      append-to-body
    >
      <el-form ref="book" :model="book" :rules="bookrules" label-width="80px">
        <el-form-item label="账套名称" prop="customerName">
          <el-input
            v-model="book.customerName"
            readonly
            placeholder="请输入账套名称"
          />
        </el-form-item>
        <el-form-item label="会计制度" prop="accountTemplateId">
          <el-select
            v-model="book.accountTemplateId"
            clearable
            placeholder="请选择会计制度"
          >
            <el-option
              v-for="dict in accountSystem"
              :key="dict.accountTemplateId"
              :label="dict.name"
              :value="parseFloat(dict.accountTemplateId)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="建账时间" prop="createPeriod">
          <el-date-picker
            clearable
            size="small"
            style="width: 200px"
            v-model="book.createPeriod"
            type="month"
            format="yyyy-MM"
            value-format="yyyy-MM"
            placeholder="选择建账时间"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="记账会计" prop="bookkeepingAccounting">
          <el-select
            v-model="book.bookkeepingAccountingUserId"
            clearable
            filterable
            :filter-method="dataFilterAccount"
            placeholder="请选择记账会计"
          >
            <el-option
              v-for="dict in accountings"
              :key="dict.userId"
              :label="dict.userName"
              :value="parseFloat(dict.userId)"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :disabled="accDisabled" type="primary" @click="submitBook"
          >确 定</el-button
        >
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 报税添加 -->
    <el-dialog
      :title="title"
      :visible.sync="taxReturns"
      width="700px"
      append-to-body
    >
      <p class="taxReturns-name">{{ taxName }}</p>
      <div class="taxReturns-time">
        <div class="taxReturns-time-top">
          <p>报税年份</p>
          <el-date-picker
            :editable="false"
            value-format="yyyy"
            v-model="taxValue"
            type="year"
            style="margin: 5.5px 15px"
            placeholder="选择年"
            @change="newYear()"
          ></el-date-picker>
        </div>
        <div class="taxReturns-time-bottom">
          <p>报税账期</p>
          <div class="taxReturns-time-bottom-right">
            <div class="taxReturns-time-bottom-right-top">
              <p
                @click="chooSe(ind)"
                v-for="(item, ind) in monthList"
                :key="ind"
                :class="xuan == ind ? 'ppp' : '' || item == 1 ? 'pp' : ''"
              >
                {{ ind + 1 }}
              </p>
            </div>
            <div class="taxReturns-time-bottom-right-bottom">
              <span style="background: #c6cacf"></span>
              <p>已报税</p>
              <span style="background: #62ca6c"></span>
              <p>选择</p>
              <span></span>
              <p>未报税</p>
            </div>
          </div>
        </div>
      </div>
      <p class="taxReturns-names" style="margin-bottom: 20px">申报数据填写</p>
      <el-form
        :model="queryParamss"
        :rules="netfile"
        :inline="true"
        label-width="110px"
      >
        <el-form-item label="增值税" prop="taxVatChange">
          <el-input
            v-model="queryParamss.taxVatChange"
            placeholder="请输入增值税"
            clearable
            size="small"
            @input="axVatChanges()"
          />
        </el-form-item>
        <el-form-item label="城市维护建设费" prop="taxBuilding">
          <el-input
            v-model="queryParamss.taxBuilding"
            placeholder="请输入城市维护建设费"
            clearable
            size="small"
            disabled
          />
        </el-form-item>
        <el-form-item label="教育附加税" prop="taxEducation">
          <el-input
            v-model="queryParamss.taxEducation"
            placeholder="请输入教育附加税"
            clearable
            size="small"
            disabled
          />
        </el-form-item>
        <el-form-item label="企业所得税" prop="taxCorporate">
          <el-input
            v-model="queryParamss.taxCorporate"
            placeholder="请输入企业所得税"
            clearable
            size="small"
            @input="axVatChangess()"
          />
        </el-form-item>
        <el-form-item label="个人所得税" prop="taxPersonal">
          <el-input
            v-model="queryParamss.taxPersonal"
            placeholder="请输入个人所得税"
            clearable
            size="small"
            @input="axVatChangess()"
          />
        </el-form-item>
        <el-form-item label="印花税" prop="taxStamp">
          <el-input
            v-model="queryParamss.taxStamp"
            placeholder="请输入印花税"
            clearable
            @keyup.native="oninput()"
            @input="axVatChangess()"
            size="small"
          />
        </el-form-item>
        <el-form-item label="其他" prop="taxOther">
          <el-input
            v-model="queryParamss.taxOther"
            placeholder="请输入其他"
            clearable
            @input="axVatChangess()"
            size="small"
          />
        </el-form-item>
        <el-form-item label="合计" prop="total">
          <el-input
            v-model="queryParamss.total"
            placeholder="请输入合计"
            disabled
            clearable
            size="small"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button  v-hasPermi="['customer:declaration:edit']" v-show="taxs" type="primary" @click="submitYears"
          >重新报税</el-button
        >
        <el-button  v-hasPermi="['customer:declaration:add']"   v-show="tax" type="primary" @click="submitYear"
          >报税</el-button
        >
        <el-button @click="submitUnhide">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listBook,
  getBook,
  delBook,
  addBook,
  updateBook,
  exportBook,
  listAccountSystem,
  listtaxValue,
  taxReturn,
  acquireDetails,
  modiFication,
  stopService,
  resumeService
} from "@/api/customer/accountbook";
import { getPersonList } from "@/api/customer/customer";
import Url from "@/utils/url";

export default {
  name: "AccountBook",
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
      // 代账服务表格数据
      bookList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        customerId: null,
        customerName: null,
        bookCount: null,
        vatType: null,
        managerAccountingUserId: null,
        bookkeepingAccountingUserId: null,
        assistantAccountingUserId: null,
        status: null,
        voucherCount: null,
        createPeriod: null,
        currentPeriod: null,
        serverStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        /*customerId: [
          { required: true, message: "客户id不能为空", trigger: "blur" }
        ],*/
        customerName: [
          { required: true, message: "客户名称不能为空", trigger: "blur" },
        ],
        /*bookCount: [
          { required: true, message: "账套个数：0：未建账套，大于0：已建账套不能为空", trigger: "blur" }
        ],*/
        vatType: [
          { required: true, message: "纳税性质不能为空", trigger: "blur" },
        ],
      },
      // 记账会计
      accountings: [],
      // 拷贝记账会计
      copyAccounts: [],
      // 纳税性质
      vatTypeOptions: [],
      // 服务类型
      serverStatus: [
        { id: 1, status: "正常服务" },
        { id: 2, status: "停止服务" },
      ],
      // 新建账套弹出框是否弹出
      isAddBook: false,
      // 账套
      book: {},
      // 账套表单校验
      bookrules: {
        customerName: [
          { required: true, message: "账套名称不能为空", trigger: "blur" },
        ],
        // 会计科目
        accountTemplateId: [
          { required: true, message: "请选择会计制度", trigger: "blur" },
        ],
        createPeriod: [
          { required: true, message: "建帐时间不能为空", trigger: "blur" },
        ],
        bookkeepingAccounting: [
          { required: true, message: "请选择记账会计", trigger: "blur" },
        ],
      },
      url: "http://localhost:8090/accounting/",
      // url: "http://cwgl.qwh365.com/accounting/",

      // 会计制度
      accountSystem: [],
      // 创建账套按钮防抖
      accDisabled: false,
      //报税弹窗
      taxReturns: false,
      //报税年月
      taxValue: this.getNowTime(),
      //报税姓名
      taxName: "",
      //报税数据
      queryParamss: {
        customerId: "",
        taxVatChange: "",
        taxBuilding: "",
        taxEducation: "",
        taxCorporate: "",
        taxPersonal: "",
        taxStamp: "",
        taxOther: "",
        total: "",
        year: "",
        month: "",
      },
      //报税账期
      monthList: [],
      //报税账期点击选择
      xuan: undefined,
      //报税数据表单验证
      netfile: {
        taxVatChange: [
          {
            pattern: /^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/,
            //required: true,
            message: "请输入正确金额,金额最小到分",
            trigger: "blur",
          },
        ],
        taxCorporate: [
          {
            pattern: /^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/,
            //required: true,
            message: "请输入正确金额,金额最小到分",
            trigger: "blur",
          },
        ],
        taxPersonal: [
          {
            pattern: /^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/,
            //required: true,
            message: "请输入正确金额,金额最小到分",
            trigger: "blur",
          },
        ],
        taxStamp: [
          {
            pattern: /^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/,
            //required: true,
            message: "请输入正确金额,金额最小到分",
            trigger: "blur",
          },
        ],
        taxOther: [
          {
            pattern: /^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/,
            //required: true,
            message: "请输入正确金额,金额最小到分",
            trigger: "blur",
          },
        ],
      },
      //报税按钮显示隐藏
      tax: true,
      //重新报税按钮显示隐藏
      taxs: false,
      //报税详情唯一id
      taxid: "",
    };
  },
  created() {
    // 获取列表数据
    this.getList();
    //字典获取纳税性质
    this.getDicts("vat_type").then((response) => {
      this.vatTypeOptions = response.data;
    });
    // 获取记账会计信息
    this.getAccounting();
    // 获取会计制度
    this.getListAccountSystem();
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
    resumeService(){
      resumeService(this.ids).then(res=>{
        if(res.code == '200'){
          this.msgSuccess("恢复服务成功");
          this.getList();
        }
      })
    },
    // 查询代账服务列表
    getList() {
      this.loading = true;
      listBook(this.queryParams).then((response) => {
        this.bookList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 纳税性质字典翻译
    vatTypeFormat(row, column) {
      return this.selectDictLabel(this.vatTypeOptions, row.vatType);
    },
    //
    // 获取记账会计列表
    getAccounting() {
      getPersonList({ roleKey: "accounting" }).then((response) => {
        this.accountings = response.data;
        this.copyAccounts = response.data;
      });
    },
    // 获取会计制度
    getListAccountSystem() {
      listAccountSystem().then((res) => {
        this.accountSystem = res.rows;
      });
    },
    // 取消按钮
    cancel() {
      this.isAddBook = false;
      this.accDisabled = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        bookId: undefined,
        customerId: undefined,
        customerName: undefined,
        bookCount: undefined,
        vatType: undefined,
        managerAccountingUserId: undefined,
        bookkeepingAccountingUserId: undefined,
        assistantAccountingUserId: undefined,
        status: "0",
        voucherCount: undefined,
        createPeriod: undefined,
        currentPeriod: undefined,
        serverStatus: "0",
        createTime: undefined,
        updateTime: undefined,
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
      this.ids = selection.map((item) => item.bookId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加代账服务";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const bookId = row.bookId || this.ids;
      getBook(bookId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改代账服务";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.bookId != undefined) {
            updateBook(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addBook(this.form).then((response) => {
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
      const bookIds = row.bookId || this.ids;
      this.$confirm(
        '是否确认删除代账服务编号为"' + bookIds + '"的数据项?',
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delBook(bookIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
    // 新建账套
    addBooks(row) {
      this.getAccounting();
      this.book = {};
      this.book = row;
      this.isAddBook = true;
      this.title = "新建账套";
      for (var i = 0; i < this.accountings.length; i++) {
        if (this.accountings[i].userId != row.bookkeepingAccountingUserId) {
          this.book.bookkeepingAccountingUserId = undefined;
        } else {
          this.$set(
            this.book,
            "bookkeepingAccountingUserId",
            row.bookkeepingAccountingUserId
          );
          break;
        }
      }
    },
    // 搜索选择记账会计
    dataFilterAccount(val) {
      if (val) {
        this.accountings = this.copyAccounts.filter((item) => {
          if (
            !!~item.userName.indexOf(val) ||
            !!~item.userName.toUpperCase().indexOf(val.toUpperCase())
          ) {
            return true;
          } else {
            return false;
          }
        });
      } else {
        //val为空时，还原数组
        this.accountings = this.copyAccounts;
      }
    },
    submitBook() {
      this.$refs["book"].validate((valid) => {
        if (valid) {
          this.accDisabled = true;
          addBook(this.book).then((response) => {
            if (response.code === 200) {
              this.msgSuccess("新建账套成功");
              this.isAddBook = false;
              this.getList();
              this.accDisabled = false;
            } else {
              this.accDisabled = false;
            }
          });
        }
      });
    },
    // 导出按钮操作
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有代账服务数据项?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportBook(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        })
        .catch(function () {});
    },
    // 进入账套
    goto (row) {
      let URL = new Url();
      let url = this.url + row.bookId ;
      let newUrl = URL.getUrl(url);
      window.open(url);


    },
    //报税
    taxReturn(row) {
      // console.log(row);
      this.taxReturns = true;
      this.title = "报税";
      this.taxName = row.customerName;
      this.xuan = undefined;
      this._listtaxValue(row.customerId);
      this.empty();
      this.monthList = [];
      this.tax = true;
      this.taxs = false;
    },
    //获取当前年进行赋值
    getNowTime() {
      var now = new Date();
      var year = now.getFullYear(); //得到年份
      //  var month = now.getMonth(); //得到月份
      //  var date = now.getDate(); //得到日期
      //  var hour =" 00:00:00"; //默认时分秒 如果传给后台的格式为年月日时分秒，就需要加这个，如若不需要，此行可忽略
      //  month = month + 1;
      //  month = month.toString().padStart(2, "0");
      //  date = date.toString().padStart(2, "0");
      var defaultDate = `${year}`;
      return defaultDate;
      this.$set(this.info, "stockDate", defaultDate);
    },
    //报税年份请求
    _listtaxValue: function (row) {
      this.queryParamss.customerId = row;
      listtaxValue({
        customerId: row,
        year: this.taxValue,
      }).then((response) => {
        this.monthList = response.data.array;
        //console.log(this.monthList);
      });
    },
    //增值税 城市维护建设费 教育附加税
    axVatChanges() {
      this.queryParamss.taxBuilding =
        this.vueFundain(this.queryParamss.taxVatChange, 0.07) || "";
      this.queryParamss.taxEducation =
        this.vueFundain(this.queryParamss.taxVatChange, 0.03) || "";
      const money =
        Number(parseFloat(this.queryParamss.taxVatChange) || 0) +
        Number(this.queryParamss.taxBuilding) +
        Number(this.queryParamss.taxEducation) +
        Number(parseFloat(this.queryParamss.taxCorporate) || 0) +
        Number(parseFloat(this.queryParamss.taxOther) || 0) +
        Number(parseFloat(this.queryParamss.taxPersonal) || 0) +
        Number(parseFloat(this.queryParamss.taxStamp) || 0);
      // console.log(money);
      if (money == 0) {
        this.queryParamss.total = "";
      } else {
        // let tempVal = parseFloat(money).toFixed(3);
        // this.queryParamss.total = tempVal.substring(0, tempVal.length - 1);
        //console.log(money)
        this.queryParamss.total = money.toFixed(2);
      }
      if (this.queryParamss.taxVatChange == "") {
        this.queryParamss.total = "";
      }
    },
    axVatChangess() {
      const moneys =
        Number(parseFloat(this.queryParamss.taxVatChange) || 0) +
        Number(this.queryParamss.taxBuilding) +
        Number(this.queryParamss.taxEducation) +
        Number(parseFloat(this.queryParamss.taxCorporate) || 0) +
        Number(parseFloat(this.queryParamss.taxOther) || 0) +
        Number(parseFloat(this.queryParamss.taxPersonal) || 0) +
        Number(parseFloat(this.queryParamss.taxStamp) || 0);
      if (moneys == 0) {
        this.queryParamss.total = "";
      } else {
        // console.log(moneys);
        //let tempVal = parseFloat(moneys).toFixed(2);
        //this.queryParamss.total = tempVal.substring(0, tempVal.length - 1);
        //console.log(parseFloat(this.queryParamss.taxPersonal))

        this.queryParamss.total = moneys.toFixed(2);
      }
    },
    //Vue浮点型乘法
    vueFundain(arg1, arg2) {
      var m = 0;
      var s1 = arg1.toString();
      var s2 = arg2.toString();
      try {
        m += s1.split(".")[1].length;
      } catch (e) {}
      try {
        m += s2.split(".")[1].length;
      } catch (e) {}

      return (
        (Number(s1.replace(".", "")) * Number(s2.replace(".", ""))) /
        Math.pow(10, m)
      );
    },
    //报账日期选着
    chooSe(ind) {
      this.xuan = ind;
      this.queryParamss.month = ind + 1;
      const acquire = {
        month: this.queryParamss.month,
        customerId: this.queryParamss.customerId,
        year: this.taxValue,
      };
      if (this.monthList[ind] == 1) {
        this.tax = false;
        this.taxs = true;
        acquireDetails(acquire).then((response) => {
          this.queryParamss.taxVatChange = response.data.taxVatChange;
          this.queryParamss.taxBuilding = response.data.taxBuilding;
          this.queryParamss.taxEducation = response.data.taxEducation;
          this.queryParamss.taxCorporate = response.data.taxCorporate;
          this.queryParamss.taxPersonal = response.data.taxPersonal;
          this.queryParamss.taxStamp = response.data.taxStamp;
          this.queryParamss.taxOther = response.data.taxOther;
          this.queryParamss.total = response.data.total;
          this.taxid = response.data.id;
        });
      } else {
        this.tax = true;
        this.taxs = false;
        this.empty();
      }
    },
    //报税年选着
    newYear() {
      listtaxValue({
        customerId: this.queryParamss.customerId,
        year: this.taxValue,
      }).then((response) => {
        this.monthList = response.data.array;
        //console.log(this.monthList);
      });
    },
    //新增报税提交
    submitYear() {
      this.axVatChangess();
      this.queryParamss.year = this.taxValue;
      if (this.queryParamss.total > 0) {
        taxReturn(this.queryParamss).then((response) => {
          this.msgSuccess("报税成功");
          this.newYear();
          this.xuan = undefined;
          this.empty();
        });
      } else {
        this.msgSuccess("报税金额不能小于0");
      }
    },
    //报税数据清空
    empty() {
      this.queryParamss.taxVatChange = "";
      this.queryParamss.taxBuilding = "";
      this.queryParamss.taxEducation = "";
      this.queryParamss.taxCorporate = "";
      this.queryParamss.taxPersonal = "";
      this.queryParamss.taxStamp = "";
      this.queryParamss.taxOther = "";
      this.queryParamss.total = "";
    },
    //重新报税
    submitYears() {
      this.axVatChangess();
      this.queryParamss.year = this.taxValue;
      this.queryParamss.id = this.taxid;
      if (this.queryParamss.total > 0) {
        modiFication(this.queryParamss).then((response) => {
          this.msgSuccess("报税成功");
          this.newYear();
          //this.xuan = undefined;
          //this.empty();
        });
      } else {
        this.msgSuccess("报税金额不能小于0");
      }
    },
    //报税弹窗隐藏
    submitUnhide() {
      this.taxReturns = false;
      this.monthList = [];
      this.empty();
    },
    //印花税只能输小数点一位判断
    oninput() {
      var str = this.queryParamss.taxStamp;
      var len1 = str.substr(0, 1);
      var len2 = str.substr(1, 1);
      //如果第一位是0，第二位不是点，就用数字把点替换掉
      if (str.length > 1 && len1 == 0 && len2 != ".") {
        str = str.substr(1, 1);
      }
      //console.log(1)
      //第一位不能是.
      if (len1 == ".") {
        str = "";
        //console.log(2)
      }
      //console.log(3)
      //限制只能输入一个小数点
      if (str.indexOf(".") != -1) {
        var str_ = str.substr(str.indexOf(".") + 1);
        if (str_.indexOf(".") != -1) {
          str = str.substr(0, str.indexOf(".") + str_.indexOf(".") + 1);
          //console.log(4)
        }
      }
      //console.log(5)
      //正则替换
      str = str.replace(/[^\d^\.]+/g, ""); // 保留数字和小数点
      //console.log(str);
      str = str.replace(/\.\d\d$/, ""); // 小数点后只能输一位
      // console.log(str);
      this.queryParamss.taxStamp = str;
      this.axVatChangess();
      return;
    },
  },
  watch: {},
};
</script>
<style lang="scss" scoped>
.taxReturns-name {
  margin: 0;
  font-size: 18px;
  border-bottom: 1px solid #dce1e7;
  height: 24px;
  font-weight: bold;
  letter-spacing: 2px;
}
.taxReturns-names {
  margin: 0;
  font-size: 17px;
  border-bottom: 1px solid #dce1e7;
  height: 24px;

  letter-spacing: 1px;
}
.taxReturns-time {
  width: 100%;
  height: 100%;
  margin-top: 15px;
  margin-bottom: 20px;
  background: #f8f8f8;
  cursor: pointer;
  .taxReturns-time-top {
    width: 100%;
    display: flex;
    p {
      margin: 0;
      width: 120px;
      height: 48px;
      line-height: 48px;
      text-align: right;
      padding-right: 10px;
      background: #f1f2f6;
    }
  }
  .taxReturns-time-bottom {
    width: 100%;
    display: flex;
    p {
      margin: 0;
      width: 120px;
      height: 83px;
      line-height: 49px;
      text-align: right;
      padding-right: 10px;
      background: #f1f2f6;
      margin-right: 15px;
    }
    .taxReturns-time-bottom-right {
      height: 83px;
      .taxReturns-time-bottom-right-top {
        width: 100%;
        display: flex;
        margin-top: 10px;
        p {
          margin: 0;
          width: 25px;
          height: 25px;
          background: #fff;
          text-align: center;
          line-height: 25px;
          padding: 0;
          margin-right: 10px;
          font-size: 14px;
          color: #838488;
        }
        .pp {
          background: rgb(198, 202, 207);
          color: white;
        }
        .ppp {
          background: #62ca6c !important;
          color: white !important;
        }
      }
      .taxReturns-time-bottom-right-bottom {
        width: 100%;
        display: flex;
        margin-top: 12px;
        p {
          margin: 0;
          width: 63px;
          height: 25px;
          background: #f8f8f8;
          text-align: center;
          line-height: 25px;
          padding: 0;
          margin-right: 10px;
          font-size: 14px;
          color: #828591;
        }
        span {
          margin: 0;
          width: 23px;
          height: 23px;
          background: #fff;
          padding: 0;
          margin-right: 10px;
        }
      }
    }
  }
}

/deep/ .el-dialog__body {
  padding: 10px 20px;
}
/deep/ .el-input__prefix {
  height: 35px !important;
}
/deep/ .el-date-editor {
  //margin: 5.5px 15px;
}
</style>
