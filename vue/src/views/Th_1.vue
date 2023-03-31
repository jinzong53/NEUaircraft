<template>
  <div class="container" style="min-height: 100%; padding-bottom: 100px;">
    Hello，欢迎使用本系统威胁分析功能，请下载模板后填写上传，示例如下：
    <el-upload action="http://localhost:9090/threaten/import" :show-file-list="false" accept="csv" :on-success="handleExcelImportSuccess" style="display: inline-block">
      <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
    </el-upload>
    <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
    </div>
</template>

<script>
export default {
  name: "Th_1",
  methods: {
    exp() {
      window.open("http://localhost:9090/threaten/export")
    },
  }
}

</script>

<style scoped>

</style>