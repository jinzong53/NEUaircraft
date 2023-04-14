<template>
  <div>
    <div class="container" style="min-height: 100%; padding-bottom: 50px;">
      请选择您威胁分析时使用的模型，可多选使用</div>
    <div>
      <el-row :gutter="15">
        <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
<!--          <el-col :span="13">-->
<!--            <el-form-item label="飞机名称" prop="field101">-->
<!--              <el-input v-model="formData.field101" placeholder="请输入检测飞机名称" clearable :style="{width: '100%'}">-->
<!--              </el-input>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
          <el-col :span="13">
            <el-form-item label="选择模型" prop="field102">
              <el-radio-group v-model="formData.field102" size="medium">
                <el-radio v-for="(item, index) in field102Options" :key="index" :label="item.value"
                          :disabled="item.disabled">{{item.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item size="large">
              <el-button type="primary" @click="submitForm">提交</el-button>
              <el-button @click="resetForm">重置</el-button>
              <el-button type="primary" @click="Po_3">开始威胁分析</el-button>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
    </div>
  </div>

</template>
<script>
export default {
  components: {},
  props: [],
  data() {
    return {
      formData: {
        field101: undefined,
        field102: undefined,
      },
      rules: {
        field101: [{
          required: true,
          message: '请输入检测飞机名称',
          trigger: 'blur'
        }],
        field102: [{
          required: true,
          message: '选择模型不能为空',
          trigger: 'change'
        }],
      },
      field102Options: [{
        "label": "模型1",
        "value": 1
      }, {
        "label": "模型2",
        "value": 2
      }, {
        "label": "模型3",
        "value": 3
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
    Po_3(){
      this.$router.push('/predict')
    },
    resetForm() {
      this.$refs['elForm'].resetFields()
    },
  }
}

</script>
<style>
</style>
