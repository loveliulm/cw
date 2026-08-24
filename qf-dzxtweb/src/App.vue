<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style lang="scss">
* {
  padding: 0;
  margin: 0;
  font-family: "Microsoft YaHei", monospace, helvetica, arial, sans-serif;
}
// html,
// body {
//   min-width: 800px;
//   min-height: 600px;
//   width: 100vw;
//   height: 100vh;
//   overflow: hidden;
// }
// ul,
// li {
//   list-style: none;
// }
// .after:after {
//   content: "";
//   display: block;
//   height: 0;
//   clear: both;
//   visibility: hidden;
// }
// .before:before {
//   content: "";
//   display: table;
// }

[v-cloak] {
  display: none;
}

//正文
.fz4 {
  font-size: 14px;
}

//正文（小）
.fz5 {
  font-size: 13px;
}

//辅助文字
.fz6 {
  font-size: 12px;
}

//自定义卡片
.el-card--custom {
  background-color: rgb(255, 255, 255);
  padding: 24px;
  border: 1px solid rgb(209, 221, 229);
  color: rgb(31, 49, 61);
  .el-button + .el-button {
    margin-left: 0;
  }
  table {
    .el-button + .el-button {
      margin-left: 10px;
    }
  }
}

//自定义表格
// .el-table {
//   border-right: 0;
//   border-bottom: 0;
//   tbody {
//     @extend .fz6;
//   }
//   .cell,
//   th > div {
//     padding-left: 8px !important;
//     padding-right: 8px !important;
//   }
// }
</style>
