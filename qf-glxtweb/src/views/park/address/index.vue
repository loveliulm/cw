<template>
  <div class="app-container">
    <div class="ant-tabs">
      <span @click="changeType(0)" :class="{ 'ant-tab-active': queryParams.addressStatus === 0 }" class="ant-tab">全部</span>
      <span @click="changeType(2)" :class="{ 'ant-tab-active': queryParams.addressStatus === 2 }" class="ant-tab">在用</span>
      <span @click="changeType(1)" :class="{ 'ant-tab-active': queryParams.addressStatus === 1 }" class="ant-tab">闲置</span>
    </div>
    <el-row :gutter="10">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="small"
          @click="handleAdd"
          v-hasPermi="['park:address:add']"
        >添加地址</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
          <el-form-item label="注册地址" prop="address">
            <el-input
              v-model="queryParams.address"
              placeholder="请输入注册地址"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="所属地区" prop="regionname">
            <el-cascader
              ref="cascader"
              filterable
              clearable
              :options="options"
              :props="{ checkStrictly: true }"
              v-model="selectedOptions"
              @change="addressChoose"
            ></el-cascader>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList">
      <el-table-column label="地址状态" align="center" prop="addressStatus" width="80">
        <template slot-scope="scope">
          <el-tag style="cursor: pointer" effect="plain" type="primary" v-if="scope.row.addressStatus == 1">闲置</el-tag>
          <el-tag style="cursor: pointer" effect="plain" type="danger" v-if="scope.row.addressStatus == 2">在用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="编号" align="center" prop="number" width="100" :show-overflow-tooltip="true"/>
      <el-table-column label="注册地址" align="center" prop="address" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-link @click="showAddAndCus(scope.row)" type="primary" :underline="false">{{ scope.row.address }}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="所属地区" align="center" prop="regionname" :show-overflow-tooltip="true"/>
      <el-table-column label="合同名称" align="center" prop="contractNo" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ scope.row.contractNo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="到期时间" align="center" prop="contractEndDate" width="95">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.contractEndDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="企业名称" align="center" prop="customerName" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.contractNo">{{ scope.row.customerName }}</span>
          <span v-else>无</span>
        </template>
      </el-table-column>
      <el-table-column label="企业现状" align="center" prop="accountingStatus" width="80">
        <template slot-scope="scope">
          <el-tag style="border-radius: 30px; cursor: pointer" effect="plain" type="primary" v-if="scope.row.accountingStatus == 1">正常</el-tag>
          <el-tag style="border-radius: 30px; cursor: pointer" effect="plain" type="info" v-else-if="scope.row.accountingStatus == 2">关闭</el-tag>
          <span v-else>无</span>
        </template>
      </el-table-column>
      <el-table-column label="企业年报" align="center" prop="annualReports" width="80">
        <template slot-scope="scope">
          <el-tag style="border-radius: 30px; cursor: pointer; width: 46px;" effect="plain" type="primary" v-if="scope.row.annualReports == 1">是</el-tag>
          <el-tag style="border-radius: 30px; cursor: pointer; width: 46px;" effect="plain" type="info" v-else-if="scope.row.annualReports == 2">否</el-tag>
          <span v-else>无</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="95">
        <template slot-scope="scope">
