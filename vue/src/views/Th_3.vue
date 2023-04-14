<template>
  <div class="echart" id="mychart" :style="myChartStyle"></div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  data() {
    return {
      xData: ['高度', '类型', '速度','角度','距离'], //横坐标
      yData: [0.34,0.64,0.58,0.12,0.29], //飞机1
      taskDate: [0.39,0.15,0.42,0.21,0.36],
      threeDate:[0.31,0.87,0.78,0.18,0.35],
      myChartStyle: { float: 'left', width: '100%', height: '400px' }, //图表样式
    }
  },
  mounted() {
    this.initEcharts()
  },
  methods: {
    initEcharts() {
      // 多列柱状图
      const mulColumnZZTData = {
        xAxis: {
          data: this.xData,
        },
        // 图例
        legend: {
          data: ['飞机1', '飞机2','飞机3'],
          top: '0%',
        },
        yAxis: {},
        color: ['#9ed075', '#5bc6af', '#4e98bd'],
        series: [
          {
            type: 'bar', //形状为柱状图
            data: this.yData,
            name: '飞机1', // legend属性
            label: {
              // 柱状图上方文本标签，默认展示数值信息
              show: true,
              position: 'top',
            },
          },
          {
            type: 'bar', //形状为柱状图
            data: this.taskDate,
            name: '飞机2', // legend属性
            label: {
              // 柱状图上方文本标签，默认展示数值信息
              show: true,
              position: 'top',
            },
          },
          {
            type: 'bar', //形状为柱状图
            data: this.threeDate,
            name: '飞机3', // legend属性
            label: {
              // 柱状图上方文本标签，默认展示数值信息
              show: true,
              position: 'top',
            },
          },
        ],
      }
      const myChart = echarts.init(document.getElementById('mychart'))
      myChart.setOption(mulColumnZZTData)
      //随着屏幕大小调节图表
      window.addEventListener('resize', () => {
        myChart.resize()
      })
    },
  },
}
</script>


