<template>
  <div class="app-container">
    <!--<div class="service-wrap">
      <div class="service-left-wrap">
        <el-tree :data="serviceProcess"
                 :props="defaultProps"
                 ref="vueTree"
                 node-key="serviceItemId"
                 highlight-current
                 default-expand-all
                 @node-click="handleNodeClick"></el-tree>
      </div>
      <div class="service-right-wrap">
        <div style="height:814px; width:100%;">
          <div class="service-right-title">步骤信息：</div>
          <el-table :data="processSteps"
                    style="width: 100%"
                    height="700"
                    border>
            <el-table-column prop="number"
                             label="步骤">
            </el-table-column>
            <el-table-column prop="serviceProcessName"
                             label="名称">

            </el-table-column>
            <el-table-column prop="processDescribe"
                             label="描述">
            </el-table-column>
            <el-table-column prop="processData"
                             label="资料">
            </el-table-column>
            <el-table-column prop="estimatedTime"
                             label="预计耗时">
            </el-table-column>
            <el-table-column align="center"
                             label="操作"
                             width="160px">
              <template slot="header"
                        slot-scope="scope">
                <el-button v-model="handleAdd"
                           size="mini"
                           type="success"
                           circle
                           plain
                           icon="el-icon-plus"
                           @click="handleAdd(scope.$index, scope.row)">
                  新增
                </el-button>
              </template>
              <template slot-scope="scope">
                <el-button size="mini"
                           type="primary"
                           circle
                           plain
                           icon="el-icon-edit"
                           @click="handleEdit(scope.$index, scope.row)">
                  编辑
                </el-button>
                <el-button size="mini"
                           type="danger"
                           circle
                           plain
                           icon="el-icon-delete"
                           @click="handleDelete(scope.$index, scope.row)">
                  修改
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="service-right-content"></div>
      </div>
    </div>-->
    <el-row :gutter="20">
      <!--菜单数据-->
      <el-col :span="4" :xs="24">
        <div class="card-config" :style="{height: tableHeight + 'px'}">
          <div style="text-align: center; margin: 10px 0">
            <el-button type="primary" icon="el-icon-plus" size="small" style="width: 150px;" @click="handleAdd()">新增流程</el-button>
          </div>
          <el-tree :data="serviceProcess"
                   :props="defaultProps"
                   ref="vueTree"
                   node-key="serviceItemId"
                   highlight-current
                   default-expand-all
                   style="background-color: #f6f6f6;"
                   @node-click="handleNodeClick">
          </el-tree>
        </div>
      </el-col>
      <!--表格数据-->
      <el-col :span="20" :xs="24">
        <div style="height: 40px; line-height: 40px; width: 100%; margin-bottom: 15px">
          <div style="float: left; font-weight: bolder">
            <span>{{ selectTressNodeName }}</span>
          </div>
          <div style="float: right">
            <el-button size="small" type="primary" @click="submitBaseItem()">保 存</el-button>
            <el-button size="small" type="danger" v-if="!isAdd" @click="deleteBaseInfo">删 除</el-button>
          </div>
        </div>
        <el-form v-loading="loading" ref="form" :model="form" :rules="rules" label-width="120px">
          <el-row>
            <el-col :span="12">
              <el-form-item label="适用地区：" prop="sysUserConfigId" v-if="isShowRegion && isShowTable">
                <el-select v-model="form.sysUserConfigId" placeholder="请选择适用地区" filterable style="width: 100%">
                  <el-option
                    v-for="item in regionData"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="isAdd">
              <el-form-item label="所属服务：" prop="serviceItemParentName">
                <el-select ref="allselect" v-model="form.serviceItemParentName" placeholder="请选择所属服务" style="width: 100%">
                  <el-option :value="form.serviceItemParentId" :label="form.serviceItemParentName" style="padding: 0; height:200px; overflow-y: auto; font-weight: 400;">
                    <el-tree
                      ref="selectTree"
                      :data="allServiceData"
                      highlight-current
                      default-expand-all
                      node-key="serviceItemId"
                      :props="defaultProps"
                      @node-click="handleAllTypeClick"
                    />
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-else>
              <el-form-item label="所属服务：" prop="serviceItemParentName" v-show="isShowRegion && isShowTable">
                <el-select ref="select" v-model="form.serviceItemParentName" placeholder="请选择所属服务" style="width: 100%">
                  <el-option :value="form.serviceItemParentId" :label="form.serviceItemParentName" style="padding: 0; height:200px; overflow-y: auto;">
                    <el-tree
                      ref="selectTree"
                      :data="serviceData"
                      highlight-current
                      default-expand-all
                      node-key="serviceItemId"
                      :props="defaultProps"
                      @node-click="handleTypeClick"
                    />
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="流程描述：" prop="serviceItemName">
                <el-input v-model="form.serviceItemName" placeholder="请输入流程描述" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="预计完成时间：" prop="days" v-if="isShowTable">
                <el-input placeholder="请输入预计完成时间" v-model="form.days">
                  <template slot="append">天</template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>

        <el-table v-loading="loading" :data="processSteps" height="500" stripe border v-if="isShowTable">
          <el-table-column prop="number" label="步骤" width="60px"></el-table-column>
          <el-table-column prop="serviceProcessName" label="名称" width="150px"></el-table-column>
          <el-table-column prop="processDescribe" label="描述"></el-table-column>
          <el-table-column prop="processData" label="资料"></el-table-column>
          <el-table-column prop="processOutput" label="交付件"></el-table-column>
          <el-table-column prop="estimatedTime" label="预计耗时" width="110px"></el-table-column>
          <el-table-column align="center" label="操作" width="180px">
            <template slot="header" slot-scope="scope">
              <el-button v-model="handleAdd" size="mini" type="success" circle plain icon="el-icon-plus" @click="addProcess('新增步骤信息')">
                新增
              </el-button>
            </template>
            <template slot-scope="scope">
              <el-button size="mini" type="primary" circle plain icon="el-icon-edit" @click="editProcess(scope.row, '编辑步骤信息')">
                编辑
              </el-button>
              <el-button size="mini" type="danger" circle plain icon="el-icon-delete" @click="deleteProcess(scope.row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-dialog :title="showTitle" :visible.sync="open" width="600px" append-to-body :close-on-click-modal="false" :before-close="closeProcess">
          <el-form ref="processForm" :model="processForm" :rules="rules" label-width="80px">
            <el-row>
              <el-col :span="24">
                <el-form-item label="步骤" prop="number">
                  <el-input-number v-model="processForm.number" controls-position="right" :min="1" style="width: 100%;"/>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="名称" prop="serviceProcessName">
                  <el-input v-model="processForm.serviceProcessName" placeholder="请输入步骤名称" />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="描述" prop="processDescribe">
                  <el-input v-model="processForm.processDescribe" placeholder="请输入步骤描述" type="textarea" :rows="2"/>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="资料" prop="processData">
                  <el-input v-model="processForm.processData" placeholder="请输入步骤资料" type="textarea" :rows="2"/>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="交付件" prop="processOutput">
                  <el-input v-model="processForm.processOutput" placeholder="请输入交付件" type="textarea" :rows="2"/>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="预计耗时" prop="estimatedTime">
                  <el-input placeholder="请输入预计耗时" v-model="processForm.estimatedTime">
                    <template slot="append">天</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button size="small" @click="closeProcess">取 消</el-button>
            <el-button size="small" type="primary" @click="submitProcess()">提 交</el-button>
          </div>
        </el-dialog>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import {serviceTreeSelect, getBaseTypeInfo, addBaseType, deleteBaseType, updateBaseType, getBaseItemInfo, addBaseItem, updateBaseItem, deleteBaseItem, getProcessSteps, getServiceProcess, addServiceProcess, updateServiceProcess, deleteServiceProcess} from "@/api/system/service";
  import { getUserConfigList } from "@/api/system/userConfig";


  export default {
    name: "Service",
    data() {
      return {
        loading: false,
        // 服务流程列表
        serviceProcess: [],
        // 树型服务流程节点显示
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        // 服务流程步骤
        processSteps: [],
        // 选中的服务流程节点
        serviceClassId: 2,
        selectTressNodeId: 0,
        selectTressNodeName: '',
        // 只有三级显示地区
        isShowRegion: false,
        isAdd: false,
        // 一级不显示表格
        isShowTable: true,
        tableHeight: window.innerHeight - 130,
        // 表单参数
        form: {
          configId: null
        },
        open: false,
        showTitle: '',
        // 流程表单
        processForm: {

        },
        // 表单校验
        rules: {
          sysUserConfigId: [
            { required: true, message: "必填项不能为空", trigger: "change" },
          ],
          serviceItemParentName: [
            { required: true, message: "必填项不能为空", trigger: "change" },
          ],
          serviceItemName: [
            { required: true, message: "必填项不能为空", trigger: "blur" },
          ],
          number: [
            { required: true, message: "步骤不能为空", trigger: "blur" },
          ],
          serviceProcessName: [
            { required: true, message: "步骤名称不能为空", trigger: "blur" },
          ],
          processDescribe: [
            { required: true, message: "步骤描述不能为空", trigger: "blur" },
          ],

        },
        regionData: [],
        allServiceData: [],
        serviceData: [],
      }
    },
    created() {
      this.initData();
      this._getProcessSteps();
      // 工商局信息
      this.getUserConfigList(6);
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
      // 注意:初始化节点数据时，默认选中的node节点一定要有serviceItemId字段否则会报错
      // 必须先加载出数据后才能设置node-key
      async initData(serviceItemId, serviceItemParentId) {
        var res = await this._getServiceProcess();
        this.serviceProcess = res;
        this.dealAllSelectTree(res)
        this.dealSelectTree(res)
        // 默认选中某个节点
        this.$nextTick(() => {
          if (serviceItemId) {
            this.$refs.vueTree.setCurrentKey(serviceItemId);
            if (serviceItemParentId) {
              this.$refs.selectTree.setCurrentKey(serviceItemParentId);
            }
          } else {
            this.$refs.vueTree.setCurrentKey(res[0].children[0].children[0].serviceItemId);
          }
          this.getInitNodeId();
        });
      },
      // 处理新增下拉框树形
      dealAllSelectTree(data) {
        let newData = JSON.parse(JSON.stringify(data));

        newData[0].children.forEach(item => {
          let itemArr = item.children
          itemArr.forEach(info => {
            delete info['children']
          })
        })
        this.allServiceData = newData
      },
      // 处理下拉框树形
      dealSelectTree(data) {
        let newData = JSON.parse(JSON.stringify(data));

        newData = newData[0].children
        newData.forEach(item => {
          let itemArr = item.children
          itemArr.forEach(info => {
            delete info['children']
          })
        })
        this.serviceData = newData
      },
      // 默认选中节点对应的服务步骤
      getInitNodeId() {
        let node = this.$refs.vueTree.getCurrentNode();
        this.selectTressNodeId = node.serviceItemId;
        this.selectTressNodeName = node.label;
      },
      // 深拷贝数组组合树型服务流程数据
      _deepClone(source) {
        // 判断复制的目标是数组还是对象
        const targetObj = Object.prototype.toString.call(source) === '[object Array]' ? [] : {};
        for (let keys in source) {
          if (source.hasOwnProperty(keys)) {
            if (source[keys] && typeof source[keys] === 'object') { // 如果值是对象，就递归一下
              if (keys == 'serviceItems') {
                targetObj['children'] = Object.prototype.toString.call(source[keys]) === '[object Array]' ? [] : {};
                targetObj['children'] = this._deepClone(source[keys]);
              } else {
                targetObj[keys] = Object.prototype.toString.call(source[keys]) === '[object Array]' ? [] : {};
                targetObj[keys] = this._deepClone(source[keys]);
              }
            } else { // 如果不是，就直接赋值
              if (keys == 'serviceTypeName' || keys == 'serviceItemName') {
                targetObj['label'] = source[keys];
              } else {
                targetObj[keys] = source[keys];
              }
            }
          }
        }
        return targetObj;
      },
      // 获取服务流程列表
      _getServiceProcess() {
        return new Promise((resolve) => {
          serviceTreeSelect().then(res => {
            //this.serviceProcess.push(this._splice(res.rows));
            let arr = []
            arr.push(this._splice(res.rows))
            resolve(arr)
          })
        })
      },
      // 树型服务流程列表
      _splice(arr) {
        // let oneLabel = '服务流程设置';
        // let twoLabel = '工商服务';
        // let two = {
        //   label: twoLabel,
        //   children: this._deepClone(arr)
        // };
        // let one = {
        //   label: oneLabel,
        // };
        // one.children = [];
        // one.children.push(two);
        // return one;
        let oneLabel = '工商服务';
        let one = {
          label: oneLabel,
          children: this._deepClone(arr)
        };
        return one;
      },
      // 点击树型服务流程节点执行回调函数
      handleNodeClick(data, node) {
        this.isAdd = false
        if (data.label ===  '工商服务') {
          // 默认选中某个节点
          // this.$nextTick(() => {
            // this.$refs.vueTree.setCurrentKey(this.selectTressNodeId);
            // this.initData()
          // })
          return
        }
        if (data.serviceClassId) {
          this.serviceClassId = data.serviceClassId
          this.isShowTable = false
          getBaseTypeInfo(data.serviceTypeId).then(res => {
            this.form.serviceItemName = res.data.serviceTypeName;
            this.selectTressNodeId = res.data.serviceTypeId;
            this.selectTressNodeName = res.data.serviceTypeName;
          })
        } else {
          this.isShowTable = true
          this.selectTressNodeId = data.serviceItemId;
          this.selectTressNodeName = data.label;
        }
      },
      // 新增时下拉框选择
      handleAllTypeClick(data, node) {
        this.$forceUpdate();
        if (data.label ===  '工商服务') {
          // 新增类型
          this.isShowTable = false
          this.isShowRegion = false
        } else {
          if (data.serviceClassId) {
            this.isShowTable = true
            this.isShowRegion = false
            this.form.serviceTypeId = data.serviceTypeId
            this.form.serviceItemParentId = 0
          } else {
            this.isShowTable = true
            this.isShowRegion = true
            this.form.serviceTypeId = data.serviceTypeId
            this.form.serviceItemParentId = data.serviceItemId
          }
        }
        this.form.serviceItemParentName = data.label;
        this.$nextTick(() => {
          this.$refs.allselect.blur()
        })
      },
      // 修改时下拉框选择
      handleTypeClick(data, node) {
        if (data.children) {
          this.$nextTick(() => {
            // 默认选中某个节点
            this.$refs.selectTree.setCurrentKey(this.form.serviceItemParentId);
          })
          return
        }
        this.form.serviceItemParentId = data.serviceItemId;
        this.form.serviceItemParentName = data.label;
        this.$nextTick(() => {
          this.$refs.select.blur()
        })
      },

      // 获取服务流程步骤
      _getProcessSteps() {
        let serviceItemId = this.selectTressNodeId;
        getProcessSteps({serviceItemId}).then(res => {
          this.processSteps = res.rows;
        })
        if (serviceItemId != 0) {
          this.getBaseItemInfo(serviceItemId)
        }
      },
      /** 查询服务项目列表 */
      getBaseItemInfo (serviceItemId) {
        this.loading = true
        this.reset()
        getBaseItemInfo(serviceItemId).then(res => {
          this.form = res.data;
          if (this.form.serviceItemParentId != 0) {
            this.selectTressNodeName = this.form.sysUserConfigName;
          } else {
            this.form.serviceItemParentName = ' '
          }
          this.isShowRegion = this.form.serviceItemParentId != 0;
          this.$nextTick(() => {
            // 默认选中某个节点
            this.$refs.selectTree.setCurrentKey(this.form.serviceItemParentId);
          })
          this.loading = false
        })

      },
      /** 查询地区列表 */
      getUserConfigList (type) {
        getUserConfigList({type: type}).then(response => {
          this.regionData = response.data;
        });
      },
      handleAdd() {
        this.selectTressNodeId = 0
        this.selectTressNodeName = '新增流程';
        this.reset()
        this.processSteps = []
        this.isShowRegion = true
        this.isShowTable = true
        this.isAdd = true
      },
      /** 提交按钮 */
      submitBaseItem: function () {
        this.$refs["form"].validate((valid) => {
          if (valid) {
            this.loading = true
            if (this.isShowTable) {
              if (this.form.serviceItemId !== undefined) {
                updateBaseItem(this.form).then((response) => {
                  if (response.code === 200) {
                    this.msgSuccess("修改成功");
                    this.initData(this.form.serviceItemId, this.form.serviceItemParentId);
                    this.reset();
                    if (this.selectTressNodeId != 0) {
                      this.getBaseItemInfo(this.selectTressNodeId)
                    }
                    this.loading = false
                  }
                });
              } else {
                addBaseItem(this.form).then((response) => {
                  if (response.code === 200) {
                    this.msgSuccess("新增成功");
                    this.isAdd = false
                    this.initData(this.form.serviceItemId, this.form.serviceItemParentId);
                    this.reset();
                    if (this.selectTressNodeId != 0) {
                      this.getBaseItemInfo(this.selectTressNodeId)
                    }
                    this.loading = false
                  }
                });
              }
            } else {
              if (this.form.serviceTypeId !== undefined) {
                updateBaseType({serviceTypeId: this.form.serviceTypeId, serviceTypeName: this.form.serviceItemName}).then((response) => {
                  if (response.code === 200) {
                    this.msgSuccess("修改成功");
                    this.initData();
                    this.isShowTable = true
                    this.reset();
                    this.loading = false
                  }
                });
              } else {
                addBaseType({serviceTypeName: this.form.serviceItemName, serviceClassId: this.serviceClassId}).then((response) => {
                  if (response.code === 200) {
                    this.msgSuccess("新增成功");
                    this.isShowTable = true
                    this.isAdd = false
                    this.initData();
                    this.reset();
                    this.loading = false
                  }
                });
              }
            }
          }
        });
      },
      /** 表单重置 */
      reset() {
        this.form = {}
        this.resetForm("form");
      },
      deleteBaseInfo() {
        var type = this.isShowTable
        const ids = this.selectTressNodeId
        if (!type) {
          this.$confirm('是否确认删除该记录?', "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
            // 删除类型
            return deleteBaseType(ids);
          }).then(() => {
            this.isShowTable = true
            this.msgSuccess("删除成功");
            this.initData()
          }).catch(function() {});
        } else {
          this.$confirm('是否确认删除该记录?', "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
            // 删除项目
            return deleteBaseItem(ids);
          }).then(() => {
            this.isShowTable = true
            this.msgSuccess("删除成功");
            this.initData()
          }).catch(function() {});
        }
      },
      // 新增流程
      addProcess(title) {
        this.showTitle = title;
        this.open = true;
      },
      // 编辑流程
      editProcess(row, title) {
        getServiceProcess(row.serviceProcessId).then((response) => {
          this.processForm = response.data;
          this.open = true;
          this.showTitle = title;
        });
      },
      /** 提交按钮 */
      submitProcess: function () {
        this.$refs["processForm"].validate((valid) => {
          if (valid) {
            this.processForm.serviceItemId = this.selectTressNodeId

            if (this.processForm.serviceProcessId !== undefined) {
              updateServiceProcess(this.processForm).then((response) => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.closeProcess();
                  this._getProcessSteps();
                }
              });
            } else {
              addServiceProcess(this.processForm).then((response) => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.closeProcess();
                  this._getProcessSteps();
                }
              });
            }
          }
        });
      },
      closeProcess() {
        this.open = false;
        this.processForm = {}
        this.resetForm("processForm");
      },
      // 删除流程
      deleteProcess(row) {
        this.$confirm('是否确认删除该记录?', "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return deleteServiceProcess(row.serviceProcessId);
        }).then(() => {
          this._getProcessSteps();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
    },
    watch: {
      'selectTressNodeId': function () {
        if (this.isShowTable) {
          this._getProcessSteps();
        }
      },
    }
  }
