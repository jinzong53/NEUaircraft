<template>
  <div>
    <div class="container" style="min-height: 100%; padding-bottom: 50px; font-size:16px">
      请选择您威胁分析时使用的模型，可多选使用</div>
    <div>
      <el-row :gutter="15">
        <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
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
              <el-button @click="resetForm">重置</el-button>
              <el-button type="primary" @click="Po_3">开始威胁分析</el-button>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
    </div>
    <loading v-if=showLoading></loading>
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
      showLoading:false,
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

    Po_3(){
      this.showLoading = true

      let tempFlag = 0;
      //先启动一次Json加载接口
      this.request.get("/predi/addTJson", {
        params: {
          tDataID: '' //后续更新可以加入用户ID来后去数据
        }
      }).then(res => {
        tempFlag = 1;
        let times = 0;
        //询问结果
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
                clearInterval(timer);
                this.showLoading = false
                this.$router.push('/predict')
              }
              if(times>=200){
                alert("处理数据失败，请重新上传数据")
                clearInterval(timer);
                this.showLoading = false
				location.reload(); //重新加载页面
              }
            })
          }.bind(this).bind(times), 0)
        }, 1000);
      }).catch((res) => {  //失败的回调
        tempFlag = 1;
        let times = 0;
        //询问结果
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
                clearInterval(timer);
                this.showLoading = false
                this.$router.push('/predict')
              }
              if(times>=200){
                alert("处理数据失败，请重新上传数据")
                clearInterval(timer);
                this.showLoading = false
              }
            })
          }.bind(this).bind(times), 0)
        }, 1000);
      });



    },
    resetForm() {
      this.$refs['elForm'].resetFields()
    },
  }
}

</script>
<style>
</style>