<!--          <span>{{ parseTime(scope.row.createTime) }}</span>-->
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right" width="252">
        <template slot-scope="scope">
          <el-tag @click="showAddAddress(scope.row)" style="height: 32px; line-height: 32px; margin-right: 10px; cursor: pointer" effect="plain" type="success" v-if="scope.row.addressStatus == 1">使用该地址</el-tag>
          <el-tag @click="showEditAddress(scope.row)" style="height: 32px; line-height: 32px; margin-right: 10px; cursor: pointer" effect="plain" type="success" v-if="scope.row.addressStatus == 2">变更为闲置</el-tag>
          <el-button
            size="small"
            type="primary"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['park:address:edit']"
          >修改</el-button>
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.row)"
            v-hasPermi="['park:address:remove']"
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

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" append-to-body :close-on-click-modal="false" :before-close="cancel">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="编号" prop="number">
              <el-input v-model="form.number" placeholder="请输入编号" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="同行联系人" prop="contacts">
              <el-input v-model="form.contacts" placeholder="请输入同行联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入电话" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="ShowCascader">
            <el-form-item label="所属地区" prop="selectedRegion">
              <el-cascader
                style="width: 100%"
                filterable
                clearable
                :options="options"
                v-model="selectedRegion"
                @change="regionChoose"
              ></el-cascader>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="注册地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入注册地址" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="成本价" prop="costAmount">
              <el-input v-model="form.costAmount" placeholder="请输入成本价" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input type="textarea" :rows="3" v-model="form.remark" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer" style="padding-top:20px">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 使用地址对话框 -->
    <el-dialog title="园区地址" :visible.sync="openAddress" width="780px" append-to-body :close-on-click-modal="false" :before-close="cancelAddress">
      <el-form :model="formContract" :rules="rules" ref="formAddressRef" label-width="90px">
        <el-form-item label="选择合同" prop="contractId">
          <el-select v-model="formContract.contractId" placeholder="请选择" clearable filterable style="width: 100%;">
            <el-option
              v-for="item in contractData"
              :key="item.contractId"
              :label="item.contractNo"
              :value="item.contractId"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <el-descriptions class="margin-top" title="地址信息" :column="2" size="medium" border :labelStyle="{'width': '140px'}" >
        <el-descriptions-item :contentStyle="{'width': '100px'}">
          <template slot="label">
            <i class="el-icon-tickets"></i>
            地址编号
          </template>
          {{ formAddress.number }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-location-outline"></i>
            注册地址
          </template>
          {{ formAddress.address }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-box"></i>
            地址状态
          </template>
          <el-tag style="cursor: pointer" effect="plain" type="primary" v-if="formAddress.addressStatus == 1">闲置</el-tag>
          <el-tag style="cursor: pointer" effect="plain" type="danger" v-if="formAddress.addressStatus == 2">再用</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-office-building"></i>
            所属地区
          </template>
          {{ formAddress.regionname }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            同行联系人
          </template>
          {{ formAddress.contacts }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            同行联系人电话
          </template>
          {{ formAddress.phone }}
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer" style="padding-top:20px">
        <el-button type="primary" @click="submitAddressForm(formAddress.id)">确 定</el-button>
        <el-button @click="cancelAddress">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 点击注册地址显示地址和公司信息对话框 -->
    <el-dialog title="地址信息·再用企业" :visible.sync="openAddAndCus" width="820px" append-to-body :before-close="cancelAddAndCus">
      <el-descriptions style="margin-top: -15px" title="地址信息" :column="2" size="medium" border :labelStyle="{'width': '140px'}" >
        <el-descriptions-item :contentStyle="{'width': '100px'}">
          <template slot="label">
            <i class="el-icon-tickets"></i>
            地址编号
          </template>
          {{ formAddAndCus.number }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-location-outline"></i>
            注册地址
          </template>
          {{ formAddAndCus.address }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-box"></i>
            地址状态
          </template>
          <el-tag style="cursor: pointer" effect="plain" type="primary" v-if="formAddAndCus.addressStatus == 1">闲置</el-tag>
          <el-tag style="cursor: pointer" effect="plain" type="danger" v-if="formAddAndCus.addressStatus == 2">再用</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-office-building"></i>
            所属地区
          </template>
          {{ formAddAndCus.regionname }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            同行联系人
          </template>
          {{ formAddAndCus.contacts }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            成本价
          </template>
          {{ formAddAndCus.costAmount }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            同行联系人电话
          </template>
          {{ formAddAndCus.phone }}
        </el-descriptions-item>
      </el-descriptions>
      <el-descriptions style="margin-top: 20px" title="在用企业" :column="2" size="medium" border :labelStyle="{'width': '140px'}" >
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-tickets"></i>
            企业名称
          </template>
          {{ formAddAndCus.customerName }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-timer"></i>
            到期日期
          </template>
          {{ formAddAndCus.expirationTime }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            联系人
          </template>
          {{ formAddAndCus.representative }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-box"></i>
            关联合同
          </template>
          {{ formAddAndCus.contractNo }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-c-scale-to-original"></i>
            园区企业现状
          </template>
          <span v-if="formAddAndCus.accountingStatus == 1">正常</span>
          <span v-if="formAddAndCus.accountingStatus == 2">关闭</span>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-monitor"></i>
            年报情况
          </template>
          <span v-if="formAddAndCus.annualReports == 1">是</span>
          <span v-if="formAddAndCus.annualReports == 2">否</span>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-chat-dot-round"></i>
            所属业务员
          </template>
          {{ formAddAndCus.belongingSalesperson }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-notebook-1"></i>
            收款账户
          </template>
          {{ formAddAndCus.bankAcount }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-time"></i>
            收款时间
          </template>
          {{ formAddAndCus.collectionDate }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-coin"></i>
            收款金额
          </template>
          {{ formAddAndCus.collectionAmount }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

  </div>
</template>

<script>
  import { selectParkAddressList, getParkAddressList, selectParkAddressById,
    addParkAddress, updateParkAddress, delParkAddress, exportParkAddress, getContractList, doParkContract } from "@/api/park/address";

  import {
    pcaTextArr, // 省市区三级联动数据，纯汉字
  } from "element-china-area-data";

  export default {
    name: "Notice",
    data() {
      return {
        type: 0,
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 表格数据
        dataList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 是否显示使用地址弹出层
        openAddress: false,
        // 是否显示地址和公司弹出层
        openAddAndCus: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          address: null,
          regionname: null,
          addressStatus: 0
        },
        // 表单参数
        form: {},
        // 表单参数
        formAddress: {},
        // 表单参数
        formAddAndCus: {},
        formContract: {},
        // 表单校验
        rules: {
          address: [
            { required: true, message: "注册地址不能为空", trigger: "blur" }
          ],
          contractId: [
            { required: true, message: "注册地址不能为空", trigger: "change" }
          ],

        },
        options: pcaTextArr, // 省市区级联数据
        selectedOptions: [], // 选择的地区
        selectedRegion: [], // 选择的地区
        ShowCascader: false,
        contractId: null, // 使用地址-合同列表
        contractData: [], // 使用地址-合同列表
      };
    },
    created() {
      this.getList();
    },
    methods: {
      changeType(type) {
        this.queryParams.addressStatus = type
        this.getList()
      },
      /** 查询列表 */
      getList() {
        this.loading = true;
        selectParkAddressList(this.queryParams).then(response => {
          this.dataList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      /**搜索省市区三级联动 */
      addressChoose(value) {
        if (value && value.length > 0) {
          if (value.length === 1) {
            this.queryParams.regionname = value[0]
          } else if (value.length === 2) {
            this.queryParams.regionname = value[0] + "/" + value[1]
          } else {
            this.queryParams.regionname = value[0] + "/" + value[1] + "/" + value[2];
          }
        } else {
          this.queryParams.regionname = null
          this.$refs.cascader.$refs.panel.checkedValue = []; // 清空选中值
          this.$refs.cascader.$refs.panel.clearCheckedNodes(); // 清空级联选择器选中状态
          this.$refs.cascader.$refs.panel.activePath = []; // 清除高亮
          this.$refs.cascader.$refs.panel.syncActivePath(); // 初始化（只展示一级节点）
        }
      },
      /**新增修改省市区三级联动 */
      regionChoose(value) {
        if (value && value.length) {
          this.form.regionname = value[0] + "/" + value[1] + "/" + value[2];
        }
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          costAmount: 0
        };
        this.selectedRegion = []
        this.resetForm("form");
        this.ShowCascader = false
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.$refs.cascader.$refs.panel.checkedValue = []; // 清空选中值
        this.$refs.cascader.$refs.panel.clearCheckedNodes(); // 清空级联选择器选中状态
        this.$refs.cascader.$refs.panel.activePath = []; // 清除高亮
        this.$refs.cascader.$refs.panel.syncActivePath(); // 初始化（只展示一级节点）
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.ShowCascader = true
        this.open = true;
        this.title = "添加园区地址";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        selectParkAddressById(row.id).then(response => {
          this.form = response.data;
          this.selectedRegion.push(this.form.regionname.split("/")[0], this.form.regionname.split("/")[1], this.form.regionname.split("/")[2])
          this.ShowCascader = true
          this.open = true;
          this.title = "修改园区地址";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateParkAddress(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.cancel()
                  this.getList();
                }
              });
            } else {
              addParkAddress(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.cancel()
                  this.getList();
                }
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        this.$confirm('是否确认删除该数据?', "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delParkAddress(row.id);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 使用地址取消按钮 */
      cancelAddress() {
        this.openAddress = false;
        this.formContract = {};
        this.resetForm("formContract");
      },
      /** 使用地址按钮操作 */
      showAddAddress(row) {
        getContractList().then(response => {
          this.contractData = response.data
        });
        selectParkAddressById(row.id).then(response => {
          this.formAddress = response.data;
          this.openAddress = true;
        });
      },
      /** 使用地址保存操作 */
      submitAddressForm(id) {
        this.$refs["formAddressRef"].validate(valid => {
          if (valid) {
            doParkContract({id: id, contractId: this.formContract.contractId}).then(response => {
              if (response.code === 200) {
                this.msgSuccess("操作成功");
                this.cancelAddress()
                this.getList();
              }
            });
          }
        });
      },
      /** 置为闲置按钮操作 */
      showEditAddress(row) {
        this.$confirm('是否确认变更为闲置?', "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return doParkContract({id: row.id});
        }).then(() => {
          this.getList();
          this.msgSuccess("操作成功");
        }).catch(function() {});
      },
      /** 显示地址和公司信息操作 */
      showAddAndCus(row) {
        selectParkAddressById(row.id).then(response => {
          this.formAddAndCus = response.data;
          this.openAddAndCus = true;
        });
      },
      /** 关闭地址和公司信息 */
      cancelAddAndCus() {
        this.openAddAndCus = false
      }

    }
  };
</script>

<style lang="scss" scoped>
  .ant-tabs {
    width: 100%;
    border-bottom: 1px solid #cbcbcb;
    margin-bottom: 20px;
  }
  .ant-tab {
    display: inline-block;
    margin: 0 32px 0 0;
    padding: 12px 16px;
    cursor: pointer;
    font-size: 14px;
  }
  .ant-tab-active {
    color: #1890ff;
    border-bottom: 2px solid #1890ff !important;
  }
</style>
