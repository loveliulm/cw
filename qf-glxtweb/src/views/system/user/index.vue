<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <div class="head-container">
            <el-input
              v-model="deptName"
              placeholder="请输入部门名称"
              clearable
              size="small"
              prefix-icon="el-icon-search"
              style="margin-bottom: 20px"
            />
          </div>
          <div class="el-row btn-groups" :gutter="24">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              class="el-col-8 el-col-xs-20 nomargin elBtn"
              @click="addSection"
              v-hasPermi="['system:user:add']"
            ></el-button>
            <el-button
              type="primary"
              icon="el-icon-edit"
              size="mini"
              class="el-col-8 el-col-xs-20 nomargin elBtn"
              :disabled="editsection"
              @click="editSection"
              v-hasPermi="['system:user:edit']"
            ></el-button>
            <el-button
              type="primary"
              icon="el-icon-delete"
              size="mini"
              class="el-col-8 el-col-xs-20 nomargin elBtn"
              :disabled="deletesection"
              @click="delSection"
              v-hasPermi="['system:user:remove']"
            ></el-button>
          </div>
          <el-tree
            :data="deptOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
          >
            <span class="span-ellipsis" slot-scope="{ node, data }">
              <span @click="handleNodeClick(data)" :title="node.label">{{
                node.label
              }}</span>
            </span>
          </el-tree>
        </div>
      </el-col>
      <!--用户数据-->
      <el-col :span="20" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="68px"
        >
          <el-form-item label="用户名称" prop="userName">
            <el-input
              v-model="queryParams.userName"
              placeholder="请输入用户名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="手机号码" prop="phonenumber">
            <el-input
              v-model="queryParams.phonenumber"
              placeholder="请输入手机号码"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="用户状态"
              clearable
              size="small"
              style="width: 240px"
            >
              <el-option
                v-for="dict in statusOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="dateRange"
              size="small"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
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

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['system:user:add']"
              >新增</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['system:user:edit']"
              >修改</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['system:user:remove']"
              >删除</el-button
            >
          </el-col>
          <!-- <el-col :span="1.5">
            <el-button type="info"
                      icon="el-icon-upload2"
                       size="mini"
                       @click="handleImport"
                       v-hasPermi="['system:user:import']">导入</el-button>
          </el-col>-->
          <el-col :span="1.5">
            <el-button
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['system:user:export']"
              >导出</el-button
            >
          </el-col>
        </el-row>

        <el-table
          v-loading="loading"
          :data="userList"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <!--<el-table-column label="用户编号"
                           align="center"
          prop="userId" />-->
          <el-table-column
            label="用户名称"
            align="center"
            prop="userName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="用户昵称"
            align="center"
            prop="nickName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="部门"
            align="center"
            prop="dept.deptName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="手机号码"
            align="center"
            prop="phonenumber"
            width="120"
          />
          <el-table-column label="状态" align="center">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="0"
                inactive-value="1"
                @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column
            label="创建时间"
            align="center"
            prop="createTime"
            width="160"
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="180"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:user:edit']"
                >修改</el-button
              >
              <el-button
                v-if="!scope.row.companyAdmin"
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:user:remove']"
                >删除</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-key"
                @click="handleResetPwd(scope.row)"
                v-hasPermi="['system:user:resetPwd']"
                >重置</el-button
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
      </el-col>
    </el-row>

    <!-- 添加对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input
                maxlength="30"
                v-model="form.nickName"
                placeholder="请输入用户昵称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户名称" prop="userName">
              <el-input
                maxlength="30"
                v-model="form.userName"
                autocomplete="off"
                placeholder="请输入用户名称"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="归属部门" prop="deptId">
              <!--:disable-branch-nodes="true":控制节点不可选-->
              <treeselect
                v-model="form.deptId"
                :options="deptOptions"
                :show-count="true"
                placeholder="请选择归属部门"
                @select="getRoles"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input
                v-model="form.email"
                placeholder="请输入邮箱"
                maxlength="20"
              />
            </el-form-item>
          </el-col>
        </el-row>

          <el-row :gutter="10">
            <el-col :span="12">
              <el-form-item label="登录号码" prop="phonenumber">
                <el-input
                  v-model="form.phonenumber"
                  placeholder="请输入手机号码"
                  maxlength="11"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="登录密码" prop="password">
                <el-input
                  v-model="form.password"
                  placeholder="请输入用户密码"
                  type="text"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :span="12">
              <el-form-item label="用户性别" prop="sex">
                <el-select v-model="form.sex" placeholder="请选择">
                  <el-option
                    v-for="dict in sexOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态">
                <el-radio-group v-model="form.status">
                  <el-radio
                    v-for="dict in statusOptions"
                    :key="dict.dictValue"
                    :label="dict.dictValue"
                    >{{ dict.dictLabel }}</el-radio
                  >
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :span="12" style="width: 100%">
              <el-form-item label="角色">
                <el-select v-model="form.roleIds" multiple placeholder="请选择">
                  <el-option
                    v-for="item in roleOptions"
                    :key="item.roleId"
                    :label="item.roleName"
                    :value="item.roleId"
                    :disabled="item.status == 1"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        <el-row :gutter="10">
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input
                v-model="form.remark"
                type="textarea"
                placeholder="请输入内容"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel()">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 修改对话框 -->
    <el-dialog :title="title" :visible.sync="opens" width="600px" append-to-body>
      <el-form ref="forms" :model="forms" :rules="ruless" label-width="80px">
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input
                maxlength="30"
                v-model="forms.nickName"
                placeholder="请输入用户昵称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户名称" prop="userName">
              <el-input
                maxlength="30"
                v-model="forms.userName"
                autocomplete="off"
                placeholder="请输入用户名称"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="归属部门" prop="deptId">
              <!--:disable-branch-nodes="true":控制节点不可选-->
              <treeselect
                v-model="forms.deptId"
                :options="deptOptions"
                :show-count="true"
                placeholder="请选择归属部门"
                @select="getRoles"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input
                v-model="forms.email"
                placeholder="请输入邮箱"
                maxlength="20"
              />
            </el-form-item>
          </el-col>
        </el-row>


          <el-row :gutter="10">
            <el-col :span="12">
              <el-form-item label="登录号码" prop="phonenumber">
                <el-input
                  v-model="forms.phonenumber"
                  placeholder="请输入手机号码"
                  maxlength="11"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="用户性别" prop="sex">
                <el-select v-model="forms.sex" placeholder="请选择">
                  <el-option
                    v-for="dict in sexOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :span="12">
              <el-form-item label="状态">
                <el-radio-group v-model="forms.status">
                  <el-radio
                    v-for="dict in statusOptions"
                    :key="dict.dictValue"
                    :label="dict.dictValue"
                    >{{ dict.dictLabel }}</el-radio
                  >
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="角色">
                <el-select v-model="forms.roleIds" multiple placeholder="请选择">
                  <el-option
                    v-for="item in roleOptions"
                    :key="item.roleId"
                    :label="item.roleName"
                    :value="item.roleId"
                    :disabled="item.status == 1"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>


        <el-row :gutter="10">
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input
                v-model="forms.remark"
                type="textarea"
                placeholder="请输入内容"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForms">确 定</el-button>
        <el-button @click="cancels()">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加部门 -->
    <el-dialog
      :title="title"
      :visible.sync="openaddsection"
      width="600px"
      @close="sectionClose('section')"
      append-to-body
    >
      <el-form ref="section" :model="section" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="上级部门" prop="parentId">
              <treeselect
                v-model="section.parentId"
                :options="deptOptions"
                :show-count="true"
                placeholder="请选择上级部门"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              v-if="form.userId == undefined"
              label="部门名称"
              prop="deptName"
            >
              <el-input
                v-model="section.deptName"
                placeholder="请输入部门名称"
                maxlength="20"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSection">确 定</el-button>
        <el-button @click="cancelSection">取 消</el-button>
      </div>
    </el-dialog>
    <!--修改部门-->
    <el-dialog
      :title="title"
      :visible.sync="openeditsection"
      width="600px"
      @close="sectionClose('sectioned')"
      append-to-body
    >
      <el-form
        ref="sectioned"
        :model="sectionEdit"
        :rules="editrules"
        label-width="80px"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item
              v-if="form.userId == undefined"
              label="部门名称"
              prop="deptName"
            >
              <el-input
                v-model="sectionEdit.deptName"
                placeholder="请输入部门名称"
                maxlength="20"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitESection">确 定</el-button>
        <el-button @click="cancelESection">取 消</el-button>
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
          <el-checkbox
            v-model="upload.updateSupport"
          />是否更新已经存在的用户数据
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
  listUser,
  getUser,
  delUser,
  addUser,
  updateUser,
  exportUser,
  resetUserPwd,
  changeUserStatus,
  importTemplate,
  addDept,
  delDept,
  updateDept,
} from "@/api/system/user";
import { getToken } from "@/utils/auth";
import { treeselect, getRolesByDeptId, getDept } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "User",
  components: { Treeselect },
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
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: "",
      // 部门树选项
      deptOptions: undefined,
      // 是否显示弹出层
      open: false,
      opens: false,

      // 部门名称

      deptName: undefined,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 状态数据字典
      statusOptions: [],
      // 性别状态字典
      sexOptions: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
       forms: {},
      defaultProps: {
        children: "children",
        label: "label",
      },
      // 用户导入参数
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
        url: process.env.VUE_APP_BASE_API + "/system/user/importData",
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined,
        deptName: undefined,
      },
      parentName: "" || this.$store.state.user.nickName,
      // 表单校验
      rules: {
        userName: [
          { required: true, message: "用户名称不能为空", trigger: "blur" },
        ],
        nickName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" },
        ],
        deptId: [
          { required: true, message: "归属部门不能为空", trigger: "blur" },
        ],
        password: [
          { required: true, message: "用户密码不能为空", trigger: "blur" },
        ],
        email: [
          // { required: true, message: "邮箱地址不能为空", trigger: "blur" },
          {
            pattern: /[0-9a-zA-Z_.-]+[@][0-9a-zA-Z_.-]+([.][a-zA-Z]+){1,2}/,
            message: "请输入正确的邮箱地址",
            trigger: "blur",
          },
        ],
        phonenumber: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur",
          },
        ],
        deptName: [
          { required: true, message: "部门名称不能为空", trigger: "blur" },
          {
            pattern: /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/,
            message: "请输入正确的部门名称",
            trigger: "blur",
          },
        ],
        parentId: [
          { required: true, message: "上级部门不能为空", trigger: "blur" },
        ],
      },
      ruless: {
        userName: [
          { required: true, message: "用户名称不能为空", trigger: "blur" },
        ],
        nickName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" },
        ],
        deptId: [
          { required: true, message: "归属部门不能为空", trigger: "blur" },
        ],
        password: [
          { required: true, message: "用户密码不能为空", trigger: "blur" },
        ],
        email: [
          // { required: true, message: "邮箱地址不能为空", trigger: "blur" },
          {
            pattern: /[0-9a-zA-Z_.-]+[@][0-9a-zA-Z_.-]+([.][a-zA-Z]+){1,2}/,
            message: "请输入正确的邮箱地址",
            trigger: "blur",
          },
        ],
        phonenumber: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur",
          },
        ],
        deptName: [
          { required: true, message: "部门名称不能为空", trigger: "blur" },
          {
            pattern: /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/,
            message: "请输入正确的部门名称",
            trigger: "blur",
          },
        ],
        parentId: [
          { required: true, message: "上级部门不能为空", trigger: "blur" },
        ],
      },
      // 添加部门表单
      section: {},
      // 添加部门弹出框是否显示
      openaddsection: false,
      // 编辑部门表单
      sectionEdit: { deptName: undefined },
      // 是否编辑部门按钮是否禁用
      editsection: true,
      // 编辑部门弹出框是否显示
      openeditsection: false,
      // 是否删除部门按钮是否禁用
      deletesection: true,
      // 修改部门校验规则
      editrules: {
        deptName: [
          { required: true, message: "部门名称不能为空", trigger: "blur" },
          {
            pattern: /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/,
            message: "请输入正确的部门名称",
            trigger: "blur",
          },
        ],
      },
      //选项框的用户名
      idsss: [],

    };
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    },
  },
  created() {
    this.getList();
    this.getTreeselect();
    this.getDicts("sys_normal_disable").then((response) => {
      this.statusOptions = response.data;
    });
    this.getDicts("sys_user_sex").then((response) => {
      this.sexOptions = response.data;
    });
    /*this.getConfigKey("sys.user.initPassword").then(response => {
      this.initPassword = response.msg;
      console.log("我是我是：" + response.msg);
    });*/
  },
  methods: {
    // 查询用户列表
    getList() {
      this.loading = true;
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(
        (response) => {
          this.userList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 查询部门下拉树结构
    getTreeselect() {
      this.$nextTick(function () {
        treeselect().then((response) => {
          this.deptOptions = response.data;
        });
      });
    },
    // 筛选节点
    filterNode(value, data) {
      console.log(222);
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.parentName = data.label;
      if (this.isTopAccounts()) {
        this.editsection = true;
        this.deletesection = true;
      } else {
        this.editsection = false;
        this.deletesection = false;
      }
      this.getList();
    },
    // 用户状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$confirm(
        '确认要"' + text + '""' + row.userName + '"用户吗?',
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return changeUserStatus(row.userId, row.status);
        })
        .then(() => {
          this.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.status = row.status === "0" ? "1" : "0";
        });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      //this.reset();
    },
    cancels() {
      this.opens = false;
      //this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: undefined,
        status: "0",
        remark: undefined,
        postIds: [],
        roleIds: [],
      };
      this.resetForm("form");
    },
    // 搜索按钮操作
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    // 重置按钮操作
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.userId);
      this.idsss = selection.map((item) => item.userName);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    // 新增按钮操作
    handleAdd() {
      this.open = true;
      this.title = "添加用户";
      this.reset();
      this.getTreeselect();

    },
    // 修改按钮操作
    handleUpdate(row) {
      this.opens = true;
      this.title = "修改用户";
      const userId = row.userId || this.ids;
      getUser(userId).then((response) => {
        this.forms = response.data;
        //this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.forms.postIds = response.postIds;
        this.forms.roleIds = response.roleIds;

      });
    },
    // 重置密码按钮操作
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.userName + '"的新密码', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputValidator: (value) => {
          // 点击按钮时，对文本框里面的值进行验证
          if (!value) {
            return "输入密码不能为空";
          }
        },
      })
        .then(({ value }) => {
          resetUserPwd(row.userId, value).then((response) => {
            if (response.code === 200) {
              this.msgSuccess("修改成功，新密码是：" + value);
            }
          });
        })
        .catch(() => {});
    },
    // 提交按钮
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.userId != undefined) {

          } else {
            addUser(this.form).then((response) => {
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
    submitForms: function () {
      this.$refs["forms"].validate((valid) => {
        if (valid) {
          if (this.forms.userId != undefined) {
            updateUser(this.forms).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.opens = false;
                this.getList();
              }
            });
          }
        }
      });
    },
    // 添加部门
    addSection: function () {
      this.section = {};
      this.openaddsection = true;
      this.title = "添加部门";
    },
    // 添加部门表单提交
    submitSection: function () {
      this.$refs["section"].validate((valid) => {
        if (valid) {
          addDept(this.section).then((response) => {
            if (response.code === 200) {
              this.msgSuccess("新增部门成功");
              this.openaddsection = false;
              this.queryParams.deptId = undefined;
              this.getList();
              this.getTreeselect();
            }
          });
        }
      });
    },
    // 取消添加部门
    cancelSection: function () {
      this.openaddsection = false;
      this.queryParams.deptId = undefined;
      this.sectionClose("section");
    },
    // 修改部门
    editSection: function () {
      this._getDept(this.queryParams.deptId).then((response) => {
        Object.assign(this.sectionEdit, response);
        this.sectionEdit.deptName = response.deptName;
        this.openeditsection = true;
        this.title = "修改部门";
      });
    },
    // 修改部门表单提交
    submitESection: function () {
      this.$refs["sectioned"].validate((valid) => {
        if (valid) {
          //this.section.deptId = this.queryParams.deptId;
          updateDept(this.sectionEdit).then((response) => {
            if (response.code === 200) {
              this.msgSuccess("修改部门成功");
              this.openeditsection = false;
              this.setBtnStatus(true, true);
              this.queryParams.deptId = undefined;
              this.getList();
              this.getTreeselect();
            }
          });
        }
      });
    },
    // 取消修改部门
    cancelESection: function () {
      this.openeditsection = false;
      this.setBtnStatus(true, true);
      this.sectionClose("sectioned");
      this.queryParams.deptId = undefined;
    },
    // ×号重置选中部门编号与校验规则
    sectionClose: function (el) {
      this.queryParams.deptId = undefined;
      if (el) {
        try {
          this.$refs[el].clearValidate();
          this.setBtnStatus(true, true);
        } catch (error) {
          console.log(error);
        }
      }
    },
    _getDept: function (deptId) {
      return new Promise((resolve, reject) => {
        getDept(this.queryParams.deptId).then((response) => {
          resolve(response.data);
        });
      });
    },
    delSection: function () {
      const deptId = this.queryParams.deptId;
      this.$confirm(
        '是否确认删除部门为"' + this.parentName + '"的数据项?',
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delDept(deptId);
        })
        .then(() => {
          this.msgSuccess("删除成功");
          this.queryParams.deptId = undefined;
          this.setBtnStatus(true, true);
          //this.section.deptName = undefined;
          this.getList();
          this.getTreeselect();
        })
        .catch(() => {
          this.queryParams.deptId = undefined;
          this.setBtnStatus(true, true);
        });
    },
    // 判断是否为顶级或公司账号
    isTopAccounts: function () {
      if (
        this.queryParams.deptId == this.$store.state.user.deptId ||
        Object.prototype.toString.call(this.queryParams.deptId) ==
          "[object Undefined]"
      ) {
        return true;
      }
      return false;
    },
    // 修改部门按钮、删除部门按钮状态
    setBtnStatus(edit, del) {
      this.editsection = edit;
      this.deletesection = del;
    },
    // 删除按钮操作
    handleDelete(row) {
      const userIds = row.userId || this.ids;
      const customerName = this.idsss;
      this.$confirm(
        '是否确认删除用户名称为"' + customerName + '"的数据项?',
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delUser(userIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
    // 导出按钮操作
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有用户数据项?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportUser(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        })
        .catch(function () {});
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
    // 获取选中部门对应的角色
    getRoles(node) {
      let data = { deptId: node.id };
      getRolesByDeptId(data).then((response) => {
        this.roleOptions = response.rows;
      });
    },
  },
};
</script>
<style  scoped lang="scss">
.btn-groups {
  margin-bottom: 20px;
  .nomargin {
    margin-left: 0;
    margin-right: 0;
  }
  .elBtn:not(:last-child) {
    border-right-style: solid !important;
    border-right-width: 2px !important;
    border-right-color: #fff !important;
  }
}
// /deep/ .el-tree-node__label {
//   display: block;
//   width: 100%;
//   text-overflow: ellipsis;
//   overflow: hidden;
//   white-space: nowrap;
//   padding: 2px 0;
// }
// /deep/ .el-tree-node__label:hover {
//   display: block;
//   width: 100%;
//   overflow: auto;
//   white-space: normal;
// }
.span-ellipsis {
  width: 100%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  display: block;
  font-size: 14px;
}
</style>
