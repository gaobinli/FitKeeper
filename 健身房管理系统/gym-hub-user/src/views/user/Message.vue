<template>
    <div class="main">
        <div class="header-con">
            <div class="center-wrap">
                <h1>健身资讯查询</h1>
                <div class="soso-box">
                    <input type="text" v-model="announcementTitle" placeholder="请输入要查询的健身资讯">
                    <a href="#" @click="queryAnnouncement()" class="btn iconfont">&#xe62d;</a>
                </div>
            </div>
        </div>
        <div class="data">
            <ul class="list" v-for="item in announcementList" :key="item.id">
                <li>
                    <router-link :to="{path:'/announcement/detail/' + item.id}" class="link-css" :title="item.name">
                        <span class="left">{{item.title}}</span> 
                        <span class="right">{{splitTime(item.createTime)}}</span>
                    </router-link>
                </li>
                <el-divider />
            </ul>
        </div>
        <el-pagination
        :page-sizes= '[10, 15, 20, 50]'
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
import { onMounted} from 'vue'
import { get,post,del,patch } from '@/api/http'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

export default {
    setup() {
        const router = useRouter()
        let userId = localStorage.getItem('userId');
        let dataTable = reactive({
            list: [],
            pageSize: 10,
            totalCount: null,
            currentPage: 1,
        })

        function handleSizeChange(val) {
            dataTable.pageSize = val
            queryAnnouncement()
        }

        function handleCurrentChange(val) {
            dataTable.currentPage = val
            queryAnnouncement()
        }

        onMounted(() => {
            queryAnnouncement()
        })
        let announcementList = ref([])
        const announcementTitle = ref('')
        function queryAnnouncement() {
            get('announcement?currentPage=' + dataTable.currentPage
                + '&pageSize=' + dataTable.pageSize
                + '&qp-title-like=' + announcementTitle.value
                + '&qp-type-eq=' + 1
                ).then(res => {
                    announcementList.value = res.list
                    dataTable.totalCount = res.totalCount
                    dataTable.pageSize = res.pageSize
                    dataTable.currentPage = res.currentPage
            })
        }
        function splitTime(val) {
        if (val.includes('T')) {
                return val.split("T")[0]
            }
            return val
        }

        return {
            dataTable,
            handleSizeChange,
            handleCurrentChange,
            userId,
            queryAnnouncement,
            announcementList,
            announcementTitle,
            splitTime
        }
    }
    
}
</script>

<style lang="scss" scoped>
.data {
    margin-top: 20px;
}
.data ul {
    list-style: none;
}
.data ul li {
    height: 18px;
}
.list {
    text-align: left;
}
.list li {
    width: 100%;
}
.list li span.left {
    font-size: 18px;
    line-height: 18px;
}
.list li span.left:hover {
    color: var(--primaryColor);
}
.list li span.right {
    float: right;
    font-size: 18px;
    line-height: 18px;
}
.list li span.right:hover {
    color: var(--primaryCurColor);
}
.list li a:hover {
    color: var(--primaryCurColor);
}




.main {
    width: 1152px;
    margin: 0 auto;
}

.el-pagination {
    float: right;
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