export default () => {
  return {
    // 辅助核算
    auxiliary: {
      auxiliaryId: null,
      auxiliaryTypeId: null,
      value: null
    },
    // 期初余额
    balance: null,
    // 本年累计借方
    credit: null,
    // 本年累计贷方
    debit: null
    // 科目id
    //subjectId: null
  };
};
