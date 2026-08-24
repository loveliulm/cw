<template>
  <div class="app-container">
    <div v-show="showType === 0">
      <!--    <div class="card-header">系统配置</div>-->
      <el-table v-loading="loading" :data="list" stripe border :max-height="tableHeight">
        <el-table-column label="系统配置">
          <el-table-column width="100px" type="index" align="center" label="序号"/>
          <el-table-column label="配置分类" align="left" prop="name" width="400"/>
          <el-table-column label="说明" align="left" prop="details"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
            <template slot-scope="scope">
              <el-button type="primary" size="small" @click="handleConfig(scope.row.id)">配置</el-button>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table>
    </div>

    <div v-if="showType === 1">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" size="small" @click="handleAdd('新增客户来源')">新增客户来源</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button size="small" @click="goBack">返回</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="dataList1" stripe border>
        <el-table-column>
          <template slot="header" slot-scope="scope">
            <span>客户来源配置</span>
            <div style="float: right; margin-right: 35px" >
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleQuery">
                <div title="刷新" style="font-size: 18px">
                  <i class="el-icon-refresh"></i>
                </div>
              </el-button>
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;">
                <el-popover placement="bottom" title="筛选列" trigger="click" width="50">
                  <el-checkbox-group v-model="checkedColumns" size="small" @change="changeColumns">
                    <el-checkbox v-for="item in checkBoxGroup1" :label="item.label" :key="item.prop"></el-checkbox>
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
          <el-table-column v-if="showColumn('name')" label="客户来源名称" align="center" prop="name"/>
          <el-table-column v-if="showColumn('sort')" label="排序" align="center" prop="sort" width="200" sortable/>
          <el-table-column v-if="showColumn('createTime')" label="创建时间" align="center" prop="createTime" width="300" sortable/>
          <el-table-column label="操作" align="center" width="250">
            <template slot-scope="scope">
              <el-button type="primary" size="small" @click="handleUpdate(scope.row, '编辑客户来源')">编辑</el-button>
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
    <div v-if="showType === 2">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" size="small" @click="handleAdd('新增日记账分类')">新增日记账分类</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button size="small" @click="goBack">返回</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="dataList2" stripe border>
        <el-table-column>
          <template slot="header" slot-scope="scope">
            <span>日记账分类配置</span>
            <div style="float: right; margin-right: 35px" >
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleQuery">
                <div title="刷新" style="font-size: 18px">
                  <i class="el-icon-refresh"></i>
                </div>
              </el-button>
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;">
                <el-popover placement="bottom" title="筛选列" trigger="click" width="50">
                  <el-checkbox-group v-model="checkedColumns" size="small" @change="changeColumns">
                    <el-checkbox v-for="item in checkBoxGroup2" :label="item.label" :key="item.prop"></el-checkbox>
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
          <el-table-column v-if="showColumn('name')" label="日记账分类名称" align="center" prop="name"/>
          <el-table-column v-if="showColumn('sort')" label="排序" align="center" prop="sort" width="200" sortable/>
          <el-table-column v-if="showColumn('createTime')" label="创建时间" align="center" prop="createTime" width="300" sortable/>
          <el-table-column label="操作" align="center" width="250">
            <template slot-scope="scope">
              <el-button type="primary" size="small" @click="handleUpdate(scope.row, '编辑日记账分类')">编辑</el-button>
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
    <div v-if="showType === 3">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" size="small" @click="handleAdd('新增日记账收支方式')">新增日记账收支方式</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button size="small" @click="goBack">返回</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="dataList3" stripe border>
        <el-table-column>
          <template slot="header" slot-scope="scope">
            <span>日记账收支方式配置</span>
            <div style="float: right; margin-right: 35px" >
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleQuery">
                <div title="刷新" style="font-size: 18px">
                  <i class="el-icon-refresh"></i>
                </div>
              </el-button>
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;">
                <el-popover placement="bottom" title="筛选列" trigger="click" width="50">
                  <el-checkbox-group v-model="checkedColumns" size="small" @change="changeColumns">
                    <el-checkbox v-for="item in checkBoxGroup3" :label="item.label" :key="item.prop"></el-checkbox>
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
          <el-table-column v-if="showColumn('name')" label="日记账收支方式名称" align="center" prop="name"/>
          <el-table-column v-if="showColumn('isCost')" label="是否属于成本" align="center" prop="isCost">
            <template slot-scope="scope">
              <span v-if="scope.row.isCost === 0">否</span>
              <span v-if="scope.row.isCost === 1">是</span>
            </template>
          </el-table-column>
          <el-table-column v-if="showColumn('sort')" label="排序" align="center" prop="sort" width="200" sortable/>
          <el-table-column v-if="showColumn('createTime')" label="创建时间" align="center" prop="createTime" width="300" sortable/>
          <el-table-column label="操作" align="center" width="250">
            <template slot-scope="scope">
              <el-button type="primary" size="small" @click="handleUpdate(scope.row, '编辑日记账收支方式')">编辑</el-button>
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
    <div v-if="showType === 4">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" size="small" @click="handleAdd('新增老板信息')">新增老板信息</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button size="small" @click="goBack">返回</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="dataList4" stripe border>
        <el-table-column>
          <template slot="header" slot-scope="scope">
            <span>老板信息配置</span>
            <div style="float: right; margin-right: 35px" >
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleQuery">
                <div title="刷新" style="font-size: 18px">
                  <i class="el-icon-refresh"></i>
                </div>
              </el-button>
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;">
                <el-popover placement="bottom" title="筛选列" trigger="click" width="50">
                  <el-checkbox-group v-model="checkedColumns" size="small" @change="changeColumns">
                    <el-checkbox v-for="item in checkBoxGroup4" :label="item.label" :key="item.prop"></el-checkbox>
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
          <el-table-column v-if="showColumn('name')" label="姓名" align="center" prop="name"/>
          <el-table-column v-if="showColumn('age')" label="年龄" align="center" prop="age"/>
          <el-table-column v-if="showColumn('sex')" label="性别" align="center" prop="sex">
            <template slot-scope="scope">
              <span v-if="scope.row.sex === 0">男</span>
              <span v-if="scope.row.sex === 1">女</span>
            </template>
          </el-table-column>
          <el-table-column v-if="showColumn('phone')" label="电话" align="center" prop="phone" width="200"/>
          <el-table-column v-if="showColumn('idCard')" label="身份证号" align="center" prop="idCard" width="300"/>
          <el-table-column v-if="showColumn('createTime')" label="创建时间" align="center" prop="createTime" width="300" sortable/>
          <el-table-column label="操作" align="center" width="250">
            <template slot-scope="scope">
              <el-button type="primary" size="small" @click="handleUpdate(scope.row, '编辑老板信息')">编辑</el-button>
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
    <div v-if="showType === 5">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" size="small" @click="handleAdd('新增税务局信息')">新增税务局信息</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button size="small" @click="goBack">返回</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="dataList5" stripe border>
        <el-table-column>
          <template slot="header" slot-scope="scope">
            <span>税务局信息配置</span>
            <div style="float: right; margin-right: 35px" >
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleQuery">
                <div title="刷新" style="font-size: 18px">
                  <i class="el-icon-refresh"></i>
                </div>
              </el-button>
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;">
                <el-popover placement="bottom" title="筛选列" trigger="click" width="50">
                  <el-checkbox-group v-model="checkedColumns" size="small" @change="changeColumns">
                    <el-checkbox v-for="item in checkBoxGroup5" :label="item.label" :key="item.prop"></el-checkbox>
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
          <el-table-column v-if="showColumn('name')" label="税务局名称" align="center" prop="name"/>
          <el-table-column v-if="showColumn('address')" label="税务局地址" align="center" prop="address" width="300"/>
          <el-table-column v-if="showColumn('phone')" label="税务局电话" align="center" prop="phone" width="200"/>
          <el-table-column v-if="showColumn('createTime')" label="创建时间" align="center" prop="createTime" width="300" sortable/>
          <el-table-column label="操作" align="center" width="250">
            <template slot-scope="scope">
              <el-button type="primary" size="small" @click="handleUpdate(scope.row, '编辑税务局信息')">编辑</el-button>
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
    <div v-if="showType === 6">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" size="small" @click="handleAdd('新增工商局信息')">新增工商局信息</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button size="small" @click="goBack">返回</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="dataList6" stripe border>
        <el-table-column>
          <template slot="header" slot-scope="scope">
            <span>工商局信息配置</span>
            <div style="float: right; margin-right: 35px" >
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleQuery">
                <div title="刷新" style="font-size: 18px">
                  <i class="el-icon-refresh"></i>
                </div>
              </el-button>
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;">
                <el-popover placement="bottom" title="筛选列" trigger="click" width="50">
                  <el-checkbox-group v-model="checkedColumns" size="small" @change="changeColumns">
                    <el-checkbox v-for="item in checkBoxGroup6" :label="item.label" :key="item.prop"></el-checkbox>
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
          <el-table-column v-if="showColumn('name')" label="工商局名称" align="center" prop="name"/>
          <el-table-column v-if="showColumn('address')" label="工商局地址" align="center" prop="address" width="300"/>
          <el-table-column v-if="showColumn('phone')" label="工商局电话" align="center" prop="phone" width="200"/>
          <el-table-column v-if="showColumn('createTime')" label="创建时间" align="center" prop="createTime" width="300" sortable/>
          <el-table-column label="操作" align="center" width="250">
            <template slot-scope="scope">
              <el-button type="primary" size="small" @click="handleUpdate(scope.row, '编辑工商局信息')">编辑</el-button>
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
    <div v-if="showType === 7">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" size="small" @click="handleAdd('新增公司/企业类型')">新增公司/企业类型</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button size="small" @click="goBack">返回</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="dataList7" stripe border>
        <el-table-column>
          <template slot="header" slot-scope="scope">
            <span>公司/企业类型配置</span>
            <div style="float: right; margin-right: 35px" >
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleQuery">
                <div title="刷新" style="font-size: 18px">
                  <i class="el-icon-refresh"></i>
                </div>
              </el-button>
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;">
                <el-popover placement="bottom" title="筛选列" trigger="click" width="160">
                  <el-checkbox-group v-model="checkedColumns" size="small" @change="changeColumns">
                    <el-checkbox v-for="item in checkBoxGroup7" :label="item.label" :key="item.prop"></el-checkbox>
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
          <el-table-column v-if="showColumn('name')" label="公司/企业类型名称" align="center" prop="name"/>
          <el-table-column v-if="showColumn('sort')" label="排序" align="center" prop="sort" width="200" sortable/>
          <el-table-column v-if="showColumn('createTime')" label="创建时间" align="center" prop="createTime" width="300" sortable/>
          <el-table-column label="操作" align="center" width="250">
            <template slot-scope="scope">
              <el-button type="primary" size="small" @click="handleUpdate(scope.row, '编辑公司/企业类型')">编辑</el-button>
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
    <div v-if="showType === 8">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" size="small" @click="handleAdd('新增公司客户状态')">新增公司客户状态</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button size="small" @click="goBack">返回</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="dataList8" stripe border>
        <el-table-column>
          <template slot="header" slot-scope="scope">
            <span>公司客户状态配置</span>
            <div style="float: right; margin-right: 35px" >
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleQuery">
                <div title="刷新" style="font-size: 18px">
                  <i class="el-icon-refresh"></i>
                </div>
              </el-button>
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;">
                <el-popover placement="bottom" title="筛选列" trigger="click" width="180">
                  <el-checkbox-group v-model="checkedColumns" size="small" @change="changeColumns">
                    <el-checkbox v-for="item in checkBoxGroup8" :label="item.label" :key="item.prop"></el-checkbox>
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
          <el-table-column v-if="showColumn('name')" label="公司客户状态名称" align="center" prop="name"/>
          <el-table-column v-if="showColumn('isContract')" label="是否属于终止合同选项" align="center" prop="isContract">
            <template slot-scope="scope">
              <span v-if="scope.row.isContract === 0">否</span>
              <span v-if="scope.row.isContract === 1">是</span>
            </template>
          </el-table-column>
          <el-table-column v-if="showColumn('sort')" label="排序" align="center" prop="sort" width="200" sortable/>
          <el-table-column v-if="showColumn('createTime')" label="创建时间" align="center" prop="createTime" width="300" sortable/>
          <el-table-column label="操作" align="center" width="250">
            <template slot-scope="scope">
              <el-button type="primary" size="small" @click="handleUpdate(scope.row, '编辑公司客户状态')">编辑</el-button>
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
    <div v-if="showType === 9">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" size="small" @click="handleAdd('新增税种分类')">新增税种分类</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button size="small" @click="goBack">返回</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="dataList9" stripe border>
        <el-table-column>
          <template slot="header" slot-scope="scope">
            <span>税种分类配置</span>
            <div style="float: right; margin-right: 35px" >
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleQuery">
                <div title="刷新" style="font-size: 18px">
                  <i class="el-icon-refresh"></i>
                </div>
              </el-button>
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;">
                <el-popover placement="bottom" title="筛选列" trigger="click" width="180">
                  <el-checkbox-group v-model="checkedColumns" size="small" @change="changeColumns">
                    <el-checkbox v-for="item in checkBoxGroup9" :label="item.label" :key="item.prop"></el-checkbox>
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
          <el-table-column v-if="showColumn('name')" label="税种分类名称" align="center" prop="name"/>
          <el-table-column v-if="showColumn('sort')" label="排序" align="center" prop="sort" width="200" sortable/>
          <el-table-column v-if="showColumn('createTime')" label="创建时间" align="center" prop="createTime" width="300" sortable/>
          <el-table-column label="操作" align="center" width="250">
            <template slot-scope="scope">
              <el-button type="primary" size="small" @click="handleUpdate(scope.row, '编辑税种分类')">编辑</el-button>
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
    <div v-if="showType === 10">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" size="small" @click="handleAdd('新增税务登记')">新增税务登记</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button size="small" @click="goBack">返回</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="dataList10" stripe border>
        <el-table-column>
          <template slot="header" slot-scope="scope">
            <span>税务登记配置</span>
            <div style="float: right; margin-right: 35px" >
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleQuery">
                <div title="刷新" style="font-size: 18px">
                  <i class="el-icon-refresh"></i>
                </div>
              </el-button>
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;">
                <el-popover placement="bottom" title="筛选列" trigger="click" width="50">
                  <el-checkbox-group v-model="checkedColumns" size="small" @change="changeColumns">
                    <el-checkbox v-for="item in checkBoxGroup10" :label="item.label" :key="item.prop"></el-checkbox>
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
          <el-table-column v-if="showColumn('name')" label="税务名称" align="center" prop="name"/>
          <el-table-column v-if="showColumn('sort')" label="排序" align="center" prop="sort" width="200" sortable/>
          <el-table-column v-if="showColumn('createTime')" label="创建时间" align="center" prop="createTime" width="300" sortable/>
          <el-table-column label="操作" align="center" width="250">
            <template slot-scope="scope">
              <el-button type="primary" size="small" @click="handleUpdate(scope.row, '编辑税务登记')">编辑</el-button>
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
    <div v-if="showType === 11">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" size="small" @click="handleAdd('新增登录身份')">新增登录身份</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button size="small" @click="goBack">返回</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="dataList11" stripe border>
        <el-table-column>
          <template slot="header" slot-scope="scope">
            <span>登录身份配置</span>
            <div style="float: right; margin-right: 35px" >
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;" @click="handleQuery">
                <div title="刷新" style="font-size: 18px">
                  <i class="el-icon-refresh"></i>
                </div>
              </el-button>
              <el-button size="mini" style="width: 28px; height: 28px; padding: 0 0;">
                <el-popover placement="bottom" title="筛选列" trigger="click" width="50">
                  <el-checkbox-group v-model="checkedColumns" size="small" @change="changeColumns">
                    <el-checkbox v-for="item in checkBoxGroup11" :label="item.label" :key="item.prop"></el-checkbox>
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
          <el-table-column v-if="showColumn('name')" label="登录身份名称" align="center" prop="name"/>
          <el-table-column v-if="showColumn('sort')" label="排序" align="center" prop="sort" width="200" sortable/>
          <el-table-column v-if="showColumn('createTime')" label="创建时间" align="center" prop="createTime" width="300" sortable/>
          <el-table-column label="操作" align="center" width="250">
            <template slot-scope="scope">
              <el-button type="primary" size="small" @click="handleUpdate(scope.row, '编辑登录身份')">编辑</el-button>
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

    <el-dialog :title="showTitle" :visible.sync="open" width="700px" append-to-body :close-on-click-modal="false" :before-close="cancel">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="24" v-if="showType === 1">
            <el-form-item label="客户来源" prop="name">
              <el-input v-model="form.name" placeholder="请输入客户来源" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showType === 2">
            <el-form-item label="日记账分类名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入日记账分类名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showType === 3">
            <el-form-item label="日记账收支方式名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入日记账收支方式名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showType === 3">
            <el-form-item label="是否属于成本" prop="isCost">
              <el-radio-group v-model="form.isCost" size="small">
                <el-radio :label=0>否</el-radio>
                <el-radio :label=1>是</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showType === 4">
            <el-form-item label="老板姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入老板姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showType === 4">
            <el-form-item label="老板年龄" prop="age">
              <el-input-number v-model="form.age" controls-position="right" :min="0"/>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showType === 4">
            <el-form-item label="性别" prop="sex">
              <el-radio-group v-model="form.sex" size="small">
                <el-radio :label=0>男</el-radio>
                <el-radio :label=1>女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showType === 4">
            <el-form-item label="电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入电话" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showType === 4">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" placeholder="请输入身份证号" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showType === 4">
            <el-form-item label="选择拥有公司" prop="companyData">
              <el-transfer
                :titles="['公司筛选','选定公司']"
                filterable
                filter-placeholder="关键词搜索"
                v-model="checkCompany"
                :data="companyData"
                :props="{
                  key: 'deptId',
                  label: 'deptName',
                }"
              >
              </el-transfer>
            </el-form-item>
          </el-col>

          <el-col :span="24" v-if="showType === 5">
            <el-form-item label="税务局名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入税务局名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showType === 5">
            <el-form-item label="税务局地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入税务局地址" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showType === 5">
            <el-form-item label="税务局电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入税务局电话" />
            </el-form-item>
          </el-col>

          <el-col :span="24" v-if="showType === 6">
            <el-form-item label="工商局名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入工商局名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showType === 6">
            <el-form-item label="工商局地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入工商局地址" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showType === 6">
            <el-form-item label="工商局电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入工商局电话" />
            </el-form-item>
          </el-col>

          <el-col :span="24" v-if="showType === 7">
            <el-form-item label="公司/企业类型名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入公司/企业类型名称" />
            </el-form-item>
          </el-col>

          <el-col :span="24" v-if="showType === 8">
            <el-form-item label="公司客户状态名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入公司客户状态名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showType === 8">
            <el-form-item label="是否属于终止合同选项" prop="isContract">
              <el-radio-group v-model="form.isContract" size="small">
                <el-radio :label=0>否</el-radio>
                <el-radio :label=1>是</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="24" v-if="showType === 9">
            <el-form-item label="税种分类名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入税种分类名称" />
            </el-form-item>
          </el-col>

          <el-col :span="24" v-if="showType === 10">
            <el-form-item label="税务名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入税务名称" />
            </el-form-item>
          </el-col>

          <el-col :span="24" v-if="showType === 11">
            <el-form-item label="登录身份名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入登录身份名称" />
            </el-form-item>
          </el-col>

          <el-col :span="24" v-if="showType !== 4 && showType !== 5 && showType !== 6">
            <el-form-item label="排序" prop="sort">
              <el-input-number v-model="form.sort" controls-position="right" :min="0"/>
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
  import { selectUserConfigList, selectUserConfigById, addUserConfig, updateUserConfig, delUserConfig, exportUserConfig } from "@/api/system/userConfig";
  import { listDept } from "@/api/system/dept";
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
        // 公司列表数据
        companyData: [],
        // 选中公司数据
        checkCompany: [],
        // 选择展示的列
        checkedColumns: [],
        // 客户来源管理表格数据
        dataList1: [],
        checkBoxGroup1: [
          { label: '客户来源名称', prop: 'name', isShow: true },
          { label: '排序', prop: 'sort', isShow: true },
          { label: '创建时间', prop: 'createTime', isShow: true },
        ],
        // 日记账分类表格数据
        dataList2: [],
        checkBoxGroup2: [
          { label: '分类名称', prop: 'name', isShow: true },
          { label: '排序', prop: 'sort', isShow: true },
          { label: '创建时间', prop: 'createTime', isShow: true },
        ],
        // 日记账收支方式表格数据
        dataList3: [],
        checkBoxGroup3: [
          { label: '收支方式名称', prop: 'name', isShow: true },
          { label: '是否属于成本', prop: 'isCost', isShow: true },
          { label: '排序', prop: 'sort', isShow: true },
          { label: '创建时间', prop: 'createTime', isShow: true },
        ],
        // 老板信息表格数据
        dataList4: [],
        checkBoxGroup4: [
          { label: '姓名', prop: 'name', isShow: true },
          { label: '年龄', prop: 'age', isShow: true },
          { label: '性别', prop: 'sex', isShow: true },
          { label: '电话', prop: 'phone', isShow: true },
          { label: '身份证号', prop: 'idCard', isShow: true },
          { label: '创建时间', prop: 'createTime', isShow: true },
        ],
        // 税务局信息表格数据
        dataList5: [],
        checkBoxGroup5: [
          { label: '税务局名称', prop: 'name', isShow: true },
          { label: '税务局地址', prop: 'address', isShow: true },
          { label: '税务局电话', prop: 'phone', isShow: true },
          { label: '创建时间', prop: 'createTime', isShow: true },
        ],
        // 工商局信息表格数据
        dataList6: [],
        checkBoxGroup6: [
          { label: '工商局名称', prop: 'name', isShow: true },
          { label: '工商局地址', prop: 'address', isShow: true },
          { label: '工商局电话', prop: 'phone', isShow: true },
          { label: '创建时间', prop: 'createTime', isShow: true },
        ],
        // 公司/企业类型表格数据
        dataList7: [],
        checkBoxGroup7: [
          { label: '公司/企业类型名称', prop: 'name', isShow: true },
          { label: '排序', prop: 'sort', isShow: true },
          { label: '创建时间', prop: 'createTime', isShow: true },
        ],
        // 公司客户状态表格数据
        dataList8: [],
        checkBoxGroup8: [
          { label: '公司客户状态名称', prop: 'name', isShow: true },
          { label: '是否属于终止合同选项', prop: 'isContract', isShow: true },
          { label: '排序', prop: 'sort', isShow: true },
          { label: '创建时间', prop: 'createTime', isShow: true },
        ],
        // 税种分类表格数据
        dataList9: [],
        checkBoxGroup9: [
          { label: '税种分类名称', prop: 'name', isShow: true },
          { label: '排序', prop: 'sort', isShow: true },
          { label: '创建时间', prop: 'createTime', isShow: true },
        ],
        // 税种分类表格数据
        dataList10: [],
        checkBoxGroup10: [
          { label: '税务名称', prop: 'name', isShow: true },
          { label: '排序', prop: 'sort', isShow: true },
          { label: '创建时间', prop: 'createTime', isShow: true },
        ],
        // 税种分类表格数据
        dataList11: [],
        checkBoxGroup11: [
          { label: '登录身份名称', prop: 'name', isShow: true },
          { label: '排序', prop: 'sort', isShow: true },
          { label: '创建时间', prop: 'createTime', isShow: true },
        ],

        // 是否显示弹出层
        open: false,
        // 表单参数
        form: {
          isCost: 0,
          sex: 0,
          isContract: 1,
        },
        // 查询参数
        queryParams: {
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
          // phone: [
          //   {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur"}
          // ],
        },
        showType: 0,
        showTitle: '',
        tableHeight: window.innerHeight - 200,
      };
    },

    created() {
      this.getList();
    },
    methods: {
      /** 系统配置列表 */
      getList() {
        this.loading = true;
        this.list = [
          {id: 1, name: '客户来源管理', details: '应用在：客户管理 -> 客户列表 里的客户来源信息'},
          {id: 2, name: '日记账分类管理', details: '应用在：财务管理 -> 日记账管理 里的分类信息。分类为：代理记账、注册地址的时候可以设置下次付费的日期。'},
          {id: 3, name: '日记账收支方式', details: '应用在：财务管理 -> 日记账管理 里的收入方式、支出方式'},
          {id: 4, name: '老板信息管理', details: '应用在：各类信息登记 -> 老板信息 等老板所属公司管理'},
          {id: 5, name: '税务局信息管理', details: '应用在：各类信息登记 -> 税务局信息 等税务局信息列表'},
          {id: 6, name: '工商局信息管理', details: '应用在：各类信息登记 -> 工商局信息 等工商局信息列表'},
          {id: 7, name: '公司/企业类型管理', details: '应用在：各类信息登记 -> 公司/企业类型 等公司/企业类型信息列表'},
          {id: 8, name: '公司客户状态管理', details: '应用在：各类信息登记 -> 公司客户状态 等公司客户状态信息列表'},
          {id: 9, name: '缴税税种名称配置管理', details: '应用在：报税管理 -> 税款缴纳 等税款相关功能的税种名称下拉列表'},
          {id: 10, name: '税务登记信息', details: '应用在：各类信息登记 -> 如一般纳税人、小规模、个体户查账征收'},
          {id: 11, name: '登录身份信息', details: '应用在：各类信息登记 -> 如一般纳税人、小规模、个体户查账征收'},
        ]
        this.loading = false;
      },
      /** 返回 */
      goBack() {
        this.showType = 0;
      },
      /** 切换配置 */
      handleConfig(type) {
        this.showType = type;
        this.handleQuery()
      },
      /** 查询 */
      handleQuery() {
        this.loading = true;
        if (this.showType === 1) {
          this.queryParams.type = this.showType
          selectUserConfigList(this.queryParams).then(res => {
            this.dataList1 = res.rows
            this.total = res.total
          })
          // 气泡框出现
          this.checkedColumns = []
          this.checkBoxGroup1.forEach(item => {
            if (item.isShow) {
              this.checkedColumns.push(item.label)
            }
          })
        }
        if (this.showType === 2) {
          this.queryParams.type = this.showType
          selectUserConfigList(this.queryParams).then(res => {
            this.dataList2 = res.rows
            this.total = res.total
          })
          // 气泡框出现
          this.checkedColumns = []
          this.checkBoxGroup2.forEach(item => {
            if (item.isShow) {
              this.checkedColumns.push(item.label)
            }
          })
        }
        if (this.showType === 3) {
          this.queryParams.type = this.showType
          selectUserConfigList(this.queryParams).then(res => {
            this.dataList3 = res.rows
            this.total = res.total
          })
          // 气泡框出现
          this.checkedColumns = []
          this.checkBoxGroup3.forEach(item => {
            if (item.isShow) {
              this.checkedColumns.push(item.label)
            }
          })
        }
        if (this.showType === 4) {
          this.queryParams.type = this.showType
          selectUserConfigList(this.queryParams).then(res => {
            this.dataList4 = res.rows
            this.total = res.total
          })
          // 气泡框出现
          this.checkedColumns = []
          this.checkBoxGroup4.forEach(item => {
            if (item.isShow) {
              this.checkedColumns.push(item.label)
            }
          })
        }
        if (this.showType === 5) {
          this.queryParams.type = this.showType
          selectUserConfigList(this.queryParams).then(res => {
            this.dataList5 = res.rows
            this.total = res.total
          })
          // 气泡框出现
          this.checkedColumns = []
          this.checkBoxGroup5.forEach(item => {
            if (item.isShow) {
              this.checkedColumns.push(item.label)
            }
          })
        }
        if (this.showType === 6) {
          this.queryParams.type = this.showType
          selectUserConfigList(this.queryParams).then(res => {
            this.dataList6 = res.rows
            this.total = res.total
          })
          // 气泡框出现
          this.checkedColumns = []
          this.checkBoxGroup6.forEach(item => {
            if (item.isShow) {
              this.checkedColumns.push(item.label)
            }
          })
        }
        if (this.showType === 7) {
          this.queryParams.type = this.showType
          selectUserConfigList(this.queryParams).then(res => {
            this.dataList7 = res.rows
            this.total = res.total
          })
          // 气泡框出现
          this.checkedColumns = []
          this.checkBoxGroup7.forEach(item => {
            if (item.isShow) {
              this.checkedColumns.push(item.label)
            }
          })
        }
        if (this.showType === 8) {
          this.queryParams.type = this.showType
          selectUserConfigList(this.queryParams).then(res => {
            this.dataList8 = res.rows
            this.total = res.total
          })
          // 气泡框出现
          this.checkedColumns = []
          this.checkBoxGroup8.forEach(item => {
            if (item.isShow) {
              this.checkedColumns.push(item.label)
            }
          })
        }
        if (this.showType === 9) {
          this.queryParams.type = this.showType
          selectUserConfigList(this.queryParams).then(res => {
            this.dataList9 = res.rows
            this.total = res.total
          })
          // 气泡框出现
          this.checkedColumns = []
          this.checkBoxGroup9.forEach(item => {
            if (item.isShow) {
              this.checkedColumns.push(item.label)
            }
          })
        }
        if (this.showType === 10) {
          this.queryParams.type = this.showType
          selectUserConfigList(this.queryParams).then(res => {
            this.dataList10 = res.rows
            this.total = res.total
          })
          // 气泡框出现
          this.checkedColumns = []
          this.checkBoxGroup10.forEach(item => {
            if (item.isShow) {
              this.checkedColumns.push(item.label)
            }
          })
        }
        if (this.showType === 11) {
          this.queryParams.type = this.showType
          selectUserConfigList(this.queryParams).then(res => {
            this.dataList11 = res.rows
            this.total = res.total
          })
          // 气泡框出现
          this.checkedColumns = []
          this.checkBoxGroup11.forEach(item => {
            if (item.isShow) {
              this.checkedColumns.push(item.label)
            }
          })
        }
        this.loading = false;
      },
      /** 选择列 */
      changeColumns(val) {
        if (this.showType === 1) {
          this.checkBoxGroup1.forEach(item => {
            item.isShow = false;
          })
          // columns将val数组存在的值设为true，不存在的设为false
          val.forEach(item => {
            let current = this.checkBoxGroup1.find(i => i.label === item)
            current.isShow = true;
          })
        }
        if (this.showType === 2) {
          this.checkBoxGroup2.forEach(item => {
            item.isShow = false;
          })
          // columns将val数组存在的值设为true，不存在的设为false
          val.forEach(item => {
            let current = this.checkBoxGroup2.find(i => i.label === item)
            current.isShow = true;
          })
        }
        if (this.showType === 3) {
          this.checkBoxGroup3.forEach(item => {
            item.isShow = false;
          })
          // columns将val数组存在的值设为true，不存在的设为false
          val.forEach(item => {
            let current = this.checkBoxGroup3.find(i => i.label === item)
            current.isShow = true;
          })
        }
        if (this.showType === 4) {
          this.checkBoxGroup4.forEach(item => {
            item.isShow = false;
          })
          // columns将val数组存在的值设为true，不存在的设为false
          val.forEach(item => {
            let current = this.checkBoxGroup4.find(i => i.label === item)
            current.isShow = true;
          })
        }
        if (this.showType === 5) {
          this.checkBoxGroup5.forEach(item => {
            item.isShow = false;
          })
          // columns将val数组存在的值设为true，不存在的设为false
          val.forEach(item => {
            let current = this.checkBoxGroup5.find(i => i.label === item)
            current.isShow = true;
          })
        }
        if (this.showType === 6) {
          this.checkBoxGroup6.forEach(item => {
            item.isShow = false;
          })
          // columns将val数组存在的值设为true，不存在的设为false
          val.forEach(item => {
            let current = this.checkBoxGroup6.find(i => i.label === item)
            current.isShow = true;
          })
        }
        if (this.showType === 7) {
          this.checkBoxGroup7.forEach(item => {
            item.isShow = false;
          })
          // columns将val数组存在的值设为true，不存在的设为false
          val.forEach(item => {
            let current = this.checkBoxGroup7.find(i => i.label === item)
            current.isShow = true;
          })
        }
        if (this.showType === 8) {
          this.checkBoxGroup8.forEach(item => {
            item.isShow = false;
          })
          // columns将val数组存在的值设为true，不存在的设为false
          val.forEach(item => {
            let current = this.checkBoxGroup8.find(i => i.label === item)
            current.isShow = true;
          })
        }
        if (this.showType === 9) {
          this.checkBoxGroup9.forEach(item => {
            item.isShow = false;
          })
          // columns将val数组存在的值设为true，不存在的设为false
          val.forEach(item => {
            let current = this.checkBoxGroup9.find(i => i.label === item)
            current.isShow = true;
          })
        }
        if (this.showType === 10) {
          this.checkBoxGroup10.forEach(item => {
            item.isShow = false;
          })
          // columns将val数组存在的值设为true，不存在的设为false
          val.forEach(item => {
            let current = this.checkBoxGroup10.find(i => i.label === item)
            current.isShow = true;
          })
        }
        if (this.showType === 11) {
          this.checkBoxGroup11.forEach(item => {
            item.isShow = false;
          })
          // columns将val数组存在的值设为true，不存在的设为false
          val.forEach(item => {
            let current = this.checkBoxGroup11.find(i => i.label === item)
            current.isShow = true;
          })
        }

        this.reload = Math.random();
      },
      // 表格列是否显示的方法
      showColumn(currentColumn) {
        if (this.showType === 1) {
          return this.checkBoxGroup1.find(item => item.prop === currentColumn).isShow;
        }
        if (this.showType === 2) {
          return this.checkBoxGroup2.find(item => item.prop === currentColumn).isShow;
        }
        if (this.showType === 3) {
          return this.checkBoxGroup3.find(item => item.prop === currentColumn).isShow;
        }
        if (this.showType === 4) {
          return this.checkBoxGroup4.find(item => item.prop === currentColumn).isShow;
        }
        if (this.showType === 5) {
          return this.checkBoxGroup5.find(item => item.prop === currentColumn).isShow;
        }
        if (this.showType === 6) {
          return this.checkBoxGroup6.find(item => item.prop === currentColumn).isShow;
        }
        if (this.showType === 7) {
          return this.checkBoxGroup7.find(item => item.prop === currentColumn).isShow;
        }
        if (this.showType === 8) {
          return this.checkBoxGroup8.find(item => item.prop === currentColumn).isShow;
        }
        if (this.showType === 9) {
          return this.checkBoxGroup9.find(item => item.prop === currentColumn).isShow;
        }
        if (this.showType === 10) {
          return this.checkBoxGroup10.find(item => item.prop === currentColumn).isShow;
        }
        if (this.showType === 11) {
          return this.checkBoxGroup11.find(item => item.prop === currentColumn).isShow;
        }
      },

      /** 新增按钮操作 */
      handleAdd(title) {
        this.open = true;
        this.showTitle = title
        if (this.showType === 4) {
          this.getDeptList()
        }
      },
      /** 修改按钮操作 */
      handleUpdate(row, title) {
        selectUserConfigById(row.id).then((response) => {
          this.form = response.data;
          this.open = true;
          this.showTitle = title;
        });
        if (this.showType === 4) {
          this.getDeptList()
        }
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate((valid) => {
          if (valid) {
            this.form.type = this.showType
            if (this.showType === 4 && this.checkCompany) {
              var str = '';
              var separator = '';
              for (var i = 0; i < this.checkCompany.length; i++) {
                if (i > 0) {
                  separator = ','; // 在第一个元素后面不添加分隔符
                }
                str += separator + this.checkCompany[i];
              }
              this.form.deptIds = str
            }
            if (this.showType !== 3) {
              delete this.form['isCost']
            }
            if (this.showType !== 4) {
              delete this.form['sex']
            }
            if (this.showType !== 8) {
              delete this.form['isContract']
            }

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
          sex: 0,
          isCost: 0,
          isContract: 1,
        }
        this.resetForm("form");
        this.checkCompany = []
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
        if (this.showType === 1) {
          let data = []
          for (let i=0; i < this.dataList1.length; i++) {
            data.push({
              field1: this.dataList1[i].name,
              field2: this.dataList1[i].sort,
              field3: this.dataList1[i].createTime,
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
              {field: 'field1', displayName: '客户来源名称', columnSize: 1},
              {field: 'field2', displayName: '排序', columnSize: 1},
              {field: 'field3', displayName: '创建时间', columnSize: 1}
            ],
            // 表头的自定义样式设置
            gridHeaderStyle: 'font-weight: 500; border: 1px solid lightgray; font-size: 18px; line-height: 32px;',
            // 表格的自定义样式设置
            gridStyle: 'text-align: center; border: 1px solid lightgray; font-size: 15px; line-height: 30px;',
          })
        }
        if (this.showType === 2) {
          let data = []
          for (let i=0; i < this.dataList2.length; i++) {
            data.push({
              field1: this.dataList2[i].name,
              field2: this.dataList2[i].sort,
              field3: this.dataList2[i].createTime,
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
              {field: 'field1', displayName: '分类名称', columnSize: 1},
              {field: 'field2', displayName: '排序', columnSize: 1},
              {field: 'field3', displayName: '创建时间', columnSize: 1}
            ],
            // 表头的自定义样式设置
            gridHeaderStyle: 'font-weight: 500; border: 1px solid lightgray; font-size: 18px; line-height: 32px;',
            // 表格的自定义样式设置
            gridStyle: 'text-align: center; border: 1px solid lightgray; font-size: 15px; line-height: 30px;',
          })
        }
        if (this.showType === 3) {
          let data = []
          for (let i=0; i < this.dataList3.length; i++) {
            data.push({
              field1: this.dataList3[i].name,
              field2: this.dataList3[i].isCost === 0 ? '否' : '是',
              field3: this.dataList3[i].sort,
              field4: this.dataList3[i].createTime,
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
              {field: 'field1', displayName: '收支方式名称', columnSize: 1},
              {field: 'field2', displayName: '是否属于成本', columnSize: 1},
              {field: 'field3', displayName: '排序', columnSize: 1},
              {field: 'field4', displayName: '创建时间', columnSize: 1}
            ],
            // 表头的自定义样式设置
            gridHeaderStyle: 'font-weight: 500; border: 1px solid lightgray; font-size: 18px; line-height: 32px;',
            // 表格的自定义样式设置
            gridStyle: 'text-align: center; border: 1px solid lightgray; font-size: 15px; line-height: 30px;',
          })
        }
        if (this.showType === 4) {
          let data = []
          for (let i=0; i < this.dataList4.length; i++) {
            data.push({
              field1: this.dataList4[i].name,
              field2: this.dataList4[i].age,
              field3: this.dataList4[i].sex === 0 ? '男' : '女',
              field4: this.dataList4[i].phone,
              field5: this.dataList4[i].idCard,
              field6: this.dataList4[i].createTime,
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
              {field: 'field1', displayName: '姓名', columnSize: 1},
              {field: 'field2', displayName: '年龄', columnSize: 1},
              {field: 'field3', displayName: '性别', columnSize: 1},
              {field: 'field4', displayName: '电话', columnSize: 1},
              {field: 'field5', displayName: '身份证号', columnSize: 1},
              {field: 'field6', displayName: '创建时间', columnSize: 1}
            ],
            // 表头的自定义样式设置
            gridHeaderStyle: 'font-weight: 500; border: 1px solid lightgray; font-size: 18px; line-height: 32px;',
            // 表格的自定义样式设置
            gridStyle: 'text-align: center; border: 1px solid lightgray; font-size: 15px; line-height: 30px;',
          })
        }
        if (this.showType === 5) {
          let data = []
          for (let i=0; i < this.dataList5.length; i++) {
            data.push({
              field1: this.dataList5[i].name,
              field2: this.dataList5[i].address,
              field3: this.dataList5[i].phone,
              field4: this.dataList5[i].createTime,
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
              {field: 'field1', displayName: '税务局名称', columnSize: 1},
              {field: 'field2', displayName: '税务局地址', columnSize: 1},
              {field: 'field3', displayName: '税务局电话', columnSize: 1},
              {field: 'field4', displayName: '创建时间', columnSize: 1}
            ],
            // 表头的自定义样式设置
            gridHeaderStyle: 'font-weight: 500; border: 1px solid lightgray; font-size: 18px; line-height: 32px;',
            // 表格的自定义样式设置
            gridStyle: 'text-align: center; border: 1px solid lightgray; font-size: 15px; line-height: 30px;',
          })
        }
        if (this.showType === 6) {
          let data = []
          for (let i=0; i < this.dataList6.length; i++) {
            data.push({
              field1: this.dataList6[i].name,
              field2: this.dataList6[i].address,
              field3: this.dataList6[i].phone,
              field4: this.dataList6[i].createTime,
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
              {field: 'field1', displayName: '工商局名称', columnSize: 1},
              {field: 'field2', displayName: '工商局地址', columnSize: 1},
              {field: 'field3', displayName: '工商局电话', columnSize: 1},
              {field: 'field4', displayName: '创建时间', columnSize: 1}
            ],
            // 表头的自定义样式设置
            gridHeaderStyle: 'font-weight: 500; border: 1px solid lightgray; font-size: 18px; line-height: 32px;',
            // 表格的自定义样式设置
            gridStyle: 'text-align: center; border: 1px solid lightgray; font-size: 15px; line-height: 30px;',
          })
        }
        if (this.showType === 7) {
          let data = []
          for (let i=0; i < this.dataList7.length; i++) {
            data.push({
              field1: this.dataList7[i].name,
              field2: this.dataList7[i].sort,
              field3: this.dataList7[i].createTime,
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
              {field: 'field1', displayName: '公司/企业类型名称', columnSize: 1},
              {field: 'field2', displayName: '排序', columnSize: 1},
              {field: 'field3', displayName: '创建时间', columnSize: 1}
            ],
            // 表头的自定义样式设置
            gridHeaderStyle: 'font-weight: 500; border: 1px solid lightgray; font-size: 18px; line-height: 32px;',
            // 表格的自定义样式设置
            gridStyle: 'text-align: center; border: 1px solid lightgray; font-size: 15px; line-height: 30px;',
          })
        }
        if (this.showType === 8) {
          let data = []
          for (let i=0; i < this.dataList8.length; i++) {
            data.push({
              field1: this.dataList8[i].name,
              field2: this.dataList8[i].isContract === 0 ? '否' : '是',
              field3: this.dataList8[i].sort,
              field4: this.dataList8[i].createTime,
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
              {field: 'field1', displayName: '公司客户状态名称', columnSize: 1},
              {field: 'field2', displayName: '是否属于终止合同选项', columnSize: 1},
              {field: 'field3', displayName: '排序', columnSize: 1},
              {field: 'field4', displayName: '创建时间', columnSize: 1}
            ],
            // 表头的自定义样式设置
            gridHeaderStyle: 'font-weight: 500; border: 1px solid lightgray; font-size: 18px; line-height: 32px;',
            // 表格的自定义样式设置
            gridStyle: 'text-align: center; border: 1px solid lightgray; font-size: 15px; line-height: 30px;',
          })
        }
        if (this.showType === 9) {
          let data = []
          for (let i=0; i < this.dataList9.length; i++) {
            data.push({
              field1: this.dataList9[i].name,
              field2: this.dataList9[i].sort,
              field3: this.dataList9[i].createTime,
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
              {field: 'field1', displayName: '税种分类名称', columnSize: 1},
              {field: 'field2', displayName: '排序', columnSize: 1},
              {field: 'field3', displayName: '创建时间', columnSize: 1}
            ],
            // 表头的自定义样式设置
            gridHeaderStyle: 'font-weight: 500; border: 1px solid lightgray; font-size: 18px; line-height: 32px;',
            // 表格的自定义样式设置
            gridStyle: 'text-align: center; border: 1px solid lightgray; font-size: 15px; line-height: 30px;',
          })
        }
        if (this.showType === 10) {
          let data = []
          for (let i=0; i < this.dataList10.length; i++) {
            data.push({
              field1: this.dataList10[i].name,
              field2: this.dataList10[i].sort,
              field3: this.dataList10[i].createTime,
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
              {field: 'field1', displayName: '税务名称', columnSize: 1},
              {field: 'field2', displayName: '排序', columnSize: 1},
              {field: 'field3', displayName: '创建时间', columnSize: 1}
            ],
            // 表头的自定义样式设置
            gridHeaderStyle: 'font-weight: 500; border: 1px solid lightgray; font-size: 18px; line-height: 32px;',
            // 表格的自定义样式设置
            gridStyle: 'text-align: center; border: 1px solid lightgray; font-size: 15px; line-height: 30px;',
          })
        }
        if (this.showType === 11) {
          let data = []
          for (let i=0; i < this.dataList11.length; i++) {
            data.push({
              field1: this.dataList11[i].name,
              field2: this.dataList11[i].sort,
              field3: this.dataList11[i].createTime,
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
              {field: 'field1', displayName: '登录身份名称', columnSize: 1},
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
      getDeptList () {
        this.queryParams.pageSize = 10000;
        // this.queryParams.parentId = this.$store.state.user.deptId;
        this.queryParams.parentId = 0;
        listDept(this.queryParams).then(response => {
          this.companyData = response.rows;
          if (this.form.deptIds) {
            let deptIdArray = this.form.deptIds.split(',')
            response.rows.forEach(item => {
              deptIdArray.forEach(deptId =>{
                if (item.deptId == deptId) {
                  this.checkCompany.push(item.deptId)
                }
              })
            })
          }
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
  ::v-deep .el-transfer__buttons {
    display: inline-block;
    vertical-align: middle;
    padding: 0 5px;
  }
  ::v-deep .el-checkbox.el-transfer-panel__item {
    display: block;
  }
</style>
