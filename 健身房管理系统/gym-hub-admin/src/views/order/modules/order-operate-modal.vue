
<script setup lang="tsx">
import { computed, ref, watch, reactive, defineComponent } from 'vue';
import type { SelectOption, useMessage  } from 'naive-ui';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';
import SvgIcon from '@/components/custom/svg-icon.vue';
import { getLocalIcons } from '@/utils/icon';
import {
fetchPostOrder,
fetchPatchOrder,
fetchUserList,
fetchOrderDetailList2,
} from '@/service/api';
import { useAuthStore } from '@/store/modules/auth';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { useAppStore } from '@/store/modules/app';
defineOptions({
  name: 'OrderOperateModal'
});

const uploadUrl = import.meta.env.VITE_SERVICE_BASE_URL + "/upload"
const authStore = useAuthStore();
const appStore = useAppStore();

export type OperateType = NaiveUI.TableOperateType | 'addChild';

interface Props {
  /** the type of operation */
  operateType: OperateType;
  /** the edit menu data or the parent menu data when adding a child menu */
  rowData?: Api.SystemManage.Menu | null;
}

const props = defineProps<Props>();

interface Emits {
  (e: 'submitted'): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
  default: false
});

const { formRef, validate, restoreValidation } = useNaiveForm();
const { defaultRequiredRule } = useFormRules();

const title = computed(() => {
  const titles: Record<OperateType, string> = {
    add: '新增',
    edit: '编辑'
  };
  return titles[props.operateType];
});

const model = ref(createDefaultModel());

function createDefaultModel(): Model {
  return {
      id: null,
      phone: null,
      status: null,
      orderNo: null,
      receiveTime: null,
      sendTime: null,
  };
}

type RuleKey = Extract<keyof Model,
'undefind'
| 'id'
| 'phone'
| 'status'
| 'orderNo'
| 'receiveTime'
| 'sendTime'
>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  'id' : defaultRequiredRule,
  'phone' : defaultRequiredRule,
  'status' : defaultRequiredRule,
  'orderNo' : defaultRequiredRule,
  'receiveTime' : defaultRequiredRule,
  'sendTime' : defaultRequiredRule,
};

function handleInitModel() {
  model.value = createDefaultModel();
  console.log('=== model.value======》', model.value)

  if (props.operateType === 'edit' && props.rowData) {
    Object.assign(model.value, props.rowData);
  }
  if(model.value.url) {
      previewFileList.value = [
          {
            id: '1',
            name: '我是react.png',
            status: 'finished',
            url: model.value.url
          },
      ]
      model.value.url
    }
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();
  // request

  if (model.value.id) {
    const { error, data } = await fetchPatchOrder(model.value.id, model.value);
    if (error) {
      return
    }
  } else {
    const { error, data } = await fetchPostOrder(model.value);
    if (error) {
      return
    }
  }

  window.$message?.success($t('common.updateSuccess'));
  closeDrawer();
  emit('submitted');
}



watch(visible, () => {
  if (visible.value) {
    handleInitModel();
    fetchOrderList2();
  } else {
    previewFileList.value = []
  }
});

// watch(
//   () => model.value.routeName,
//   () => {
//     handleUpdateRoutePathByRouteName();
//     handleUpdateI18nKeyByRouteName();
//   }
// );

const orderDtails = ref([])
async function fetchOrderList2() {
  const { error, data } = await fetchOrderDetailList2(model.value.orderNo);
  orderDtails.value = data
}

const previewFileList = ref([])
function handleFileListUpdate(files) {
  //console.log("======files======>", files)
  if (files && files.event && files.event?.target && files.event.target.response) {
    model.value.url = JSON.parse(files.event.target.response).data
  }

  if(model.value.url) {
    previewFileList.value = [
        {
          id: '1',
          name: '我是react.png',
          status: 'finished',
          url: model.value.url
        },
    ]
    model.value.url
  }
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



</script>


<template>
  <NModal v-model:show="visible" :title="title" preset="card" class="w-800px">
    <NScrollbar class="h-480px pr-20px">
       <NTable :bordered="false" :single-line="false">
    <thead>
      <tr>
        <th>订单号</th>
        <th>名称</th>
        <th>封面</th>
        <th>价格</th>
        <th>数量</th>
        <th>总价</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="item in orderDtails" :key="item.id">
        <td>{{item.orderNo}}</td>
        <td>{{item.skuName}}</td>
        <td><NImage :src='item.skuImg' width='80'></NImage></td>
        
        <td>{{item.price}}</td>
        <td>{{item.number}}</td>
        <td>{{item.price * item.number}}</td>
      </tr>
    </tbody>
  </NTable>
    </NScrollbar>
    <template #footer>
      <NSpace justify="end" :size="16">
        <NButton @click="closeDrawer">{{ $t('common.cancel') }}</NButton>
        <NButton type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</NButton>
      </NSpace>
    </template>
  </NModal>
</template>

<style scoped></style>