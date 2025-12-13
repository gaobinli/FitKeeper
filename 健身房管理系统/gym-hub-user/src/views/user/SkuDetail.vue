<template>
    <div class="main-css">
        <div class="common-layout">
            <el-container>
                <el-aside class="el-aside-css"><img :src="sku.url" class="image"/></el-aside>
                <el-main>
                    <el-form class="form-css" id="selectForm" label-width="auto">
                        <el-form-item label="美食名称：">{{sku.name}}</el-form-item>
                        <el-form-item label="分类：">{{sku.categoryName}}</el-form-item>
                        <el-form-item label="价格：">￥{{sku.price}}</el-form-item>
                        <el-form-item label="剩余量：">{{sku.inventory}}</el-form-item>
                        <el-form-item label="简短介绍：">{{sku.simpleDesc}}</el-form-item>
                        <el-form-item class="btn-css">
                            <el-button type="primary" size="large" @click="showPayDialog(sku)"> ￥{{sku.price}} </el-button>
                            <el-button size="large" @click="addCart(sku)">加入购物车</el-button>
                        </el-form-item>
                        <!-- <el-form-item class="btn-css">
                            <el-button type="primary" size="large" @click="showReviewDialog(sku)">评论</el-button>
                            <el-button size="large" @click="addCart(sku)">点赞</el-button>
                            <el-button size="large" @click="addCart(sku)">收藏</el-button>
                        </el-form-item> -->
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
                <el-tab-pane label="美食介绍">
                    <span class="editor-css" v-html="sku.detail"></span>
                </el-tab-pane>
                <el-tab-pane label="美食评价">
                    <div class="list" v-for="appraise in appraises" :key="appraise">
                        <div class="item">
                            <!-- <i class="avatar"></i> -->
                            <img class="avatar" :src="appraise.avatarUrl"/>
                            <div class="info">
                                <p class="name">{{appraise.userName}}</p>
                               
                                <p><span class="rate-css">美食评分：
                                    <el-rate
                                        style="line-height:2px"
                                        v-model="appraise.skuAppraise"
                                        disabled
                                    >
                                    </el-rate></span></p>
                                <span class="editor-css" v-html="appraise.content"></span>
                                <p class="time">评价时间：{{appraise.createTime.replace('T', ' ')}}</p>
                            </div>
                        </div>
                    </div>
                </el-tab-pane>
            </el-tabs>

        </div>
    </div>
     <el-dialog v-model="payDialogVisible" title="购买提示" width="30%" center >
        <span>您好！本次订单价格为 {{orderDetail.price}} ，购买请点击支付</span>
        <template #footer>
        <span class="dialog-footer">
            <el-button @click="payDialogVisible = false">取消支付</el-button>
            <el-button type="primary" @click="pay">支付</el-button>
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
import E from 'wangeditor'

