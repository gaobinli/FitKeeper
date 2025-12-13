<template>
    <div id="myList" class="myList">
      <ul>
        <li v-for="item in list" :key="item.id">
          <router-link class="item-info" :to="{ path: '/sku/Detail/' + item.id}">
            <img :src="item.url" alt="" />
            <h2>{{item.name}}</h2>
            <h3>{{item.subName}}</h3>
            <p class="price">{{item.price}}元</p>
          </router-link>
        </li>
        <li v-show="isMore && list != null && list.length>=1" id="more">
          <router-link :to="{ path: '/skus', query: { categoryId: moduleIdValue } } ">
            浏览更多
            <i class="el-icon-d-arrow-right"></i>
          </router-link>
        </li>
      </ul>
    </div>
  </template>
  
  <script lang="ts">
  import { Ref, unref } from 'vue'
  import { defineComponent, ref, onMounted } from 'vue'
  
  export default defineComponent({
    name: "skuList",
    props: ["list", "isMore", "isDelete", "moduleIdValue", "delType"],
    setup(props) {
      const buttonRef = ref()
      const popoverRef = ref()
      onMounted(() => {
        
      })
      const onClickOutside = () => {
        unref(popoverRef).popperRef?.delayHide?.()
      }
      
      return {
        onClickOutside,
        buttonRef,
        popoverRef
      }
    }
  })
  </script>
  <!-- @import '../assets/scss/mixin.scss';
@import '../assets/scss/config.scss'; -->
<style lang="scss" scoped>

.myList ul li {
z-index: 1;
float: left;
width: 236px;
height: 273px;
padding: 10px 0;
margin: 0 11px 14.5px 0;
background-color: white;
-webkit-transition: all 0.2s linear;
transition: all 0.2s linear;
position: relative;
}
.myList ul li:nth-child(5n) {
    margin-right: 0;
}
.myList ul li:hover {
z-index: 2;
-webkit-box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
-webkit-transform: translate3d(0, -2px, 0);
transform: translate3d(0, -2px, 0);
}
.myList ul li img {
display: block;
width: 220px;
height: 175px;
// background: url(../assets/imgs/placeholder.png) no-repeat 50%;
margin: 0 auto;
}
.myList ul li h2 {
  margin: 10px 10px 0;
  font-size: 14px;
  font-weight: 400;
  color: #333;
  font-weight: bold;
  text-align: center;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
.myList ul li h3 {
  margin: 8px 10px;
  height: 20px;
  font-size: 12px;
  font-weight: 400;
  color: #b0b0b0;
  text-align: center;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
.myList ul li p {
margin: 10px 10px 10px;
text-align: center;
color: #ff6700;
}
.myList ul li p .del {
margin-left: 0.5em;
color: #b0b0b0;
text-decoration: line-through;
}
.myList #more {
text-align: center;
line-height: 280px;
}
.myList #more a {
font-size: 18px;
color: #333;
}
.myList #more a:hover {
color: #ff6700;
}
.myList ul li .delete {
position: absolute;
top: 10px;
right: 10px;
display: none;
}
.myList ul li:hover .delete {
display: block
}
.myList ul li .delete:hover {
color: #ff6700;
}

.myList .item-info h3 {
}

.myList .item-info p {
    
        line-height: 13px;
        margin: 6px auto 13px;
}

.myList .item-info .price {
    color: #c83939;
    font-weight: bold;
    cursor: pointer;

    &::after {
        content: '';
        margin-left: 5px;
        vertical-align: middle;
    }
}
</style>