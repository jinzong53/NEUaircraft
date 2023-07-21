<template>
  <div class="echart" id="mychart" :style="myChartStyle"></div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  data() {
    return {
      tLength: 0,
      tName: [],
      xData: ['高度', '类型', '速度','角度','距离'], //横坐标
      tHeight: [],
      tDistance: [],
      tSpeed:[],
      tMod:[],
      tAngle:[],
      tSeries:[], //自定义全部数据
      test:[],
      myChartStyle: { float: 'left', width: '100%', height: '400px' }, //图表样式
    }
  },
  created() {

  },
  mounted() {
    //请求服务器的JSON文件.并更新上述数据
    this.request.get("/file/tempThreatenData.json", {
      params: {
      }
    }).then(res => {
      this.tLength = res.data.length //数据长度
      for(let i=0;i<res.data.length;i++){
        this.tName[i] = res.data[i].data.name
        this.tHeight[i] = res.data[i].data.treat_height
        this.tDistance[i] = res.data[i].data.treat_distance
        this.tSpeed[i] = res.data[i].data.treat_speed
        this.tMod[i] = res.data[i].data.treat_mod
        this.tAngle[i] = res.data[i].data.treat_angle

        //更新TS
        let tempLabel = {};
                        tempLabel['show']='true';
                        tempLabel['position']='top';
        let tempItem = {};
                        tempItem['type']='bar';
                        tempItem['data']=[this.tHeight[i],this.tDistance[i],this.tSpeed[i],this.tMod[i],this.tAngle[i]];
                        tempItem['name']=this.tName[i];
                        tempItem['label']=tempLabel;
        this.tSeries[i] = tempItem;
      }
      console.log(this.tSeries);
      this.initEcharts() //更新数据
    });

  },
  methods: {
    initEcharts() {

      // 多列柱状图
      const mulColumnZZTData = {
        xAxis: {
          data: this.xData,
        },
        tooltip: {
          trigger: 'item'
        },
        label:{
          // 内容格式器
          formatter: (value,index)=> {
            return value.value.toFixed(3)
          },
        },
        // 图例
        legend: {
          data: this.tName,
          top: '0%',
        },
        yAxis: {},
        color: ['#9ed075', '#5bc6af', '#4e98bd','#0792d7','#9861d3'],
        series: this.tSeries
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


