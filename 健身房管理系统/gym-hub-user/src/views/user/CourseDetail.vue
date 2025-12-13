<template>
    <div class="main-css">
        <div class="common-layout">
            <el-container>
                <el-aside class="el-aside-css"><img :src="course.coverUrl" class="image"/></el-aside>
                <el-main>
                    <el-form class="form-css" id="selectForm" label-width="auto">
                        <el-form-item label="课程名称：">{{course.name}}</el-form-item>
                        <el-form-item label="分类：">{{course.classificationName}}</el-form-item>
                        <el-form-item label="教练：">{{course.coachName}}</el-form-item>
                        <el-form-item label="价格：">￥{{course.price}}</el-form-item>
                        <el-form-item label="课程时间：">{{course.courseTime}}</el-form-item>
                        <el-form-item label="限制人数：">{{course.limitNumber}}</el-form-item>
                        <el-form-item label="简短介绍：">{{course.simpleDesc}}</el-form-item>
                        <el-form-item label="推荐指数：">{{course.recommendedScore || 0}}/5</el-form-item>
                        <el-form-item class="btn-css">
                            <el-button v-if="isReserved" type="success" size="large" disabled>已预约</el-button>
                            <el-button v-else type="primary" size="large" @click="showReserveDialog(course)">预约</el-button>
                        </el-form-item>
                    </el-form>
                </el-main>
            </el-container>
            <el-tabs
                v-model="activeName"
                style="margin-top:40px"
                type="card"
                class="demo-tabs"
                @tab-click="handleClick"
            >
                <el-tab-pane label="课程内容" name="content">
                    <span class="editor-css" v-html="course.content"></span>
                </el-tab-pane>
                <el-tab-pane label="课程评价" name="appraise">
                    <div class="list" v-for="appraise in appraises" :key="appraise.id">
                        <div class="item">
                            <img class="avatar" :src="appraise.avatarUrl"/>
                            <div class="info">
                                <p class="name">{{appraise.userName}}</p>
                                <p><span class="rate-css">评分：
                                    <el-rate
                                    style="line-height:2px"
                                    v-model="appraise.courseAppraise"
                                    disabled
                                >
                                </el-rate></span>
                                </p>
                                <span class="editor-css" v-html="appraise.content"></span>
                                <p class="time">评价时间：{{appraise.createTime.replace('T', ' ')}}</p>
                                <div v-if="appraise.replyContent" class="reply-box">
                                    <p class="reply-label">官方回复：</p>
                                    <span class="editor-css" v-html="appraise.replyContent"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </el-tab-pane>
            </el-tabs>

        </div>
    </div>
    <el-dialog v-model="reserveDialogVisible" title="预约确认" width="30%" center >
        <span>您好！是否确认预约本课程？课程价格为 ￥{{course.price}}</span>
        <template #footer>
        <span class="dialog-footer">
            <el-button @click="reserveDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="reserve">确认预约</el-button>
        </span>
        </template>
    </el-dialog>
</template>

<script>
import { onMounted, computed, getCurrentInstance} from 'vue'
import { useRouter } from "vue-router";
import { get,post,del,patch } from '@/api/http'
import { reactive, ref } from '@vue/reactivity'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useStore } from "vuex";
import { Check, Star } from '@element-plus/icons'

