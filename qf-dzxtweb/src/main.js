import Vue from "vue";

import Cookies from "js-cookie";

import "normalize.css/normalize.css"; // a modern alternative to CSS resets

import Element from "element-ui";
import "./assets/styles/element-variables.scss";

import "@/assets/styles/index.scss"; // global css
import "@/assets/styles/ruoyi.scss"; // ruoyi css
import App from "./App";
import store from "./store";
import router from "./router";
import permission from "./directive/permission";

import "./assets/icons"; // icon
import "./permission"; // permission control
import { getDicts } from "@/api/system/dict/data";
import { getConfigKey } from "@/api/system/config";
import {
  parseTime,
  resetForm,
  addDateRange,
  selectDictLabel,
  download,
  handleTree,
  clearFormValidate,
  deepClone,
  substr,
  sliceLastSymbol,
  toDecimal2,
  compareDate,
  equalsDate
} from "@/utils/ruoyi";
import Pagination from "@/components/Pagination";
import { localStorage } from "@/utils/storage";
import { getSubjectVoucher } from "@/api/voucher/viewvoucher";
import { isvalidNumber } from "@/utils/validate";
import { trimStr } from "@/utils/index";
import htmlToPdf from "@/assets/js/htmlToPdf";
import book from "./store/modules/book";

Vue.use(htmlToPdf);

// 全局方法挂载
Vue.prototype.getDicts = getDicts;
Vue.prototype.getConfigKey = getConfigKey;
Vue.prototype.parseTime = parseTime;
Vue.prototype.resetForm = resetForm;
Vue.prototype.clearFormValidate = clearFormValidate;
Vue.prototype.addDateRange = addDateRange;
Vue.prototype.selectDictLabel = selectDictLabel;
Vue.prototype.download = download;
Vue.prototype.handleTree = handleTree;
Vue.prototype.deepClone = deepClone;
Vue.prototype.localStorage = localStorage;
Vue.prototype.substr = substr;
Vue.prototype.sliceLastSymbol = sliceLastSymbol;
Vue.prototype.toDecimal2 = toDecimal2;
Vue.prototype.isvalidNumber = isvalidNumber;
Vue.prototype.trimStr = trimStr;
Vue.prototype.compareDate = compareDate;
Vue.prototype.equalsDate = equalsDate;

Vue.prototype.msgSuccess = function(msg) {
  this.$message({
    showClose: true,
    message: msg,
    type: "success",
    dangerouslyUseHTMLString: true
  });
};

Vue.prototype.msgError = function(msg) {
  this.$message({
    showClose: true,
    message: msg,
    type: "error",
    dangerouslyUseHTMLString: true
  });
};

Vue.prototype.msgInfo = function(msg) {
  this.$message.info(msg);
};

Vue.prototype.getSubjectList = function() {
  let bookId = this.getUrlBookId();
    // this.$store.state.bookId == null
    //   ? this.localStorage.getItem("bookId")
    //   : this.$store.state.bookId;
  getSubjectVoucher({ bookId }).then(res => {
    this.$store.commit("getSubjectData", res.rows);
    this.localStorage.setItem("subjectList", res.rows);
  });
};

Vue.prototype.findStringIndex = function(str,cha,num){
  let x = str.indexOf(cha);
    for(let i = 0;i < num;i++){
        x = str.indexOf(cha,x+1);
    }
    return x;
};
Vue.prototype.getUrlBookId = function(){
  let query = window.location.href;
  let jhIndex = this.findStringIndex(query, '#', 0) ;
  let jhStr = query.substring(0,jhIndex);
  let xhlastIndex = jhStr.lastIndexOf('/') + 1;
  let bookId = jhStr.substring(xhlastIndex);
  
  return bookId;
};
// 全局组件挂载
Vue.component("Pagination", Pagination);

Vue.use(permission);

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
  size: Cookies.get("size") || "medium" // set element-ui default size
});

Vue.config.productionTip = false;

new Vue({
  el: "#app",
  router,
  store,
  render: h => h(App)
});

//金额千分位,补零，全局方法
Vue.prototype.moneyChange = {
  outputmoney: function(num) {
    // debugger;
    if (isNaN(num) || num === "") return "";
    num = Math.round(num * 100) / 100;
    if (num < 0)
      return (
        "-" +
        this.outputdollars(Math.floor(Math.abs(num) - 0) + "") +
        this.outputcents(Math.abs(num) - 0)
      );
    else
      return (
        this.outputdollars(Math.floor(num - 0) + "") + this.outputcents(num - 0)
      );
  },
  outputdollars: function(m) {
    if (m.length <= 3) return m == "" ? "0" : m;
    else {
      let mod = m.length % 3;
      let output = mod == 0 ? "" : m.substring(0, mod);
      for (let i = 0; i < Math.floor(m.length / 3); i++) {
        if (mod == 0 && i == 0)
          output += m.substring(mod + 3 * i, mod + 3 * i + 3);
        else output += "," + m.substring(mod + 3 * i, mod + 3 * i + 3);
      }
      return output;
    }
  },
  outputcents: function(amount) {
    amount = Math.round((amount - Math.floor(amount)) * 100);
    return amount < 10 ? ".0" + amount : "." + amount;
  }
};
