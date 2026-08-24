<template>
  <div class="app-container">
    <div class="wagesModel">
      <div class="wagesModel-tab">
        <el-table :data="wagesModellist"
                  v-loading="loading"
                  border>
          <el-table-column label="操作"
                           width="100"
                           align="center">
            <template slot-scope="scope">
              <el-button type="text"
                         size="small"
                         @click.stop="editWagesModel(scope.row)"
                         class="el-icon-edit">
              </el-button>
            </template>
          </el-table-column>
          <el-table-column prop="type"
                           label="模板类型">
          </el-table-column>
          <el-table-column prop="name"
                           label="模板名称">
          </el-table-column>
          <el-table-column prop="remark"
                           label="模板说明">
          </el-table-column>
        </el-table>
      </div>
    </div>
    <!-- 修改工资凭证模板弹出框--->
    <el-dialog :title="title"
               :visible.sync="openWagesModel"
               :close-on-click-modal="false"
               size="tiny">
      <el-form ref="form"
               :model="form"
               :rules="rules"
               label-width="80px"
               label-position="left">
        <el-form-item label="模板名称"
                      prop="name">
          <el-input v-model="form.name"
                    placeholder="请输入模板名称"
                    style="width:50%;" />
        </el-form-item>
        <el-form-item label="模板说明"
                      prop="remark">
          <el-input type="textarea"
                    v-model="form.remark"
                    placeholder="请输入模板说明" />
        </el-form-item>
        <div>
          <table class="carryover"
                 cellpadding="0"
                 cellspacing="0">
            <thead class="carryover-th">
              <tr>
                <th>操作</th>
                <th>摘要</th>
                <th>凭证科目</th>
                <th>借方</th>
                <th>贷方</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item,index) of tableData"
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
                  <el-input v-model="item.abstract.val" />
                </td>
                <td>
                  <el-autocomplete type="text"
                                   style="width:100%;"
                                   v-model="item.subject.value"
                                   default-first-option
                                   @select="((item)=>{subjectSelect(item,index)})"
                                   :fetch-suggestions="subjectSearch">
                  </el-autocomplete>
                  <!--暂不开启选择辅助核算功能-->
                  <!--<el-popover placement="bottom"
                              title="标题"
                              width="200"
                              trigger="manual"
                              content="这是一段内容,这是一段内容,这是一段内容,这是一段内容。"
                              v-model="item.showAux">
                  </el-popover>-->
                </td>
                <td style="width:100px;">
                  <el-select v-model="item.DeCr.De_val"
                             placeholder="借方"
                             @change="((selVal)=>{deChange(selVal,index)})"
                             size="small">
                    <el-option v-for="(dict,index) in decrList"
                               :key="index"
                               :label="dict.name"
                               :value="dict.value" />
                  </el-select>
                </td>
                <td style="width:100px;">
                  <el-select v-model="item.DeCr.Cr_val"
                             placeholder="贷方"
                             @change="((selVal)=>{crChange(selVal,index)})"
                             size="small">
                    <el-option v-for="(dict,index) in decrList"
                               :key="index"
                               :label="dict.name"
                               :value="dict.value" />
                  </el-select>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </el-form>
      <div slot="footer"
           class="dialog-footer">
        <el-button type="primary"
                   @click="submitForm">保 存</el-button>
        <el-button @click="close">取 消</el-button>
      </div>
    </el-dialog>
    <!--辅助核算对话框-->
    <!--<el-popover v-if="dialogAuxRefreshVisible"
                ref="popover"
                placement="bottom-start"
                width="320"
                v-model="dialogAuxVisible"
                @show="dialogAuxOpen"
                @hide="dialogAuxClose">
    </el-popover>-->
  </div>
</template>
<script>
import { wagesModelList, wagesModelDetail, saveWagesModel } from "@/api/wages/wagesModel"
import { subjectList } from "@/api/account/settleAccounts"
import voucherTrObj from "@/assets/js/voucherTrObj.js";
import { getSubjectVoucher } from "@/api/voucher/viewvoucher";



