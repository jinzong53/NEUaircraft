<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 60px">
      <el-col :span="6">
        <el-card style="color: #409EFF">
          <div><i class="el-icon-user-solid" /> 检测总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            100
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #F56C6C">
          <div><i class="el-icon-money" /> 高危飞机</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            6
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #67C23A">
          <div><i class="el-icon-bank-card" /> 登录人数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            3
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #E6A23C">
          <div><i class="el-icon-s-shop" /> 高频飞机</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            民航
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <div id="main" style="width: 500px; height: 450px"></div>
      </el-col>

      <el-col :span="12">
        <div id="pie" style="width: 500px; height: 400px"></div>
      </el-col>
    </el-row>

    <el-row style="margin-top: 20px">
      <el-col :span="12">
        <div id="candlestick" style="width: 500px; height: 450px"></div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: "Home",
  data() {
    return {

    }
  },
  mounted() {  // 页面元素渲染之后再触发
    const option = {
      title: {
        text: '近期检测数量统计',
        subtext: '趋势图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      xAxis: {
        type: 'category',
        data: ["2023/3/18", "2023/3/19", "2023/3/20", "2023/3/21"]
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: "高危飞机",
          data: [],
          type: 'bar'
        },
        {
          name: "高危飞机",
          data: [],
          type: 'line'
        },
        {
          name: "检测总数",
          data: [],
          type: 'bar'
        },
        {
          name: "检测总数",
          data: [],
          type: 'line'
        }
      ]
    };

    // 饼图

    const pieOption = {
      title: {
        text: '近期检测数量统计',
        subtext: '比例图',
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
      series: [
        {
          name: "高危飞机",
          type: 'pie',
          radius: '55%',
          center: ['25%', '70%'],
          label:{            //饼图图形上的文本标签
            normal:{
              show:true,
              position:'inner', //标签的位置
              textStyle : {
                fontWeight : 300 ,
                fontSize : 14,    //文字的字体大小
                color: "#fff"
              },
              formatter:'{c}({d}%)'
            }
          },
          data: [],  // 填空
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        },
        {
          name: "检测总数",
          type: 'pie',
          radius: '50%',
          center: ['75%', '50%'],
          label:{            //饼图图形上的文本标签
            normal:{
              show:true,
              position:'inner', //标签的位置
              textStyle : {
                fontWeight : 300 ,
                fontSize : 14,    //文字的字体大小
                color: "#fff"
              },
              formatter:'{d}%'
            }
          },
          data: [
            {name: "2023/3/18", value: 5},
            {name: "2023/3/19", value: 6},
            {name: "2023/3/20", value: 7},
            {name: "2023/3/21", value: 8}
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
      // 填空
      // option.xAxis.data = res.data.x
      option.series[0].data = res.data
      option.series[1].data = res.data

      option.series[2].data = [5,6,7,8]
      option.series[3].data = [5,6,7,8]
      // 数据准备完毕之后再set
      myChart.setOption(option);


      pieOption.series[0].data = [
        {name: "2023/3/18", value: res.data[0]},
        {name: "2023/3/19", value: res.data[1]},
        {name: "2023/3/20", value: res.data[2]},
        {name: "2023/3/21", value: res.data[3]},
      ]
      pieChart.setOption(pieOption)
    })
  }
}
</script>

<style scoped>

</style>
