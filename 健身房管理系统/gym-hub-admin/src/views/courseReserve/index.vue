
<script setup lang="tsx">
import { computed, ref, watch, reactive } from 'vue';
import { useAuthStore } from '@/store/modules/auth';
import { NButton, NPopconfirm, NTag } from 'naive-ui';
import { useBoolean } from '@sa/hooks';
import {
fetchCourseReserveData,
fetchDeleteCourseReserve,
fetchUserList,
fetchCourseList,
fetchCourseData,
fetchPatchCourseReserve,
} from '@/service/api';
import { useAppStore } from '@/store/modules/app';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import { yesOrNoRecord } from '@/constants/common';
import SvgIcon from '@/components/custom/svg-icon.vue';
import CourseReserveOperateModal, { type OperateType } from './modules/courseReserve-operate-modal.vue';
import CourseReserveSearch from './modules/courseReserve-search.vue';

const appStore = useAppStore();

const { bool: visible, setTrue: openModal } = useBoolean();

const authStore = useAuthStore();

const wrapperRef = ref<HTMLElement | null>(null);

// Store for coach's course IDs (comma-separated string)
const coachCourseIds = ref<string>('');
// Flag to track if coach courses have been loaded
const coachCoursesLoaded = ref<boolean>(false);

// Initialize search params with reactive object
const initialApiParams = reactive({
  currentPage: 1,
  pageSize: 10,
  qp_userId_eq: null,
  qp_courseId_eq: null,
  qp_price_like: null,
  qp_auditDesc_like: null,
  qp_auditStatus_eq: null,
  qp_courseId_in: null,
});

