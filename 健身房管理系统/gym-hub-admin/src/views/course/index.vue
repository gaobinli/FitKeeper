
<script setup lang="tsx">
import { computed, ref, reactive, watch } from 'vue';
import { NButton, NPopconfirm, NTag } from 'naive-ui';
import { useBoolean } from '@sa/hooks';
import {
fetchCourseData,
fetchDeleteCourse,
fetchPatchCourse,
fetchCoachList,
fetchClassificationList,
} from '@/service/api';
import { useAppStore } from '@/store/modules/app';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import { yesOrNoRecord } from '@/constants/common';
import SvgIcon from '@/components/custom/svg-icon.vue';
import CourseOperateModal, { type OperateType } from './modules/course-operate-modal.vue';
import CourseSearch from './modules/course-search.vue';
import { useAuthStore } from '@/store/modules/auth';

const authStore = useAuthStore();

const appStore = useAppStore();

const { bool: visible, setTrue: openModal } = useBoolean();

const wrapperRef = ref<HTMLElement | null>(null);

// Initialize search params with reactive object
const initialApiParams = reactive({
  currentPage: 1,
  pageSize: 10,
  qp_name_like: null,
  qp_coachId_eq: null,
  qp_price_like: null,
  qp_classificationId_eq: null,
  qp_coverUrl_like: null,
  qp_content_like: null,
  qp_courseTime_like: null,
  qp_limitNumber_like: null,
  qp_auditDesc_like: null,
  qp_auditStatus_eq: null,
  qp_recommendedScore_like: null,
  qp_simpleDesc_like: null,
});

const { columns, columnChecks, data, loading, pagination, getData, getDataByPage, searchParams, resetSearchParams } = useTable({
  apiFn: fetchCourseData,
  apiParams: initialApiParams,
  immediate: false, // Don't fetch immediately, wait for role check
  columns: () => [
    {
      type: 'selection',
      align: 'center',
      width: 48
    },
    {
      key: 'index',
      title: '序号',
      //width: 80,
      align: 'center'
    },
    {
      key: 'name',
      title: '课程名称',
      //width: 80,
      align: 'center'
    },
    {
      key: 'coachId',
      title: '教练',
      //width: 80,
      align: 'center',
      render: row => {
        if (row.coachId) {
          let res = []
          for(const item of coachOptions.value) {
              if (item.value === row.coachId) {
                res.push(item.label)
              }
            // for (const item2 of row.coachIds) {
            //   if (item.value === item2) {
            //     res.push(item.label)
            //   }
            // }
          }
          return res.join("、")
        }
        return ''
      }
    },
    {
      key: 'price',
      title: '价格',
      //width: 80,
      align: 'center'
    },
    {
      key: 'classificationId',
      title: '分类',
      //width: 80,
      align: 'center',
      render: row => {
        if (row.classificationId) {
          let res = []
          for(const item of classificationOptions.value) {
              if (item.value === row.classificationId) {
                res.push(item.label)
              }
            // for (const item2 of row.classificationIds) {
            //   if (item.value === item2) {
            //     res.push(item.label)
            //   }
            // }
          }
          return res.join("、")
        }
        return ''
      }
    },
    {
      key: 'coverUrl',
      title: '课程封面',
      //width: 80,
      align: 'center',
      render: row => {
        if (row.coverUrl === null) {
          return null;
        }
        return <NImage src={row.coverUrl} width='100'></NImage>;
      }
    },
    {
      key: 'content',
      title: '课程内容',
      //width: 80,
      align: 'center',
      render: row => {
        return <NButton type="primary" ghost size="small" onClick={() => handleViewContent(row)}>
          查看
        </NButton>;
      }
    },
    {
      key: 'courseTime',
      title: '课程时间',
      width: 154,
      align: 'center',
      render: row => {
        if (row.courseTime) {
          return row.courseTime.replace('T', ' ')
        }
        return ''
      }
    },
    {
      key: 'limitNumber',
      title: '限制人数',
      //width: 80,
      align: 'center'
    },
    {
      key: 'simpleDesc',
      title: '简单描述',
      //width: 80,
      align: 'center'
    },
    {
      key: 'auditDesc',
      title: '审核描述',
      //width: 80,
      align: 'center'
    },
    {
      key: 'auditStatus',
      title: '审核状态',
      //width: 80,
      align: 'center',
      render: row => {
        if (row.auditStatus) {
          let res = []
          for(const item of auditStatusOptions.value) {
              if (item.value === row.auditStatus) {
                res.push(item.label)
              }
            // for (const item2 of row.auditStatuss) {
            //   if (item.value === item2) {
            //     res.push(item.label)
            //   }
            // }
          }
          return res.join("、")
        }
        return ''
      }
    },
    {
      key: 'recommendedScore',
      title: '推荐指数',
      //width: 80,
      align: 'center',
      render: row => {
        if (row.recommendedScore === null || row.recommendedScore === undefined) {
          return null;
        }
        return <NRate value={row.recommendedScore} readonly />;
      }
    },
    // {
    //   key: 'modifyUser',
    //   title: '修改人',
    //   //width: 80,
    //   align: 'center'
    // },
    // {
    //   key: 'modifyTime',
    //   title: '修改时间',
    //   width: 154,
    //   align: 'center',
    //   render: row => {
    //     if (row.modifyTime) {
    //       return row.modifyTime.replace('T', ' ')
    //     }
    //     return ''
    //   }
    // },
    {
      key: 'operate',
      title: $t('common.operate'),
      align: 'center',
      width: 300,
      render: row => (
        <div class="flex-center justify-center gap-8px">
          <NButton type="primary" ghost size="small" onClick={() => handleEdit(row)}>
            {$t('common.edit')}
          </NButton>
          <NButton type="warning" ghost size="small" onClick={() => handleAudit(row)}>
            审核
          </NButton>
          <NPopconfirm onPositiveClick={() => handleDelete(row.id)}>
            {{
              default: () => $t('common.confirmDelete'),
              trigger: () => (
                <NButton type="error" ghost size="small">
                  {$t('common.delete')}
                </NButton>
              )
            }}
          </NPopconfirm>
        </div>
      )
    }
  ]
});

