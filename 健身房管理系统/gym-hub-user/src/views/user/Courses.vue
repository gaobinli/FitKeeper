<template>
    <div class="courses-main">
        <div class="header-con">
            <div class="center-wrap">
                <h1>{{$projectName}}</h1>
                <div class="soso-box">
                    <input type="text" v-model="courseName" placeholder="查询课程" @keyup.enter="queryCourses()">
                    <a href="#" @click="queryCourses()" class="btn iconfont">&#xe62d;</a>
                </div>
            </div>
        </div>
        <section class="xxsw content-part common-style">
            <div class="center-wrap">
                <el-tabs v-model="classificationId" class="demo-tabs" @tab-click="handleClick">
                    <el-tab-pane label="全部" name=""></el-tab-pane>
                    <el-tab-pane v-for="item in classificationList" :key="item.id" :label="item.name" :name="item.id"></el-tab-pane>
                </el-tabs>
                <ul>
                    <template v-for="course in courses" :key="course.id">
                        <li class="pro-grid">
                            <router-link :to="{path:'/course/detail/' + course.id}">
                                <div class="picbox">
                                    <img :src="course.coverUrl" alt="">
                                </div>
                                <div class="wordbox">
                                    <p class="proname">{{course.name}}</p>
                                    <p class="course-price-score">
                                        <span class="price-text">￥{{course.price}}</span>
                                        <span class="score-text">推荐指数: {{course.recommendedScore || 0}}/5</span>
                                    </p>
                                </div>
                            </router-link>
                        </li>
                    </template>
                </ul>
            </div>
        </section>
        <el-divider />
        <el-pagination
            :page-sizes= '[8, 10, 15, 20, 50]'
            :page-size="dataTable.pageSize"
            :current-page="dataTable.currentPage"
            layout="total, sizes, prev, pager, next, jumper"
            :total="dataTable.totalCount"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            >
        </el-pagination>
    </div>
</template>

<script>
import { reactive, ref } from '@vue/reactivity'
import { onMounted, toRefs} from 'vue'
import { get,post,del,patch } from '@/api/http'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useRouter, useRoute  } from 'vue-router'

export default {
    setup() {
        const router = useRouter()
        const route = useRoute();
        let {query} = toRefs(route)
        let userId = localStorage.getItem('userId');
        let classificationId =  new ref('')
        let courseName =  new ref('')
        classificationId.value = query.value.classificationId ? Number(query.value.classificationId) : '';
        courseName.value = query.value.searchName ? query.value.searchName : '';
        let dataTable = reactive({
            list: [],
            pageSize: 8,
            totalCount: null,
            currentPage: 1,
        })

        function handleSizeChange(val) {
            dataTable.pageSize = val
            queryCourses()
        }

        function handleCurrentChange(val) {
            dataTable.currentPage = val
            queryCourses()
        }

        onMounted(() => {
            queryCourses()
            queryClassificationList()
        })
        let courses = ref([])
        function queryCourses() {
            let queryUrl = 'course?currentPage=' + dataTable.currentPage
                + '&pageSize=' + dataTable.pageSize
                + '&qp-auditStatus-eq=3'

            if (classificationId.value) {
                queryUrl += '&qp-classificationId-eq=' + classificationId.value
            }
            if (courseName.value) {
                queryUrl += '&qp-name-like=' + courseName.value
            }

            get(queryUrl).then(res => {
                    courses.value = res.list
                    dataTable.totalCount = res.totalCount
                    dataTable.pageSize = res.pageSize
                    dataTable.currentPage = res.currentPage
            })
        }

        function handleClick(tab) {
            dataTable.currentPage = 1
            dataTable.list = []
            classificationId.value = tab.props.name
            queryCourses()
        }

        let classificationList = ref([])
        function queryClassificationList() {
            get('classification/list').then(res => {
                classificationList.value = res
            })
        }

        return {
            dataTable,
            handleSizeChange,
            handleCurrentChange,
            userId,
            courses,
            handleClick,
            classificationId,
            queryCourses,
            courseName,
            classificationList,
            classificationId,
        }
    }

}
</script>

