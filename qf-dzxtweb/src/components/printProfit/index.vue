<template>
    <div id="printProfit" style="position: relative; left: -9999px;">
      <div class="profitTitle">
        <span>利润表</span>
      </div>
      <div class="profitHeader">
        <div>
          <span>{{profitHeadData.customerName}}</span>
        </div>
        <div>
          <span>{{profitHeadData.currentPeriodData.str_before}}年{{profitHeadData.currentPeriodData.str_after}}期</span>
        </div>
        <div>
          <span>单位:元</span>
        </div>
      </div>
      <div class="profitContent">
        <table>
          <thead>
            <th>项目</th>
            <th>行次</th>
            <th>本期金额</th>
            <th>本年累计金额</th>
          </thead>
          <tbody>
            <tr v-for="(item,index) of allTableData" :key="index">
              <td>{{item.profitName}}</td>
              <td align="center">{{item.rowNum}}</td>
              <td align="right">{{item.currentPeriodHappen == 0?'':item.currentPeriodHappen}}</td>
              <td align="right">{{item.yearTotal == 0?'':item.yearTotal}}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
</template>
<script>
export default {
    name: "printProfit",
    props:["allTableData","profitHeadData"],
     methods: {
         printAssets() {
           setTimeout(()=>{
             this.getPdf("#printProfit");
           },1000);
         }
     }
}
</script>
<style lang="scss" scoped>
#printProfit{
   width: 900px;
   margin:0 auto;
   font-size:13px;
   .profitTitle{
     padding-top:25px;
     display:flex;
     justify-content: center;
     align-items: center;
     font-size:25px;
   }
   .profitHeader{
     width:90%;
     margin:10px auto 0 auto;
     display:flex;
     flex-flow: row nowrap;
     justify-content: space-between;
     align-items: center;
   }
   .profitContent{
     width:90%;
     margin:10px auto 0 auto;
     table{
       width:100%;
       border-collapse: collapse;
       padding: 2px;
       thead th {
        border: 1px solid #dfe6ec;
        padding: 8px;
        background-color: #f7f7f7;
       // color: #909399;
        font-weight: 500;
       }
       tbody tr {
        td {
          border: 1px solid #dfe6ec;
          padding: 8px;
        }
       }
     }
   }
}

</style>