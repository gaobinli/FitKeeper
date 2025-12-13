<template>
    <div class="container-css">
        <div class="main-css">
            <el-table :data="tableData" @selection-change="handleSelectionChange">
                <el-table-column type="selection" />
                <el-table-column prop="url" label="美食封面">
                    <template #default="scope" >
                        <div class="img-con">
                            <img :src="scope.row.url"/>    
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="美食名称"/>
                <el-table-column prop="simpleDesc" label="美食介绍"/>
                <el-table-column prop="price" label="价格" width="100px"/>
                <el-table-column prop="" label="数量">
                    <template #default="scope">
                        <el-input-number v-model="scope.row.number" :min="1" @change="handleChange" />
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button type="warning" @click="del(scope.row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="jiesuan">
                <div class="settle-price" v-if="countPrize" >价格：{{countPrize}}</div>
                <el-button class="settle-btn" type="text" @click="settle">结算</el-button>
            </div>
        </div>
    </div>

    <el-dialog v-model="payDialogVisible" title="购买提示" width="30%" center>
        <span>您好！本次订单价格为 {{countPrize}} ，购买请点击支付</span>
        <template #footer>
        <span class="dialog-footer">
            <el-button @click="payDialogVisible = false">取消支付</el-button>
            <el-button type="primary" @click="pay">支付</el-button>
        </span>
        </template>
    </el-dialog>
</template>

<script>
import { onMounted } from "vue"
import { reactive, ref } from '@vue/reactivity'
import { ElMessage, ElMessageBox } from 'element-plus'
import { get,post,del,patch } from '@/api/http'
import { useStore } from "vuex";
import { useRouter } from "vue-router";

export default ({
    setup() {
        const router = useRouter()
        const store = useStore()
        let tableData = ref([])
        let countPrize = ref(0)
        let skus = ref([])
        let payDialogVisible = ref(false)
        let order = ref({
            userId: null,
            orderDetails: []
        })

        let orderDetail = reactive({
            skuId: null,
            number: null,
            price: null
        })

        onMounted(() => {
            tableData.value = JSON.parse(localStorage.getItem('shoppingcar'))
        })

        function handleChange() {
            localStorage.setItem('shoppingcar', JSON.stringify(tableData.value))
            let val = []
            countPrize.value = 0
            tableData.value.forEach((item) => {
                skus.value.forEach((item2)=> {
                    if (item.id === item2.id) {
                        countPrize.value = countPrize.value + item.price * item.number
                        val.push(item)
                    }
                })
            })
            skus.value = val
        }

        function handleSelectionChange(val) {
            countPrize.value = 0
            val.forEach((item) => {
                countPrize.value = countPrize.value + item.price * item.number
            })
            skus.value = val
        }

        function del(val) {
            let storage = JSON.parse(localStorage.getItem('shoppingcar'))
            let result = storage.findIndex(item => item.id == val)
            storage.splice(result, 1)
            localStorage.setItem('shoppingcar', JSON.stringify(storage))
            tableData.value = storage
            store.commit('decrCarNumber')
        }

        function settle() {
            let token = localStorage.getItem('token');
            if (token == null || token.length === 0) {
                ElMessage({
                    message: '哎呀！你还没登录呢！',
                    type: 'info',
                })
                return;
            }


            if(skus.value.length > 0) {
                payDialogVisible.value = true
            } else if(skus.value.length == 0 && tableData.value.length > 0) {
                ElMessage({
                    message: '哎呀！你还没选择要结算的美食呢！',
                    type: 'info',
                })
            } else {
                ElMessage({
                    message: '亲，购物车空空如也呢！',
                    type: 'info',
                })
            }
        }

        function pay() {
            // Query latest user info to check balance
            get('user/getUserInfo').then(userInfo => {
                if (userInfo.balance < countPrize.value) {
                    ElMessage({
                        message: '余额不足，请前往充值',
                        type: 'warning',
                    })
                    return
                }

                order.value.userId = localStorage.getItem('userId');
                let tempArr = [];
                skus.value.forEach((item) => {
                    orderDetail = {}
                    orderDetail.number = item.number
                    orderDetail.skuId = item.id
                    orderDetail.price = item.price
                    tempArr.push(orderDetail)
                })
                order.value.orderDetails = tempArr;
                post('order', order.value).then(res => {
                    // Update user balance after successful payment
                    let newBalance = userInfo.balance - countPrize.value
                    let userId = localStorage.getItem('userId')
                    patch('user/' + userId, {
                        id: parseInt(userId),
                        balance: newBalance
                    }).then(res => {
                        for (let i = tableData.value.length - 1; i >= 0 ; i--){
                            let result = skus.value.findIndex(item => item.id == tableData.value[i].id)
                            if(result != -1) {
                                tableData.value.splice(i, 1)
                            }
                        }
                        localStorage.setItem('shoppingcar', JSON.stringify(tableData.value))
                        ElMessage({
                            message: '亲！恭喜您支付成功',
                            type: 'success',
                        })
                        payDialogVisible.value = false
                        order.value.userId = '';
                        order.value.orderDetails = [];
                    })
                })
            }).catch(() => {
                ElMessage({
                    message: '获取用户信息失败',
                    type: 'error',
                })
            })
        }

        return{
            tableData,
            countPrize,
            pay,
            settle,
            payDialogVisible,
            handleChange,
            del,
            handleSelectionChange
        }
    },
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
.main-css .img-con {
    width: 128px;
    height: 128px;
    position: relative;
}
.main-css img {
    width: 100%;
    height: 100%;
    object-fit: contain;
}
.jiesuan {
    float: right;
    display: block;
    margin-top: 20px;
}
.jiesuan .settle-price {
    float: left;
    font-size: 30px;
    line-height: 34px;
    padding-right: 20px;
    color: #e73b3b;
}
.jiesuan .settle-btn {
    float: right;
    font-size: 30px;
    line-height: 30px;
}
</style>