const { columns, columnChecks, data, loading, pagination, getData, getDataByPage, searchParams, resetSearchParams } = useTable({
  apiFn: fetchCourseReserveData,
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
      key: 'userId',
      title: '用户',
      //width: 80,
      align: 'center',
      render: row => {
        if (row.userId) {
          let res = []
          for(const item of userOptions.value) {
              if (item.value === row.userId) {
                res.push(item.label)
              }
            // for (const item2 of row.userIds) {
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
      key: 'courseId',
      title: '课程',
      //width: 80,
      align: 'center',
      render: row => {
        if (row.courseId) {
          let res = []
          for(const item of courseOptions.value) {
              if (item.value === row.courseId) {
                res.push(item.label)
              }
            // for (const item2 of row.courseIds) {
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
      key: 'auditStatus',
      title: '预约状态',
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
      key: 'auditDesc',
      title: '审核描述',
      //width: 80,
      align: 'center'
    },
    {
      key: 'modifyUser',
      title: '修改人',
      //width: 80,
      align: 'center'
    },
    {
      key: 'modifyTime',
      title: '修改时间',
      width: 154,
      align: 'center',
      render: row => {
        if (row.modifyTime) {
          return row.modifyTime.replace('T', ' ')
        }
        return ''
      }
    },
    {
      key: 'operate',
      title: $t('common.operate'),
      align: 'center',
      width: 300,
      render: row => (
        <div class="flex-center justify-center gap-8px">
          {authStore.userInfo.role !== 'other' && (
            <NButton type="warning" ghost size="small" onClick={() => handleAudit(row)}>
              审核
            </NButton>
          )}
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
  const { error, data } =  await fetchDeleteCourseReserve(checkedRowKeys.value);
  // request
  console.log(checkedRowKeys.value);

  onBatchDeleted();
}

async function handleDelete(id: number) {
  // request
  const { error, data } =  await fetchDeleteCourseReserve(id);
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

function handleAddChildMenu(item: Api.SystemManage.Menu) {
  operateType.value = 'addChild';

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
    window.$message?.error('预约状态和审核描述都必须填写');
    return;
  }
  const submitData = {
    id: auditModel.value.id,
    auditStatus: auditModel.value.auditStatus,
    auditDesc: auditModel.value.auditDesc,
  };
  const { error, data } = await fetchPatchCourseReserve(auditModel.value.id, submitData);
  if (error) {
    return;
  }
  window.$message?.success($t('common.updateSuccess'));
  auditVisible.value = false;
  getDataByPage();
}

const allPages = ref<string[]>([]);

async function init() {
  // Load dropdown options first
  await getUserOptions()
  await getCourseOptions()

  // For coach role, must fetch their courses first
  // If courses cannot be fetched, don't fetch list data
  if (authStore.userInfo.role === 'other') {
    if (!authStore.userInfo.userId) {
      // No user ID, cannot proceed
      return
    }
    // Fetch coach courses
    const success = await fetchCoachCourses()
    if (!success) {
      // No courses found, don't fetch list data
      return
    }
  }

  // Apply role-based filter before fetching data
  applyRoleBasedFilter()

  // Fetch data with role-based conditions
  getData()
}

/**
 * Handle reserve search with role-based filtering
 * For coach role, must have courses to search
 */
async function handleReserveSearch() {
  // For coach role, must ensure courses are loaded
  if (authStore.userInfo.role === 'other') {
    if (!authStore.userInfo.userId) {
      // No user ID, cannot search
      return
    }
    // If courses not loaded yet, fetch them
    if (!coachCourseIds.value) {
      const success = await fetchCoachCourses()
      if (!success) {
        // No courses found, cannot search
        window.$message?.warning('暂无课程，无法搜索预约数据')
        return
      }
    }
  }

  // Reapply role-based filter before searching
  applyRoleBasedFilter()
  // Execute search
  getDataByPage()
}

/**
 * Apply role-based filter to query parameters
 * If user is coach (role='other'), add course ID filter with their own courses
 */
function applyRoleBasedFilter() {
  if (authStore.userInfo.role === 'other' && coachCourseIds.value) {
    // Coach role: filter by their own course IDs
    initialApiParams.qp_courseId_in = coachCourseIds.value;
    searchParams.qp_courseId_in = coachCourseIds.value;
  } else {
    // Admin or other role: no filter
    initialApiParams.qp_courseId_in = null;
    searchParams.qp_courseId_in = null;
  }
}

// Fetch courses for the logged-in coach
async function fetchCoachCourses() {
  try {
    const { error, data } = await fetchCourseData({
      qp_coachId_eq: authStore.userInfo.userId
    });

    if (!error && data && data.list && data.list.length > 0) {
      // Extract course IDs and join with comma
      const courseIds = data.list.map((course: any) => course.id).join(',');
      coachCourseIds.value = courseIds;
      // Set the filter in search params
      initialApiParams.qp_courseId_in = courseIds;
      searchParams.qp_courseId_in = courseIds;
      return true; // Successfully fetched courses
    }
    return false; // No courses found
  } catch (error) {
    console.error('Failed to fetch coach courses:', error);
    return false; // Error occurred
  }
}


const userOptions = ref([])
async function getUserOptions() {
  const { error, data } = await fetchUserList();
  if (!data) {
    return
  }
  userOptions.value = []
  for (const item of data) {
    userOptions.value.push({ label: item.name, value: item.id });
  }
  //console.log('userOptions========>', userOptions.value)
}
const courseOptions = ref([])
async function getCourseOptions() {
  const { error, data } = await fetchCourseList();
  if (!data) {
    return
  }
  courseOptions.value = []
  for (const item of data) {
    courseOptions.value.push({ label: item.name, value: item.id });
  }
  //console.log('courseOptions========>', courseOptions.value)
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

/**
 * Handle course reserve search reset with role-based filtering
 * Keep role filter after reset
 */
function handleReserveReset() {
  resetSearchParams()
  // Reapply role-based filter after reset
  applyRoleBasedFilter()
}

// init
init();
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <CourseReserveSearch v-model:model="searchParams" @reset="handleReserveReset" @search="handleReserveSearch" />
    <NCard title="数据列表" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper">
      <template #header-extra>
        <TableHeaderOperation
          v-model:columns="columnChecks"
          :disabled-delete="checkedRowKeys.length === 0"
          :loading="loading"
          @add="handleAdd"
          @delete="handleBatchDelete"
          @refresh="getData"
          :hideAdd="authStore.userInfo.role === 'other' ? true : false"
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
      <CourseReserveOperateModal
        v-model:visible="visible"
        :operate-type="operateType"
        :row-data="editingData"
        :all-pages="allPages"
        @submitted="getDataByPage"
      />
      <NModal v-model:show="auditVisible" title="预约审核" preset="card" class="w-600px">
        <NForm label-placement="left" :label-width="100">
          <NFormItem label="预约状态：" required>
            <NSelect
              v-model:value="auditModel.auditStatus"
              :options="auditStatusOptions"
              placeholder="请选择预约状态"
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
    </NCard>
  </div>
</template>

<style scoped></style>