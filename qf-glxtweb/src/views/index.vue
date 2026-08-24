<template>
  <div class="app-main">
    <div class="dashboard-editor-container">
      <div class="container-wrap">

        <div class="container-top">
          <el-row :span="24" :gutter="20">
            <el-col :span="14">
              <div class="monthData_new">
                <el-row :span="24">
                  <el-col :span="6">
                    <div class="monthData_itemOne_new">
                      <div class="monthData_itemValue">
                        <div class="monthData_itemAmount_new">{{ dataInfo1.data1 }}</div>
                        <div class="monthData_itemTag_new">本月签约合同</div>
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="monthData_itemTwo_new">
                      <div class="monthData_itemValue">
                        <div class="monthData_itemAmount_new">{{ dataInfo1.data2 }}</div>
                        <div class="monthData_itemTag_new">本月新增代账服务</div>
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="monthData_itemThree_new">
                      <div class="monthData_itemValue">
                        <div class="monthData_itemAmount_new">{{ dataInfo1.data3 }}</div>
                        <div class="monthData_itemTag_new">本月新增工商服务</div>
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="monthData_itemFour_new">
                      <div class="monthData_itemValue">
                        <div class="monthData_itemAmount_new">{{ dataInfo1.data4 }}</div>
                        <div class="monthData_itemTag_new">本月新增收款</div>
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </el-col>
            <el-col :span="5">
              <div class="h-panel" style="height: 170px">
                <div class="h-panel-bar">
                  <div class="h-panel-title" style="font-size: 17px; color: black; font-weight: bold;">欠费提醒</div>
                </div>
                <div style="text-align: center; margin: 15px 0">
                  <el-row :span="24">
                    <el-col :span="12">
                      <p style="font-size: 36px; color: #666666">{{ dataInfo1.data5 }}</p>
                      <span style="font-size: 15px; color: #666666">代账欠费</span>
                    </el-col>
                    <el-col :span="12">
                      <p style="font-size: 36px; color: #666666">{{ dataInfo1.data6 }}</p>
                      <span style="font-size: 15px; color: #666666">工商欠费</span>
                    </el-col>
                  </el-row>
                </div>
              </div>
            </el-col>
            <el-col :span="5">
              <div class="h-panel" style="height: 170px">
                <div class="h-panel-bar">
                  <div class="h-panel-title" style="font-size: 17px; color: black; font-weight: bold;">公告</div>
                  <span class="data-time">
                    <router-link to="/system/notice" class="bullshit__return-home">
                    更多 >>
                    </router-link>
                  </span>
                </div>
                <div>
                  <ul class="notice-list">
                    <li class="notice-item" v-for="(item, index) in noticeDataList">
                      <div class="notice-title">
                        <span class="dot">•</span>
                        <a v-if="index === 0" style="color: #4ccde5">{{ item.noticeTitle }}</a>
