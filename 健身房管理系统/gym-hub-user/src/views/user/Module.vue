<template>
    <div class="news-and-notice">
        <div class="news">
            <h3>健身资讯</h3>
            <div class="news-content">
                <div class="hot-news">
                    <a href="#">
                        <img src="../../assets/images/adLeft.png" alt="">
                        <div class="info">
                            健身资讯，开启课程之旅
                        </div>
                    </a>
                </div>
                <ul>
                    <li v-for="item in announcementList1" :key="item.id">
                        <router-link :to="{path:'/announcement/detail/' + item.id}" :title="item.name">{{item.title}}</router-link>
                        <span>{{splitTime(item.createTime)}}</span>
                    </li>
                </ul>
            </div>
        </div>
        <div class="notice">
            <h3>系统公告</h3>
            <ol>
                <li  v-for="(item, index) in announcementList2" :key="item.id">
                    <div class="num">{{++index}}</div>
                    <dl>
                        <dt>
                            <router-link :to="{path:'/announcement/detail/' + item.id}" :title="item.name">{{item.title}}</router-link>
                        </dt>
                    </dl>
                </li>
            </ol>
        </div> 
    </div>

    <br><br><br>
    <!-- <aside class="ad-images">
        <a href="">
            <img src="../../assets/images/ad.png" alt="">
        </a>
    </aside> -->

    <section class="zsjsdlx content-part common-style" v-for="item in classifications" :key="item.id">
        <div class="center-wrap">
            <div class="hd clearfix">
                <h2>{{item.name}}</h2>
                <!-- <em class="gee-flag-css">{{geeFlag[Math.floor(Math.random() * geeFlag.length)].name}}</em> -->
                <nav>
                    <ul>
                        <li class="current"><a href="javascript:void()">{{item.name}}</a></li>
                    </ul>
                </nav>
            </div>
            <div class="bd">
                <ul>
                    <template v-for="(course, index) in item.courses" :key="course.id">
                        <li class="big-grid havehover"  v-if="index < 1">
                            <router-link :to="{path:'/course/detail/' + course.id}">
                                <div class="mask"></div>
                                <div class="circle">
                                    <p>课程</p>
                                    <p>cover</p>
                                </div>
                                <div class="word">
                                    {{course.name}}
                                </div>
                                <img :src="course.coverUrl" alt="">
                            </router-link>
                        </li>
                    </template>
                    <template v-for="(course, index) in item.courses" :key="course.id">
                        <li class="pro-grid" v-if="index >= 1">
                            <router-link :to="{path:'/course/detail/' + course.id}">
                                <div class="picbox">
                                    <img :src="course.coverUrl" alt="">
                                </div>
                                <div class="wordbox">
                                    <p class="proname">{{course.name}}</p>
                                    <p class="course-price-score">
                                        <span class="price-text">￥{{course.price}}</span>
                                        <span class="score-text">推荐指数: {{course.recommendedScore || 0}}/5</span>
                                    </p>
                                </div>
                            </router-link>
                        </li>
                    </template>
                    <li class="more-grid">
                        <router-link :to="{path:'/courses', query:{classificationId: item.id} }">
                            <p>查看更多</p>
                            <p>{{item.name}}</p>
                            <span class="iconfont">&#xe619;</span>
                        </router-link>
                    </li>
                </ul>
            </div>
        </div>
    </section>
</template>

<script>
import { reactive, ref } from '@vue/reactivity'
import { onMounted, computed} from 'vue'
import { get,post,del,patch } from '@/api/http'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useStore } from "vuex";

