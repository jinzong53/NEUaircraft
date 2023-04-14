<!--<template>-->
<!--  <div>-->

<!--    <a-steps :current="current">-->
<!--      <a-step v-for="item in steps" :key="item.title" :title="item.title" />-->
<!--    </a-steps>-->
<!--    <div class="steps-content">{{steps[current].content}}</div>-->
<!--    <div class="steps-action">-->
<!--      <el-button v-if="current < steps.length - 1" type="primary" @click="next">-->
<!--        下一步-->
<!--      </el-button>-->
<!--      <el-button-->
<!--          v-if="current == steps.length - 1"-->
<!--          type="primary"-->
<!--          @click="$message.success('Processing complete!')"-->
<!--      >-->
<!--        开始威胁分析-->
<!--      </el-button>-->
<!--      <el-button v-if="current>0" style="margin-left: 8px" @click="prev">-->
<!--        上一步-->
<!--      </el-button>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->

<!--export default {-->
<!--  data() {-->
<!--    return {-->
<!--      current: 0,-->
<!--      steps: [-->
<!--        {-->
<!--          title: '下载模板',-->
<!--          content: 'First-content',-->
<!--        },-->
<!--        {-->
<!--          title: '上传CSV文件',-->
<!--          content: 'Second-content',-->
<!--        },-->
<!--        {-->
<!--          title: '选择检测模型',-->
<!--          content: 'Last-content',-->
<!--        },-->
<!--      ],-->
<!--    };-->
<!--  },-->
<!--  methods: {-->
<!--    next() {-->
<!--      this.current++;-->
<!--    },-->
<!--    prev() {-->
<!--      this.current&#45;&#45;;-->
<!--    },-->
<!--  },-->
<!--};-->
<!--</script>-->

<!--<style scoped>-->
<!--.steps-content {-->
<!--  margin-top: 16px;-->
<!--  border: 1px dashed #e9e9e9;-->
<!--  border-radius: 6px;-->
<!--  background-color: #fafafa;-->
<!--  min-height: 200px;-->
<!--  text-align: center;-->
<!--  padding-top: 80px;-->
<!--}-->

<!--.steps-action {-->
<!--  margin-top: 24px;-->
<!--}-->
<!--</style>-->

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
import Step1 from '@/views/Th_1.vue'
import Step2 from '@/views/Th_2.vue'

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
