
<script setup lang="tsx">
import { computed, ref, watch, reactive } from 'vue';
import type { SelectOption } from 'naive-ui';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';
import SvgIcon from '@/components/custom/svg-icon.vue';
import { getLocalIcons } from '@/utils/icon';
import {
fetchPostOrderDetail,
fetchPatchOrderDetail,
fetchSkuList,
} from '@/service/api';
import { useAuthStore } from '@/store/modules/auth';

defineOptions({
  name: 'OrderDetailOperateModal'
});

const uploadUrl = import.meta.env.VITE_SERVICE_BASE_URL + "/upload"
const authStore = useAuthStore();

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
      orderNo: null,
      skuId: null,
      number: null,
      price: null,
  };
}

type RuleKey = Extract<keyof Model,
'undefind'
| 'id'
| 'orderNo'
| 'skuId'
| 'number'
| 'price'
>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  'id' : defaultRequiredRule,
  'orderNo' : defaultRequiredRule,
  'skuId' : defaultRequiredRule,
  'number' : defaultRequiredRule,
  'price' : defaultRequiredRule,
};

function handleInitModel() {
  model.value = createDefaultModel();

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
    const { error, data } = await fetchPatchOrderDetail(model.value.id, model.value);
    if (error) {
      return
    }
  } else {
    const { error, data } = await fetchPostOrderDetail(model.value);
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
    restoreValidation();
    getSkuOptions()
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


const skuOptions = ref([])
async function getSkuOptions() {
  const { error, data } = await fetchSkuList();
  if (!data) {
    return
  }
  skuOptions.value = []
  for (const item of data) {
    skuOptions.value.push({ label: item.name, value: item.id });
  }
  //console.log('skuOptions========>', skuOptions.value)
}
</script>


<template>
  <NModal v-model:show="visible" :title="title" preset="card" class="w-800px">
    <NScrollbar class="h-480px pr-20px">
      <NForm ref="formRef" :model="model" :rules="rules" label-placement="left" :label-width="100">
        <NGrid responsive="screen" item-responsive>
          <NFormItemGi span="24 m:24" label="订单编号：" path="orderNo">
            <NInput v-model:value="model.orderNo" placeholder="请输入订单编号" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="skuId：" path="skuId" class="pr-24px">
            <NSelect
              v-model:value="model.skuId"
              :options="skuOptions"
              placeholder="请选择"
            />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="数量：" path="number">
            <NInput v-model:value="model.number" placeholder="请输入数量" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="购买价格：" path="price">
            <NInput v-model:value="model.price" placeholder="请输入购买价格" />
          </NFormItemGi>
        </NGrid>
      </NForm>
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