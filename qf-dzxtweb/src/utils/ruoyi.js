/**
 * 通用js方法封装处理
 * Copyright (c) 2019 ruoyi
 */

const baseURL = process.env.VUE_APP_BASE_API;

// 日期格式化
export function parseTime(time, pattern) {
  if (arguments.length === 0 || !time) {
    return null;
  }
  const format = pattern || "{y}-{m}-{d} {h}:{i}:{s}";
  let date;
  if (typeof time === "object") {
    date = time;
  } else {
    if (typeof time === "string" && /^[0-9]+$/.test(time)) {
      time = parseInt(time);
    } else if (typeof time === "string") {
      //time = time.replace(new RegExp(/-/gm), "/");
    }
    if (typeof time === "number" && time.toString().length === 10) {
      time = time * 1000;
    }
    date = new Date(time);
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  };
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key];
    // Note: getDay() returns 0 on Sunday
    if (key === "a") {
      return ["日", "一", "二", "三", "四", "五", "六"][value];
    }
    if (result.length > 0 && value < 10) {
      value = "0" + value;
    }
    return value || 0;
  });
  return time_str;
}

// 表单重置
export function resetForm(refName) {
  if (this.$refs[refName]) {
    this.$refs[refName].resetFields();
  }
}

// 清除表单验证
export function clearFormValidate(refName) {
  if (this.$refs[refName]) {
    this.$refs[refName].clearValidate();
  }
}

// 添加日期范围
export function addDateRange(params, dateRange) {
  var search = params;
  search.beginTime = "";
  search.endTime = "";
  if (null != dateRange && "" != dateRange) {
    search.beginTime = this.dateRange[0];
    search.endTime = this.dateRange[1];
  }
  return search;
}

// 回显数据字典
export function selectDictLabel(datas, value) {
  var actions = [];
  Object.keys(datas).map(key => {
    if (datas[key].dictValue == "" + value) {
      actions.push(datas[key].dictLabel);
      return false;
    }
  });
  return actions.join("");
}

// 通用下载方法
export function download(fileName) {
  window.location.href =
    baseURL +
    "/common/download?fileName=" +
    encodeURI(fileName) +
    "&delete=" +
    true;
}

// 字符串格式化(%s )
export function sprintf(str) {
  var args = arguments,
    flag = true,
    i = 1;
  str = str.replace(/%s/g, function() {
    var arg = args[i++];
    if (typeof arg === "undefined") {
      flag = false;
      return "";
    }
    return arg;
  });
  return flag ? str : "";
}

// 转换字符串，undefined,null等转化为""
export function praseStrEmpty(str) {
  if (!str || str == "undefined" || str == "null") {
    return "";
  }
  return str;
}

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 * @param {*} rootId 根Id 默认 0
 */
export function handleTree(data, id, parentId, children, rootId) {
  id = id || "id";
  parentId = parentId || "parentId";
  children = children || "children";
  rootId = rootId || 0;
  //对源数据深度克隆
  const cloneData = JSON.parse(JSON.stringify(data));
  //循环所有项
  const treeData = cloneData.filter(father => {
    let branchArr = cloneData.filter(child => {
      //返回每一项的子级数组
      return father[id] === child[parentId];
    });
    branchArr.length > 0 ? (father.children = branchArr) : "";
    //返回第一层
    return father[parentId] === rootId;
  });
  return treeData != "" ? treeData : data;
}

// 深拷贝
export function deepClone(obj) {
  // 根据类型制造一个新的数组或对象 => 指向一个新的空间
  // 由于数组的typeof也是'object',所以用Array.isArray(obj)
  var new_obj = Array.isArray(obj) ? [] : {};
  // 首先判断obj的类型
  // 普通类型
  if (typeof obj != "object") {
    // 这里不能直接返回obj,不然就是浅拷贝的性质
    return (new_obj = obj);
  }
  //引用类型
  //数组
  if (obj instanceof Array) {
    for (let i = 0; i < obj.length; i++) {
      new_obj[i] = obj[i];
      if (typeof new_obj[i] == "object") {
        deepClone(new_obj[i]);
      }
    }
  } else {
    //对象
    for (let key in obj) {
      if (obj.hasOwnProperty(key)) {
        // 对象中的数组和对象
        if (typeof obj[key] == "object") {
          new_obj[key] = deepClone(obj[key]);
        } else {
          //对象中没有引用类型
          new_obj[key] = obj[key];
        }
      }
    }
  }
  return new_obj;
}

export function superAdministrator() {
  let roles = this.$store.state.roles[0];
  let roleName = roles.roleName.replace(/^\s*|\s*$/g, "");
  return roleName === "超级管理员";
}

// 截取特定字符后的字符串
export function substr(str, symbol) {
  let index = str.indexOf(symbol);
  str = str.substring(index + 1, str.length);
  return str;
}
// 截取特定字符前的字符串
export function subBeforeStr(str, symbol) {
  let index = str.indexOf(symbol);
  str = str.substring(0, index + 1);
  return str;
}
// 去除字符串最后一个符号
export function sliceLastSymbol(str) {
  let lastIndex = str.lastIndexOf(",");
  return str.slice(0, lastIndex) + "<br/>";
}

// 保留两位小数函数
export function toDecimal2(x) {
  var f = parseFloat(x);
  if (isNaN(f)) {
    return false;
  }
  var f = Math.round(x * 100) / 100;
  var s = f.toString();
  var rs = s.indexOf(".");
  if (rs < 0) {
    rs = s.length;
    s += ".";
  }
  while (s.length <= rs + 2) {
    s += "0";
  }
  return s;
}

//  小数四舍五入为整数
export function toInteger(val, num) {
  num = num ? num : 2; //默认两位小数
  val = Number(val).toString() == "NaN" ? 0 : Number(val);
  var value = (val + "").split(".");
  var decResult = ""; //小数结果
  var intValue = value[0]; //整数
  var decValue = value[1]; //小数
  var recursion = function(decList, num, join) {
    //递归计算进一
    var i = Number(decList[num - 1]);
    if (i + join > 9) {
      decList[num - 1] = "0"; //归零
      if (num - 1 == 0) {
        //小数位都进一 给整数加一
        intValue = Number(intValue) + 1;
        return decList;
      } else {
        return recursion(decList, num - 1, join); //进一
      }
    } else {
      decList[num - 1] = (i + join).toString();
      return decList;
    }
  };
  if (decValue) {
    var decList = decValue.split("");
    if (decList.length > num) {
      //如果小数位多于截取位数进行计算
      var join = 0;
      if (decList[num] >= 5) {
        //截取位数的后一位大于5进一
        join = 1;
      }
      decResult = recursion(decList, num, join)
        .join("")
        .substring(0, num); //截取小数位
    } else {
      decResult = decValue;
    }
  }
  return Number(intValue + "." + decResult);
}

// 比较日期时间的大小
export function compareDate(beginDate, endDate) {
  var d1 = new Date(beginDate.replace(/\-/g, "/"));
  var d2 = new Date(endDate.replace(/\-/g, "/"));
  if (beginDate != "" && endDate != "" && d1.getTime() <= d2.getTime()) {
    return true;
  } else {
    return false;
  }
}

// 判断日期时间是否相等
export function equalsDate(beginDate, endDate) {
  var d1 = new Date(beginDate.replace(/\-/g, "/"));
  var d2 = new Date(endDate.replace(/\-/g, "/"));
  if (beginDate != "" && endDate != "" && d1.getTime() == d2.getTime()) {
    return true;
  } else {
    return false;
  }
}
