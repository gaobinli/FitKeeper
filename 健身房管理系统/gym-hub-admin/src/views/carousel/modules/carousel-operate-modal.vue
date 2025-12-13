
<script setup lang="tsx">
import { computed, ref, watch, reactive, nextTick } from 'vue';
import type { SelectOption } from 'naive-ui';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';
import SvgIcon from '@/components/custom/svg-icon.vue';
import { getLocalIcons } from '@/utils/icon';
import {
fetchPostCarousel,
fetchPatchCarousel,
} from '@/service/api';
import { useAuthStore } from '@/store/modules/auth';

defineOptions({
  name: 'CarouselOperateModal'
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
      url: null,
      sort: null,
  };
}

type RuleKey = Extract<keyof Model,
'undefind'
| 'id'
| 'name'
| 'url'
| 'sort'
>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  'id' : defaultRequiredRule,
  'name' : defaultRequiredRule,
  'url' : defaultRequiredRule,
  'sort' : defaultRequiredRule,
};

function handleInitModel() {
  model.value = createDefaultModel();

  if (props.operateType === 'edit' && props.rowData) {
    Object.assign(model.value, props.rowData);
  }

  // 重置文件列表
  fileList.value = [];

  console.log('handleInitModel - operateType:', props.operateType);
  console.log('handleInitModel - rowData:', props.rowData);
  console.log('handleInitModel - model.url:', model.value.url);

  if(model.value.url) {
      fileList.value = [
          {
            id: '1',
            name: '我是react.png',
            status: 'finished',
            url: model.value.url
          },
      ]
      console.log('handleInitModel - fileList:', fileList.value);
    }
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();
  // request

  if (model.value.id) {
    const { error, data } = await fetchPatchCarousel(model.value.id, model.value);
    if (error) {
      return
    }
  } else {
    const { error, data } = await fetchPostCarousel(model.value);
    if (error) {
      return
    }
  }

  window.$message?.success($t('common.updateSuccess'));
  closeDrawer();
  emit('submitted');
}

watch(visible, async () => {
  if (visible.value) {
    await nextTick();
    handleInitModel();
    restoreValidation();
  }
});

// watch(
//   () => model.value.routeName,
//   () => {
//     handleUpdateRoutePathByRouteName();
//     handleUpdateI18nKeyByRouteName();
//   }
// );

const fileList = ref([])

function handleFileListUpdate(files) {
  //console.log("======files======>", files)
  if (files && files.event && files.event?.target && files.event.target.response) {
    model.value.url = JSON.parse(files.event.target.response).data
  }

  if(model.value.url) {
    fileList.value = [
        {
          id: '1',
          name: '我是react.png',
          status: 'finished',
          url: model.value.url
        },
    ]
  }
}


</script>


<template>
  <NModal v-model:show="visible" :title="title" preset="card" class="w-800px">
    <NScrollbar class="h-480px pr-20px">
      <NForm ref="formRef" :model="model" :rules="rules" label-placement="left" :label-width="100">
        <NGrid responsive="screen" item-responsive>
          <NFormItemGi span="24 m:24" label="名称：" path="name">
            <NInput v-model:value="model.name" placeholder="请输入轮播图名称" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="图片：" path="url">
            <NUpload
              :action="uploadUrl"
              v-model:file-list="fileList"
              list-type="image-card"
              :max=1
              @change="handleFileListUpdate"
            />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="排序：" path="sort">
            <NInputNumber v-model:value="model.sort" placeholder="请输入排序" />
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