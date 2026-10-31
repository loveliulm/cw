<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'

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
      default: '300px'
    },
    chartData: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler (val) {
        this.setOptions(val)
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
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
      if (this.chartData) {
        this.setOptions(this.chartData)
      }
    },
    setOptions (data) {
      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{b} : {c} <br> 占比 : ({d}%)'
        },
        legend: {
          orient: 'horizontal',
          left: 'center',
          top: '3%',
          textStyle: {
            fontSize: 16,
            color: '#747474',
            padding:[3, 3]
          }
        },
        graphic: {
          type: "text",
          left: "center",
          top: "45%",
          data: [12],
          style: {
            text: String(
              data.reduce((sum, item, index, array)=>{
                return sum + item.value
              }, 0.00)
            ).replace(/(\d)(?=(?:\d{6})+$)/g, '$1.') + "\n\n" +  "合计",
            textAlign: "center",
            fill: "#333",
            fontSize: 15,
          }
        },
        color: ['#4499ec', '#2ee261', '#efc541'],
        series: [
          {
            type: 'pie',
            center: ['50%', '50%'],
            radius: ['30%', '60%'],
            avoidLabelOverlap: false,
            label: {
              show: true,
              formatter(param) {
                return param.value;
              }
            },
            labelLine: {
              show: true
            },
            data: data
          }
        ]
      })
    }
  }
}
</script>