export default({
  setup() {
    const store = useStore()
    let classifications = ref([])
    let payDialogVisible = ref(false)
    let order = ref({
        phone: null,
        orderDetails: [],
        sllerId: null
    })
    let orderDetail = reactive({
        skuId: null,
        number: null,
        price: null,
        sllerId: null
    })
    
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
      let userCode = localStorage.getItem('userCode');
      if (userCode == null || userCode.length === 0) {
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
      orderDetail.sllerId = val.userId
    }

    function pay() {
        order.value.phone = localStorage.getItem('userCode');
        order.value.orderDetails.push(orderDetail);
        post('order', order.value).then(res => {
            ElMessage({
                message: '亲！恭喜您支付成功',
                type: 'success',
            })
            payDialogVisible.value = false  
        })
        order.value.phone = '';
        order.value.orderDetails = [];
    }

    onMounted(() => {
        get('classification?currentPage=1'
            + '&pageSize=10'
            + '&sorter=desc-sort'
            ).then(res => {
                classifications.value = res.list
                if (classifications.value) {
                for(const item of classifications.value) {
                    const classificationId = item.id
                    get('course?currentPage=1'
                        + '&pageSize=7'
                        + '&qp-classificationId-eq=' + classificationId
                        + '&qp-auditStatus-eq=3'
                        ).then(res => {
                        if (res.list && res.list.length > 0) {
                            item.courses = res.list
                        } else {
                            item.courses = []
                        }
                    }).catch(error => {
                        console.error('获取分类 ' + classificationId + ' 的课程失败:', error)
                        item.courses = []
                    })
                }
                }
        })

      showAnnouncement1(8)
      showAnnouncement2(6)
    })


    let announcementList1 = ref([])
    let announcementList2 = ref([])

    function showAnnouncement1(val) {
        get('announcement?qp-type-eq=1', '').then(res => {
          announcementList1.value = res.list
          if (val != null) {
            announcementList1.value = announcementList1.value.slice(0, val)
          }
        })
    }

    function showAnnouncement2(val) {
        get('announcement?qp-type-eq=2', '').then(res => {
          announcementList2.value = res.list
          if (val != null) {
            announcementList2.value = announcementList2.value.slice(0, val)
          }
        })
    }

    function splitTime(val) {
        if (val.includes('T')) {
            return val.split("T")[0]
        }
        return val
    }

    const geeFlag = [

    ]

    return {
      classifications,
      payDialogVisible,
      orderDetail,
      announcementList1,
      announcementList2,
      showAnnouncement1,
      showAnnouncement2,
      pay,
      addCart,
      showPayDialog,
      splitTime,
      geeFlag,
    }
  },
})
</script>

<style scoped>
.news-and-notice {
    width: 1152px;
    height: 376px;
    margin: 0 auto;
    margin-top: 40px;
    padding: 30px 0;
    border-top: 2px solid #f0f0f0;
}

.news-and-notice .news {
    float: left;
    width: 857px;
    height: 376px;
}

.news-and-notice .notice {
    float: left;
    width: 295px;
    height: 376px;
}

.news-and-notice .news h3 {
    margin-bottom: 20px;
    font-size: 24px;
    color: #333;
    font-weight: bold;
    border-left: 4px solid var(--primaryColor);
    padding-left: 12px;
}
.news-and-notice .news .news-content .hot-news {
    float: left;
    width: 422px;
    height: 328px;
    margin-right: 20px;
    position: relative;
}
.news-and-notice .news .news-content .hot-news img {
    max-width: 100%;
        max-height: 100%;
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
}
.news-and-notice .news .news-content .hot-news .info {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 40px;
    background-color: rgba(8,8,8,0.40);
    text-align: center;
    color: #FFFFFF;
    line-height: 40px;
}
.news-and-notice .news .news-content ul {
    float: left;
    width: 380px;
    height: 328px;
}
.news-and-notice .news .news-content ul li {
    line-height: 41px;
    overflow: hidden;
    font-size: 16px;
}
.news-and-notice .news .news-content ul li a {
    float: left;
    color: #000000;
}
.news-and-notice .news .news-content ul li a:hover {
    color: var(--primaryCurColor);
}
.news-and-notice .news .news-content ul li span {
    float: right;
}
.news-and-notice .notice h3 {
    margin-bottom: 20px;
    font-size: 24px;
    color: #333;
    font-weight: bold;
    border-left: 4px solid var(--primaryColor);
    padding-left: 12px;
}

.news-and-notice .notice ol li {
    overflow: hidden;
    margin-bottom: 12px;
    transition: all 0.3s ease;
}

.news-and-notice .notice ol li:last-child {
    margin-bottom: 0;
}

.news-and-notice .notice ol li:hover {
    transform: translateX(4px);
}

