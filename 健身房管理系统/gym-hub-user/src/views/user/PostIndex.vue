<template>
    <div class="contaner">
        <div class="main-css">
            <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
                <el-tab-pane label="最新发布">
                    <el-button v-if="userId" type="primary" @click="addPost()">发布内容</el-button>
                    <el-table :data="dataTable.list" height="550" style="width: 100%;">
                        <el-table-column prop="title" label="主题" >
                            <template #default="scope">
                                <el-col :span="4"><div class="grid-content bg-purple" style="width: 250px"><router-link  :to="{path:'/postDetail/' + scope.row.id}">{{scope.row.title}}</router-link></div></el-col>
                            </template>
                        </el-table-column>
                        <el-table-column prop="createUser" label="发布人"/>
                        <el-table-column prop="createTime" label="发布时间" :formatter='handleTime' />
                        <el-table-column label="操作" width="270px">
                            <template #default="scope">
                                <el-button v-if="scope.row.userId == userId" type="success" @click="updated(scope.row)">修改</el-button>
                                <el-button v-if="scope.row.userId == userId" type="danger" icon="i-delete" @click="deleted(scope.row)"></el-button>
                            </template>
                        </el-table-column>
                    </el-table>
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
                </el-tab-pane>
                <el-tab-pane label="热门">
                    <el-table :data="dataTable2.list" height="550" style="width: 100%;">
                        <el-table-column prop="title" label="主题" >
                            <template #default="scope">
                                <el-col :span="4"><div class="grid-content bg-purple" style="width: 500px"><router-link :to="{path:'/postDetail/' + scope.row.id}">{{scope.row.title}}</router-link></div></el-col>
                            </template>
                        </el-table-column>
                        <el-table-column prop="createUser" label="发布人" />
                        <el-table-column prop="createTime" label="创建时间" :formatter='handleTime' />
                    </el-table>
                    <el-pagination
                    :page-sizes= '[10, 15, 20, 50]'
                    :page-size="dataTable2.pageSize"
                    :current-page="dataTable2.currentPage"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="dataTable2.totalCount"
                    @size-change="handleSizeChange2"
                    @current-change="handleCurrentChange2"
                    >
                    </el-pagination>
                </el-tab-pane>
            </el-tabs>
        </div>
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

        let dataTable2 = reactive({
            list: [],
            pageSize: 10,
            totalCount: null,
            currentPage: 1,
        })

        function handleSizeChange2(val) {
            dataTable2.pageSize = val
            queryData2()
        }

        function handleCurrentChange2(val) {
            dataTable2.currentPage = val
            queryData2()
        }

        function queryData2() {
            get('post?currentPage=' + dataTable2.currentPage
            + '&pageSize=' + dataTable2.pageSize
            + '&qp-excellent-eq=' + 1
            ).then(res => {
                dataTable2.list = res.list
                dataTable2.totalCount = res.totalCount
                dataTable2.pageSize = res.pageSize
                dataTable2.currentPage = res.currentPage
            })
        }

        function queryData() {
            get('post?currentPage=' + dataTable.currentPage
            + '&pageSize=' + dataTable.pageSize
            ).then(res => {
                dataTable.list = res.list
                dataTable.totalCount = res.totalCount
                dataTable.pageSize = res.pageSize
                dataTable.currentPage = res.currentPage
            })
        }

        function handleSizeChange(val) {
            dataTable.pageSize = val
            queryData()
        }

        function handleCurrentChange(val) {
            dataTable.currentPage = val
            queryData()
        }

        function addPost() {
            router.push({path:'/postAdd'});
        }

        function updated(val) {
            router.push({path:'/postUpdate/' + val.id});
        }

        function deleted(row) {
            ElMessageBox.confirm('请确认是否删除?','提示',{
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }).then(() => {
                del('post/' + row.id, {}).then(res => {
                    queryData()
                    ElMessage({
                        message: '亲，删除成功',
                        type: 'success',
                    })
                })
            })
            .catch(() => {
                ElMessage({
                    message: '亲，你取消了删除操作',
                    type: 'info',
                })
            });
        }

        onMounted(() => {
            queryData()
            queryData2()
        })
        function handleTime(row, column, cellValue, index) {
            if(cellValue) {
                return cellValue.replace('T', ' ')
            }
        }
        return {
            dataTable,
            dataTable2,
            deleted,
            addPost,
            handleSizeChange,
            handleSizeChange2,
            handleCurrentChange,
            handleCurrentChange2,
            userId,
            updated,
            handleTime
        }
    }
    
}
</script>

<style lang="scss" scoped>
.contaner {
    background-color: #F7F7F7;
    height: 95vh;
}
.main-css {
    margin: 0 auto;
    padding: 50px;
    width: 1152px;
}

.el-pagination {
    margin-top: 20px;
    float: right;
}
.search {
  margin-top: 20px;
  margin-bottom: 30px;
  text-align: center;
}
.search_input {
  width: 500px;
  padding-right: 10px;
}

/*包含以下五种的链接*/
a {
    text-decoration: none;
    color: rgba(12, 11, 11, 0.658);
}
/*正常的未被访问过的链接*/
a:link {
    text-decoration: none;
}
/*已经访问过的链接*/
a:visited {
    text-decoration: none;
}
/*鼠标划过(停留)的链接*/
a:hover {
    text-decoration: none;
}
/* 正在点击的链接，鼠标在元素上按下还没有松开*/
a:active {
    text-decoration: none;
}
/* 获得焦点的时候 鼠标松开时显示的颜色*/
a:focus {
    text-decoration: none;
}
</style>