<template>
    <div class="skus-main">
        <div class="header-con">
            <div class="center-wrap">
                <h1>{{$projectName}}</h1>
                <div class="soso-box">
                    <input type="text" v-model="skuName" placeholder="查询课程课程">
                    <a href="#" @click="querySkus()" class="btn iconfont">&#xe62d;</a>
                </div>
            </div>
        </div>
        <section class="xxsw content-part common-style">
            <div class="center-wrap">
                <el-tabs v-model="categoryId" class="demo-tabs" @tab-click="handleClick">
                    <el-tab-pane label="全部" name=""></el-tab-pane>
                    <el-tab-pane v-for="item in categoryList" :key="item.id" :label="item.name" :name="item.id"></el-tab-pane>
                </el-tabs>
                <ul>
                    <template v-for="sku in skus" :key="sku.id">
                        <li class="pro-grid">
                            <router-link :to="{path:'/sku/detail/' + sku.id}">
                                <div class="picbox">
                                    <img :src="sku.url" alt="">
                                </div>
                                <div class="wordbox">
                                    <p class="proname">{{sku.name}}</p>
                                    <p>
                                        <span class="score">￥{{sku.price}}</span>
                                        <span class="comment"> &nbsp;&nbsp;{{Math.floor(Math.random() * (9999 - 99 + 1)) + 98}} 人购买过</span>
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
        let categoryId =  new ref('')
        let tasteId =  new ref()
        let cuisineId =  new ref()
        let activityTopicId =  new ref()
        let skuName =  new ref('')
        categoryId.value = query.value.categoryId ? Number(query.value.categoryId) : '';
        skuName.value = query.value.searchName ? query.value.searchName : '';
        let dataTable = reactive({
            list: [],
            pageSize: 8,
            totalCount: null,
            currentPage: 1,
        })

        function handleSizeChange(val) {
            dataTable.pageSize = val
            querySkus()
        }

        function handleCurrentChange(val) {
            dataTable.currentPage = val
            querySkus()
        }

        onMounted(() => {
            querySkus()
            queryCategoryList()
        })
        let skus = ref([])
        function querySkus() {
            get('sku?currentPage=' + dataTable.currentPage
                + '&pageSize=' + dataTable.pageSize
                + '&qp-categoryId-eq=' + (categoryId.value ? categoryId.value : '')
                + '&name=' + skuName.value
                ).then(res => {
                    skus.value = res.list
                    dataTable.totalCount = res.totalCount
                    dataTable.pageSize = res.pageSize
                    dataTable.currentPage = res.currentPage
            })
        }
        
        function handleClick(tab) {
            dataTable.currentPage = 1
            dataTable.list = []
            categoryId.value = tab.props.name
            querySkus()
        }

        let categoryList = ref([])
        function queryCategoryList() {
            get('category/list').then(res => {
                categoryList.value = res
            })
        }

        return {
            dataTable,
            handleSizeChange,
            handleCurrentChange,
            userId,
            skus,
            handleClick,
            categoryId,
            querySkus,
            skuName,
            categoryList,
            categoryId,
        }
    }
    
}
</script>

<style lang="scss" scoped>
.skus-main {
    height: 95vh;
    // background-color: #F7F7F7;
    width: 1152px;
    margin: 0 auto;
}
.select-input {
    width: 160px;
    padding-left: 20px;
    padding-top: 3px;
}
.xxsw {
    // padding-top: 40px;
    height: 698px;
    // background-color: #F7F7F7;
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
.xxsw ul li {
    float: left;
    width: 264px;
    height: 270px;
    margin-right: 32px;
    background: white;
    margin-bottom: 32px;
}
.xxsw ul li:nth-child(4n) {
    margin-right: 0;
}
.xxsw ul li .wordbox {
    padding: 15px;
    color: #1C1F21;
    font-size: 14px;
}
.xxsw ul li:hover {
    box-shadow: 0 8px 16px 0 rgba(0,0,0,0.20);
}

.common-style {
    height: 698px;
    // padding-top: 40px;
}
.common-style h2 {
    float: left;
}
.common-style em {
    float: left;
}
.common-style .bd {
    padding-top: 32px;
}
.common-style ul>li {
    float: left;
    margin-right: 32px;
    margin-bottom: 32px;
    box-shadow: 0 4px 8px 0 rgba(0,0,0,0.08);
    background-color: white;
}
.common-style ul>li:hover {
    box-shadow: 0 8px 16px 0 rgba(0,0,0,0.20);
}
.common-style .bd>ul>li:nth-child(3), .common-style .bd>ul>li:nth-child(7) {
    margin-right: 0;
}
.common-style .bd>ul>li.big-grid {
    width: 560px;
    height: 270px;
    position: relative;
}
.common-style .bd>ul>li.big-grid .info {
    position: absolute;
    width: 560px;
    height: 103px;
    color: white;
    bottom: 0;
    left: 0;
    background-color: rgba(0, 0, 0, .55);
    box-sizing: border-box;
    padding: 30px 26px;
    line-height: 26px;
}
.common-style .bd>ul>li .tag {
    position: absolute;
    width: 74px;
    height: 40px;
    color: white;
    background-color: rgba(0, 0, 0, .55);
    bottom: 103px;
    left: 0;
    text-align: center;
    line-height: 40px;
    /* 图片一触碰，就会有transform变形，那么此时会神奇的盖住tag，所以为了防止盖住，我们应该设置 z-index */
    z-index: 9999;
}
.common-style .bd>ul>li.pro-grid {
    width: 264px;
    height: 270px;
} 
.common-style .bd>ul>li.pro-grid .picbox {  
    position: relative;
    transform: scale();
    height: 184px;
}
.common-style .bd>ul>li.pro-grid .picbox img {  
    width: 264px;
    height: 184px;
}
.common-style .bd>ul>li.pro-grid .picbox .tag {
    bottom: 0;
}
.common-style .bd>ul>li.pro-grid .wordbox {
    padding: 14px;
    font-size: 15px;
    color: #1C1F21;
    line-height: 26px;
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
/* 公共的一个特效，鼠标触碰图片，能够有放大效果 */
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
.content-part .grid {
    overflow: hidden;
}
.content-part li .picbox {
    height: 184px;
}

.el-pagination {
    float: right;
}

.xxsw ul li .wordbox {
    padding: 16px 9px 0;
}
.xxsw ul li .wordbox .proname {
    font-weight: bold;
    font-size: 16px;
    color: #000000;
    height: 30px;
    overflow: hidden;
}
.xxsw ul li .wordbox .score {
    font-size: 15px;
    color: #FA6400;
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
    width: 200px;
    height: 28px;
    border: 1px solid var(--primaryColor);
    border-radius: 4px 0 0 4px;
    font-size: 14px;
    /* 取消默认的外线（外线是文本框特有的东西） */
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
    line-height: 28px;
    font-size: 18px;
    border-radius: 0 4px 4px 0;
}
</style>