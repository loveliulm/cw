<template>
  <div class="app-container">
    <div class="assetsAdd-wrap">
      <div class="assetsAdd-tab">
        <el-button type="primary"
                   @click="addClick">保存</el-button>
        <el-button type="primary"
                   @click="handleDel">删除</el-button>
        <el-button v-if="showClearStatus"
                   @click="clearCard(false)">清理</el-button>
        <el-button v-else
                   @click="clearCard(true)">取消清理</el-button>
      </div>
      <div class="assetsAdd-content">
        <div class="assetsAdd-form">
          <el-form ref="form"
                   :model="form"
                   :rules="rules"
                   label-width="110px"
                   label-position="left">
            <el-row :gutter="10">
              <el-col :span="8">
                <el-form-item label="资产编码"
                              prop="cardCode">
                  <el-input class="ipt_width"
                            v-model="form.cardCode"
                            disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="资产名称"
                              prop="name">
                  <el-input class="ipt_width"
                            v-model="form.name"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="8">
                <el-form-item label="资产类别"
                              prop="categoryId">
                  <el-select v-model="form.categoryId"
                             @change="categoryChange">
                    <el-option v-for="item in categroyList"
                               :key="item.categoryId"
                               :label="item.code + ' ' + item.name"
                               :value="item.categoryId"
                               size="small">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="录入期间"
                              prop="yearPeriod">
                  <el-input class="ipt_width"
                            v-model="form.yearPeriod"
                            disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="8">
                <el-form-item label="使用部门"
                              prop="departmentId">
                  <el-select v-model="form.departmentId">
                    <el-option v-for="item in departmentList"
                               :key="item.auxiliaryId"
                               :label="item.name"
                               :value="item.auxiliaryId">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="开始使用日期"
                              prop="startDate">
                  <el-date-picker style="wisth:140px;"
                                  v-model="form.startDate"
                                  :default-value="currentDate"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="选择日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="清理期间"
                              prop="cleanPeriod">
                  <el-input class="ipt_width"
                            v-model="form.cleanPeriod"
                            disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <div class="tab-tip">
              <h4>折旧方式</h4>
              <div class="tab-border"></div>
            </div>
            <el-row :gutter="10">
              <el-col :span="8">
                <el-form-item label="折旧方式"
                              prop="depreciationMethod">
                  <el-select v-model="form.depreciationMethod">
                    <el-option v-for="item in depreciationMethods"
                               :key="item.dictValue"
                               :label="item.dictLabel"
                               :value="parseInt(item.dictValue)">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="预计使用期限"
                              prop="term">
                  <el-input class="ipt_width"
                            v-model="form.term"></el-input>
                  <span style="display:inline;">期</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="8">
                <el-form-item label="固定资产科目"
                              prop="fixedAssetSubjectId">
                  <el-autocomplete type="text"
                                   style="width:200px;"
                                   v-model="form.fixedAssetSubjectNameBy"
                                   default-first-option
                                   @select="((item)=>{subjectSelect(item,'fixedAssetSubjectId')})"
                                   :fetch-suggestions="subjectSearch">
                  </el-autocomplete>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="累计折旧科目"
                              prop="depreciationSubjectId">
                  <el-autocomplete type="text"
                                   style="width:200px;"
                                   default-first-option
                                   v-model="form.depreciationSubjectNameBy"
                                   @select="((item)=>{subjectSelect(item,'depreciationSubjectId')})"
                                   :fetch-suggestions="subjectSearch">
                  </el-autocomplete>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="折旧费用科目"
                              prop="depreciationFeeSubjectId">
                  <el-autocomplete type="text"
                                   style="width:200px;"
                                   default-first-option
                                   v-model="form.depreciationFeeSubjectNameBy"
                                   @select="((item)=>{subjectSelect(item,'depreciationFeeSubjectId')})"
                                   :fetch-suggestions="subjectSearch">
                  </el-autocomplete>
                </el-form-item>
              </el-col>
            </el-row>
            <div class="tab-tip">
              <h4>原值、净值、累计折旧</h4>
              <div class="tab-border"></div>
            </div>
            <el-row :gutter="10">
              <el-col :span="8">
                <el-form-item label="原值"
                              prop="originalValue">
                  <el-input-number v-model="form.originalValue"
                                   :min="1"
                                   :controls="false"
                                   :precision="2"></el-input-number>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="8">
                <el-form-item label="残值率"
                              prop="residualRate">
                  <el-input-number v-model="form.residualRate"
                                   :min="0"
                                   :controls="false"
                                   :precision="2"></el-input-number>
                  <span style="display:inline;">%</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="预计残值"
                              prop="residualRatePre">
                  <el-input-number v-model="residualRatePre"
                                   :min="0"
                                   :controls="false"
                                   :precision="2"
                                   disabled></el-input-number>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="8">
                <el-form-item label="减值准备"
                              prop="impairmentPrepare">
                  <el-input-number v-model="form.impairmentPrepare"
                                   :min="0"
                                   :controls="false"
                                   :precision="2"></el-input-number>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="已折旧期间"
                              prop="alreadyDepreciationDate">
                  <el-input class="ipt_width"
                            v-model="form.alreadyDepreciationDate"></el-input>
                  <span>期</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="8">
                <el-form-item label="期初累计折旧"
                              prop="startDepreciation">
                  <el-input-number v-model="startDepreciation"
                                   :min="0"
                                   :controls="false"
                                   :precision="2"></el-input-number>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="期初净值"
                              prop="startNetValue">
                  <el-input-number v-model="startNetValue"
                                   :min="0"
                                   :controls="false"
                                   :precision="2"
                                   disabled></el-input-number>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="8">
                <el-form-item label="月折旧"
                              prop="monthDepreciation">
                  <el-input-number v-model="monthDepreciation"
                                   :min="0"
                                   :controls="false"
                                   :precision="2"></el-input-number>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="16">
                <el-form-item label="备注"
                              prop="remark">
                  <el-input type="textarea"
                            v-model="form.remark"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <div class="showClearStstus">
            <img v-if="showClearStatus"
                 src="@/assets/image/usesuccess.png"
                 alt="正常使用">
            <img v-else
                 src="@/assets/image/useclear.png"
                 alt="已清理">
          </div>
        </div>

      </div>
      <div class="assetsAdd-footer">
      </div>
    </div>
  </div>
