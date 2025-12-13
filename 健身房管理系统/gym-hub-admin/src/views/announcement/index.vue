
<script setup lang="tsx">
import { computed, ref, watch, reactive } from 'vue';
import { useAuthStore } from '@/store/modules/auth';
import { NButton, NPopconfirm, NTag } from 'naive-ui';
import { useBoolean } from '@sa/hooks';
import {
fetchAnnouncementData,
fetchDeleteAnnouncement,
} from '@/service/api';
import { useAppStore } from '@/store/modules/app';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import { yesOrNoRecord } from '@/constants/common';
import SvgIcon from '@/components/custom/svg-icon.vue';
import AnnouncementOperateModal, { type OperateType } from './modules/announcement-operate-modal.vue';
import AnnouncementSearch from './modules/announcement-search.vue';

const appStore = useAppStore();

const { bool: visible, setTrue: openModal } = useBoolean();

const authStore = useAuthStore();

const wrapperRef = ref<HTMLElement | null>(null);

// Initialize search params with reactive object
const initialApiParams = reactive({
  currentPage: 1,
  pageSize: 10,
  qp_title_like: null,
  qp_type_eq: null,
  qp_content_like: null,
  qp_coachId_eq: null,
});

const { columns, columnChecks, data, loading, pagination, getData, getDataByPage, searchParams, resetSearchParams } = useTable({
  apiFn: fetchAnnouncementData,
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
      key: 'title',
      title: '标题',
      //width: 80,
      align: 'center'
    },
    {
      key: 'type',
      title: '类型',
      //width: 80,
      align: 'center',
      render: row => {
        if (row.type) {
          let res = []
          for(const item of typeOptions.value) {
              if (item.value === row.type) {
                res.push(item.label)
              }
            // for (const item2 of row.types) {
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
    // {
    //   key: 'content',
    //   title: '内容',
    //   //width: 80,
    //   align: 'center'
    // },
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
      width: 230,
      render: row => (
        <div class="flex-center justify-center gap-8px">
          <NButton type="primary" ghost size="small" onClick={() => handleEdit(row)}>
            {$t('common.edit')}
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
  // For coach role, set coachId to current user's ID
  const dataToPass = authStore.userInfo.role === 'other' ? { coachId: authStore.userInfo.userId } : null;
  editingData.value = dataToPass;
  openModal();
}

async function handleBatchDelete() {
  const { error, data } =  await fetchDeleteAnnouncement(checkedRowKeys.value);
  // request
  console.log(checkedRowKeys.value);

  onBatchDeleted();
}

async function handleDelete(id: number) {
  // request
  const { error, data } =  await fetchDeleteAnnouncement(id);
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

const allPages = ref<string[]>([]);

/**
 * Apply role-based filter to query parameters
 * Only coach (role='other') needs coachId filter
 */
function applyRoleBasedFilter() {
  if (authStore.userInfo.role === 'other' && authStore.userInfo.userId) {
    // Coach role: filter by their own coach ID
    initialApiParams.qp_coachId_eq = authStore.userInfo.userId;
    searchParams.qp_coachId_eq = authStore.userInfo.userId;
  }
  // For admin and other roles: don't set the filter, keep it null
}

/**
 * Initialize page data
 * The watch listeners with immediate: true will handle data fetching
 */
function init() {
  // This function is now minimal since watch listeners handle initialization
}

/**
 * Handle announcement search with role-based filtering
 * Apply role filter before search
 */
function handleAnnouncementSearch() {
  // Reapply role-based filter before searching
  applyRoleBasedFilter()
  // Execute search
  getDataByPage()
}

/**
 * Handle announcement search reset with role-based filtering
 * Keep role filter after reset
 */
function handleAnnouncementReset() {
  resetSearchParams()
  // Reapply role-based filter after reset
  applyRoleBasedFilter()
}

const typeOptions = ref([
    {
      label: '健身资讯',
      value: 1
    },
    {
      label: '系统公告',
      value: 2
    },
])

/**
 * Watch for role changes and update filters accordingly
 */
watch(() => authStore.userInfo.role, (newRole) => {
  if (newRole !== undefined) {
    // Only fetch data for 'other' (coach) role
    if (newRole === 'other' && authStore.userInfo.userId) {
      applyRoleBasedFilter()
      getData()
    } else if (newRole !== 'other') {
      // For admin role, fetch data without filter
      applyRoleBasedFilter()
      getData()
    }
  }
}, { immediate: true })

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
}, { immediate: true })

// init
init();
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <AnnouncementSearch v-model:model="searchParams" @reset="handleAnnouncementReset" @search="handleAnnouncementSearch" />
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
      <AnnouncementOperateModal
        v-model:visible="visible"
        :operate-type="operateType"
        :row-data="editingData"
        :all-pages="allPages"
        @submitted="getDataByPage"
      />
    </NCard>
  </div>
</template>

<style scoped></style>