
<script setup lang="tsx">
import { computed, ref, watch } from 'vue';
import { useAuthStore } from '@/store/modules/auth';
import { NButton, NPopconfirm, NTag } from 'naive-ui';
import { useBoolean } from '@sa/hooks';
import {
fetchPostData,
fetchDeletePost,
fetchUserList,
} from '@/service/api';
import { useAppStore } from '@/store/modules/app';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import { yesOrNoRecord } from '@/constants/common';
import SvgIcon from '@/components/custom/svg-icon.vue';
import PostOperateModal, { type OperateType } from './modules/post-operate-modal.vue';
import PostSearch from './modules/post-search.vue';

const appStore = useAppStore();

const { bool: visible, setTrue: openModal } = useBoolean();

const authStore = useAuthStore();

const wrapperRef = ref<HTMLElement | null>(null);

const { columns, columnChecks, data, loading, pagination, getData, getDataByPage, searchParams, resetSearchParams } = useTable({
  apiFn: fetchPostData,
  apiParams: {
    currentPage: 1,
    pageSize: 10,
    qp_title_like: null,
    qp_content_like: null,
    qp_userId_eq: null,
    qp_excellent_eq: null,
  },
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
      key: 'excellent',
      title: '是否热门',
      //width: 80,
      align: 'center',
      render: row => {
        if (row.excellent === null) {
          return null;
        }
        return <NTag type={row.excellent === 1 ? 'success' : 'warning'}>{row.excellent === 1 ? '热门': '非热门' }</NTag>;
      }
    },
    {
      key: 'modifyUser',
      title: '修改人',
      //width: 80,
      align: 'center',
      render: row => {
        if (row.modifyTime) {
          return row.modifyTime.replace('T', ' ')
        }
        return ''
      }
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
  openModal();
}

async function handleBatchDelete() {
  const { error, data } =  await fetchDeletePost(checkedRowKeys.value);
  // request
  console.log(checkedRowKeys.value);

  onBatchDeleted();
}

async function handleDelete(id: number) {
  // request
  const { error, data } =  await fetchDeletePost(id);
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

function init() {
  getUserOptions()
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
    <PostSearch v-model:model="searchParams" @reset="resetSearchParams" @search="getDataByPage" />
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
      <PostOperateModal
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