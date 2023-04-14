<template>
  <div>
    <div>
      本系统可从飞机高度、经度、纬度3个方面检测飞机威胁的位置</div>
    <div>
      同时根据以上3个维度生成飞机的轨迹预测</div>
    <div class="container" style="min-height: 100%; padding-bottom: 50px;">
      轨迹预测结果由散点图显示
    </div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
<!--      <el-input style="width: 200px" placeholder="请输入" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>-->
<!--      <el-input style="width: 200px" placeholder="请输入" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>-->
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
<!--      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>-->
<!--      <el-popconfirm-->
<!--          class="ml-5"-->
<!--          confirm-button-text='确定'-->
<!--          cancel-button-text='我再想想'-->
<!--          icon="el-icon-info"-->
<!--          icon-color="red"-->
<!--          title="您确定批量删除这些数据吗？"-->
<!--          @confirm="delBatch"-->
<!--      >-->
<!--        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>-->
<!--      </el-popconfirm>-->
<!--       <el-upload action="http://localhost:9090/predict/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">-->
<!--        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>-->
<!--      </el-upload>-->
      <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="name" label="飞机名"></el-table-column>
      <el-table-column prop="geoAltitude" label="飞机地理高度"></el-table-column>
      <el-table-column prop="latitude" label="飞机纬度"></el-table-column>
      <el-table-column prop="longitude" label="飞机经度"></el-table-column>
      <el-table-column prop="predGeoaltitude" label="预测高度"></el-table-column>
      <el-table-column prop="predLatitude" label="预测纬度"></el-table-column>
      <el-table-column prop="predLongitude" label="预测经度"></el-table-column>
      <el-table-column prop="timeAtServer" label="时间戳"></el-table-column>

<!--      <el-table-column label="操作"  width="180" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>-->
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

    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">
        <el-form-item label="飞机名">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="飞机地理高度">
          <el-input v-model="form.geoAltitude" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="飞机纬度">
          <el-input v-model="form.latitude" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="飞机经度">
          <el-input v-model="form.longitude" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="预测高度">
          <el-input v-model="form.predGeoaltitude" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="预测纬度">
          <el-input v-model="form.predLatitude" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="预测经度">
          <el-input v-model="form.predLongitude" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="时间戳">
          <el-date-picker v-model="form.timeAtServer" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <div  id="main" style="width: 900px;height: 600px"></div>
  </div>
</template>

<script>

import * as echarts from 'echarts';
import "echarts-gl"

