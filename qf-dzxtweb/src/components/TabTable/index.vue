<template>
  <div>
    <el-table :data="tableData"
              border
              max-height="680"
              style="width:100%;"
              v-loading='loading'
              @cell-mouse-enter="show"
              @cell-mouse-leave="disappear">
      <el-table-column prop="code"
                       label="科目编码"
                       width="90">
      </el-table-column>
      <el-table-column label="科目名称"
                       width="370">
        <template slot-scope="scope">
          <span>{{scope.row.fullName}}</span>
          <span :class="scope.row.isCustomer?'customer':''">{{scope.row.isCustomer?'客':''}}</span>
          <span :class="scope.row.isStock?'stock':''">{{scope.row.isStock?'库':''}}</span>
          <span :class="scope.row.isSupplier?'supplier':''">{{scope.row.isSupplier?'供':''}}</span>
          <span :class="scope.row.isDepartment?'department':''">{{scope.row.isDepartment?'部':''}}</span>
          <span :class="scope.row.isPerson?'person':''">{{scope.row.isPerson?'人':''}}</span>
          <span :class="scope.row.isProject?'project':''">{{scope.row.isProject?'项':''}}</span>

          <!-- 显示数量核算是否开启 -->
          <span :class="scope.row.isQuantity?'quantity':''">{{scope.row.isQuantity?'数':''}}</span>

          <span v-hasPermi="['accounting:subject:add']">
            <i class="el-icon-circle-plus-outline right "
             v-if="scope.row.isShow"
             @click='handleAdd(scope.row)'></i>
          </span>   
          <span v-hasPermi="['accounting:subject:edit']">
            <i class="el-icon-edit right"
             v-if="scope.row.isShow"
             @click='handleEdit(scope.row)'></i>
          </span>
          <span v-hasPermi="['accounting:subject:del']">
            <i class="el-icon-delete right"
             v-if="scope.row.isShow && scope.row.deletable"
             @click='handleDel(scope.row)'></i>
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="balanceDirectionText"
                       label="方向"
                       width="50">
      </el-table-column>
      <el-table-column prop="balanceInit"
                       label="期初余额">
        <template slot-scope="scope">
          <el-input :disabled="!scope.row.balanceInitChecked || !isChange"
                    size="mini"
                    v-model="scope.row.balanceInit"
                    @focus="showAuxiliary(scope.row)"
                    @blur='computedBalanceInit(scope.row)' />
        </template>
      </el-table-column>
      <el-table-column prop="debit"
                       label="本年累计借方">
        <template slot-scope="scope">
          <el-input :disabled="!scope.row.creditChecked || !isChange"
                    size="mini"
                    v-model="scope.row.debit"
                    @focus="showAuxiliary(scope.row)"
                    @blur='computedDebit(scope.row)' />
        </template>
      </el-table-column>
      <el-table-column prop="credit"
                       label="本年累计贷方">
        <template slot-scope="scope">
          <el-input :disabled="!scope.row.debitChecked || !isChange"
                    size="mini"
                    v-model="scope.row.credit"
                    @focus="showAuxiliary(scope.row)"
                    @blur='computedCredit(scope.row)' />
        </template>
      </el-table-column>
      <el-table-column label="年初余额"
                       prop="yearBalance"
                       :formatter="yearBalanceFormatter">
      </el-table-column>
      <!--<el-table-column label="科目状态">
      </el-table-column>-->
    </el-table>
  </div>
</template>
<script>
export default {
  name: 'TabTable',
  props: {
    tableData: {
      type: Array,
      default: []
    },
    loading: {
      type: Boolean,
      default: true
    },
    isChange: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      isShow: false,
      selectRow: {},

    }
  },
  methods: {
    show (row) {
      row.isShow = true;
      this.selectRow = row;
    },
    disappear (row) {
      row.isShow = false;
      this.selectRow = {};
    },
    // 新增
    handleAdd (row) {
      this.$emit('handleAdd', row);
    },
    // 编辑
    handleEdit (row) {
      this.$emit('handleEdit', row);
    },
    // 删除
    handleDel (row) {
      this.$emit('handleDel', row);
    },
    // 格式化期初余额
    balanceInitFormatter (row) {
      return row.balanceInit == 0 ? '' : row.balanceInit;
    },
    // 格式化年初年初余额
    yearBalanceFormatter (row) {
      return row.yearBalance == 0 ? '' : row.yearBalance;
    },
    // 期初余额输入数据
    computedBalanceInit (row) {
      this.$emit('computedBalanceInit', row);
    },
    // 本年累计借方输入数据
    computedDebit (row) {
      this.$emit('computedDebit', row);
    },
    // 本年累计贷方输入数据
    computedCredit (row) {
      this.$emit('computedCredit', row);
    },
    // 显示辅助核算弹框
    showAuxiliary (row) {
      this.$emit('showAuxiliary', row);
    }

  }
}
</script>
<style lang="scss" scoped>
@import "~@/assets/styles/variables.scss";
@import "~@/assets/styles/mixin.scss";

.el-table--medium td {
  padding: 5px 0 !important;
}
.right {
  float: right !important;
  margin-right: 3px;
}
[class^="el-icon-"] {
  line-height: 23px !important;
}
[class^="el-icon-"]:hover {
  color: $light-blue !important;
  font-size: 20px !important;
  cursor: pointer;
}

.customer {
  @include servicetype(rgb(0, 140, 255));
}
.stock {
  @include servicetype(rgb(89, 212, 158));
}
.supplier {
  @include servicetype(rgb(212, 129, 89));
}
.department {
  @include servicetype(rgb(67, 68, 236));
}
.person {
  @include servicetype(rgb(144, 147, 153));
}
.project {
  @include servicetype(rgb(160, 160, 245));
}

.quantity {
  @include servicetype(rgb(241, 171, 206));
}
</style>