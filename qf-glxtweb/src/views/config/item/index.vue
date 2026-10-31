<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--菜单数据-->
      <el-col :span="4" :xs="24">
        <div class="card-config" :style="{height: tableHeight + 'px'}">
          <div class="treeItems">
            <ul>
              <li v-for="item in list">
                <div @click="handleConfig(item.id)">
                  <div v-if="item.id === showType" class="treeItem treeItemSub active">
                    <span>{{ item.name }}</span>
                  </div>
                  <div v-else class="treeItem treeItemSub">
                    <span>{{ item.name }}</span>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </el-col>
      <!--表格数据-->
      <el-col :span="20" :xs="24">
        <div v-if="showType === 21">
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-input
                v-model="queryParams.name"
                placeholder="请输入搜索内容"
                clearable
                size="small"
                prefix-icon="el-icon-search"
              />
            </el-col>
            <el-col :span="1.5">
              <el-button size="small" @click="handleQuery">搜索</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="primary" size="small" @click="handleAdd('新增物品类型')">新增物品类型</el-button>
            </el-col>
          </el-row>
          <el-table v-loading="loading" size="small" :data="dataList21" stripe border :key="showType">
            <el-table-column>
              <template slot="header" slot-scope="scope">
                <span>物品类型管理</span>
                <div style="float: right; margin-right: 35px" >
                  <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleQuery">
                    <div title="刷新" style="font-size: 18px">
                      <i class="el-icon-refresh"></i>
                    </div>
                  </el-button>
                  <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;">
                    <el-popover placement="bottom" title="筛选列" trigger="click" width="50">
                      <el-checkbox-group v-model="checkedColumns" size="small" @change="changeColumns">
                        <el-checkbox v-for="item in checkBoxGroup21" :label="item.label" :key="item.prop"></el-checkbox>
                      </el-checkbox-group>
                      <div title="筛选列" slot="reference" style="font-size: 18px">
                        <i class="el-icon-s-operation"></i>
                      </div>
                    </el-popover>
                  </el-button>
                  <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleExport">
                    <div title="导出" style="font-size: 18px">
                      <i class="el-icon-receiving"></i>
                    </div>
                  </el-button>
                  <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handlePrint">
                    <div title="打印" style="font-size: 18px">
                      <i class="el-icon-printer"></i>
                    </div>
                  </el-button>
                </div>
              </template>
              <el-table-column width="100px" type="index" align="center" label="序号" sortable/>
              <el-table-column v-if="showColumn('name')" label="物品类型名称" align="center" prop="name"/>
              <el-table-column v-if="showColumn('details')" label="所属类型" align="center" prop="details"/>
              <el-table-column v-if="showColumn('sort')" label="排序" align="center" prop="sort" width="200" sortable/>
              <el-table-column v-if="showColumn('createTime')" label="创建时间" align="center" prop="createTime" width="300" sortable/>
              <el-table-column label="操作" align="center" width="250">
                <template slot-scope="scope">
                  <el-button type="primary" size="small" @click="handleUpdate(scope.row, '编辑物品类型')">编辑</el-button>
                  <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table-column>
          </el-table>
          <pagination
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="handleQuery"
          />
        </div>
        <div v-if="showType === 22">
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-input
                v-model="queryParams.name"
                placeholder="请输入搜索内容"
                clearable
                size="small"
                prefix-icon="el-icon-search"
              />
            </el-col>
            <el-col :span="1.5">
              <el-select v-model="queryParams.details" placeholder="请选择物品类型" clearable size="small">
                <el-option
                  v-for="item in itemTypeData"
                  :key="item.id + ''"
                  :label="item.name"
                  :value="item.id + ''"
                />
              </el-select>
            </el-col>
            <el-col :span="1.5">
              <el-button size="small" @click="handleQuery">搜索</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="primary" size="small" @click="handleAdd('新增物品名称')">新增物品名称</el-button>
            </el-col>
          </el-row>
          <el-table v-loading="loading" size="small" :data="dataList22" stripe border :key="showType">
            <el-table-column>
              <template slot="header" slot-scope="scope">
                <span>物品名称管理</span>
                <div style="float: right; margin-right: 35px" >
                  <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleQuery">
                    <div title="刷新" style="font-size: 18px">
                      <i class="el-icon-refresh"></i>
                    </div>
                  </el-button>
                  <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;">
                    <el-popover placement="bottom" title="筛选列" trigger="click" width="50">
                      <el-checkbox-group v-model="checkedColumns" size="small" @change="changeColumns">
                        <el-checkbox v-for="item in checkBoxGroup22" :label="item.label" :key="item.prop"></el-checkbox>
                      </el-checkbox-group>
                      <div title="筛选列" slot="reference" style="font-size: 18px">
                        <i class="el-icon-s-operation"></i>
                      </div>
                    </el-popover>
                  </el-button>
                  <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleExport">
                    <div title="导出" style="font-size: 18px">
                      <i class="el-icon-receiving"></i>
                    </div>
                  </el-button>
                  <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handlePrint">
                    <div title="打印" style="font-size: 18px">
                      <i class="el-icon-printer"></i>
                    </div>
                  </el-button>
                </div>
              </template>
              <el-table-column width="100px" type="index" align="center" label="序号" sortable/>
              <el-table-column v-if="showColumn('name')" label="物品名称" align="center" prop="name"/>
              <el-table-column v-if="showColumn('unit')" label="单位" align="center" prop="unit"/>
              <el-table-column v-if="showColumn('details')" label="所属类型" align="center" prop="details"/>
              <el-table-column v-if="showColumn('sort')" label="排序" align="center" prop="sort" width="200" sortable/>
              <el-table-column v-if="showColumn('createTime')" label="创建时间" align="center" prop="createTime" width="300" sortable/>
              <el-table-column label="操作" align="center" width="250">
                <template slot-scope="scope">
                  <el-button type="primary" size="small" @click="handleUpdate(scope.row, '编辑物品名称')">编辑</el-button>
                  <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table-column>
          </el-table>
          <pagination
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="handleQuery"
          />
        </div>
        <div v-if="showType === 23">
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-input
                v-model="queryParams.name"
                placeholder="请输入搜索内容"
                clearable
                size="small"
                prefix-icon="el-icon-search"
              />
            </el-col>
            <el-col :span="1.5">
              <el-button size="small" @click="handleQuery">搜索</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="primary" size="small" @click="handleAdd('新增物品库位置')">新增物品库位置</el-button>
            </el-col>
          </el-row>
          <el-table v-loading="loading" size="small" :data="dataList23" stripe border :key="showType">
            <el-table-column>
              <template slot="header" slot-scope="scope">
                <span>物品库位置管理</span>
                <div style="float: right; margin-right: 35px" >
                  <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleQuery">
                    <div title="刷新" style="font-size: 18px">
                      <i class="el-icon-refresh"></i>
                    </div>
                  </el-button>
                  <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;">
                    <el-popover placement="bottom" title="筛选列" trigger="click" width="50">
                      <el-checkbox-group v-model="checkedColumns" size="small" @change="changeColumns">
                        <el-checkbox v-for="item in checkBoxGroup23" :label="item.label" :key="item.prop"></el-checkbox>
                      </el-checkbox-group>
                      <div title="筛选列" slot="reference" style="font-size: 18px">
                        <i class="el-icon-s-operation"></i>
                      </div>
                    </el-popover>
                  </el-button>
                  <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleExport">
                    <div title="导出" style="font-size: 18px">
                      <i class="el-icon-receiving"></i>
                    </div>
                  </el-button>
                  <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handlePrint">
                    <div title="打印" style="font-size: 18px">
                      <i class="el-icon-printer"></i>
                    </div>
                  </el-button>
                </div>
              </template>
              <el-table-column width="100px" type="index" align="center" label="序号" sortable/>
              <el-table-column v-if="showColumn('name')" label="物品库位置名称" align="center" prop="name"/>
              <el-table-column v-if="showColumn('sort')" label="排序" align="center" prop="sort" width="200" sortable/>
              <el-table-column v-if="showColumn('createTime')" label="创建时间" align="center" prop="createTime" width="300" sortable/>
              <el-table-column label="操作" align="center" width="250">
                <template slot-scope="scope">
                  <el-button type="primary" size="small" @click="handleUpdate(scope.row, '编辑物品库位置')">编辑</el-button>
                  <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table-column>
          </el-table>
          <pagination
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="handleQuery"
          />
        </div>
        <div v-if="showType === 24">
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-input
                v-model="queryParams.name"
                placeholder="请输入搜索内容"
                clearable
                size="small"
                prefix-icon="el-icon-search"
              />
            </el-col>
            <el-col :span="1.5">
              <el-button size="small" @click="handleQuery">搜索</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="primary" size="small" @click="handleAdd('新增物品出库方式')">新增物品出库方式</el-button>
            </el-col>
          </el-row>
          <el-table v-loading="loading" size="small" :data="dataList24" stripe border :key="showType">
            <el-table-column>
              <template slot="header" slot-scope="scope">
                <span>物品出库方式管理</span>
                <div style="float: right; margin-right: 35px" >
                  <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleQuery">
                    <div title="刷新" style="font-size: 18px">
                      <i class="el-icon-refresh"></i>
                    </div>
                  </el-button>
                  <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;">
                    <el-popover placement="bottom" title="筛选列" trigger="click" width="160">
                      <el-checkbox-group v-model="checkedColumns" size="small" @change="changeColumns">
                        <el-checkbox v-for="item in checkBoxGroup24" :label="item.label" :key="item.prop"></el-checkbox>
                      </el-checkbox-group>
                      <div title="筛选列" slot="reference" style="font-size: 18px">
                        <i class="el-icon-s-operation"></i>
                      </div>
                    </el-popover>
                  </el-button>
                  <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleExport">
                    <div title="导出" style="font-size: 18px">
                      <i class="el-icon-receiving"></i>
                    </div>
                  </el-button>
                  <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handlePrint">
                    <div title="打印" style="font-size: 18px">
                      <i class="el-icon-printer"></i>
                    </div>
                  </el-button>
                </div>
              </template>
              <el-table-column width="100px" type="index" align="center" label="序号" sortable/>
              <el-table-column v-if="showColumn('name')" label="物品出库方式名称" align="center" prop="name"/>
              <el-table-column v-if="showColumn('sort')" label="排序" align="center" prop="sort" width="200" sortable/>
              <el-table-column v-if="showColumn('createTime')" label="创建时间" align="center" prop="createTime" width="300" sortable/>
              <el-table-column label="操作" align="center" width="250">
                <template slot-scope="scope">
                  <el-button type="primary" size="small" @click="handleUpdate(scope.row, '编辑物品出库方式')">编辑</el-button>
                  <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table-column>
          </el-table>
          <pagination
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="handleQuery"
          />
        </div>
      </el-col>
    </el-row>
    <!--弹框数据-->
    <el-dialog :title="showTitle" :visible.sync="open" width="700px" append-to-body :close-on-click-modal="false" :before-close="cancel">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="24" v-if="showType === 21">
            <el-form-item label="物品类型名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入物品类型名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showType === 21">
            <el-form-item label="所属类型" prop="details">
              <el-input v-model="form.details" placeholder="请输入所属类型" />
            </el-form-item>
          </el-col>

          <el-col :span="24" v-if="showType === 22">
            <el-form-item label="物品名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入物品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showType === 22">
            <el-form-item label="单位" prop="unit">
              <el-input v-model="form.unit" placeholder="请输入单位" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showType === 22">
            <el-form-item label="所属类型" prop="details">
              <el-select v-model="form.details" placeholder="请选择物品类型" filterable>
                <el-option
                  v-for="item in itemTypeData"
                  :key="item.id + ''"
                  :label="item.name"
                  :value="item.id + ''"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24" v-if="showType === 23">
            <el-form-item label="物品库位置名称" prop="name">
              <el-input v-model="form.name" placeholder="物品库位置名称" />
            </el-form-item>
          </el-col>

          <el-col :span="24" v-if="showType === 24">
            <el-form-item label="物品出库方式名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入物品出库方式名称" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="排序" prop="sort">
              <el-input-number v-model="form.sort" controls-position="right" :min="1"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" type="primary" @click="submitForm()">提 交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { selectUserConfigList, getUserConfigList, selectUserConfigById, addUserConfig, updateUserConfig, delUserConfig, exportUserConfig } from "@/api/system/userConfig";
  import printJS from 'print-js'

  export default {
    name: "SystemConfig",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 总条数
        total: 0,
        // 表格数据
        list: [],
        // 物品类型列表数据
        itemTypeData: [],
        // 选择展示的列
        checkedColumns: [],
        // 物品类型表格数据
        dataList21: [],
        checkBoxGroup21: [
          { label: '物品类型名称', prop: 'name', isShow: true },
          { label: '所属类型', prop: 'details', isShow: true },
          { label: '排序', prop: 'sort', isShow: true },
          { label: '创建时间', prop: 'createTime', isShow: true },
        ],
        // 物品名称表格数据
        dataList22: [],
        checkBoxGroup22: [
          { label: '物品名称', prop: 'name', isShow: true },
          { label: '单位', prop: 'unit', isShow: true },
          { label: '所属类型', prop: 'details', isShow: true },
          { label: '排序', prop: 'sort', isShow: true },
          { label: '创建时间', prop: 'createTime', isShow: true },
        ],
        // 物品库位置表格数据
        dataList23: [],
        checkBoxGroup23: [
          { label: '物品库位置名称', prop: 'name', isShow: true },
          { label: '排序', prop: 'sort', isShow: true },
          { label: '创建时间', prop: 'createTime', isShow: true },
        ],
        // 物品出库方式表格数据
        dataList24: [],
        checkBoxGroup24: [
          { label: '物品出库方式名称', prop: 'name', isShow: true },
          { label: '排序', prop: 'sort', isShow: true },
          { label: '创建时间', prop: 'createTime', isShow: true },
        ],

        // 是否显示弹出层
        open: false,
        // 表单参数
        form: {
          sort: 1
        },
        // 查询参数
        queryParams: {
          name: '',
          details: '',
          pageNum: 1,
          pageSize: 10,
        },
        // 表单校验
        rules: {
          name: [
            { required: true, message: "必填项不能为空", trigger: "blur" },
          ],
          sort: [
            { required: true, message: "顺序不能为空", trigger: "blur" },
          ],
          details: [
            { required: true, message: '类型不能为空', trigger: 'change' }
          ],
        },
        showType: 21,
        showTitle: '',
        tableHeight: window.innerHeight - 130,
      };
    },

    created() {
      this.getList();
      this.handleConfig(this.showType);
    },
    mounted() {
      this.handleResize();
      window.addEventListener('resize', this.handleResize)
    },
    beforeDestroy() {
      window.removeEventListener('resize', this.handleResize)
    },
    methods: {
      handleResize() {
        this.tableHeight = window.innerHeight - 130
      },
      /** 系统配置列表 */
      getList() {
        this.loading = true;
        this.list = [
          {id: 21, name: '物品类型管理', details: '应用在：各类信息登记 -> 如一般纳税人、小规模、个体户查账征收'},
          {id: 22, name: '物品名称管理', details: '应用在：各类信息登记 -> 如一般纳税人、小规模、个体户查账征收'},
          {id: 23, name: '物品库位置管理', details: '应用在：各类信息登记 -> 如一般纳税人、小规模、个体户查账征收'},
          {id: 24, name: '物品出库方式管理', details: '应用在：各类信息登记 -> 如一般纳税人、小规模、个体户查账征收'},
        ]
        this.loading = false;
      },
      /** 切换配置 */
      handleConfig(type) {
        this.showType = type;
        this.handleQuery()
      },
      /** 查询 */
      handleQuery() {
        this.loading = true;
        if (this.showType === 21) {
          this.queryParams.type = this.showType
          selectUserConfigList(this.queryParams).then(res => {
            this.dataList21 = res.rows
            this.total = res.total
            this.loading = false
          })
          // 气泡框出现
          this.checkedColumns = []
          this.checkBoxGroup21.forEach(item => {
            if (item.isShow) {
              this.checkedColumns.push(item.label)
            }
          })
        }
        if (this.showType === 22) {
          this.queryParams.type = this.showType
          selectUserConfigList(this.queryParams).then(res => {
            this.dataList22 = res.rows
            this.total = res.total
            this.loading = false
          })
          this.getUserConfigList(21)
          // 气泡框出现
          this.checkedColumns = []
          this.checkBoxGroup22.forEach(item => {
            if (item.isShow) {
              this.checkedColumns.push(item.label)
            }
          })
        }
        if (this.showType === 23) {
          this.queryParams.type = this.showType
          selectUserConfigList(this.queryParams).then(res => {
            this.dataList23 = res.rows
            this.total = res.total
            this.loading = false
          })
          // 气泡框出现
          this.checkedColumns = []
          this.checkBoxGroup23.forEach(item => {
            if (item.isShow) {
              this.checkedColumns.push(item.label)
            }
          })
        }
        if (this.showType === 24) {
          this.queryParams.type = this.showType
          selectUserConfigList(this.queryParams).then(res => {
            this.dataList24 = res.rows
            this.total = res.total
            this.loading = false
          })
          // 气泡框出现
          this.checkedColumns = []
          this.checkBoxGroup24.forEach(item => {
            if (item.isShow) {
              this.checkedColumns.push(item.label)
            }
          })
        }
      },
      /** 选择列 */
      changeColumns(val) {
        if (this.showType === 21) {
          this.checkBoxGroup21.forEach(item => {
            item.isShow = false;
          })
          // columns将val数组存在的值设为true，不存在的设为false
          val.forEach(item => {
            let current = this.checkBoxGroup21.find(i => i.label === item)
            current.isShow = true;
          })
        }
        if (this.showType === 22) {
          this.checkBoxGroup22.forEach(item => {
            item.isShow = false;
          })
          // columns将val数组存在的值设为true，不存在的设为false
          val.forEach(item => {
            let current = this.checkBoxGroup22.find(i => i.label === item)
            current.isShow = true;
          })
        }
        if (this.showType === 23) {
          this.checkBoxGroup23.forEach(item => {
            item.isShow = false;
          })
          // columns将val数组存在的值设为true，不存在的设为false
          val.forEach(item => {
            let current = this.checkBoxGroup23.find(i => i.label === item)
            current.isShow = true;
          })
        }
        if (this.showType === 24) {
          this.checkBoxGroup24.forEach(item => {
            item.isShow = false;
          })
          // columns将val数组存在的值设为true，不存在的设为false
          val.forEach(item => {
            let current = this.checkBoxGroup24.find(i => i.label === item)
            current.isShow = true;
          })
        }

        this.reload = Math.random();
      },
      // 表格列是否显示的方法
      showColumn(currentColumn) {
        if (this.showType === 21) {
          return this.checkBoxGroup21.find(item => item.prop === currentColumn).isShow;
        }
        if (this.showType === 22) {
          return this.checkBoxGroup22.find(item => item.prop === currentColumn).isShow;
        }
        if (this.showType === 23) {
          return this.checkBoxGroup23.find(item => item.prop === currentColumn).isShow;
        }
        if (this.showType === 24) {
          return this.checkBoxGroup24.find(item => item.prop === currentColumn).isShow;
        }
      },

      /** 新增按钮操作 */
      handleAdd(title) {
        this.open = true;
        this.showTitle = title
        if (this.showType === 22) {
          this.getUserConfigList(21)
        }
      },
      /** 修改按钮操作 */
      handleUpdate(row, title) {
        selectUserConfigById(row.id).then((response) => {
          this.form = response.data;
          this.open = true;
          this.showTitle = title;
        });
        if (this.showType === 22) {
          this.getUserConfigList(21)
        }
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate((valid) => {
          if (valid) {
            this.form.type = this.showType

            if (this.form.id !== undefined) {
              updateUserConfig(this.form).then((response) => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.cancel();
                  this.handleQuery();
                }
              });
            } else {
              addUserConfig(this.form).then((response) => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.cancel();
                  this.handleQuery();
                }
              });
            }
          }
        });
      },
      /** 取消按钮 */
      cancel() {
        this.open = false;
        this.reset();
      },
      /** 表单重置 */
      reset() {
        this.form = {
          sort: 1
        }
        this.resetForm("form");
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        this.$confirm('是否确认删除该记录?', "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delUserConfig(row.id);
        }).then(() => {
          this.handleQuery();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = {type: this.showType}
        this.$confirm('是否确认导出配置?', "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportUserConfig(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      },
      /** 打印按钮操作 */
      handlePrint() {
        if (this.showType === 21) {
          let data = []
          for (let i=0; i < this.dataList21.length; i++) {
            data.push({
              field1: this.dataList21[i].name,
              field2: this.dataList21[i].details,
              field3: this.dataList21[i].sort,
              field4: this.dataList21[i].createTime,
            })
          }
          printJS({
            type: 'json',
            header: '',
            headerStyle: 'text-align: center',
            // 打印的数据
            printable: data,
            // 表头名称(必传)，要和json中的键值对的键保持一致
            properties: [
              {field: 'field1', displayName: '物品类型名称', columnSize: 1},
              {field: 'field2', displayName: '所属类型', columnSize: 1},
              {field: 'field3', displayName: '排序', columnSize: 1},
              {field: 'field4', displayName: '创建时间', columnSize: 1}
            ],
            // 表头的自定义样式设置
            gridHeaderStyle: 'font-weight: 500; border: 1px solid lightgray; font-size: 18px; line-height: 32px;',
            // 表格的自定义样式设置
            gridStyle: 'text-align: center; border: 1px solid lightgray; font-size: 15px; line-height: 30px;',
          })
        }
        if (this.showType === 22) {
          let data = []
          for (let i=0; i < this.dataList22.length; i++) {
            data.push({
              field1: this.dataList22[i].name,
              field2: this.dataList22[i].unit,
              field3: this.dataList22[i].details,
              field4: this.dataList22[i].sort,
              field5: this.dataList22[i].createTime,
            })
          }
          printJS({
            type: 'json',
            header: '',
            headerStyle: 'text-align: center',
            // 打印的数据
            printable: data,
            // 表头名称(必传)，要和json中的键值对的键保持一致
            properties: [
              {field: 'field1', displayName: '物品名称', columnSize: 1},
              {field: 'field2', displayName: '单位', columnSize: 1},
              {field: 'field3', displayName: '所属类型', columnSize: 1},
              {field: 'field4', displayName: '排序', columnSize: 1},
              {field: 'field5', displayName: '创建时间', columnSize: 1}
            ],
            // 表头的自定义样式设置
            gridHeaderStyle: 'font-weight: 500; border: 1px solid lightgray; font-size: 18px; line-height: 32px;',
            // 表格的自定义样式设置
            gridStyle: 'text-align: center; border: 1px solid lightgray; font-size: 15px; line-height: 30px;',
          })
        }
        if (this.showType === 23) {
          let data = []
          for (let i=0; i < this.dataList23.length; i++) {
            data.push({
              field1: this.dataList23[i].name,
              field2: this.dataList23[i].sort,
              field3: this.dataList23[i].createTime,
            })
          }
          printJS({
            type: 'json',
            header: '',
            headerStyle: 'text-align: center',
            // 打印的数据
            printable: data,
            // 表头名称(必传)，要和json中的键值对的键保持一致
            properties: [
              {field: 'field1', displayName: '物品库位置名称', columnSize: 1},
              {field: 'field2', displayName: '排序', columnSize: 1},
              {field: 'field3', displayName: '创建时间', columnSize: 1}
            ],
            // 表头的自定义样式设置
            gridHeaderStyle: 'font-weight: 500; border: 1px solid lightgray; font-size: 18px; line-height: 32px;',
            // 表格的自定义样式设置
            gridStyle: 'text-align: center; border: 1px solid lightgray; font-size: 15px; line-height: 30px;',
          })
        }
        if (this.showType === 24) {
          let data = []
          for (let i=0; i < this.dataList24.length; i++) {
            data.push({
              field1: this.dataList24[i].name,
              field2: this.dataList24[i].sort,
              field3: this.dataList24[i].createTime,
            })
          }
          printJS({
            type: 'json',
            header: '',
            headerStyle: 'text-align: center',
            // 打印的数据
            printable: data,
            // 表头名称(必传)，要和json中的键值对的键保持一致
            properties: [
              {field: 'field1', displayName: '物品出库方式名称', columnSize: 1},
              {field: 'field2', displayName: '排序', columnSize: 1},
              {field: 'field3', displayName: '创建时间', columnSize: 1}
            ],
            // 表头的自定义样式设置
            gridHeaderStyle: 'font-weight: 500; border: 1px solid lightgray; font-size: 18px; line-height: 32px;',
            // 表格的自定义样式设置
            gridStyle: 'text-align: center; border: 1px solid lightgray; font-size: 15px; line-height: 30px;',
          })
        }
      },
      /** 查询部门列表 */
      getUserConfigList (type) {
        getUserConfigList({type: type}).then(response => {
          this.itemTypeData = response.data;
        });
      },
    }
  };
</script>

<style lang="scss" scoped>
  .card-header {
    position: relative;
    height: 42px;
    line-height: 42px;
    padding: 0 15px;
    border-bottom: 1px solid #f6f6f6;
    color: #333;
    border-radius: 2px 2px 0 0;
    font-size: 14px;
  }
  .card-config {
    min-height: 200px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    display: block;
    font-size: 14px;
    overflow-y: auto;
    border: 1px solid #f6f6f6;
    background-color: #f6f6f6;
  }
  ul, ol {
    list-style: none;
    margin: 0;
    padding: 0;
  }
  .treeItem:hover, .treeItem.active {
    color: #ffffff;
    background-color: #7dbbfd;
    /*background-color: #c6eefd;*/
    /*background-color: #69d3fa;*/
  }
  .treeItem {
    position: relative;
    height: 40px;
    line-height: 40px;
    cursor: pointer;
  }
  .treeItem.active:before {
    content: " ";
    position: absolute;
    left: 0;
    width: 5px;
    height: 24px;
    margin: 8px;
    z-index: 5;
    background-color: #4265D6;
  }
  .treeItemSub {
    padding-left: 20px;
  }
  ::v-deep .el-transfer__buttons {
    display: inline-block;
    vertical-align: middle;
    padding: 0 5px;
  }
  ::v-deep .el-checkbox.el-transfer-panel__item {
    display: block;
  }
</style>
