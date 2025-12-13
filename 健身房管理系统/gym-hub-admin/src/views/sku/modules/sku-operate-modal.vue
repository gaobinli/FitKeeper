
<script setup lang="tsx">
import { computed, ref, watch, reactive } from 'vue';
import type { SelectOption } from 'naive-ui';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';
import SvgIcon from '@/components/custom/svg-icon.vue';
import { getLocalIcons } from '@/utils/icon';
import {
fetchPostSku,
fetchPatchSku,
fetchCategoryList,
} from '@/service/api';
import { useAuthStore } from '@/store/modules/auth';

defineOptions({
  name: 'SkuOperateModal'
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
      name: null,
      categoryId: null,
      url: null,
      price: null,
      simpleDesc: null,
      inventory: null,
      content: null,
      detail: null,
  };
}

type RuleKey = Extract<keyof Model,
'undefind'
| 'id'
| 'name'
| 'categoryId'
| 'url'
| 'price'
| 'simpleDesc'
| 'inventory'
| 'content'
| 'detail'
>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  'id' : defaultRequiredRule,
  'name' : defaultRequiredRule,
  'categoryId' : defaultRequiredRule,
  'url' : defaultRequiredRule,
  'price' : defaultRequiredRule,
  'simpleDesc' : defaultRequiredRule,
  'inventory' : defaultRequiredRule,
  'content' : defaultRequiredRule,
  'detail' : defaultRequiredRule,
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
    const { error, data } = await fetchPatchSku(model.value.id, model.value);
    if (error) {
      return
    }
  } else {
    const { error, data } = await fetchPostSku(model.value);
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
    getCategoryOptions()
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


const categoryOptions = ref([])
async function getCategoryOptions() {
  const { error, data } = await fetchCategoryList();
  if (!data) {
    return
  }
  categoryOptions.value = []
  for (const item of data) {
    categoryOptions.value.push({ label: item.name, value: item.id });
  }
  //console.log('categoryOptions========>', categoryOptions.value)
}
import WangEditor from "@/components/custom/WangEditor.vue";
const getWangEditorValue = (str: string) => {
  model.value.content = str
};
const getWangEditorValue2 = (str: string) => {
  model.value.detail = str
};
</script>


<template>
  <NModal v-model:show="visible" :title="title" preset="card" class="w-800px">
    <NScrollbar class="h-480px pr-20px">
      <NForm ref="formRef" :model="model" :rules="rules" label-placement="left" :label-width="100">
        <NGrid responsive="screen" item-responsive>
          <NFormItemGi span="24 m:24" label="美食：" path="name">
            <NInput v-model:value="model.name" placeholder="请输入名称" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="分类：" path="categoryId" class="pr-24px">
            <NSelect
              v-model:value="model.categoryId"
              :options="categoryOptions"
              placeholder="请选择"
            />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="封面：" path="url">
            <NUpload
              :action="uploadUrl"
              :default-file-list="previewFileList"
              list-type="image-card"
              :max=1
              @change="handleFileListUpdate"
            />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="价格：" path="price">
            <NInputNumber min=1 v-model:value="model.price" placeholder="请输入价格" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="库存量：" path="inventory">
            <NInputNumber min=1 v-model:value="model.inventory" placeholder="请输入库存量" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="简单描述：" path="simpleDesc">
            <NInput v-model:value="model.simpleDesc" placeholder="请输入简单描述" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="美食介绍：" path="detail">
            <wang-editor :contentHtml="model.detail" v-on:getWangEditorValue="getWangEditorValue2"></wang-editor>
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