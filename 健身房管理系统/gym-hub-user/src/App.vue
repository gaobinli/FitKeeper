<template>
    <router-view></router-view>
</template>

<script>
  export default {
    name: 'App'
  }
</script>

<style>
:root{
    /* 健身房主题：活力绿色 */
    --primaryColor: #20BD9A;
    --primaryCurColor: #35857f;

    /* 备选方案：深绿色
    --primaryColor: #2d9764;
    --primaryCurColor: #1a6b47;
    */

    /* 旧配色方案
    --primaryColor: #e97840;
    --primaryCurColor: #f1996d;
    */
}

@font-face {
    font-family: "PingFangSCRegular";
    src: url('/fonts/PingFangSCRegular.ttf') format('truetype');
}
@font-face {
    font-family: 'iconfont';
    src: url('/fonts/iconfont.eot');
    src: url('/fonts/iconfont.eot?#iefix') format('embedded-opentype'),
        url('/fonts/iconfont.woff2') format('woff2'),
        url('/fonts/iconfont.woff') format('woff'),
        url('/fonts/iconfont.ttf') format('truetype'),
        url('/fonts/iconfont.svg#iconfont') format('svg');
    /* src: url('./fonts/iconfont.eot');
    src: url('./fonts/iconfont.eot?#iefix') format('embedded-opentype'),
        url('./fonts/iconfont.woff2') format('woff2'),
        url('./fonts/iconfont.woff') format('woff'),
        url('./fonts/iconfont.ttf') format('truetype'),
        url('./fonts/iconfont.svg#iconfont') format('svg'); */
}
.iconfont {
    font-family: "iconfont" !important;
    font-size: 16px;
    font-style: normal;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
}
body {
    font-family: "PingFangSCRegular";
}
.center-wrap {
    width: 1152px;
    margin: 0 auto;
}
.clearfix {
    overflow: hidden;
}
.clearfix::after {
    content: '';
    display: block;
    clear: both;
    overflow: hidden;
}
.tac {
    text-align: center;
}
.db {
    display: block;
}
.dib {
    display: inline-block;
}
</style>
