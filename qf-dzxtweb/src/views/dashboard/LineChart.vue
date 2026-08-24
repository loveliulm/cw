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
    /*minWidth: {
      type: String,
      default: '846px'
    },*/
    height: {
      type: String,
      default: '405px'
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
  data () {
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
  mounted () {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy () {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart () {
      this.chart = echarts.init(this.$el, 'macarons')
      if (this.chartData.taxVat) {
        this.setOptions(this.chartData)
      }
    },
    setOptions ({ taxVat, taxBuilding, taxEducation, taxPersonal, taxCorporate } = {}) {
      this.chart.setOption({
        backgroundColor: '#fff',
        title: {
          x: 'center',
          y: 'top',
          textAlign: 'left',
          // text: this.className,
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          top: '1%',
          data: ['增值税', '城市维护建设税', '教育附加税', '个人所得税', '企业所得税'],
        },
        grid: {
          left: 12,
          right: 12,
          bottom: 10,
          top: 35,
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
          boundaryGap: false,
          axisLabel: {
            interval: 0
          },
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: taxVat.name,
            type: taxVat.type,
            data: taxVat.data
          },
          {
            name: taxBuilding.name,
            type: taxBuilding.type,
            data: taxBuilding.data
          },
          {
            name: taxEducation.name,
            type: taxEducation.type,
            data: taxEducation.data
          },
          {
            name: taxPersonal.name,
            type: taxPersonal.type,
            data: taxPersonal.data
          },
          {
            name: taxCorporate.name,
            type: taxCorporate.type,
            data: taxCorporate.data
          }]
      })
    }
  }
}
</script>