<style lang="scss" scoped>
.courses-main {
    height: 95vh;
    width: 1152px;
    margin: 0 auto;
}
.select-input {
    width: 160px;
    padding-left: 20px;
    padding-top: 3px;
}
.xxsw {
    height: 698px;
}
.xxsw .hd {
    margin-bottom: 32px;
}
.content-part h2 {
    font-size: 36px;
    font-weight: bold;
    color: #1C1F21;
}
.xxsw .hd h2 {
    float: left;
}
.content-part em {
    position: relative;
    top: 20px;
    left: 10px;
    color: #1C1F21;
    float: left;
}

.content-part .hd nav {
    float: right;
}
.content-part .hd nav ul {
    padding-top: 16px;
}
.content-part .hd nav ul li {
    float: left;
    margin-right: 16px;
    font-size: 14px;
}
.content-part .hd nav ul li:last-child {
    margin-right: 0;
}
.content-part .hd nav ul li a {
    color: rgba(0,0,0,0.85)
}
.content-part .hd nav ul li.current {
    border-bottom: 4px solid var(--primaryColor);
    padding-bottom: 4px;
}
.content-part .hd nav ul li.current a {
    color: var(--primaryColor);
}

.content-part img {
    transition: transform .4s ease 0s;
    width: 100%;
    height: 100%;
}
.content-part img:hover {
    transform: scale(1.1);
}
.content-part li {
    overflow: hidden;
}

.xxsw .bd {
    padding-top: 32px;
}
.xxsw .bd>ul>li {
    float: left;
    margin-right: 32px;
    margin-bottom: 32px;
    box-shadow: 0 4px 8px 0 rgba(0,0,0,0.08);
    background-color: white;
}
.xxsw .bd>ul>li:hover {
    box-shadow: 0 8px 16px 0 rgba(0,0,0,0.20);
}
.xxsw .bd>ul>li:nth-child(4n) {
    margin-right: 0;
}
.xxsw .bd>ul>li.pro-grid {
    width: 264px;
    height: 270px;
}
.xxsw .bd>ul>li.pro-grid .picbox {
    height: 184px;
}
.xxsw .bd>ul>li.pro-grid .picbox img {
    display: block;
    margin: 0 auto;
}
.xxsw .bd>ul>li .wordbox {
    padding: 14px;
    font-size: 15px;
    color: #1C1F21;
    line-height: 26px;
}
.xxsw .bd>ul>li .wordbox .proname {
    font-weight: bold;
    font-size: 16px;
    color: #000000;
    height: 30px;
    overflow: hidden;
}
.xxsw .bd>ul>li .wordbox .score {
    font-size: 15px;
    color: #FA6400;
}

.course-price-score {
    display: flex !important;
    justify-content: space-between !important;
    align-items: center !important;
    margin-top: 8px !important;
    padding: 0 !important;
    line-height: normal !important;
}
.course-price-score .price-text {
    font-size: 15px !important;
    color: #FA6400 !important;
    font-weight: bold;
    white-space: nowrap;
}
.course-price-score .score-text {
    font-size: 13px !important;
    color: #666 !important;
    white-space: nowrap;
}

.header-con {
    height: 36px;
    padding: 21px 0;
}
.header-con h1 {
    font-size: 24px;
    color: var(--primaryColor);
    float: left;
    font-weight: bold;
    line-height: 36px;
}
.header-con .soso-box {
    float: left;
    margin-left: 20px;
    width: 405px;
    padding-top: 4px;
}
.header-con .soso-box input {
    width: 261px;
    height: 28px;
    border: 1px solid var(--primaryColor);
    border-radius: 4px 0 0 4px;
    font-size: 14px;
    outline: none;
    padding-left: 10px;
    float: left;
}
.header-con .soso-box .btn {
    float: left;
    width: 32px;
    height: 28px;
    background-color: var(--primaryColor);
    color: #fff;
    text-align: center;
    line-height: 30px;
    font-size: 18px;
    border-radius: 0 4px 4px 0;
}
</style>