.news-and-notice .notice ol li .num {
    float: left;
    width: 43px;
    height: 47px;
    background: linear-gradient(135deg, var(--primaryColor) 0%, var(--primaryCurColor) 100%);
    font-size: 30px;
    color: #FFFFFF;
    text-align: center;
    line-height: 47px;
    margin-right: 9px;
    border-radius: 4px;
    box-shadow: 0 2px 6px rgba(32, 189, 154, 0.3);
}
.news-and-notice .notice ol li dl dt a {
    color: #666;
    font-size: 16px;
    line-height: 47px;
}
.news-and-notice .notice ol li dl dt a:hover {
    color: var(--primaryCurColor);
}

.news-and-notice .notice ol li dl dd a {
    color: #999;
}
.ad-images {
    width: 1152px;
    margin: 20px auto;
    overflow: hidden;
    height: 200px;
}

.ad-images img {
   width: 100%;
    height: 100%;
    object-fit: cover;
}







.tsddty {
    height: 456px;
}
.tsddty .center-wrap {
    padding-top: 40px;
}
.tsddty .center-wrap h2 {
    text-align: center;
}
.tsddty .center-wrap ul li {
    float: left;
}
.tsddty .bd ul li {
    float: left;
    width: 264px;
    height: 290px;
    margin-right: 32px;
    box-shadow: 0 4px 8px 0 rgba(0,0,0,0.08);
}
.tsddty .bd {
    padding-top: 32px;
}
.tsddty .bd ul li:last-child {
    margin-right: 0;
}
.tsddty .bd ul li .wordbox {
    padding: 16px 9px 0;
}
.tsddty .bd ul li .wordbox .proname {
    font-weight: bold;
    font-size: 16px;
    color: #000000;
    height: 48px;
}
.tsddty .bd ul li .wordbox .score {
    font-size: 13px;
    color: #FA6400;
}
.tsddty .bd ul li .wordbox .comment {
    font-size: 13px;
    color: #000000;
}
.tsddty .bd ul li:hover {
    box-shadow: 0 8px 16px 0 rgba(0,0,0,0.20);
}
.content-part h2 {
    font-size: 32px;
    font-weight: bold;
    color: #333;
    position: relative;
    padding-bottom: 12px;

    &::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 0;
        width: 60px;
        height: 4px;
        background: linear-gradient(to right, var(--primaryColor), var(--primaryCurColor));
        border-radius: 2px;
    }
}

.content-part em {
    position: relative;
    top: 20px;
    left: 10px;
    color: #1C1F21;
    float: left;
}


