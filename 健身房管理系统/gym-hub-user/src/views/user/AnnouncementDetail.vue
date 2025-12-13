<template>
    <div class="main">
        <div class="content">
            <h1>{{announcement.title}}</h1>
            <div class="editor-css" v-html="announcement.content"></div>
        </div>
    </div>
</template>

<script>
import { onMounted, computed} from 'vue'
import { useRouter } from "vue-router";
import { get,post,del,patch } from '@/api/http'
import { reactive, ref } from '@vue/reactivity'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useStore } from "vuex";

export default ({
    setup() {
        const router = useRouter()
        const store = useStore()
        let routerId = router.currentRoute.value.params.id
        let announcement = ref({})

        onMounted(() => {
            get('announcement/' + routerId, '').then(res => {
                announcement.value = res
            })
        })

        return {
            routerId,
            announcement,
        }
    },
})
</script>

<style scoped>
.main {
    background-color: #F7F7F7;
}
.main .content {
    width: 1152px;
    margin: 0 auto;
    padding-top: 50px;
    padding-bottom: 50px;
}
.main h1 {
    font-size: 50px;
    text-align: center;
}
h2 h3 h4 h5 {
  font-size: 30px;
}
.main .editor-css {
    /* font-size: 30px; */
    font-size: 20px;
    line-height: 50px;
    padding-top: 30px;
   
}
.editor-css >>> img {
  margin: 0 auto;
  width: 1152px !important;
}
.editor-css >>> h1 {
  font-size: 30px;
}
.editor-css >>> h2 {
  font-size: 27px;
}
.editor-css >>> h3 {
  font-size: 25px;
}
.editor-css >>> h4 {
  font-size: 30px;
}
.editor-css >>> h5 {
  font-size: 30px;
}
.editor-css >>> h6 {
  font-size: 30px;
}

.title-class {
    margin-top: -15px;
    color: rgb(219, 108, 108);
}
.item-class {
    text-align: left;
    color: rgb(145, 85, 170);
}
#selectForm >>> .el-form-item__label {
  font-size: 20px;
}
#selectForm >>> .el-form-item__content {
    font-size: 20px;
}
#selectForm >>> .el-form-item__content {
    margin-bottom: 18px;
}
/* .image {
    height: 400px;
    width: 100%;
} */

.el-header,
.el-footer {
  background-color: #b3c0d1;
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 60px;
}

/* .el-aside {
  background-color: #d3dce6;
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 200px;
} */

.el-main {
  /* background-color: #e9eef3; */
  color: var(--el-text-color-primary);
  text-align: center;
  /* line-height: 160px; */
  font-size: 22px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}


.user {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    cursor: pointer;
    vertical-align: middle;
}

.spe-class {
    overflow: hidden;			/* 文字长度超出限定宽度，则隐藏超出的内容 */
  	text-overflow: ellipsis;
  	display: -webkit-box;     	/* 将对象作为弹性伸缩盒子模型显示 */      
  	-webkit-line-clamp: 1;    	/* 控制最多显示几行，这里采用最多显示两行 */      
  	-webkit-box-orient: vertical; 
}

.li {
    
}

.infinite-list {
  height: 800px;
  padding: 0;
  margin: 0;
  list-style: none;
}
/* .infinite-list .infinite-list-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  background: var(--el-color-primary-light-9);
  margin: 10px;
  color: var(--el-color-primary);
}
.infinite-list .infinite-list-item + .list-item {
  margin-top: 10px;
} */
</style>
