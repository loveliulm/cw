<template>
  <div class="app-container">
    <el-form :inline="true">
      <el-form-item label="公司名称">
        <el-input v-model="queryParams.deptName"
                  placeholder="请输入公司名称"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryParams.status"
                   placeholder="公司状态"
                   clearable
                   size="small">
          <el-option v-for="dict in statusOptions"
                     :key="dict.dictValue"
                     :label="dict.dictLabel"
                     :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button class="filter-item"
                   type="primary"
                   icon="el-icon-search"
                   size="mini"
                   @click="handleQuery">搜索</el-button>
        <el-button class="filter-item"
                   type="primary"
                   icon="el-icon-plus"
                   size="mini"
                   @click="handleAdd"
                   v-hasPermi="['system:dept:add']">新增</el-button>
        <!--<el-button class="filter-item"
                   type="primary"
                   icon="el-icon-plus"
                   size="mini"
                   @click="handleAdd"
                   v-hasPermi="['system:dept:add']">批量删除</el-button>-->
      </el-form-item>
    </el-form>

    <el-table v-loading="loading"
              :data="deptList">
      <el-table-column type="selection"
                       width="55"
                       align="center" />
      <el-table-column label="公司名称"
                       align="center"
                       prop="deptName"
                       width="160" />
      -
      <el-table-column label="排序"
                       align="center"
                       prop="orderNum"
                       :show-overflow-tooltip="true"
                       width="140" />
      <el-table-column label="账套个数"
                       align="center"
                       prop="accountBookNum"
                       :show-overflow-tooltip="true"
                       width="100" />
      <el-table-column label="状态"
                       align="center"
                       width="160">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status"
                     active-value="0"
                     inactive-value="1"
                     @change="handleStatusChange(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间"
                       align="center"
                       prop="createTime"
                       width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作"
                       align="center"
                       class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini"
                     type="text"
                     icon="el-icon-edit"
                     @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:user:edit']">修改</el-button>
          <el-button v-if="scope.row.userId !== 1"
                     size="mini"
                     type="text"
                     icon="el-icon-delete"
                     @click="handleDelete(scope.row)"
                     v-hasPermi="['system:user:remove']">删除</el-button>
          <el-button size="mini"
                     type="text"
                     icon="el-icon-key"
                     slot="reference"
                     @click="hadleDetail(scope.row)"
                     v-hasPermi="['system:user:resetPwd']">查看部门</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0"
                :total="total"
                :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize"
                @pagination="getList" />

    <!-- 添加或修改部门对话框 -->
    <el-dialog :title="title"
               :visible.sync="open"
               width="600px"
               append-to-body>
      <el-form ref="form"
               :model="form"
               :rules="rules"
               label-width="80px">
        <el-row>
          <el-col :span="24"
                  v-if="form.parentId !== 0">
            <el-form-item label="上级部门"
                          prop="parentId">
              <!--<treeselect v-model="form.parentId"
                          :options="deptOptions"
                          :normalizer="normalizer"
                          placeholder="选择上级部门" />-->
              <el-input v-model="parentName"
                        readonly />
              <!--<el-input v-model="form.parentId"
                        type="hidden" />-->
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="名称"
                          prop="deptName">
              <el-input v-model="form.deptName"
                        placeholder="请输入公司名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账套个数"
                          prop="accountBookNum">
              <el-input-number v-model="form.accountBookNum"
                               :min="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开始时间"
                          prop="beginDate">
              <el-date-picker v-model="form.beginDate"
                              type="datetime"
                              placeholder="结束日期"
                              value-format="yyyy-MM-dd"
                              format="yyyy-MM-dd"
                              :picker-options="pickerOptionsStart" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间"
                          prop="endDate">
              <el-date-picker v-model="form.endDate"
                              type="datetime"
                              placeholder="结束日期"
                              value-format="yyyy-MM-dd"
                              format="yyyy-MM-dd"
                              :picker-options="pickerOptionsEnd" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序"
                          prop="orderNum">
              <el-input-number v-model="form.orderNum"
                               controls-position="right"
                               :min="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人"
                          prop="leader">
              <el-input v-model="form.leader"
                        placeholder="请输入负责人"
                        maxlength="20" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话"
                          prop="phone">
              <el-input v-model="form.phone"
                        placeholder="请输入联系电话"
                        maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱"
                          prop="email">
              <el-input v-model="form.email"
                        placeholder="请输入邮箱"
                        maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label=" 公司状态">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in statusOptions"
                          :key="dict.dictValue"
                          :label="dict.dictValue">{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer"
           class="dialog-footer">
        <el-button type="primary"
                   @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!--弹出框，显示公司部门-->
    <el-dialog :visible.sync="showSection"
               width="600px"
               append-to-body>
      <el-table :data="tableData"
                style="width: 100%">

        <!--<el-table-column prop="parentName"
                         label="所属公司">
        </el-table-column>-->
        <el-table-column prop="deptName"
                         label="部门名称">
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { listDept, getDept, delDept, addDept, updateDept, listDeptExcludeChild } from "@/api/system/dept";
import { changeUserStatus } from "@/api/system/user";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Dept",
  components: { Treeselect },
  data () {
    return {
      // 遮罩层
      loading: true,
      // 表格树数据
      deptList: [],
      // 部门树选项
      deptOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 状态数据字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        deptName: undefined,
        status: undefined,
        pageNum: 1,
        pageSize: 10,
        parentId: this.$store.state.user.deptId
      },
      parentName: this.$store.state.user.nickName,

      // 总条数
      total: 0,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        /*parentId: [
          { required: true, message: "上级部门不能为空", trigger: "blur" }
        ],*/
        deptName: [
          { required: true, message: "部门名称不能为空", trigger: "blur" }
        ],
        accountBookNum: [
          { required: true, message: '账套个数不能为空', trigger: "blur" }
        ],
        beginDate: [
          { required: true, message: '账套开始时间不能为空', trigger: "blur" }
        ],
        endDate: [
          { required: true, message: '账套结束时间不能为空', trigger: "blur" }
        ],
        orderNum: [
          { required: true, message: "显示排序不能为空", trigger: "blur" }
        ],
        email: [
          {
            type: "email",
            message: "'请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        phone: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      },
      // pickerOptionsStart 与 pickerOptionsEnd 设置结束日期大于开始日期并且开始日期小于结束日期
      pickerOptionsStart: {
        disabledDate: time => {
          if (this.form.endDate) {
            return time.getTime() > new Date(this.form.endDate).getTime()
          }
        }
      },
      pickerOptionsEnd: {
        disabledDate: time => {
          if (this.form.beginDate) {
            return time.getTime() < new Date(this.form.beginDate).getTime() - 86400000
          }
        }
      },
      // 公司部门详情
      showSection: false,
      tableData: []
    };
  },
  created () {
    this.getList();
    this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    /** 查询部门列表 */
    getList () {
      this.loading = true;
      listDept(this.queryParams).then(response => {
        this.deptList = response.rows;
        this.total = response.total;
        //this.deptList = this.handleTree(response.data, "deptId");
        this.loading = false;
      });
    },
    /** 转换部门数据结构 */
    normalizer (node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      };
    },
    // 字典状态字典翻译
    statusFormat (row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 取消按钮
    cancel () {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset () {
      this.form = {
        deptId: undefined,
        parentId: undefined,
        deptName: undefined,
        orderNum: undefined,
        leader: undefined,
        phone: undefined,
        email: undefined,
        status: "0"
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.parentId = this.$store.state.user.deptId;
      this.getList();
    },
    /** 新增按钮操作 */
    handleAdd () {
      this.reset();
      this.form.parentId = this.$store.state.user.deptId;
      this.open = true;
      this.title = "添加公司";
      /*listDept().then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId"); //this.$store.state.user.nickName
      });*/
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset();
      getDept(row.deptId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改公司";
      });
      /*listDeptExcludeChild(row.deptId).then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId");
      });*/
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.parentId = this.$store.state.user.deptId;

          if (this.form.deptId != undefined) {
            updateDept(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }

            });
          } else {
            addDept(this.form).then(response => {
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
    handleDelete (row) {
      this.$confirm('是否确认删除名称为"' + row.deptName + '"的数据项?', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delDept(row.deptId);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () { });
    },
    // 用户状态修改
    handleStatusChange (row) {
      let _this = this;
      let text = row.status === "0" ? "启用" : "停用";
      _this.$confirm('确认要"' + text + '""' + row.deptName + '"公司吗?', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        updateDept(row).then(response => {
          if (response.code === 200) {
            _this.msgSuccess(text + "成功");
            _this.getList();
          }
        });
      }).catch(function () {
        row.status = row.status === "0" ? "1" : "0";
      });
    },
    // 显示公司部门
    hadleDetail (row) {
      listDept({ parentId: row.deptId }).then(response => {
        if (Object.prototype.toString.call(response.rows) == '[object Object]') {
          this.tableData.push(response.rows);
        } else {
          this.tableData = response.rows;
        };
      })
      this.showSection = true;
    }
  }
};
</script>
<style scoped lang="scss">
.el-date-editor.el-input {
  width: 200px !important;
}
</style>