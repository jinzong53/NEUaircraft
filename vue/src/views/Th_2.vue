<template>
  <div>
    <div class="container" style="min-height: 100%; padding-bottom: 50px;">
      请选择您威胁分析时使用的模型，可多选使用
    </div>
    <div>
      <el-form>
        <el-form-item>
          <div style="display: inline;font-size: 16px;ont-family: '宋体'">评价方式:</div> &#12288
          <input type="radio" name="radios" value="grayScore" v-model="param"><label style="display: inline;font-size: 16px;ont-family: '宋体'"> 灰色关联分析</label> &#12288;
          <input type="radio" name="radios" value="topsisScore" v-model="param"><label style="display: inline;font-size: 16px;ont-family: '宋体'"> TOPSIS</label> &#12288;
          <input type="radio" name="radios" value="totalScore" v-model="param"><label style="display: inline;font-size: 16px;ont-family: '宋体'"> 综合分析</label> &#12288;
        </el-form-item>

        <el-form-item size="large">
          <el-button @click="resetForm">重置</el-button>
          <el-button type="primary" @click="Th_3">开始威胁分析</el-button>
        </el-form-item>
      </el-form>
    </div>
    <loading v-if=show></loading>
  </div>

</template>
<script>
import loading from '@/views/loading'
export default {
  name: "Th_2",
  components: {
    loading
  },
  props: [],
  data() {
    return {
      param:'grayScore', //设置默认值为grayScore，即设置第一个单选框为选中状态
      show:false,
    }
  },
  methods: {
    Th_3() {
      this.show = true
      let times = 0;
      let tempFlag = 0;
      this.request.get("threatenAnalyseCopy1/addTJson", {
        params: {
          tDataID: '', //后续更新可以加入用户ID来后去数据
          startValue: this.param
        }
      }).then(res => {
        tempFlag = 1;
        if(tempFlag == 1){
          let timer = window.setInterval(() => {
            setTimeout(function () {
              this.request.get("/threatenAnalyseCopy1/isExist", {
                params: {
                  tDataID: '' //后续更新可以加入用户ID来后去数据
                }
              }).then(res => {
                times++
                console.log(res.data)
                if(res.data == 1){
                  clearInterval(timer);
                  this.$router.push('/threatenAnalyseCopy1')
                  this.showLoading = false
                  this.show =true
                }
                if(times>=10){
                  clearInterval(timer);
                  alert("处理数据失败，请重新上传数据")
                  this.showLoading = false
                  this.show =true
				  location.reload(); //重新加载页面
                }
              })
            }.bind(this).bind(times), 0)
          }, 2000);
        }
      }).catch((res) => {
        tempFlag = 1;
        if(tempFlag == 1){
          let timer = window.setInterval(() => {
            setTimeout(function () {
              this.request.get("/threatenAnalyseCopy1/isExist", {
                params: {
                  tDataID: '' //后续更新可以加入用户ID来后去数据
                }
              }).then(res => {
                times++
                console.log(res.data)
                if(res.data == 1){
                  clearInterval(timer);
                  this.$router.push('/threatenAnalyseCopy1')
                  this.showLoading = false
                  this.show =true
                }
                if(times>=10){
                  clearInterval(timer);
                  alert("处理数据失败，请重新上传数据")
                  this.showLoading = false
                }
              })
            }.bind(this).bind(times), 0)
          }, 2000);
        }
      })


    },
    resetForm()
    {
      this.param = 'grayScore'
    },
  }
}

</script>
<style>
</style>
