
<template>
    <div class="container-css">
        <div class="main-css">
            <el-table :data="dataTable.list">
                <el-table-column prop="orderNo" label="订单编号" />
                <el-table-column prop="orderPrice" label="订单价格" width="100px"/>
                <el-table-column prop="status" label="订单状态" :formatter='handleOrderStatus' width="100px"/>
                <el-table-column prop="createTime" label="购买时间" :formatter='handleTime' width="200px"/>
                <el-table-column prop="sendTime" label="出餐时间" :formatter='handleTime' width="200px"/>
                <el-table-column prop="receiveTime" label="取餐时间" :formatter='handleTime' width="200px"/>
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button type="text" @click="showOrderDetail(scope.row)">订单详情</el-button>
                        <el-button v-if="scope.row.status === 2" type="text" @click="receipt(scope.row.id, 3)">取餐</el-button>
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
        v-model="orderDetailDialogVisible"
        title="订单详情"
        width="50%"
        :before-close="orderDetailHandleClose"
        center
    >
        <el-form :inline="false" :model="oneData" ref="dataFormRuleRef" :rules="addRules" class="demo-form-inline">
            <el-table :data="orderDetails" height="450" style="width: 100%;">
                <el-table-column prop="orderNo" label="订单编号" />
                <el-table-column prop="skuName" label="美食名称">
                    <template #default="scope">
                        <router-link style="color: #236ad7" :to="{path:'/sku/detail/' + scope.row.skuId}">{{scope.row.skuName}}</router-link>
                    </template>    
                </el-table-column>
                <el-table-column prop="img" label="图片">
                    <template #default="scope">
                        <img :src="scope.row.skuImg" style="widht:148px; height:148px" />
                    </template>    
                </el-table-column>
                <el-table-column prop="number" label="数量" />
                <el-table-column prop="price" label="单价"/>
                <el-table-column label="操作">
                    
                <template #default="scope">
                    <span v-if="!scope.row.appraiseFlag &&scope.row.status === 3">
                        <el-button type="text" @click="appraiseDialog(scope.row)">评价</el-button>
                    </span>
                    <span v-if="scope.row.appraiseFlag">
                        <el-button disabled="true" type="text">已评价</el-button>
                    </span>
                </template>
                </el-table-column>
            </el-table>
            <el-form-item style="padding-left: 90%;margin-top:40px">
                <el-button type="primary" @click="orderDetailDialogVisible = false" >确定</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>

    <el-dialog
        v-model="appraiseDialogVisible"
        title="评价"
        width="25%"
        :before-close="appraiseHandleClose"
    >
            <el-form :model="appraiseData" label-width="100px" class="appraise-css" :size="large" :rules="appraiseRules" ref="appraiseDataFormRuleRef">
                <el-form-item label="美食评分：" prop="skuAppraise">
                    <el-rate
                        style="line-height:2px"
                        v-model="appraiseData.skuAppraise"
                        :texts="['非常差', '差', '一般', '好', '非常好']"
                        show-text
                    >
                    </el-rate>
                </el-form-item>
                <el-form-item label="评价内容：" prop="content">
                    <el-input v-model="appraiseData.content" placeholder="请输入评价内容"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button style="margin-right: 0px" type="primary" @click="appraiseSubmit">确定</el-button>
                </el-form-item>
            </el-form>
    </el-dialog>
    
</template>

<script>
import { reactive, ref } from '@vue/reactivity'
import { onMounted} from 'vue'
import { get,post,del,patch } from '@/api/http'
import { ElMessageBox, ElMessage } from 'element-plus'

export default({
    setup() {
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
            experienceAppraise: null,
            skuAppraise: null,
            orderNo: null
        })
        let orderDetails = ref([])

        let appraiseRules = reactive({
            content: {required: true, message: '请填写评价内容', trigger: 'blur'},
            experienceAppraise: {required: true, message: '请做出体验评分', trigger: 'blur'},
            skuAppraise: {required: true, message: '请做出美食评价', trigger: 'blur'},
        })

        function queryData() {
            let userId = localStorage.getItem('userId');
            if(!userId)  {
                return;
            }
            
            get('order?currentPage=' + dataTable.currentPage
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

        function showDetail(id) {
            get("order/" + id, {}).then(res => {
                oneData.id = res.id
                oneData.userId = res.userId
                oneData.orderNo = res.orderNo
                oneData.sendTime = res.sendTime
                oneData.receiveTime = res.receiveTime
                updateDialogVisible.value = true
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
            appraiseData.experienceAppraise = null
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
                    appraiseData.experienceAppraise = null
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
</style>