</script>

<style lang="scss" scoped>
  .service-wrap {
    display: flex;
    -ms-flex-direction: row;
    flex-direction: row;
    width: 100%;
    height: 100%;
    overflow: hidden;
    padding: 12px 20px;
    box-sizing: border-box;
    background-color: #eceef0;

    .service-left-wrap {
      width: 200px;
      //height: 914px;
      -ms-flex: none;
      flex: none;
      overflow-x: hidden;
      overflow-y: auto;
      box-sizing: border-box;
      display: -ms-flexbox;
      display: flex;
      -ms-flex-direction: column;
      flex-direction: column;
    }

    .service-right-wrap {
      flex-grow: 1;
      display: -ms-flexbox;
      display: flex;
      -ms-flex-direction: row;
      flex-direction: row;
      margin-left: 12px;
      background-color: #f8f8f8;
      max-height: 914px;
      overflow: auto;

      .service-right-title {
        background: #fff;
        margin: 8px;
      }
    }
  }
  .card-config {
    min-height: 500px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    display: block;
    font-size: 14px;
    overflow-y: auto;
    overflow-x: scroll;
    border: 1px solid #f6f6f6;
    background-color: #f6f6f6;
  }
  ::v-deep.el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
    color: #ffffff;
    background-color: #1890ff;
  }
  ::v-deep .card-config .el-tree > .el-tree-node {
    min-width: 100%;
    display: inline-block;
  }
</style>
