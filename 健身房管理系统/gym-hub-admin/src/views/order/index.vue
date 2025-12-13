
<script setup lang="tsx">
import { computed, ref, watch } from 'vue';
import { useAuthStore } from '@/store/modules/auth';
import { NButton, NPopconfirm, NTag } from 'naive-ui';
import { useBoolean } from '@sa/hooks';
import {
fetchOrderData,
fetchDeleteOrder,
fetchUserList,
postOrderSend,
} from '@/service/api';
import { useAppStore } from '@/store/modules/app';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import { yesOrNoRecord } from '@/constants/common';
import SvgIcon from '@/components/custom/svg-icon.vue';
import OrderOperateModal, { type OperateType } from './modules/order-operate-modal.vue';
import OrderSearch from './modules/order-search.vue';

const appStore = useAppStore();

const { bool: visible, setTrue: openModal } = useBoolean();

const authStore = useAuthStore();

const wrapperRef = ref<HTMLElement | null>(null);

const { columns, columnChecks, data, loading, pagination, getData, getDataByPage, searchParams, resetSearchParams } = useTable({
  apiFn: fetchOrderData,
  apiParams: {
    currentPage: 1,
    pageSize: 10,
    qp_userId_eq: null,
    qp_status_eq: null,
    qp_orderNo_like: null,
    qp_receiveTime_like: null,
    qp_sendTime_like: null,
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
      key: 'orderNo',
      title: '订单编号',
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
              if (item.value == row.userId) {
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
      key: 'status',
      title: '订单状态',
      //width: 80,
      align: 'center',
      render: row => {
        if (row.status) {
          let res = []
          for(const item of statusOptions.value) {
              if (item.value === row.status) {
                res.push(item.label)
              }
            // for (const item2 of row.statuss) {
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
      key: 'sendTime',
      title: '出餐时间',
      width: 154,
      align: 'center',
      render: row => {
        if (row.sendTime) {
          return row.sendTime.replace('T', ' ')
        }
        return ''
      }
    },
    {
      key: 'receiveTime',
      title: '取餐时间',
      width: 154,
      align: 'center',
      render: row => {
        if (row.receiveTime) {
          return row.receiveTime.replace('T', ' ')
        }
        return ''
      }
    },
    {
      key: 'createTime',
      title: '下单时间',
      width: 154,
      align: 'center',
      render: row => {
        if (row.createTime) {
          return row.createTime.replace('T', ' ')
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
          <NButton type="warning" ghost size="small" onClick={() => {visible2.value = true, orderId.value = row.id, oderStatus.value = row.status}}>
            发货
          </NButton>
          <NButton type="primary" ghost size="small" onClick={() => handleEdit(row)}>
            订单详情
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
  const { error, data } =  await fetchDeleteOrder(checkedRowKeys.value);
  // request
  console.log(checkedRowKeys.value);

  onBatchDeleted();
}

async function handleDelete(id: number) {
  // request
  const { error, data } =  await fetchDeleteOrder(id);
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

const statusOptions = ref([
    {
      label: '待出餐',
      value: 1
    },
    {
      label: '已出餐',
      value: 2
    },
    {
      label: '已取餐',
      value: 3
    },
])

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

const visible2 = ref(false)
const orderId = ref(null)
const oderStatus = ref(null)
async function handleSubmit2() {
  if (oderStatus.value !== 1) {
    window.$message?.success('订单已发货，无需再次发货');  
    visible2.value = false
    return
  }
  const { error, data } = await postOrderSend(orderId.value);
  if (error) {
    return
  }
  visible2.value = false
  window.$message?.success('发货成功');
  getDataByPage()
}
async function cancel2() {
  visible2.value = false
}

// init
init();
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <OrderSearch v-model:model="searchParams" @reset="resetSearchParams" @search="getDataByPage" />
    <NCard title="数据列表" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper">
      <template #header-extra>
        <TableHeaderOperation
          v-model:columns="columnChecks"
          :disabled-delete="checkedRowKeys.length === 0"
          :loading="loading"
          :hideAdd="true"
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
      <OrderOperateModal
        v-model:visible="visible"
        :operate-type="operateType"
        :row-data="editingData"
        :all-pages="allPages"
        @submitted="getDataByPage"
      />
    </NCard>

    <NModal v-model:show="visible2" title="订单发货" preset="card" class="w-800px">
    <NScrollbar class="h-50px pr-20px">
          是否确定发货
    </NScrollbar>
    <template #footer>
      <NSpace justify="end" :size="16">
        <NButton @click="cancel2">{{ $t('common.cancel') }}</NButton>
        <NButton type="primary" @click="handleSubmit2">{{ $t('common.confirm') }}</NButton>
      </NSpace>
    </template>
  </NModal>
  </div>
</template>

<style scoped></style>