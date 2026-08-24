<template>
  <div>
    <table class="carryover" cellpadding="0" cellspacing="0">
      <thead class="carryover-th">
        <tr>
          <th>操作</th>
          <th>{{ auxiliaryName }}</th>
          <th>期初余额</th>
          <th>本年累计-借方</th>
          <th>本年累计-贷方</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) of tableData" :key="item.auxiliaryId">
          <td>
            <div>
              <i
                class="el-icon-circle-plus-outline"
                @click="addtr({ i: index, arr: tableData })"
              ></i>
              <i
                class="el-icon-remove-outline"
                @click="deltr({ i: index, arr: tableData })"
              ></i>
            </div>
          </td>
          <td>
            <el-autocomplete
              type="text"
              v-model="item.auxiliary.value"
              default-first-option
              @select="
                item => {
                  auxiliaryhSelect(item, index);
                }
              "
              :fetch-suggestions="auxiliarySearch"
            >
            </el-autocomplete>
          </td>
          <td class="summary">
            <el-input v-model="item.balance" @blur="cleanNoNumber(item,'balance')" />
          </td>
          <td class="summary">
            <el-input v-model="item.debit" @blur="cleanNoNumber(item, 'debit')" />
          </td>
          <td class="summary">
            <el-input v-model="item.credit" @blur="cleanNoNumber(item, 'credit')" />
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
<script>
export default {
  name: "AuxiliaryTable",
  props: {
    auxiliaryName: {
      type: String
    },
    tableData: {
      type: Array,
      default: []
    },
    auxiliaryList: {
      type: Array,
      default: []
    },
    // 判断何时返回数据
    backData: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    //添加行
    addtr(n) {
      this.$emit("addtr", n);
    },
    //删除行
    deltr(n) {
      this.$emit("deltr", n);
    },
    // 返回数据
    backList() {
      this.$emit("backList", this.tableData);
    },
    // 辅助核算选择
    auxiliaryhSelect(item, index) {
      let tableData = this.tableData[index];
      tableData.auxiliary.value = item.value;
      tableData.auxiliary.auxiliaryId = item.auxiliaryId;
      tableData.auxiliary.auxiliaryTypeId = item.auxiliaryTypeId;
    },
    //辅助核算搜索
    auxiliarySearch(queryString, callback) {
      let sub = this.auxiliaryList,
        result;
      if (queryString) {
        if (sub.filter(this.auxiliaryCodeFilter(queryString)).length === 0) {
          result = this.auxiliaryFormat(
            sub.filter(this.auxiliaryCodeFilter(queryString))
          );
        } else {
          result = this.auxiliaryFormat(
            sub.filter(this.auxiliaryValueFilter(queryString))
          ); //搜索编码
        }
      } else {
        result = this.auxiliaryFormat(sub);
      }
      callback(result);
    },
    auxiliaryCodeFilter(queryString) {
      return res => {
        return res.code.indexOf(queryString.toLowerCase()) !== 0;
      };
    },
    auxiliaryValueFilter(queryString) {
      return res => {
        return res.name.indexOf(queryString.toLowerCase()) !== -1;
      };
    },
    // 拼接科目数据
    auxiliaryFormat(auxiliaryList) {
      let array = auxiliaryList;
      array.forEach(item => {
        item.value = item.code + " " + item.name;
      });
      return array;
    },
    // 文本框校验正能输入数字
    cleanNoNumber(item, str){
      switch(str){
        case 'balance': item.balance = this.validNumber(item.balance);
                        break;
        case 'debit': item.debit = this.validNumber(item.debit);
                      break;
        case 'credit': item.credit = this.validNumber(item.credit);
                       break;
      }
    },
    // 判断只能输入纯数字或者小数点，及负整数
    validNumber(str){
      let value = str;
      let t = value.charAt(0);
      //先把非数字的都替换掉，除了数字和.
      value = value.replace(/[^\d.]/g,"");
      //保证只有出现一个.而没有多个.
      value = value.replace(/\.{2,}/g,".");
      //必须保证第一个为数字而不是.
      value  =value.replace(/^\./g,"");
      //保证.只出现一次，而不能出现两次以上
      value  = value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
      //只能输入两个小数
      //item.balance  = item.balance.replace(/^(\d+)\.(\d\d).*$/,'$1$2.$3');
      //如果第一位是负号，则允许添加 
      if(t == "-"){
        value = "-" + value;
      }
      return value;
    },
  },
  watch: {
    backData() {
      if (this.backData) {
        this.backList();
      }
    },
    
  }
};
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
</style>
