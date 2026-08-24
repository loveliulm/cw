<template>
    <div id="printAssets" style="position: relative; left: -9999px;">
      <div class="assetsTitle">
        <span>资产负债表</span>
      </div>
      <div class="assetsHeader">
        <div>
          <span>{{assetsHeadData.customerName}}</span>
        </div>
        <div>
          <span>{{assetsHeadData.currentPeriodData.str_before}}年{{assetsHeadData.currentPeriodData.str_after}}期</span>
        </div>
        <div>
          <span>单位:元</span>
        </div>
      </div>
      <div class="assetsContent">
        <table>
          <thead>
            <th>资产名称</th>
            <th>行次</th>
            <th>期末数</th>
            <th>年初数</th>
            <th>负债和所有者(或股东)权益</th>
            <th>行次</th>
            <th>期末数</th>
            <th>年初数</th>
          </thead>
          <tbody>
            <tr v-for="(item,index) of allTableData" :key="index">
              <td>{{item.assetsName}}</td>
              <td align="center">{{item.assetsRowNum}}</td>
              <td align="right">{{item.assetsEndBalance == 0?'':item.assetsEndBalance}}</td>
              <td align="right">{{item.assetsYearInitBalance == 0?'':item.assetsYearInitBalance}}</td>
              <td>{{item.debtName}}</td>
              <td align="center">{{item.debtRowNum}}</td>
              <td align="right">{{item.debtEndBalance == 0?'':item.debtEndBalance}}</td>
              <td align="right">{{item.debtYearInitBalance == 0?'':item.debtYearInitBalance}}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
</template>
<script>
export default {
    name: "printAssets",
    props:["allTableData","assetsHeadData"],
    data() {
        return {
            allPrintVoucher: []
        };
    },
     methods: {
         printAssets() {
           setTimeout(()=>{
             this.getPdf("#printAssets");
           },1000);
         }
     }
}
</script>
<style lang="scss" scoped>
#printAssets{
   width: 900px;
   margin:0 auto;
   font-size:13px;
   .assetsTitle{
     padding-top:25px;
     display:flex;
     justify-content: center;
     align-items: center;
     font-size:25px;
   }
   .assetsHeader{
     width:90%;
     margin:10px auto 0 auto;
     display:flex;
     flex-flow: row nowrap;
     justify-content: space-between;
     align-items: center;
   }
   .assetsContent{
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