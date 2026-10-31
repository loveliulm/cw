<template>
  <div class="dashboard-editor-container">
    <!-- <panel-group @handleSetLineChartData="handleSetLineChartData" /> -->
    <div class="content">
      <div class="content-div">
        <p class="content-p">代账服务</p>
        <el-switch
          class="content-switch"
          style="display: block"
          v-model="payValue"
          active-text="全部"
          inactive-text="本月"
        ></el-switch>
        <div class="content-bottom">
          <div class="content-bottom-div">
            <p class="content-bottom-pp">{{pay.amount || 0 }}</p>
            <p class="content-bottom-p">建账总数</p>
          </div>
          <div class="content-bottom-div">
            <p class="content-bottom-pp">{{pay.unfinished || 0}}</p>
            <p class="content-bottom-p">待建账</p>
          </div>
          <div class="content-bottom-div">
            <p class="content-bottom-pp">{{pay.uncleared || 0}}</p>
            <p class="content-bottom-p">收款未结</p>
          </div>
        </div>
      </div>
      <div class="content-div">
        <p class="content-p">工商服务</p>
        <el-switch
          class="content-switch"
          style="display: block"
          v-model="btecValue"
          active-text="全部"
          inactive-text="本月"
        ></el-switch>
        <div class="content-bottom">
          <div class="content-bottom-div">
            <p class="content-bottom-pp">{{btec.amount　|| 0}}</p>
            <p class="content-bottom-p">工商总数</p>
          </div>
          <div class="content-bottom-div">
            <p class="content-bottom-pp">{{btec.unfinished || 0}}</p>
            <p class="content-bottom-p">未完成</p>
          </div>
          <div class="content-bottom-div">
            <p class="content-bottom-pp">{{btec.uncleared || 0}}</p>
            <p class="content-bottom-p">收款未结</p>

          </div>
        </div>
      </div>
    </div>
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart :chart-data="lineChartData" />
    </el-row>
    <!--
        <el-row :gutter="32">
          <el-col :xs="24" :sm="24" :lg="8">
            <div class="chart-wrapper">
              <raddar-chart />
            </div>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="8">
            <div class="chart-wrapper">
              <pie-chart />
            </div>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="8">
            <div class="chart-wrapper">
              <bar-chart />
            </div>
          </el-col>
        </el-row> -->
  </div>
</template>

<script>
  import PanelGroup from "./dashboard/PanelGroup";
  import LineChart from "./dashboard/LineChart";
  import RaddarChart from "./dashboard/RaddarChart";
  import PieChart from "./dashboard/PieChart";
  import BarChart from "./dashboard/BarChart";
  import { btecList, payList } from "@/api/login";
  const lineChartData = {
    newVisitis: {
      expectedData: [100, 120, 161, 134, 105, 160, 165],
      actualData: [120, 82, 91, 154, 162, 140, 145],
    },
    messages: {
      expectedData: [200, 192, 120, 144, 160, 130, 140],
      actualData: [180, 160, 151, 106, 145, 150, 130],
    },
    purchases: {
      expectedData: [80, 100, 121, 104, 105, 90, 100],
      actualData: [120, 90, 100, 138, 142, 130, 130],
    },
    shoppings: {
      expectedData: [130, 140, 141, 142, 145, 150, 160],
      actualData: [120, 82, 91, 154, 162, 140, 130],
    },
  };

  export default {
    name: "Index",
    components: {
      PanelGroup,
      LineChart,
      RaddarChart,
      PieChart,
      BarChart,
    },
    data() {
      return {
        lineChartData: lineChartData.newVisitis,
        btec: [],
        pay: [],
        btecValue: false,
        payValue: false,
      };
    },
    methods: {
      handleSetLineChartData(type) {
        this.lineChartData = lineChartData[type];
      },
      _btecList() {
        btecList("month").then((response) => {
          this.btec = response.data;
        });
      },
      _payList() {
        payList("month").then((response) => {
          this.pay = response.data;
          // console.log(this.pay);
        });
      },
    },
    created() {
      this._btecList();
      this._payList();
    },
    watch: {
      btecValue: function () {
        if (this.btecValue == false) {
          btecList("month").then((response) => {
            this.btec = response.data;
          });
        } else {
          btecList("all").then((response) => {
            this.btec = response.data;
          });
        }
      },
      payValue: function () {
        if (this.payValue == false) {
          payList("month").then((response) => {
            this.pay = response.data;
          });
        } else {
          payList("all").then((response) => {
            this.pay = response.data;
          });
        }
      },
    },
  };
</script>

<style lang="scss" scoped>
  .dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;
    min-height: calc(100vh - 100px);
    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
    .content {
      width: 100%;
      height: 300px;
      display: flex;
      justify-content: space-between;
      margin-bottom: 0px;
      .content-div {
        width: 47%;
        height: 240px;
        background: white;
        position: relative;
        .content-switch {
          position: absolute;
          top: 7%;
          right: 4%;
        }
        .content-bottom {
          width: 100%;
          display: flex;
          .content-bottom-div {
            width: 33.3%;
            height: 100%;
            .content-bottom-p {
              width: 100%;
              text-align: center;

              color: #646464;
              font-size: 13px;
              margin: 12px 0;
            }
            .content-bottom-pp {
              width: 100%;
              text-align: center;
              color: #1fadf6;
              font-size: 40px;
              margin: 50px 0 10px 0;
              height: 46px;
            }
          }
        }
        .content-p {
          width: 100%;
          text-align: center;
          font-size: 21px;
          font-weight: bold;
          padding: 0;
          margin: 0;
          border-bottom: 1px solid #e9ecf1;
          padding: 16px;
        }
      }
    }
  }

  @media (max-width: 1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }
</style>
