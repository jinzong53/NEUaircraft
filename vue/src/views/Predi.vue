<template>
  <div>
    <div v-if=show>
      <div>
        本系统可从飞机高度、经度、纬度3个方面检测飞机威胁的位置</div>
      <div>
        同时根据以上3个维度生成飞机的轨迹预测</div>
      <div class="container" style="min-height: 100%; padding-bottom: 50px;">
        轨迹预测结果由散点图显示
      </div>
      <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
        <el-table-column prop="name" label="目标名"></el-table-column>
        <el-table-column prop="geo" label="高度"></el-table-column>
        <el-table-column prop="latitude" label="纬度"></el-table-column>
        <el-table-column prop="longitude" label="经度"></el-table-column>
        <el-table-column prop="preda" label="预测高度"></el-table-column>
        <el-table-column prop="predla" label="预测纬度"></el-table-column>
        <el-table-column prop="predlo" label="预测经度"></el-table-column>
        <el-table-column prop="time" label="时间戳"></el-table-column>


      </el-table>
      <div style="padding: 10px 0">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[2, 5, 10, 20]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
      <el-divider/>
      <div class="container" style="min-height: 100%; padding-bottom: 50px;color: #7f3c3c;font-size: 24px">
        飞机预计轨迹图
        <div id="main" style="width: 900px;height: 600px;"></div></div>

    </div>
    <loading v-if=showLoading></loading>
  </div>
</template>

<script>

import loading from '@/views/loading'
import * as echarts from 'echarts';
import "echarts-gl"

export default {
  name: "Predi",
  components: {
    loading
  },
  data() {
    return {
      showLoading:true,
      show:false,
      data:[],
      tLength:0,
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    this.load()
    //用于判断服务器时候有数据
    let times = 0;
    let timer = window.setInterval(() => {
      setTimeout(function () {
        this.request.get("/predi/isExist", {
          params: {
            tDataID: '' //后续更新可以加入用户ID来后去数据
          }
        }).then(res => {
          times++
          console.log(res.data)
          if(res.data == 1){
            alert("临时数据加载完毕")
            this.showLoading = false
            this.show =true
            clearInterval(timer);

          }
          if(times>=10){
            clearInterval(timer);
            alert("临时数据不存在，请重新上传数据")

            this.showLoading = false
            this.$router.push('/positionPrediction')
          }
        })
      }.bind(this).bind(times), 0)
    }, 1000);
  },

  mounted() {
    //请求服务器的JSON文件.并更新上述数据
    this.request.get("/file/tempPositionPredictionData.json", {
      params: {
      }
    }).then(res => {
      this.tLength = res.data.length //数据长度
      for(let i=0;i<res.data.length;i++){
        let tempItem = [];
        tempItem[0] = res.data[i].pred_latitude;
        tempItem[1] = res.data[i].pred_pred_longitude;
        tempItem[2] = res.data[i].pred_geoAltitude;
        this.data[i] = tempItem;
      }
      this.drewLine()
    });

  },

  methods: {
    drewLine(){

      var symbolSize = 5;
      var chart = echarts.init(document.getElementById('main'));
      chart.setOption({
        xAxis3D: {
          type: 'value',
          min: 50.35,
          // name: '',
          // data: dataX,
          // axisLabel:{
          //   show: true,
          //   interval: 0  //使x轴都显示
          // }
        },
        yAxis3D: {
          type: 'value',
          min: 5.45,
          // name: '',
          // data: dataY,
          // axisLabel:{
          //   show: true,
          //   interval: 0   //使y轴都显示
          // }
        },
        zAxis3D: {
          type: 'value',
          min: 6.55,
          name: ''
        },

        tooltip:{
          show:true
        },
        grid3D: {
          boxWidth: 300,
          boxHeight:120,
          boxDepth: 200,
          axisLine: {
            show: true,
            interval: 0,
            lineStyle: {
              color: '#2c3e50'
            }
          },
          viewControl: {
            distance: 400
          }
        },
        series:[
          {
            type: 'scatter3D',
            name: '飞机',
            itemStyle: {
              color: 'rgb(165,  0, 38)'
            },
            // label: {  //当type为scatter3D时有label出现
            //   show: true,
            //   position: 'top',   //标签的位置，也就是data中数据相对于线在哪个位置
            //   distance: 0,
            //   textStyle: {
            //     color: '#2c3e50',
            //     fontSize: 12,
            //     borderWidth: 0,
            //     borderColor: '#2c3e50',
            //     backgroundColor: 'transparent'
            //   }

            // },
            data: this.data,
          },

        ]
      })
      window.addEventListener('resize', function () {
        chart.resize();
      });
    },


    load() {
      this.request.get("/predi/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save() {
      this.request.post("/predi", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
      this.$nextTick(() => {
        if(this.$refs.img) {
          this.$refs.img.clearFiles();
        }
        if(this.$refs.file) {
          this.$refs.file.clearFiles();
        }
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
      this.$nextTick(() => {
        if(this.$refs.img) {
          this.$refs.img.clearFiles();
        }
        if(this.$refs.file) {
          this.$refs.file.clearFiles();
        }
      })
    },
    del(id) {
      this.request.delete("/predi/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      if (!this.multipleSelection.length) {
        this.$message.error("请选择需要删除的数据")
        return
      }
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/predi/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    handleFileUploadSuccess(res) {
      this.form.file = res
    },
    handleImgUploadSuccess(res) {
      this.form.img = res
    },
    download(url) {
      window.open(url)
    },
    exp() {
      window.open("http://localhost:9090/predi/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
