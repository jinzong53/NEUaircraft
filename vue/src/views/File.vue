<template>
  <div>
    <el-row :gutter="15">
      <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">

        <el-col :span="14">
          <p>
            <el-form-item label="选择模型" prop="field101">
              <el-select v-model="formData.field101" placeholder="请选择选择模型" clearable
                         :style="{width: '100%'}" v-on:change="indexSelect($event)">
                <el-option v-for="(item, index) in field101Options" :key="index" :label="item.label"
                           :value="item.value" :disabled="item.disabled" ></el-option>
              </el-select>
              <b>当前选中模型：</b>
              <b v-model="functionContent" style="color: #42b983;font-size: 14px;">
                {{functionContent}}
              </b>
            </el-form-item>

          </p>
        </el-col>


        <el-col :span="12">
          <el-form-item label="选择权重" prop="field102">
            <el-radio-group v-model="formData.field102" size="medium">
              <el-radio v-for="(item, index) in field102Options" :key="index" :label="item.value"
                        :disabled="item.disabled">{{ item.label }}
              </el-radio>
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
          <br/>
          <el-form-item label="上传图片" prop="field103">
            <el-upload ref="field103" :file-list="field103fileList" :action="field103Action":on-success="onSuccess" multiple
                       :before-upload="field103BeforeUpload" list-type="picture">
              <el-button size="small" type="primary" icon="el-icon-upload">点击上传（可批量）</el-button>
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <br/>
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
            <el-button type="primary" @click="run" icon="el-icon-search" size="medium"> 运行</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item>
            <el-button type="primary" @click="resume" size="medium"> 恢复</el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>


    <div style="width: 550px;text-align: center;">

      <div style="display: flex;">
        <div style="flex: 1;">
          <div>
            <el-tag>初始图片</el-tag><br>
            <div >
              <el-image id="picture1"
                        style="width: 350px; height: 300px;"
                        :src="imgArr[0]"
						:preview-src-list="imgArr">
			  </el-image>
            </div>
          </div>
        </div>
        <div style="flex: 1;text-align: center;">
          <div style="width: 100px;height: 350px"></div>
          <div>
            <el-button @click="run2">
              超时加载
            </el-button>
          </div>
        </div>
        <div style="flex: 1;">
          <div>
            <el-tag type="warning">检测结果</el-tag>
            <div>
              <el-image id="picture2"
                        style="width: 350px; height: 300px;"
                        :src="imgArr1[0]"
						:preview-src-list="imgArr1">
              </el-image>
            </div>
          </div>
        </div>
      </div>
    </div>
	
    <div>
      <loading v-if="show"></loading>
    </div>
  </div>
</template>
<script>
import loading from '@/views/loading'
export default {
  components: {
    loading
  },
  props: [],
  data() {
    return {
      show: false, //加载页面
      functionContent:"请选择选择模型",
      imgArr: [
        "http://localhost:9090/file/waitsubmit.png", //待提交的图片
      ],
      imgArr1: [
        "http://localhost:9090/file/waitsolve.png",  //待检测的图片
      ],
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
        "value": 3
      }],
      field102Options: [{
        "label": "improve_yolo",
        "value": 1
      }, {
        "label": "wsdan",
        "value": 2
      }, {
        "label": "yolo_single",
        "value": 3
      },{
        "label": "yolo",
        "value": 4
      }],
      picData:{
        fileState:"null",
        fileName:"null",
        fileModel:"null",
        fileWeight:"null"
      }
    }
  },
  computed: {},
  watch: {},
  created() {
  },
  mounted() {
  },
  methods: {
    /*
    * 2023/4/19
    * 内容：选择模型的下拉框事件
    * */
    indexSelect(event){
      console.log(this.formData.field101);
      console.log(this.field101Options[this.formData.field101-1].label);
      this.functionContent = this.field101Options[this.formData.field101-1].label;
    },

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
    /*
    * 2023/6/30
    * 内容：目标检测
    * */
    run() {
      //第一步先在服务器建立txt文件
      this.request.get("/file/setPicData", {
        params: {
          fileState: this.picData.fileState,
          fileModel: this.picData.fileModel,
          fileName: this.picData.fileName,
          fileWeight: this.picData.fileWeight
        }
      }).then(res => {
        console.log(res) //这里容易超时
      })


      //不断询问服务器是否加载完成
      this.show = true;
      let flag = 0;
      let times = 1;
      let timer = window.setInterval(() => {
        setTimeout(function () {
          this.request.get("/file/isExist", {
            params: {
              picID: this.picData.fileName
            }
          }).then(res => {
            flag = res.data;
            times++;
            var msg = document.getElementById("picture1");
            msg.src = 'http://localhost:9090/file/' + this.picData.fileName;  //首先确定提交的图片的可以显示
			this.imgArr[0] = msg.src; //设置图片放大功能
			
            if(flag==1){
              clearInterval(timer);
              var msg1 = document.getElementById("picture2");
              this.show = false;
              msg1.src = 'http://localhost:9090/file/' + 'detect_' + this.picData.fileName; //如果完成的话
			  this.imgArr1[0] = msg1.src; //设置图片放大功能
			  
            }
            if(times>=250){
              clearInterval(timer);
              this.show = false;
              window.alert("服务器响应超时，请使用超时加载")
            }
          })
        }.bind(this).bind(flag).bind(times), 0)
      }, 2000);
    },

    //手动获取
    run2(){
      var msg = document.getElementById("picture1");
      var msg1 = document.getElementById("picture2");
      msg.src = 'http://localhost:9090/file/' + this.picData.fileName;
      msg1.src = 'http://localhost:9090/file/' + 'detect_' + this.picData.fileName;
	  this.imgArr[0] = msg.src; //设置图片放大功能
	  this.imgArr1[0] = msg1.src; //设置图片放大功能
    },

    resume() {
      var msg = document.getElementById("picture1");
      var msg1 = document.getElementById("picture2");
    },
    /*
    * 2023/6/30
    * 内容：重置事件修改
    * */
    resetForm() {
      this.functionContent = "请选择选择模型"
      this.$refs['elForm'].resetFields()
    },

    /*
   * 2023/6/30
   * 内容：单张图片信息
   * */
    onSuccess(res){

      if(res == "fail"){
        window.alert("MD5验证失败，图片上传重复，请重新上传")
        location.reload();
      }

      //直接记录单张图片信息,获取action结果,因为顺序问题
      this.picData.fileState = "yes"
      this.picData.fileModel = 'yolo' //默认是yolo
      this.picData.fileName = res
      this.picData.fileWeight = this.field102Options[this.formData.field102-1].label
    },

    field103BeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 100
      console.log("pass")
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
