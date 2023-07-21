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
      tscoret: "grayScore",
      tLength: 0,
      tName: [], //横坐标
      tScore: [], //数据
      tSeries: []
      // myChartStyle: { float: 'left', width: '90%', height: '300px' }, //图表样式
    }
  },
  mounted() {  // 页面元素渲染之后再触发
    //请求服务器的JSON文件.并更新上述数据
    this.request.get("/file/tempThreatenData.json", {
      params: {}
    }).then(res => {

      this.tscoret = res.type;
      this.tLength = res.data.length //数据长度
      for (let i = 0; i < res.data.length; i++) {
        this.tName[i] = res.data[i].data.name
        if(res.type == 'totalScore'){
          this.tScore[i] = res.data[i].data.total_score
          let tempItem = {};
          //{name: "目标1", value: 0.5499428844812302},
          tempItem['name'] = res.data[i].data.name;
          tempItem['value'] = res.data[i].data.total_score;
          this.tSeries[i] = tempItem;
        }else if(res.type == 'topsisScore'){
          this.tScore[i] = res.data[i].data.topsis_score
          let tempItem = {};
          //{name: "目标1", value: 0.5499428844812302},
          tempItem['name'] = res.data[i].data.name;
          tempItem['value'] = res.data[i].data.topsis_score;
          this.tSeries[i] = tempItem;
        }else{
          this.tScore[i] = res.data[i].data.gray_score
          let tempItem = {};
          //{name: "目标1", value: 0.5499428844812302},
          tempItem['name'] = res.data[i].data.name;
          tempItem['value'] = res.data[i].data.gray_score;
          this.tSeries[i] = tempItem;
        }

      }
      console.log(this.tSeries);
      this.initEcharts() //更新数据
    });


    const option = {
      title: {
        text: '各目标总体威胁指数',
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
        {offset: 0, color: '#b5c6c6'}, //0 起始颜色
        {offset: 1, color: '#193d65'}, //1 结束颜色
      ]),
      xAxis: {
        data: this.tName,
      },
      yAxis: {
        type: 'value'
      },
      label:{
        // 内容格式器
        formatter: (value,index)=> {
          return value.value.toFixed(3)
        },
      },
      series: [
        {
          name: "威胁指数",
          data: 1,
          type: 'bar',
        },
      ]
    };


    // 饼图
    const pieOption = {
      title: {
        text: '目标威胁占比',
        top: '8%',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c} '
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      color: ['#9ed075', '#5bc6af', '#4e98bd', '#0792d7', '#9861d3'],
      series: [
        {
          name: "威胁率",
          type: 'pie',
          radius: '50%',
          center: ['55%', '50%'],
          label: {            //饼图图形上的文本标签
            normal: {
              show: true,
              position: 'inner', //标签的位置
              textStyle: {
                fontWeight: 300,
                fontSize: 14,    //文字的字体大小
                color: "#fff"
              },
              formatter: (value,index)=> {
                return value.value.toFixed(3)
              },
            }
          },
          data: this.tSeries,  // 填空
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0,0, 0.5)'
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

      option.series[0].data = this.tScore
      // 数据准备完毕之后再set
      myChart.setOption(option);


      pieOption.series[0].data = this.tSeries
      pieChart.setOption(pieOption)
    })
  }
}

</script>


