<template>
    <header class="site-head">
        <div class="topbar">
            <div class="center-wrap">
                <nav class="shortcut-links">
                    <ul>
                        <template v-for="(item, index) in navItems" :key="item.id">
                          <li v-if="index !== 8"  :class="{ cur: selectedIndex === index}">
                            <router-link @click="selectItem(index)" :to="{path:'' + item.routerLink}" class="link-css" :title="item.name">{{item.name}}</router-link>
                          </li>
                        </template>
                    </ul>
                </nav>
                <nav class="topbar-r">
                    <router-link v-if="username" :title="username" :to="{path:'/userInfo'}">欢迎您！{{username}}</router-link>
                    <router-link v-if="username" :to="{path:'/'}" @click="clearUserInfo">|&nbsp;&nbsp;退出</router-link>
                    <router-link v-if="!username" :to="{path:'/login'}">登陆</router-link>
                    <router-link v-if="!username" :to="{path:'/register'}">|&nbsp;&nbsp;注册</router-link>
                </nav>
            </div>
        </div>
    </header>
</template>

<script>
import { onMounted, computed, getCurrentInstance} from 'vue'
import { useStore } from "vuex"
import { get,post,del,patch } from '@/api/http'
import { reactive, ref } from '@vue/reactivity'
import { useRouter } from "vue-router";

export default({
  setup() {
    const store = useStore()
    const router = useRouter()
    let token = localStorage.getItem('token');
    const { proxy } = getCurrentInstance()
    let username = ref('')
    onMounted(() => {
      if (token != null) {
        get('user/getUserInfo', '').then(res => {
          username.value = res.name
          localStorage.setItem('userId', res.id)
          localStorage.setItem('userPhone', res.phone)
        })
      }
      refreshSelectIndex()
    })

    let carNumber = computed(() => {
      return store.state.tab.carNumber
    })

    function refreshSelectIndex() {
      if (router.currentRoute.value && router.currentRoute.value.path) {
        navItems.forEach(item => {
          if (item.routerLink === router.currentRoute.value.path) {
            selectItem(item.id - 1)
          }
        })
      }
    }

    const navItems = [
      { id: 1, name: '首页', routerLink: '/'},
      { id: 2, name: '健身资讯', routerLink: '/message' },
      { id: 3, name: '系统公告', routerLink: '/guide' },
      { id: 7, name: '课程', routerLink: '/course'},
      { id: 7, name: '教练', routerLink: '/coaches'},
      { id: 8, name: '我的预约', routerLink: '/courseReserve'},
      { id: 8, name: '交流社区', routerLink: '/post'},
      { id: 5, name: '点餐', routerLink: '/skus'},
      { id: 4, name: '购物车', routerLink: '/shoppingCar'},
      { id: 6, name: '点餐订单', routerLink: '/orders'},
    ]
    const selectedIndex = ref(0);
    function selectItem(index) {
      selectedIndex.value = index;
    }

    function clearUserInfo() {
      localStorage.removeItem('userPhone');
      localStorage.removeItem('userId');
      localStorage.removeItem('token');
      router.push({path:'/'});
      setTimeout(function () {
        window.location.reload();
      }, 10);
    }

    return{
      carNumber,
      username,
      clearUserInfo,
      navItems,
      selectItem,
      selectedIndex
    }
  },
})
</script>


<style lang="scss" scoped>
.site-head {
    height: 40px;
    /* 使用 min-width 属性 */
    min-width: 1152px;
}
.site-head .topbar {
    height: 40px;
    // background-color: #2A2A2A;
    color: white;
    line-height: 38px;
    /* 使用 min-width 属性 */
    min-width: 1152px;
    // background-color: #20BD9A;
    background-color: var(--primaryColor);
    
}
.site-head .topbar a {
    color: white;
    font-size: 18px;
}
.site-head .topbar .shortcut-links {
    float: left;
}
.site-head .topbar .shortcut-links>ul>li {
    float: left;
    // width: 80px;
    padding: 0 10px;
    text-align: center;
    // font-size: 14px;
}
.site-head .topbar .shortcut-links > ul > li:last-child {
    margin-right: 0;
}
.site-head .topbar .shortcut-links > ul > li.have-menu {
    padding-right: 12px;
    position: relative;
}
.site-head .topbar .shortcut-links > ul > li.have-menu .arrow {
    position: absolute;
    right: 0;
    top: 50%;
    margin-top: -6px;
    width: 12px;
    height: 12px;
    transition: transform .3s ease 0s;
}
.site-head .topbar .shortcut-links > ul > li.have-menu:hover .arrow {
    transform: rotate(180deg);
}
.site-head .topbar .shortcut-links > ul > li.have-menu .arrow b {
    position: absolute;
    left: 3px;
    top: 2px;
    width: 6px;
    height: 6px;
    background-color: #fff;
    transform: rotate(45deg);
}
.site-head .topbar .shortcut-links > ul > li.have-menu .arrow i {
    position: absolute;
    left: 3px;
    top: 0.5px;
    width: 6px;
    height: 6px;
    // background-color: #2A2A2A;
    transform: rotate(45deg);
}
.site-head .topbar .shortcut-links > ul > li.have-menu .menu {
    position: absolute;
    top: 32px;
    left: 0;
    background-color: #fff;
    z-index: 9999;
    /* 隐藏 */
    display: none;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
    padding: 10px;
}
.site-head .topbar .shortcut-links > ul > li.have-menu .sqmenu {
    width: 110px;
}
.site-head .topbar .shortcut-links > ul > li.have-menu .scmenu {
    width: 80px;
}
.site-head .topbar .shortcut-links > ul > li.have-menu .jdmsmenu {
    width: 80px;
}
/* 鼠标碰到 have-menu，就让 menu 出现 */
.site-head .topbar .shortcut-links > ul > li.have-menu:hover .menu {
    display: block;
}
.site-head .topbar .shortcut-links > ul > li.have-menu .menu .menu-list li {
    height: 40px;
    line-height: 40px;
}
.site-head .topbar .shortcut-links > ul > li.have-menu .menu .menu-list li:hover {
    background-color: #20BD9A;
}
.site-head .topbar .shortcut-links > ul > li.have-menu .menu .menu-list a {
    font-size: 17px;
    line-height: 20px;
    color: #1C1F21;
}
.site-head .topbar .shortcut-links > ul > li.have-menu .menu .menu-list a:hover {
    color: #FFFFFF;
}
.site-head .topbar .topbar-r {
    float: right;
}
.site-head .topbar .topbar-r a, .site-head .topbar .topbar-r span {
    margin-right: 11px;
}
.site-head .topbar .topbar-r a:last-child {
    margin-right: 0;
}


