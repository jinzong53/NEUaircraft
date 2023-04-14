<template>
  <div>
    <el-steps :active="active" finish-status="success">
      <el-step title="步骤 1"></el-step>
      <el-step title="步骤 2"></el-step>
    </el-steps>

    <Step1 v-show="active==1"/>
    <Step2 v-show="active==2"/>
    <el-button @click="prev" >上一步</el-button>
    <el-button style="margin-top: 12px;" @click="next">下一步</el-button>
  </div>
</template>

<script>
//引入组件
import Step1 from '@/views/Po_1.vue'
import Step2 from '@/views/Po_2.vue'

export default {
  data() {
    return {
      //默认显示第一个组件
      active: 1

    }
  },
  components: {
    Step1,
    Step2,
  },
  methods: {
    //上一步
    prev() {

      //判断条件，当active小于1，显示第一个组件
      this.active--;
    },
    //下一步
    next() {

      this.active ++;
    }
  }
}
</script>

<style>

</style>
