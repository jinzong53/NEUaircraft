<template>
  <div>
  <div class="container" style="min-height: 100%; padding-bottom: 50px;">
    请选择您威胁分析时使用的模型，可多选使用</div>
  <div>
    <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
      <el-form-item label="权值计算：" prop="field101">
        <el-checkbox-group v-model="formData.field101" size="medium">
          <el-checkbox v-for="(item, index) in field101Options" :key="index" :label="item.value"
                       :disabled="item.disabled">{{item.label}}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="评价方式：" prop="field102">
        <el-checkbox-group v-model="formData.field102" size="medium">
          <el-checkbox v-for="(item, index) in field102Options" :key="index" :label="item.value"
                       :disabled="item.disabled">{{item.label}}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item size="large">
        <el-button type="primary" @click="submitForm">确定</el-button>
<!--        <router-link to="/person">个人信息</router-link>-->
        <el-button @click="resetForm">重置</el-button>
        <el-button type="primary" @click="Th_3">开始威胁分析</el-button>
      </el-form-item>
    </el-form>
  </div> </div>

</template>
<script>
export default {
  name: "Th_2",
  components: {},
  props: [],
  data() {
    return {
      formData: {
        field101: [],
        field102: [],
      },
      rules: {
        field101: [],
        field102: [],
      },
      field101Options: [{
        "label": "熵权法",
        "value": 1
      }, {
        "label": "CRITIC法",
        "value": 2
      }],
      field102Options: [{
        "label": "灰色关联分析",
        "value": 1
      }, {
        "label": "TOPSSS",
        "value": 2
      }],
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    submitForm() {
      this.$refs['elForm'].validate(valid => {
        if (!valid) return
        // TODO 提交表单
      })
    },
    Th_3(){
      this.$router.push('/threatenAnalyseCopy1')
    },
    resetForm() {
      this.$refs['elForm'].resetFields()
    },
  }
}

</script>
<style>
</style>
