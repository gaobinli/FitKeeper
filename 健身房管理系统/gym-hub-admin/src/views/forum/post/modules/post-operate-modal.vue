
<script setup lang="tsx">
import { computed, ref, watch, reactive } from 'vue';
import type { SelectOption } from 'naive-ui';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';
import SvgIcon from '@/components/custom/svg-icon.vue';
import { getLocalIcons } from '@/utils/icon';
import {
fetchPostPost,
fetchPatchPost,
fetchUserList,
} from '@/service/api';
import { useAuthStore } from '@/store/modules/auth';

defineOptions({
  name: 'PostOperateModal'
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
      title: null,
      content: null,
      userId: null,
      excellent: null,
  };
}

type RuleKey = Extract<keyof Model,
'undefind'
| 'id'
| 'title'
| 'content'
| 'userId'
| 'excellent'
>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  'id' : defaultRequiredRule,
  'title' : defaultRequiredRule,
  'content' : defaultRequiredRule,
  'userId' : defaultRequiredRule,
  'excellent' : defaultRequiredRule,
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
    const { error, data } = await fetchPatchPost(model.value.id, model.value);
    if (error) {
      return
    }
  } else {
    const { error, data } = await fetchPostPost(model.value);
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
    getUserOptions()
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


import WangEditor from "@/components/custom/WangEditor.vue";
const getWangEditorValue = (str: string) => {
  model.value.content = str
};
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

const excellentOptions = ref([
    {
      label: '是',
      value: 1
    },
    {
      label: '否',
      value: 2
    },
])
</script>


<template>
  <NModal v-model:show="visible" :title="title" preset="card" class="w-800px">
    <NScrollbar class="h-480px pr-20px">
      <NForm ref="formRef" :model="model" :rules="rules" label-placement="left" :label-width="100">
        <NGrid responsive="screen" item-responsive>
          <NFormItemGi span="24 m:24" label="标题：" path="title">
            <NInput v-model:value="model.title" placeholder="请输入标题" />
          </NFormItemGi>
          
          <NFormItemGi span="24 m:24" label="用户：" path="userId" class="pr-24px">
            <NSelect
              v-model:value="model.userId"
              :options="userOptions"
              placeholder="请选择"
            />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="是否热门：" path="excellent">
            <NSelect
              v-model:value="model.excellent"
              :options="excellentOptions"
              placeholder="请选择"
            />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="内容：" path="content">
            <wang-editor :contentHtml="model.content" v-on:getWangEditorValue="getWangEditorValue"></wang-editor>
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