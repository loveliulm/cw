export default () => {
  return {
    abstract: {
      show: false, //是否显示摘要输入框
      val: "" //摘要的值
    },
    subject: {
      show: false, //是否显示科目输入框
      subjectId: null, //科目id
      value: "", //不拼接辅助核算的科目名称
      auxData: [], //辅助核算数据
      DeCr_direction: "", //借贷方向(true是借，false是贷)
      balance_show: false, //显示余额
      balance_val: "" //余额(不存)
    },
    quantity: {
      show: false,//是否显示数量列
      num_val: '',//数量
      price_val: '',//单价
      cmeasure: ''//单位 
    },
    currency: {
      show: false,//是否显示外币列
      exchangeRate: '',//汇率
      original: "", //原币
      homeCurrency: "", //是否默认币种(1是，0不是)
      currencyValue: "", //币种(uid)
      currencyOptions: [] //币种下拉选项(不存)...目前先不考虑一个科目可以开启多个币种，暂时先不启用，后期如果需要再启用此参数
    },
    DeCr: {
      state: true, //当前的借贷显示，true是借，false是贷
      show: false, //是否显示借贷输入框
      De_val: "", //借方金额数据(state为true时有值，false时为空字符串)
      Cr_val: "" //贷方金额数据(state为false时有值，true时为空字符串)
    },
    isaux: false, //是否有除外币和数量外的辅助核算项subject.auxData长度不为0时为true
    percentage: "" //金额百分比(期末用的)
  };
};
