
<script setup lang="tsx">
import { computed, ref, watch } from 'vue';
import { useAuthStore } from '@/store/modules/auth';
import { NButton, NPopconfirm, NTag } from 'naive-ui';
import { useBoolean } from '@sa/hooks';
import {
fetchCoachData,
fetchDeleteCoach,
} from '@/service/api';
import { useAppStore } from '@/store/modules/app';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import { yesOrNoRecord } from '@/constants/common';
import SvgIcon from '@/components/custom/svg-icon.vue';
import CoachOperateModal, { type OperateType } from './modules/coach-operate-modal.vue';
import CoachSearch from './modules/coach-search.vue';

const appStore = useAppStore();

const { bool: visible, setTrue: openModal } = useBoolean();

const authStore = useAuthStore();

const wrapperRef = ref<HTMLElement | null>(null);

const { columns, columnChecks, data, loading, pagination, getData, getDataByPage, searchParams, resetSearchParams } = useTable({
  apiFn: fetchCoachData,
  apiParams: {
    currentPage: 1,
    pageSize: 10,
    qp_name_like: null,
    qp_username_like: null,
    qp_password_like: null,
    qp_phone_like: null,
    qp_url_like: null,
    qp_birthday_like: null,
    qp_address_like: null,
    qp_sex_like: null,
    qp_enable_like: null,
    qp_introduceContent_like: null,
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
      key: 'name',
      title: '姓名',
      //width: 80,
      align: 'center'
    },
    {
      key: 'username',
      title: '账号',
      //width: 80,
      align: 'center'
    },
    // {
    //   key: 'password',
    //   title: '密码',
    //   //width: 80,
    //   align: 'center'
    // },
    {
      key: 'phone',
      title: '手机号',
      //width: 80,
      align: 'center'
    },
    {
      key: 'url',
      title: '宣传照',
      //width: 80,
      align: 'center',
      render: row => {
        if (row.url === null) {
          return null;
        }
        return <NImage src={row.url} width='100'></NImage>;
      }
    },
    {
      key: 'birthday',
      title: '出生日期',
      //width: 80,
      align: 'center',
      render: row => {
        if (row.birthday === null) {
          return null;
        }
        return row.birthday.split(' ')[0];
      }
    },
    {
      key: 'address',
      title: '地址',
      //width: 80,
      align: 'center'
    },
    {
      key: 'sex',
      title: '性别',
      //width: 80,
      align: 'center',
      render: row => {
        if (row.sex === null) {
          return null;
        }
        return <NTag type={row.sex === 1 ? 'info' : 'warning'}>{row.sex === 1 ? '男': '女' }</NTag>;
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
      key: 'introduceContent',
      title: '介绍',
      //width: 80,
      align: 'center',
      render: row => {
        if (row.introduceContent === null) {
          return <NButton type="primary" ghost size="small" onClick={() => handleViewIntroduce(row)}>
            查看
          </NButton>;
        }
        return <NButton type="primary" ghost size="small" onClick={() => handleViewIntroduce(row)}>
          查看
        </NButton>;
      }
    },
    {
      key: 'enable',
      title: '是否启用',
      //width: 80,
      align: 'center',
      render: row => {
        if (row.enable === null) {
          return null;
        }
        return <NTag type={row.enable ? 'success' : 'error'}>{row.enable ? '启用': '禁用' }</NTag>;
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
  const { error, data } =  await fetchDeleteCoach(checkedRowKeys.value);
  // request
  console.log(checkedRowKeys.value);

  onBatchDeleted();
}

async function handleDelete(id: number) {
  // request
  const { error, data } =  await fetchDeleteCoach(id);
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

const introduceVisible = ref(false);
const introduceData = ref<{ name: string; introduceContent: string } | null>(null);

function handleViewIntroduce(row: any) {
  introduceData.value = {
    name: row.name,
    introduceContent: row.introduceContent
  };
  introduceVisible.value = true;
}

function handleAddChildMenu(item: Api.SystemManage.Menu) {
  operateType.value = 'addChild';

  editingData.value = { ...item };

  openModal();
}

const allPages = ref<string[]>([]);

function init() {
}


// init
init();
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <CoachSearch v-model:model="searchParams" @reset="resetSearchParams" @search="getDataByPage" />
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
      <CoachOperateModal
        v-model:visible="visible"
        :operate-type="operateType"
        :row-data="editingData"
        :all-pages="allPages"
        @submitted="getDataByPage"
      />
      <NModal v-model:show="introduceVisible" preset="card" class="w-800px">
        <template #header>
          {{ introduceData?.name }} - 介绍
        </template>
        <div v-if="introduceData?.introduceContent" v-html="introduceData.introduceContent" class="prose prose-sm max-w-none"></div>
        <div v-else class="text-center text-gray-500">
          暂无介绍内容
        </div>
      </NModal>
    </NCard>
  </div>
</template>

<style scoped></style>