const { checkedRowKeys, onBatchDeleted, onDeleted } = useTableOperate(data, getData);

const operateType = ref<OperateType>('add');

function handleAdd() {
  operateType.value = 'add';
  openModal();
}

async function handleBatchDelete() {
  const { error, data } =  await fetchDeleteCourse(checkedRowKeys.value);
  // request
  console.log(checkedRowKeys.value);

  onBatchDeleted();
}

async function handleDelete(id: number) {
  // request
  const { error, data } =  await fetchDeleteCourse(id);
  console.log(id);
  onDeleted();
}

/** the edit menu data or the parent menu data when adding a child menu */
const editingData: Ref<Api.SystemManage.Menu | null> = ref(null);

function handleEdit(item: Api.SystemManage.Menu) {
  operateType.value = 'edit';
  editingData.value = { ...item };
  openModal();
}

const auditVisible = ref(false);
const auditFormRef = ref(null);
const auditModel = ref({
  id: null,
  auditStatus: null,
  auditDesc: null,
});

function handleAudit(row: any) {
  auditModel.value = {
    id: row.id,
    auditStatus: row.auditStatus || null,
    auditDesc: row.auditDesc || null,
  };
  auditVisible.value = true;
}

async function handleAuditSubmit() {
  if (!auditModel.value.auditStatus || !auditModel.value.auditDesc) {
    window.$message?.error('审核状态和审核描述都必须填写');
    return;
  }
  const submitData = {
    id: auditModel.value.id,
    auditStatus: auditModel.value.auditStatus,
    auditDesc: auditModel.value.auditDesc,
  };
  const { error, data } = await fetchPatchCourse(auditModel.value.id, submitData);
  if (error) {
    return;
  }
  window.$message?.success($t('common.updateSuccess'));
  auditVisible.value = false;
  getDataByPage();
}

const contentVisible = ref(false);
const contentData = ref<{ name: string; content: string } | null>(null);

function handleViewContent(row: any) {
  contentData.value = {
    name: row.name,
    content: row.content
  };
  contentVisible.value = true;
}

function handleAddChildMenu(item: Api.SystemManage.Menu) {
  operateType.value = 'addChild';

  editingData.value = { ...item };

  openModal();
}

const allPages = ref<string[]>([]);

// Flag to track if role has been initialized
const roleInitialized = ref(false);

/**
 * Apply role-based filter to query parameters
 * If user is coach (role='other'), add coachId filter with user's ID
 */
function applyRoleBasedFilter() {
  if (authStore.userInfo.role === 'other' && authStore.userInfo.userId) {
    // Coach role: filter by their own coach ID
    initialApiParams.qp_coachId_eq = authStore.userInfo.userId;
    searchParams.qp_coachId_eq = authStore.userInfo.userId;
  } else {
    // Admin or other role: no filter
    initialApiParams.qp_coachId_eq = null;
    searchParams.qp_coachId_eq = null;
  }
}

/**
 * Initialize page data
 * Must apply role-based filter before fetching data
 */
