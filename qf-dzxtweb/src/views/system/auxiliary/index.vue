<template>
  <div class="app-container">
    <div class="auxiliary-wrap">
      <div class="auxiliary-title">
        <h3 style="font-weight:400">辅助管理</h3>
      </div>
      <div class="auxiliary-tab">
        <div class="tab-left">
          <ul class="tabsList">
            <li :class="activeIndex==item.auxiliaryTypeId?'active':''"
                v-for="item of auxiliaryType"
                :key="item.auxiliaryTypeId"
                @click="handleClick(item)">{{item.auxiliaryTypeText}}
            </li>
          </ul>
          <el-form :model="queryParams"
                   ref="queryForm"
                   @submit.native.prevent
                   :inline="true">
            <el-input placeholder="名称"
                      v-model="searchName"
                      size="mini"
                      clearable
                      @keyup.enter.native="searchList()"
                      style="margin-left:5px;">
              <el-button slot="append"
                         icon="el-icon-search"
                         @click='searchList'></el-button>
            </el-input>
          </el-form>
        </div>
        <div class="tab-right">
          <el-button type="primary"
                     icon="el-icon-plus"
                     size="mini"
                     v-hasPermi="['accounting:auxiliary:add']"
                     @click="handleAdd">新增</el-button>
          <el-button type="danger"
                     icon="el-icon-delete"
                     size="mini"
                     :disabled="multiple"
                     v-hasPermi="['accounting:auxiliary:del']"
                     @click="handleDelete">删除</el-button>
        </div>
      </div>
      <div class="auxiliary-content">
        <el-table v-loading="loading"
                  :data="auxiliaryList"
                  @selection-change="handleSelectionChange"
                  border>
          <el-table-column type="selection"
                           width="55"
                           align="center" />
          <el-table-column label="编码"
                           align="center"
                           prop="code" />
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
                         v-hasPermi="['accounting:auxiliary:edit']"
                         @click="handleUpdate(scope.row)">修改</el-button>
              <el-button size="mini"
                         type="text"
                         icon="el-icon-delete"
                         v-hasPermi="['accounting:auxiliary:del']"
                         @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <!-- 添加或修改科目 -->
    <el-dialog :title="title"
               :visible.sync="open"
               width="650px"
               @close="close('form')"
               append-to-body>
      <el-form ref="form"
               :model="form"
               :rules="rules"
               label-width="80px"
               label-position="left">
        <el-row :gutter="10">
          <el-col :xs="24"
                  :md="12"
                  :xl="12">
            <el-form-item label="编码"
                          prop="code">
              <el-input v-model="form.code"
                        placeholder="请输入编码"
                        disabled
                        readonly />
            </el-form-item>
          </el-col>
          <el-col :xs="24"
                  :md="12"
                  :xl="12">
            <el-form-item label="名称"
                          prop="name">
              <el-input v-model="form.name"
                        placeholder="名称" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer"
           class="dialog-footer el-row">
        <el-button type="primary"
                   @click="submit">确 定</el-button>
        <el-button @click="close('form')">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { auxiliaryType, auxiliaryList, auxiliaryCode, addAuxiliary, updateAuxiliary, delAuxiliary, auxiliaryDetail } from "@/api/system/auxiliary";