.site-head .main-nav {
    height: 40px;
    /* 使用 min-width 属性 */
    min-width: 1152px;
    background-color: #20BD9A;
}
.site-head .main-nav ul li {
    float: left;
    // margin-right: 18px;
    // padding-right: 18px;
    width: 100px;
    text-align: center;
}
.site-head .main-nav ul li a {
    line-height: 40px;
    font-size: 16px;
    color: #FFFFFF;
}
.site-head .topbar ul li.have-menu {
    padding-right: 16px;
    position: relative;
}
.site-head .topbar ul li.have-menu::before {
    content: '';
    position: absolute;
    width: 0px;
    height: 0px;
    right: 0;
    top: 18px;
    border: 5px solid transparent;
    border-bottom: none;
    // border-top-color: #FFFFFF;
    transition: transform .5s ease 0s;
}
.site-head .main-nav ul li.have-menu:hover::before {
    transform: rotate(180deg);
}
.site-head .main-nav ul li .menu {
    position: absolute;
    top: 35px;
    left: 0;
    background-color: #fff;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.20);
    padding: 10px;
    display: none;
}
.site-head .main-nav ul li:hover .menu {
    display: block;
}
.site-head .main-nav ul li .menu dl dd a {
    color: #1C1F21;
}
.site-head .main-nav ul li .menu dl dd a:hover {
    color: pink;
}
.site-head .main-nav ul li .jjzyxmenu{
    width: 150px;
}
.site-head .main-nav ul li .gtymenu{
    width: 90px;
}
.site-head .main-nav ul li .ddwlmenu {
    width: 190px;
}
.site-head .main-nav ul li .sdlymenu {
    width: 160px;
}

.site-head .header-con {
    height: 36px;
    padding: 21px 0;
}
.site-head .header-con h1 {
    font-size: 24px;
    color: #20BD9A;
    float: left;
    font-weight: bold;
    line-height: 36px;
}
.site-head .header-con .soso-box {
    float: left;
    margin-left: 20px;
    width: 405px;
    padding-top: 4px;
}
.site-head .header-con .soso-box input {
    width: 261px;
    height: 28px;
    border: 1px solid #20BD9A;
    border-radius: 4px 0 0 4px;
    font-size: 14px;
    /* 取消默认的外线（外线是文本框特有的东西） */
    outline: none;
    padding-left: 10px;
    float: left;
}
.site-head .header-con .soso-box .btn {
    float: left;
    width: 32px;
    height: 28px;
    background-color: #20BD9A;
    color: #fff;
    text-align: center;
    line-height: 30px;
    font-size: 18px;
    border-radius: 0 4px 4px 0;
}
.cur {
    background-color: var(--primaryCurColor);
}


/* 头部 */
.header-top {
    /* 清除浮动 */
    overflow: hidden;
    // width: 1152px;
    width: 1152px;
    margin: 0 auto;
}
.header-top .title {
    float: left;
    padding-top: 27px;
    width: 320px;
    height: 63px;
    color: #015E58;
    font-size: 20px;
}
.header-top .title h1 {
    font-size: 35px;
    color: #20BD9A;
    float: left;
    font-weight: bold;
    line-height: 63px;
}
.header-top .tool {
    float: right;
    width: 266px;
    height: 77px;
    padding-top: 22px;
}
.header-top .tool .r1 {
    margin-bottom: 14px;
}
.header-top .tool .r1 .tel {
    width: 32px;
    height: 25px;
}
.header-top .tool .r1 .telnumber {
    font-size: 20px;
    color: #00978E;
    display: inline-block;
    width: 158px;
    height: 26px;
    text-align: center;
}
.header-top .tool .r1 .icon {
    width: 24px;
    height: 24px;
}
.header-top .tool .r1 .chinese_icon {
    margin-right: 15px;
}
.header-top .tool .r2 {
    width: 264px;
    height: 28px;
    border: 1px solid rgba(202,202,202,0.60);
    border-radius: 4px;
}
.header-top .tool .r2 input {
    float: left;
    width: 224px;
    height: 28px;
    /* 去掉边框线 */
    border: none;
    padding-left: 20px;
    /* 去掉鼠标点击时候的蓝色框 */
    outline: none;
    font-size: 14px;
    line-height: 28px;
}
.header-top .tool .r2 button {
    float: left;
    width: 20px;
    height: 20px;
    /* 背景去掉 */
    background: none;
    /* 去掉边框线 */
    border: none;
    /* 去掉鼠标点击时的蓝色框 */
    outline: none;
    /* 设置触碰时的鼠标指针样式 */
    cursor: pointer;
    /* 相对定位来微调位置 */
    position: relative;
    top: 4px;
    right: 4px;
}
</style>


