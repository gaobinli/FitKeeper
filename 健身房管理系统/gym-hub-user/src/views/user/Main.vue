<template>
    <div class="header-con">
        <div class="center-wrap">
            <h1>{{$projectName}}</h1>
            <div class="soso-box">
                <input type="text" v-model="searchName" placeholder="查询课程或教练">
                <router-link :to="{path:'/courses', query:{searchName: searchName} }" class="btn iconfont">&#xe62d;</router-link>
            </div>
        </div>
    </div>

    <!-- 轮播图 -->
    <div class="carousel-section"><Carousel/></div>

    <!-- 快速导航 -->
    <div class="quick-nav">
        <div class="center-wrap">
            <div class="nav-item">
                <router-link to="/courses">
                    <div class="nav-icon">💪</div>
                    <div class="nav-text">
                        <p class="nav-title">精品课程</p>
                        <p class="nav-desc">专业教练精心设计</p>
                    </div>
                </router-link>
            </div>
            <div class="nav-item">
                <router-link to="/coaches">
                    <div class="nav-icon">👨‍🏫</div>
                    <div class="nav-text">
                        <p class="nav-title">教练团队</p>
                        <p class="nav-desc">专业运动指导</p>
                    </div>
                </router-link>
            </div>
            <div class="nav-item">
                <router-link to="/courseReserve">
                    <div class="nav-icon">📅</div>
                    <div class="nav-text">
                        <p class="nav-title">我的预约</p>
                        <p class="nav-desc">管理课程预约</p>
                    </div>
                </router-link>
            </div>
            <div class="nav-item">
                <router-link to="/userInfo">
                    <div class="nav-icon">💳</div>
                    <div class="nav-text">
                        <p class="nav-title">我的钱包</p>
                        <p class="nav-desc">充值和余额管理</p>
                    </div>
                </router-link>
            </div>
        </div>
    </div>

    <!-- 主要内容 -->
    <div style="margin-top:40px"><Module/></div>
</template>

<script>
import Carousel from './Carousel.vue'
import Module from './Module.vue'
import { reactive, ref } from '@vue/reactivity'

export default({
    components: { Carousel,Module },
    setup() {
        const searchName = ref('')
        return {
            searchName
        }
    },
    
})
</script>

<style lang="scss" scoped>
.header-con {
    height: 36px;
    padding: 21px 0;
    background: linear-gradient(to bottom, #ffffff 0%, #f9f9f9 100%);
}

.header-con h1 {
    font-size: 28px;
    color: var(--primaryColor);
    float: left;
    font-weight: bold;
    line-height: 36px;
    letter-spacing: 1px;
}

.header-con .soso-box {
    float: left;
    margin-left: 30px;
    width: 420px;
    padding-top: 4px;
}

.header-con .soso-box input {
    width: 280px;
    height: 28px;
    border: 2px solid var(--primaryColor);
    border-radius: 4px 0 0 4px;
    font-size: 14px;
    outline: none;
    padding-left: 12px;
    float: left;
    transition: all 0.3s ease;

    &:focus {
        border-color: var(--primaryCurColor);
        box-shadow: 0 0 8px rgba(32, 189, 154, 0.2);
    }
}

.header-con .soso-box .btn {
    float: left;
    width: 32px;
    height: 28px;
    background-color: var(--primaryColor);
    color: #fff;
    text-align: center;
    line-height: 30px;
    font-size: 18px;
    border-radius: 0 4px 4px 0;
    cursor: pointer;
    transition: background-color 0.3s ease;

    &:hover {
        background-color: var(--primaryCurColor);
    }
}

.carousel-section {
    margin: 20px 0;
}

/* 快速导航 */
.quick-nav {
    background: linear-gradient(135deg, var(--primaryColor) 0%, var(--primaryCurColor) 100%);
    padding: 30px 0;
    margin: 20px 0;
    box-shadow: 0 4px 12px rgba(32, 189, 154, 0.15);
}

.quick-nav .center-wrap {
    display: flex;
    justify-content: space-between;
    gap: 20px;
}

.nav-item {
    flex: 1;
    background: white;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    transition: all 0.3s ease;

    &:hover {
        transform: translateY(-8px);
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);

        .nav-icon {
            transform: scale(1.15);
        }
    }

    a {
        display: flex;
        align-items: center;
        padding: 20px;
        text-decoration: none;
        color: inherit;
    }
}

.nav-icon {
    font-size: 40px;
    margin-right: 16px;
    transition: transform 0.3s ease;
    min-width: 60px;
    text-align: center;
}

.nav-text {
    flex: 1;

    .nav-title {
        font-size: 16px;
        font-weight: bold;
        color: #333;
        margin: 0 0 4px 0;
    }

    .nav-desc {
        font-size: 13px;
        color: #999;
        margin: 0;
    }
}
</style>


