<template>
  <div class="el-alert el-alert--success is-light"
       :class="alertSuccess?'el-alert--success is-light':'el-alert--error is-light'"
       v-if="showAlert">
    <i class="el-alert__icon el-icon-success"></i>
    <div class="el-alert__content">
      <span class="el-alert__title">{{alertTitle}}</span>
      <i class="el-alert__closebtn el-icon-close"
         @click='closeAlert'></i>
    </div>
  </div>
</template>
<script>
export default {
  name: 'AlertMessage',
  props: {
    alertTitle: {
      type: String,
      default: '恭喜您，您录入的初始余额平衡!'
    },
    showAlert: {
      type: Boolean,
      default: true
    },
    alertSuccess: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {

    }
  },
  methods: {
    closeAlert () {
      this.$emit('closeAlert');
    },
  }
}
</script>
<style lang="scss" scoped>
.el-alert {
  width: 100%;
  padding: 8px 16px;
  margin: 0;
  box-sizing: border-box;
  border-radius: 4px;
  position: relative;
  background-color: #fff;
  overflow: hidden;
  opacity: 1;
  display: flex;
  align-items: center;
  transition: opacity 0.2s;
}
.el-alert__icon {
  font-size: 16px;
  width: 16px;
}
[class^="el-icon-"],
[class*=" el-icon-"] {
  font-family: "element-icons" !important;
  //speak: none;
  font-style: normal;
  font-weight: normal;
  font-variant: normal;
  text-transform: none;
  line-height: 1;
  vertical-align: baseline;
  display: inline-block;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
.el-alert__content {
  display: table-cell;
  padding: 0 8px;
}
.el-alert__title {
  font-size: 13px;
  line-height: 18px;
}
.el-alert--success.is-light {
  background-color: #e7faf0;
  color: #13ce66;
}
.el-alert--error.is-light {
  background-color: #fef0f0;
  color: #f56c6c;
}
.el-alert__closebtn {
  font-size: 12px;
  opacity: 1;
  position: absolute;
  top: 12px;
  right: 15px;
  cursor: pointer;
}

.el-alert.is-light .el-alert__closebtn {
  color: #c0c4cc;
}
</style>