<template>
  <div>
    <el-row>
      <el-col :span="12">
        <div id="main" style="width: 400px; height: 350px"></div>
      </el-col>

      <el-col :span="12">
        <div id="pie" style="width: 400px; height: 350px"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  data() {
    return {
      xData: ['飞机1', '飞机2', '飞机3'], //横坐标
      yData: [23, 24, 18], //数据
      // myChartStyle: { float: 'left', width: '90%', height: '300px' }, //图表样式
    }
  },
  mounted() {  // 页面元素渲染之后再触发
    const option = {
      title: {
        text: '各飞机总体威胁率',
        top: '8%',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      grid: {
        top: '23%',

      },
      color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
        { offset: 0, color: '#b5c6c6' }, //0 起始颜色
        { offset: 1, color: '#193d65' }, //1 结束颜色
      ]),

      xAxis: {
        type: 'category',
        data: this.xData,
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: "检测总数",
          data: [],
          type: 'bar',
          barWidth: 38,
        },
      ]
    };

    // 饼图

    const pieOption = {
      title: {
        text: '飞机威胁比',
        top: '8%',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      color: ['#9ed075', '#5bc6af', '#4e98bd'],
      series: [
        {
          name: "威胁率",
          type: 'pie',
          radius: '50%',
          center: ['55%', '50%'],
          label:{            //饼图图形上的文本标签
            normal:{
              show:true,
              position:'inner', //标签的位置
              textStyle : {
                fontWeight : 300 ,
                fontSize : 14,    //文字的字体大小
                color: "#fff"
              },
              formatter:'{c}({d}%)%'
            }
          },
          data: [
            {name: "飞机1", value: 0.41},
            {name: "飞机2", value: 0.23},
            {name: "飞机3", value: 0.64},
          ],  // 填空
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ],

    };



    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);

    var pieDom = document.getElementById('pie');
    var pieChart = echarts.init(pieDom);



    this.request.get("/echarts/members").then(res => {

      option.series[0].data = [0.41,0.23,0.64]
      // 数据准备完毕之后再set
      myChart.setOption(option);


      pieOption.series[0].data = [
        {name: "飞机1", value: 0.41},
        {name: "飞机2", value: 0.23},
        {name: "飞机3", value: 0.64},
      ]
      pieChart.setOption(pieOption)
    })
  }
}

</script>