<!--                        <a v-else :href="item.url" target="_break">{{ item.noticeTitle }}</a>-->
                        <a v-else>{{ item.noticeTitle }}</a>
                      </div>
                      <div class="notice-time">
                        {{ parseTime(item.createTime, '{y}-{m}-{d}') }}
                      </div>
                    </li>
                  </ul>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>

        <div class="h-panel" style="margin-top: 20px">
          <el-row :span="24" class="amount-row">
            <el-col :span="3">
              <div class="home-show-image bg-light0">
                <span class="panel-title">工商总数</span>
                <p class="panel-amount">{{ dataInfo2.data1 }}</p>
              </div>
            </el-col>
            <el-col :span="3">
              <div class="home-show-image bg-light1">
                <span class="panel-title">未完成</span>
                <p class="panel-amount">{{ dataInfo2.data2 }}</p>
              </div>
            </el-col>
            <el-col :span="3">
              <div class="home-show-image bg-light2">
                <span class="panel-title">已完成</span>
                <p class="panel-amount">{{ dataInfo2.data3 }}</p>
              </div>
            </el-col>
            <el-col :span="3">
              <div class="home-show-image bg-light3">
                <span class="panel-title">建账总数</span>
                <p class="panel-amount">{{ dataInfo2.data4 }}</p>
              </div>
            </el-col>
            <el-col :span="3">
              <div class="home-show-image bg-light4">
                <span class="panel-title">待建账</span>
                <p class="panel-amount">{{ dataInfo2.data5 }}</p>
              </div>
            </el-col>
            <el-col :span="3">
              <div class="home-show-image bg-light5">
                <span class="panel-title">未报税</span>
                <p class="panel-amount">{{ dataInfo2.data6 }}</p>
              </div>
            </el-col>
            <el-col :span="3">
              <div class="home-show-image bg-light6">
                <el-switch
                  style="margin: 27px 0"
                  v-model="queryType"
                  active-text="全部"
                  inactive-text="本月">
                </el-switch>
              </div>
            </el-col>
          </el-row>
        </div>

        <div class="container-footer">
          <div class="container-footer-left">
            <div style="margin-bottom: 20px">
              <el-row :gutter="10">
                <el-col :span="5">
                  <div class="h-panel" style="height: 160px">
                    <div class="h-panel-bar">
                      <div class="h-panel-title" style="font-size: 17px; color: black; font-weight: bold;">合同到期预警</div>
                    </div>
                    <div style="text-align: center; margin: 10px 0">
                      <el-row :span="24">
                        <el-col :span="12">
                          <p style="font-size: 36px; color: #666666">{{ dataInfo3.data1 }}</p>
                          <span style="font-size: 15px; color: #666666">本月到期</span>
                        </el-col>
                        <el-col :span="12">
                          <p style="font-size: 36px; color: #666666">{{ dataInfo3.data2 }}</p>
                          <span style="font-size: 15px; color: #666666">下月到期</span>
                        </el-col>
                      </el-row>
                    </div>
                  </div>
                </el-col>
                <el-col :span="5">
                  <div class="h-panel" style="height: 160px">
                    <div class="h-panel-bar">
                      <div class="h-panel-title" style="font-size: 17px; color: black; font-weight: bold;">代账收款预警</div>
                    </div>
                    <div style="text-align: center; margin: 10px 0">
                      <el-row :span="24">
                        <el-col :span="12">
                          <p style="font-size: 36px; color: #666666">{{ dataInfo3.data3 }}</p>
                          <span style="font-size: 15px; color: #666666">本月到期</span>
                        </el-col>
                        <el-col :span="12">
                          <p style="font-size: 36px; color: #666666">{{ dataInfo3.data4 }}</p>
                          <span style="font-size: 15px; color: #666666">下月到期</span>
                        </el-col>
                      </el-row>
                    </div>
                  </div>
                </el-col>
                <el-col :span="5">
                  <div class="h-panel" style="height: 160px">
                    <div class="h-panel-bar">
                      <div class="h-panel-title" style="font-size: 17px; color: black; font-weight: bold;">工商年报预警</div>
                    </div>
                    <div style="text-align: center; margin: 10px 0">
                      <el-row :span="24">
                        <el-col :span="12">
                          <p style="font-size: 36px; color: #666666">{{ dataInfo3.data5 }}</p>
                          <span style="font-size: 15px; color: #666666">待申报</span>
                        </el-col>
                        <el-col :span="12">
                          <p style="font-size: 36px; color: #666666">{{ dataInfo3.data6 }}</p>
                          <span style="font-size: 15px; color: #666666">已逾期</span>
                        </el-col>
                      </el-row>
                    </div>
                  </div>
                </el-col>
                <el-col :span="5">
                  <div class="h-panel" style="height: 160px">
                    <div class="h-panel-bar">
                      <div class="h-panel-title" style="font-size: 17px; color: black; font-weight: bold;">个税务申报</div>
                    </div>
                    <div style="text-align: center; margin: 10px 0">
                      <el-row :span="24">
                        <el-col :span="12">
                          <p style="font-size: 36px; color: #666666">{{ dataInfo3.data7 }}</p>
                          <span style="font-size: 15px; color: #666666">待申报</span>
                        </el-col>
                        <el-col :span="12">
                          <p style="font-size: 36px; color: #666666">{{ dataInfo3.data8 }}</p>
                          <span style="font-size: 15px; color: #666666">已逾期</span>
                        </el-col>
                      </el-row>
                    </div>
                  </div>
                </el-col>
                <el-col :span="5">
                  <div class="h-panel" style="height: 160px">
                    <div class="h-panel-bar">
                      <div class="h-panel-title" style="font-size: 17px; color: black; font-weight: bold;">季度申报</div>
                    </div>
                    <div style="text-align: center; margin: 10px 0">
                      <el-row :span="24">
                        <el-col :span="12">
                          <p style="font-size: 36px; color: #666666">{{ dataInfo3.data9 }}</p>
                          <span style="font-size: 15px; color: #666666">待申报</span>
                        </el-col>
                        <el-col :span="12">
                          <p style="font-size: 36px; color: #666666">{{ dataInfo3.data10 }}</p>
                          <span style="font-size: 15px; color: #666666">已逾期</span>
                        </el-col>
                      </el-row>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div class="h-panel">
              <div class="h-panel-bar">
                <div class="h-panel-title" style="font-size: 17px; color: black; font-weight: bold;">续费趋势表</div>
              </div>
              <div style="width:99%;padding-top:10px;background-color:#fff;border-box:box-sizing;">
                <line-chart :chart-data="taxsData" :height="'290px'"/>
              </div>
            </div>
          </div>
          <div class="container-footer-right">
            <div class="h-panel" style="min-height: 160px; margin-bottom: 20px">
              <div class="h-panel-bar">
                <div class="h-panel-title" style="font-size: 17px; color: black; font-weight: bold;">快捷菜单</div>
              </div>
              <el-row :span="24" style="margin: 10px">
                <el-col :span="4" v-for="(item, index) in quickMenuData" :key="index">
                  <div class="first-one" @click="goToMenu(item.component)">
                    <img :src="require('../assets/image/index/menu/contract' + (index % 10) + '.png')" class="first-one-img"/>
                    <p>{{ item.meta.title }}</p>
                  </div>
                </el-col>
                <el-col :span="4">
                  <div class="first-one" @click="showMenu">
                    <div class="shortcutMenu_tagAdd_new">
                      <span>+</span>
                    </div>
                    <p>添加</p>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div class="h-panel">
              <div class="h-panel-bar">
                <div class="h-panel-title" style="font-size: 17px; color: black; font-weight: bold;">代账服务收款分析</div>
              </div>
              <div style="width:100%;padding-top:10px;background-color:#fff;border-box:box-sizing;">
                <pie-charts :chart-data="costData" :height="'290px'"/>
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>
    <!-- 添加或修改代账服务对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body>
      <div class="menu_dlg">
        <div class="menu_con">
          <div class="menu_con_wraper" v-for="(item, index) in menuData">
            <label class="menu_label">{{ item.meta.title}}</label>
            <div class="menu_item">
              <span v-for="(info, indexs) in item.children" :class="info.check ? 'menu_item_selected' : ''" @click="chooseMenu(info, index, indexs)">
                {{ info.meta.title}}
              </span>
            </div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitMenu">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import LineChart from './dashboard/LineChart'
  import PieCharts from './dashboard/PieCharts'
  import {} from "@/api/index";
  import { getRouters } from '@/api/menu'
  import { btecList, payList } from "@/api/login";
  import { listNotice } from "@/api/system/notice";

  export default {
    name: 'Index',
    components: {
      LineChart,
      PieCharts,
    },
    data() {
      return {
        // 第1行数据
        dataInfo1: {
          data1: '0',
          data2: '0',
          data3: '0',
          data4: '0',
          data5: '0',
          data6: '0',
        },
        // 第2行数据
        dataInfo2: {
          data1: '0',
          data2: '0',
          data3: '0',
          data4: '0',
          data5: '0',
          data6: '0',
        },
        // 第3行数据
        dataInfo3: {
          data1: '0',
          data2: '0',
          data3: '0',
          data4: '0',
          data5: '0',
          data6: '0',
          data7: '0',
          data8: '0',
          data9: '0',
          data10: '0',
        },
        queryType: false,
        // 续费趋势表数据
        taxsData: {},
        // 代账服务收款分析数据
        costData: [{name: '正常', value: 0}, {name: '预收', value: 0}, {name: '欠费', value: 0}],
        noticeDataList: [],
        btec: [],
        // 弹出层标题
        title: "快捷菜单",
        // 是否显示弹出层
        open: false,
        quickMenuData: [],
        menuData: [],
        // 选中提交菜单id
        checkMenuId: []
      }
    },
    created() {
      this.initData();
      this.queryNoticeList();
      this.getAnalysisData();
      this.queryQuickMenuData();
    },
    watch: {
      queryType: function () {
        this.initData()
      },
    },
    methods: {
      // 查询 dataInfo1，dataInfo2，dataInfo3数据
      async initData() {
        if (!this.queryType) {
          btecList("month").then((response) => {
            this.btec = response.data;
          });
        } else {
          btecList("all").then((response) => {
            this.btec = response.data;
          });
        }
      },

      // 快捷菜单跳转
      goToMenu(path) {
        this.$router.push(path.substring(0, path.length - 6));
      },

      queryNoticeList() {
        // 查询公告数据接口，取三条数据
        let queryParams = {
          pageNum : 1,
          pageSize : 3
        }
        listNotice(queryParams).then(response => {
          this.noticeDataList = response.rows;
        });
      },

      // 代账服务收款分析数据
      getAnalysisData() {
        this.costData = [{name: '正常', value: 1}, {name: '预收', value: 3}, {name: '欠费', value: 1}];
      },

      // 查询快捷菜单
      queryQuickMenuData() {
        this.quickMenuData = [
          {
            id: 2004,
            check: true,
            component: "customer/customer/index",
            hidden: false,
            meta: {title: "客户", icon: "people"},
            name: "Customer",
            path: "customer"
          },
          {
            id: 2014,
            check: true,
            component: "customer/contract/index",
            hidden: false,
            meta: {title: "合同", icon: "education"},
            name: "Contract",
            path: "contract"
          },
        ]

        this.quickMenuData.forEach(item => this.checkMenuId.push(item.id))
      },

      showMenu() {
        // 添加接口比  getRouters 多返回id，check状态
        getRouters().then(res => {
          this.menuData = res.data
          this.menuData[0].children[0].id = 2004
          this.menuData[0].children[0].check = true
          this.menuData[0].children[1].id = 2014
          this.menuData[0].children[1].check = true
        })
        this.open = true
      },
      // 取消按钮
      cancel() {
        this.open = false;
      },
      submitMenu() {
        // 保存接口，参数是 this.checkMenuId

        this.cancel()
      },

      chooseMenu(info, index, indexs) {
        info.check = !info.check;
        this.$set( this.menuData[index].children, indexs, info);
        if (info.check) {
          this.checkMenuId.push(info.id)
        } else {
          this.checkMenuId = this.checkMenuId.filter(item => item !== info.id)
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "~@/assets/styles/variables.scss";

  .app-main {
    background-color: rgb(240, 242, 245);

    .dashboard-editor-container {
      padding: 20px;
      height: 100%;
      //position: relative;
      .container-wrap {
        margin: auto;

        .container-top,
        .container-footer {
          margin: auto;
        }

        .h-panel {
          border: none;
          background: #fff;
          border-radius: 2px;
          position: relative;
          border: 1px solid #eee;
        }

        .h-panel-bar {
          border-top: 0 solid #eee;
          padding: 10px 20px;
          border-bottom: 1px solid #eee;
          position: relative;
        }

        .h-panel-title {
          line-height: 35px;
          font-size: 18px;
          display: inline-block;
          vertical-align: middle;
        }

        .monthData_new {
          position: relative;
          height: 170px;
          background: #fff;
          border-radius: 2px;
          border: 1px solid #eee;
        }
        .monthData_itemOne_new {
          margin-right: 12px;
          height: 170px;
          background: url(../assets/image/index/top_bg_1.2926dfd.png) 100% 100% no-repeat;
          background-size: cover;
        }
        .monthData_itemTwo_new {
          margin-right: 12px;
          height: 170px;
          background: url(../assets/image/index/top_bg_2.02c3722.png) 100% 100% no-repeat;
          background-size: cover;
        }
        .monthData_itemThree_new {
          margin-right: 12px;
          height: 170px;
          background: url(../assets/image/index/top_bg_3.c38ac9e.png) 100% 100% no-repeat;
          background-size: cover;
        }
        .monthData_itemFour_new {
          height: 170px;
          background: url(../assets/image/index/top_bg_4.87e130c.png) 100% 100% no-repeat;
          background-size: cover;
        }
        .monthData_itemValue {
          text-align: center;
          color: #fff;
          padding: 55px 0;
        }
        .monthData_itemAmount_new {
          font-size: 36px;
        }
        .monthData_itemTag_new {
          font-size: 15px;
          margin-top: 5px;
        }

        .data-time {
          font-size: 15px;
          color: #666666;
          line-height: 35px;
        }
        .data-time a {
          color: #999;
          float: right;
          margin-right: 10px;
        }
        .notice-list {
          margin: 5px 15px;
        }
        ul, ol {
          list-style: none;
          margin: 0;
          padding: 0;
        }
        .notice-item {
          font-size: 14px;
          line-height: 32px;
          height: 32px;
        }
        .notice-title {
          float: left;
          /*width: 200px;*/
          color: #333;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          position: absolute;
        }
        .notice-title a {
          margin-left: 5px;
          color: #333;
          /*vertical-align: middle;*/
        }
        .dot {
          color: #ff6c55;
          font-size: 24px;
          vertical-align: middle;
        }
        .notice-time {
          float: right;
          color: #999;
          margin-right: 10px;
          line-height: 32px;
          height: 32px;
        }

        .first-one {
          cursor: pointer;
          text-align: center;
          margin: 8px 0;

          &:hover {
            color: #f60 !important;
          }

          p {
            font-size: 14px;
          }
        }

        p {
          margin: 5px 0;
        }

        .first-one-img {
          margin: auto;
          width: 40px;
          height: 40px;
        }

        .home-show-invoice {
          text-align: center;
          img {
            height: 18px;
            display: inline;
            vertical-align: sub;
          }
        }
        .shortcutMenu_tagAdd_new {
          background-color: #dce8f3;
          width: 40px;
          height: 40px;
          line-height: 40px;
          color: #a3b9cd;
          border-radius: 4px;
          margin: 0 auto;
          margin-bottom: 3px;
        }
        .shortcutMenu_tagAdd_new span {
          display: inline-block;
          text-align: center;
          font-size: 36px;
        }

        .show-invoice-p {
          font-size: 20px;
          color: #666666;
          text-align: center;
          margin-top: 15px;
        }

        .show-invoice-footer {
          font-size: 14px;
          background-color: #f5f5f5;
          border-radius: 4px;
          height: 20px;
          margin-top: 25px;
          padding: 6px 10px
        }

        .home-show-image.bg-light0 {
          background-image: url("../assets/image/index/库存现金.png");
        }
        .home-show-image.bg-light1 {
          background-image: url("../assets/image/index/银行存款.png");
        }
        .home-show-image.bg-light2 {
          background-image: url("../assets/image/index/应收账款.png");
        }
        .home-show-image.bg-light3 {
          background-image: url("../assets/image/index/应付账款.png");
        }
        .home-show-image.bg-light4 {
          background-image: url("../assets/image/index/库存现金.png");
        }
        .home-show-image.bg-light5 {
          background-image: url("../assets/image/index/银行存款.png");
        }
        .home-show-image.bg-light6 {
          background-image: url("../assets/image/index/应收账款.png");
        }

        .home-show-image {
          width: 90%;
          /*height: 140px;*/
          display: inline-block;
          border-radius: 4px;
          box-shadow: 0px 1px 8px 0px rgba(196, 196, 196, 0.5);
          box-sizing: border-box;
          padding: 24px 10px;
          background-size: 40%;
          background-position: 90% 50%;
          background-repeat: no-repeat;
          cursor: pointer;
          transition: all 0.3s;
          margin: 0 20px;
        }

        .panel-title {
          font-size: 16px;
          color: #999;
          margin-left: 10px;
        }

        .panel-amount {
          font-size: 36px;
          color: #999;
          margin-top: 10px;
          margin-left: 10px;
        }

        .fcolor {
          color: #f00 !important;
        }

        .home-show-image:hover {
          transform: scale(1.08);
        }

        .amount-row {
          .el-col {
            /*width: 14.28571429%;*/
            width: 14.1571428%;
            padding: 20px 0;
          }
        }

        .container-top {

        }

        .container-footer {
          display: flex;
          flex-flow: row wrap;
          justify-content: space-between;
          /*align-items: center;*/
          margin-top: 20px;

          .container-footer-left {
            width: 63.8%;
            min-height: 320px !important;
          }

          .container-footer-right {
            width: 35%;
            min-height: 320px;
          }
        }

        .chart-wrapper {
          background: #fff;
          padding: 16px 16px 0;
          margin-bottom: 32px;
        }
      }
    }

  }

  @media screen and(min-width: 1600px) {
    .notice-title {
      width: 60%;
    }
  }

  @media screen and(max-width: 1600px) {
    .notice-title {
      width: 50%;
    }
  }
  @media screen and(max-width: 1200px) {
    .notice-title {
      width: 40%;
    }
  }

  @media screen and(max-width: 800px) {
    .notice-title {
      width: 30%;
    }
  }

  .menu_dlg {
    margin: 0 10px;
    overflow-y: scroll;
    height: 400px;
  }
  .menu_con {
    background-color: #f8f8f8;
  }
  .menu_con_wraper {
    display: -ms-flexbox;
    display: flex;
    -ms-flex-direction: row;
    flex-direction: row;
  }
  .menu_label {
    min-width: 90px;
    background-color: #f1f2f6;
    text-align: right;
    padding-right: 10px;
    padding-top: 12px;
  }
  .menu_item {
    padding: 7px 10px;
  }
  .menu_item span {
    border: 1px solid #dce1e7;
    border-radius: 2px;
    display: inline-block;
    height: 30px;
    line-height: 30px;
    font-size: 13px;
    color: #646464;
    padding: 0 12px;
    margin-right: 12px;
    margin-bottom: 8px;
    cursor: pointer;
    position: relative;
  }
  .menu_item_selected {
    border: 1px solid #1fadf6!important;
    background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA8AAAAMCAMAAACKnBfWAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA39pVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpkMDYwOTcyZi1jOGUxLTZlNGUtODA2Yi0xNjZiZDk1MzQ2YzUiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6QkE5NjQ0MUREMzQxMTFFNkIzMTBENUY5Q0FGRkU0MDgiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6QkE5NjQ0MUNEMzQxMTFFNkIzMTBENUY5Q0FGRkU0MDgiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOmFlNmYyNzVlLTdkNTEtZjM0ZC04YTU2LTUwOGMyY2I2Y2ZmOSIgc3RSZWY6ZG9jdW1lbnRJRD0iYWRvYmU6ZG9jaWQ6cGhvdG9zaG9wOmU5ODBjYWVhLWI2YzItMTFlNi05NjdhLWIzNmEwYjhkZjhhNiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PjAv6GAAAAAMUExURY/W+o/W+////x+t9nBxPPgAAAAxSURBVHjaYmBGBQxE8xkYUPgMTIwwPkgGygXzGZkYYFyIekYmGBeqn5GRdPshACDAALmZAe8x7FPsAAAAAElFTkSuQmCC) no-repeat 100% 100%;
  }
  ::v-deep .container-footer-left .el-col-5 {
    width: 20%;
  }
</style>
