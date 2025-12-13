<template>
    <el-container>
      <el-header>
          <Header/>
      </el-header>
      <el-main>
          <router-view/>
      </el-main>
      <el-footer>
          <Footer/>
      </el-footer>
    </el-container>
</template>

<script>
import Header from './Header.vue'
import Footer from './Footer.vue'

export default ({
    setup() {
        
    },
    components: { Header,Footer }
})
</script>

<style lang="scss" scoped>
/* .el-main {
    margin: 0px auto;
    margin-top: 40px;
    margin-bottom: 60px;
    z-index: 100;
    height: 100%;
    width: 1152px;
} */
.el-header {
    padding: 0 0;
    height: 100%;
}
.el-main {
    padding: 0 0;
}
.el-footer {
  background: linear-gradient(to bottom, #f5f5f5, #efefef);
  color: #666;
  text-align: center;
  line-height: 60px;
  padding: 0;
  border-top: 1px solid #e0e0e0;
  margin-top: 40px;
}
</style>