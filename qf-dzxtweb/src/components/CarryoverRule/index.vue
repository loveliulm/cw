<template>
  <div>
    <table class="carryover"
           cellpadding="0"
           cellspacing="0">
      <thead class="carryover-th">
        <tr>
          <th rowspan="2">操作</th>
          <th rowspan="2">摘要</th>
          <th rowspan="2">凭证科目</th>
          <th rowspan="2">方向</th>
          <th colspan="4">公式</th>
        </tr>
        <tr>
          <th>取数来源科目</th>
          <th>取值类型</th>
          <!--<th>期间</th>-->
          <th>百分比</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item,index) of tableData.trData"
            :key="index">
          <td>
            <div>
              <i class="el-icon-circle-plus-outline"
                 @click='addtr(index)'></i>
              <i class="el-icon-remove-outline"
                 @click='deltr(index)'></i>
            </div>
          </td>
          <td class="summary">
            <el-input v-model="item.abstract.value" />
          </td>
          <td>
            <el-autocomplete type="text"
                             v-model="item.subject.value"
                             default-first-option
                             @select="((item)=>{subjectSelect(item,index)})"
                             :fetch-suggestions="subjectSearch">
            </el-autocomplete>
          </td>
          <td style="width:80px;">
            <el-select v-model="item.direction"
                       placeholder="借贷方向"
                       size="small">
              <el-option v-for="dict in directionOptions"
                         :key="dict.id"
                         :label="dict.name"
                         :value="dict.value" />
            </el-select>
          </td>
          <td>
            <el-autocomplete type="text"
                             :disabled="item.sourceSubject.isDisabled"
                             v-model="item.sourceSubject.value"
                             default-first-option
                             @select="((item)=>{sourceSubjectSelect(item,index)})"
                             :fetch-suggestions="sourceSubjectSearch">
            </el-autocomplete>
          </td>
          <td style="width:100px;">
            <el-select v-if="isProfit"
                       v-model="item.dateRule"
                       placeholder="取值类型"
                       size="small"
                       @change="((selVal)=>{dateRuleChange(selVal,index)})">
              <el-option v-for="dict in profitDateRules"
                         :key="dict.id"
                         :label="dict.value"
                         :value="dict.value" />
            </el-select>
            <el-select v-else
                       v-model="item.dateRule"
                       placeholder="取值类型"
                       size="small"
                       @change="((selVal)=>{dateRuleChange(selVal,index)})">
              <el-option v-for="dict in dateRules"
                         :key="dict.id"
                         :label="dict.value"
                         :value="dict.value" />
            </el-select>
          </td>
          <td style="width:80px;">
            <el-input-number v-model="item.percentage.value"
                                   :min="0"
                                   :controls="false"
                                   :precision="0"
                                   :disabled="item.percentage.isDisabled"></el-input-number>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
<script>
export default {
  name: "carryoverRule",
  props: ['tableData', 'subjectList', 'isProfit', 'sourceSubjectList'],
  data () {
    return {
      // 当前索引值
      nowIndex: null,
      // 借贷方向数组
      directionOptions: [{ id: 1, value: 'DEBIT', name: '借' }, { id: 2, value: 'CREDIT', name: '贷' }],
      // 取值类型数组
      dateRules: [{ id: 1, value: '本期发生额' }, { id: 2, value: '自动平衡' }],
      profitDateRules: [{ id: 1, value: '期末余额' }, { id: 2, value: '本期发生额' }, { id: 3, value: '自动平衡' }],
      // 是否禁用
      isDisabled: false,
    }
  },
  methods: {
    // 科目选择
    subjectSelect (item, index) {
      let nowIndex = index;
      let newitem = this.tableData.trData[nowIndex];
      newitem.subject.value = item.subjectNameBy;
      newitem.subject.subjectId = item.subjectId;
      newitem.subject.subjectCode = item.code;
      newitem.subject.isLeaf = item.isLeaf;
    },
    // 取数来源科目选择
    sourceSubjectSelect (item, index) {
      let nowindex = index;
      let newitem = this.tableData.trData[nowindex];
      newitem.sourceSubject.value = item.subjectNameBy;
      newitem.sourceSubject.sourceSubjectId = item.subjectId;
      newitem.sourceSubject.sourceSubjectCode = item.code;
    },
    //科目搜索
    subjectSearch (queryString, callback) {
      let sub = this.subjectList,
        result;
      if (queryString) {
        if (sub.filter(this.subjectCodeFilter(queryString)).length === 0) {
          result = this.subjectFormat(sub.filter(this.subjectValueFilter(queryString)));
        } else {
          result = this.subjectFormat(sub.filter(this.subjectCodeFilter(queryString))); //搜索编码
        }
      } else {
        result = this.subjectFormat(sub);
      }
      callback(result);
    },
    // 取数来源科目搜索
    sourceSubjectSearch (queryString, callback) {
      let sub = this.sourceSubjectList,
        result;
      if (queryString) {
        if (sub.filter(this.subjectCodeFilter(queryString)).length === 0) {
          result = this.subjectFormat(sub.filter(this.subjectValueFilter(queryString)));
        } else {
          result = this.subjectFormat(sub.filter(this.subjectCodeFilter(queryString))); //搜索编码
        }
      } else {
        result = this.subjectFormat(sub);
      }
      callback(result);
    },
    subjectCodeFilter (queryString) {
      return res => {
        return res.code.indexOf(queryString.toLowerCase()) === 0;
      };
    },
    subjectValueFilter (queryString) {
      return res => {
        return res.name.indexOf(queryString.toLowerCase()) !== -1;
      };
    },
    // 拼接科目数据
    subjectFormat (subjectList) {
      let array = Object.prototype.toString.call(subjectList) === '[object Array]' ? [] : {};
      let arr = subjectList;
      arr.forEach(item => {
        item.value = item.subjectNameBy;//item.code + ' ' + item.name;
      })
      return arr;
    },
    // 取值类型改变
    dateRuleChange (selVal, index) {
      let nowIndex = index;
      let newitem = this.tableData.trData[nowIndex];
      if (selVal == '自动平衡') {
        if (newitem.sourceSubject.value != '' && newitem.percentage.value != '') {
          newitem.sourceSubject.isDisabled = true;
          newitem.percentage.isDisabled = true;
        } else {
          if (newitem.sourceSubject.value == '') {
            this.msgError('请选择【取数来源科目】');
            newitem.dateRule = '期末余额';
            newitem.sourceSubject.isDisabled = false;
          }
          if (newitem.percentage.value == '') {
            this.msgError('请输入百分比');
            newitem.dateRule = '期末余额';
            newitem.percentage.isDisabled = false;
          }
        }
      } else {
        newitem.sourceSubject.isDisabled = false;
        newitem.percentage.isDisabled = false;
      }
    },

    //添加行
    addtr (n) {
      this.$emit("addtr", n)
    },

    //删除行
    deltr (n) {
      this.$emit("deltr", n);
    },
  },
  watch: {
    isProfit () {

    }
  }
}
</script>
<style lang="scss" scoped>
.carryover {
  width: 100%;
  text-align: center;
  border-collapse: collapse;
  padding: 2px;
  .carryover-th th {
    border: 1px solid #dfe6ec;
    padding: 4px;
    background-color: #f7f7f7;
    color: #909399;
    font-weight: 500;
  }
  tbody tr {
    td {
      border: 1px solid #dfe6ec;
      padding: 4px;
    }
    .summary input {
      padding: 4px 6px;
      border: 0;
    }
  }
}
/deep/.el-input-number.is-without-controls .el-input__inner {
  text-align: left !important;
}
</style>