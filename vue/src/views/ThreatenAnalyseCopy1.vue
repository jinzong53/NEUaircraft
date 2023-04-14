<template>
  <div>
    <div>
      本系统可从飞机高度、类型、速度、角度、距离五个方面检测飞机威胁的大小</div>
    <div>
      同时根据以上五个维度生成飞机总威胁值</div>
    <div class="container" style="min-height: 100%; padding-bottom: 50px;">
      威胁值由0至1逐渐增大
    </div>

<!--    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
&lt;!&ndash;      <el-input style="width: 200px" placeholder="请输入" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>&ndash;&gt;
&lt;!&ndash;      <el-input style="width: 200px" placeholder="请输入" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>&ndash;&gt;
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>-->

 <!--   <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      &lt;!&ndash; <el-upload action="http://localhost:9090/threatenAnalyseCopy1/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button> &ndash;&gt;
    </div>
-->
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="name" label="飞机名"></el-table-column>
      <el-table-column prop="height" label="高度威胁值"></el-table-column>
      <el-table-column prop="type" label="类型威胁值"></el-table-column>
      <el-table-column prop="velocity" label="速度威胁值"></el-table-column>
      <el-table-column prop="angle" label="角度威胁值"></el-table-column>
      <el-table-column prop="distance" label="距离威胁值"></el-table-column>
      <el-table-column prop="sumTest" label="总威胁值"></el-table-column>

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
<!--    <div class="echart" id="mychart" :style="myChartStyle"></div>-->


<!--    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>-->

    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">
        <el-form-item label="飞机名">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="高度威胁值">
          <el-input v-model="form.height" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="类型威胁值">
          <el-input v-model="form.type" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="速度威胁值">
          <el-input v-model="form.velocity" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="角度威胁值">
          <el-input v-model="form.angle" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="距离威胁值">
          <el-input v-model="form.distance" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="总威胁值">
          <el-input v-model="form.sumTest" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
    <el-divider/>
    <div>
      <button @mouseenter="hand1">各项威胁分析</button>
      <button @mouseenter="hand2">总体威胁分析</button>
      <component :is="map"></component>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import Th_3 from '@/views/Th_3.vue'
import Th_4 from '@/views/Th_4.vue'

export default {
  name: "ThreatenAnalyseCopy1",
  components: {
    Th_3,
    Th_4,
  },
  data() {
    return {
      Th_3,
      Th_4,
      map: 'Th_3',
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      // xData: ["飞机1", "飞机2", "飞机3"], //横坐标
      // yData: [0.41, 0.64, 0.23], //数据
      // myChartStyle: {  width: "25%", height: "300px" }, //图表样式
    }
  },
  created() {
    this.load()
  },
  mounted() {
    this.initEcharts();
  },
  methods: {
    hand1() {
      this.map = this.map === 'Th_3' ? 'Th_4' : 'Th_3'
    },
    hand2() {
      this.map = this.map === 'Th_4' ? 'Th_3' : 'Th_4'
    },
    initEcharts() {
      // 基本柱状图
      const option = {
        xAxis: {
          data: this.xData
        },
        yAxis: {},
        series: [
          {
            type: "bar", //形状为柱状图
            data: this.yData
          }
        ]
      };
      const myChart = echarts.init(document.getElementById("mychart"));
      myChart.setOption(option);
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
    load() {
      this.request.get("/threatenAnalyseCopy1/page", {
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
      this.request.post("/threatenAnalyseCopy1", this.form).then(res => {
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
      this.request.delete("/threatenAnalyseCopy1/" + id).then(res => {
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
      this.request.post("/threatenAnalyseCopy1/del/batch", ids).then(res => {
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
      window.open("http://localhost:9090/threatenAnalyseCopy1/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  },
  computed: {},
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
