<template>
  <div class="app-container">
    <div class="service-wrap">
      <div class="service-left-wrap">
        <el-tree :data="serviceTypes"
                 :props="defaultProps"
                 ref="vueTree"
                 node-key="servieTypeId"
                 highlight-current
                 @node-click="handleNodeClick"
                 default-expand-all></el-tree>
      </div>
      <div class="service-right-wrap">
        <div style="width:100%;">
          <el-table v-loading="loading"
                    :data="serviceItems">
            <!--<el-table-column type="selection"
                             width="55"
                             align="center" />-->
            <!--<el-table-column label="序号"
                             align="center"
                             prop="serviceItemId" />-->-->
            <!--<el-table-column label="编码"
                             align="center"
                             prop="serviceItemId" />-->
            <el-table-column label="服务"
                             align="center"
                             prop="serviceItemName" />
            <el-table-column label="所属类型"
                             align="center"
                             prop="serviceTypeName" />
            <!--<el-table-column label="服务周期"
                             align="center"
                             prop="vatType" />
            <el-table-column label="有无流程"
                             align="center"
                             prop="normalContactPhone" />-->
          </el-table>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { serviceTypeTree, getServiceItems } from "@/api/system/servicetype";

export default {
  name: 'serviceType',
  data () {
    return {
      // 服务类型列表
      serviceTypes: [],
      // 树型服务流程节点显示
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      // 树型服务类型选中的节点
      selectTressNodeId: null,
      // 服务项目
      serviceItems: [],
      // 表示数据正在加载
      loading: true,
    }
  },
  created () {
    this.initData()
  },
  methods: {
    // 注意:初始化节点数据时，默认选中的node节点一定要有serviceItemId字段否则会报错
    // 必须先加载出数据后才能设置node-key
    async initData () {
      var res = await this._getServiceTypes();
      this.serviceTypes = res;
      // 默认选中某个节点
      this.$nextTick(() => {
        this.$refs.vueTree.setCurrentKey(res[0].children[0].children[0].servieTypeId);
        this.getInitNodeId();
      })
    },
    // 默认选中节点对应的服务步骤
    getInitNodeId () {
      let node = this.$refs.vueTree.getCurrentNode();
      this.selectTressNodeId = node.servieTypeId;
    },
    // 深拷贝数组组合树型服务流程数据
    _deepClone (source) {
      const targetObj = Object.prototype.toString.call(source) === '[object Array]' ? [] : {}; // 判断复制的目标是数组还是对象
      for (let keys in source) {
        if (source.hasOwnProperty(keys)) {
          if (source[keys] && typeof source[keys] === 'object') { // 如果值是对象，就递归一下
            if (keys == 'serviceTypeVos') {
              targetObj['children'] = Object.prototype.toString.call(source[keys]) === '[object Array]' ? [] : {};
              targetObj['children'] = this._deepClone(source[keys]);
            } else {

              targetObj[keys] = Object.prototype.toString.call(source[keys]) === '[object Array]' ? [] : {};
              targetObj[keys] = this._deepClone(source[keys]);
            }
          } else { // 如果不是，就直接赋值
            if (keys == 'name') {
              targetObj['label'] = source[keys];
            } else {
              targetObj[keys] = source[keys];
            }
          }
        }
      }
      return targetObj;
    },
    // 获取服务类型
    _getServiceTypes () {
      this.loading = true;
      return new Promise((resolve) => {
        serviceTypeTree().then(res => {
          let that = this;
          let array = [];
          let object = {
            label: '服务类型设置',
            children: that._deepClone(res.rows)
          }
          //this.serviceTypes.push(object);
          array.push(object);
          this.loading = false;
          resolve(array)
        })
      })

    },
    // 获取服务项目
    _getServiceItems () {
      let serviceTypeId = this.selectTressNodeId;
      getServiceItems({ serviceTypeId }).then(res => {
        this.serviceItems = res.rows;
      })
    },
    // 点击树型服务类型节点执行回调函数
    handleNodeClick (data, node) {
      // 前期接口问题通知过杨洋
      this.selectTressNodeId = data.servieTypeId;
    }
  },
  watch: {
    // 监听节点变化
    'selectTressNodeId': function () {
      this._getServiceItems();
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
    .service-left-content {
      flex-grow: 0;
      width: 180px;
      position: relative;
      overflow-y: hidden;
      box-sizing: border-box;
    }
  }
}
</style>