</template>
<script>
import { fixedAssertCategroy, delAsset } from "@/api/fixedassets/card";
import { auxiliaryList, DEPARTMENT } from "@/api/system/auxiliary";
import { assetConfig, assetCode, saveAsset, assetDetail, editAsset, fixedAssetCardClean } from "@/api/fixedassets/assetAdd";
import { currentPeriod, getSubjectVoucher } from "@/api/voucher/viewvoucher";
import { toInteger } from "@/utils/ruoyi";
import { assetCategoryDetail } from "@/api/fixedassets/assetCategory"

export default {
  name: 'AssetEdit',
  data () {
    return {
      // 新增工资表单
      form: {
        cardCode: null, // 编号
        startDate: null, // 开始使用日期
        yearPeriod: null, // 录入期间
        term: 0, // 预计使用期限
        cleanPeriod: null, // 清理期间
        departmentId: null, // 部门Id
        fixedAssetSubjectId: null, // 固定资产科目Id
        fixedAssetSubjectNameBy: null,
        depreciationSubjectId: null, // 累计折旧科目Id
        depreciationSubjectNameBy: null,
        depreciationFeeSubjectId: null, // 折旧费用科目Id
        depreciationFeeSubjectNameBy: null,
        originalValue: 0, // 原值
        residualRate: 0, // 残值率
        residualRatePre: 0, // 预计残值
        impairmentPrepare: 0, // 减值准备
        alreadyDepreciationDate: 0, // 已折旧期间
        startDepreciation: 0, // 期初累计折旧
        startNetValue: 0, // 期初净值
        monthDepreciation: 0, // 月折旧
        status: false // 清理状态
      },
      // 资产类别
      categroyList: [],
      // bookId
      bookId: this.getUrlBookId(),//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId,
      // 当前账期
      currentPeriod: null,
      // 部门
      departmentList: [],
      // 折旧方式
      depreciationMethods: [],
      // 科目列表
      subjectlist: [],
      // 表单验证规则
      rules: {
        cardCode: [
          { required: true, message: '资产编码不能为空' }
        ],
        name: [
          { required: true, message: '资产名称不能为空' }
        ],
        categoryId: [
          { required: true, message: '资产类别不能为空' }
        ],
        departmentId: [
          { required: true, message: '使用部门不能为空' }
        ],
        yearPeriod: [
          { required: true, message: '录入期间不能为空' }
        ],
        startDate: [
          { required: true, message: '开始使用日期不能为空' }
        ],
        depreciationMethod: [
          { required: true, message: '折旧方法不能为空' }
        ],
        term: [
          { required: true, message: '预计使用期限不能为空' }
        ],
        fixedAssetSubjectId: [
          { required: true, message: '固定资产不能为空' }
        ],
        depreciationSubjectId: [
          { required: true, message: '累计折旧不能为空' }
        ],
        depreciationFeeSubjectId: [
          { required: true, message: '折旧费用科目不能为空' }
        ],
        originalValue: [
          { required: true, message: '原值不能为空' }
        ],
      },
      // 默认开始使用日期
      currentDate: null,
      // 固定资产默认科目
      defaultSubject: [],
      // 清理状态
      showClearStatus: false,
      // 卡片id
      cardId: null,
    }

    var validatNumber = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error("请输入数字"));
      } else if (!this.isvalidNumber(value)) {
        callback(new Error("请输入数字"));
      } else {
        callback();
      }
    }
  },
  created () {
    this.getAssetDetail();
    this.getFixedAssertCategroy();
    this.getDepartmentList();
    this.getDepreciationMethod();
    this.getSubjectList();
    this.getAssetConfig();
    this.getCurrentPeriod();
    this.getCurrentDate();
  },
  // 判断路由：是从哪个页面跳转过来的
  beforeRouteEnter (to, from, next) {
    next(vm => {
      if (from.path == "/fixedassets/card") {
        let cardId = vm.$route.query.cardId;
        vm.cardId = vm.$route.query.cardId;
        vm.getAssetDetail(cardId);
      } else {
        Object.prototype.toString.call(vm.$route.query.cardId) != '[object Undefined]' ? vm.getAssetDetail(vm.$route.query.cardId) : vm.getAssetDetail();
      }
    });
  },
  mounted () {
    this.getAssetDetail();
  },
  methods: {
    // 获取固定资产卡片详情
    getAssetDetail (cardId) {
      if (Object.prototype.toString.call(cardId) != '[object Undefined]') {
        assetDetail(cardId).then(res => {
          this.form = res.data;
          this.form.originalValue.toString();
          this.showClearStatus = res.data.status;
        })
      }
    },
    // 清理
    clearCard (status) {
      fixedAssetCardClean({ cardId: this.form.cardId, status, yearPeriod: this.form.yearPeriod }).then(res => {
        if (res.code == '200') {
          if (status) {
            console.log(this.form.cleanPeriod);
            this.form.cleanPeriod = null;
            this.msgSuccess("取消卡片清理成功");
          } else {
            this.msgSuccess("卡片清理成功");
            this.form.cleanPeriod = this.form.yearPeriod;
          }
          this.showClearStatus = status;
        }
      })
    },
    // 保存 
    addClick () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.bookId = this.bookId;
          this.form.fixedAssetSubjectId = this.form.fixedAssetSubjectId || this.defaultSubject[0].subjectId;
          this.form.depreciationSubjectId = this.form.depreciationSubjectId || this.defaultSubject[1].subjectId;
          this.form.depreciationFeeSubjectId = this.form.depreciationFeeSubjectId || this.defaultSubject[2].subjectId;
          this.form.residualRatePre = this.residualRatePre;
          this.form.monthDepreciation = this.monthDepreciation;
          this.form.startDepreciation = this.startDepreciation;
          this.form.startNetValue = this.startNetValue;
          this.form.status = this.showClearStatus;
          editAsset(this.form).then(res => {
            if (res.code === 200) {
              this.msgSuccess("修改成功");
              let cardId = this.form.cardId;
              this.reset();
              this.getAssetDetail(cardId);
            }
          })
        }
      })
    },
    initForm () {
      this.form = {
        cardId: null, // 卡片Id
        cardCode: null, // 编号
        name: '', // 资产名称
        categoryId: null, // 资产类别
        startDate: null, // 开始使用日期
        yearPeriod: null, // 录入期间
        departmentId: null, // 使用部门Id
        term: 0, // 预计使用期限
        cleanPeriod: null, // 录入期间
        fixedAssetSubjectId: null, // 固定资产科目Id
        fixedAssetSubjectNameBy: null,
        depreciationSubjectId: null, // 累计折旧科目Id
        depreciationSubjectNameBy: null,
        depreciationFeeSubjectId: null, // 折旧费用科目Id
        depreciationFeeSubjectNameBy: null,
        originalValue: 0, // 原值
        residualRate: 0, // 残值率
        residualRatePre: 0, // 预计残值
        impairmentPrepare: 0, // 减值准备
        alreadyDepreciationDate: 0, // 已折旧期间
        startDepreciation: 0, // 期初累计折旧
        startNetValue: 0, // 期初净值
        monthDepreciation: 0, // 月折旧
        status: false // 清理状态
      };
      this.resetForm("form");
    },
    // 表单重置
    reset () {
      this.form = {
        cardId: null, // 卡片Id
        cardCode: null, // 编号
        name: '', // 资产名称
        categoryId: null, // 资产类别
        startDate: null, // 开始使用日期
        yearPeriod: null, // 录入期间
        departmentId: null, // 使用部门Id
        term: 0, // 预计使用期限
        cleanPeriod: null, // 录入期间
        fixedAssetSubjectId: this.defaultSubject[0].subjectId, // 固定资产科目Id
        fixedAssetSubjectNameBy: this.defaultSubject[0].subjectBy,
        depreciationSubjectId: this.defaultSubject[1].subjectId, // 累计折旧科目Id
        depreciationSubjectNameBy: this.defaultSubject[1].subjectBy,
        depreciationFeeSubjectId: this.defaultSubject[2].subjectId, // 折旧费用科目Id
        depreciationFeeSubjectNameBy: this.defaultSubject[2].subjectBy,
        originalValue: 0, // 原值
        residualRate: 0, // 残值率
        residualRatePre: 0, // 预计残值
        impairmentPrepare: 0, // 减值准备
        alreadyDepreciationDate: 0, // 已折旧期间
        startDepreciation: 0, // 期初累计折旧
        startNetValue: 0, // 期初净值
        monthDepreciation: 0, // 月折旧
        status: false // 清理状态
      };
      this.resetForm("form");
    },
    // 删除工资
    // 工资删除
    handleDel () {
      let cardId = this.$route.query.cardId;
      this.$confirm(
        '是否确认删除固定工资卡片编号为"' + cardId + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function () {
          return delAsset(cardId);
        })
        .then(() => {
          this.msgSuccess("删除成功");
          this.$router.push({
            path: '/fixedassets/card',
          })
        })
        .catch(function () { });
    },
    // 获取折旧方式
    getDepreciationMethod () {
      // 字典获取折旧方式
      this.getDicts("depreciationMethod").then(response => {
        this.depreciationMethods = response.data;
      });
    },
    // 获取当天日期
    getCurrentDate () {
      let currDate = this.parseTime(new Date(), '{y}-{m}-{d}');
      this.currentDate = currDate;
      this.form.startDate = currDate;
    },
    // 获取科目列表
    getSubjectList () {
      return new Promise((resolve, reject) => {
        getSubjectVoucher({ bookId: this.bookId }).then(res => {
          this.subjectlist = res.rows;
          resolve("success");
        });
      });
    },
    // 固定资产编码
    getAssetCode () {
      assetCode({ bookId: this.bookId }).then(res => {
        this.form.cardCode = res;
      })
    },
    // 固定资产、累计折旧、折旧费用科目默认值
    getAssetConfig () {
      assetConfig({ bookId: this.bookId }).then(res => {
        this.form.fixedAssetSubjectId = res.rows[0].subjectId;
        this.form.fixedAssetSubjectNameBy = res.rows[0].subjectBy;
        this.form.depreciationSubjectId = res.rows[1].subjectId;
        this.form.depreciationSubjectNameBy = res.rows[1].subjectBy;
        this.form.depreciationFeeSubjectId = res.rows[2].subjectId;
        this.form.depreciationFeeSubjectNameBy = res.rows[2].subjectBy;
        this.defaultSubject = res.rows;
      })
    },
    // 获取部门
    getDepartmentList () {
      let data = { bookId: this.bookId, auxiliaryTypeId: DEPARTMENT };
      auxiliaryList(data).then(res => {
        this.departmentList = res.rows;
      })
    },
    // 获取固定资产类型列表
    getFixedAssertCategroy () {
      fixedAssertCategroy({ bookId: this.bookId }).then(res => {
        this.categroyList = res.rows;
      })
    },
    // 根据资产类别改变预计使用期限
    categoryChange (selVal) {
      let categoryId = selVal;
      assetCategoryDetail(categoryId).then(res => {
        if (res.code == '200') {
          let years = res.data.years;
          this.form.term = years * 12;
        }
      })
    },
    // 获取当前账期
    getCurrentPeriod () {
      return new Promise(resolve => {
        currentPeriod({ bookId: this.bookId }).then(res => {
          this.form.yearPeriod = res.data.yearPeriod;
          resolve(res.data.yearPeriod);
        });
      });
    },
    // 科目选择
    subjectSelect (item, type) {
      switch (type) {
        case 'fixedAssetSubjectId': this.form.fixedAssetSubjectId = item.subjectId;
          this.form.fixedAssetSubjectNameBy = item.subjectNameBy;
          break;
        case 'depreciationSubjectId': this.form.depreciationSubjectId = item.subjectId;
          this.form.depreciationSubjectNameBy = item.subjectNameBy;
          break;
        case 'depreciationFeeSubjectId': this.form.depreciationFeeSubjectId = item.subjectId;
          this.form.depreciationFeeSubjectNameBy = item.subjectNameBy;
          break;
      }
    },
    //科目搜索
    subjectSearch (queryString, callback) {
      let sub = this.subjectlist,
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
  },
  computed: {
    // 预计残值 = 原值 * 残值率
    residualRatePre: {
      get () {
        let originalValue = this.form.originalValue == '' ? 0 : this.form.originalValue;
        let residualRate = this.form.residualRate == '' ? 0 : this.form.residualRate;
        this.form.residualRatePre = originalValue * (residualRate / 100);
        return originalValue * (residualRate / 100);
      },
      set () {
      }
    },
    // 月折旧 = (原值 - 减值准备 - 预计残值)/预计使用期限
    monthDepreciation: {
      get () {
        if (this.form.depreciationMethod == '1') {
          let originalValue = this.form.originalValue == '' ? 0 : this.form.originalValue;
          let impairmentPrepare = this.form.impairmentPrepare == '' ? 0 : this.form.impairmentPrepare;
          let residualRatePre = this.residualRatePre == '' ? 0 : this.residualRatePre;
          let term = this.form.term == '' ? 1 : this.form.term;
          return (originalValue - impairmentPrepare - residualRatePre) / term;
        } else {
          return 0;
        }
      },
      set () { }
    },
    // 期初累计折旧 = 已折旧期间 * 月折旧
    startDepreciation: {
      get () {
        let alreadyDepreciationDate = this.form.alreadyDepreciationDate == 0 ? 0 : this.form.alreadyDepreciationDate;
        let monthDepreciation = this.monthDepreciation == 0 ? 0 : this.monthDepreciation;
        return alreadyDepreciationDate * monthDepreciation;
      },
      set () { }
    },
    // 期初净值 = 原值- 减值准备 - 起初累计折旧
    startNetValue: {
      get () {
        let originalValue = this.form.originalValue == '' ? 0 : this.form.originalValue;
        let impairmentPrepare = this.form.impairmentPrepare == '' ? 0 : this.form.impairmentPrepare;
        let startDepreciation = this.startDepreciation == 0 ? 0 : this.startDepreciation;
        return originalValue - impairmentPrepare - impairmentPrepare;
      },
      set () { }
    }
  },
  watch: {
    // 预计使用期限只能输入数字并且是整数
    'form.term': {
      handler (newVal, oldVal) {
        this.form.term = toInteger(newVal, 0);
      }
    },
    // 预计使用期只能输入数字并且是整数
    'form.alreadyDepreciationDate': {
      handler (newVal, oldVal) {
        this.form.alreadyDepreciationDate = toInteger(newVal);
      }
    },
    // 原值只能输入数字
    /*'form.originalValue': {
      handler (newVal, oldVal) {
        console.log("newVal:" + newVal);

        let val = newVal.replace(/[^\d.]/g, '').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
        this.form.originalValue = val;
      }
    },*/
  }
}
</script>
<style lang="scss" scoped>
.assetsAdd-wrap {
  display: flex;
  -ms-flex-direction: column;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  overflow: hidden;
  padding: 0 20px;
  box-sizing: border-box;
  //background-color: #eceef0;
  .assetsAdd-tab {
    width: 98%;
    height: 40px;
    margin: 3px 10px;
    display: flex;
    align-items: center;
  }
  .assetsAdd-content {
    width: 98%;
    margin: 3px 10px;
    .assetsAdd-form {
      width: 1064px;
      padding: 20px 30px;
      border: 1px solid #cfcfcf;
      background: #fff;
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
      position: relative;
      .showClearStstus {
        position: absolute;
        top: 10px;
        right: 40px;
      }
      .tab-tip {
        margin: 0 0 10px 0;
        font-size: 14px;
        .tab-border {
          border-bottom: 2px solid #ccc;
        }
      }
    }
  }
  .assetsAdd-footer {
    width: 98%;
    height: 40px;
    margin: 3px 10px;
    display: flex;
    flex-flow: row-reverse nowrap;
    justify-content: space-between;
    align-items: center;
  }
}
/*.el-form-item {
  margin-bottom: 6px !important;
}*/
.el-date-editor.el-input,
.el-date-editor.el-input__inner {
  width: 195px !important;
}
/deep/.el-input-number.is-without-controls .el-input__inner {
  text-align: left !important;
}
.ipt_width {
  width: 200px !important;
}
</style>