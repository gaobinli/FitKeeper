
<template>
    <div class="container-css">
        <div class="main-css">
            <el-table :data="dataTable.list">
                <el-table-column prop="subjectName" label="科目"/>
                <el-table-column prop="exercisesClassifyName" label="分类"/>
                <el-table-column prop="title" label="题目">
                    <template #default="scope">
                        <span v-html="scope.row.title"></span>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="练习时间" :formatter='handleTime'/>
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button type="text" @click="showDetail(scope.row.id)">查看详情</el-button>
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
        </div>
    </div>

    <el-dialog
        v-model="detailDialogVisible"
        title="错题详情"
        width="50%"
        :before-close="orderDetailHandleClose"
        center
    >
        <div class="question_body">
            <!-- <div v-for="(item, index) in practiceRecordDetails" :key="item.id"> -->
                <div class="question_title">
                    <span  style="float:left">1、</span><span v-html="practiceRecordDetail.title" style="float:left"></span>
                </div>

                <ul v-if="practiceRecordDetail.type === 1" class="question_options">
                    <li :class="{'cur': practiceRecordDetail.answer==='A', 'right': practiceRecordDetail.eAnswer==='1', 'wrong': practiceRecordDetail.answer==='A' && practiceRecordDetail.answer !== (practiceRecordDetail.eAnswer==='1'? 'A': '')}" @click="chooseAnswer('A')">
                        <span class="option-tag-css">A</span><span class="option-content-css">{{practiceRecordDetail.optionA}}</span>
                    </li>
                    <li :class="{'cur': practiceRecordDetail.answer==='B', 'right': practiceRecordDetail.eAnswer==='2', 'wrong': practiceRecordDetail.answer==='B' && practiceRecordDetail.answer !== (practiceRecordDetail.eAnswer==='2'? 'B': '')}" @click="chooseAnswer('B')">
                        <span class="option-tag-css">B</span><span class="option-content-css">{{practiceRecordDetail.optionB}}</span>
                    </li>
                    <li :class="{'cur': practiceRecordDetail.answer==='C', 'right': practiceRecordDetail.eAnswer==='3', 'wrong': practiceRecordDetail.answer==='C' && practiceRecordDetail.answer !== (practiceRecordDetail.eAnswer==='3'? 'C': '')}" @click="chooseAnswer('C')">
                        <span class="option-tag-css">C</span><span class="option-content-css">{{practiceRecordDetail.optionC}}</span>
                    </li>
                    <li :class="{'cur': practiceRecordDetail.answer==='D', 'right': practiceRecordDetail.eAnswer==='4', 'wrong': practiceRecordDetail.answer==='D' && practiceRecordDetail.answer !== (practiceRecordDetail.eAnswer==='4'? 'D': '')}" @click="chooseAnswer('D')">
                        <span class="option-tag-css">D</span><span class="option-content-css">{{practiceRecordDetail.optionD}}</span>
                    </li>
                </ul>
                <ul v-if="practiceRecordDetail.type === 2" class="question_options">
                    <li :class="{'cur': practiceRecordDetail.answer==='A', 'right': practiceRecordDetail.eAnswer==='1', 'wrong': practiceRecordDetail.answer==='A' && practiceRecordDetail.answer !== (practiceRecordDetail.eAnswer==='1'? 'A': '')}" @click="chooseAnswer('A')">
                        <span class="option-tag-css">A</span><span class="option-content-css">对</span>
                    </li>
                    <li :class="{'cur': practiceRecordDetail.answer==='B', 'right': practiceRecordDetail.eAnswer==='2', 'wrong': practiceRecordDetail.answer==='B' && practiceRecordDetail.answer !== (practiceRecordDetail.eAnswer==='2'? 'B': '')}" @click="chooseAnswer('B')">
                        <span class="option-tag-css">B</span><span class="option-content-css">错</span>
                    </li>
                </ul>
            </div>
            
            
        <!-- </div> -->

    </el-dialog>
    
</template>

<script>
import { reactive, ref } from '@vue/reactivity'
import { onMounted} from 'vue'
import { get,post,del,patch } from '@/api/http'
import { ElMessageBox, ElMessage } from 'element-plus'
import { fa, tr } from 'element-plus/es/locale'