export default {
  name: 'Wages',
  data () {
    return {
      // 是否正在加载
      loading: true,
      // 工资凭证模板列表
      wagesModellist: [],
      // bookId
      bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId,
      // 是否打开工资凭证模板修改弹出框
      openWagesModel: false,
      // 弹框名称
      title: "",
      // 工资凭证模板表单
      form: {},
      // 凭证科目
      subjectList: [],
      // 借贷列表
      decrList: [{ value: 'MEDICARE_INSURANCE', name: '基本养老保险费' }, { value: 'ENDOWMENT_INSURANCE', name: '基本医疗保险费' }, { value: 'UNEMPLOYED_INSURANCE', name: '失业保险费' },
      { value: 'SICKNESS_INSURANCE', name: '大病保险' }, { value: 'HOUSING_FUND', name: '住房公积金' }, { value: 'WITH_HOLDING_TAX', name: '本月应缴个税' },
      { value: 'NET_PAY', name: '实发工资' }, { value: 'GROSS_PAY', name: '应发工资' }, { value: 'OTHER_DEDUCTIONS', name: '其他扣除' }],
      // 工资凭证模板数据
      tableData: [],
      // 辅助核算框是否弹出
      auxOpen: false,
      // 工资凭证模板表单校验
      rules: {
        name: [
          { required: true, message: '请输入模板名称' }
        ]
      },
    }
  },
  created () {
    this.getWagesModelList();
    this.getSubjectList();

  },
  methods: {
    // 获取工资凭证模板列表
    getWagesModelList () {
      this.loading = true;
      wagesModelList({ bookId: this.bookId }).then(res => {
        this.wagesModellist = res.rows;
        this.loading = false;
      })
    },
    // 清空表单
    reset () {
      this.form = {
        name: null,
        remark: null,
        modelId: null
      };
      this.tableData = [];
      this.resetForm("form");
    },
    // 关闭弹窗
    close () {
      this.clearFormValidate("form");
      this.openWagesModel = false;
    },
    // 编辑
    editWagesModel (row) {
      this.reset();
      this.title = row.name;
      let modelId = row.modelId;
      wagesModelDetail(modelId).then(res => {
        this.form.name = res.data.name;
        this.form.remark = res.data.remark;
        this.form.modelId = res.data.modelId;
        this.openWagesModel = true;

        this.tableData = this.dataWagesModel(res.data.detail);
      })
    },
    // 格式化工资凭证模板数据
    dataWagesModel (data) {
      let arr = data;
      let array = [];
      arr.forEach(detail => {
        let item = { abstract: {}, subject: {}, DeCr: {} };
        item.abstract.val = detail.summary;
        item.subject.subjectId = detail.subjectId;
        item.subject.value = detail.subjectNameBy;
        item.DeCr.state = detail.balanceDirection == 'DEBIT' ? true : false;
        item.DeCr.De_val = detail.balanceDirection == 'DEBIT' ? detail.filedCode : '';
        item.DeCr.Cr_val = detail.balanceDirection == 'CREDIT' ? detail.filedCode : '';
        item.isaux = detail.auxiliarys.length == 0 ? false : true;
        //item.showAux = detail.auxiliarys.length == 0 ? false : true;
        item.subject.auxData = detail.auxiliarys;
        array.push(item);
      })
      return array;
    },
    // 工资凭证模板数据格式化
    wagesModelData () {
      let array = this.tableData;
      let data = {};
      let messages = [];
      let details = [];
      array.forEach((item, index) => {
        let detail = {};
        let tip = { message: '' };
        detail.summary = item.abstract.val;
        tip.message += item.abstract.val == '' ? '请输入摘要,' : '';
        detail.subjectId = item.subject.subjectId;
        tip.message += item.subject.value == '' ? '请选择科目,' : '';
        // 借方 && 贷方方已选择
        if (item.DeCr.state && item.DeCr.De_val != '') {
          detail.balanceDirection = 'DEBIT';
          detail.filedCode = item.DeCr.De_val;
        } else if (!item.DeCr.state && item.DeCr.Cr_val != '') {
          detail.balanceDirection = 'CREDIT';
          detail.filedCode = item.DeCr.Cr_val;
        } else if (item.DeCr.state && item.DeCr.Cr_val != '') {
          detail.balanceDirection = 'CREDIT';
          detail.filedCode = item.DeCr.Cr_val;
        } else if (!item.DeCr.state && item.DeCr.De_val != '') {
          detail.balanceDirection = 'DEBIT';
          detail.filedCode = item.DeCr.De_val;
        } else {
          tip.message += '请选择借方或贷方,'
        }
        details.push(detail);
        tip.id = index;
        messages.push(tip);
      })
      data.modelId = this.form.modelId;
      data.name = this.form.name;
      data.remark = this.form.remark;
      data.details = details;
      return { data, messages };
    },
    // 保存工资凭证模板
    submitForm () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          let data = this.wagesModelData();
          let wagesModelData = data.data;
          let saveData = this.warningInfo(data.messages);
          if (saveData.save) {
            saveWagesModel(wagesModelData).then(res => {
              this.msgSuccess("修改成功");
              this.openWagesModel = false;
              this.getWagesModelList();
            })
          } else {
            this.msgError(saveData.result);
            this.openWagesModel = true;
            return;
          }
        }
      })
    },
    // 处理保存时存在的警告信息
    warningInfo (arr) {
      let array = arr;
      let count = 0;
      let save = true;
      let result = '';
      array.forEach(item => {
        if (item.message != "") {
          result += "第" + parseInt(item.id + 1) + "行" + item.message + "<br/>";
          // 获取result 最后一个字符，如果为逗号将逗号去掉
          result = result.charAt(result.length - 6).indexOf(",") != -1 ? this.sliceLastSymbol(result) : result;
          save = false;
        } else {
          count++;
        }
        if (count == array.length) {
          save = true;
        }
      });
      return { save, result };
    },
    // 获取科目列表
    getSubjectList () {
      return new Promise((resolve, reject) => {
        getSubjectVoucher({ bookId: this.bookId }).then(res => {
          this.subjectList = res.rows;
          resolve("success");
        });
      });
    },
    // 借方选择选项
    deChange (selVal, index) {
      let nowIndex = index;
      let newItem = this.tableData[nowIndex];
      if (newItem.DeCr.Cr_val != '' && selVal != '') {
        newItem.DeCr.Cr_val = '';
        newItem.DeCr.state = true;
      }
    },
    // 贷方选择选项
    crChange (selVal, index) {
      let nowIndex = index;
      let newItem = this.tableData[nowIndex];
      if (newItem.DeCr.De_val != '' && selVal != '') {
        newItem.DeCr.De_val = '';
        newItem.DeCr.state = false;
      }
    },
    // 科目选择
    subjectSelect (item, index) {
      let nowIndex = index;
      let newItem = this.tableData[nowIndex];
      newItem.subject.value = item.subjectNameBy;
      newItem.subject.subjectId = item.subjectId;
      if (item.isAuxiliary) {
        newItem.isaux = true;
        //newItem.showAux = true;
      }
    },
    // 判断科目开启了那些辅助核算

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
    //添加行
    addtr (i) {
      let tr = voucherTrObj();
      let tableData = this.tableData;
      if (i !== undefined) {
        tr.key = Math.random() + (i + 1);
        tableData.splice(i + 1, 0, tr);
      } else {
        tr.key = Math.random();
        tableData.push(tr);
      }
    },

    // 删除行
    deltr (n) {
      if (this.tableData.length <= 2) {
        this.$message.warning("至少保留2条数据！");
      } else {
        this.tableData.splice(n, 1);
      }
    }

  }
}
</script>
<style lang="scss" scoped>
.wagesModel {
  display: flex;
  -ms-flex-direction: column;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  overflow: hidden;
  padding: 12px 20px;
  box-sizing: border-box;
  .wagesModel-tab {
    width: 98%;
    display: flex;
    flex-flow: row wrap;
    justify-content: space-between;
    align-items: center;
    margin: 3px 10px;
  }
}
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