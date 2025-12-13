<script setup lang="tsx">
import { NButton, NPopconfirm, NTag } from 'naive-ui';
import { fetchGetRoleList, fetchAllParentMenu, fetchDeleteRole } from '@/service/api';
import { useAppStore } from '@/store/modules/app';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import { enableStatusRecord } from '@/constants/business';
import RoleOperateDrawer from './modules/role-operate-drawer.vue';
import RoleSearch from './modules/role-search.vue';
import { computed, effectScope, onScopeDispose, reactive, ref, watch } from 'vue';

const appStore = useAppStore();

const {
  columns,
  columnChecks,
  data,
  loading,
  getData,
  getDataByPage,
  mobilePagination,
  searchParams,
  resetSearchParams
} = useTable({
  apiFn: fetchGetRoleList,
  apiParams: {
    current: 1,
    size: 10,
    // if you want to use the searchParams in Form, you need to define the following properties, and the value is null
    // the value can not be undefined, otherwise the property in Form will not be reactive
    // qp_name_like: null,
    // qp_code_like: null
  },
  columns: () => [
    {
      type: 'selection',
      align: 'center',
      width: 48
    },
    {
      key: 'id',
      title: 'ID',
      width: 64,
      align: 'center'
    },
    {
      key: 'code',
      title: '角色编码',
      align: 'center',
      minWidth: 120
    },
    {
      key: 'name',
      title: '角色名称',
      align: 'center',
      minWidth: 120
    },
    {
      key: 'menuCodes',
      title: '菜单',
      align: 'center',
      minWidth: 120,
      render: row => {
        if (row.menuCodes) {
          let res = []
          for(const item of menuOptions.value) {
            for (const item2 of row.menuCodes) {
              if (item.value === item2) {
                res.push(item.label)
              }
            }
          }
          return res.join("、")
        }
        
        return ''
      }
    },
    {
      key: 'modifyUser',
      title: '修改人',
      minWidth: 120
    },
    {
      key: 'modifyTime',
      title: '修改时间',
      minWidth: 120
    },
    {
      key: 'operate',
      title: $t('common.operate'),
      align: 'center',
      width: 130,
      render: row => (
        <div class="flex-center gap-8px">
          <NButton type="primary" ghost size="small" onClick={() => edit(row.id)}>
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

const {
  drawerVisible,
  operateType,
  editingData,
  handleAdd,
  handleEdit,
  checkedRowKeys,
  onBatchDeleted,
  onDeleted
  // closeDrawer
} = useTableOperate(data, getData);

async function handleBatchDelete() {
  // request
  const { error, data } =  await fetchDeleteRole(checkedRowKeys.value);
  console.log(checkedRowKeys.value);

  onBatchDeleted();
}

async function handleDelete(id: number) {
  // request
  const { error, data } =  await fetchDeleteRole(id);
  console.log(id);

  onDeleted();
}

function edit(id: number) {
  handleEdit(id);
}

const menuOptions = ref([])
async function getMenuOptions() {
  const { error, data } = await fetchAllParentMenu();
  if (!data) {
    return
  }
  menuOptions.value = []
  for (const item of data) {
    menuOptions.value.push({ label: item.name, value: item.code });
    console.log('menuOptions========>', menuOptions.value)
  }
}
getMenuOptions()

</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <RoleSearch v-model:model="searchParams" @reset="resetSearchParams" @search="getDataByPage" />
    <NCard :title="$t('page.manage.role.title')" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper">
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
        :scroll-x="702"
        :loading="loading"
        remote
        :row-key="row => row.id"
        :pagination="mobilePagination"
        class="sm:h-full"
      />
      <RoleOperateDrawer
        v-model:visible="drawerVisible"
        :operate-type="operateType"
        :row-data="editingData"
        @submitted="getDataByPage"
      />
    </NCard>
  </div>
</template>

<style scoped></style>