.common-style {
    height: 698px;
    padding-top: 40px;
}
.common-style .hd h2 {
    float: left;
}
.common-style .hd em {
    float: left;
}
.content-part .hd nav {
    float: right;
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
.common-style .bd {
    padding-top: 32px;
}
.common-style .bd>ul>li {
    float: left;
    margin-right: 32px;
    margin-bottom: 32px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    background-color: white;
    border-radius: 6px;
    overflow: hidden;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.common-style .bd>ul>li:hover {
    box-shadow: 0 8px 20px rgba(32, 189, 154, 0.15);
    transform: translateY(-6px);
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
.common-style .bd>ul>li.big-grid img {
    /* width: 100%;
    height: 100%;
    object-fit: contain; */
    display: block;
    margin: 0 auto;
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
    transform: scale();
    height: 184px;
    position: relative;
}
.common-style .bd>ul>li.pro-grid .picbox img {  
    /* width: 100%;
    height: 100%;
    object-fit: contain; */
    display: block;
    margin: 0 auto;
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










.content-part .more-grid {
    width: 264px;
    height: 270px;
    /* background-image: linear-gradient(180deg, #34E3BC 0%, #15AF7A 98%); */
    background-image: linear-gradient(180deg, var(--primaryColor) 0%, var(--primaryCurColor) 98%);
    box-sizing: border-box;
    position: relative ;
}
.content-part .more-grid a {
    display: block;
    padding-top: 46px;
    height: 270px;
    box-sizing: border-box;
}
.content-part .more-grid p {
    text-align: center;
    color: white;
    line-height: 24px;
    font-size: 15px;
}
.content-part .more-grid .iconfont {
    position: absolute;
    font-size: 48px;
    color: white;
    width: 48px;
    height: 48px;
    top: 118px;
    left: 50%;
    margin-left: -24px;
}
/* 鼠标触碰more-grid盒子之后，iconfont进行蹦跳动画 */
.content-part .more-grid:hover .iconfont {
    animation: ud .4s ease 0s infinite alternate;
}
@keyframes ud {
    form {
        transform: translateY(-10px);
    }
    to {
        transform: translateY(10px);
    }
}
.content-part .more-grid ul {
    margin-top: 112px;
    padding-left: 42px;
    margin: 112px auto 0 auto;
}
.content-part .more-grid ul li {
    float: left;
    border-right: 1px solid white;
    color: white;
    padding: 0 6px;
    height: 16px;
    line-height: 16px;
    font-size: 15px;
}
.content-part .more-grid ul li:last-child {
    border-right: none;
}

.zsjsdlx {
    height: 738px;
    background-color: #f9f9f9;
    border-top: 1px solid #f0f0f0;
    border-bottom: 1px solid #f0f0f0;
}
.zsjsdlx .hd em {
    float: left;
    width: 300px;
}
.zsjsdlx .hd nav {
    float: right;
    width: 85px;
}
.zsjsdlx .bd ul li .wordbox {
    padding: 16px 9px 0;
}
.zsjsdlx .bd ul li .wordbox .proname {
    font-weight: bold;
    font-size: 16px;
    color: #000000;
    height: 30px;
    overflow: hidden;
}
.zsjsdlx .bd ul li .wordbox .score {
    font-size: 15px;
    color: #FA6400;
}
.tsdzsjsdlxdty .bd ul li .wordbox .comment {
    font-size: 15px;
    color: #000000;
}
.zsjsdlx .bd .more-grid {
    /* background-image: linear-gradient(180deg, #BD5EE8 0%, #832ABB 98%); */
    background-image: linear-gradient(180deg, var(--primaryColor) 0%, var(--primaryCurColor) 98%);
    
}
.zsjsdlx .bd .more-grid ul {
    padding-left: 50px;
}
.gee-flag-css {
    font-size: 15px;
}
.big-grid.havehover .mask {
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    background-color: rgba(0,0,0,.7);
    /* 总透明度 */
    opacity: 0;
    /* 过渡 */
    transition: opacity .4s ease 0s;
}
.big-grid.havehover:hover .mask {
    opacity: 1;
}
.big-grid.havehover .circle {
    position: absolute;
    top: -106px;
    left: 50%;
    margin-left: -53px;
    width: 106px;
    height: 106px;
    border: 1px solid white;
    border-radius: 50%;
    color: white;
    box-sizing: border-box;
    padding-top: 25px;
    font-size: 18px;
    transition: top .5s ease 0s;
}
.big-grid.havehover .circle p{
    text-align: center;
}
.big-grid.havehover:hover .circle{
    top: 62px;
}
.big-grid.havehover .word{
    position: absolute;
    bottom: -100px;
    left: 10px;
    color: white;
    transition: bottom .5s ease 0s;
}
.big-grid.havehover:hover .word {
    bottom: 30px;
}

.backtotop {
    position: fixed;
    bottom: 80px;
    right: 80px;
    width: 80px;
    height: 80px;
    background: #ccc;
    font-size: 20px;
    text-align: center;
    padding-top: 12px;
    box-sizing: border-box;
    color: #000;
    /* 隐藏元素 */
    display: none;
}


.bottom {
  margin-top: 13px;
  line-height: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* .button {
  padding: 0;
  min-height: auto;
} */

.image {
  width: 100%;
  display: block;
  height: 400px;
}

.el-row {
    background:white;
}
.module-css{
    color: rgb(63, 58, 59);
    margin-bottom: 0px;
}
.sku-name{
  overflow: hidden;			/* 文字长度超出限定宽度，则隐藏超出的内容 */
  text-overflow: ellipsis;
  /* 将对象作为弹性伸缩盒子模型显示 */
  /* display: -webkit-box;     	       */
  /* 控制最多显示几行，这里采用最多显示两行  */
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  color: rgb(71, 178, 240);
}
.course-price-score {
  display: flex !important;
  justify-content: space-between !important;
  align-items: center !important;
  margin-top: 8px !important;
  padding: 0 !important;
  line-height: normal !important;
}
.course-price-score .price-text {
  font-size: 15px !important;
  color: #FA6400 !important;
  font-weight: bold;
  white-space: nowrap;
}
.course-price-score .score-text {
  font-size: 13px !important;
  color: #666 !important;
  white-space: nowrap;
}
</style>
