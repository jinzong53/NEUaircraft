<template>
  <div>
    <el-row :gutter="15">
      <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
        <el-col :span="14">
          <p><el-form-item label="选择模型" prop="field101">
            <el-select v-model="formData.field101" placeholder="请选择选择模型" clearable
                       :style="{width: '100%'}">
              <el-option v-for="(item, index) in field101Options" :key="index" :label="item.label"
                         :value="item.value" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item></p>
        </el-col>

        <el-col :span="24">
          <el div style="color: #42b983;font-size: 14px;">
        <b>yolo_改进版为yolov5基础上我们提供的更高效精准的无人机检测模型</b></el>
        <br />
        <el-divider />
        </el-col>

        <el-col :span="12">
          <el-form-item label="选择权重" prop="field102">
            <el-radio-group v-model="formData.field102" size="medium">
              <el-radio v-for="(item, index) in field102Options" :key="index" :label="item.value"
                        :disabled="item.disabled">{{item.label}}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label-width="115px" label="输入权重文件" prop="field108">
            <el-input v-model="formData.field108" type="textarea" placeholder="请输入输入权重文件" :maxlength="2000"
                      show-word-limit :autosize="{minRows: 1, maxRows: 200}" :style="{width: '100%'}"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <br />
          <el-form-item label="上传图片" prop="field103" required>
            <el-upload ref="field103" :file-list="field103fileList" :action="field103Action" multiple
                       :before-upload="field103BeforeUpload" list-type="picture">
              <el-button size="small" type="primary" icon="el-icon-upload">点击上传（可批量）</el-button>
            </el-upload>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item size="large">
            <el-button type="primary" @click="submitForm">提交</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
  </div>
</template>
<script>
export default {
  components: {},
  props: [],
  data() {
    return {
      formData: {
        field101: '',
        field102: undefined,
        field108: undefined,
        field103: null,
        field104: undefined,
        field105: undefined,
      },
      rules: {
        field101: [{
          required: true,
          message: '请选择选择模型',
          trigger: 'change'
        }],
        field102: [{
          required: true,
          message: '选择权重不能为空',
          trigger: 'change'
        }],
        field108: [],
      },
      field103Action: 'http://localhost:9090/file/upload',
      field103fileList: [],
      field101Options: [{
        "label": "yolo_改进版",
        "value": 1
      }, {
        "label": "yolov5",
        "value": 2
      }, {
        "label": "rcnn",
        "value": ""
      }],
      field102Options: [{
        "label": "权重1",
        "value": 1
      }, {
        "label": "权重2",
        "value": 2
      }, {
        "label": "上传权重",
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
    resetForm() {
      this.$refs['elForm'].resetFields()
    },
    field103BeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 2
      if (!isRightSize) {
        this.$message.error('文件大小超过 100MB')
      }
      return isRightSize
    },
  }
}

</script>
<style>
.el-upload__tip {
  line-height: 1.2;
}

</style>
