<template>
  <div class="app-container">
    <el-form :model="queryParams"
             ref="queryForm"
             :inline="true"
             label-width="68px">
      <el-form-item label="会计期间">
        <el-col :span="11">
          <el-date-picker v-model="queryParams.yearPeriod"
                          size="small"
                          type="month"
                          placeholder="开始月份"
                          value-format="yyyy-MM">
          </el-date-picker>
        </el-col>
      </el-form-item>
      <el-form-item>
        <el-button type="primary"
                   icon="el-icon-search"
                   size="small"
                   @click="handleQuery">搜索</el-button>
        <!-- <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button> -->
      </el-form-item>
    </el-form>

    <el-table v-loading="loading"
              :data="totalList"
              border>
      <el-table-column label="科目编号"
                       align="center"
                       prop="subjectCode" />
      <el-table-column label="科目名称"
                       align="center"
                       prop="subjectName" />
      <el-table-column label="借方金额"
                       align="center"
                       prop="debit">
        <template slot-scope="scope">
          <p v-text="scope.row.debit == 0?'':scope.row.debit"></p>
        </template>
      </el-table-column>
      <el-table-column label="贷方金额"
                       align="center"
                       prop="credit">
        <template slot-scope="scope">
          <p v-text="scope.row.credit == 0?'':scope.row.credit"></p>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0"
                :total="total"
                :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize"
                @pagination="getList" />

  </div>
</template>

<script>
import { listTotal, getTotal, delTotal, addTotal, updateTotal, exportTotal } from "@/api/accBook/vouchTotal";
import { currentPeriod } from "@/api/voucher/viewvoucher";

export default {
  name: "Total",
  data () {
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
      // accBook表格数据
      totalList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId,
        yearPeriod: null,
        subjectCode: undefined,
        subjectName: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created () {
    this.initData()
  },
  methods: {
    async initData () {
      let currentPeriod = await this.getCurrentPeriod();
      this.queryParams.yearPeriod = currentPeriod.yearPeriod;
      this.getList();
    },
    /** 查询accBook列表 */
    getList () {
      this.loading = true;
      listTotal(this.queryParams).then(response => {
        this.totalList = response.rows;
        this.total = response.total;
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
    // 取消按钮
    cancel () {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset () {
      this.form = {
        id: undefined,
        subjectCode: undefined,
        subjectName: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateTotal(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addTotal(this.form).then(response => {
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
  }
};
</script>