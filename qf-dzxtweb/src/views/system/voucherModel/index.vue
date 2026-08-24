<template>
  <div class="app-container">
    <div class="vouchermodel-wrap">
      <div class="vouchermodel-title">
        <h3 style="font-weight:400">凭证模板</h3>
      </div>
      <div class="vouchermodel-tab">
        <div class="tab-left">
          <el-form :model="queryParams"
                   ref="queryForm"
                   :inline="true">
            <el-input placeholder="名称"
                      v-model="queryParams.name"
                      size="mini"
                      clearable>
              <el-button slot="append"
                         icon="el-icon-search"
                         @click='handleQuery'></el-button>
            </el-input>
          </el-form>
        </div>
        <div class="tab-right">
          <el-button type="danger"
                     icon="el-icon-delete"
                     size="mini"
                     :disabled="multiple"
                     v-hasPermi="['accounting:voucherModel:del']"
                     @click="handleDelete">删除</el-button>
          <!--<el-button type="warning"
                     icon="el-icon-download"
                     size="mini"
                     @click="handleExport">导出</el-button>-->
        </div>
      </div>
      <div class="vouchermodel-content">
        <el-table v-loading="loading"
                  :data="voucherModelList"
                  border
                  @selection-change="handleSelectionChange">
          <el-table-column type="selection"
                           width="55"
                           align="center" />
          <el-table-column label="名称"
                           align="center"
                           prop="name" />
          <el-table-column label="操作"
                           align="center"
                           class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini"
                         type="text"
                         icon="el-icon-edit"
                         v-hasPermi="['accounting:voucherModel:edit']"
                         @click="handleUpdate(scope.row)">修改</el-button>
              <el-button size="mini"
                         type="text"
                         icon="el-icon-delete"
                         v-hasPermi="['accounting:voucherModel:del']"
                         @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0"
                    :total="total"
                    :page.sync="queryParams.pageNum"
                    :limit.sync="queryParams.pageSize"
                    @pagination="getList" />
      </div>
    </div>
    <!-- 添加或修改模板凭证名称-->
    <el-dialog :title="title"
               :visible.sync="open"
               width="500px"
               append-to-body>
      <el-form ref="form"
               :model="form"
               :rules="rules"
               label-width="80px">
        <el-form-item label="名称"
                      prop="name">
          <el-input v-model="form.name"
                    placeholder="请输入名称" />
        </el-form-item>
      </el-form>
      <div slot="footer"
           class="dialog-footer">
        <el-button type="primary"
                   @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listVoucherModel, getVoucherModel, delVoucherModel, addVoucherModel, updateVoucherModel, exportVoucherModel } from "@/api/system/voucherModel";

export default {
  name: "VoucherModel",
  data () {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中数组名称
      names:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 凭证模板表格数据
      voucherModelList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem('bookId') : this.$store.state.bookId,
        name: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created () {
    this.getList();
  },
  methods: {
    /** 查询凭证模板列表 */
    getList () {
      this.loading = true;
      listVoucherModel(this.queryParams).then(response => {
        this.voucherModelList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        modelId: undefined,
        bookId: undefined,
        name: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    /*resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },*/
    // 多选框选中数据
    handleSelectionChange (selection) {
      this.ids = selection.map(item => item.modelId);
      this.names = selection.map(item => item.names);
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    /*handleAdd () {
      this.reset();
      this.open = true;
      this.title = "添加凭证模板";
    },*/
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset();
      const modelId = row.modelId || this.ids
      getVoucherModel(modelId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改凭证模板";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.modelId != undefined) {
            updateVoucherModel(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          }
          /*else {
            addVoucherModel(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
            });
          }*/
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      const modelIds = row.modelId || this.ids;
      const names = row.name || this.names;
      this.$confirm('是否确认删除凭证模板名称为"' + names + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delVoucherModel(modelIds);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () { });
    },
    /** 导出按钮操作 */
    handleExport () {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有凭证模板数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportVoucherModel(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function () { });
    }
  },
  watch: {
    'queryParams.name' () {
      this.getList();
    }
  }
};
</script>
<style lang="scss" scoped>
@import "~@/assets/styles/variables.scss";

.vouchermodel-wrap {
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
  .vouchermodel-title {
    width: 98%;
    height: 20px;
    margin: 3px 0;
    display: flex;
    flex-flow: row nowrap;
    justify-content: center;
    align-items: center;
  }
  .vouchermodel-tab {
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
  }
  .vouchermodel-content {
    width: 98%;
    margin: 3px 10px;
  }
}
</style>