const book = {
  state: {
    bookId: null,
    // 当前账套名称
    customerName: "",
    // 当前账套账期
    currentPeriod: "",
    // 当前账套账期
    currentPeriodData: {},
    // 账套所选的会计准则Id
    accountTemplateId: null,
    // 账套所选的会计准则名称
    accountTemplateName: "",
    // 纳税人性质
    vatType: "",
    // 是否为当前账套
    isCurrentBook: true
  },

  mutations: {
    SET_CUSTOMERNAME: (state, customerName) => {
      state.customerName = customerName;
    },
    SET_CURRENTPERIOD: (state, currentPeriod) => {
      state.currentPeriod = currentPeriod;
      state.currentPeriodData = getStr(currentPeriod, "-");
    },
    SET_CURRENTPERIODDATA: (state, currentPeriodData) => {
      state.currentPeriodData = currentPeriodData;
    },
    SET_ACCOUNTTEMPLATEID: (state, accountTemplateId) => {
      state.accountTemplateId = accountTemplateId;
    },
    SET_ACCOUNTTEMPLATENAME: (state, accountTemplateName) => {
      state.accountTemplateName = accountTemplateName;
    },
    SET_VATTYPE: (state, vatType) => {
      state.vatType = vatType;
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions;
    },
    SET_ISCURRENTBOOK: (state, isCurrentBook) => {
      state.isCurrentBook = isCurrentBook;
    }
  },

  actions: {}
};
// 分割字符
function getStr(string, str) {
  let str_before = string.split(str)[0];
  let str_after = string.split(str)[1];
  return { str_before, str_after };
}

export default book;
