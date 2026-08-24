<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="客户id" prop="customerId">
        <el-input
          v-model="queryParams.customerId"
          placeholder="请输入客户id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="增值税" prop="taxVatChange">
        <el-input
          v-model="queryParams.taxVatChange"
          placeholder="请输入增值税"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="城市维护建设费" prop="taxBuilding">
        <el-input
          v-model="queryParams.taxBuilding"
          placeholder="请输入城市维护建设费"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="教育附加税" prop="taxEducation">
        <el-input
          v-model="queryParams.taxEducation"
          placeholder="请输入教育附加税"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="企业所得税" prop="taxCorporate">
        <el-input
          v-model="queryParams.taxCorporate"
          placeholder="请输入企业所得税"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="个人所得税" prop="taxPersonal">
        <el-input
          v-model="queryParams.taxPersonal"
          placeholder="请输入个人所得税"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="印花税" prop="taxStamp">
        <el-input
          v-model="queryParams.taxStamp"
          placeholder="请输入印花税"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="其他" prop="taxOther">
        <el-input
          v-model="queryParams.taxOther"
          placeholder="请输入其他"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="合计" prop="total">
        <el-input
          v-model="queryParams.total"
          placeholder="请输入合计"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="年" prop="year">
        <el-input
          v-model="queryParams.year"
          placeholder="请输入年"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="月" prop="month">
        <el-input
          v-model="queryParams.month"
          placeholder="请输入月"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
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
          v-hasPermi="['customer:declaration:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['customer:declaration:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['customer:declaration:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['customer:declaration:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="declarationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="客户id" align="center" prop="customerId" />
      <el-table-column label="增值税" align="center" prop="taxVatChange" />
      <el-table-column label="城市维护建设费" align="center" prop="taxBuilding" />
      <el-table-column label="教育附加税" align="center" prop="taxEducation" />
      <el-table-column label="企业所得税" align="center" prop="taxCorporate" />
      <el-table-column label="个人所得税" align="center" prop="taxPersonal" />
      <el-table-column label="印花税" align="center" prop="taxStamp" />
      <el-table-column label="其他" align="center" prop="taxOther" />
      <el-table-column label="合计" align="center" prop="total" />
      <el-table-column label="年" align="center" prop="year" />
      <el-table-column label="月" align="center" prop="month" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['customer:declaration:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['customer:declaration:remove']"
          >删除</el-button>
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

    <!-- 添加或修改报税对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="客户id" prop="customerId">
          <el-input v-model="form.customerId" placeholder="请输入客户id" />
        </el-form-item>
        <el-form-item label="增值税" prop="taxVatChange">
          <el-input v-model="form.taxVatChange" placeholder="请输入增值税" />
        </el-form-item>
        <el-form-item label="城市维护建设费" prop="taxBuilding">
          <el-input v-model="form.taxBuilding" placeholder="请输入城市维护建设费" />
        </el-form-item>
        <el-form-item label="教育附加税" prop="taxEducation">
          <el-input v-model="form.taxEducation" placeholder="请输入教育附加税" />
        </el-form-item>
        <el-form-item label="企业所得税" prop="taxCorporate">
          <el-input v-model="form.taxCorporate" placeholder="请输入企业所得税" />
        </el-form-item>
        <el-form-item label="个人所得税" prop="taxPersonal">
          <el-input v-model="form.taxPersonal" placeholder="请输入个人所得税" />
        </el-form-item>
        <el-form-item label="印花税" prop="taxStamp">
          <el-input v-model="form.taxStamp" placeholder="请输入印花税" />
        </el-form-item>
        <el-form-item label="其他" prop="taxOther">
          <el-input v-model="form.taxOther" placeholder="请输入其他" />
        </el-form-item>
        <el-form-item label="合计" prop="total">
          <el-input v-model="form.total" placeholder="请输入合计" />
        </el-form-item>
        <el-form-item label="年" prop="year">
          <el-input v-model="form.year" placeholder="请输入年" />
        </el-form-item>
        <el-form-item label="月" prop="month">
          <el-input v-model="form.month" placeholder="请输入月" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDeclaration, getDeclaration, delDeclaration, addDeclaration, updateDeclaration, exportDeclaration } from "@/api/customer/declaration";

export default {
  name: "Declaration",
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
      // 报税表格数据
      declarationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        customerId: undefined,
        taxVatChange: undefined,
        taxBuilding: undefined,
        taxEducation: undefined,
        taxCorporate: undefined,
        taxPersonal: undefined,
        taxStamp: undefined,
        taxOther: undefined,
        total: undefined,
        year: undefined,
        month: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询报税列表 */
    getList() {
      this.loading = true;
      listDeclaration(this.queryParams).then(response => {
        this.declarationList = response.rows;
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
        customerId: undefined,
        taxVatChange: undefined,
        taxBuilding: undefined,
        taxEducation: undefined,
        taxCorporate: undefined,
        taxPersonal: undefined,
        taxStamp: undefined,
        taxOther: undefined,
        total: undefined,
        year: undefined,
        month: undefined,
        createTime: undefined,
        updateTime: undefined,
        createBy: undefined,
        updateBy: undefined
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加报税";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDeclaration(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改报税";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateDeclaration(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addDeclaration(this.form).then(response => {
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
      this.$confirm('是否确认删除报税编号为"' + ids + '"的数据项?', "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delDeclaration(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有报税数据项?', "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportDeclaration(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    }
  }
};
</script>