export default({
    setup() {
        let userId = localStorage.getItem('userId');
        let updateDialogVisible = ref(false)
        let addDialogVisible = ref(false)
        let orderDetailDialogVisible = ref(false)
        let appraiseDialogVisible = ref(false)
        let dataFormRuleRef = ref(null)
        let appraiseDataFormRuleRef = ref(null)
        let tableSelectIds = ref([])
        let userPhone = localStorage.getItem('userCode');
        let sllerId = localStorage.getItem('userId');
        let dataTable = reactive({
            list: [],
            pageSize: 10,
            totalCount: null,
            currentPage: 1,
        })
        let params = reactive({
            userId: '',
            orderNo: '',
            sendTime: '',
            receiveTime: '',
        })
        let oneData = reactive({
            id: null,
            userId: null,
            orderNo: '',
            sendTime: '',
            receiveTime: '',
            orderType: 1
        })
        let appraiseData = reactive({
            skuId: null,
            userId: null,
            content: '',
            logisticeAppraise: null,
            skuAppraise: null,
            orderNo: null
        })
        let orderDetails = ref([])

        let appraiseRules = reactive({
            content: {required: true, message: '请填写评价内容', trigger: 'blur'},
            logisticeAppraise: {required: true, message: '请做出物流评价', trigger: 'blur'},
            skuAppraise: {required: true, message: '请做出课程评价', trigger: 'blur'},
        })

        function queryData() {
            if(!userId)  {
                return;
            }
            get('errorCollection?currentPage=' + dataTable.currentPage
            + '&pageSize=' + dataTable.pageSize
            + '&qp-userId-eq=' + userId
            ).then(res => {
                dataTable.list = res.list
                dataTable.totalCount = res.totalCount
                dataTable.pageSize = res.pageSize
                dataTable.currentPage = res.currentPage
            })
        }

        function handleTime(row, column, cellValue, index) {
            if(cellValue) {
                return cellValue.replace('T', ' ')
            }
        }
        function handleOrderStatus(row, column, cellValue, index) {
            if (cellValue === 1) {
                return '待出餐'
            }  else if (cellValue === 2) {
                return '已出餐'
            }  else if (cellValue === 3) {
                return '已取餐'
            }
            return '未知'
        }
        

        function handleSizeChange(val) {
            dataTable.pageSize = val
            queryData()
        }

        function handleCurrentChange(val) {
            dataTable.currentPage = val
            queryData()
        }

        function deleted(row) {
            ElMessageBox.confirm('请确认是否删除?','提示',{
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }).then(() => {
                del('order/' + row.id, {}).then(res => {
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
        const detailDialogVisible = ref(false)
        const practiceRecordDetail = ref([])
        function showDetail(id) {
            get("errorCollection/" + id, {}).then(res => {
                practiceRecordDetail.value = res
                detailDialogVisible.value = true
            })
        }

        function updated() {
            dataFormRuleRef.value.validate((valid) => {
                if (valid) {
                    patch('order/' + oneData.id, oneData).then(res => {
                    oneData.userId = null
                    oneData.orderNo = ''
                    oneData.sendTime = ''
                    oneData.receiveTime = ''
                    queryData()
                    updateDialogVisible.value = false
                    ElMessage({message: '亲，修改成功', type: 'success'})
            })
                } else {
                    return false
                }
            })
        }

        function receipt(id, status) {
            post('order/receipt?id=' + id, {'status': status}).then(res => {
                queryData()
                updateDialogVisible.value = false
                if (status == 3) {
                    ElMessage({message: '亲，取餐成功', type: 'success'})
                }
            })
        }

        function updateEnable(row) {
            patch('order/' + row.id, {enable: row.enable}).then(res => {
                ElMessage({message: '亲，修改成功', type: 'success'})
            })
        }

        function add() {
            dataFormRuleRef.value.validate((valid) => {
                if (valid) {
                    post('order', oneData).then(res => {
                    oneData.userId = null
                    oneData.orderNo = ''
                    oneData.sendTime = ''
                    oneData.receiveTime = ''
                    queryData()
                    addDialogVisible.value = false
                    ElMessage({message: '亲，新增成功', type: 'success'})
            })
                } else {
                    return false
                }
            })
        }

        function handleSelectionChange(val) {
            tableSelectIds.value = val;
        }

        function updateHandleClose() {
            updateDialogVisible.value = false
            oneData.id = null
            oneData.userId = null
            oneData.orderNo = ''
            oneData.sendTime = ''
            oneData.receiveTime = ''
        }

        function appraiseHandleClose() {
            appraiseDialogVisible.value = false
            orderDetailDialogVisible.value = true
            appraiseData.skuId = null
            appraiseData.userId = null
            appraiseData.content = ''
            appraiseData.logisticeAppraise = null
            appraiseData.skuAppraise = null
            appraiseData.orderNo = null
        }

        function addHandleClose() {
            addDialogVisible.value = false
            oneData.userId = null
            oneData.orderNo = ''
            oneData.sendTime = ''
            oneData.receiveTime = ''
        }

        function showOrderDetail(val) {
            const userIdLocal = localStorage.getItem('userId');
            get('orderDetail/list?orderNo=' + val.orderNo + "&userId=" + userIdLocal, '').then(res => {
                orderDetails.value = res
            })
            orderDetailDialogVisible.value = true
        }

        function appraiseDialog(val) {
            appraiseData.userId = localStorage.getItem('userId');
            appraiseData.skuId = val.skuId
            appraiseData.orderNo = val.orderNo
            appraiseDialogVisible.value = true
            orderDetailDialogVisible.value = false
        }

        function appraiseSubmit() {
            appraiseDataFormRuleRef.value.validate((valid) => {
                if (valid) {
                    const orderNoTemp = appraiseData.orderNo
                    post('skuAppraise', appraiseData).then(res => {
                    appraiseData.skuId = null
                    appraiseData.userId = null
                    appraiseData.content = ''
                    appraiseData.logisticeAppraise = null
                    appraiseData.skuAppraise = null
                    appraiseData.orderNo = null
                    ElMessage({message: '亲，评价成功', type: 'success'})
                    appraiseDialogVisible.value = false

                    const userIdLocal = localStorage.getItem('userId');
                    get('orderDetail/list?orderNo=' + orderNoTemp + "&userId=" + userIdLocal, '').then(res => {
                        orderDetails.value = res
                    })
                    orderDetailDialogVisible.value = true
            })
                } else {
                    return false
                }
            })
        }

        onMounted(() => {
            queryData()
        })

        function orderTypeChange(val) {
            dataTable.pageSize = 10
            dataTable.currentPage = 1
            queryData()
        }


        return {
            dataTable,
            params,
            updateDialogVisible,
            addDialogVisible,
            orderDetailDialogVisible,
            showOrderDetail,
            oneData,
            orderDetails,
            dataFormRuleRef,
            tableSelectIds,
            handleSelectionChange,
            add,
            addHandleClose,
            updateHandleClose,
            showDetail,
            updateEnable,
            deleted,
            updated,
            queryData,
            handleTime,
            handleSizeChange,
            handleCurrentChange,
            appraiseDialog,
            appraiseHandleClose,
            appraiseData,
            appraiseDialogVisible,
            appraiseRules,
            appraiseSubmit,
            appraiseDataFormRuleRef,
            orderTypeChange,
            handleOrderStatus,
            userPhone,
            sllerId,
            receipt,

            practiceRecordDetail,
            detailDialogVisible,
        }
    }
})
</script>

<style lang="scss" scoped>
.container-css {
    background-color: #F7F7F7;
    height: 95vh;
}

.main-css {
    width: 1152px;
    margin: 0 auto;
    padding-top: 50px;
}

.el-pagination {
    margin-top: 20px;
    float: right;
}

.login {
  display: flex;
  justify-content: center;
  align-items: center;
  background-size: cover;
  
}

.appraise-css {
    text-align: center;
    font-size: 20px;
}

.login-form {
  border-radius: 6px;
//   background: #f3efef;
  width: 100%;
//   padding: 5px 5px 5px 5px;
  
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  } 
}







html{ max-width:750px; margin:0 auto;}
body{ font-family:"PingFangSC-Regular","STHeitiSC-Light","微软雅黑","Microsoft YaHei","sans-serif"; font-size:28px; line-height:1.6; color: #666;
    -webkit-user-select:none; user-select:none;
    -webkit-touch-callout:none; touch-callout:none;
}
li{ list-style:none;}
[v-cloak]{ display: none;}
 
.index{ height: 100vh; display: flex; justify-content: center; align-items: center;}
.base_btn{ width: 420px; height: 80px; line-height: 80px; margin: 20px auto; text-align: center; color: #fff; border-radius: 40px; background-color: #FB3A1C;}
/* 答题页 */
.question_body{ padding: 0 40px;}
.question_number{ padding: 20px 0; text-align: center; font-size: 32px; font-weight: bold; position: relative;}
.question_title{ 
    min-height: 50px; 
    line-height: 44px;
    text-align: justify; 
    word-wrap: break-word;
    font-size: 24px;
}
.question_options{ 
    margin-top: 20px; 
}
.question_options li{ 
    line-height: 42px; 
    padding: 10px 20px 10px 60px; 
    position: relative; 
    cursor: pointer;
}
.question_options li:first-child ~ li{ margin-top: 10px;}
/* 选中样式 */
.question_options li.cur{ 
    background-color: rgba(50, 202, 153, 0.5);
    border-radius: 25px;
    border-width: 1px;
    border-color: rgba(50, 202, 153, 0.8);
}
.question_options li.cur .option-tag-css{ 
    color: white; 
    background-color: rgba(50, 202, 153, 1);
    font-weight: bold;}
.question_options li .option-tag-css { 
    width: 46px; 
    height: 46px; 
    line-height: 44px; 
    text-align: center; border-radius: 50%; 
    border: #666 solid 1px; position: absolute; 
    left: 0; top: 50%; margin-top: -23px;
    margin-left: 10px;
    font-size: 20px;
}
.question_options li .option-content-css { 
    margin-left: 15px;
    font-size: 20px;
}

/* 选中样式 */
.question_options li.cur span{ 
    // border: #c9240b solid 1px;
}
/* 正确样式 */
.question_options li.right .option-tag-css{ 
  font-size: 0; border-radius: 0; border: none; 
  background: url(../../assets/images/right.png) no-repeat center center;
  background-size: 100% 100%
  }
/* 错误样式 */
.question_options li.wrong .option-tag-css{ 
    font-size: 0; border-radius: 0; border: none; 
  background: url(../../assets/images/error.png) no-repeat center center;
  background-size: 100% 100%
}
/* 提示正确答案 */
.question_err{ margin: 20px 0; color: #E62225;}
.question_err span{ font-weight: bold;}
/* 计时显示 */
.question_time{ min-width: 170px; margin-top: 50px; height: 50px; line-height: 45px; text-align: center; color: #fff; font-size: 24px; font-weight: bold; border-radius: 25px 0 0 25px; background-color: #20BD9A; position: absolute; right: 0; top: 38px;}
.question_time span{ margin: 0 5px; font-size: 30px;}
/* 动画展示设置 */
.question.showanimation .question_body{
  -webkit-animation: flipToMax .6s ease-out both;
          animation: flipToMax .6s ease-out both;
}
.question.showanimation .question_number{
  -webkit-animation: flipToMax .6s .2s ease-out both;
          animation: flipToMax .6s .2s ease-out both;
}
.question.showanimation .question_title{
  -webkit-animation: fadeToTop .6s .2s ease-out both;
          animation: fadeToTop .6s .2s ease-out both;
}
.question.showanimation .question_options li:nth-child(1){
  -webkit-animation: fadeInMinToMax 1s .4s ease-out both;
          animation: fadeInMinToMax 1s .4s ease-out both;
}
.question.showanimation .question_options li:nth-child(2){
  -webkit-animation: fadeInMinToMax 1s .6s ease-out both;
          animation: fadeInMinToMax 1s .6s ease-out both;
}
.question.showanimation .question_options li:nth-child(3){
  -webkit-animation: fadeInMinToMax 1s .8s ease-out both;
          animation: fadeInMinToMax 1s .8s ease-out both;
}
.question.showanimation .question_options li:nth-child(4){
  -webkit-animation: fadeInMinToMax 1s 1s ease-out both;
          animation: fadeInMinToMax 1s 1s ease-out both;
}
.question.showanimation .base_btn{
  -webkit-animation: flipToMax .8s 1s ease-out both;
          animation: flipToMax .8s 1s ease-out both;
}
@-webkit-keyframes flipToMax{
  0%{ -webkit-transform:scale(0) scaleX(-1); opacity:0;}
  100%{ -webkit-transform:scale(1) scaleX(1); opacity:1;}
}
@keyframes flipToMax{
  0%{ transform:scale(0) scaleX(-1); opacity:0;}
  100%{ transform:scale(1) scaleX(1); opacity:1;}
}
@-webkit-keyframes fadeToTop{
  0%{ -webkit-transform:translateY(100%); opacity:0;}
  100%{ -webkit-transform:translateY(0); opacity:1;}
}
@keyframes fadeToTop{
  0%{ transform:translateY(100%); opacity:0;}
  100%{ transform:translateY(0); opacity:1;}
}
@-webkit-keyframes fadeInMinToMax{
  0%{ -webkit-transform:scale(0); opacity:0;}
  100%{ -webkit-transform:scale(1); opacity:1;}
}
@keyframes fadeInMinToMax{
  0%{ transform:scale(0); opacity:0;}
  100%{ transform:scale(1); opacity:1;}
}
/* 答题结束结果页 */
.pop{ width: 100%; height: 100%; background-color: rgba(0,0,0,.6); position: fixed; left: 0; top: 0;
  -webkit-animation: fadeIn .5s linear both;
      animation: fadeIn .5s linear both;
}
.pop_box{ width: 100%; height: 100%; padding-bottom: 80px;
  display: flex; justify-content: center; align-items: center;
}
.pop_body{ text-align: center; color: #fff; position: relative;}
.pop_close{ width: 54px; position: absolute; left: 50%; margin-left: -27px; bottom: -85px;}
</style>












