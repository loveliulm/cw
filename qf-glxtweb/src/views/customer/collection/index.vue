<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="收款人" prop="customerName">
        <el-input
          v-model="queryParams.customerName"
          placeholder="请输入收款人"
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

      <!-- <el-form-item label="已收金额" prop="amount">
        <el-input
          v-model="queryParams.amount"
          placeholder="请输入累计已收金额"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <!-- <el-form-item label="欠款金额" prop="debt">
        <el-input
          v-model="queryParams.debt"
          placeholder="请输入欠款金额"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="是否完结" prop="isEnd">
        <!-- <el-input
          v-model="queryParams.isEnd"
          placeholder="请输入是否完结，0：否 1：是"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />-->
        <el-select v-model="queryParams.isEnd" placeholder="请输入是否完结" clearable size="small">
          <el-option
            v-for="item in  finished"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="短信发送状态0：未发送1：已发送" prop="isNotice">
        <el-input
          v-model="queryParams.isNotice"
          placeholder="请输入短信发送状态0：未发送1：已发送"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->

      <!-- <el-form-item label="收款日期" prop="lastPayDate">
        <el-date-picker
          clearable
          size="small"
          style="width: 200px"
          v-model="queryParams.lastPayDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择最后收款日期"
        ></el-date-picker>
      </el-form-item>-->
      <!-- <el-form-item label="服务收款" prop="serviceClassId">
       
        <el-select
          v-model="queryParams.serviceClassId"
          placeholder="请输入服务收款"
          clearable
          size="small"
        >
          <el-option
            v-for="item in  payee"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>-->

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['customer:collection:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['customer:collection:edit']"
        >修改</el-button>
      </el-col>-->
      <!-- <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['customer:collection:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['customer:collection:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="collectionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="合同编号" align="center" prop="contractNo" />
      <el-table-column label="收款人" align="center" prop="customerName" />
      <el-table-column label="合同金额" align="center" prop="total" />
      <el-table-column label="累计已收金额" align="center" prop="amount" />
      <el-table-column label="欠款金额" align="center" prop="debt" />
      <el-table-column label="是否完结" align="center" prop="isEnd" :key="1">
        <template slot-scope="scope">
          <span style="color:#1682e6" v-if="scope.row.isEnd == '1'">是</span>
          <span style="color:#ff4949" v-if="scope.row.isEnd == '0'">否</span>
        </template>
      </el-table-column>-->
      <!-- <el-table-column label="预警状态" align="center" prop="isWarning">
        <template slot-scope="scope">
          <span style="color:#1682e6" v-if="scope.row.isWarning == '正常'">正常</span>
          <span style="color:#ff4949" v-if="scope.row.isWarning == '已经欠费'">已经欠费</span>
            <span style="color:#ff4949" v-if="scope.row.isWarning == '即将到期'">即将到期</span>
        </template>
      </el-table-column>
      <el-table-column label="最后收款日期" align="center" prop="lastPayDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastPayDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>-->
      <el-table-column label="服务收款" align="center" prop="serviceClassId" :key="3">
        <template slot-scope="scope">
          <span v-if="scope.row.serviceClassId == '1'">代账服务收款</span>
          <span v-if="scope.row.serviceClassId == '2'">工商服务收款</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="备注" align="center" prop="remark" /> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            type="success"
            style="width: 60px;color: #fff;"
            size="mini"
            @click="Collection(scope.row)"
          >收款</el-button>

          <!-- <el-button
            type="success"
            style="width: 60px;color: #fff;"
            size="mini"
            @click="sunn()"
          >收款</el-button>-->
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

    <!-- 添加或修改收款对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="750px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="二级部门" prop="deptId">
          <el-input v-model="form.deptId" placeholder="请输入二级部门" />
        </el-form-item>
        <el-form-item label="合同id" prop="contractId">
          <el-input v-model="form.contractId" placeholder="请输入合同id" />
        </el-form-item>
        <el-form-item label="合同金额" prop="total">
          <el-input v-model="form.total" placeholder="请输入合同金额" />
        </el-form-item>
        <el-form-item label="累计已收金额" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入累计已收金额" />
        </el-form-item>
        <el-form-item label="欠款金额" prop="debt">
          <el-input v-model="form.debt" placeholder="请输入欠款金额" />
        </el-form-item>
        <el-form-item label="是否完结" prop="isEnd">
          <el-select v-model="form.isEnd" placeholder="请输入是否完结" clearable size="small">
            <el-option
              v-for="item in  finished"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="短信发送状态0：未发送1：已发送" prop="isNotice">
          <el-input v-model="form.isNotice" placeholder="请输入短信发送状态0：未发送1：已发送" />
        </el-form-item>
        <el-form-item label="最后收款日期" prop="lastPayDate">
          <el-date-picker
            clearable
            size="small"
            style="width: 200px"
            v-model="form.lastPayDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择最后收款日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="1：代账服务收款2：工商服务收款" prop="serviceClassId">
          <el-input v-model="form.serviceClassId" placeholder="请输入1：代账服务收款2：工商服务收款" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="titlee" :visible.sync="gathering" width="880px" append-to-body>
      <el-form :model="payeeList" ref="payeeList" :inline="true" label-width="68px">
        <el-form-item label="合同编号" prop="contractNo">
          <el-input
            v-model="payeeList.contractNo"
            placeholder="请输入合同编号"
            clearable
            disabled 
            :readonly="true"
            size="small"
            style="width: 295px"
          />
        </el-form-item>
        <el-form-item label="客户名称" prop="customerName">
          <el-input
            v-model="payeeList.customerName"
            placeholder="请输入客户名称"
            clearable
            size="small"
            disabled 
            :readonly="true"
            style="width: 295px"
          />
        </el-form-item>
      </el-form>

      <el-form :model="moneyxinn" ref="moneyxinn" :inline="true" label-width="68px">
        <el-form-item label="开始日期" prop="lastPayDate">
          <el-date-picker
            clearable
            size="small"
            style="width: 295px"
            v-model="moneyxinn.contractStartDate"
            type="date"
            disabled 
            :readonly="true"
            value-format="yyyy-MM-dd"
            placeholder="选择开始收款日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="终止日期" prop="contractEndDate">
          <el-date-picker
            clearable
            size="small"
            style="width: 295px"
            v-model="moneyxinn.contractEndDate"
            type="date"
            disabled 
            :readonly="true"
            value-format="yyyy-MM-dd"
            placeholder="选择结束收款日期"
          ></el-date-picker>
        </el-form-item>
      </el-form>

      <el-form :model="moneyxin" ref="moneyxinn" :inline="true" label-width="68px">
        <el-form-item label="合同总额" prop="total">
          <el-input
            v-model="moneyxin.total"
            placeholder="请输入合同总额"
            clearable
            size="small"
            :readonly="true"
            style="width: 295px"
            disabled 
          >
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="累计支付" prop="amount">
          <el-input
            v-model="moneyxin.amount"
            placeholder="请输入累计支付"
            clearable
            size="small"
            :readonly="true"
            type="text"
            style="width: 295px"
            disabled 
          >
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="剩余欠款" prop="debt">
          <el-input
            v-model="moneyxin.debt"
            placeholder="请输入剩余欠款"
            clearable
            size="small"
            :readonly="true"
            style="width: 295px"
            disabled 
          >
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
      </el-form>

      <el-table :data="moneyData" height="140" border style="width: 100%">
        <el-table-column header-align="center" align="center" label="序号" width="60">
          <template slot-scope="scope">
            <span>{{ scope.$index+1}}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column header-align="center" align="center" prop="name" label="待收金额" width="80"></el-table-column> -->
        <el-table-column
          header-align="center"
          align="center"
          prop="collectionAmount"
          label="已收金额"
          width="133"
        ></el-table-column>
        <el-table-column
          header-align="center"
          align="center"
          prop="status"
          label="是否收款"
          width="130"
        ></el-table-column>
        <el-table-column
          header-align="center"
          align="center"
          prop="payment"
          label="收款方式"
          width="133"
        ></el-table-column>
        <el-table-column
          header-align="center"
          align="center"
          prop="startDate"
          label="收款日期"
          width="143"
        >
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.collectionDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column width="200" header-align="center" align="center" prop label="服务日期">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span> ----
            <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>-->
        <el-table-column width="240" header-align="center" align="center" prop label="操作">
          <template slot-scope="scope">
            <el-button type="success" size="mini" @click="examine(scope.row)">查看</el-button>
            <el-button
              v-if="scope.$index+1 == ff"
              type="primary"
              size="mini"
              @click="compile(scope.row)"
            >编辑</el-button>

            <el-button
              v-if="scope.$index+1 == ff"
              type="danger"
              size="mini"
              v-hasPermi="['customer:record:remove']"
              @click="remove(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <br />
      <br />
      <el-form
        :model="PayyeeList"
        :rules="ruless"
        ref="PayyeeList"
        :inline="true"
        label-width="85px"
      >
        <el-form-item label="收款金额" prop="collectionAmount">
          <el-input
            v-model="PayyeeList.collectionAmount"
            placeholder="请输入收款金额"
            size="small"
            style="width: 655px"
            type="number"
          >
            <template slot="append">元</template>
          </el-input>
        </el-form-item>

        <el-form-item label="付款方式" prop="payment">
          <el-select
            v-model="PayyeeList.payment"
            placeholder="请输入收款方式"
            clearable
            size="small"
            style="width: 655px"
          >
            <el-option
              v-for="item in  moneyList"
              :key="item.value"
              :label="item.payment"
              :value="item.payMent"
            ></el-option>
          </el-select>
        </el-form-item>
        <!-- 
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker
            clearable
            size="small"
            style="width: 276px"
            v-model="PayyeeList.startDate"
            type="date"
            :readonly="true"
            value-format="yyyy-MM-dd"
            placeholder="选择开始收款日期"
          >></el-date-picker>
        </el-form-item>
        <el-form-item label="终止日期" prop="endDate">
          <el-date-picker
            clearable
            size="small"
            style="width: 280px"
            v-model="PayyeeList.endDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择结束收款日期"
            :picker-options="pickerOptionsEnd"
          >></el-date-picker>
        </el-form-item>-->
        <el-form-item label="收款备注" prop="total">
          <el-input
            v-model="PayyeeList.remark"
            placeholder="请输入备注"
            clearable
            size="small"
            style="width: 655px"
          ></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer" v-if="footer">
        <el-button v-hasPermi="['customer:record:add']" type="primary" @click="Submit()">确定收款</el-button>
        <el-button v-hasPermi="['customer:record:add']"  @click="calloff()">确认收款并关闭</el-button>
      </div>
      <div slot="footer" class="dialog-footer" v-if="footers">
        <el-button v-hasPermi="['customer:record:edit']" type="primary" @click="Submits()">确定修改</el-button>
        <el-button v-hasPermi="['customer:record:edit']" @click="calloffs()">确认修改并关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listCollection,
  getCollection,
  delCollection,
  addCollection,
  updateCollection,
  exportCollection,
  paymentList,
  SearchList,
  removeDelete,
  detailAmend,
} from "@/api/customer/collection";

export default {
  name: "Collection",
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
      // 收款表格数据
      collectionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //收款遮罩成
      gathering: false,
      titlee: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        total: undefined,
        amount: undefined,
        debt: undefined,
        isEnd: undefined,
        isNotice: undefined,
        isWarning: undefined,
        lastPayDate: undefined,
        serviceClassId: 2,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deptId: [
          {
            required: true,
            message: "二级部门（代账公司id）不能为空",
            trigger: "blur",
          },
        ],
        contractId: [
          { required: true, message: "合同id不能为空", trigger: "blur" },
        ],
      },
      //是否完结
      finished: [
        {
          value: "1",
          label: "是",
        },
        {
          value: "0",
          label: "否",
        },
      ],
      //服务收款
      payee: [
        {
          value: "1",
          label: "代账服务收款",
        },
        {
          value: "2",
          label: "工商服务收款",
        },
      ],
      //选择收款数据
      payeeList: {},
      //收款添加数据
      PayyeeList: {
        collectionId: undefined, //收款人id
        collectionAmount: undefined, //收款金额
        payment: undefined, // 付款方式
        startDate: undefined, //开始日期
        endDate: undefined, //结束日期
        remark: undefined, //备注
        id: undefined, // 详情id
      },
      //收款方式
      moneyList: [
        {
          payment: "现金",
          payMent: "现金",
        },
        {
          payment: "微信",
          payMent: "微信",
        },
        {
          payment: "支付宝",
          payMent: "支付宝",
        },
        {
          payment: "银行卡",
          payMent: "银行卡",
        },
        // {
        //   payment: "银行转账(个人)",
        //   payMent: 4,
        // },
        // {
        //   payment: "其他",
        //   payMent: 5,
        // },
      ],
      //收款详细
      moneyData: [],
      //收款id
      moneyId: "",
      //
      moneyxin: {},
      //开始日期 结束日期
      moneyxinn: {},
      ruless: {
        collectionAmount: [ 
          {
            pattern:/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/,
             required: true,
            message: "请输入正确金额,金额最小到分，如:10.53",
            trigger: "blur",
           
            //transform: (value) => Number(value),
          },
        ],
        payment: [
          { required: true, message: "收款方式不能为空", trigger: "blur" },
        ],
        endDate: [
          { required: true, message: "终止日期不能为空", trigger: "blur" },
        ],
      },
      // pickerOptionsStart 与 pickerOptionsEnd 设置结束日期大于开始日期并且开始日期小于结束日期
      // pickerOptionsStart: {
      //   disabledDate: (time) => {
      //     if (this.PayyeeList.endDate) {
      //       return time.getTime() > new Date(this.PayyeeList.endDate).getTime();
      //     }
      //   },
      // },
      pickerOptionsEnd: {
        disabledDate: (time) => {
          if (this.moneyxinn.contractEndDate) {
            //return   time.getTime() > new Date(this.moneyxinn.contractEndDate).getTime() ;
            return (
              time.getTime() < new Date(this.PayyeeList.startDate).getTime() ||
              time.getTime() >
                new Date(this.moneyxinn.contractEndDate).getTime()
            );
          }
        },
      },
      // 收款记录删除判断
      ff: "",
      //底部确认按钮显示隐藏判断
      footer: true,
      footers: false,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询收款列表 */
    getList() {
      this.loading = true;
      listCollection(this.queryParams).then((response) => {
        this.collectionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        deptId: undefined,
        contractId: undefined,
        total: undefined,
        amount: undefined,
        debt: undefined,
        isEnd: undefined,
        isNotice: undefined,
        isWarning: undefined,
        lastPayDate: undefined,
        createTime: undefined,
        serviceClassId: undefined,
        remark: undefined,
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
      this.idss = selection.map((item) => item.customerName);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加收款";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getCollection(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改收款";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != undefined) {
            updateCollection(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addCollection(this.form).then((response) => {
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
      const idss = row.customerName || this.idss;
      this.$confirm('是否确认删除客户姓名为"' + idss + '"的数据项?', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delCollection(ids);
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
      this.$confirm("是否确认导出所有收款数据项?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportCollection(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        })
        .catch(function () {});
    },
    //收款
    sunn() {
      console.log(123456);
    },
    Collection(row) {
      this.footer = true;
      this.footers = false;
      this.gathering = true;
      this.titlee = "合同收款";
      this.payeeList = row;
      // console.log(row);
      this.PayyeeList.collectionId = row.id;
      this.searchList(row.id);
      this.moneyId = row.id;
      this.payyee();
      //console.log(this.PayyeeList);
    },
    //收款结束详情
    searchList(item) {
      SearchList(item).then((response) => {
        this.PayyeeList.startDate = response.data.beginTime;
        this.moneyData = response.data.TCollectionRecords;
        this.ff = response.data.TCollectionRecords.length;
        this.moneyxin = response.data.tCollection;
        this.moneyxinn = response.data.contract;
      });
    },
    //收款确认
    Submit() {
      this.$refs["PayyeeList"].validate((valid) => {
        if (valid) {
          paymentList(this.PayyeeList).then((response) => {
            if (response.code === 200) {
              this.msgSuccess("支付成功");
              this.payye();
              this.searchList(this.moneyId);
              this.PayyeeList.collectionId = this.moneyId;
              this.getList();
            }
          });
        } else {
          //this.msgSuccess("支付失败");
        }
      });
    },
    //收款确认取消
    calloff() {
      this.$refs["PayyeeList"].validate((valid) => {
        if (valid) {
          paymentList(this.PayyeeList).then((response) => {
            this.payye();
            this.searchList(this.moneyId);
            this.PayyeeList.collectionId = this.moneyId;
            this.getList();
            this.gathering = false;
          });
        } else {
          //this.msgSuccess("支付失败");
        }
      });
    },
    //收款表单重置
    payye() {
      this.PayyeeList = {
        collectionId: undefined, //收款人id
        collectionAmount: undefined, //收款金额
        payment: undefined, // 付款方式
        startDate: undefined, //开始日期
        endDate: undefined, //结束日期
        remark: undefined, //备注
      };
    },
    payyee() {
      this.PayyeeList.collectionAmount = "";
      this.PayyeeList.payment = "";
      this.PayyeeList.endDate = "";
      this.PayyeeList.remark = "";
    },
    //收款查看
    examine(row) {
      this.footer = false;
      this.footers = false;
      this.PayyeeList = {
        collectionId: row.collectionId, //收款人id
        collectionAmount: row.collectionAmount, //收款金额
        payment: row.payment, // 付款方式
        startDate: row.startDate, //开始日期
        endDate: row.endDate, //结束日期
        remark: row.remark, //备注
      };
    },
    // 收款编辑
    compile(row) {
      this.footer = false;
      this.footers = true;
      this.PayyeeList = {
        collectionId: row.collectionId, //收款人id
        collectionAmount: row.collectionAmount, //收款金额
        payment: row.payment, // 付款方式
        startDate: row.startDate, //开始日期
        endDate: row.endDate, //结束日期
        remark: row.remark, //备注
        id: row.id,
      };
    },
    //收款删除
    remove(row) {
     
      //console.log(row);
      const id = row.id;
      removeDelete(id).then((response) => {
        this.msgSuccess("删除成功");
         this.footers = false;
         this.footer =true;
        this.payye();
        this.searchList(this.moneyId);
        this.PayyeeList.collectionId = this.moneyId;
        this.getList();
      });
    },
    //收款详情修改
    Submits() {
      this.$refs["PayyeeList"].validate((valid) => {
        if (valid) {
          detailAmend(this.PayyeeList).then((response) => {
            if (response.code === 200) {
              this.msgSuccess("修改成功");
              //this.payye();
              this.searchList(this.moneyId);
              this.PayyeeList.collectionId = this.moneyId;
              this.getList();
            }
          });
        } else {
          this.msgSuccess("修改失败");
        }
      });
    },
    calloffs() {
      this.$refs["PayyeeList"].validate((valid) => {
        if (valid) {
          detailAmend(this.PayyeeList).then((response) => {
            this.payye();
            this.msgSuccess("修改成功");
            this.searchList(this.moneyId);
            this.PayyeeList.collectionId = this.moneyId;
            this.getList();
            this.gathering = false;
          });
        } else {
          this.msgSuccess("修改失败");
        }
      });
    },
  },
};
</script>
<style lang="sass" scoped>

</style>