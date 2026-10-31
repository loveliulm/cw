export default () => {
  return {
    abstract: {
      show: false, //是否显示摘要输入框
      value: "" //摘要的值
    },
    subject: {
      show: false, //是否显示科目输入框
      subjectId: null, //科目id
      value: "", //科目名称
      subjectCode: "", //科目code
      isLeaf: null //是否末级节点 true-是，false-否
    },
    direction: "DEBIT", // 借贷方向
    sourceSubject: {
      isDisabled: false, // 是否禁用
      sourceSubjectId: null, // 来源科目id
      value: "", // 来源科目名称
      sourceSubjectCode: "" // 来源科目code
    },
    dateRule: "期末余额", // 取值类型
    period: "", // 区间
    percentage: {
      value: "100",
      isDisabled: false // 是否禁用
    }
  };
};
