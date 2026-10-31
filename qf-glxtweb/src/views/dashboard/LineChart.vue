<template>
  <div :class="className" :style="{height:height,width:width}"/>
</template>

<script>
  import echarts from 'echarts'

  require('echarts/theme/macarons') // echarts theme
  import resize from './mixins/resize'
  import {zheList} from "@/api/login";

  export default {
    mixins: [resize],
    props: {
      className: {
        type: String,
        default: 'chart'
      },
      width: {
        type: String,
        default: '100%'
      },
      height: {
        type: String,
        default: '350px'
      },
      autoResize: {
        type: Boolean,
        default: true
      },
      chartData: {
        type: Object,
        required: true
      }
    },
    data() {
      return {
        chart: null,
        keList1: [],
        keList2: [],
        keList3: [],
      }
    },
    // watch: {
    //   chartData: {
    //     deep: true,
    //     handler(val) {
    //       this.setOptions(val)
    //     }
    //   }
    // },
    mounted() {
      this._zheList()
      // this.$nextTick(() => {
      //   this.initChart()
      // })
    },
    beforeDestroy() {
      if (!this.chart) {
        return
      }
      this.chart.dispose()
      this.chart = null
    },
    methods: {
      initChart() {
        this.chart = echarts.init(this.$el, 'macarons')
        this.setOptions()
      },

      setOptions() {
        this.chart.setOption({
          xAxis: {
            data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
            boundaryGap: false,
            axisTick: {
              show: false
            },
            axisLabel: {
              margin: 15,
              rotate: 1
            }
          },
          grid: {
            left: 10,
            right: 10,
            bottom: 20,
            top: 30,
            containLabel: true
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross'
            },
            padding: [5, 10]
          },
          yAxis: {
            axisTick: {
              show: false
            }
          },
          legend: {
            data: ['未续费客户数', '已续费客户数', '续费率'],
            icon: 'roundRect',
            itemWidth: 22, // 长方形宽度
            itemHeight: 15, // 长方形高度
            itemGap: 40 // 设置间距
          },
          series: [
            {
              name: '未续费客户数',
              itemStyle: {
                normal: {
                  color: '#f99f50',
                  lineStyle: {
                    color: '#f99f50',
                    width: 2
                  }
                }
              },
              smooth: true,
              type: 'line',
              data: this.keList1,
              animationDuration: 2800,
              animationEasing: 'cubicInOut'
            },
            {
              name: '已续费客户数',
              itemStyle: {
                normal: {
                  color: '#31bece',
                  lineStyle: {
                    color: '#31bece',
                    width: 2
                  }
                }
              },
              smooth: true,
              type: 'line',
              data: this.keList2,
              animationDuration: 2800,
              animationEasing: 'cubicInOut'
            },
            {
              name: '续费率',
              itemStyle: {
                normal: {
                  color: '#ec4f5d',
                  lineStyle: {
                    color: '#ec4f5d',
                    width: 2
                  }
                }
              },
              smooth: true,
              type: 'line',
              data: this.keList3,
              animationDuration: 2800,
              animationEasing: 'cubicInOut'
            },
          ]
        })
      },
      _zheList() {
        zheList().then((response) => {
          // 三个接口数据
          this.keList1 = response.data;
          this.keList2 = [1, 1.5, 1, 1, 1, 0, 1, 1.5, 1, 0, 1, 1];
          this.keList3 = [0, 1, 1, 2, 1, 0.5, 1, 0, 0, 0, 0, 1];
          this.initChart()
        });
      },
    }
  }
</script>
