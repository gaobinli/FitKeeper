
<script setup lang="tsx">
import { computed, ref, watch, reactive } from 'vue';
import type { SelectOption } from 'naive-ui';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';
import SvgIcon from '@/components/custom/svg-icon.vue';
import { getLocalIcons } from '@/utils/icon';
import {
fetchPostUser,
fetchPatchUser,
} from '@/service/api';
import { useAuthStore } from '@/store/modules/auth';

defineOptions({
  name: 'UserOperateModal'
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
const { defaultRequiredRule, patternRules } = useFormRules();

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
      password: null,
      nickName: null,
      phone: null,
      age: null,
      address: null,
      sex: null,
      enable: null,
      avatarUrl: null,
  };
}

type RuleKey = Extract<keyof Model,
'undefind'
| 'id'
| 'password'
| 'name'
| 'phone'
| 'age'
| 'address'
| 'sex'
| 'enable'
>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  'id' : defaultRequiredRule,
  'name' : defaultRequiredRule,
  'phone' : [defaultRequiredRule, patternRules.phone],
  'sex' : defaultRequiredRule,
  'enable' : defaultRequiredRule,
};
function handleInitModel() {
  model.value = createDefaultModel();
  if (props.operateType === 'edit' && props.rowData) {
    Object.assign(model.value, props.rowData);
    if(model.value.avatarUrl) {
      previewFileList.value = [
          {
            id: '1',
            name: '我是react.png',
            status: 'finished',
            url: model.value.avatarUrl
          },
      ]
      // model.value.avatarUrl
    }
  }
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();
  // request

  if (model.value.id) {
    const { error, data } = await fetchPatchUser(model.value.id, model.value);
    if (error) {
      return
    }
  } else {
    const { error, data } = await fetchPostUser(model.value);
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
  console.log("======files======>", files)
  if (files && files.event && files.event?.target && files.event.target.response) {
    model.value.avatarUrl = JSON.parse(files.event.target.response).data
  }

  if(model.value.avatarUrl) {
    previewFileList.value = [
        {
          id: '1',
          name: '我是react.png',
          status: 'finished',
          url: model.value.avatarUrl
        },
    ]
    // model.value.avatarUrl
  }
}


const sexOptions = ref([
    {
      label: '男',
      value: 1
    },
    {
      label: '女',
      value: 2
    },
])

const enableOptions = ref([
    {
      label: '启用',
      value: true
    },
    {
      label: '禁用',
      value: false
    },
])
</script>


<template>
  <NModal v-model:show="visible" :title="title" preset="card" class="w-800px">
    <NScrollbar class="h-480px pr-20px">
      <NForm ref="formRef" :model="model" :rules="rules" label-placement="left" :label-width="100">
        <NGrid responsive="screen" item-responsive>
          <NFormItemGi span="24 m:24" label="手机号：" path="phone">
            <NInput v-model:value="model.phone" placeholder="请输入手机号" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="姓名：" path="name">
            <NInput v-model:value="model.name" placeholder="请输入昵称" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="头像：" path="url">
            <!-- v-model:file-list="fileList" -->
            <!-- :default-file-list="previewFileList" -->
            <NUpload
              :action="uploadUrl"
              v-model:value="model.avatarUrl"
              :default-file-list="previewFileList"
              list-type="image-card"
              :max=1
              @change="handleFileListUpdate"
            />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="性别：" path="sex">
            <NSelect
              v-model:value="model.sex"
              :options="sexOptions"
              placeholder="请选择"
            />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="状态：" path="enable">
            <NSelect
              v-model:value="model.enable"
              :options="enableOptions"
              placeholder="请选择"
            />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="密码：" path="password">
            <NInput v-model:value="model.password" placeholder="请输入密码" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="年龄：" path="age">
            <NInputNumber v-model:value="model.age" placeholder="请输入年龄" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="地址：" path="address">
            <NInput v-model:value="model.address" placeholder="请输入地址" />
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