export default ({
    components: { Check, Star },
    setup() {
        const { proxy } = getCurrentInstance()
        const router = useRouter()
        let payDialogVisible = ref(false)
        let userId = localStorage.getItem('userId')
        let userPhone = localStorage.getItem('userCode')
        const store = useStore()
        let reviewDialogVisible = ref(false)
        let skuId = router.currentRoute.value.params.id
        let sku = ref({})
        const appraises = ref([])
        const userBuyFlag = ref(false)
      
        let dataTable = reactive({
            list: [],
            pageSize: 10,
            totalCount: null,
            currentPage: 1,
        })
        let skuData = reactive({
            skuId: router.currentRoute.value.params.id,
            userId: localStorage.getItem('userId'),
            content: '',
            skuAppraise: '',
        })
        let order = ref({
            userId: null,
            orderDetails: []
        })
        let orderDetail = reactive({
            skuId: null,
            number: null,
            price: null
        })
        let params = reactive({
            skuId: router.currentRoute.value.params.id,
            userId: null,
            content: '',
            logisticeAppraise: '',
            skuAppraise: '',
        })
         

        const editorContainer3 = ref(null);  
        const editor3 = ref(null);  
        onMounted(() => {
            querySkuById(skuId)
            queryData()
        })

        function appraiseSubmit(val) {
            let token = localStorage.getItem('token');
            if (token == null || token.length === 0) {
                ElMessage({
                    message: '哎呀！你还没登录呢！',
                    type: 'info',
                })
                return;
            }
            
            skuData.content = editor3.value.txt.html();
            if (!skuData.content || !skuData.skuAppraise) {
                ElMessage({
                    message: '请填写评论在提交哦！',
                    type: 'info',
                })
                return
            }
            post('skuAppraise', skuData).then(res => {
                skuData.skuAppraise = ''
                editor3.value.txt.html('')
                skuData.content= ''
                queryData()
            })
        }

        function queryData() {
            // get('skuAppraise/list?skuId=' + skuId
            // ).then(res => {
            //     appraises.value = res
            // })

            get('skuAppraise?currentPage=1'
            + '&pageSize=10000'
            + '&qp-skuId-eq=' + skuId
            ).then(res => {
                appraises.value = res.list
                if (appraises.value) {
                    get('user/list').then(res => {
                        const userInfos = res
                        if (userInfos) {
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

        function favoritesFunction(val) {
            const tmpToken = localStorage.getItem('token')
            if (!tmpToken) {
                ElMessage({
                    message: '亲！请先登录',
                    type: 'infos',
                })
                return 
            }
            const userId = localStorage.getItem('userId')
            post('favorites/operation', {'foodId': sku.value.id, 'userId': userId, 'type': val}
            ).then(res => {
                appraises.value = res
                querySkuById(skuId)
            })
        }
        
        function addCart(sku) {
            let shoppingCars = JSON.parse(localStorage.getItem('shoppingcar'));
            if(shoppingCars === null) {
                shoppingCars = []
            }
            let result = shoppingCars.findIndex(item => item.id == sku.id)
            if(result === -1) {
                sku.number = 1
                shoppingCars.push(sku)
                localStorage.setItem('shoppingcar', JSON.stringify(shoppingCars))
                store.commit('addCarNumber')
                ElMessage({
                    message: '亲，成功添加购物车',
                    type: 'success',
                })
            } else {
                ElMessage({
                    message: '亲，我已经躺在购物车里了',
                    type: 'info',
                })
            }
        }

        function showPayDialog(val) {
            let token = localStorage.getItem('token');
            if (token == null) {
                ElMessage({
                    message: '哎呀！你还没登录呢！',
                    type: 'info',
                })
                return;
            }
            payDialogVisible.value =true
            orderDetail.number = 1
            orderDetail.skuId = val.id
            orderDetail.price = val.price
            orderDetail.userId = localStorage.getItem('userId')
            
        }

        function pay() {
            // Query latest user info to check balance
            get('user/getUserInfo').then(userInfo => {
                if (userInfo.balance < orderDetail.price) {
                    ElMessage({
                        message: '余额不足，请前往充值',
                        type: 'warning',
                    })
                    return
                }

                order.value.userId = localStorage.getItem('userId');
                order.value.orderDetails.push(orderDetail);
                post('order', order.value).then(res => {
                    // Update user balance after successful payment
                    let newBalance = userInfo.balance - orderDetail.price
                    patch('user/' + userId, {
                        id: parseInt(userId),
                        balance: newBalance
                    }).then(res => {
                        ElMessage({
                            message: '亲！恭喜您支付成功',
                            type: 'success',
                        })
                        payDialogVisible.value = false
                    })
                })
                order.value.id = ''
                order.value.orderDetails = []
                querySkuById(skuId)
            }).catch(() => {
                ElMessage({
                    message: '获取用户信息失败',
                    type: 'error',
                })
            })
        }

        function querySkuById(skuId) {
            get('sku/' + skuId + "?userId=" + (userId == null ? '' : userId), '').then(res => {
                sku.value = res

                userBuyFlag.value = res.purchased
                if (userId) {
                    get('order?qp-userId-eq=' + userId).then(res => {
                        if (res.list && res.list.length > 0) {
                            for (let value of res.list) {
                                console.log(value);
                                get('orderDetail?qp-skuId-eq=' + skuId + "&qp-orderNo-eq=" + value.orderNo).then(res => {
                                    if (res.list && res.list.length > 0) {
                                        userBuyFlag.value = true
                                        
                                    }
                                })
                            }
                        }
                    })
                    
                }
                
                queryCategoryFunction()
            })
        }

        function queryCategoryFunction(val) {
            get('category/list').then(res => {
                sku.value.categoryName = ''
                if (res && res.length > 0) {
                    for (const category of res) {
                        if (category.id === sku.value.categoryId) {
                            sku.value.categoryName = category.name
                        }
                    }
                }
            })
        }

        return {
            favoritesFunction,
            skuId,
            sku,
            dataTable,
            appraises,
            Check,
            Star,
            skuData,
            editorContainer3,
            editor3,
            appraiseSubmit,
            addCart,
            showPayDialog,
            payDialogVisible,
            pay,
            order,
            orderDetail,
            userBuyFlag,
        }
    },
})
</script>

<style scoped>
.main-css {
    width: 1152px;
    margin: 0 auto;
}
.main-css .common-layout {
    margin-top: 20px;
}

.main-css .common-layout .el-aside-css {
    width: 500px;
    height: 500px;
    /* border: 2px solid red; */
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
    line-height: 50px;
    padding-top: 30px;
}
.demo-tabs {
    padding-bottom: 100px;
}
/* .title-class {
    margin-top: -15px;
    color: rgb(219, 108, 108);
}
.item-class {
    text-align: left;
    color: rgb(145, 85, 170);
}
#selectForm >>> .el-form-item__label {
  font-size: 20px;
}
#selectForm >>> .el-form-item__content {
    font-size: 20px;
}
#selectForm >>> .el-form-item__content {
    margin-bottom: 18px;
}
.image {
    height: 400px;
    width: 100%;
}

.el-header,
.el-footer {
  background-color: #b3c0d1;
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #d3dce6;
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 200px;
}

.el-main {
  color: var(--el-text-color-primary);
  text-align: center;
  font-size: 22px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

*/
.user {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    cursor: pointer;
    vertical-align: middle;
}

.spe-class {
    overflow: hidden;			
  	text-overflow: ellipsis;
  	display: -webkit-box;     
  	-webkit-line-clamp: 1;    
  	-webkit-box-orient: vertical; 
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
.apprise-css {
    font-size: 17px;
    height: 110px;
}
.apprise-css-left {
    float: left;
    padding-left: 14px;
}
.apprise-css-right {
    padding-left: 60px;
    float: left;
}

.element.style {
    margin-top: 0px;
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