function init() {
  // Load dropdown options first
  getCoachOptions()
  getClassificationOptions()

  // Apply role-based filter before fetching data
  applyRoleBasedFilter()

  // Fetch data with role-based conditions
  getData()

  // Mark role as initialized
  roleInitialized.value = true
}

/**
 * Watch for role changes and update filters accordingly
 */
watch(() => authStore.userInfo.role, (newRole) => {
  if (newRole !== undefined) {
    // Role has changed, reapply filters and refresh data
    applyRoleBasedFilter()
    getData()
  }
})

/**
 * Watch for userId changes (especially on page refresh)
 * When userId is loaded from API, apply filters and fetch data
 */
watch(() => authStore.userInfo.userId, (newUserId) => {
  if (newUserId && authStore.userInfo.role === 'other') {
    // User ID is now available, reapply filters and refresh data for coach
    applyRoleBasedFilter()
    getData()
  }
})

const coachOptions = ref([])
async function getCoachOptions() {
  const { error, data } = await fetchCoachList();
  if (!data) {
    return
  }
  coachOptions.value = []
  for (const item of data) {
    coachOptions.value.push({ label: item.name, value: item.id });
  }
  //console.log('coachOptions========>', coachOptions.value)
}
const classificationOptions = ref([])
async function getClassificationOptions() {
  const { error, data } = await fetchClassificationList();
  if (!data) {
    return
  }
  classificationOptions.value = []
  for (const item of data) {
    classificationOptions.value.push({ label: item.name, value: item.id });
  }
  //console.log('classificationOptions========>', classificationOptions.value)
}

/**
 * Handle course search with role-based filtering
 * Apply role filter before search
 */
function handleCourseSearch() {
  // Reapply role-based filter before searching
  applyRoleBasedFilter()
  // Execute search
  getDataByPage()
}

/**
 * Handle course search reset with role-based filtering
 * Keep role filter after reset
 */
function handleCourseReset() {
  resetSearchParams()
  // Reapply role-based filter after reset
  applyRoleBasedFilter()
}

const auditStatusOptions = ref([
    {
      label: '待审核',
      value: 1
    },
    {
      label: '审核驳回',
      value: 2
    },
    {
      label: '审核通过',
      value: 3
    },
])


// init
init();
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    
    <CourseSearch v-model:model="searchParams" @reset="handleCourseReset" @search="handleCourseSearch" />
    <NCard title="数据列表" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper">
      <template #header-extra>
        <TableHeaderOperation
          v-model:columns="columnChecks"
          :disabled-delete="checkedRowKeys.length === 0"
          :loading="loading"
          @add="handleAdd"
          @delete="handleBatchDelete"
          @refresh="getData"
        />
      </template>
      <NDataTable
        v-model:checked-row-keys="checkedRowKeys"
        :columns="columns"
        :data="data"
        size="small"
        :flex-height="!appStore.isMobile"
        :scroll-x="1088"
        :loading="loading"
        :row-key="row => row.id"
        remote
        :pagination="pagination"
        class="sm:h-full"
      />
      <CourseOperateModal
        v-model:visible="visible"
        :operate-type="operateType"
        :row-data="editingData"
        :all-pages="allPages"
        @submitted="getDataByPage"
      />
      <NModal v-model:show="auditVisible" title="课程审核" preset="card" class="w-600px">
        <NForm label-placement="left" :label-width="100">
          <NFormItem label="审核状态：" required>
            <NSelect
              v-model:value="auditModel.auditStatus"
              :options="auditStatusOptions"
              placeholder="请选择审核状态"
            />
          </NFormItem>
          <NFormItem label="审核描述：" required>
            <NInput
              v-model:value="auditModel.auditDesc"
              type="textarea"
              placeholder="请输入审核描述"
              :rows="4"
            />
          </NFormItem>
        </NForm>
        <template #footer>
          <NSpace justify="end" :size="16">
            <NButton @click="auditVisible = false">{{ $t('common.cancel') }}</NButton>
            <NButton type="primary" @click="handleAuditSubmit">{{ $t('common.confirm') }}</NButton>
          </NSpace>
        </template>
      </NModal>
      <NModal v-model:show="contentVisible" preset="card" class="w-900px">
        <template #header>
          {{ contentData?.name }} - 课程内容
        </template>
        <NScrollbar class="h-480px">
          <div v-if="contentData?.content" v-html="contentData.content" class="prose prose-sm max-w-none p-16px"></div>
          <div v-else class="text-center text-gray-500 p-16px">
            暂无课程内容
          </div>
        </NScrollbar>
      </NModal>
    </NCard>
  </div>
</template>

<style scoped></style>