
<script setup lang="tsx">
import { computed, ref, watch, reactive } from 'vue';
import { useAuthStore } from '@/store/modules/auth';
import { NButton, NPopconfirm, NTag } from 'naive-ui';
import { useBoolean } from '@sa/hooks';
import {
fetchCourseAppraiseData,
fetchDeleteCourseAppraise,
fetchCourseList,
fetchCourseData,
fetchUserList,
fetchPatchCourseAppraise,
} from '@/service/api';
import { useAppStore } from '@/store/modules/app';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import { yesOrNoRecord } from '@/constants/common';
import SvgIcon from '@/components/custom/svg-icon.vue';
import WangEditor from '@/components/custom/WangEditor.vue';
import CourseAppraiseOperateModal, { type OperateType } from './modules/courseAppraise-operate-modal.vue';
import CourseAppraiseSearch from './modules/courseAppraise-search.vue';

const appStore = useAppStore();

const { bool: visible, setTrue: openModal } = useBoolean();

const authStore = useAuthStore();

const wrapperRef = ref<HTMLElement | null>(null);

// Store for coach's course IDs (comma-separated string)
const coachCourseIds = ref<string>('');

// Initialize search params with reactive object
const initialApiParams = reactive({
  currentPage: 1,
  pageSize: 10,
  qp_courseId_eq: null,
  qp_userId_eq: null,
  qp_content_like: null,
  qp_courseAppraise_like: null,
  qp_courseId_in: null,
});

const { columns, columnChecks, data, loading, pagination, getData, getDataByPage, searchParams, resetSearchParams } = useTable({
  apiFn: fetchCourseAppraiseData,
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
      key: 'content',
      title: '评价',
      //width: 80,
      align: 'center',
      render: row => {
        return <span v-html={row.content}></span>;
      }
    },
    {
      key: 'courseAppraise',
      title: '评分',
      //width: 80,
      align: 'center',
      render: row => {
        if (row.courseAppraise === null || row.courseAppraise === undefined) {
          return null;
        }
        return <NRate value={row.courseAppraise} readonly />;
      }
    },
    {
      key: 'replyContent',
      title: '回复',
      align: 'center',
      render: row => {
        return <span v-html={row.replyContent}></span>;
      }
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
          <NButton type="primary" ghost size="small" onClick={() => handleEdit(row)}>
            {$t('common.edit')}
          </NButton>
          <NButton type="info" ghost size="small" onClick={() => handleReply(row)}>
            回复
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
  const { error, data } =  await fetchDeleteCourseAppraise(checkedRowKeys.value);
  // request
  console.log(checkedRowKeys.value);

  onBatchDeleted();
}

async function handleDelete(id: number) {
  // request
  const { error, data } =  await fetchDeleteCourseAppraise(id);
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

const replyVisible = ref(false);
const replyModel = ref({
  id: null,
  content: null,
  replyContent: null,
});

function handleReply(row: any) {
  replyModel.value = {
    id: row.id,
    content: row.content || null,
    replyContent: row.replyContent || null,
  };
  replyVisible.value = true;
}

async function handleReplySubmit() {
  if (!replyModel.value.replyContent) {
    window.$message?.error('回复内容不能为空');
    return;
  }
  const submitData = {
    replyContent: replyModel.value.replyContent,
  };
  const { error, data } = await fetchPatchCourseAppraise(replyModel.value.id, submitData);
  if (error) {
    return;
  }
  window.$message?.success($t('common.updateSuccess'));
  replyVisible.value = false;
  getDataByPage();
}

const allPages = ref<string[]>([]);

async function init() {
  // Load dropdown options first
  await getCourseOptions()
  await getUserOptions()

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
 * Handle appraise search with role-based filtering
 * For coach role, must have courses to search
 */
async function handleAppraiseSearch() {
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
        window.$message?.warning('暂无课程，无法搜索评价数据')
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

/**
 * Handle course appraise search reset with role-based filtering
 * Keep role filter after reset
 */
function handleAppraiseReset() {
  resetSearchParams()
  // Reapply role-based filter after reset
  applyRoleBasedFilter()
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

// init
init();
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <CourseAppraiseSearch v-model:model="searchParams" @reset="handleAppraiseReset" @search="handleAppraiseSearch" />
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
      <CourseAppraiseOperateModal
        v-model:visible="visible"
        :operate-type="operateType"
        :row-data="editingData"
        :all-pages="allPages"
        @submitted="getDataByPage"
      />
      <NModal v-model:show="replyVisible" title="回复评价" preset="card" class="w-800px">
        <NScrollbar class="h-480px pr-20px">
          <NSpace vertical>
            <div>
              <div class="font-semibold mb-8px">原评价内容：</div>
              <div class="p-12px bg-gray-50 rounded border border-gray-200">
                <div v-if="replyModel.content" v-html="replyModel.content" class="prose prose-sm max-w-none"></div>
                <div v-else class="text-gray-400">暂无评价内容</div>
              </div>
            </div>
            <div>
              <div class="font-semibold mb-8px">回复内容：</div>
              <wang-editor :contentHtml="replyModel.replyContent" v-on:getWangEditorValue="(val) => replyModel.replyContent = val"></wang-editor>
            </div>
          </NSpace>
        </NScrollbar>
        <template #footer>
          <NSpace justify="end" :size="16">
            <NButton @click="replyVisible = false">{{ $t('common.cancel') }}</NButton>
            <NButton type="primary" @click="handleReplySubmit">{{ $t('common.confirm') }}</NButton>
          </NSpace>
        </template>
      </NModal>
    </NCard>
  </div>
</template>

<style scoped></style>