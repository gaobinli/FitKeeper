<template>
  <div class="carousel-wrapper">
    <el-carousel :interval="2000" height="500px">
      <el-carousel-item v-for="item in carousels" :key="item.id">
          <img :src="item.url" class="carousel-img" />
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
import { reactive, ref } from '@vue/reactivity'
import { onMounted} from 'vue'
import { get,post,del,patch } from '@/api/http'
import { ElMessageBox, ElMessage } from 'element-plus'

export default({
    setup() {
        let carousels = ref([])

        function getCarousel() {
            get('carousel/list', '').then(res => {
                carousels.value = res
            })
        }
        onMounted(() => {
            getCarousel()
        })

        return {
            carousels
        }
    },
})
</script>


<style scoped>
.carousel-wrapper {
    width: 100%;
    box-shadow: 0 4px 16px rgba(32, 189, 154, 0.1);
    border-radius: 8px;
    overflow: hidden;
    margin-bottom: 20px;
}

.carousel-img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block;
}

/* 自定义轮播指示器样式 */
:deep(.el-carousel__indicator) {
    background-color: rgba(255, 255, 255, 0.5);
}

:deep(.el-carousel__indicator.is-active) {
    background-color: var(--primaryColor);
}

/* 自定义导航按钮 */
:deep(.el-carousel__button) {
    background-color: var(--primaryColor);
}

:deep(.el-carousel__button:hover) {
    background-color: var(--primaryCurColor);
}
</style>