export default {
  name: "Predict",
  data() {
    return {
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
  },

    mounted() {
      this.drewLine()
    },


methods: {
  drewLine(){

    var symbolSize = 5;
    var data=[

      [50.46462754,	5.487967826,	6.57606,],
      [50.46327209,	5.490706594,	6.58368,],
      [50.46257924,	5.492124815,	6.5913,],
      [50.46185303,	5.493525455,	6.5913,],
      [50.4606706,	5.495984876,	6.60654,],
      [50.46006542,	5.49724682,	6.60654,],
      [50.45878601,	5.499885961,	6.62178,],
      [50.45820333,	5.501032649,	6.62178,],
      [50.45750427,	5.502487986,	6.6294,],
      [50.45703953,	5.503482303,	6.63702,],
      [50.456574,	5.50452155,	6.64464,],
      [50.45544434,	5.506824694,	6.64464,],
      [50.45411682,	5.509643555,	6.65988,],
      [50.45310974,	5.511884187,	6.6675,],
      [50.45136015,	5.515730574,	6.69036,],
      [50.44963772,	5.519516404,	6.69798,],
      [50.44731011,	5.524786872,	6.72084,],
      [50.44447042,	5.531170819,	6.7437,],
      [50.44340515,	5.53356773,	6.75894,],
      [50.44181694,	5.537109375,	6.77418,],
      [50.44047546,	5.540145071,	6.78942,],
      [50.43986175,	5.54148906,	6.78942,],
      [50.43934968,	5.542676771,	6.80466,],
      [50.43753414,	5.546759528,	6.8199,],
      [50.43695068,	5.548023425,	6.81228,],
      [50.43644714,	5.54917988,	6.82752,],
      [50.43585205,	5.550480893,	6.82752,],
      [50.43515997,	5.552029996,	6.83514,],
      [50.43452454,	5.55344431,	6.83514,],
      [50.43385651,	5.554925042,	6.84276,],
      [50.43329788,	5.556186985,	6.85038,],
      [50.43278503,	5.557347348,	6.85038,],
      [50.43223572,	5.558503803,	6.86562,],
      [50.4316864,	5.559732538,	6.86562,],
      [50.43115648,	5.56093783,	6.86562,],
      [50.43049622,	5.562406841,	6.88086,],
      [50.4299469,	5.563563297,	6.88086,],
      [50.42948914,	5.564575195,	6.88086,],
      [50.42817714,	5.567470241,	6.8961,],
      [50.42640815,	5.571330302,	6.8961,],
      [50.42573547,	5.572814941,	6.90372,],
      [50.42523193,	5.573971397,	6.91134,],
      [50.42459262,	5.575338828,	6.91896,],
      [50.42408054,	5.576452307,	6.91134,],
      [50.42356847,	5.577640018,	6.92658,],
      [50.42298889,	5.578886333,	6.92658,],
      [50.42230225,	5.580404181,	6.9342,],
      [50.42179948,	5.581500079,	6.9342,],
      [50.42114775,	5.582910486,	6.9342,],
      [50.42068223,	5.583949733,	6.94182,],
      [50.42001343,	5.585391396,	6.94182,],
      [50.41937877,	5.586770547,	6.94944,],
      [50.41809082,	5.589655826,	6.95706,],
      [50.41747012,	5.591001768,	6.95706,],
      [50.41680908,	5.592474686,	6.96468,],
      [50.41551493,	5.595307221,	6.9723,],
      [50.41438293,	5.597823294,	6.9723,],
      [50.4137925,	5.599167283,	6.97992,],
      [50.41314077,	5.60057769,	6.97992,],
      [50.41191101,	5.603244179,	6.98754,],
      [50.41145325,	5.604328356,	6.98754,],
      [50.41094971,	5.605340255,	6.99516,],
      [50.40988211,	5.607778188,	6.99516,],
      [50.40925598,	5.609098736,	7.00278,],
      [50.40857865,	5.610673234,	7.00278,],
      [50.40802002,	5.611786713,	7.00278,],
      [50.40687561,	5.614302786,	7.0104,],
      [50.40637207,	5.615459241,	7.0104,],
      [50.4057312,	5.616904811,	7.01802,],
      [50.40518033,	5.618170661,	7.01802,],
      [50.40449524,	5.619651393,	7.02564,],
      [50.40161133,	5.626011899,	7.04088,],
      [50.39978027,	5.630047772,	7.0485,],
      [50.39805784,	5.633907834,	7.06374,],
      [50.39573023,	5.639029838,	7.07898,],
      [50.39503195,	5.640588709,	7.07898,],
      [50.39447021,	5.641768606,	7.0866,],
      [50.3939209,	5.64299734,	7.0866,],
      [50.39321641,	5.644523002,	7.09422,],
      [50.39270433,	5.645710713,	7.09422,],
      [50.39204407,	5.647117213,	7.10184,],
      [50.39099121,	5.649430124,	7.10946,],
      [50.39042327,	5.650758486,	7.10946,],
      [50.38970947,	5.652248985,	7.11708,],
      [50.38920593,	5.65340544,	7.11708,],
      [50.38856506,	5.65485101,	7.1247,],
      [50.38806152,	5.655935187,	7.1247,],
      [50.38739738,	5.657365129,	7.13232,],
      [50.38677979,	5.658754048,	7.13232,],
      [50.38413872,	5.664565628,	7.15518,],
      [50.38105774,	5.671330502,	7.17804,],
      [50.38041687,	5.672776072,	7.17804,],
      [50.37918091,	5.675522654,	7.18566,],
      [50.37867737,	5.676606831,	7.19328,],
      [50.3782196,	5.677691008,	7.19328,],
      [50.37650415,	5.68141628,	7.20852,],
      [50.37579346,	5.683039615,	7.21614,],
      [50.3747406,	5.685352526,	7.22376,],
      [50.37408344,	5.68676098,	7.22376,],]


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
/*      //图例
      legend: {
        orient: 'vertical',
        right: 50,
        top: 200,
        icon: 'roundRect',
        textStyle:{
          color: '#2c3e50'
        }
      },*/
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
          data: data,
        },

      ]
    })
    window.addEventListener('resize', function () {
      chart.resize();
    });
  },


    load() {
      this.request.get("/predict/page", {
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
      this.request.post("/predict", this.form).then(res => {
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
      this.request.delete("/predict/" + id).then(res => {
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
      this.request.post("/predict/del/batch", ids).then(res => {
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
      window.open("http://localhost:9090/predict/export")
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
