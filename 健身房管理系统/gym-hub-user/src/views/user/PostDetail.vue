<template>
    <div class="main-css">
        <h1>{{oneData.title}}</h1>
            <div class="detail-css">
            <span class="editor-css" v-html="oneData.content"></span>
            </div>
            <el-divider/>
            <el-button type="primary" @click="addComment" >评论</el-button>
            <div class="list" v-for="comment in comments" :key="comment.id">
                <div class="item">
                    <img class="avatar" :src="comment.avatarUrl ? comment.avatarUrl : 'https://m.media-amazon.com/images/S/amazon-avatars-global/default.png'" alt="用户头像">
                    <div class="info">
                        <p class="name">{{comment.createUser}}</p>
                        <span class="editor-css" v-html="comment.commentContent"></span>
                        <p class="time">{{comment.createTime.replace('T', ' ')}}</p>
                    </div>
                </div>
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

            <el-dialog
                    v-model="addDialogVisible"
                    title="新增评论"
                    width="40%"
                    :before-close="addHandleClose"
                    center
                >
                    
                    <el-form :inline="false" :model="commentOneData" ref="dataFormRuleRef" :rules="customizeRules">
                        <el-form-item label="评论：" prop="commentContent">
                            <v-wangeditor
                                :options="options"
                                @change="change"
                                style="width: 100%; margin: 0 0"
                            />
                        </el-form-item>
                    </el-form>
                    <template #footer>
                        <span class="dialog-footer" style="text-align:center">
                            <el-button @click="addHandleClose">取消</el-button>
                            <el-button type="primary" @click="add" >确定</el-button>
                        </span>
                    </template>
                </el-dialog>    
    </div>
</template>

<script>
import { reactive, ref, onMounted, onBeforeUnmount,getCurrentInstance } from 'vue'
import { get,post,del,patch } from '@/api/http'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

export default {
    setup() {
        const router = useRouter()
        let addDialogVisible = ref(false)
        let userId = localStorage.getItem('userId');
        let postId = router.currentRoute.value.params.id
        let oneData = ref({})
        let dataTable = reactive({
            list: [],
            pageSize: 10,
            totalCount: null,
            currentPage: 1,
        })
        let commentOneData = reactive({
            postId: null,
            sort: null,
            commentContent: '',
            userId: null,
        })

        const { proxy } = getCurrentInstance()
        let dataFormRuleRef = ref(null)

        let customizeRules = reactive({
            postId: {required: true, message: '请输入帖子id', trigger: 'blur'},
            sort: {required: true, message: '请输入排序', trigger: 'blur'},
            commentContent: {required: true, message: '请输入评论内容', trigger: 'blur'},
            commentUserId: {required: true, message: '请输入评论用户id', trigger: 'blur'},
            userId: {required: true, message: '请输入回复评论用户', trigger: 'blur'},
        })

        const options = reactive({
            height: 400,
            zIndex: 1,
            pasteIgnoreImg: false,
            uploadImgServer: proxy.$imgUpload,
            uploadFileName: 'file',
            uploadImgHooks: {
                customInsert: function (insertLinkImg, result, editor) {
                    console.log('customInsert')
                    console.log('result：', result)
                    // 图片上传并返回结果，自定义插入图片的事件，而不是编辑器自动插入图片
                    insertLinkImg(result.data)
                },
            },
            uploadVideoServer: proxy.$imgUpload,
            uploadVideoName: 'file',
            uploadVideoHooks: {
                customInsert: function(insertVideoFn, result) {
                    // result 即服务端返回的接口
                    console.log('customInsert', result)
                    // insertVideoFn 可把视频插入到编辑器，传入视频 src ，执行函数即可
                    insertVideoFn(result.data)
                }
            },
        })

        function change({ html }) {
            console.log(html)
            commentOneData.commentContent = html
        }

        const comments = ref([])
        function queryData() {
            get('postComment?currentPage=1'
            + '&pageSize=10000'
            + '&qp-postId-eq=' + postId
            ).then(res => {
                comments.value = res.list
                if (comments.value) {
                    get('user/list').then(res => {
                        const userInfos = res
                        if (userInfos) {
                            comments.value.forEach((element1, index) => {
                                 userInfos.forEach((element2, index) => {
                                    if (element1.userId === element2.id) {
                                        element1.userName = element2.name
                                        element1.avatarUrl = element2.avatarUrl
                                    }
                                });
                            });
                        }
                    })
                }
            })
        }

        function add() {
            dataFormRuleRef.value.validate((valid) => {
                if (valid) {
                    commentOneData.auditStatus = 0
                    commentOneData.postId = postId
                    commentOneData.commentUserId = userId
                    post('postComment', commentOneData).then(res => {
                        queryData()
                        ElMessage({message: '亲，评论成功', type: 'success'})
                        location.reload();
                    })
                } else {
                    return false
                }
            })
        }

        function addHandleClose() {
            commentOneData.commentContent = ''
            addDialogVisible.value = false
            commentOneData.userId = null
            commentOneData.sort = null
        }

        function reply(val) {
            addDialogVisible.value = true
            commentOneData.sort = val.sort
        }

        function addComment() {
            let userToken = localStorage.getItem('token');
            let userId = localStorage.getItem('userId');
            if (userToken == null) {
                ElMessage({
                    message: '哎呀！你还没登录呢！',
                    type: 'info',
                })
                return;
            }
            addDialogVisible.value = true
            commentOneData.userId = userId
        }

        onMounted(() => {
             get('post/'+ postId).then(res => {
                oneData.value = res
            })
            queryData()
        })

        return {
            oneData,
            queryData,
            dataTable,
            add,
            commentOneData,
            customizeRules,
            dataFormRuleRef,
            options,
            change,
            addDialogVisible,
            addHandleClose,
            reply,
            addComment,
            comments
        }
    }
    
}
</script>

