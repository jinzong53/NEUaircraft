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
        <br/>
        <el-divider/>
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
          <el-form-item label="上传图片" prop="field103" >
            <el-upload ref="field103" :file-list="field103fileList" :action="field103Action" multiple
                       :before-upload="field103BeforeUpload" list-type="picture">
              <el-button size="small" type="primary" icon="el-icon-upload">点击上传（可批量）</el-button>
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <br />
          <el-form-item label="上传数据集" prop="field109">
            <el-upload ref="field109" :file-list="field103fileList" :action="field103Action" multiple
                       :before-upload="field103BeforeUpload" list-type="picture">
              <el-button size="small" type="primary" icon="el-icon-upload">点击上传</el-button>
            </el-upload>
          </el-form-item>
        </el-col>


        <el-col :span="24">
          <el-form-item size="large">
            <el-button type="primary" @click="submitForm">提交</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="检测">
            <el-button type="primary" @click="run" icon="el-icon-search" size="medium"> 运行 </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item>
            <el-button type="primary" @click="resume" size="medium"> 恢复 </el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>




    <div style="width: 550px;text-align: center;">
      <el-tag>初始图片</el-tag>
      <div style=":block">
          <el-image id="picture1"
                    style="width: 350px; height: 300px"
                    :src="imgArr[0]"></el-image>
      </div>

      <el-tag type="warning">检测结果</el-tag>
      <div class="block">
        <el-image id="picture2"
            style="width: 350px; height: 300px"
                  :src="imgArr1[0]">
          <div slot="placeholder" class="image-slot">
            加载中<span class="dot">...</span>
          </div>
        </el-image>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  components: {},
  props: [],
  data() {
    return {

      imgArr:[
          "http://localhost:9090/file/8a7d34e080c04172a3f801f943972f7f.jpg",
          "http://localhost:9090/file/0aff099ca37e41cbbff4e4f045c05634.jpg",

      ],
      imgArr1:[
        "http://localhost:9090/file/8b7d34e080c04172a3f801f943972f7f.jpg",
        "http://localhost:9090/file/9bff099ca37e41cbbff4e4f045c05634.jpg",

      ],
      // src1: 'http://localhost:9090/file/8a7d34e080c04172a3f801f943972f7f.jpg',
      // src2: 'http://localhost:9090/file/8b7d34e080c04172a3f801f943972f7f.jpg',
      formData: {
        field101: '',
        field102: undefined,
        field108: undefined,
        field103: null,
        field104: undefined,
        field105: undefined,
        field111: undefined,
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
      this.request.get("/print", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total,
            console.log(res)

      })
    },
    run(){
      var msg=document.getElementById("picture1");
      // msg.src='http://localhost:9090/file/0aff099ca37e41cbbff4e4f045c05634.jpg';
      var msg1=document.getElementById("picture2");
      msg1.src='http://localhost:9090/file/9bff099ca37e41cbbff4e4f045c05634.jpg';

      msg.src='http://localhost:9090/file/0aff099ca37e41cbbff4e4f045c05634.jpg';
  },

    resume(){
    var msg=document.getElementById("picture1");
    msg.src='http://localhost:9090/file/8a7d34e080c04172a3f801f943972f7f.jpg';
    var msg1=document.getElementById("picture2");
    msg1.src='http://localhost:9090/file/8b7d34e080c04172a3f801f943972f7f.jpg';

  },
    resetForm() {
      this.$refs['elForm'].resetFields()
    },
    field103BeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 100
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
