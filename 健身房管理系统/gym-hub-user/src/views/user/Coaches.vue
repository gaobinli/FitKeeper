<template>
  <div class="coaches-container">
    <div class="coaches-wrapper">
      <h1 class="page-title">专业教练团队</h1>

      <!-- 搜索和筛选 -->
      <div class="filter-section">
        <el-input
          v-model="searchName"
          placeholder="搜索教练名称"
          class="search-input"
          clearable
          @input="handleSearch"
        />
      </div>

      <!-- 教练卡片列表 -->
      <div class="coaches-grid">
        <div class="coach-card" v-for="coach in coaches" :key="coach.id">
          <div class="coach-image">
            <img :src="coach.url" alt="教练头像" />
          </div>
          <div class="coach-info">
            <h3 class="coach-name">{{ coach.name }}</h3>
            <p class="coach-detail">
              <span class="label">性别：</span>
              <span class="value">{{ coach.sex === 1 ? '男' : '女' }}</span>
            </p>
            <p class="coach-detail" v-if="coach.phone">
              <span class="label">联系方式：</span>
              <span class="value">{{ coach.phone }}</span>
            </p>
            <p class="coach-detail" v-if="coach.birthday">
              <span class="label">出生日期：</span>
              <span class="value">{{ formatDate(coach.birthday) }}</span>
            </p>
            <p class="coach-detail" v-if="coach.address">
              <span class="label">地址：</span>
              <span class="value">{{ coach.address }}</span>
            </p>
            <div class="coach-introduce" v-if="coach.introduceContent">
              <span class="label">介绍：</span>
              <p class="introduce-text">{{ coach.introduceContent }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination-section" v-if="dataTable.totalCount > 0">
        <el-pagination
          :current-page="dataTable.currentPage"
          :page-size="dataTable.pageSize"
          layout="total, prev, pager, next, jumper"
          :total="dataTable.totalCount"
          @current-change="handlePageChange"
        />
      </div>

      <!-- 无数据提示 -->
      <div class="empty-state" v-if="coaches.length === 0 && !loading">
        <p>暂无教练信息</p>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted, reactive, ref } from 'vue'
import { get } from '@/api/http'

export default {
  setup() {
    let coaches = ref([])
    let searchName = ref('')
    let loading = ref(false)

    let dataTable = reactive({
      pageSize: 12,
      totalCount: 0,
      currentPage: 1,
    })

    onMounted(() => {
      window.scrollTo(0, 0)
      queryCoaches()
    })

    function queryCoaches() {
      loading.value = true
      let queryUrl = 'coach?currentPage=' + dataTable.currentPage
        + '&pageSize=' + dataTable.pageSize

      if (searchName.value) {
        queryUrl += '&qp-name-like=' + searchName.value
      }

      get(queryUrl).then(res => {
        coaches.value = res.list || []
        dataTable.totalCount = res.totalCount || 0
        dataTable.pageSize = res.pageSize || 12
        dataTable.currentPage = res.currentPage || 1
        loading.value = false
      }).catch(() => {
        loading.value = false
      })
    }

    function handlePageChange(page) {
      dataTable.currentPage = page
      queryCoaches()
    }

    function handleSearch() {
      dataTable.currentPage = 1
      queryCoaches()
    }

    function formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    }

    return {
      coaches,
      searchName,
      loading,
      dataTable,
      queryCoaches,
      handlePageChange,
      handleSearch,
      formatDate
    }
  }
}
</script>

<style lang="scss" scoped>
.coaches-container {
  width: 100%;
  min-height: 95vh;
  background-color: #f5f5f5;
  padding: 40px 0;
}

.coaches-wrapper {
  width: 1152px;
  margin: 0 auto;
}

.page-title {
  font-size: 28px;
  color: #333;
  margin-bottom: 30px;
  text-align: center;
  font-weight: bold;
}

.filter-section {
  margin-bottom: 30px;
  display: flex;
  gap: 10px;

  .search-input {
    width: 250px;
  }
}

.coaches-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(270px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.coach-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-8px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  }

  .coach-image {
    width: 100%;
    height: 200px;
    overflow: hidden;
    background-color: #f0f0f0;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }

  .coach-info {
    padding: 16px;

    .coach-name {
      font-size: 18px;
      font-weight: bold;
      color: #333;
      margin: 0 0 12px 0;
    }

    .coach-detail {
      margin: 8px 0;
      font-size: 13px;
      color: #666;

      .label {
        color: #999;
        margin-right: 4px;
      }

      .value {
        color: #333;
      }
    }

    .coach-introduce {
      margin-top: 12px;
      padding-top: 12px;
      border-top: 1px solid #eee;

      .label {
        color: #999;
        font-size: 13px;
        display: block;
        margin-bottom: 4px;
      }

      .introduce-text {
        font-size: 12px;
        color: #666;
        line-height: 1.6;
        margin: 0;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
        overflow: hidden;
      }
    }
  }
}

.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  padding-top: 20px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
  font-size: 16px;
}
</style>
