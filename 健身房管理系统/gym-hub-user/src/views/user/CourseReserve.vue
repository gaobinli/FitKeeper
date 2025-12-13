<template>
    <div class="container">
        <div class="header-con">
            <div class="center-wrap">
                <h1>我的预约</h1>
            </div>
        </div>
        <section class="reserve-section">
            <div class="center-wrap">
                <el-table :data="reserves" stripe>
                    <el-table-column prop="id" label="预约ID" min-width="100" />
                    <el-table-column label="课程封面" min-width="120">
                        <template #default="scope">
                            <img v-if="scope.row.coverUrl" :src="scope.row.coverUrl" class="course-cover" @click="goToCourseDetail(scope.row.courseId)" />
                            <span v-else>-</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="课程" min-width="150">
                        <template #default="scope">
                            <span class="course-link" @click="goToCourseDetail(scope.row.courseId)">{{ scope.row.courseName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="教练" min-width="120">
                        <template #default="scope">
                            <span>{{ scope.row.coachName || '-' }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="price" label="价格" min-width="100">
                        <template #default="scope">
                            <span>￥{{ scope.row.price }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="预约状态" min-width="120">
                        <template #default="scope">
                            <el-tag v-if="scope.row.auditStatus === 1" type="info">待审核</el-tag>
                            <el-tag v-else-if="scope.row.auditStatus === 2" type="danger">审核驳回</el-tag>
                            <el-tag v-else-if="scope.row.auditStatus === 3" type="success">审核通过</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="审核描述" min-width="150">
                        <template #default="scope">
                            <span>{{ scope.row.auditDesc || '-' }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="createTime" label="预约时间" min-width="180">
                        <template #default="scope">
                            <span>{{ scope.row.createTime ? scope.row.createTime.replace('T', ' ') : '' }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" min-width="120" fixed="right">
                        <template #default="scope">
                            <el-button v-if="scope.row.auditStatus === 3"
                                link
                                type="primary"
                                @click="handleAppraise(scope.row)">
                                {{ scope.row.hasAppraise ? '已评价' : '评价' }}
                            </el-button>
                            <span v-else style="color: #999;">-</span>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination
                    :page-sizes="[8, 10, 15, 20, 50]"
                    :page-size="dataTable.pageSize"
                    :current-page="dataTable.currentPage"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="dataTable.totalCount"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    style="margin-top: 20px; float: right;"
                />
            </div>
        </section>
    </div>

    <el-dialog v-model="appraiseDialogVisible" title="课程评价" width="60%" center @close="resetAppraiseForm">
        <div class="appraise-form">
            <div class="form-item form-item-inline">
                <label>课程评分：</label>
                <el-rate v-model="appraiseForm.courseAppraise" :max="5"></el-rate>
            </div>
            <div class="form-item">
                <label>评价内容：</label>
                <RichTextEditor v-model="appraiseForm.content" />
            </div>
        </div>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="appraiseDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="submitAppraise">提交评价</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script>
import { onMounted, ref, reactive, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { get, post } from '@/api/http'
import RichTextEditor from '@/components/RichTextEditor.vue'

export default {
    components: {
        RichTextEditor
    },
    setup() {
        const router = useRouter()
        let userId = localStorage.getItem('userId')
        let reserves = ref([])
        let dataTable = reactive({
            pageSize: 8,
            totalCount: 0,
            currentPage: 1,
        })


        function handleSizeChange(val) {
            dataTable.pageSize = val
            queryReserves()
        }

        function handleCurrentChange(val) {
            dataTable.currentPage = val
            queryReserves()
        }

        onMounted(() => {
            window.scrollTo(0, 0)
            if (!userId) {
                ElMessage({
                    message: '请先登录',
                    type: 'info',
                })
                return
            }
            queryReserves()
        })

        function queryReserves() {
            if (!userId) {
                return
            }
            let queryUrl = 'courseReserve?currentPage=' + dataTable.currentPage
                + '&pageSize=' + dataTable.pageSize
                + '&qp-userId-eq=' + userId

            get(queryUrl).then(res => {
                reserves.value = res.list || []
                dataTable.totalCount = res.totalCount || 0
                dataTable.pageSize = res.pageSize || 8
                dataTable.currentPage = res.currentPage || 1

                // 查询课程名称
                if (reserves.value && reserves.value.length > 0) {
                    queryCoursesInfo()
                }
            }).catch(() => {
                reserves.value = []
                dataTable.totalCount = 0
            })
        }

        function queryCoursesInfo() {
            get('course/list').then(res => {
                const courses = res || []
                if (courses.length > 0) {
                    reserves.value.forEach((reserve) => {
                        courses.forEach((course) => {
                            if (reserve.courseId === course.id) {
                                reserve.courseName = course.name
                                reserve.coachId = course.coachId
                                reserve.coverUrl = course.coverUrl
                            }
                        })
                    })
                }
                // 查询教练信息
                if (reserves.value && reserves.value.length > 0) {
                    queryCoachesInfo()
                }
            })
        }

        function queryCoachesInfo() {
            reserves.value.forEach((reserve) => {
                if (reserve.coachId) {
                    get('coach/' + reserve.coachId).then(res => {
                        reserve.coachName = res.name || '-'
                    }).catch(() => {
                        reserve.coachName = '-'
                    })
                } else {
                    reserve.coachName = '-'
                }
            })
            // 查询评价状态
            queryAppraiseStatus()
        }

        function goToCourseDetail(courseId) {
            router.push({
                path: '/course/detail/' + courseId
            })
        }

        let appraiseDialogVisible = ref(false)
        let currentReserve = ref(null)
        let appraiseForm = reactive({
            courseAppraise: 5,
            content: ''
        })

        function handleAppraise(reserve) {
            currentReserve.value = reserve
            // 检查是否已经评价过
            if (reserve.hasAppraise) {
                ElMessage({
                    message: '您已经评价过这个课程了',
                    type: 'info',
                })
                return
            }
            appraiseDialogVisible.value = true
            resetAppraiseForm()
        }

        function resetAppraiseForm() {
            appraiseForm.courseAppraise = 5
            appraiseForm.content = ''
        }

        function submitAppraise() {
            if (!appraiseForm.content.trim()) {
                ElMessage({
                    message: '请输入评价内容',
                    type: 'warning',
                })
                return
            }

            let appraiseData = {
                courseId: currentReserve.value.courseId,
                userId: parseInt(userId),
                courseAppraise: appraiseForm.courseAppraise,
                content: appraiseForm.content
            }

            post('courseAppraise', appraiseData).then(res => {
                ElMessage({
                    message: '评价提交成功',
                    type: 'success',
                })
                appraiseDialogVisible.value = false
                currentReserve.value.hasAppraise = true
                resetAppraiseForm()
            }).catch(err => {
                ElMessage({
                    message: '评价提交失败，请重试',
                    type: 'error',
                })
            })
        }

        function queryAppraiseStatus() {
            reserves.value.forEach((reserve) => {
                if (reserve.auditStatus === 3) {
                    get('courseAppraise?currentPage=1&pageSize=10000&qp-userId-eq=' + userId + '&qp-courseId-eq=' + reserve.courseId)
                        .then(res => {
                            reserve.hasAppraise = res.list && res.list.length > 0
                        })
                        .catch(() => {
                            reserve.hasAppraise = false
                        })
                }
            })
        }

        return {
            reserves,
            dataTable,
            handleSizeChange,
            handleCurrentChange,
            goToCourseDetail,
            appraiseDialogVisible,
            appraiseForm,
            handleAppraise,
            submitAppraise,
            resetAppraiseForm
        }
    }
}
</script>

<style lang="scss" scoped>
.container {
    min-height: 95vh;
    background-color: #f5f5f5;
}

.header-con {
    height: 36px;
    padding: 21px 0;
    background-color: white;
    border-bottom: 1px solid #e5e5e5;
}

.header-con .center-wrap {
    width: 1152px;
    margin: 0 auto;
}

.header-con h1 {
    font-size: 24px;
    color: var(--primaryColor);
    font-weight: bold;
    line-height: 36px;
    margin: 0;
}

.reserve-section {
    background-color: white;
    margin-top: 20px;
    padding: 20px 0;
}

.reserve-section .center-wrap {
    width: 1152px;
    margin: 0 auto;
    box-sizing: border-box;
    overflow: hidden;
}

.center-wrap {
    width: 1152px;
    margin: 0 auto;
}

:deep(.el-table) {
    width: 100%;
    box-sizing: border-box;
}

:deep(.el-table__header) {
    table-layout: auto;
}

:deep(.el-table__body) {
    table-layout: auto;
}

:deep(.el-table__row) {
    height: auto;
}

:deep(.el-table td) {
    padding: 12px 0;
}

.course-cover {
    width: 80px;
    height: 60px;
    object-fit: cover;
    border-radius: 4px;
    cursor: pointer;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.course-cover:hover {
    transform: scale(1.05);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.course-link {
    color: var(--primaryColor);
    cursor: pointer;
    transition: color 0.3s ease;
}

.course-link:hover {
    color: #4dbf99;
    text-decoration: underline;
}

.appraise-form {
    padding: 20px 0;
}

.form-item {
    margin-bottom: 24px;
}

.form-item-inline {
    display: flex;
    align-items: center;
    gap: 16px;
}

.form-item-inline label {
    margin-bottom: 0;
}

.form-item label {
    display: block;
    margin-bottom: 8px;
    font-weight: 500;
    color: #333;
    font-size: 14px;
}
</style>