export default ({
    components: { Check, Star },
    setup() {
        const { proxy } = getCurrentInstance()
        const router = useRouter()
        let reserveDialogVisible = ref(false)
        let userId = localStorage.getItem('userId')
        let userPhone = localStorage.getItem('userCode')
        const store = useStore()
        let courseId = router.currentRoute.value.params.id
        let course = ref({})
        const appraises = ref([])
        const activeName = ref('content')
        let isReserved = ref(false)

        onMounted(() => {
            window.scrollTo(0, 0)
            queryCourseById(courseId)
            queryData()
            queryReserveStatus()
        })

        function queryData() {
            get('courseAppraise?currentPage=1'
            + '&pageSize=10000'
            + '&qp-courseId-eq=' + courseId
            ).then(res => {
                appraises.value = res.list || []
                if (appraises.value && appraises.value.length > 0) {
                    get('user/list').then(res => {
                        const userInfos = res || []
                        if (userInfos.length > 0) {
                            appraises.value.forEach((element1, index) => {
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

        function queryReserveStatus() {
            if (!userId) {
                isReserved.value = false
                return
            }
            get('courseReserve?currentPage=1&pageSize=10000&qp-userId-eq=' + userId + '&qp-courseId-eq=' + courseId)
                .then(res => {
                    isReserved.value = res.list && res.list.length > 0
                })
                .catch(() => {
                    isReserved.value = false
                })
        }

        function showReserveDialog(val) {
            let token = localStorage.getItem('token');
            if (token == null) {
                ElMessage({
                    message: '哎呀！你还没登录呢！',
                    type: 'info',
                })
                return;
            }
            reserveDialogVisible.value = true
        }

        function reserve() {
            if (!userId) {
                ElMessage({
                    message: '请先登录',
                    type: 'info',
                })
                return
            }

            // 先查询课程已预约成功人数
            get('courseReserve?currentPage=1&pageSize=10000&qp-courseId-eq=' + courseId + '&qp-auditStatus-eq=3')
                .then(res => {
                    let reservedCount = res.list ? res.list.length : 0

                    // 检查课程人数是否已满
                    if (reservedCount >= course.value.limitNumber) {
                        ElMessage({
                            message: '人数已满，不可预约',
                            type: 'warning',
                        })
                        return
                    }

                    // 查询用户最新信息检查余额
                    get('user/getUserInfo').then(userInfo => {
                        if (userInfo.balance < course.value.price) {
                            ElMessage({
                                message: '余额不足，请前往充值',
                                type: 'warning',
                            })
                            return
                        }

                        let reserveData = {
                            userId: parseInt(userId),
                            courseId: parseInt(courseId),
                            price: course.value.price,
                            auditStatus: 1
                        }
                        post('courseReserve', reserveData).then(res => {
                            // Update user balance after successful reservation
                            let newBalance = userInfo.balance - course.value.price
                            patch('user/' + userId, {
                                id: parseInt(userId),
                                balance: newBalance
                            }).then(res => {
                                ElMessage({
                                    message: '预约成功，请等待审核',
                                    type: 'success',
                                })
                                reserveDialogVisible.value = false
                                queryReserveStatus()
                            })
                        })
                    }).catch(() => {
                        ElMessage({
                            message: '获取用户信息失败',
                            type: 'error',
                        })
                    })
                }).catch(() => {
                    ElMessage({
                        message: '获取课程信息失败',
                        type: 'error',
                    })
                })
        }

        function queryCourseById(courseId) {
            get('course/' + courseId, '').then(res => {
                course.value = res
                queryClassificationAndCoachFunction()
            })
        }

        function queryClassificationAndCoachFunction() {
            // 获取分类信息
            get('classification/list').then(res => {
                course.value.classificationName = ''
                if (res && res.length > 0) {
                    for (const classification of res) {
                        if (classification.id === course.value.classificationId) {
                            course.value.classificationName = classification.name
                        }
                    }
                }
            })

            // 获取教练信息
            if (course.value.coachId) {
                get('coach/' + course.value.coachId).then(res => {
                    course.value.coachName = res.name || '暂无'
                })
            } else {
                course.value.coachName = '暂无'
            }
        }

        return {
            courseId,
            course,
            appraises,
            Check,
            Star,
            showReserveDialog,
            reserveDialogVisible,
            reserve,
            activeName,
            isReserved
        }
    },
})
</script>

<style scoped>
.main-css {
    width: 1152px;
    margin: 0 auto;
    box-sizing: border-box;
    overflow: hidden;
}
.main-css .common-layout {
    margin-top: 20px;
    width: 100%;
    box-sizing: border-box;
    overflow: hidden;
}

.main-css .common-layout >>> .el-container {
    width: 100%;
    box-sizing: border-box;
    overflow: hidden;
}

.main-css .common-layout >>> .el-main {
    width: auto;
    overflow: hidden;
    box-sizing: border-box;
}

.main-css .common-layout .el-aside-css {
    width: 500px;
    height: 500px;
    position: relative;
}
.main-css .common-layout .el-aside-css img {
    max-width: 100%;
    max-height: 100%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
}
.main-css .common-layout .form-css{
    padding-top: 63px;
    padding-left: 20px;
}

#selectForm >>> .el-form-item__label {
  font-size: 20px;
}
#selectForm >>> .el-form-item__content {
    font-size: 20px;
}
.btn-css {
    padding-top: 28px;
}
.editor-css {
    font-size: 17px;
    line-height: 1.8;
    padding-top: 30px;
    word-break: break-word;
    overflow-wrap: break-word;
    display: block;
    width: 100%;
    box-sizing: border-box;
    overflow: hidden;
}

.editor-css img {
    max-width: 100% !important;
    width: 100% !important;
    height: auto !important;
    display: block !important;
    margin: 20px 0 !important;
    border-radius: 4px;
}

.editor-css p {
    margin: 15px 0;
}

.editor-css a {
    color: var(--primaryColor);
    text-decoration: none;
}

.editor-css a:hover {
    text-decoration: underline;
}

.editor-css ul, .editor-css ol {
    margin: 15px 0;
    padding-left: 30px;
}

.editor-css li {
    margin: 8px 0;
}

.editor-css blockquote {
    border-left: 4px solid var(--primaryColor);
    padding-left: 15px;
    margin: 15px 0;
    color: #666;
    background-color: #f5f5f5;
    padding: 15px;
}

.editor-css code {
    background-color: #f5f5f5;
    padding: 2px 6px;
    border-radius: 3px;
    font-family: 'Courier New', monospace;
}

.editor-css pre {
    background-color: #f5f5f5;
    padding: 15px;
    border-radius: 4px;
    overflow-x: auto;
    margin: 15px 0;
}

.editor-css table {
    width: 100%;
    border-collapse: collapse;
    margin: 15px 0;
}

.editor-css table th,
.editor-css table td {
    border: 1px solid #ddd;
    padding: 12px;
    text-align: left;
}

.editor-css table th {
    background-color: #f5f5f5;
    font-weight: bold;
}

.demo-tabs {
    padding-bottom: 100px;
    width: 100%;
    box-sizing: border-box;
    overflow: hidden;
}

.demo-tabs >>> .el-tabs__content {
    overflow: hidden;
    width: 100%;
    box-sizing: border-box;
}

.demo-tabs >>> .el-tab-pane {
    overflow: hidden;
    width: 100%;
    box-sizing: border-box;
}

.user {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    cursor: pointer;
    vertical-align: middle;
}

.infinite-list {
  height: 800px;
  padding: 0;
  margin: 0;
  list-style: none;
}
.infinite-list .infinite-list-item {
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
}

.avatar {
    width: 48px;
    height: 48px;
    border-radius: 50%;
    overflow: hidden;
    margin-right: 20px;
}

.wrapper {
    min-width: 400px;
    max-width: 800px;
    display: flex;
    justify-content: flex-end;
}

.wrapper textarea {
    outline: none;
    border-color: transparent;
    resize: none;
    background: #f5f5f5;
    border-radius: 4px;
    flex: 1;
    padding: 10px;
    transition: all 0.5s;
    height: 30px;
}

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

.list {
    /* min-width: 400px;
    max-width: 800px; */
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

.reply-box {
    margin-top: 15px;
    padding: 12px;
    background-color: #f5f5f5;
    border-left: 3px solid var(--primaryColor);
    border-radius: 3px;
}

.reply-label {
    color: var(--primaryColor);
    font-size: 14px;
    font-weight: bold;
    margin: 0 0 8px 0;
}

.reply-box .editor-css {
    font-size: 14px;
    line-height: 1.6;
    padding-top: 0;
    color: #333;
}
</style>