<style lang="scss" scoped>
.main-css {
    margin: 0 auto;
    width: 1152px;
    margin-top: 50px;
    padding-bottom: 100px;
}
.main-css h1 {
    font-size: 50px;
    text-align: center;
}
.main-css .editor-css {
    /* font-size: 30px; */
    font-size: 20px;
    line-height: 50px;
    padding-top: 30px;
}
.el-pagination {
    margin-top: 20px;
    float: right;
    margin-bottom: 100px;
}
h1 {
//   text-align: center;
  font-weight:normal;
  font-size: 35px;
  /* color: #030303b4; */
  letter-spacing: 1px;
  padding-bottom: 20px;
}

// .detail-css {
//     padding-left: 300px;
//     padding-right: 200px;
// }

.infinite-list {
  height: 800px;
  padding: 0;
  margin: 0;
  list-style: none;
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











.wrapper {
            min-width: 400px;
            max-width: 800px;
            display: flex;
            justify-content: flex-end;
        }
 
        .avatar {
            width: 48px;
            height: 48px;
            border-radius: 50%;
            overflow: hidden;
            /* 不允许超出大盒子 */
            /* background: url(./images/avatar.jpg) no-repeat center / cover; */
            
            /* 大部分情况下，图片样式都是这样设置的 */
            margin-right: 20px;
        }
 
        /* 
        outline：outline（轮廓）是绘制于元素周围的一条线，位于边框边缘的外围，可起到突出元素的作用。
        注释：轮廓线不会占据空间，也不一定是矩形。
    */
 
        /* 
        transition：设置过度属性
        与.wrapper textarea:focus {
            border-color: #e4e4e4;
            background: #fff;
            height: 50px;
        }配合使用，表示光标在文本域中时，所有样式（属性值all）在2s内向
            border-color:#e4e4e4;
            background: #fff;
            height: 50px;
        平滑过渡
    */
 
        .wrapper textarea {
            outline: none;
            /* 这行代码可省略 */
 
            border-color: transparent;
            /* 边框颜色为透明 */
 
            resize: none;
            /* 用户无法自行调元素的尺寸，常用于文本域 */
 
            background: #f5f5f5;
            border-radius: 4px;
            flex: 1;
            padding: 10px;
 
            /* 这行代码与下面.wrapper textarea:focus的那几行代码配合，可以让相关样式在获取焦点事件的时候平滑过渡 */
            transition: all 0.5s;
 
            height: 30px;
        }
 
        /*用法类似于 选择器:hover */
        .wrapper textarea:focus {
            border-color: #e4e4e4;
            background: #fff;
            height: 50px;
        }
 
        .wrapper button {
            background: #00aeec;
            color: #fff;
            border: none;
            border-radius: 4px;
            margin-left: 10px;
            width: 70px;
            cursor: pointer;
        }
 
        .wrapper .total {
            margin-right: 80px;
            color: #999;
            margin-top: 5px;
            opacity: 0;
            /* 
                opacity设置透明度，0是完全透明，1是完全不透明
                opacity从0变成1时，元素会平滑显现
            */
            transition: all 0.5s;
        }
 
        .list {
            /* min-width: 400px;
            max-width: 800px; */
            margin-top: 15px;
        }
 
        .list .item {
            width: 100%;
            display: flex;
        }
 
        .list .item .info {
            flex: 1;
            border-bottom: 1px dashed #e4e4e4;
            padding-bottom: 10px;
        }
 
        .list .item p {
            margin: 0;
        }
 
        .list .item .name {
            color: #FB7299;
            font-size: 17px;
            font-weight: bold;
            line-height: 2em;
        }

        .list .item .rate-css {
            color: #828893;
            font-size: 17px;
            font-weight: bold;
            line-height: 2em;
        }
        
 
        .list .item .text {
            color: #333;
            padding: 10px 0;
        }
 
        .list .item .time {
            color: #999;
            padding-top: 15px;
            font-size: 16px;
        }

        .editor-css {
            font-size: 20px;
    line-height: 50px;
    padding-top: 30px;
        }
</style>