export default {
  name: 'Auxiliary',
  data () {
    return {
      // 辅助核算类型
      auxiliaryType: [],
      // 选中的辅助核算类型
      activeIndex: null,
      // 辅助核算列表
      auxiliaryList: [],
      // 正在加载
      loading: true,
      // 查询条件
      queryParams: {
        bookId: this.getUrlBookId(),//this.$store.state.bookId || this.localStorage.getItem('bookId'),
        auxiliaryTypeId: null,
        auxiliaryTypeText: null
      },
      // 辅助核算表单
      form: {},
      // 辅助核算表单校验
      rules: {
        code: [
          { required: true, message: "编号不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: " 名称不能为空", trigger: "blur" }
        ]
      },
      // 
      open: false,
      // 新增或编辑表单标题
      title: '',
      // 新增 or 修改
      addOrEdit: 'add',
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 选中数组
      ids: [],
      // 选中数组编码
      codes:[],
      // 搜索名称
      searchName: null,
      // 是否监听activeIndex
      isClick: true

    }
  },
  created () {
    this.getAuxiliaryType();
    this.getAuxiliaryList();
  },
  methods: {
    // 获取辅助核算类型
    getAuxiliaryType () {
      auxiliaryType().then(res => {
        this.auxiliaryType = res.rows;
        this.activeIndex = res.rows[0].auxiliaryTypeId;
        this.isClick = true;
        this.queryParams.auxiliaryTypeId = res.rows[0].auxiliaryTypeId;
        this.queryParams.auxiliaryTypeText = res.rows[0].auxiliaryTypeText;
      })
    },
    // 获取辅助核算列表
    getAuxiliaryList () {
      this.loading = true;
      auxiliaryList(this.queryParams).then(res => {
        this.auxiliaryList = res.rows;
        this.loading = false;
      })
    },
    // 辅助核算点击事件
    handleClick (item) {
      this.queryParams.auxiliaryTypeId = item.auxiliaryTypeId;
      this.queryParams.auxiliaryTypeText = item.auxiliaryTypeText;
      this.activeIndex = item.auxiliaryTypeId;
      this.isClick = true;
    },
    // 重置辅助核算表单
    // 重置科目表单
    reset () {
      this.form = {
        code: null,
        name: null,
        auxiliaryTypeId: null
      }
      this.resetForm('form');
    },
    // 辅助核算新增
    handleAdd () {
      this.reset();
      this.addOrEdit = 'add';
      this.title = '新增' + this.queryParams.auxiliaryTypeText;
      this.open = true;
      auxiliaryCode(this.queryParams).then(res => {
        this.form.code = res;
      })
    },
    // 辅助核算修改
    handleUpdate (data) {
      this.reset();
      this.addOrEdit = 'edit';
      this.title = '编辑' + this.queryParams.auxiliaryTypeText + ' 辅助核算';
      auxiliaryDetail(data.auxiliaryId).then(res=>{
        this.form = res.data;
        this.open = true;
      })
    },
    // 辅助核算表单信息提交
    submit () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.addOrEdit == 'add') {
            let obj = Object.assign(this.form, this.queryParams);
            addAuxiliary(obj).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getAuxiliaryList();
              }
            });
          } else {
            let obj = Object.assign(this.form, this.queryParams);
            updateAuxiliary(obj).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getAuxiliaryList();
              }
            });
          }
        }
      });
    },
    // 新增或修改辅助核算取消
    // cancel () {
    //   this.open = false;
    //   this.clearFormValidate('form')
    // },
    // 新增或修改辅助核算弹框关闭
    close (el) {
      this.open = false;
      this.clearFormValidate(el);

    },
    // 多选框选中数据
    handleSelectionChange (selection) {
      this.ids = selection.map(item => item.auxiliaryId);
      this.codes = selection.map(item => item.code);
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    // 删除辅助核算
    handleDelete (data) {
      const ids = data.auxiliaryId || this.ids;
      const codes = data.code  || this.codes;
      this.$confirm('是否确认删除辅助核算编码为"' + codes + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delAuxiliary(ids);
      }).then(() => {
        this.msgSuccess("删除成功");
        this.getAuxiliaryList();
      }).catch(function () { });
    },
    //根据辅助核算名称查询列表
    searchList (query) {
      let bookId = this.getUrlBookId();
      let object = { bookId: bookId, name: this.searchName, auxiliaryTypeId:this.queryParams.auxiliaryTypeId }
      let queryParams = {};
      if (Object.prototype.toString.call(query) !== '[object Undefined]') {
        queryParams = Object.keys(query).length == 0 ? object : Object.assign(object, query);
      } else {
        queryParams = object;
      }
      this.loading = true;
      auxiliaryList(queryParams).then(res => {
        this.auxiliaryList = res.rows;
        // if (res.rows.length != 0) {
        //   this.activeIndex = res.rows[0].auxiliaryTypeId;
        // } else {
        //   this.activeIndex = this.auxiliaryType[0].auxiliaryTypeId;
        // }
        this.isClick = false;
        this.loading = false;
      })
    },
  },
  watch: {
    activeIndex () {
      if (this.isClick)
        this.getAuxiliaryList();
    },
    searchName () {
      // if (this.searchName == '' || this.searchName == null || this.searchName.length == 0) {
      //   let auxiliaryTypeId = this.auxiliaryType[0].auxiliaryTypeId
      //   this.searchList({ auxiliaryTypeId })
      // } else {
      //   this.searchList();
      // }
      this.searchList();
    }
  }
}
</script>
<style lang="scss" scoped>
@import "~@/assets/styles/variables.scss";

.auxiliary-wrap {
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
  .auxiliary-title {
    width: 98%;
    height: 20px;
    margin: 3px 0;
    display: flex;
    flex-flow: row nowrap;
    justify-content: center;
    align-items: center;
  }
  .auxiliary-tab {
    width: 98%;
    height: 40px;
    margin: 3px 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .tab-left {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
  .auxiliary-content {
    width: 98%;
    margin: 3px 10px;
  }
}
.tabsList {
  width: 70%;
  min-width: 260px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-left: 0;
  li {
    padding: 5px 10px;
    box-sizing: border-box;
    list-style: none;
    font-size: 14px;
    font-weight: 200;
    margin-right: 5px;
    transition: all .3s cubic-bezier(0.645, 0.045, 0.355, 1);
    border: 1px solid $gray;
    border-radius: 3px;
    cursor: pointer;
  }
  li:last-child {
    margin-right: 0;
  }
  li.active {
    color: #fff;
    background-color: $light-blue;
    transition: transform .3s;
    z-index: 2;
  }
}
</style>