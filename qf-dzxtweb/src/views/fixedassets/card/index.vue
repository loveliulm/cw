<template>
  <div class="app-container">
    <div class="card-wrap">
      <div class="card-tab">
        <div class="tab-left">
          <el-form refs="queryForm"
                   :inline="true"
                   :model="queryParams"
                   class="demo-form-inline">
            <el-form-item label="资产类型">
              <el-select v-model="queryParams.categoryId"
                         placeholder="请选择资产类型"
                         clearable
                         size="mini">
                <el-option v-for="item in categroyList"
                           :key="item.categoryId"
                           :label="item.name"
                           :value="item.categoryId">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="固定资产">
              <el-input v-model="queryParams.name"
                        clearable
                        placeholder="名称"
                        size="mini"></el-input>
            </el-form-item>

            <el-form-item>
              <el-button type="primary"
                         size="mini"
                         @click="handleQuery">搜索</el-button>
              <el-button size="mini"
                         @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="tab-right">
          <el-button type="primary"
                     icon="el-icon-plus"
                     size="mini"
                     v-hasPermi="['accounting:fixedAssetCard:add']"
                     @click="handleAdd">新增</el-button>
          <el-button type="danger"
                     icon="el-icon-delete"
                     size="mini"
                     :disabled="multiple"
                     v-hasPermi="['accounting:fixedAssetCard:del']"
                     @click="handleDel">删除</el-button>
          <!-- <el-button type="info"
                     icon="el-icon-upload2"
                     size="mini"
                     @click="handleImport">导入</el-button> -->
          <!-- <el-button type="warning"
                     icon="el-icon-download"
                     size="mini"
                     @click="handleExport">导出</el-button> -->
        </div>
      </div>
      <div class="card-content">
        <el-table :data="fixedAssetCardList"
                  border
                  style="width: 100%"
                  v-loading="loading"
                  @selection-change="handleSelectionChange">
          <el-table-column type="selection"
                           width="55">
          </el-table-column>
          <el-table-column label="操作"
                           align="center"
                           class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini"
                         type="text"
                         icon="el-icon-edit"
                         v-hasPermi="['accounting:fixedAssetCard:edit']"
                         @click="handleUpdate(scope.row)"></el-button>
              <el-button size="mini"
                         type="text"
                         icon="el-icon-delete"
                         v-hasPermi="['accounting:fixedAssetCard:del']"
                         @click="handleDel(scope.row)"></el-button>
            </template>
          </el-table-column>
          <el-table-column prop="cardCode"
                           label="编码">
          </el-table-column>
          <el-table-column prop="name"
                           label="名称">
          </el-table-column>
          <el-table-column prop="categoryName"
                           label="类别">
          </el-table-column>
          <el-table-column prop="startDate"
                           label="开始使用日期">
          </el-table-column>
          <el-table-column prop="originalValue"
                           label="原值">
          </el-table-column>
          <el-table-column prop="startDepreciation"
                           label="期初累计折旧">
          </el-table-column>
          <!--暂时没有返回这个字段-->
          <el-table-column prop="subsidy"
                           label="期末累计折旧">
          </el-table-column>
          <el-table-column prop="monthDepreciation"
                           label="月折旧">
          </el-table-column>
          <el-table-column prop="term"
                           label="预计使用期限">
          </el-table-column>
          <!--暂时没有返回这个字段-->
          <el-table-column prop="endowmentInsurance"
                           label="已使用期">
          </el-table-column>
          <!--暂时没有返回这个字段-->
          <el-table-column prop="medicareInsurance"
                           label="残值">
          </el-table-column>
          <el-table-column prop="residualRate"
                           label="残值率">
          </el-table-column>
          <el-table-column prop="impairmentPrepare"
                           label="减值准备">
          </el-table-column>
          <el-table-column prop="startNetValue"
                           label="期初净值">
          </el-table-column>
          <!--暂时没有返回这个字段-->
          <el-table-column prop="otherDeductions"
                           label="期末净值">
          </el-table-column>
          <!--暂时没有返回这个字段-->
          <el-table-column prop="accOtherDeduction"
                           label="折旧方法">
          </el-table-column>
          <el-table-column prop="accOtherDeduction"
                           label="状态">
            <template slot-scope="scope">
              <p v-text="scope.row.status?'正常':'清理'"></p>
            </template>
          </el-table-column>
          <el-table-column prop="cleanPeriod"
                           label="清理期间">
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0"
                    :total="total"
                    :page.sync="queryParams.pageNum"
                    :limit.sync="queryParams.pageSize"
                    @pagination="getFixedAssetCardList" />
      </div>
    </div>
  </div>
</template>
<script>
import { fixedAssertCategroy, fixedAssetCard, delAsset } from "@/api/fixedassets/card";
export default {
  name: 'Card',
  data () {
    return {
      // 查询条件
      queryParams: {
        bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId,
        categoryId: null,
        name: null,
        pageNum: 1,
        pageSize: 10,
      },
      // bookId
      bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId,
      // 固定资产类别
      categroyList: [],
      // 固定资产卡片列表
      fixedAssetCardList: [],
      // 固定资产列表是否正在加载中
      loading: true,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 选中行的工资id
      ids: [],
      // 选中行的工资编码
      cardCodes:[],
      // 总条数
      total: 0,

    }
  },
  created () {
    this.getFixedAssertCategroy();
    this.getFixedAssetCardList();
  },
  methods: {
    // 导入
    handleImport () { },
    // 导出
    handleExport () { },
    // 重置按钮操作 
    resetQuery () {
      this.queryParams.categoryId = null;
      this.queryParams.name = null;
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 搜索按钮操作 
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getFixedAssetCardList();
    },
    // 多选框选中数据
    handleSelectionChange (selection) {
      this.ids = selection.map(item => item.cardId);
      this.cardCodes = selection.map(item => item.cardCode);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    // 工资删除
    handleDel (row) {
      const cardIds = row.cardId || this.ids;
      const cardCodes = row.cardCode || this.cardCodes;                                                                                                                                                                                                                                                                                                                                                     
      this.$confirm(
        '是否确认删除固定工资卡片编码为"' + cardCodes + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function () {
          return delAsset(cardIds);
        })
        .then(() => {
          this.msgSuccess("删除成功");
          this.getFixedAssetCardList();
        })
        .catch(function () { });
    },
    // 新增
    handleAdd () {
      this.$router.push({
        path: '/fixedassets/assetAdd',
      })
    },
    // 编辑
    handleUpdate (row) {
      const cardId = row.cardId;
      this.$router.push({
        path: '/fixedassets/assetEdit',
        query: { cardId }
      })
    },
    // 获取固定资产类型列表
    getFixedAssertCategroy () {
      fixedAssertCategroy({ bookId: this.bookId }).then(res => {
        this.categroyList = res.rows;
      })
    },
    // 获取固定资产卡片列表
    getFixedAssetCardList () {
      this.loading = true;
      fixedAssetCard(this.queryParams).then(res => {
        this.fixedAssetCardList = res.rows;
        this.total = res.total;
        this.loading = false;
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.card-wrap {
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
  .card-tab {
    width: 98%;
    display: flex;
    flex-flow: row wrap;
    justify-content: space-between;
    //align-items: center;
    //margin: 3px 10px;
    .tab-left {
      float: left;
    }
    .tab-right {
      float: right;
      margin-top: 2px;
    }
  }
  .card-content {
    width: 98%;
    // margin: 3px 10px;
  }
}
</style>