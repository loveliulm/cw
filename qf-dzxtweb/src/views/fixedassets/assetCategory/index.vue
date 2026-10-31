<template>
  <div class="app-container">
    <div class="assetCategory-wrap">
      <div class="assetCategory-title">
        <h3 style="font-weight:400">资产类别</h3>
      </div>
      <div class="assetCategory-tab">
        <div class="tab-right">
          <el-button type="primary"
                     icon="el-icon-plus"
                     size="mini"
                     v-hasPermi="['accounting:fixedAssertCategroy:add']"
                     @click="handleAdd">新增</el-button>
          <el-button type="danger"
                     icon="el-icon-delete"
                     size="mini"
                     :disabled="multiple"
                     v-hasPermi="['accounting:fixedAssertCategroy:del']"
                     @click="handleDel">删除</el-button>
        </div>
      </div>
      <div class="assetCategory-content">
        <el-table v-loading="loading"
                  :data="categroyList"
                  border
                  @selection-change="handleSelectionChange">
          <el-table-column type="selection"
                           width="55"
                           align="center" />
          <el-table-column label="操作"
                           align="center"
                           class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini"
                         type="text"
                         icon="el-icon-edit"
                         v-hasPermi="['accounting:fixedAssertCategroy:edit']"
                         @click="handleUpdate(scope.row)"></el-button>
              <el-button size="mini"
                         type="text"
                         icon="el-icon-delete"
                         v-hasPermi="['accounting:fixedAssertCategroy:del']"
                         @click="handleDel(scope.row)"></el-button>
            </template>
          </el-table-column>
          <el-table-column prop="code"
                           label="类别编码">
          </el-table-column>
          <el-table-column prop="name"
                           label="类别名称">
          </el-table-column>
          <el-table-column prop="years"
                           label="预计使用年限">
          </el-table-column>
          <el-table-column prop="remark"
                           label="备注">
          </el-table-column>
        </el-table>

        <pagination v-show="total>0"
                    :total="total"
                    :page.sync="queryParams.pageNum"
                    :limit.sync="queryParams.pageSize"
                    @pagination="getFixedAssertCategroy" />
      </div>
    </div>
    <!--新增或修改资产类别弹出框-->
    <el-dialog :title="title"
               :visible.sync="openAssetCategory"
               :close-on-click-modal="false"
               size="tiny">
      <el-form ref="form"
               :model="form"
               :rules="rules"
               label-width="110px"
               label-position="left">
        <el-form-item label="编码"
                      prop="code">
          <el-input v-model="form.code"
                    disabled></el-input>
        </el-form-item>
        <el-form-item label="类别名称"
                      prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="预计使用年限"
                      prop="years">
           <el-input-number v-model="form.years"
                                   :min="0"
                                   :controls="false"
                                   :precision="0"></el-input-number>
        </el-form-item>
        <el-form-item label="备注"
                      prop="remark">
          <el-input type="textarea"
                    v-model="form.remark"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer"
           class="dialog-footer">
        <el-button type="primary"
                   @click="submitForm">保 存</el-button>
        <el-button @click="close">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { fixedAssertCategroy } from "@/api/fixedassets/card";
import { delAssetCategory, AssetCategoryCode, addAssetCategory, editAssetCategory, assetCategoryDetail } from "@/api/fixedassets/assetCategory"
export default {
  name: 'AssetCategory',
  data () {
    return {
      // 资产类型列表是否正在加载
      loading: true,
      // 资产类型列表
      categroyList: [],
      // bookId
      bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId,
      // 资产类别列表总条数
      total: 0,
      // 查询条件
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 多选的id
      ids: [],
      // 多选的code
      codes:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 资产类别表单
      form: {},
      // 表单校验规则
      rules: {
        code: [
          { required: true, message: '请输入编码' }
        ],
        name: [
          { required: true, message: '请输入类别名称' }
        ],
        years: [
          { required: true, message: '请输入预计使用年限' }
        ]
      },
      // 新增或修改资产类别列表弹出框是否弹出
      openAssetCategory: false,
      // 弹框标题
      title:'',
    }
  },
  created () {
    this.getFixedAssertCategroy();
  },
  methods: {
    // 获取固定资产类型列表
    getFixedAssertCategroy () {
      this.loading = true;
      fixedAssertCategroy({ bookId: this.bookId }).then(res => {
        this.categroyList = res.rows;
        this.total = res.total;
        this.loading = false;
      })
    },
    // 多选框选中数据
    handleSelectionChange (selection) {
      this.ids = selection.map(item => item.categoryId);
      this.codes = selection.map(item => item.code);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    // 删除按钮操作
    handleDel (row) {
      const categoryIds = row.categoryId || this.ids;
      const categroyCodes = row.code || this.codes;
      this.$confirm(
        '是否确认删除类别编码为"' + categroyCodes + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function () {
          return delAssetCategory(categoryIds);
        })
        .then(() => {
          this.getFixedAssertCategroy();
          this.msgSuccess("删除成功");
        })
        .catch(function () { });
    },
    // 重置表单
    reset () {
      this.form = {
        categoryId: null,
        code: null,
        name: null,
        years: null,
        remark: null
      };
      this.resetForm("form");
    },
    // 资产类型新增
    handleAdd () {
      this.reset();
      this.title = '新增资产类别';
      AssetCategoryCode({ bookId: this.bookId }).then(res => {
        this.form.code = res;
        this.openAssetCategory = true;
      })
    },
    // 资产类型编辑
    handleUpdate (row) {
      this.reset();
      this.title = '编辑资产类别';
      let categoryId = row.categoryId;
      assetCategoryDetail(categoryId).then(res => {
        this.form = res.data;
        this.openAssetCategory = true;
      })
    },
    // 提交表单
    submitForm () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.bookId = this.bookId;
          if (this.form.categoryId != null) {
            editAssetCategory(this.form).then(res => {
              this.msgSuccess("修改成功");
              this.openAssetCategory = false;
              this.getFixedAssertCategroy();
            })
          } else {
            addAssetCategory(this.form).then(res => {
              this.msgSuccess("保存成功");
              this.openAssetCategory = false;
              this.getFixedAssertCategroy();
            })
          }
        }
      })
    },
    // 关闭弹窗
    close () {
      this.clearFormValidate("form");
      this.openAssetCategory = false;
    },
  }
}
</script>
<style lang="scss" scoped>
@import "~@/assets/styles/variables.scss";

.assetCategory-wrap {
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
  .assetCategory-title {
    width: 98%;
    height: 20px;
    margin: 3px 0;
    display: flex;
    flex-flow: row nowrap;
    justify-content: center;
    align-items: center;
  }
  .assetCategory-tab {
    width: 98%;
    height: 40px;
    margin: 3px 10px;
    display: flex;
    flex-flow: row-reverse wrap;
    justify-content: space-between;
    align-items: center;
    .tab-right,
    .tab-left {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
  .assetCategory-content {
    width: 98%;
    margin: 3px 10px;
  }
}
/deep/.el-input-number.is-without-controls .el-input__inner {
  text-align: left !important;
}
</style>